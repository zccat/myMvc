package zx.learn.aop;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodProxy;
import zx.learn.aop.advice.Advice;
import zx.learn.aop.advice.AfterReturningAdvice;
import zx.learn.aop.advice.MethodBeforeAdvice;
import zx.learn.aop.advice.ThrowsAdvice;

import java.lang.reflect.Method;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProxyAdvisor {

    private Advice advice;

    /**
     * AspectJ表达式切点匹配器
     */
    private ProxyPointcut pointcut;

    /**
     * 执行顺序
     */
    private int order;

    /**
     * 执行代理方法
     */
    public Object doProxy(AdviceChain adviceChain) throws Throwable {
        Object result = null;
        Class<?> targetClass = adviceChain.getTargetClass();
        Method method = adviceChain.getMethod();
        Object[] args = adviceChain.getArgs();

        if (advice instanceof MethodBeforeAdvice) {
            ((MethodBeforeAdvice) advice).before(targetClass, method, args);
        }
        try {
            result = adviceChain.doAdviceChain(); //执行代理链方法
            if (advice instanceof AfterReturningAdvice) {
                ((AfterReturningAdvice) advice).afterReturning(targetClass, result, method, args);
            }
        } catch (Exception e) {
            if (advice instanceof ThrowsAdvice) {
                ((ThrowsAdvice) advice).afterThrowing(targetClass, method, args, e);
            } else {
                throw new Throwable(e);
            }
        }
        return result;
    }


//    public Object doProxy(Object target, Class<?> targetClass, Method method, Object[] args, MethodProxy proxy) throws Throwable {
//
//        if (!pointcut.matches(method)) {
//            return proxy.invokeSuper(target, args);
//        }
//
//        Object result = null;
//        //如果是前置通知
//        if (advice instanceof MethodBeforeAdvice) {
//            ((MethodBeforeAdvice) advice).before(targetClass, method, args);
//        }
//        try {
//            result = proxy.invokeSuper(target, args);
////            如果是后置通知
//            if (advice instanceof AfterReturningAdvice) {
//                ((AfterReturningAdvice) advice).afterReturning(targetClass, result, method, args);
//            }
//        } catch (Exception exception) {
//            //如果是抛出异常通知
//            if (advice instanceof ThrowsAdvice) {
//                ((ThrowsAdvice) advice).afterThrowing(targetClass, method, args, exception);
//            }
//        }
//        return result;
//    }
}
