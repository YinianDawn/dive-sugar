package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间类型
 *
 * @author dawn
 */
public class DateTime extends BaseTimeColumn {

    // `id` datetime DEFAULT NULL

    private Date min;
    private Date max;

    {
        try {
            min = new SimpleDateFormat(sdf_()).parse("1000-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            max = new SimpleDateFormat(sdf_()).parse("9999-12-31 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    Date min() {
        return min;
    }

    @Override
    Date max() {
        return max;
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
            return new SimpleDateFormat(sdf_()).format((java.util.Date) value);
        }
        return null;
    }
}
