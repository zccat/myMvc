package zx.learn.util;

import com.alibaba.fastjson.JSON;

public class CastUtil {
    public static Object primitiveNull(Class<?> type) {
        return null;
    }

    public static Object convert(Class<?> type, String requestValue) {
        return JSON.parseObject(requestValue, type);
    }
}
