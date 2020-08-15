package com.zj.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by ZJ on 2020/8/13
 * comment:
 */

@ApiModel("用户实体类")
public class User {
    @ApiModelProperty("用户名")
    private String password;


    @ApiModelProperty("密码")
    private String username;
}
