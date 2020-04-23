package ldbc.snb.datagen.test.csv;

import java.util.List;

/**
 * Created by aprat on 18/12/15.
 */
public abstract class Check {

    protected String checkName = null;
    protected List<Integer> columns = null;

    public Check(String name, List<Integer> columns) {
        this.checkName = name;
        this.columns = columns;
    }

    public String getCheckName() {
        return checkName;
    }

    public List<Integer> getColumns() {
        return columns;
    }

    public abstract boolean check(List<String> values);
}

