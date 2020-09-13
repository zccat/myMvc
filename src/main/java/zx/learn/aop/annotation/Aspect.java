package zx.learn.aop.annotation;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
//    /**
//     * 目标代理类的范围 需要用注解来指定代理范围
//     */
//    Class<? extends Annotation> target();

    /**
     * 切点表达式
     */
    String pointcut() default "";
}