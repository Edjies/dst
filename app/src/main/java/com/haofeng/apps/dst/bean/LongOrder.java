package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

/**
 * Created by WIN10 on 2017/6/19.
 */

public class LongOrder {
    public String  m_apply_id = ""; //长租申请id
    public String  m_apply_no = ""; //需求单号
    public String  m_order_time = ""; //下单时间
    public String  m_total_num = ""; //申请数量 
    public String  m_es_take_car_time = ""; //预计提车时间
    public String  m_sales = ""; //服务专员
    public String  m_sales_mobile = ""; //服务专员手机号
    public String  m_status = ""; //响应状态，0等待响应，1已经响应指派了服务专员

    public LongOrder parse(JSONObject jsonObj) {
        this.m_apply_id = jsonObj.optString("apply_id");
        this.m_apply_no = jsonObj.optString("apply_no");
        this.m_order_time = jsonObj.optString("order_time");
        this.m_total_num = jsonObj.optString("total_num");
        this.m_es_take_car_time = jsonObj.optString("es_take_car_time");
        this.m_sales =jsonObj.optString("sales");
        this.m_sales_mobile = jsonObj.optString("sales_mobile");
        this.m_status = jsonObj.optString("status");
        return this;
    }
}
