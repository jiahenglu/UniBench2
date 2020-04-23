package Unibench;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class InitializeCustomer {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        conf.set("mapreduce.output.basename", "InitializeCustomer");
        Job job1 = Job.getInstance(conf, "InitializeCustomer");
        job1.setJarByClass(TableProcessing.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);
        job1.setMapperClass(TableProcessing.class);
        job1.setReducerClass(JoinReducer.class);
        FileInputFormat.addInputPath(job1, new Path(args[0]));
        FileOutputFormat.setOutputPath(job1, new Path(args[1]));

        job1.waitForCompletion(true);

    }

    public static class TableProcessing extends Mapper<Object, Text, Text, Text> {
        private String filename;
        private String PersonID, Tag, tabletag1 = "1+";
        private String CustomerID, information, tabletag2 = "2+";


        protected void setup(Context context) throws IOException, InterruptedException {
            MetaDataParcer.initialize(new File("input/tagList.txt"));
        }


        // Use map function to process different records of different files -- add prefix to the value
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            filename = ((FileSplit) context.getInputSplit()).getPath().getName();
            if (filename.trim().equals("person_hasInterest_tag_0_0.csv")) {
                String line = value.toString();
                String splitarray[] = line.split("\\|", 2);
                PersonID = (splitarray[0].trim());
                Tag = splitarray[1].trim();

                if (MetaDataParcer.tagList.containsKey(Tag) && MetaDataParcer.tagList.get(Tag) != true) {
                    MetaDataParcer.tagList.put(Tag, true);
                    context.write(new Text(PersonID), new Text(tabletag1));
                }
            } else if (filename.trim().equals("person_0_0.csv")) {
                String line = value.toString();
                String splitarray[] = line.split("\\|", 2);
                CustomerID = splitarray[0].trim();
                information = splitarray[1].trim();
                context.write(new Text(CustomerID), new Text(tabletag2 + information));
            }
        }
    }

    // Use reduce function to do the join for records from the different files with same key
    public static class JoinReducer extends Reducer<Text, Text, Text, Text> {
        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException,
                InterruptedException {
            Iterator ite = values.iterator();
            boolean flag = false;
            String information = null;
            while (ite.hasNext()) {
                String record = ite.next().toString();
                char type = record.charAt(0);
                if (type == '1') {
                    flag = true;
                }
                if (type == '2') {
                    information = record.substring(2);
                }
            }
            if (flag && information != null) {
                context.write(key, new Text(information));
            }

        }
    }
}