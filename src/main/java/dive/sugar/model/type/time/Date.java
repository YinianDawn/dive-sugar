package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期类型
 *
 * @author dawn
 */
public class Date extends BaseDateColumn {

    // `id` date DEFAULT NULL

    {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            min = sdf.parse("1000-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            max = sdf.parse("9999-12-31");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

}
