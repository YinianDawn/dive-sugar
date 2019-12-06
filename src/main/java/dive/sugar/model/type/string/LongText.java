package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class LongText extends BaseTextColumn {

    @Override
    long lengthMax() {
        return 4294967295L;
    }

    public LongText(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
