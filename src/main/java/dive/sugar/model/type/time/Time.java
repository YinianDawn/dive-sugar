package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间类型
 *
 * @author dawn
 */
public class Time extends BaseTimeColumn {

    // `id` time DEFAULT NULL

    private Date max;

    {
        try {
            max = new SimpleDateFormat(sdf_() + ".SSSSSS")
                    .parse("59:59.999999");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    String sdf_() {
        return "mm:ss";
    }

    @Override
    Date min() {
        return null;
    }

    @Override
    Date max() {
        return max;
    }

    public Time(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean initOnUpdate() { onUpdate = null; return true; }

    @Override
    protected boolean check(String defaultValue, String from) {
        if (!exist(defaultValue)) {
            return true;
        }
        String scale = "";
        if (defaultValue.contains(".")) {
            scale = defaultValue.substring(defaultValue.lastIndexOf(".") + 1);
        }
        Integer f = fsp;
        if (!exist(f)) {
            f = 0;
        }
        if (f < scale.length()) {
            log.error("{}: fsp is error: fsp:{}, default value:{}",
                    from, f, defaultValue);
            return false;
        }
        f = scale.length();
        StringBuilder sb = new StringBuilder(sdf_());
        if (0 < f) {
            sb.append(".");
            while (f-->0) sb.append("S");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
        try {
            String hour = defaultValue.split(":")[0];
            long time = Long.parseLong(hour);
            boolean result = -838 <= time && time <= 838;
            if (result) {
                if (time == -838 || time == 838) {
                    max = new SimpleDateFormat(sdf_()).parse("59:59");
                }
                Date date = sdf.parse(defaultValue
                        .substring(defaultValue.indexOf(":") +1));
                if (exist(max)) {
                    result = date.getTime() <= max.getTime();
                    if (result) {
                        return true;
                    }
                } else {
                    log.error("{}: init max error: null", from);
                }
            }
            log.error("{}: {} is not a valid date", from, defaultValue);
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("{}: {} can not cast to date", from, defaultValue);
        }
        return false;
    }

    @Override
    public String value(Object value) {
        return null;
    }
}
