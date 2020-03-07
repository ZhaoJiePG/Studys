package com.yadea;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;
import sun.security.provider.MD5;

import java.math.BigDecimal;

public class Test1 {
    public static void main(String[] args) {
        System.out.println(DigestUtils.md5Hex("yadea"));

        System.out.println(convertServerLatOrLon(Double.valueOf("216159795")));
        System.out.println(convertServerLatOrLon(Double.valueOf("50028123")));

    }

    public static double convertServerLatOrLon(double latOrLon) {
        double val = latOrLon/30000;
        val = val/60;
        BigDecimal bigDecimal = new BigDecimal(val);
        return bigDecimal.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
