package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class LongBlob extends BaseBlobColumn {

    @Override
    long lengthMax() {
        return 4294967295L;
    }

    public LongBlob(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
