package com.haofeng.apps.dst.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by WIN10 on 2017/6/19.
 */

public class LongOrderDetail {
    public String m_apply_no = ""; //需求单号
    public String m_order_time = ""; //下单时间
    public String m_status = ""; //响应状态，0等待响应，1已经响应指派了服务专员
    public String  m_sales = ""; //服务专员
    public String  m_sales_mobile = ""; //服务专员手机号
    public String m_city = ""; //取车城市
    public String m_es_take_car_time = ""; //预计提车时间



    public String m_company_name = ""; //企业名称
    public String m_contact_name = ""; //联系人姓名
    public String m_contact_mobile = ""; //联系人手机
    public String m_contact_email = ""; // 联系人邮箱


    public ArrayList<CarModel> m_car_models = new ArrayList<>();

    public LongOrderDetail parse(JSONObject jsonObj) {
        this.m_apply_no = jsonObj.optString("apply_no");
        this.m_order_time = jsonObj.optString("order_time");
        this.m_status = jsonObj.optString("status");
        this.m_sales = jsonObj.optString("sales");
        this.m_sales_mobile = jsonObj.optString("sales_mobile");
        this.m_city = jsonObj.optString("city");
        this.m_es_take_car_time = jsonObj.optString("es_take_car_time");
        // 解析车型列表
        try {
            JSONArray carModelObj = new JSONArray(jsonObj.optString("car_models"));
            if(carModelObj != null) {
                for(int i = 0; i < carModelObj.length(); i++) {
                    JSONObject itemObj = carModelObj.optJSONObject(i);
                    if(itemObj != null) {
                        this.m_car_models.add(new CarModel().parse(itemObj));
                    }
                }
            }
        }catch (Exception e) {

        }

        this.m_company_name = jsonObj.optString("company_name");
        this.m_contact_name = jsonObj.optString("contact_name");
        this.m_contact_mobile = jsonObj.optString("contact_mobile");
        this.m_contact_email = jsonObj.optString("contact_email");
        return this;
    }

    public static class CarModel{
        public String m_car_type_id= "";//车辆型号id
        public String m_num = "";//需求数量
        public String m_use_time= "";//租车时长

        public CarModel parse(JSONObject jsonObj) {
            this.m_car_type_id = jsonObj.optString("car_type_id");
            this.m_num = jsonObj.optString("num");
            this.m_use_time = jsonObj.optString("use_time");
            return this;
        }
    }
}
