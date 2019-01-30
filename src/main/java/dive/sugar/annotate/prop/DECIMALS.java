package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 小数精度
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface DECIMALS {
    /**
     * 小数精度
     *
     * @return 小数精度
     */
    int value() default 0;

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
