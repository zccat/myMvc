package zx.learn.mvc;

import lombok.extern.slf4j.Slf4j;
import zx.learn.core.BeanContainer;
import zx.learn.mvc.annotation.RequestMapping;
import zx.learn.mvc.annotation.RequestParam;
import zx.learn.mvc.bean.RequestType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 控制器分发器  需要对Bean进行扫描，之后拿出所有的 URL 和 方法的对应信息
 * 之后 通过URL直接查找方法进行执行
 */
@Slf4j
public class ControllerHandler {

    private Map<PathInfo, ControllerInfo> urlMap = new ConcurrentHashMap<>();

    private BeanContainer beanContainer;

    public ControllerHandler(){
        // 1. 先获取需要处理的类
        beanContainer = BeanContainer.getInstance();
        Set<Class<?>> classSet = beanContainer.getClassByAnnotation(RequestMapping.class);

        for (Class<?> clz : classSet) {
            putUrlMap(clz);
        }
    }

    /**
     * 获取ControllerInfo
     */
    public ControllerInfo getController(String requestMethod, RequestType requestPath) {
        PathInfo pathInfo = new PathInfo(requestMethod, requestPath);
        return urlMap.get(pathInfo);
    }

    // 从类上获取信息 之后放入 MAP中
    private void putUrlMap(Class<?> clz) {
//        PathInfo pathInfo = getPathInfo(clz);
//        先从方法上获取信息
        String basePath = clz.getAnnotation(RequestMapping.class).url();
        for (Method method : clz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                Parameter[] parameters = method.getParameters();
                // 获取参数名和对象类型的映射
                Map<String, Class<?>> paraMap = new HashMap<>();
                for (Parameter parameter : parameters) {
                    RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                    if (requestParam == null) {
                        throw new RuntimeException("必须有RequestParam指定的参数名");
                    } else {
                        paraMap.put(requestParam.value(), parameter.getType());
                    }
                }

                // 构造 PathInfo
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                String methodPath = requestMapping.url();
                PathInfo pathInfo = new PathInfo(basePath + methodPath,requestMapping.method());
                if (urlMap.containsKey(pathInfo)) {
                    log.error("url:{} 重复注册", pathInfo.getUrl());
                    throw new RuntimeException("URL重复注册了");
                }

                // 构造 ControllerInfo
                ControllerInfo controllerInfo = new ControllerInfo(clz, method, paraMap);
                this.urlMap.put(pathInfo, controllerInfo);
                log.info("Add Controller RequestMethod:{}, RequestPath:{}, Controller:{}, Method:{}",
                        pathInfo.getUrl(), pathInfo.getRequestMethod(),
                        controllerInfo.getControllerClass().getName(), controllerInfo.getInvokeMethod().getName());

            }
        }

    }


}
