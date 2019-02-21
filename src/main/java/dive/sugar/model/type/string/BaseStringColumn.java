package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * @author dawn
 */
public abstract class BaseStringColumn extends Column {

    static int lengthMax;

    BaseStringColumn(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean check(Integer length, String from) {
        if (!exist(length)) {
            return true;
        }
        boolean result = 0 <= length && length <= lengthMax;
        if (result) {
            return true;
        }
        log.error("{}: the length of type {} must in 0 ~ {}: {}",
                from, type, lengthMax, length);
        return false;
    }

    @Override
    protected boolean check(String defaultValue, String from) {
        if (!exist(defaultValue)) {
            return true;
        }
        int l = defaultValue.length();
        if (length < l) {
            log.error("{}: the length of type {} is {}, " +
                            "but default value({})'s length is {}",
                    from, type, length, defaultValue, l);
            return false;
        }
        return true;
    }

    @Override
    protected boolean initDecimals() { return true; }

    @Override
    protected boolean check(Integer decimal, String from, Integer precision,
                            Integer scale, Consumer<Integer> consumer) {
        return false;
    }

    @Override
    protected boolean initFSP() { return true; }

    @Override
    protected void initUnsigned() { }

    @Override
    protected void initZerofill() { }

    @Override
    protected void initAutoIncrement() { }

    @Override
    public String value(Object value) {
        if (!exist(value)) {
            return null;
        }
        return "'" + value.toString() + "'";
    }
}
