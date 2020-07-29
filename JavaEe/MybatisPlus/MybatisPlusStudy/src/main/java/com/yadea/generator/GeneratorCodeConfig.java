package com.yadea.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import net.sf.jsqlparser.schema.Table;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by ZJ on 2020/7/22
 * comment:代码自动生成器
 */
public class GeneratorCodeConfig {
    public static void main(String[] args) {

//        System.out.println("从键盘录入");
//        Scanner sc = new Scanner(System.in);
//        String name = sc.nextLine();

        //构建一个代码生成器对象
        AutoGenerator mpg = new AutoGenerator();
        //配置策略
        //1.全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath+"\\JavaEe\\MybatisPlus\\MybatisPlusStudy\\src\\main\\java");
        gc.setAuthor("admin");
        //是否打开文件
        gc.setOpen(false);
        //是否覆盖
        gc.setFileOverride(false);
        //去Service的I前缀
        gc.setServiceName("%sService");
        gc.setIdType(IdType.ID_WORKER);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);

        //2.设置配置和数据源
        mpg.setGlobalConfig(gc);
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://10.149.1.154:3306/test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("root");
        dsc.setDbType(DbType.MYSQL);
        mpg.setDataSource(dsc);

        //3.包的配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("blog");
        pc.setParent("com.yadea");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
        pc.setController("controller");
        mpg.setPackageInfo(pc);

        //4.策略配置(驼峰命名)
        StrategyConfig st = new StrategyConfig();
        //设置要映射的表名
        st.setInclude("users");
        st.setNaming(NamingStrategy.underline_to_camel);
        st.setColumnNaming(NamingStrategy.underline_to_camel);
//        st.setSuperEntityClass("");
        //自动lombok
        st.setEntityLombokModel(true);
        st.setLogicDeleteFieldName("deleted");
        //自动填充策略
        TableFill gmtCreate = new TableFill("gmt_create", FieldFill.INSERT);
        TableFill gmtModified = new TableFill("gmt_modified", FieldFill.INSERT_UPDATE);
        ArrayList<TableFill> al = new ArrayList<TableFill>();
        al.add(gmtCreate);
        al.add(gmtModified);
        st.setTableFillList(al);
        //乐观锁配置
        st.setVersionFieldName("version");
        //restful的controller风格
        st.setRestControllerStyle(true);
        mpg.setStrategy(st);

        //执行
        mpg.execute();
    }

}
