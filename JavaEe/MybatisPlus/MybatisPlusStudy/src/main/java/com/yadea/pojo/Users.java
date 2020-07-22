package com.yadea.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ZJ on 2020/7/22
 * comment:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "users",schema = "test")
public class Users {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
