package zx.learn.ioc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import zx.learn.bean.DoodleController;
import zx.learn.core.BeanContainer;

@Slf4j
public class IocTest {
    @Test
    public void doIoc() {
        BeanContainer beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans("zx.learn");
        new Ioc().doIoc();
        DoodleController controller = (DoodleController) beanContainer.getBean(DoodleController.class);
        controller.hello();
    }
}