package com.yadea.controller;

import com.alibaba.fastjson.JSON;
import com.yadea.mapper.mysql.UserMapper;
import com.yadea.entity.Users;
import com.yadea.util.ResultCode;
import com.yadea.util.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by ZJ on 2020/7/22
 * comment:
 */
@Controller
@ResponseBody
public class UserController {

    @GetMapping("/hello")
    public String Hello(@RequestParam(value = "name") String str){
        return str+"hello";
    }

    @Autowired
    UserMapper userMapper;

    @GetMapping("/getUser")
    public Users getUser(){
        return userMapper.selectById(1l);
    }

    @GetMapping("/findAllUser")
    public String findAllUser(){
        List<Users> allUser = userMapper.findAllUser();
        ResultInfo resultInfo = new ResultInfo(ResultCode.SUCCESS, allUser);
        return JSON.toJSONString(resultInfo);
    }

    @PostMapping("/findAllUser")
    public String getSubtribute(){
        List<Users> allUser = userMapper.findAllUser();
        ResultInfo resultInfo = new ResultInfo(ResultCode.SUCCESS, allUser);
        return JSON.toJSONString(resultInfo);
    }
}
