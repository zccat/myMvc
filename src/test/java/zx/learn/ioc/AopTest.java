package zx.learn.ioc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import zx.learn.aop.Aop;
import zx.learn.bean.DoodleController;
import zx.learn.core.BeanContainer;

@Slf4j
public class AopTest {
    @Test
    public void doAop() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("zx.learn");
        new Aop().doAop();
        new Ioc().doIoc();
        DoodleController controller = (DoodleController) beanContainer.getBean(DoodleController.class);
        controller.hello();
    }

    @Test
    public void testAspectAop() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("zx.learn");
        new Aop().doAop();
        new Ioc().doIoc();
        DoodleController controller = (DoodleController) beanContainer.getBean(DoodleController.class);
        controller.hello();
        controller.helloForAspect();
    }
}