package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据长度
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface LENGTH {

    /**
     * 数据长度
     *
     * @return 数据长度
     */
    int value() default 0;


    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
