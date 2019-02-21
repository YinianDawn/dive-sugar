package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class LongBlob extends BaseBlobColumn {

    static {
        lengthMax = 4294967295L;
    }

    public LongBlob(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "LONGBLOB"; }

}
