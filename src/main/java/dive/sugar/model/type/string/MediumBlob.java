package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class MediumBlob extends BaseBlobColumn {

    static {
        lengthMax = 16777215;
    }

    public MediumBlob(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "MEDIUMBLOB"; }

}
