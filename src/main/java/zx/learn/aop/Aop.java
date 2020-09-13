package zx.learn.aop;

import lombok.extern.slf4j.Slf4j;
import zx.learn.aop.advice.Advice;
import zx.learn.aop.annotation.Aspect;
import zx.learn.aop.annotation.Order;
import zx.learn.core.BeanContainer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Aop {


    /**
     * Bean容器
     */
    private BeanContainer beanContainer;

    public Aop() {
        beanContainer = BeanContainer.getInstance();
    }

    /**
     * 遍历在BeanContainer容器被Aspect注解的Bean，并找到实现了Advice接口的类，这些类便是切面
     * 获取切面上的注解Aspect的target()的值，这个值就是要被代理的类的注解。
     * 比如说有个切面的注解为@Aspect(target = Controller.class)，那么这个切面会作用在被Controller注解的类上。
     * 遍历BeanContainer容器被aspect.target()的值注解的Bean，找到目标代理类
     * 创建ProxyAdvisor代理类并通过cglib创建出这个代理类的实例，并把这个类实例放回到BeanContainer容器中。
     */
//    public void doAop() {
//        //找到Advice的实现类
//        beanContainer.getClassesBySuper(Advice.class)
//                .stream()
//                //过滤出被 Aspect 注解的类
//                .filter(clz -> clz.isAnnotationPresent(Aspect.class))
//                .forEach(clz ->{
//                    // 获取到切面实例
//                    final Advice advice = (Advice) beanContainer.getBean(clz);
//                    // 获取到这个类的 Aspect 注解实例 以便获取到注解信息
//                    Aspect aspect = clz.getAnnotation(Aspect.class);
//                    // 获取到这个 切面的目标类 可能不止一个
//                    beanContainer.getClassByAnnotation(aspect.target())
//                            .stream()
//                            /**
//                             * 确定由此 类对象表示的类或接口是否与由指定的Class 类表示的类或接口 相同 或是 超类 或 类接口。
//                             * 换句话说 什么时候返回true  A.class.isAssignableFrom(B.class)
//                             * 1. A 与 B 类相同
//                             * 2. A 是 B 的父类
//                             * 3. B 实现了 A
//                             */
//                            // 过滤出父类不是Advice的Class （也可以直接是Advice类型）
//                            .filter(target -> !Advice.class.isAssignableFrom(target))
//                            // 过滤此元素上有指定类型（Aspect）的注解的类
//                            .filter(target -> !target.isAnnotationPresent(Aspect.class))
//                            .forEach(target -> {
//                                Object proxyBean = ProxyCreator.createProxy(target, advisor);
//                                beanContainer.addBean(target, proxyBean);
//                            });
//                });
//    }


    /**
     * 1. 找到切面类 需要实现 Advice 接口
     * 2. 这个切面类需要有Aspect注解，以便获取切点信息
     */
//    public void doAop() {
//        beanContainer.getClassesBySuper(Advice.class)
//                .stream()
//                .filter(clz -> clz.isAnnotationPresent(Aspect.class))
//                .map(this::createProxyAdvisor)
//                // 到这里其实就已经创建好切面类的，后面就是进行切面类的替换了
//                .forEach(proxyAdvisor -> beanContainer.getClasses()
//                        .stream()
//                        // 过滤 这个类 没有实现Advice接口 没有被Aspect注解
//                        .filter(target -> !Advice.class.isAssignableFrom(target))
//                        .filter(target -> !target.isAnnotationPresent(Aspect.class))
//                        .forEach(target -> {
//                                    // 通过切点是否匹配该类 决定是否进行替换
//                                    if (proxyAdvisor.getPointcut().matches(target)) {
//                                        Object proxyBean = ProxyCreator.createProxy(target, proxyAdvisor);
//                                        beanContainer.addBean(target, proxyBean);
//                                    }
//                                }
//                        )
//                );
//    }

    public void doAop() {
        //创建所有的代理通知列表
        List<ProxyAdvisor> proxyList = beanContainer.getClassesBySuper(Advice.class)
                .stream()
                .filter(clz -> clz.isAnnotationPresent(Aspect.class))
                .map(this::createProxyAdvisor)
                .collect(Collectors.toList());

        //创建代理类并注入到Bean容器中
        beanContainer.getClasses()
                .stream()
                .filter(clz -> !Advice.class.isAssignableFrom(clz))
                .filter(clz -> !clz.isAnnotationPresent(Aspect.class))
                .forEach(clz -> {
                    List<ProxyAdvisor> matchProxies = createMatchProxies(proxyList, clz);
                    if (matchProxies.size() > 0) {
                        Object proxyBean = ProxyCreator.createProxy(clz, matchProxies);
                        beanContainer.addBean(clz, proxyBean);
                    }
                });
    }

//    private ProxyAdvisor createProxyAdvisor(Class<?> aClass) {
//        String expression = aClass.getAnnotation(Aspect.class).pointcut();
//        ProxyPointcut proxyPointcut = new ProxyPointcut();
//        proxyPointcut.setExpression(expression);
//        Advice advice = (Advice) beanContainer.getBean(aClass);
//        return new ProxyAdvisor(advice, proxyPointcut);
//    }

    private ProxyAdvisor createProxyAdvisor(Class<?> aspectClass) {
        int order = 0;
        if (aspectClass.isAnnotationPresent(Order.class)) {
            order = aspectClass.getAnnotation(Order.class).value();
        }
        String expression = aspectClass.getAnnotation(Aspect.class).pointcut();
        ProxyPointcut proxyPointcut = new ProxyPointcut();
        proxyPointcut.setExpression(expression);
        Advice advice = (Advice) beanContainer.getBean(aspectClass);
        return new ProxyAdvisor(advice, proxyPointcut, order);
    }

    /**
     * 获取目标类匹配的代理通知列表
     */
    private List<ProxyAdvisor> createMatchProxies(List<ProxyAdvisor> proxyList, Class<?> targetClass) {
        Object targetBean = beanContainer.getBean(targetClass);
        return proxyList
                .stream()
                .filter(advisor -> advisor.getPointcut().matches(targetBean.getClass()))
                .sorted(Comparator.comparingInt(ProxyAdvisor::getOrder))
                .collect(Collectors.toList());
    }

}
