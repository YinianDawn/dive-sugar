package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 是否非空
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface NOTNULL {

    /**
     * 是否非空
     * alias for useful
     *
     * @return 是否非空
     */
    boolean value() default true;

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
