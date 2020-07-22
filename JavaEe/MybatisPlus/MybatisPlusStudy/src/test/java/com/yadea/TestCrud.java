package com.yadea;

import com.yadea.mapper.UserMapper;
import com.yadea.pojo.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.text.Format;
import java.util.List;

/**
 * Created by ZJ on 2020/7/22
 * comment:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestCrud {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads(){
        //参数是一个wrapper，条件构造器，不用写null
        //查询全部用户
        List<Users> users = userMapper.selectList(null);
        for (Users user:users) {
            System.out.println(user);
        }
    }

    @Test
    public void insert(){
        Users users = new Users();
        users.setName("yadea2");
        users.setAge(3);
        users.setEmail("qq.com");
        int insert = userMapper.insert(users);
        System.out.println(insert);
    }

    @Test
    public void update(){
        Users users = new Users();
        users.setName("yadea");
        users.setAge(3);
        users.setEmail("qq1.com");
        int insert = userMapper.updateById(users);
        System.out.println(insert);
    }
}
