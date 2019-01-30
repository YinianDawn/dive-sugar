package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 时间精度
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface FSP {

    /**
     * 精度
     *
     * @return 精度
     */
    int value() default 0;

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
