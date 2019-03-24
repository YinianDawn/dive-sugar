package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Double extends BaseFloatColumn {

    // `id` double DEFAULT NULL

    public Double(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected int lengthMax() {
        return 35;
    }

    @Override
    protected int decimalMax() {
        return 15;
    }

}
