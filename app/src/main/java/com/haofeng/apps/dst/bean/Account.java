package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

/**
 *
 * Created by WIN10 on 2017/6/13.
 */

public class Account {
    public String m_id = "";
    public String m_code = "";
    public String m_client = "";
    public String m_mobile = "";
    public String m_password = "";
    public String m_sex = "";
    public String m_email = "";
    public String m_mark = "";
    public String m_app_ver = "";
    public String m_systime = "";
    public String m_sysuser = "";
    public String m_is_del = "";
    public String m_money_acount = "";
    public String m_shot_message_code = "";
    public String m_sm_reqtime = "";
    public String m_sm_number = "";
    public String m_score = "";
    public String m_history_score = "";
    public String m_id_card_url = "";
    public String m_driving_card_url = "";
    public String m_id_card_auth = "";
    public String m_driving_card_auth = "";
    public String m_type = "";
    public String m_category = "";
    public String m_enterprise_name = "";
    public String m_id_card_url1;
    public String m_business_license_url = "";
    public String m_business_license_auth = "";
    public String m_work_units = "";
    public String m_nickname = "";
    public String m_foregift_acount = "";
    public String m_name = "";
    public String m_add_time = "";
    public String m_amount = "";
    public String m_order_total = "";
    public String m_ongoing_count = "";

    public Account parse(JSONObject jsonObj) {
        this.m_id = jsonObj.optString("id");
        this.m_code = jsonObj.optString("code");
        this.m_client = jsonObj.optString("client");
        this.m_mobile = jsonObj.optString("mobile");
        this.m_password = jsonObj.optString("password");
        this.m_sex = jsonObj.optString("sex");
        this.m_email = jsonObj.optString("email");
        this.m_mark = jsonObj.optString("mark");
        this.m_app_ver = jsonObj.optString("app_ver");
        this.m_systime = jsonObj.optString("systime");
        this.m_sysuser = jsonObj.optString("sysuser");
        this.m_is_del = jsonObj.optString("is_del");
        this.m_money_acount = jsonObj.optString("money_acount");
        this.m_shot_message_code = jsonObj.optString("shot_message_code");
        this.m_sm_reqtime = jsonObj.optString("sm_reqtime");
        this.m_sm_number = jsonObj.optString("sm_number");
        this.m_score = jsonObj.optString("score");
        this.m_history_score = jsonObj.optString("history_score");
        this.m_id_card_url = jsonObj.optString("id_card_url");
        this.m_driving_card_url = jsonObj.optString("driving_card_url");
        this.m_id_card_auth = jsonObj.optString("id_card_auth");
        this.m_driving_card_auth = jsonObj.optString("driving_card_auth");
        this.m_type = jsonObj.optString("type");
        this.m_category = jsonObj.optString("category");
        this.m_enterprise_name = jsonObj.optString("enterprise_name");
        this.m_id_card_url1 = jsonObj.optString("id_card_url1");
        this.m_business_license_url = jsonObj.optString("business_license_url");
        this.m_business_license_auth = jsonObj.optString("business_license_auth");
        this.m_work_units = jsonObj.optString("work_units");
        this.m_nickname = jsonObj.optString("nickname");
        this.m_foregift_acount = jsonObj.optString("foregift_acount");
        this.m_name = jsonObj.optString("name");
        this.m_add_time = jsonObj.optString("add_time");
        this.m_amount = jsonObj.optString("amount");
        this.m_order_total = jsonObj.optString("order_total");
        this.m_ongoing_count = jsonObj.optString("ongoing_count");
        return this;
    }
    //{"errcode":0,"msg":"请求成功！","data":{"id":"1550","code":"VIP20170608164218295","client":"chengyan","mobile":"18682308371",
    // "password":"18fb4c68d899f462e8f2c965bf23f7c0","sex":"0","email":"","mark":"","app_ver":"android1.1.1","systime":"1496911338",
    // "sysuser":"","is_del":"0","money_acount":"0.00","shot_message_code":"","sm_reqtime":"0","sm_number":"","score":"0",
    // "history_score":"0","id_card_url":null,"driving_card_url":"","id_card_auth":"1","driving_card_auth":"1","type":"4","category":"1",
    // "enterprise_name":"","id_card_url1":null,"business_license_url":null,"business_license_auth":"3","work_units":"","nickname":"",
    // "foregift_acount":"0.00","name":"chengyan","add_time":"1496911338","amount":"0.00","order_total":"0","ongoing_count":"0"}}


}
