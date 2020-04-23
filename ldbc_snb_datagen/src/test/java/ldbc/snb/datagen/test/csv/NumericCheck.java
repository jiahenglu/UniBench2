package ldbc.snb.datagen.test.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aprat on 23/12/15.
 */
public abstract class NumericCheck<T extends Number> extends Check {

    protected NumericCheckType type;
    protected T val1;
    protected T val2;
    protected Parser<T> parser;
    public NumericCheck(Parser<T> parser, String name, Integer column, NumericCheckType type, T val1, T val2) {
        super(name, new ArrayList<Integer>());
        this.getColumns().add(column);
        this.type = type;
        this.val1 = val1;
        this.val2 = val2;
        this.parser = parser;
    }

    @Override
    public boolean check(List<String> values) {
        switch (type) {
            case G:
                return greater(parser.parse(values.get(0)), val1);
            case GE:
                return greaterEqual(parser.parse(values.get(0)), val1);
            case L:
                return less(parser.parse(values.get(0)), val1);
            case LE:
                return lessEqual(parser.parse(values.get(0)), val1);
            case E:
                return equals(parser.parse(values.get(0)), val1);
            case NE:
                return nonEquals(parser.parse(values.get(0)), val1);
            case BETWEEN:
                return between(parser.parse(values.get(0)), val1, val2);
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

    public abstract boolean between(T val1, T val2, T val3);

    public enum NumericCheckType {
        G,
        GE,
        L,
        LE,
        E,
        NE,
        BETWEEN
    }

}
