package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * 整型类型
 *
 * @author dawn
 */
public abstract class BaseIntegerColumn extends Column {

    long unsignedMin;
    long unsignedMax;
    long signedMin;
    long signedMax;
    int defaultLength;

    BaseIntegerColumn(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean check(Integer length, String from) {
        if (!exist(length)) {
            return true;
        }
        boolean result = 1 <= length && length <= 255;
        if (result) {
            return true;
        }
        log.error("{}: the length of type {} must be 1 ~ 255: {}",
                from, type, length);
        return false;
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
            Long value = Long.parseLong(defaultValue);
            if (exist(unsigned) && unsigned) {
                boolean result = unsignedMin <= value && value < unsignedMax;
                if (result) {
                    return true;
                }
                log.error("{}: the default value of type unsigned {} " +
                                "must be {} ~ {}: {}",
                        from, type, unsignedMin, unsignedMax, defaultValue);
            } else {
                boolean result = signedMin <= value && value <= signedMax;
                if (result) {
                    return true;
                }
                log.error("{}: the default value of type {} " +
                                "must be {} ~ {}: {}",
                        from, type, signedMin, signedMax, defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}: {} can not cast to number", from, defaultValue);
        }
        return false;
    }

    @Override
    public String definition() {
        StringBuilder sb = new StringBuilder();

        sb.append("`").append(name).append("` ").append(type);

        if (exist(length)) {
            sb.append("(").append(length).append(")");
        }

        if (isTrue(unsigned)) {
            sb.append(" UNSIGNED");
        }
        if (isTrue(zerofill)) {
            sb.append(" ZEROFILL");
        }

        if (isTrue(notNull)) {
            sb.append(" NOT NULL");
        } else if (!isTrue(primary) && isTrue(nullable)) {
            sb.append(" NULL");
        }

        if (exist(defaultValue)) {
            sb.append(" DEFAULT ").append(defaultValue);
        }

        if (isTrue(primary) && isTrue(autoIncrement)) {
            sb.append(" AUTO_INCREMENT");
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
        return value.toString();
    }

    @Override
    public boolean same(Column s) {
        if (!exist(s)
                || !name.equals(s.name)
                || !type.equals(s.type)) {
            return false;
        }

        Integer length = this.length;
        if (!exist(length)) {
            length = defaultLength;
        }
        if (!length.equals(s.length)) {
            return false;
        }

        if (isTrue(unsigned) && !isTrue(s.unsigned)) {
            return false;
        } else if (!isTrue(unsigned) && isTrue(s.unsigned)) {
            return false;
        }
        if (isTrue(zerofill) && !isTrue(s.zerofill)) {
            return false;
        } else if (!isTrue(zerofill) && isTrue(s.zerofill)) {
            return false;
        }

        if (isTrue(notNull) && !isTrue(s.notNull)) {
            return false;
        } else if (!isTrue(notNull) && isTrue(s.notNull)) {
            return false;
        }

        if (useful(defaultValue) && !defaultValue.equals(s.defaultValue)) {
            return false;
        } else if (!useful(defaultValue) && useful(s.defaultValue)) {
            return false;
        }

        if (isTrue(autoIncrement) && !isTrue(s.autoIncrement)) {
            return false;
        } else if (!isTrue(autoIncrement) && isTrue(s.autoIncrement)) {
            return false;
        }

        if (isTrue(primary) && !isTrue(s.primary)) {
            return false;
        } else if (!isTrue(primary) && isTrue(s.primary)) {
            return false;
        }

        if (exist(comment) && !comment.equals(s.comment)) {
            return false;
        } else if (!exist(collate) && exist(s.comment)) {
            return false;
        }

        return place.equals(s.place);
    }

}
