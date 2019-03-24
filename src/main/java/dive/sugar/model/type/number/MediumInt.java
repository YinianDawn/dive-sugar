package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class MediumInt extends BaseIntegerColumn {

    // `id` mediumint(9) DEFAULT NULL

    public MediumInt(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected long unsignedMin() { return 0; }

    @Override
    protected long unsignedMax() { return 16777215L; }

    @Override
    protected long signedMin() { return -8388608L; }

    @Override
    protected long signedMax() { return 8388607L; }

    @Override
    protected int defaultLength() { return 9; }

}
