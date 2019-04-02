package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.annotate.prop.ON_UPDATE;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

/**
 * 基本时间列
 *
 * @author dawn
 */
public abstract class BaseTimeColumn extends Column {

    static String SDF = "yyyy-MM-dd HH:mm:ss";
    static java.util.Date min;
    static java.util.Date max;

    BaseTimeColumn(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean initLength() { return true; }

    @Override
    protected boolean initDecimals() { return true; }

    @Override
    protected boolean check(Integer decimal, String from, Integer precision,
                            Integer scale, Consumer<Integer> consumer) {
        return false;
    }

    @Override
    protected boolean check(Integer fsp, String from) {
        if (!exist(fsp)) {
            return true;
        }
        boolean result = 0 <= fsp && fsp <= 6;
        if (result) {
            return true;
        }
        log.error("{}: the fsp of type {} must be 0 ~ 6: {}",
                from, type, fsp);
        return false;
    }

    @Override
    protected void initUnsigned() { }

    @Override
    protected void initZerofill() { }

    @Override
    protected void initBinary() { }

    @Override
    protected boolean initCharset() { return true; }

    @Override
    protected boolean initCollate() { return true; }

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
        StringBuilder sb = new StringBuilder(SDF);
        if (0 < f) {
            sb.append(".");
            while (f-->0) sb.append("S");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(sb.toString());
        try {
            Date date = sdf.parse(defaultValue);
            if (exist(min) && exist(max)) {
                boolean result = min.getTime() < date.getTime()
                        && date.getTime() < max.getTime();
                if (result) {
                    return true;
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat(SDF);
                log.error("{}: {} is not a valid date, min:{} ~ max:{}",
                        from, defaultValue, sdf2.format(min), sdf2.format(max));
            } else {
                log.error("{}: init min and max error: {}, {}",
                        from, min, max);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            log.error("{}: {} can not cast to date", from, defaultValue);
        }
        return false;
    }

    @Override
    protected boolean initOnUpdate() {
        // from ON_UPDATE
        ON_UPDATE onUpdateAnnotate =
                field.getAnnotation(ON_UPDATE.class);
        if (exist(onUpdateAnnotate)) {
            this.onUpdate = onUpdateAnnotate.value();
            if (!useful(this.onUpdate)) {
                log.error("onUpdate can not be {}", onUpdate);
                return false;
            }
        }

        // from type
        if (exist(this.model) && useful(this.model.onUpdate)) {
            this.onUpdate = this.model.onUpdate;
        }
        return true;
    }

    @Override
    public String definition() {
        StringBuilder sb = new StringBuilder();

        sb.append("`").append(name).append("` ").append(type);

        if (exist(fsp)) {
            sb.append("(").append(fsp).append(")");
        }

        if (isTrue(notNull)) {
            sb.append(" NOT NULL");
        } else if (!isTrue(primary) && isTrue(nullable)) {
            sb.append(" NULL");
        }

        if (useful(defaultValue)) {
            if (defaultValue.startsWith("'")
                || defaultValue.equalsIgnoreCase("CURRENT_TIMESTAMP")
                || defaultValue.equalsIgnoreCase("NULL")) {
                sb.append(" DEFAULT ").append(defaultValue);
            } else {
                sb.append(" DEFAULT '").append(defaultValue).append("'");
            }
        }

        if (useful(onUpdate)) {
            sb.append(" ON UPDATE ").append(onUpdate);
        }

        if (isTrue(primary)) {
            sb.append(" PRIMARY KEY");
        }

        if (exist(comment)) {
            sb.append(" COMMENT '").append(comment).append( "'");
        }

        return sb.toString();
    }

    @Override
    public boolean same(Column s) {
        if (!exist(s)
                || !name.equals(s.name)
                || !type.equals(s.type)) {
            return false;
        }

        if (exist(fsp)) {
            if (exist(s.fsp)) {
                if (!fsp.equals(s.fsp)) {
                    return false;
                }
            } return false;
        } else {
            if (exist(s.fsp) && 0 != s.fsp) {
                return false;
            }
        }

        if (isTrue(notNull) && !isTrue(s.notNull)) {
            return false;
        } else if (!isTrue(notNull) && isTrue(s.notNull)) {
            return false;
        }

        if (exist(defaultValue) && !defaultValue.equals(s.defaultValue)) {
            return false;
        } else if (!exist(defaultValue) && exist(s.defaultValue)) {
            return false;
        }

        if (isTrue(primary) && !isTrue(s.primary)) {
            return false;
        } else if (!isTrue(primary) && isTrue(s.primary)) {
            return false;
        }

        if (exist(comment) && !comment.equals(s.comment)) {
            return false;
        } else if (!exist(comment) && exist(s.comment)) {
            return false;
        }

        return place.equals(s.place);
    }
}
