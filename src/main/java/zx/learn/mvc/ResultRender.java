package zx.learn.mvc;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import zx.learn.Doodle;
import zx.learn.core.BeanContainer;
import zx.learn.mvc.annotation.ResponseBody;
import zx.learn.mvc.bean.ModelAndView;
import zx.learn.util.CastUtil;
import zx.learn.util.ValidateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 这个类是用来执行 Controller 的
 */
@Slf4j
public class ResultRender {


    private BeanContainer beanContainer;

    public ResultRender() {
        beanContainer = BeanContainer.getInstance();
    }

    // 调用执行Controller 方法

    /**
     * 缺点 Controller中使用Request作为参数怎么进行注入
     */
    public void invokeController(HttpServletRequest req, HttpServletResponse resp, ControllerInfo controllerInfo) {
        //获取参数 字符串类型
        Map<String, String> requestParams = getRequestParams(req);
        List<Object> methodParams = instantiateMethodArgs(controllerInfo.getMethodParameter(), requestParams);

        Method invokeMethod = controllerInfo.getInvokeMethod();

        Object controllerObj = beanContainer.getBean(controllerInfo.getControllerClass());

        Object result = null;
        try {
            if (methodParams.isEmpty()) {
                result = invokeMethod.invoke(controllerObj);
            } else {
                result = invokeMethod.invoke(controllerObj, methodParams);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        returnResult(controllerInfo, result, req, resp);

    }



    // 将获取到的参数进行实例化 按照顺序
    private List<Object> instantiateMethodArgs(Map<String, Class<?>> methodParams, Map<String, String> requestParams) {
        return methodParams.keySet().stream().map(paramName -> {
            Class<?> type = methodParams.get(paramName);
            String requestValue = requestParams.get(paramName);
            Object value;
            if (null == requestValue) {
                value = CastUtil.primitiveNull(type);
            } else {
                value = CastUtil.convert(type, requestValue);
                // TODO: 实现非原生类的参数实例化  这里偷懒使用 fastjson 去实现了 不知道会不会出问题
            }
            return value;
        }).collect(Collectors.toList());
    }

    /**
     * 取出Request中的参数
     */
    private Map<String, String> getRequestParams(HttpServletRequest req) {
        Map<String, String> result = new HashMap<>();
        //GET和POST方法是这样获取请求参数的
        req.getParameterMap().forEach((paramName, paramsValues) -> {
            if (ValidateUtil.isNotEmpty(paramsValues)) {
                result.put(paramName, paramsValues[0]);
            }
        });
        return result;
    }
    private void returnResult(ControllerInfo controllerInfo, Object result, HttpServletRequest req, HttpServletResponse resp) {
        if (null == result) {
            return;
        }
        boolean isJson = controllerInfo.getInvokeMethod().isAnnotationPresent(ResponseBody.class);
        if (isJson) {
            // 设置响应头
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            // 向响应中写入数据
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(JSON.toJSONString(result));
                writer.flush();
            } catch (IOException e) {
                log.error("转发请求失败", e);
                // TODO: 异常统一处理，400等...
            }
        } else {
            String path;
            if (result instanceof ModelAndView) {
                ModelAndView mv = (ModelAndView) result;
                path = mv.getView();
                Map<String, Object> model = mv.getModel();
                if (ValidateUtil.isNotEmpty(model)) {
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                }
            } else if (result instanceof String) {
                path = (String) result;
            } else {
                throw new RuntimeException("返回类型不合法");
            }
            try {
//                req.getRequestDispatcher("/templates/" + path).forward(req, resp);
                req.getRequestDispatcher(Doodle.getConfiguration().getResourcePath() + path).forward(req, resp);
            } catch (Exception e) {
                log.error("转发请求失败", e);
                // TODO: 异常统一处理，400等...
            }
        }

    }

}
