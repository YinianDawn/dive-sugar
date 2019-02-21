package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期时间类型
 *
 * @author dawn
 */
public class DateTime extends BaseTimeColumn {

    // `id` datetime DEFAULT NULL

    static {
        try {
            min = new SimpleDateFormat(SDF).parse("1000-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            max = new SimpleDateFormat(SDF).parse("9999-12-31 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public DateTime(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean check(String defaultValue, String from) {
        if ("CURRENT_TIMESTAMP".equals(defaultValue)) {
            return true;
        }
        return super.check(defaultValue, from);
    }

    @Override
    public String value(Object value) {
        if (!exist(value)) {
            return null;
        }
        if (value instanceof java.util.Date) {
            return new SimpleDateFormat(SDF).format((java.util.Date) value);
        }
        return null;
    }
}
