package dive.sugar.annotate.type;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * SET
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface SET {

    /**
     * 集合值
     *
     * @return 值
     */
    String[] value() default {};

}
