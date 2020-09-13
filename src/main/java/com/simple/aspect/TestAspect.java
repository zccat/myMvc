package com.simple.aspect;

import lombok.extern.slf4j.Slf4j;
import zx.learn.aop.advice.AroundAdvice;
import zx.learn.aop.annotation.Aspect;

import java.lang.reflect.Method;

@Slf4j
@Aspect(pointcut = "execution(* com.simple.controller.DoodleController.*(..))")
public class TestAspect implements AroundAdvice {

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
