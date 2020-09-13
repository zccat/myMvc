package zx.learn.ioc;

import zx.learn.core.BeanContainer;
import zx.learn.ioc.annotation.Autowired;
import zx.learn.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.Optional;

public class Ioc {

    BeanContainer beanContainer;

    public Ioc() {
        beanContainer = BeanContainer.getInstance();
    }


    // 现在把所有的Bean都加载到了容器中，需要对每个Bean进行遍历，如果他的Field有Autowired 注解 则注入
    public void doIoc() {
        for (Class<?> clz : beanContainer.getClasses()) {
            final Object targetBean = beanContainer.getBean(clz);
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    final Class<?> fieldClass = field.getType();
                    Object fieldValue = getClassInstance(fieldClass);
                    if (null != fieldValue) {
                        ClassUtil.setField(field, targetBean, fieldValue);
                    } else {
                        throw new RuntimeException("无法注入对应的类，目标类型:" + fieldClass.getName());
                    }
                }
            }
        }
    }

    private Object getClassInstance(Class<?> fieldClass) {
        return Optional
                .ofNullable(beanContainer.getBean(fieldClass))
                .orElseGet(() -> {
                    Class<?> implClass = getImplementClass(fieldClass);
                    if (null != implClass) {
                        return beanContainer.getBean(implClass);
                    }
                    return null;
                });
    }

    private Class<?> getImplementClass(final Class<?> interfaceClass) {
        return beanContainer.getClassesBySuper(interfaceClass).stream().findFirst().orElse(null);
    }

}
