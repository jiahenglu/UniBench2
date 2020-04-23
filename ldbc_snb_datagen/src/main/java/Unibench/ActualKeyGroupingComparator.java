package Unibench;


import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class ActualKeyGroupingComparator extends WritableComparator {

    protected ActualKeyGroupingComparator() {

        super(CompositeKey.class, true);
    }

    public int compare(WritableComparable w1, WritableComparable w2) {

        CompositeKey key1 = (CompositeKey) w1;
        CompositeKey key2 = (CompositeKey) w2;

        return key1.getPersonid().compareTo(key2.getPersonid());
    }
}
