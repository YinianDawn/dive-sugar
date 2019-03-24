package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Float extends BaseFloatColumn {

    // `id` float DEFAULT NULL

    public Float(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected int lengthMax() { return 16; }

    @Override
    protected int decimalMax() { return 7; }

}
