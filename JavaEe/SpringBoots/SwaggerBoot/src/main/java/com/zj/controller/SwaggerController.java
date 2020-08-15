package com.zj.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zj.pojo.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * Created by ZJ on 2020/8/6
 * comment:
 */
@Api("Swagger测试亲求")
@RestController
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

    @PostMapping(value = "/hello")
    @ApiOperation("测试hello")
    public String hello(@RequestBody @ApiParam(name = "name",value = "名称",required = true) String name){
        return name + ":hello";
    }


    @PostMapping(value = "/user")
    public User user(){
        return new User();
    }
}
