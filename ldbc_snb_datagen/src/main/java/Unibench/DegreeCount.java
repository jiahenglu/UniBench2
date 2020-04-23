package Unibench;

/**
 * Created by chzhang on 24/02/2017.
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.Iterator;

public class DegreeCount {


    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Degree count");
        job.getConfiguration().set("mapreduce.output.basename", "person");
        job.setJarByClass(TableProcessing.class);
        job.setMapperClass(TableProcessing.class);
        // job.setCombinerClass(IntSumReducer.class);

        //Secondary Sort by Workyear
        job.setMapOutputKeyClass(CompositeKey.class);
        job.setPartitionerClass(ActualKeyPartitioner.class);
        job.setGroupingComparatorClass(ActualKeyGroupingComparator.class);
        job.setSortComparatorClass(CompositeKeyComparator.class);

        job.setReducerClass(RemoveReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

    public static class TableProcessing extends Mapper<LongWritable, Text, CompositeKey, Text> {
        private String filename;
        private String PersonID;
        private String OrganizeID;
        private String WorkFrom;

        // Use map function to process different records of different files -- add prefix to the value
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            filename = ((FileSplit) context.getInputSplit()).getPath().getName();
            if (filename.trim().equals("person_workAt_organisation_0_0.csv")) {
                if (key.get() > 0) {
                    String line = value.toString();
                    String splitarray[] = line.split("\\|");
                    PersonID = splitarray[0].trim();
                    OrganizeID = splitarray[1].trim();
                    WorkFrom = splitarray[2].trim();
                    // context.write(new CompositeKey(PersonID.toString(),WorkFrom.toString()), new Text(OrganizeID));
                }

            }
        }
    }

    public static class RemoveReducer extends Reducer<CompositeKey, Text, CompositeKey, Text> {
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