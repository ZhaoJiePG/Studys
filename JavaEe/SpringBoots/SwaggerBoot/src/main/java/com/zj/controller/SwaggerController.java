package com.zj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by ZJ on 2020/8/6
 * comment:
 */
@Controller
@ResponseBody
@RequestMapping("/zj")
public class SwaggerController {

    @PostMapping(value = "/testMessage",produces={"application/json;charset=UTF-8"})
    public JSONObject testMessage(@RequestBody JSONObject messageParamn){
        System.out.println(messageParamn);
        HashMap messageMap = new HashMap<String,Object>();
        messageMap.put("errcode",0);
        messageMap.put("errmsg","success");
        JSONObject messegeJSONObject = new JSONObject();
        messegeJSONObject.put("code", JSON.toJSON(messageMap));
        return messegeJSONObject;
    }

    @GetMapping(value = "/hello")
    public String hello(){
        return "hello";
    }
}
