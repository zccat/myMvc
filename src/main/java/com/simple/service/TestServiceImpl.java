package com.simple.service;


import zx.learn.ioc.annotation.Service;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String sayHello() {
        return "hello";
    }
}
