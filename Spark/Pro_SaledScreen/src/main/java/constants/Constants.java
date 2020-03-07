package constants;

import java.io.Serializable;

public interface Constants extends Serializable {

    // 呼叫中心
    String BASIC_CALLL_UPDATE_SQL = "UPDATE basic_call SET CURR_DAY_COMPLAINT_COUNT = ?, " +
            "CURR_DAY_WORK_TIME_TELL_CONNECT_PERCENT = ?, " +
            "CURR_DAY_ALL_TIME_TELL_CONNECT_PERCENT = ?, " +
            "CURR_MONTH_COMPLAINT_COUNT = ?, " +
            "CURR_MONTH_ALL_TIME_TELL_CONNECT_PERCENT = ?, " +
            "CURR_DAY_REAL_TIME_CALLIN_COUNT = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_ONE = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_ONE = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TWO = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TWO = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_THREE = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_THREE = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_FOUR = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_FOUR = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_FIVE = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_FIVE = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_SIX = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_SIX = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_SEVEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_SEVEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_EIGHT = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_EIGHT = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_NINE = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_NINE = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_ELEVEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_ELEVEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TWELVE = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TWELVE = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_THIRTEEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_THIRTEEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_FOURTEEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_FOURTEEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_FIFTEEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_FIFTEEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_SIXTEEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_SIXTEEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_SEVENTEEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_SEVENTEEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_EIGHTEEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_EIGHTEEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_NINETEEN = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_NINETEEN = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TWENTY = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TWENTY = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TWENTY_ONE = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TWENTY_ONE = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TWENTY_TWO = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TWENTY_TWO = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TWENTY_THREE = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TWENTY_THREE = ?, " +
            "CURR_DAY_REAL_TIME_SWITCH_COUNT_TWENTY_FOUR = ?, " +
            "CURR_DAY_REAL_TIME_ANSWER_COUNT_TWENTY_FOUR = ?, " +
            "CURR_REAL_TIME_ADVISORY_COUNT = ?, " +
            "CURR_REAL_TIME_SALED_COUNT = ?, " +
            "CURR_REAL_TIME_COMPLAINT_COUNT = ?, " +
            "CURR_REAL_TIME_JOIN_COUNT = ?, " +
            "CURR_REAL_TIME_EMPLOYEE_CALLIN_COUNT = ?, " +
            "CURR_REAL_TIME_EMPLOYEE_CALLOUT_COUNT = ?, " +
            "CURR_REAL_TIME_CAR_OWER_SYSTEM_COUNT = 1, " +
            "UPDATE_TIME = ? " +
            " WHERE ID = 1";

    String STORE_DESTRIBUTE_INFO_SQL = "INSERT INTO store_site_and_sale_info SET TYPE = ?, NATION = ?, PROVINCE = ?, " +
            "CITY = ?, " +
            "TOWN = ?, " +
            "STORE_LEVEL = ?, " +
            "GROUPBY_OPTION = ?, " +
            "COMPUTE_COUNT = ?, " +
            "COMPUTE_DATE = ? ";

    String DELETE_PRE_STORE_DESTRIBUTE_INFO_SQL = "DELETE FROM store_site_and_sale_info WHERE type = ?";

    String YESTERYEAR_ERROR_TOP_10_FITTING = "select " +
            " repairment_name, " +
            " COUNT(repairment_name) as repair_item_count " +
            " from " +
            " dwd.dwd_prod_repair " +
            " where " +
            " sale_time between date_sub(current_date,365) and current_date " +
            " group by " +
            " repairment_name ORDER by repair_item_count desc LIMIT 10";

    String YESTERYEAR_ERROR_TOP_10_PROVINCE = "select a.repair_province, " +
            " sum(a.vinnumber) as repair_sum , " +
            " row_number() over (order by sum(a.vinnumber) desc) as rownumber " +
            " from " +
            " ( select " +
            "  repair_province, " +
            "  count(vinnumber) as vinnumber " +
            "  from dwd.dwd_prod_repair " +
            " where sale_time between date_sub(current_date, 365) and current_date " +
            " group by vinnumber, repair_province " +
            " having count(vinnumber) >= 2 ) a " +
            "group by a.repair_province limit 10 ";

    String YESTERYEAR_ERROR_PROVINCE_TOP_10_CARMODE_EXCLUDE = "select " +
            " ecc_name, " +
            " count(vinnumber) as repair_count " +
            " from dwd.dwd_prod_repair " +
            " where sale_time between date_sub(current_date, 365) and current_date " +
            " group by ecc_name " +
            " having " +
            " count(vinnumber) >= 2 ";

    String YESTERYEAR_ERROR_PROVINCE_TOP_10_CARMODE_INCLUDE = "select repair_province, " +
            " ecc_name, " +
            " count(vinnumber) as repair_count " +
            " from dwd.dwd_prod_repair " +
            " where sale_time between date_sub(current_date, 365) and current_date " +
            " group by repair_province, ecc_name " +
            " having " +
            " count(vinnumber) >= 2 ";

    String YESTERYEAR_ERROR_GROUP_BY_BASIC = "select count(*) as basic_count, basic_name, basic_code " +
            " from dwd.dwd_prod_repair " +
            " where sale_time between date_sub(current_date,365) and current_date " +
            "group by basic_name,basic_code ";

    String YESTERYEAR_ERROR_GROUP_BY_BASIC_CAR_MODE = " select  " +
            " basic_code, basic_name, ecc_name, " +
            " count(ecc_name) as ecc_name_count " +
            " from dwd.dwd_prod_repair " +
            " where sale_time between date_sub(current_date,365) and current_date " +
            " group by basic_name,basic_code, ecc_name ";

    String YESTERYEAR_ERROR_GROUP_BY_BASIC_CAR_AND_SABC = "select  " +
            " basic_code, basic_name, ecc_name, prod_level, " +
            " count(prod_level) as prod_level_count " +
            " from dwd.dwd_prod_repair " +
            " where sale_time between date_sub(current_date, 365) and current_date " +
            " group by basic_code,basic_name,ecc_name,prod_level ";

    String LASTMONTH_WORK_CARD_ALL_RECORD_AND_REPONSE_COUNT = "select count(createdts) as record_count, " +
            " sum(second(order_time) - SECOND(createdts)) as reponse_count " +
            " from dw.dw_aftersale_repairorder_info_dtl where createdts <> '' and order_time <> '' " +
            " and createdts > date_sub(from_unixtime(unix_timestamp(),'yyyy-MM-dd'),30) ";

    // 工单
    String WORK_CARD_ALL_RECORDS_OF_24_HOUR = " select k.event_hour, count(k.event_hour) as hour_count, sum(k.resp_hour_count) as resp_hour_count from " +
            " (select (second(order_time) - SECOND(createdts)) as resp_hour_count, HOUR(createdts) as event_hour " +
            " from dw.dw_aftersale_repairorder_info_dtl where createdts <> '' and order_time <> '') k " +
            " GROUP BY k.event_hour ";

    // 维修
    String REPAIR_PERSON_DESCRIBUTE = "select * from (" +
            " select DISTINCT(repair_peo.man_code),repair_peo.man_name " +
            " ,store_info.privince " +
            " ,store_info.city " +
            " ,store_info.town " +
            " from " +
            " dw.dw_repairstore_info_dtl repair_peo left JOIN dw.dw_store_info_dtl store_info  " +
            " on repair_peo.store_code = store_info.store_code) k where k.privince <> '' ";


    // 配件
    String SPARE_PART_SQL = "select order_detail.order_head_vbeln " +
        "	,order_detail.order_head_erdat " +
            "	,order_detail.order_head_kunnr " +
            "	,order_detail.order_head_vdatu " +
            "	,if(order_detail.order_head_province is null,'国外',order_detail.order_head_province) as order_head_province " +
            "	,order_detail.order_line_vbeln " +
            "	,order_detail.order_line_erdat " +
            "	,order_detail.order_line_posnr " +
            "	,order_detail.order_line_matnr " +
            "	,order_detail.order_line_arktx " +
            "	,order_detail.order_line_matkl " +
            "	,order_detail.order_line_kwmeng " +
            "	,order_detail.order_line_werks " +
            "	,invoice_detail.invoice_header_vbeln " +
            "	,invoice_detail.invoice_header_erdat " +
            "	,invoice_detail.invoice_header_kunag " +
            "	,invoice_detail.invoice_header_wadat_ist " +
            "	,invoice_detail.invoice_line_vbeln " +
            "	,invoice_detail.invoice_line_erdat " +
            "	,invoice_detail.invoice_line_posnr " +
            "	,invoice_detail.invoice_line_vgbel " +
            "	,invoice_detail.invoice_line_vgpos " +
            "	,invoice_detail.invoice_line_lfimg " +
            "	,invoice_detail.vbup_wbsta  " +
            "	,basic_factory.t188t_vtext " +
            "from  " +
            "(select vbak.vbeln as order_head_vbeln " +
            "		,vbak.erdat as order_head_erdat " +
            "		,vbak.kunnr as order_head_kunnr " +
            "		,vbak.vdatu as order_head_vdatu " +
            "		,dealer_info.province as order_head_province " +
            "		,vbap.vbeln as order_line_vbeln " +
            "		,vbap.erdat as order_line_erdat " +
            "		,vbap.posnr as order_line_posnr " +
            "		,vbap.matnr as order_line_matnr " +
            "		,vbap.arktx as order_line_arktx " +
            "		,vbap.matkl as order_line_matkl " +
            "		,vbap.kwmeng as order_line_kwmeng " +
            "		,vbap.werks as order_line_werks " +
            "	from ods.s06_sale_vbap vbap  " +
            "		left join ods.s06_sale_vbak vbak on vbap.vbeln = vbak.vbeln " +
            "		LEFT JOIN dict.dict_dealer_info dealer_info on vbak.kunnr = dealer_info.main_code " +
            "	WHERE substring(vbap.matkl,1,1) <> 'A'  " +
            "		and vbap.matkl <> 'B0700' and vbap.matkl <> 'YD001') order_detail " +
            "LEFT JOIN " +
            "(select likp.vbeln as invoice_header_vbeln " +
            "		,likp.erdat as invoice_header_erdat " +
            "		,likp.kunag as invoice_header_kunag " +
            "		,likp.wadat_ist as invoice_header_wadat_ist " +
            "		,lips.vbeln as invoice_line_vbeln " +
            "		,lips.erdat as invoice_line_erdat " +
            "		,lips.posnr as invoice_line_posnr " +
            "		,lips.vgbel as invoice_line_vgbel " +
            "		,lips.vgpos as invoice_line_vgpos " +
            "		,lips.lfimg as invoice_line_lfimg " +
            "		,vbup.wbsta as vbup_wbsta " +
            "	from ods.s06_delivery_lips lips  " +
            "		left join ods.s06_delivery_likp likp on lips.vbeln=likp.vbeln  " +
            "		left join ods.s06_sale_vbup vbup on lips.vbeln=vbup.vbeln and lips.posnr=vbup.posnr " +
            ") invoice_detail " +
            "on order_detail.order_line_vbeln = invoice_detail.invoice_line_vgbel " +
            "and order_detail.order_line_posnr = invoice_detail.invoice_line_vgpos " +
            "LEFT JOIN  " +
            "(select vbkd.vbeln as basic_vbeln,  " +
            "		t188t.konda as t188t_konda,  " +
            "		t188t.vtext as t188t_vtext  " +
            "		from ods.s06_sale_vbkd vbkd JOIN ods.s06_dict_t188t t188t  " +
            "		on vbkd.konda = t188t.konda where t188t.mandt = '800' and t188t.spras = '1') basic_factory  " +
            "ON order_detail.order_line_vbeln = basic_factory.basic_vbeln  " +
            "where order_detail.order_head_vbeln is not null  ";

}
