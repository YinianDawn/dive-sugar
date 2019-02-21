package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.annotate.type.SET;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author dawn
 */
public class Set extends BaseSetColumn {

    public Set(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean initValues() {
        // from SET
        SET s = field.getAnnotation(SET.class);
        if (exist(s)) {
            values = s.value();
        }

        if ((!exist(values) || 0 == values.length)
                && exist(model)
                && exist(model.values)
                && 0 < model.values.length) {
            values = model.values;
        }

        if (!exist(values) || 0 == values.length) {
            log.error("the values of type SET can not be {}",
                    exist(values) ? 0 : "null");
            return false;
        }

        return true;
    }

    @Override
    protected boolean check(String defaultValue, String from) {
        if (!useful(defaultValue)) {
            return true;
        }
        String[] vs;
        if (defaultValue.startsWith("'") && defaultValue.endsWith("'")) {
            defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
            vs = defaultValue.split(",");
        } else {
            vs = defaultValue.split(",");
        }
        for (String s : vs) {
            boolean result = false;
            for (String v : values) {
                if (v.equals(s)) {
                    result = true;
                }
            }
            if (!result) {
                log.error("default value {} must in {}",
                        defaultValue, Arrays.toString(values));
                return false;
            }
        }
        return true;
    }

    @Override
    public String value(Object value) {
        if (!exist(value)) {
            return null;
        }
        if (value instanceof java.util.Set) {
            java.util.Set set = (java.util.Set) value;
            StringBuilder sb = new StringBuilder();
            if (0 < set.size()) {
                for (Object o : set) {
                    sb.append(o.toString()).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                return "'" + sb.toString() + "'";
            } else {
                log.error("set is empty");
            }
        }
        return super.value(value);
    }
}
