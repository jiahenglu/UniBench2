package Unibench;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class CompositeKey implements Writable,
        WritableComparable<CompositeKey> {

    private String Personid;
    private Integer WorkFrom;

    public CompositeKey() {
    }

    public CompositeKey(String Personid, Integer WorkFrom) {
        this.Personid = Personid;
        this.WorkFrom = WorkFrom;
    }

    @Override
    public String toString() {
        return (new StringBuilder().append(Personid).append("\t")
                .append(WorkFrom)).toString();
    }

    public void readFields(DataInput dataInput) throws IOException {
        Personid = WritableUtils.readString(dataInput);
        WorkFrom = WritableUtils.readVInt(dataInput);
    }

    public void write(DataOutput dataOutput) throws IOException {
        WritableUtils.writeString(dataOutput, Personid);
        WritableUtils.writeVInt(dataOutput, WorkFrom);
    }

    public int compareTo(CompositeKey objKeyPair) {
        int result = Personid.compareTo(objKeyPair.Personid);
        if (0 == result) {
            result = WorkFrom.compareTo(objKeyPair.WorkFrom);
        }
        return result;
    }

    public String getPersonid() {
        return Personid;
    }

    public void setPersonid(String Personid) {
        this.Personid = Personid;
    }

    public Integer getWorkFrom() {
        return WorkFrom;
    }

    public void setWorkFrom(Integer WorkFrom) {
        this.WorkFrom = WorkFrom;
    }
}