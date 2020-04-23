/*
package Unibench;

import java.io.IOException;
import java.util.HashMap;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerialization {
public static class Map extends Mapper<Object, Text, Text, NullWritable> {
    private Text text = new Text();
    ObjectMapper mapper = new ObjectMapper();
    HashMap<String,String> products= new HashMap<String,String>();
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String splitarray[] = line.split("\\|");
        String Name=splitarray[1];
        String authorName=splitarray[2];
        String abstracts=splitarray[3];
        String isbn=splitarray[4];
        products.put("Name", Name);
        products.put("Category", "book");
        products.put("AuthorName", authorName);
        products.put("Abstract", abstracts);
        products.put("ISBN", isbn); 
        String product = mapper.writeValueAsString(products);
        text.set(product);
        context.write(text, NullWritable.get());
    }
}

public static class Reduce extends Reducer<Text, NullWritable, Text, NullWritable> {
    public void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        context.write(key, NullWritable.get());
    }
}

public static void main(String[] args) throws Exception {
    Configuration configuration = new Configuration();
    Job job = new Job(configuration, "JsonSerialization");
    job.setJarByClass(JsonSerialization.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setNumReduceTasks(1);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(NullWritable.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
}

}*/
