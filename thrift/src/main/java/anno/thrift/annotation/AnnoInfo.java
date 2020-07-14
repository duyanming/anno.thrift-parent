package anno.thrift.annotation;

import java.lang.annotation.*;

/**
 * @author duyanming
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnnoInfo {
    String name() default "";

    String desc() default "";

    boolean required() default true;

    String defaultValue() default "";
}
