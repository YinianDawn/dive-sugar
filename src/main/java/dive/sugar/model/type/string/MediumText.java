package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class MediumText extends BaseTextColumn {

    static {
        lengthMax = 16777215;
    }

    public MediumText(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
