package com.haofeng.apps.dst.bean;

import com.haofeng.apps.dst.utils.UNumber;

import org.json.JSONObject;

import java.io.Serializable;

/**
 *
 * Created by WIN10 on 2017/6/12.
 */

public class CarDetail implements Serializable{
     public String m_id  = "";
     public String m_brand_name = "";   //品牌
     public String m_car_model_name  = "";//车型
     public String m_car_type_name  = "";//类型
     public String m_check_mass  =  "" ;//车载重
     public String m_shaft_distance  = "";//轴距
     public String m_cubage  = "";//货箱容积
     public String m_endurance_mileage  = "";//续航
     public String m_outside_long  = "";//长
     public String m_outside_width = "";//宽
     public String m_outside_height = "";//高
     public String m_time_price  = "";//时租价格
     public String m_day_price  = "";//日租价格
     public String m_starting_price = "";//起步价
     public String m_imgs  =  "";//轮播图url地址
     public String m_car_full_img = ""; // 封面图
     public String m_deposit = "";   //车辆押金
     public String m_wz_deposit  = ""; //违章押金
     public String m_insurance_expense = ""; // 保险
     public String m_insurance_bjmp = ""; // 不计免赔

    public CarDetail parse(JSONObject jsonObj) {
        this.m_id = jsonObj.optString("id");
        this.m_brand_name = jsonObj.optString("brand_name");
        this.m_car_model_name = jsonObj.optString("car_model_name");
        this.m_car_type_name = jsonObj.optString("car_type_name");
        this.m_check_mass = jsonObj.optString("check_mass");
        this.m_shaft_distance = jsonObj.optString("shaft_distance");
        this.m_cubage = jsonObj.optString("cubage");
        this.m_endurance_mileage = jsonObj.optString("endurance_mileage");
        this.m_outside_long = jsonObj.optString("outside_long");
        this.m_outside_width = jsonObj.optString("outside_width");
        this.m_outside_height = jsonObj.optString("outside_height");
        this.m_time_price = jsonObj.optString("time_price");
        this.m_day_price = jsonObj.optString("day_price");
        this.m_starting_price = jsonObj.optString("starting_price");
        this.m_imgs = jsonObj.optString("imgs");
        this.m_car_full_img = jsonObj.optString("car_full_img");
        this.m_deposit = jsonObj.optString("deposit");
        this.m_wz_deposit = jsonObj.optString("wz_deposit");
        this.m_insurance_expense = jsonObj.optString("insurance_expense");
        this.m_insurance_bjmp = jsonObj.optString("insurance_bjmp");
        if("".equals(m_insurance_expense)) {
            m_insurance_expense = "0.00";
        }

        if("".equals(m_insurance_bjmp)) {
            m_insurance_bjmp = "0.00";
        }
        return this;
    }

    /** 获取总押金(包括汽车押金 和 违章押金)*/
    public String getDeposit() {
        float deposit = UNumber.getFloat(m_deposit, 0);
        float wz_deposit = UNumber.getFloat(m_wz_deposit, 0);
        return String.valueOf(deposit + wz_deposit);
    }
}
