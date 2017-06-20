package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

/**
 *
 * Created by WIN10 on 2017/6/12.
 */

public class OrderPay {
    public String m_trade_no = ""; // 用户网站订单号
    public String m_total_fee = ""; // 金额
    public String m_pay_way = ""; // 支付方式
    public String m_notify_url = ""; //

    public OrderPay parse(JSONObject jsonObj) {
        this.m_trade_no = jsonObj.optString("trade_no");
        this.m_total_fee = jsonObj.optString("total_fee");
        this.m_pay_way = jsonObj.optString("pay_way");
        this.m_notify_url = jsonObj.optString("notify_url");
        return this;
    }
}
