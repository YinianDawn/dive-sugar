package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 是否以0填充
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ZEROFILL {
    /**
     * 是否以0填充
     * alias for useful
     *
     * @return 是否以0填充
     */
    boolean value() default true;

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
