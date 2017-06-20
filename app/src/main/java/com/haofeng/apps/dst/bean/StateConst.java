package com.haofeng.apps.dst.bean;

/**
 * 协议中定义的状态常量
 * Created by WIN10 on 2017/6/13.
 */

public class StateConst {
    public final static String ORDER_STATE_0 = "0"; // 押金未付
    public final static String ORDER_STATE_1 = "1"; // 待提车
    public final static String ORDER_STATE_2 = "2"; // 已提车
    public final static String ORDER_STATE_3 = "3"; // 已还车
    public final static String ORDER_STATE_4 = "4"; // 已付款
    public final static String ORDER_STATE_5 = "5"; // 已完结
    public final static String ORDER_STATE_6 = "6"; // 已取消


    public final static String ORDER_TYPE_HOUR = "1"; // 时租
    public final static String ORDER_TYPE_DAY = "2"; // 日租

    public final static String PAY_WAY_WX = "wechat"; // 微信支付
    public final static String PAY_WAY_ZFB = "alipay"; // 支付宝

    /* 租车 费用确认 状态*/
    public final static String ORDER_AMOUNT_CONFIRM_0 = "0"; // 默认状态
    public final static String ORDER_AMOUNT_CONFIRM_1 = "1"; // 等待确认状态
    public final static String ORDER_AMOUNT_CONFIRM_2 = "2"; // 已确认状态
}
