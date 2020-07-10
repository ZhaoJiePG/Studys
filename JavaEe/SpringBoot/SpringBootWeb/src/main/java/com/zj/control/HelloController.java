package com.zj.control;

import com.alibaba.fastjson.JSON;
import com.zj.domain.User;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by ZJ on 2020/7/7
 * comment:
 */

@Controller
public class HelloController {

    @ResponseBody
    @RequestMapping(value = "/hello",method={RequestMethod.GET}, produces={"application/json;charset=UTF-8"})
    public String hello(){
        User user = new User();
        user.setName("YYY");
        user.setAge(18);
        return JSON.toJSONString(user);
    }

    @RequestMapping("/success")
    public String success(Map<String,Object> map){
        map.put("hello","你好");
        map.put("users", Arrays.asList("zhangsan","lisi","wangwu"));
        return "success";
    }

    //每次进来都要先访问这个
    @RequestMapping({"/","/index.html"})
    public String index(){
        return "index";
    }
}
