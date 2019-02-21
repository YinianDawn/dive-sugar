package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author dawn
 */
public class Decimal extends BaseDecimalColumn {

    // `id` decimal(10,0) DEFAULT NULL

    {
        lengthMax = 65;
        decimalMax = 30;
    }

    public Decimal(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "DECIMAL"; }

    @Override
    public boolean same(Column s) {
        if (!exist(s)
                || !name.equals(s.name)
                || !type.equals(s.type)) {
            return false;
        }

        Integer length = this.length;
        if (!exist(length)) {
            length = 10;
        }
        if (!length.equals(s.length)) {
            return false;
        }

        Integer decimals = this.decimals;
        if (!exist(decimals)) {
            decimals = 0;
        }
        if (!decimals.equals(s.decimals)) {
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
