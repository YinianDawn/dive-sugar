package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.annotate.type.BOOLEAN;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class TinyInt extends BaseIntegerColumn {

    // `id` tinyint(4) DEFAULT NULL
    // `id` tinyint(1) DEFAULT NULL boolean

    private boolean bool = false;

    public TinyInt(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected long unsignedMin() { return 0; }

    @Override
    protected long unsignedMax() { return 255L; }

    @Override
    protected long signedMin() { return -128L; }

    @Override
    protected long signedMax() { return 127L; }

    @Override
    protected int defaultLength() { return 4; }

    @Override
    protected void init() {
        boolean exist = field.isAnnotationPresent(BOOLEAN.class);
        Class clazz = field.getType();
        if (exist || clazz == boolean.class || clazz == Boolean.class) {
            bool = true;
        }

        super.init();
    }

    @Override
    protected boolean check(Integer length, String from) {
        if (bool) {
            if (exist(length) && 1 != length) {
                log.error("the length of type {} by boolean must be 1: {}",
                        type, length);
                return false;
            }
            if (!exist(length)) {
                this.length = 1;
            }
            return true;
        }
        return super.check(length, from);
    }

    @Override
    protected boolean check(String defaultValue, String from) {
        if (!exist(defaultValue)) {
            return true;
        }

        if (bool) {
            if ("true".equalsIgnoreCase(defaultValue)
                    || "false".equalsIgnoreCase(defaultValue)
                    || "1".equals(defaultValue)
                    || "0".equals(defaultValue)) {
                return true;
            }
            log.error("{}: the value of type {} by boolean is wrong: {}",
                    from, type, defaultValue);
            return false;
        }

        return super.check(defaultValue, from);
    }

}
