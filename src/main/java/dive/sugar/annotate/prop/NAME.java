package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 命名
 * 表命名 或 列命名
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface NAME {

    /**
     * 表命名或列命名
     *
     * @return 命名
     */
    String value();

}
