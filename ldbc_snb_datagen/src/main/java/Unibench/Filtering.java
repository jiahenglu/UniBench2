package Unibench;

/**
 * Created by chzhang on 24/02/2017.
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Filtering {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("mapreduce.output.basename", "Filtering");
        Job job1 = Job.getInstance(conf, "Filtering");
        job1.setJarByClass(TableProcessing.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);
        job1.setMapperClass(TableProcessing.class);
        job1.setReducerClass(JoinReducer.class);
        TextInputFormat.addInputPath(job1, new Path(args[0]));
        TextOutputFormat.setOutputPath(job1, new Path(args[1]));

        job1.waitForCompletion(true);

        Configuration conf2 = new Configuration();
        conf2.set("mapreduce.output.basename", "Representatives");
        Job job2 = Job.getInstance(conf, "Representatives");
        job2.setMapperClass(Sort_Group.class);
        //Secondary Sort by DegreeCount
        job2.setMapOutputKeyClass(CompositeKey.class);
        job2.setPartitionerClass(ActualKeyPartitioner.class);
        job2.setGroupingComparatorClass(ActualKeyGroupingComparator.class);
        job2.setSortComparatorClass(CompositeKeyComparator.class);
        job2.setReducerClass(SelectRepre.class);
        TextInputFormat.addInputPath(job2, new Path(args[1]));
        // String timeStamp= new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss", Locale.UK).format(new Timestamp(System.currentTimeMillis()));
        TextOutputFormat.setOutputPath(job2, new Path(args[2]));

        job2.waitForCompletion(true);

    }

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            String line = value.toString();
            String SplitArray[] = line.split("\\|");
            context.write(new Text(SplitArray[0]), one);
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static class TableProcessing extends Mapper<Object, Text, Text, Text> {
        private String filename;
        private String PersonID, value1, tabletag1 = "1+";
        private String OrganizeID, value2, tabletag2 = "2+";

        // Use map function to process different records of different files -- add prefix to the value
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            filename = ((FileSplit) context.getInputSplit()).getPath().getName();
            if (filename.trim().equals("part-r-00000")) {
                String line = value.toString();
                String splitarray[] = line.split("\\s+");
                PersonID = (splitarray[0].trim());
                value1 = splitarray[1].trim();

                if (value1 != null) {
                    context.write(new Text(PersonID), new Text(tabletag1 + value1));
                }
            } else if (filename.trim().equals("p2o")) {
                String line = value.toString();
                String splitarray[] = line.split("\\t");
                PersonID = splitarray[0].trim();
                OrganizeID = splitarray[2].trim();
                if (OrganizeID != null) {
                    context.write(new Text(PersonID), new Text(tabletag2 + OrganizeID));
                }
            }
        }
    }

    // Use reduce function to do the join for records from the different files with same key
    public static class JoinReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException,
                InterruptedException {
            Iterator ite = values.iterator();
            List<String> DegreeCount = new ArrayList<String>();
            List<String> Organization = new ArrayList<String>();
            while (ite.hasNext()) {
                String record = ite.next().toString();
                char type = record.charAt(0);
                if (type == '1') {
                    DegreeCount.add(record.substring(2));
                } else if (type == '2') {
                    Organization.add(record.substring(2));
                }
            }
            if (DegreeCount.size() != 0 && Organization.size() != 0) {
                for (String str1 : DegreeCount) {
                    for (String str2 : Organization) {
                        StringBuilder strBuilder = new StringBuilder(key.toString()).append("\t").append(str1);
                        context.write(new Text(str2), new Text(strBuilder.toString()));
                    }
                }
            }
        }
    }

    public static class Sort_Group extends Mapper<LongWritable, Text, CompositeKey, Text> {
        private String filename;
        private String PersonID;
        private String OrganizeID;
        private Integer DegreeCount;

        // Use map function to process different records of different files -- add prefix to the value
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            filename = ((FileSplit) context.getInputSplit()).getPath().getName();
            if (filename.trim().equals("Filtering-r-00000")) {
                if (key.get() > 0) {
                    String line = value.toString();
                    String splitarray[] = line.split("\\t");
                    OrganizeID = splitarray[0].trim();
                    PersonID = splitarray[1].trim();
                    DegreeCount = Integer.parseInt(splitarray[2].trim());
                    context.write(new CompositeKey(OrganizeID, DegreeCount), new Text(PersonID));
                }

            }
        }
    }

    public static class SelectRepre extends Reducer<CompositeKey, Text, CompositeKey, Text> {
        public void reduce(CompositeKey key, Iterable<Text> values, Context context) throws IOException,
                InterruptedException {
            Iterator ite = values.iterator();
            while (ite.hasNext()) {
                context.write(key, new Text(ite.next().toString()));
                break;
            }

        }
    }
}