package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class SmallInt extends BaseIntegerColumn {

    // `id` smallint(6) DEFAULT NULL

    public SmallInt(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected long unsignedMin() { return 0; }

    @Override
    protected long unsignedMax() { return 65535L; }

    @Override
    protected long signedMin() { return -32768L; }

    @Override
    protected long signedMax() { return 32767L; }

    @Override
    protected int defaultLength() { return 6; }

}
