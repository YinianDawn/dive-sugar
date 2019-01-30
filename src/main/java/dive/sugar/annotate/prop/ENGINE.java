package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 表引擎
 *
 * @author dawn
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ENGINE {

    /**
     * 表引擎
     *
     * @return 表引擎
     */
    String value() default "InnoDB";

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
