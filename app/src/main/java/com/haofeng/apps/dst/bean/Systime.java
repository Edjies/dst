package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by WIN10 on 2017/6/17.
 */

public class Systime {
    public long m_time = 0L;
    public Calendar mCalendar = Calendar.getInstance();

    public Systime parse(JSONObject jsonObj) {
        this.m_time = jsonObj.optLong("time");
//        if(m_time != 0L) {
//            mCalendar.setTimeInMillis(m_time);
//        }
        return this;
    }
}
