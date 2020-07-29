package com.yadea.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yadea.pojo.Users;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * Created by ZJ on 2020/7/22
 * comment:
 */

//在对应的mapper上面实现基本的接口basemapper
//代表持久层
@Mapper
@Repository
public interface UserMapper extends BaseMapper<Users> {
    //所有crud操作已完成

}
