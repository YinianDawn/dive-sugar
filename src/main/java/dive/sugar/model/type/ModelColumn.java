package dive.sugar.model.type;

import dive.sugar.model.Column;

import java.util.function.Consumer;

import static sun.management.Agent.error;

/**
 * 样例列
 *
 * @author dawn
 */
public class ModelColumn extends Column {

    public ModelColumn() {}



    @Override
    protected boolean check(Integer length, String from) {
        return false;
    }

    @Override
    protected boolean check(Integer decimal, String from, Integer precision, Integer scale, Consumer<Integer> consumer) {
        return false;
    }

    @Override
    protected boolean check(String defaultValue, String from) {
        return false;
    }

    @Override
    public String definition() {
        return null;
    }

    @Override
    public String value(Object value) {
        return null;
    }

    @Override
    public boolean same(Column s) {
        return false;
    }
}
