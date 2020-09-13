package zx.learn.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import zx.learn.aop.annotation.Aspect;
import zx.learn.ioc.annotation.*;
import zx.learn.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanContainer {

    public final Map<Class<?>, Object> beanMap = new ConcurrentHashMap<>();

    private boolean isLoadBean = false;

    public static final List<Class<? extends Annotation>> BEAN_ANNOTATION
            = Arrays.asList(Controller.class, Service.class, Repository.class, Component.class, Aspect.class);

    private enum ContainerHolder {
        HOLDER;
        private BeanContainer instance;

        ContainerHolder() {
            instance = new BeanContainer();
        }
    }

    public static BeanContainer getInstance() {
        return ContainerHolder.HOLDER.instance;
    }

    public Object getBean(Class<?> clz) {
        if (null == clz) {
            return null;
        }
        return beanMap.get(clz);
    }

    public void addBean(Class<?> clz, Object obj) {
        beanMap.put(clz, obj);
    }

    public void removeBean(Class<?> clz) {
        beanMap.remove(clz);
    }

    public int size() {
        return beanMap.size();
    }

    public Object getBeans() {
        return new HashSet<>(beanMap.values());
    }

    public Set<Class<?>> getClasses() {
        return beanMap.keySet();
    }

    //    通过注解获取Bean的Class集合
    public Set<Class<?>> getClassByAnnotation(Class<? extends Annotation> annotation) {
        return beanMap.keySet().parallelStream()
                .filter(clz -> clz.isAnnotationPresent(annotation))
                .collect(Collectors.toSet());
    }

    //    通过实现类或者父类获取Bean的Class集合  例如 传入 UserService 传出 UserServiceImpl
    public Set<Class<?>> getClassesBySuper(Class<?> superClass) {
        return beanMap.keySet().stream()
                .filter(superClass::isAssignableFrom)
                .filter(clz -> !clz.equals(superClass))
                .collect(Collectors.toSet());
    }

    /**
     * 加载Bean的类型以及创建Bean的实例到Map中
     * @param basePackage 需要扫描的Bean的包名
     */
    public void loadBeans(String basePackage) {
        if (isLoadBean) {
            log.warn("已经加载了Bean");
        }
        Set<Class<?>> classSet = ClassUtil.getPackageClass(basePackage);
        classSet.stream()
                .filter(clz -> {
                    for (Class<? extends Annotation> aClass : BEAN_ANNOTATION) {
                        if (clz.isAnnotationPresent(aClass)) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(clz -> addBean(clz, ClassUtil.newInstance(clz)));
        isLoadBean = true;
    }

    public boolean isLoadBean() {
        return isLoadBean;
    }


}
