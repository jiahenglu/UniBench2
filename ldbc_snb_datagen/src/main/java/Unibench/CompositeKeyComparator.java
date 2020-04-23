package Unibench;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class CompositeKeyComparator extends WritableComparator {
    protected CompositeKeyComparator() {
        super(CompositeKey.class, true);
    }

    public int compare(WritableComparable w1, WritableComparable w2) {

        CompositeKey key1 = (CompositeKey) w1;
        CompositeKey key2 = (CompositeKey) w2;

// (first check on udid)
        int compare = key1.getPersonid().compareTo(key2.getPersonid());

        if (compare == 0) {
// only if we are in the same input group should we try and sort by value (datetime)
            return -key1.getWorkFrom().compareTo(key2.getWorkFrom());
        }

        return compare;
    }
}
