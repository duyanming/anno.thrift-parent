package anno.thrift.dynamicProxy;

import java.lang.annotation.*;

/**
 * @author duyanming
 */
@Target({ElementType.PARAMETER,ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/***
 * Anno 接口注解代理
 */
public @interface AnnoProxy {
    /***
     * 管道、Package Namespace
     * @return 管道
     */
    String channel() default "";

    /***
     * 路由 class
     * @return 路由
     */
    String router() default "";

    /***
     * 方法 Action
     * @return 方法
     */
    String method() default "";
}
