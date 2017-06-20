package com.haofeng.apps.dst.bean;

import com.haofeng.apps.dst.utils.UNumber;

import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * Created by WIN10 on 2017/6/12.
 */

public class Order implements Serializable{
    public String m_order_no = "";//订单号
    public String m_member_id = "";//会员ID
    public String m_order_time = "";//下单时间
    public String m_car_no = "";//车牌号
    public String m_type = ""; //租车类型:1，时租 2，日租
    public String m_qc_time = "";//预计取车时间
    public String m_hc_time = "";//预计还车时间
    public String m_qc_real_time = "";//实际取车时间
    public String m_hc_real_time = "";//实际还车时间
    public String m_qc_store_id = "";//取车门店ID(#门店#)
    public String m_hc_store_id = "";//还车门店ID(#门店#)
    public String m_order_state = "";//订单状态（0押金未付,1待提车,2已提车,3已还车, 4.已付款 5已完结,6已取消）
    public String m_amount_confirm = "";//'用户费用确认 默认为0  1，等待确认 2，已确认
    public String m_is_safe = ""; //是否有购买保险 0,有 1，无
    public String m_is_iop = "";    //是否有不计免赔 0,有 1，无
    public String m_safe_amount = "";    //保险费用
    public String m_iop_amount = "";    //不计免赔费用
    public String m_chesun_amount = "";    //车损金额
    public String m_chesun_remark = "";    //车损备注
    public String m_rent_amount = "";    //预计租金费用
    public String m_way = "";    //下单渠道  1,APP 2,线下
    public String m_car_type_id = "";    //车型ID
    public String m_rent_real_amount = "";    //实际租金费用
    public String m_has_pay_amount = "";    //已付租金
    public String m_foregift_amount = "";    //车辆押金
    public String m_system_time = "";

    public Order parse(JSONObject jsonObj) {
        this.m_order_no = jsonObj.optString("order_no");
        this.m_member_id = jsonObj.optString("member_id");
        this.m_order_time = jsonObj.optString("order_time");
        this.m_car_no = jsonObj.optString("car_no");
        this.m_type = jsonObj.optString("type");
        this.m_qc_time = jsonObj.optString("qc_time");
        this.m_hc_time = jsonObj.optString("hc_time");
        this.m_qc_real_time = jsonObj.optString("qc_real_time");
        this.m_hc_real_time = jsonObj.optString("hc_real_time");
        this.m_qc_store_id = jsonObj.optString("qc_store_id");
        this.m_hc_store_id = jsonObj.optString("hc_store_id");
        this.m_order_state = jsonObj.optString("order_state");
        this.m_amount_confirm = jsonObj.optString("amount_confirm");
        this.m_is_safe = jsonObj.optString("is_safe");
        this.m_is_iop = jsonObj.optString("is_iop");
        this.m_safe_amount = jsonObj.optString("safe_amount");
        this.m_iop_amount = jsonObj.optString("iop_amount");
        this.m_chesun_amount = jsonObj.optString("chesun_amount");
        this.m_chesun_remark = jsonObj.optString("chesun_remark");
        this.m_rent_amount = jsonObj.optString("rent_amount");
        this.m_way = jsonObj.optString("way");
        this.m_car_type_id = jsonObj.optString("car_type_id");
        this.m_rent_real_amount = jsonObj.optString("rent_real_amount");
        this.m_has_pay_amount = jsonObj.optString("has_pay_amount");
        this.m_foregift_amount = jsonObj.optString("foregift_amount");
        this.m_system_time = jsonObj.optString("system_time");
        return this;
    }


    public String getOrderStateName() {
        String state = m_order_state;
        // 订单状态（0押金未付,1待提车,2已提车,3已还车, 4.已付款 5已完结,6已取消
        if("0".equals(state)) {
            return "押金未付";
        }

        else if("1".equals(state)) {
            return "待提车";
        }

        else if("2".equals(state)) {
            return "已提车";
        }

        else if("3".equals(state)) {
            return "已还车";
        }

        else if("4".equals(state)) {
            return "已付款";
        }

        else if("5".equals(state)) {
            return "已完结";
        }

        else if("6".equals(state)) {
            return "已取消";
        }

        return "--";
    }

    /**
     * 获取待支付租金
     * @return
     */
    public String getPayAmount() {
        float real_amount = UNumber.getFloat(m_rent_real_amount, 0);
        float has_pay_amount = UNumber.getFloat(m_has_pay_amount, 0);
        return UNumber.formatFloat(real_amount - has_pay_amount, "0.00");
    }

}
