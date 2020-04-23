package Unibench;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;

public class ActualKeyPartitioner extends Partitioner<CompositeKey, Text> {

    HashPartitioner<Text, Text> hashPartitioner = new HashPartitioner<Text, Text>();
    Text newKey = new Text();

    @Override
    public int getPartition(CompositeKey key, Text value, int numReduceTasks) {

        try {
// Execute the default partitioner over the first part of the key
            newKey.set(key.getPersonid());
            return hashPartitioner.getPartition(newKey, value, numReduceTasks);
        } catch (Exception e) {
            e.printStackTrace();
            return (int) (Math.random() * numReduceTasks); // this would return a random value in the range
// [0,numReduceTasks)
        }
    }
}
