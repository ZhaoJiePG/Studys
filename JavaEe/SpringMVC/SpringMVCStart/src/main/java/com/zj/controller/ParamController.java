package com.zj.controller;

import com.zj.domain.Account;
import com.zj.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ZJ on 2020/6/22
 * comment:请求参数绑定
 */
@Controller
@RequestMapping("/param")
public class ParamController {

    /**
     * 请求参数绑定如入门
     * @return
     */
    @RequestMapping("/testParam")
    public String testParam(String username,String password){
        System.out.println("执行了请求参数绑定如入门");
        System.out.println("用户名："+username+"===密码："+password);
        return "success";
    }
    /**
     * 请求参数绑定把数据封装到JavaBean的类中
     * @return
     */
    @RequestMapping("/saveAccount")
    public String SaveAccount(Account account){
        System.out.println(account.toString());
        return "success";
    }
    /**
     * 自定义类型转换器
     * @return
     */
    @RequestMapping("/saveUser")
    public String SaveUser(User user){
        System.out.println(user.toString());
        return "success";
    }


}
