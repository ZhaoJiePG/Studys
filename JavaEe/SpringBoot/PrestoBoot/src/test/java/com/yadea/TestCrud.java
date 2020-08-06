package com.yadea;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yadea.entity.Users;
import com.yadea.mapper.mysql.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
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

    //测试乐观锁（多线程）
//    @Test
//    public void testOptimisticLocker(){
//        //线程1
//        Users users1 = userMapper.selectById(1L);
//        users1.setName("qqqq1");
//        users1.setEmail("qqqq1");
//
//        //模拟另外一个线程执行插队操作
//        Users users2 = userMapper.selectById(1L);
//        users2.setName("qqqq2");
//        users2.setEmail("qqqq2");
//        userMapper.updateById(users2);
//
//        //如果没有乐观锁就会覆盖插队线程的值
//        userMapper.updateById(users1);
//    }

    //查询用户
    @Test
    public void testSelectById(){
        Users users = userMapper.selectById(1l);
        System.out.println(users);
    }

    //批量查询
    @Test
    public void testSelectByIds(){
        List<Users> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        System.out.println(users);
    }

    //条件查询
    @Test
    public void testSelectByBatctIds(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name","Tom");
        List<Users> users = userMapper.selectByMap(map);
        System.out.println(users);
    }

    //分页查询
    @Test
    public void testSelectPages(){
        //参数1：当前页 参数2：页面大小
        Page<Users> page = new Page<Users>(1, 2);
        userMapper.selectPage(page, null);
        List<Users> records = page.getRecords();
        System.out.println(records);
        System.out.println(page.getTotal());
    }

    //逻辑删除（类似回收站）
    @Test
    public void testDelLogic(){
       userMapper.deleteById(1L);
    }
}
