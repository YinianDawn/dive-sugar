package dive.sugar.annotate.prop;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 排序
 * 类名前：表的排序
 * 属性前：列的排序
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface COLLATE {

    /**
     * 排序
     *
     * @return 排序
     */
    String value() default "utf8mb4_general_ci";

}
