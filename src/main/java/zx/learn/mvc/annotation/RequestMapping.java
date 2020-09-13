package zx.learn.mvc.annotation;

import zx.learn.mvc.bean.RequestType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    RequestType method() default RequestType.GET;

    String url()  default "";;



}
