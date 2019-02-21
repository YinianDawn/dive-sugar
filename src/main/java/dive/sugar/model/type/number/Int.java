package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Int extends BaseIntegerColumn {

    // `id` int(11) DEFAULT NULL

    {
        unsignedMin = 0;
        unsignedMax = 4294967295L;
        signedMin = -2147483648;
        signedMax = 2147483647;
        defaultLength = 11;
    }

    public Int(Field field, Sugar builder, Column model) {
        super(field, builder, model);
        if (isTrue(unsigned)) {
            defaultLength = 10;
        }
    }

    @Override
    protected void initType() { this.type = "INT"; }

}
