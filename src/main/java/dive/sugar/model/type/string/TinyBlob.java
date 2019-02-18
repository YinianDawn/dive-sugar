package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class TinyBlob extends BaseBlobColumn {

    static {
        lengthMax = 255;
    }

    public TinyBlob(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "TINYBLOB"; }

}
