package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
abstract class BaseTextColumn extends BaseStringColumn {

    BaseTextColumn(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected boolean initLength() { return true; }

    @Override
    protected boolean check(Integer length, String from) { return false; }

    @Override
    protected boolean check(String defaultValue, String from) {
        if (!exist(defaultValue)) {
            return true;
        }
        int l = defaultValue.length();
        if (lengthMax() < l) {
            log.error("{}: the max length of type {} is {}, " +
                            "but default value({})'s length is {}",
                    from, type, lengthMax(), defaultValue, l);
            return false;
        }
        return true;
    }

    @Override
    public String definition() {
        StringBuilder sb = new StringBuilder();

        sb.append("`").append(name).append("` ").append(type);

        if (isTrue(binary)) {
            sb.append(" BINARY");
        }

        if (useful(charset)) {
            sb.append(" CHARACTER SET '").append(charset).append("'");
        }
        if (useful(collate)) {
            sb.append(" COLLATE '").append(collate).append("'");
        }

        if (isTrue(notNull)) {
            sb.append(" NOT NULL");
        } else if (!isTrue(primary) && isTrue(nullable)) {
            sb.append(" NULL");
        }

        if (exist(defaultValue)) {
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
    public boolean same(Column s) {
        if (!exist(s)
                || !name.equals(s.name)
                || !type.equals(s.type)) {
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
//        else if (!useful(charset) && useful(s.collate)) {
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
