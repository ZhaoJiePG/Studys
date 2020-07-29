package com.yadea.prestoboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by ZJ on 2020/7/28
 * comment:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DmDimDay {
    private  String day_id;
    private  String month_id;
    private  String year_id;
}
