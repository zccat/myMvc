package com.simple.controller;

import com.simple.service.TestService;
import zx.learn.ioc.annotation.Autowired;
import zx.learn.ioc.annotation.Controller;
import zx.learn.mvc.annotation.RequestMapping;
import zx.learn.mvc.annotation.ResponseBody;

@Controller
@RequestMapping
public class DoodleController {

    @Autowired
    TestService testService;


    @RequestMapping
    @ResponseBody
    public String hello() {
        return testService.sayHello();
    }


}