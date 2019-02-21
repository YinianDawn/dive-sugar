package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class SmallInt extends BaseIntegerColumn {

    // `id` smallint(6) DEFAULT NULL

    {
        unsignedMin = 0;
        unsignedMax = 65535;
        signedMin = -32768;
        signedMax = 32767;
        defaultLength = 6;
    }

    public SmallInt(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
