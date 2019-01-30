package dive.sugar.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 表标记
 *
 * @author dawn
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface TABLE {

    /**
     * 表名称
     *
     * @return 表命名
     */
    String value() default "";

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
