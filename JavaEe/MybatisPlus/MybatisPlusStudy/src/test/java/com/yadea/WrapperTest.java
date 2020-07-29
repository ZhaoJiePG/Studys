package com.yadea;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yadea.mapper.UserMapper;
import com.yadea.mapper.UserMapper;
import com.yadea.pojo.Users;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by ZJ on 2020/7/22
 * comment:wrapper复杂查询
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class WrapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testWrapper1(){
        //测试查询name不为空的、邮箱不为空、年龄大于等于12的
        QueryWrapper<Users> wrapper = new QueryWrapper<Users>();
        wrapper.isNotNull("name")
                .isNotNull("email")
                .ge("age",12);
        List<Users> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

    @Test
    public void testWrapper2(){
        //查询名字
        QueryWrapper<Users> wrapper = new QueryWrapper<Users>();
        wrapper.eq("name","qqqq");
        Users users = userMapper.selectOne(wrapper);
        System.out.println(users);
    }

    @Test
    public void testWrapper3(){
        //查询年龄在20岁-30岁之间的
        QueryWrapper<Users> wrapper = new QueryWrapper<Users>();
        wrapper.between("age",20,30);
        Integer integer = userMapper.selectCount(wrapper);
        System.out.println(integer);
    }

    @Test
    public void testWrapper4(){
        //查询年龄在20岁-30岁之间的
        QueryWrapper<Users> wrapper = new QueryWrapper<Users>();
        wrapper.notLike("name","e")
                .likeRight("email","t");
        Integer integer = userMapper.selectCount(wrapper);
        System.out.println(integer);
    }

    @Test
    public void testWrapper5(){
        //查询年龄在20岁-30岁之间的
        QueryWrapper<Users> wrapper = new QueryWrapper<Users>();
        wrapper.inSql("id","select id from user where id<3");
        List<Object> objects = userMapper.selectObjs(wrapper);
        System.out.println(objects);
    }

    //测试6多表查询
    @Test
    public void testWrapper6(){
        //通过id进行排序
        QueryWrapper<Users> wrapper = new QueryWrapper<Users>();
        wrapper.orderByDesc("id");
        List<Users> users = userMapper.selectList(wrapper);
        System.out.println(users);
    }

}
