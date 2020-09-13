package zx.learn.bean;

import lombok.extern.slf4j.Slf4j;
import zx.learn.aop.advice.AroundAdvice;
import zx.learn.aop.annotation.Aspect;
import zx.learn.aop.annotation.Order;
import zx.learn.ioc.annotation.Controller;

import java.lang.reflect.Method;

@Slf4j
//@Aspect(target = Controller.class)
@Order(1)
@Aspect(pointcut = "execution(* zx.learn.bean.DoodleController.helloForAspect(..))")
public class DoodleAspect implements AroundAdvice {

    @Override
    public void before(Class<?> clz, Method method, Object[] args) throws Throwable {
        log.info("Before  DoodleAspect ----> class: {}, method: {}", clz.getName(), method.getName());
    }

    @Override
    public void afterReturning(Class<?> clz, Object returnValue, Method method, Object[] args) throws Throwable {
        log.info("After  DoodleAspect ----> class: {}, method: {}", clz, method.getName());
    }

    @Override
    public void afterThrowing(Class<?> clz, Method method, Object[] args, Throwable e) {
        log.error("Error  DoodleAspect ----> class: {}, method: {}, exception: {}", clz, method.getName(), e.getMessage());
    }
}