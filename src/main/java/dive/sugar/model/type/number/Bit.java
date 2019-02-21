package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.function.Consumer;

/**
 * @author dawn
 */
public class Bit extends Column {

    public Bit(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() {
        this.type = "BIT";
    }

    @Override
    protected boolean check(Integer length, String from) {
        boolean result = 1 <= length && length <= 64;
        if (result) {
            return true;
        }
        log.error("{}: the length of type {} must be 1 ~ 64: {}",
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
            Integer l = length;
            if (!exist(l)) {
                l = 1;
            }
            if (defaultValue.startsWith("b")) {
                if (l < defaultValue.length() - 1) {
                    log.error("{}: the length of type {} is {}, " +
                                    "but default value is {}, length is {}",
                            from, type, l, defaultValue,
                            defaultValue.length() - 1);
                    return false;
                }
            } else if (defaultValue.startsWith("b'")
                    && defaultValue.endsWith("'")) {
                if (l < defaultValue.length() - 3) {
                    log.error("{}: the length of type {} is {}, " +
                                    "but default value is {}, length is {}",
                            from, type, l, defaultValue,
                            defaultValue.length() - 3);
                    return false;
                }
            } else {
                BigInteger value = new BigInteger(defaultValue);
                if (l < value.bitLength()){
                    log.error("{}: the length of type {} is {}, " +
                                    "but default value is {}, length is {}",
                            from, type, l, defaultValue, value.bitLength());
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}: {} can not cast to number", from, defaultValue);
            return false;
        }
        return true;
    }

    @Override
    public String definition() {
        StringBuilder sb = new StringBuilder();

        sb.append("`").append(name).append("` ").append(type);

        if (exist(length)) {
            sb.append("(").append(length).append(")");
        }

        if (isTrue(notNull)) {
            sb.append(" NOT NULL");
        } else if (!isTrue(primary) && isTrue(nullable)) {
            sb.append(" NULL");
        }

        if (exist(defaultValue)) {
            sb.append(" DEFAULT ");
            if (defaultValue.startsWith("b") && !defaultValue.startsWith("b'")) {
                sb.append("b'").append(defaultValue.substring(1)).append("'");
            } else {
                sb.append(defaultValue);
            }
        }

        if (isTrue(primary) && isTrue(autoIncrement))
            sb.append(" AUTO_INCREMENT");

        if (isTrue(primary)) sb.append(" PRIMARY KEY");

        if (exist(comment)) sb.append(" COMMENT '").append(comment).append( "'");

        return sb.toString();
    }

    @Override
    public String value(Object value) {
        if (!exist(value)) {
            return null;
        }
        if (value instanceof String) {
            return "b'" + value + "'";
        }
        return null;
    }

    @Override
    public boolean same(Column s) {
        if (!exist(s)) return false;
        if (!name.equals(s.name)) return false;
        if (!type.equals(s.type)) return false;

        Integer length = this.length;
        if (!exist(length)) {
            length = 20;
        }
        if (!length.equals(s.length)) {
            return false;
        }

        if (isTrue(notNull) && !isTrue(s.notNull)) {
            return false;
        } else if (!isTrue(notNull) && isTrue(s.notNull)) {
            return false;
        }

        if (useful(defaultValue) && !defaultValue.equals(s.defaultValue)) {
            return false;
        } else if (!useful(defaultValue) && useful(defaultValue)) {
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
