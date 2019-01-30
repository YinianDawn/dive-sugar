package dive.sugar.annotate.index;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 索引
 * 位置：类名 和 属性
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
@Repeatable(INDEXES.class)
public @interface INDEX {

    /**
     * 索引名称
     * 默认不设置
     *
     * @return 索引名称
     */
    String name() default "";

    /**
     * 需要索引的列名称
     * 属性前默认为本属性
     *
     * @return 列名称
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
     * 默认非唯一索引
     *
     * @return 是否唯一索引
     */
    boolean unique() default false;

}
