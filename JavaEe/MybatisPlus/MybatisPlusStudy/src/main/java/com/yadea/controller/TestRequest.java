package com.yadea.controller;

import org.springframework.core.ReactiveTypeDescriptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ZJ on 2020/7/22
 * comment:
 */
@Controller
@ResponseBody
public class TestRequest {

    @RequestMapping("/hello")
    public String Hello(@RequestParam(value = "name") String str){
        return str+"hello";
    }
}
