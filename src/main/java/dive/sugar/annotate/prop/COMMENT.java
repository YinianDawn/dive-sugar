package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 注释
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface COMMENT {

    /**
     * 注释
     *
     * @return 注释
     */
    String value();

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
