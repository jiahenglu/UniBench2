package ldbc.snb.datagen.test.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aprat on 30/03/16.
 */
public abstract class NumericPairCheck<T> extends Check {

    protected NumericCheckType type;
    protected Parser<T> parser;
    public NumericPairCheck(Parser<T> parser, String name, Integer columnA, Integer columnB, NumericCheckType type) {
        super(name, new ArrayList<Integer>());
        this.getColumns().add(columnA);
        this.getColumns().add(columnB);
        this.type = type;
        this.parser = parser;
    }

    @Override
    public boolean check(List<String> values) {
        switch (type) {
            case G:
                return greater(parser.parse(values.get(0)), parser.parse(values.get(1)));
            case GE:
                return greaterEqual(parser.parse(values.get(0)), parser.parse(values.get(1)));
            case L:
                return less(parser.parse(values.get(0)), parser.parse(values.get(1)));
            case LE:
                return lessEqual(parser.parse(values.get(0)), parser.parse(values.get(1)));
            case E:
                return equals(parser.parse(values.get(0)), parser.parse(values.get(1)));
            case NE:
                return nonEquals(parser.parse(values.get(0)), parser.parse(values.get(1)));
            default:
                return false;
        }
    }

    public abstract boolean greater(T val1, T val2);

    public abstract boolean greaterEqual(T val1, T val2);

    public abstract boolean less(T val1, T val2);

    public abstract boolean lessEqual(T val1, T val2);

    public abstract boolean equals(T val1, T val2);

    public abstract boolean nonEquals(T val1, T val2);

    public enum NumericCheckType {
        G,
        GE,
        L,
        LE,
        E,
        NE
    }

}
