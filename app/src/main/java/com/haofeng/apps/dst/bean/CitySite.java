package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by WIN10 on 2017/6/20.
 */

public class CitySite implements Serializable{
     public String  m_region_id = "77";
     public String m_region_name = "深圳";

    public CitySite parse(JSONObject jsonObj) {
        this.m_region_id = jsonObj.optString("region_id");
        this.m_region_name = jsonObj.optString("region_name");
        return this;
    }
}
