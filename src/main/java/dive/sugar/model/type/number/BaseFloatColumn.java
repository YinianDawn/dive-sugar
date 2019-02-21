package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.function.Consumer;

/**
 * 浮点数类型
 *
 * @author dawn
 */
public abstract class BaseFloatColumn extends BaseDecimalColumn {

    BaseFloatColumn(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean check(Integer decimals, String from, Integer precision,
                            Integer scale, Consumer<Integer> consumer) {
        boolean result = super.check(decimals, from, precision, scale, consumer);
        if (!result) {
            return false;
        }
        if ((exist(this.length) && !exist(this.decimals))
                || (!exist(this.length) && exist(this.decimals))) {
            log.error("{}: the length and decimals of type {} " +
                            "must be not null at the same time: " +
                            "length->{},decimals->{}", from, type,
                    this.length, this.decimals);
            return false;
        }
        return true;
    }

    @Override
    public String definition() {
        StringBuilder sb = new StringBuilder();

        sb.append("`").append(name).append("` ").append(type);

        if (exist(length) && exist(decimals)) {
            sb.append("(").append(length).append(",");
            sb.append(decimals).append(")");
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
    public boolean same(Column s) {
        if (!exist(s)
                || !name.equals(s.name)
                || !type.equals(s.type)) {
            return false;
        }

        if (exist(length)) {
            if (exist(s.length)) {
                if (!length.equals(s.length)) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (exist(s.length)) {
                return false;
            }
        }

        if (exist(decimals)) {
            if (exist(s.decimals)) {
                if (!decimals.equals(s.decimals)) {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            if (exist(s.decimals)) {
                return false;
            }
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

        if (useful(defaultValue)) {
            BigDecimal d = new BigDecimal(defaultValue);
            BigDecimal e = new BigDecimal(s.defaultValue);
            int scale = d.scale() < e.scale() ? e.scale() : d.scale();
            d = d.setScale(scale, BigDecimal.ROUND_DOWN);
            e = e.setScale(scale, BigDecimal.ROUND_DOWN);
            if (d.compareTo(e) != 0) {
                return false;
            }
        } else if (useful(defaultValue)) {
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
