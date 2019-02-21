package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

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

    public Double(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
