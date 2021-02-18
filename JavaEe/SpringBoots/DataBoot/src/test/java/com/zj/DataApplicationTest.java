package com.zj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;


/**
 * Created by ZJ on 2020/8/18
 * comment:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataApplicationTest {

    DataSource dataSource;

    @Test
    public void test1(){
        //查看一下默认的数据源
        System.out.println(dataSource.getClass());
    }
}
