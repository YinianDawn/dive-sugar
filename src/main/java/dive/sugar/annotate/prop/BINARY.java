package dive.sugar.annotate.prop;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 二进制存储
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface BINARY {
}
