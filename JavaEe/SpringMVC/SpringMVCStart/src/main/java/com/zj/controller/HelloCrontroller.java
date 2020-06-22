package com.zj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by ZJ on 2020/6/16
 * comment:控制器类
 */

@Controller
//@RequestMapping(path = "/user")
public class HelloCrontroller {

    @RequestMapping(path = "/hello")
    public String sayHello(){
        System.out.println("hello springmvc");
        return "success";
    }
    @RequestMapping(path = "/testRequestMapping"
            ,method = {RequestMethod.GET}
//            ,params = {"username=zj"}
//            ,headers = {"Accept"}
            )
    public String testRequestMapping(){
        System.out.println("测试 testRequestMapping");

        return "success";
    }
}
