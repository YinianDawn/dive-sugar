package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class VarBinary extends BaseBinaryColumn {

    static {
        lengthMax = 65535;
    }

    public VarBinary(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "VARBINARY"; }

    @Override
    public boolean same(BaseColumn s) {
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
        } else if (!exist(collate) && exist(s.comment)) {
            return false;
        }

        return place.equals(s.place);
    }

}