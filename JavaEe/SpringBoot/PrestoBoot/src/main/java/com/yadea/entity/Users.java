package com.yadea.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.util.Date;

/**
 * Created by ZJ on 2020/7/22
 * comment:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "users",schema = "test")
public class Users {
    //对应数据库中的主键（uuid、自增id、雪花算法、redis、zookeeper）
    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private String name;
    private Integer age;
    private String email;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //乐观锁注释
    @Version
    private Integer version;

    //逻辑删除注解
    @TableLogic
    private Integer deleted;
}
