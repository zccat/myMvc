package zx.learn.mvc.bean;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {

    private String view;

    private Map<String, Object> model = new HashMap<>();

    public String getView() {
        return view;
    }

    public ModelAndView setView(String view) {
        this.view = view;
        return this;
    }

    public ModelAndView put(String k, Object v) {
        model.put(k, v);
        return this;
    }

    public ModelAndView addObject(String k, Object v) {
        model.put(k, v);
        return this;
    }

    public ModelAndView addAllObject(Map<String,?> attrMap) {
        model.putAll(attrMap);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
