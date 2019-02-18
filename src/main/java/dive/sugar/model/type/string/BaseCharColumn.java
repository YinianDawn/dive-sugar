package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public abstract class BaseCharColumn extends BaseStringColumn {


    BaseCharColumn(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    public String definition() {
        StringBuilder sb = new StringBuilder();

        sb.append("`").append(name).append("` ").append(type);

        if (exist(length)) {
            sb.append("(").append(length).append(")");
        }

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
}
