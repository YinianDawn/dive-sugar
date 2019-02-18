package dive.sugar.model.type.time;

import dive.sugar.Sugar;
import dive.sugar.model.BaseColumn;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 年类型
 *
 * @author dawn
 */
public class Year extends BaseDateColumn {

    // `id` year(4) DEFAULT NULL

    static {
        SDF = new SimpleDateFormat("yyyy");
        try {
            min = SDF.parse("1900");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            max = SDF.parse("2156");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Year(Field field, Sugar builder, BaseColumn model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "YEAR"; }

}