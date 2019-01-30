package dive.sugar.annotate.type;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * INTEGER
 *
 * @author dawn
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface INTEGER {
}
