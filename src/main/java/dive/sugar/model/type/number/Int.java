package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class Int extends BaseIntegerColumn {

    // `id` int(11) DEFAULT NULL

    public Int(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected long unsignedMin() { return 0; }

    @Override
    protected long unsignedMax() { return 4294967295L; }

    @Override
    protected long signedMin() { return -2147483648L; }

    @Override
    protected long signedMax() { return 2147483647L; }

    @Override
    protected int defaultLength() {
        return isTrue(unsigned) ? 10 : 11;
    }

}
