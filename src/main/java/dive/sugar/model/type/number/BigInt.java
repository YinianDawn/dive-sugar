package dive.sugar.model.type.number;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.math.BigInteger;

/**
 * @author dawn
 */
public class BigInt extends BaseIntegerColumn {

    // `id` int(20) DEFAULT NULL

    private static final BigInteger UNSIGNED_MIN = new BigInteger("0");
    private static final BigInteger UNSIGNED_MAX =
            new BigInteger("18446744073709551615");
    private static final BigInteger SIGNED_MIN =
            new BigInteger("-9223372036854775808");
    private static final BigInteger SIGNED_MAX =
            new BigInteger("9223372036854775807");

    public BigInt(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected long unsignedMin() { return 0; }

    @Override
    protected long unsignedMax() { return 0; }

    @Override
    protected long signedMin() { return 0; }

    @Override
    protected long signedMax() { return 0; }

    @Override
    protected int defaultLength() { return 20; }

    @Override
    protected boolean check(String defaultValue, String from) {
        if (!exist(defaultValue)) {
            return true;
        }
        try {
            BigInteger value = new BigInteger(defaultValue);
            if (exist(unsigned) && unsigned) {
                boolean result = UNSIGNED_MIN.compareTo(value) <= 0
                        && value.compareTo(UNSIGNED_MAX) <= 0;
                if (result) {
                    return true;
                }
                log.error("{}: the default value of type unsigned {} " +
                                "must be {} ~ {}: {}", from, type,
                        UNSIGNED_MIN.toString(), UNSIGNED_MAX.toString(),
                        defaultValue);
            } else {

                boolean result = SIGNED_MIN.compareTo(value) <= 0
                        && value.compareTo(SIGNED_MAX) <= 0;
                if (result) {
                    return true;
                }
                log.error("{}: the default value of type {} " +
                                "must be {} ~ {}: {}", from, type,
                        SIGNED_MIN.toString(), SIGNED_MAX.toString(), defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}: {} can not cast to number", from, defaultValue);
        }
        return false;
    }
}
