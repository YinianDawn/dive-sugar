package dive.sugar.annotate.index;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多个唯一索引
 *
 * @author dawn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UNIQUES {

    /**
     * 允许多个唯一索引
     *
     * @return 多个唯一索引
     */
    UNIQUE[] value() default {};

}
