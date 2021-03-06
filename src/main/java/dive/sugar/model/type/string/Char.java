package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Char extends BaseCharColumn {

    @Override
    long lengthMax() {
        return 255;
    }

    public Char(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean check(Integer length, String from) {
        if (!exist(length)) {
            return true;
        }
        return super.check(length, from);
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
            length = 1;
        }
        if (!length.equals(s.length)) {
            return false;
        }

        if (isTrue(binary) && !isTrue(s.binary)) {
            return false;
        } else if (!isTrue(binary) && isTrue(s.binary)) {
            return false;
        }
        if (useful(charset) && !charset.equals(s.charset)) {
            return false;
        }
//        else if (!useful(charset) && useful(s.charset)) {
//            return false;
//        }
        if (useful(collate) && !collate.equals(s.collate)) {
            return false;
        }
//        else if (!useful(collate) && useful(s.collate)) {
//            return false;
//        }

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
