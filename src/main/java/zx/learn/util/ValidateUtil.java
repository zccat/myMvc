package zx.learn.util;

/**
 * 验证相关工具类
 */
public final class ValidateUtil {

    /**
     * Object是否为null
     */
    public static boolean isEmpty(Object obj) {
        return obj == null;
    }

    /**
     * String是否为null或""
     */
    public static boolean isEmpty(String obj) {
        return (obj == null || "".equals(obj));
    }
    


    /**
     * Object是否不为null
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * String是否不为null或""
     */
    public static boolean isNotEmpty(String obj) {
        return !isEmpty(obj);
    }


}