package ldbc.snb.datagen.test.csv;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aprat on 18/12/15.
 */
public class StringLengthCheck extends Check {

    private int length;


    public StringLengthCheck(int column, int length) {
        super("String length check of " + length + " unicode characters", (new ArrayList<Integer>()));
        this.length = length;
        this.getColumns().add(column);
    }

    @Override
    public boolean check(List<String> vals) {
        for (String value : vals) {
            if (value.length() > length) return false;
        }
        return true;
    }
}
