package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * 小数类型
 *
 * @author dawn
 */
public abstract class BaseDecimalColumn extends Column {

    BaseDecimalColumn(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    /**
     * 最大长度
     * @return 最大长度
     */
    protected abstract int lengthMax();

    /**
     * 最大精度
     * @return 最大精度
     */
    protected abstract int decimalMax();

    @Override
    protected boolean check(Integer length, String from) {
        if (!exist(length)) {
            return true;
        }
        boolean result = 1 <= length && length <= lengthMax();
        if (result) {
            return true;
        }
        log.error("{}: the length of type {} must be 1 ~ {}: {}",
                from, type, lengthMax(), length);
        return false;
    }

    @Override
    protected boolean check(Integer decimals, String from, Integer precision,
                            Integer scale, Consumer<Integer> consumer) {
        if (!exist(decimals) && !exist(from)) {
            return true;
        }
        switch (from) {
            case "Model":
            case "DECIMALS":
                boolean result = 0 <= decimals && decimals <= decimalMax();
                if (result) {
                    break;
                }
                log.error("{}: the length of type {} must be 0 ~ {}: {}",
                        from, type, decimalMax(), decimals);
                return false;
            case "Column":
                boolean rp = 0 <= precision && precision <= decimalMax();
                boolean rs = 0 <= scale && scale <= decimalMax();
                if (!rp && !rs) {
                    log.error("Column: the length of type {} " +
                                    "must be 0 ~ {}: precision:{}, scale:{}",
                            type, precision, decimalMax(), scale);
                    return false;
                }
                if (rp) {
                    decimals = precision;
                    consumer.accept(precision);
                } else {
                    decimals = scale;
                    consumer.accept(scale);
                }
                break;
            default:
                log.error("can not recognise the source of decimals: {}",
                        from);
                return false;
        }
        Integer l = length;
        if (!exist(l)) {
            l = 10;
        }
        if (l <= decimals) {
            log.error("{}： the length of type {} must greater than " +
                    "decimals: length->{}, decimals->{}", from, type, l, decimals);
            return false;
        }
        return true;
    }

    @Override
    protected boolean check(String defaultValue, String from) {
        if (!exist(defaultValue)) {
            return true;
        }
        try {
            if (exist(length)) {
                String l = defaultValue.replace("-", "")
                        .replace(".", "");
                if (length < l.length()) {
                    log.error("{}: the default value {} is too long," +
                            " the length is {}", from, defaultValue, length);
                    return false;
                }
            }

            if (exist(decimals)) {
                String l = defaultValue.substring(
                        defaultValue.lastIndexOf(".") + 1);
                if (decimals < l.length()) {
                    log.error("{}: the default value {} is too long," +
                            " the decimals is {}", from, defaultValue, decimals);
                    return false;
                }
            }
            new BigDecimal(defaultValue);
            return true;
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
            sb.append("(").append(length);
            if (exist(decimals)) {
                sb.append(",").append(decimals);
            }
            sb.append(")");
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
}
