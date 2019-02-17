package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.function.Consumer;

/**
 * 基本日期列
 *
 * @author dawn
 */
abstract class BaseDateColumn extends BaseColumn {

    static SimpleDateFormat SDF;
    static java.util.Date min;
    static java.util.Date max;

    BaseDateColumn(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected boolean initLength() { return true; }

    @Override
    protected boolean check(Integer length, String from) { return false; }

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
        try {
            java.util.Date date = SDF.parse(defaultValue);
            if (exist(min) && exist(max)) {
                boolean result = min.getTime() < date.getTime()
                        && date.getTime() < max.getTime();
                if (result) {
                    return true;
                }
                log.error("{}: {} is not a valid date", from, defaultValue);
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
    protected void initAutoIncrement() { }

    @Override
    public String definition() {
        StringBuilder sb = new StringBuilder();

        sb.append("`").append(name).append("` ").append(type);

        if (isTrue(notNull)) {
            sb.append(" NOT NULL");
        } else if (!isTrue(primary) && isTrue(nullable)) {
            sb.append(" NULL");
        }

        if (useful(defaultValue)) {
            sb.append(" DEFAULT '").append(defaultValue).append("'");
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
    public String value(Object value) {
        if (!exist(value)) {
            return null;
        }
        if (value instanceof java.util.Date) {
            return SDF.format((java.util.Date) value);
        }
        return null;
    }

    @Override
    public boolean same(BaseColumn s) {
        if (!exist(s)) return false;
        if (!name.equals(s.name)) return false;
        if (!type.equals(s.type)) return false;

        if (isTrue(notNull) && !isTrue(s.notNull)) return false;
        else if (!isTrue(notNull) && isTrue(s.notNull)) return false;

        if (exist(defaultValue) && !defaultValue.equals(s.defaultValue))
            return false;
        else if (!exist(defaultValue) && exist(s.defaultValue)) return false;

        if (isTrue(primary) && !isTrue(s.primary)) return false;
        else if (!isTrue(primary) && isTrue(s.primary)) return false;

        if (exist(comment) && !comment.equals(s.comment)) return false;
        else if (!exist(collate) && exist(s.comment)) return false;

        return place.equals(s.place);
    }
}
