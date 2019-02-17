package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 改名
 * 表改名 或 列改名
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface FROM {

    /**
     * 改名
     *
     * @return 改名来源
     */
    String value();

}
