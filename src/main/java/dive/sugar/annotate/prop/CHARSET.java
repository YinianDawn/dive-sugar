package dive.sugar.annotate.prop;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 字符集
 * 类名前：表的字符集
 * 属性前：列的字符集
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface CHARSET {

    /**
     * 字符集
     *
     * @return 字符集
     */
    String value() default "utf8mb4";

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
