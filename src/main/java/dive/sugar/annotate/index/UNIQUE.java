package dive.sugar.annotate.index;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 唯一索引
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(UNIQUES.class)
public @interface UNIQUE {

    /**
     * 索引名称
     * 默认不设置
     *
     * @return 索引名称
     */
    String name() default "";

    /**
     * 需要索引的列名称
     * alias for names
     *
     * @return 列名称
     */
    String[] value() default {};

    /**
     * 被索引的列
     * 属性上默认本属性
     *
     * @return 列名
     */
    String[] names() default {};

    /**
     * 注释
     *
     * @return 注释
     */
    String comment() default "";

    /**
     * 是否唯一索引
     * 默认是唯一索引
     *
     * @return 是否唯一索引
     */
    boolean unique() default true;

}
