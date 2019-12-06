package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class TinyBlob extends BaseBlobColumn {

    {
        lengthMax = 255;
    }

    public TinyBlob(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
