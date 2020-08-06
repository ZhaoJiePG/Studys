package com.yadea.util;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by ZJ on 2020/7/29
 * comment:
 */
@Data
public class ResultInfo implements Serializable {
    private Integer code;

    private String message;

    private Object data;

    public ResultInfo(ResultCode resultCode, Object data) {
        this.code = resultCode.code;
        this.message = resultCode.message;
        this.data = data;
    }
//
//    //返回成功
//    public static ResultInfo success(Object obj){
//        ResultInfo resultInfo = new ResultInfo();
//        resultInfo.setCode(ResultCode.SUCCESS);
//        resultInfo.setData(obj);
//        return resultInfo;
//    }
}
