package com.yadea.generator;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;

/**
 * Created by ZJ on 2020/7/22
 * comment:代码自动生成器
 */
public class GeneratorCodeConfig {
    public static void main(String[] args) {
        //构建一个代码生成器对象
        AutoGenerator mpg = new AutoGenerator();
        //配置策略
        //1.全局配置
        GlobalConfig gc = new GlobalConfig();
        System.getProperty("user.dir");
//        gc.setOutputDir()
        //执行
        mpg.execute();
    }

}
