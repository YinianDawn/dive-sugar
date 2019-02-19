package dive.sugar.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 是否从父类继承属性列名
 * 继承指定类的属性列名
 *
 * @author dawn
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface EXTEND {

    /**
     * 继承类
     *
     * @return 继承类
     */
    Class value() default Object.class;

    /**
     * 排出的列名
     *
     * @return 排出的列
     */
    String[] exclude() default {};

}
