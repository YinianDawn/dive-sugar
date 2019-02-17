package dive.sugar.annotate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 是否不检查
 *
 * @author dawn
 */
@Target({TYPE, FIELD})
@Retention(RUNTIME)
public @interface TRANSIENT {
}
