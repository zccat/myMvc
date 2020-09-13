// DoodleServiceImpl
package zx.learn.bean;


import zx.learn.ioc.annotation.Service;

@Service
public class DoodleServiceImpl implements DoodleService{
    @Override
    public String helloWord() {
        return "hello word";
    }
}