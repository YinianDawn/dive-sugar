package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Blob extends BaseBlobColumn {

    static {
        lengthMax = 65535;
    }

    public Blob(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
