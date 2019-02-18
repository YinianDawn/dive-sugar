package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class MediumInt extends BaseIntegerColumn {

    // `id` mediumint(9) DEFAULT NULL

    {
        unsignedMin = 0;
        unsignedMax = 16777215;
        signedMin = -8388608;
        signedMax = 8388607;
        defaultLength = 9;
    }

    public MediumInt(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "MEDIUMINT"; }

}
