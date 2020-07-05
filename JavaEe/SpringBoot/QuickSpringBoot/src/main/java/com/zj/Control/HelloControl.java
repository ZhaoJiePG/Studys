package com.zj.Control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ZJ on 2020/5/10
 * comment:
 */

//这个类的所有方法返回的数据直接写给浏览器(如果是对象还可以返回为json对象)
//@ResponseBody
//@Controller
@RestController
public class HelloControl {

    @RequestMapping("/hello")
    public String hello(){
        return "hello world quickly!";
    }
}
