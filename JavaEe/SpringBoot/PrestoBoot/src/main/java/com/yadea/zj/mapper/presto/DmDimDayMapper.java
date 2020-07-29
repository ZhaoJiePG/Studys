package com.yadea.zj.mapper.presto;

import com.yadea.prestoboot.entity.DmDimDay;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ZJ on 2020/7/28
 * comment:
 */
@Mapper
@Repository
public interface DmDimDayMapper {

    List<DmDimDay> getDayInfo();
}
