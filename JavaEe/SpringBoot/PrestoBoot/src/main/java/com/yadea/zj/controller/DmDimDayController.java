package com.yadea.zj.controller;

import com.yadea.prestoboot.entity.DmDimDay;
import com.yadea.zj.mapper.presto.DmDimDayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by ZJ on 2020/7/28
 * comment:
 */
@RestController
public class DmDimDayController {

    @Autowired
    DmDimDayMapper dmDimDayMapper;

    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/getYearMonthDay")
    public List<DmDimDay> getYearMonthDay(){
        List<DmDimDay> totalNum = this.dmDimDayMapper.getDayInfo();
        return totalNum;
    }
}
