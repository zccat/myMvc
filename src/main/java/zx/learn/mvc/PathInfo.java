package zx.learn.mvc;

import lombok.AllArgsConstructor;
import lombok.Data;
import zx.learn.mvc.bean.RequestType;

import java.util.Objects;

@Data
@AllArgsConstructor
public class PathInfo {

    private String url;

    private RequestType requestMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PathInfo)) return false;
        PathInfo pathInfo = (PathInfo) o;
        return Objects.equals(url, pathInfo.url) &&
                Objects.equals(requestMethod, pathInfo.requestMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, requestMethod);
    }



}
