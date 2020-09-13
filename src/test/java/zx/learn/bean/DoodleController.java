package zx.learn.bean;

import lombok.extern.slf4j.Slf4j;
import zx.learn.ioc.annotation.Autowired;
import zx.learn.ioc.annotation.Controller;

@Controller
@Slf4j
public class DoodleController {

    @Autowired
    private DoodleService doodleService;

    public void helloForAspect() {
        log.info("helloForAspect");
    }

    public void hello() {
        log.info(doodleService.helloWord());
    }
}
