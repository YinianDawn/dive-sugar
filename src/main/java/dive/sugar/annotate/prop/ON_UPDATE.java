package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 更新动作
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface ON_UPDATE {

    /**
     * 更新动作
     * 常用与时间
     *
     * @return 更新动作
     */
    String value() default "CURRENT_TIMESTAMP";

    /**
     * 是否启用
     *
     * @return 是否启用
     */
    boolean useful() default true;

}
