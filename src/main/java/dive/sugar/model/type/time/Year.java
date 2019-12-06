package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 年类型
 *
 * @author dawn
 */
public class Year extends BaseDateColumn {

    // `id` year(4) DEFAULT NULL

    private Date min;
    private Date max;

    {
        SimpleDateFormat sdf = sdf();
        try {
            min = sdf.parse("1900");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            max = sdf.parse("2156");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    SimpleDateFormat sdf() {
        return new SimpleDateFormat("yyyy");
    }

    @Override
    Date min() {
        return min;
    }

    @Override
    Date max() {
        return max;
    }

    public Year(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
