package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Double extends BaseFloatColumn {

    // `id` double DEFAULT NULL

    {
        lengthMax = 35;
        decimalMax = 15;
    }

    public Double(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "DOUBLE"; }

}
