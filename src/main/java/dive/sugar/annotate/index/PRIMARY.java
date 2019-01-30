package dive.sugar.annotate.index;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 主键索引
 * 不支持联合主键
 *
 * @author dawn
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface PRIMARY {

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
