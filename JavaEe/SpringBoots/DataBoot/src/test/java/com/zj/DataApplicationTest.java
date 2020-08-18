package com.zj;

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

    @Autowired
    DataSource dataSource;
}
