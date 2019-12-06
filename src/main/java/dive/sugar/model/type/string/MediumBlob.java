package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class MediumBlob extends BaseBlobColumn {

    @Override
    long lengthMax() {
        return 16777215;
    }

    public MediumBlob(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
