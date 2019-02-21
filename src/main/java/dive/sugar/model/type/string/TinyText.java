package dive.sugar.model.type.string;

import dive.sugar.Sugar;
import dive.sugar.model.Column;

import java.lang.reflect.Field;

/**
 * @author dawn
 */
public class TinyText extends BaseTextColumn {

    static {
        lengthMax = 255;
    }

    public TinyText(Field field, Sugar builder, Column model) {
        super(field, builder, model);
    }

    @Override
    protected void initType() { this.type = "TINYTEXT"; }

}
