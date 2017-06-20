package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

/**
 *
 * Created by WIN10 on 2017/6/12.
 */

public class OrderCreate {
    public String m_order_no = "";

    public OrderCreate parse(JSONObject jsonObj) {
        this.m_order_no = jsonObj.optString("order_no");
        return this;
    }
}
