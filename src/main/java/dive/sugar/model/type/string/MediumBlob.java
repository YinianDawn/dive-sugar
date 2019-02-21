package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class MediumBlob extends BaseBlobColumn {

    static {
        lengthMax = 16777215;
    }

    public MediumBlob(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "MEDIUMBLOB"; }

}
