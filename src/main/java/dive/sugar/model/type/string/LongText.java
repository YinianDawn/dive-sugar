package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class LongText extends BaseTextColumn {

    static {
        lengthMax = 4294967295L;
    }

    public LongText(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "LONGTEXT"; }

}
