package com.haofeng.apps.dst.bean;

import com.haofeng.apps.dst.utils.UNumber;

import org.json.JSONObject;

/**
 * Created by WIN10 on 2017/6/17.
 */

public class RentConfig {
    public String m_start_time = "06:00";  // 营业开始时间
    public String m_end_time = "23:00";  // 营业结束时间
    public String m_h_longest_rent = "";           //最长出租（时）
    public String m_h_timeout_settlement = "";     //超时结算周期（时）
    public String m_d_longest_rent = "";           //最长出租时间(天)
    public String m_d_normal_settlement = "";     //正常结算周期(天)
    public String m_d_timeout_settlement = "";   //超时结算周期(时)

    public int mStartHour = 6;
    public int mStartMinute = 0;
    public int mEndHour = 23;
    public int mEndMinute = 0;

    public RentConfig parse(JSONObject jsonObj) {
        this.m_start_time = jsonObj.optString("start_time");
        this.m_end_time = jsonObj.optString("end_time");
        this.m_h_longest_rent = jsonObj.optString("h_longest_rent");
        this.m_h_timeout_settlement = jsonObj.optString("h_timeout_settlement");
        this.m_d_longest_rent = jsonObj.optString("d_longest_rent");
        this.m_d_normal_settlement = jsonObj.optString("d_normal_settlement");
        this.m_d_timeout_settlement = jsonObj.optString("d_timeout_settlement");

        String[] startTime = m_start_time.split(":");
        String[] endTime = m_end_time.split(":");
        if(startTime.length == 2) {
            mStartHour = UNumber.getInt(startTime[0], 6);
            mStartMinute = UNumber.getInt(startTime[1], 0);
        }

        if(endTime.length == 2) {
            mEndHour = UNumber.getInt(endTime[0], 23);
            mEndMinute = UNumber.getInt(endTime[1], 0);
        }
        return this;
    }
}
