package zx.learn.mvc.bean;

import javax.servlet.http.HttpServletRequest;

public enum RequestType {
    GET("GET"), POST("POST");

    String value;

    RequestType(String value) {
        this.value = value;
    }

    public static RequestType getType(HttpServletRequest req) {
        if (req.getMethod().equals("GET")) {
            return GET;
        } else if (req.getMethod().equals("POST")) {
            return POST;
        }
        return GET;
    }
}