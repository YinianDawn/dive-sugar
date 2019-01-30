package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 二进制存储
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BINARY {

    /**
     * 是否启用 alias for useful
     *
     * @return 是否启用
     */
    boolean value() default true;

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
