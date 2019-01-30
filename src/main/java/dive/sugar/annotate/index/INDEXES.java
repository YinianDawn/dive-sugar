package dive.sugar.annotate.index;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 索引
 * 多个索引
 *
 * @author dawn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface INDEXES {

    /**
     * 允许多个索引
     *
     * @return 多个索引
     */
    INDEX[] value() default {};

}
