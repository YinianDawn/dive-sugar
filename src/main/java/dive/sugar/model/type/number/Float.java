package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Float extends BaseFloatColumn {

    // `id` float DEFAULT NULL

    {
        lengthMax = 16;
        decimalMax = 7;
    }

    public Float(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "FLOAT"; }

}
