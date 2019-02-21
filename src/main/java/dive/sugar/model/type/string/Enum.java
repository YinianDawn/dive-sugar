package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.annotate.type.ENUM;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author dawn
 */
public class Enum extends BaseSetColumn {
    
    public Enum(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean initValues() {
        // from Enum
        ENUM e = field.getAnnotation(ENUM.class);
        if (exist(e)) {
            values = e.value();
        }
        
        if (!exist(values) || 0 == values.length) {
            Class c = field.getType();
            if (c.isEnum()) {
                Object[] objects = c.getEnumConstants();
                values = new String[objects.length];
                for (int i = 0; i < objects.length; i++) {
                    values[i] = objects[i].toString();
                }
            }
        }

        if ((!exist(values) || 0 == values.length)
                && exist(model)
                && exist(model.values)
                && 0 < model.values.length) {
            values = model.values;
        }

        if (!exist(values) || 0 == values.length) {
            log.error("the values of type ENUM can not be {}",
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
        String value;
        if (defaultValue.startsWith("'") && defaultValue.endsWith("'")) {
            value = defaultValue.substring(1, defaultValue.length() - 1);
        } else {
            value = defaultValue;
        }
        for (String v : values) {
            if (v.equals(value)) {
                return true;
            }
        }
        log.error("default value {} must in {}",
                defaultValue, Arrays.toString(values));
        return false;
    }
    
}
