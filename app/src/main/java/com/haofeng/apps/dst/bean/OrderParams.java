package com.haofeng.apps.dst.bean;

import java.io.Serializable;

/**
 * Created by WIN10 on 2017/6/13.
 */

public class OrderParams implements Serializable{
    public String m_type = "";  // 1，时租 2，日租
    public String m_qc_time = "";  // 预计取车时间
    public String m_hc_time = "";  // 预计还车时间
    public String m_store_id = ""; // 还车门店ID、取车门店ID
    public String m_is_safe = "0";  // 是否勾选保险 0勾选 1不勾选
    public String m_is_iop = "0";   // 是否勾选不计免赔 0勾选 1不勾选
    public String m_car_type_id = "";  // 车型ID
    public String m_way = "";  // 下单渠道


    public String m_yu_ji_zu_jing = ""; // 预计租金， （非下单参数）
}
