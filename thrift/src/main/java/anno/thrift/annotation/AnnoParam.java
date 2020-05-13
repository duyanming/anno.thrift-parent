package anno.thrift.annotation;

import java.lang.annotation.*;

/**
 * @author duyanming
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnoParam {
    String name() default "";

    boolean required() default true;

    String defaultValue() default "";
}
