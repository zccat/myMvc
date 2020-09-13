package zx.learn.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ClassTest {

    public static void main(String[] args) {
        System.out.println(Ann.class.isAssignableFrom(B.class));
        System.out.println(B.class.isAssignableFrom(Ann.class));
    }

}

interface A {}

@Ann
class B implements A {}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Ann{}