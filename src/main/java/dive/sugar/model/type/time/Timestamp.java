package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间戳类型
 *
 * @author dawn
 */
public class Timestamp extends BaseTimeColumn {

    // `id` datetime DEFAULT NULL

    private Date min;
    private Date max;

    {
        try {
            min = new SimpleDateFormat(sdf_()).parse("1970-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            max = new SimpleDateFormat(sdf_()).parse("2038-01-19 03:14:08");
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

    public Timestamp(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }


    @Override
    protected boolean check(String defaultValue, String from) {
        if ("0000-00-00 00:00:00".equals(defaultValue)) {
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
