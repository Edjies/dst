package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

/**
 *
 * Created by WIN10 on 2017/6/12.
 */

public class LongOrderCreate {
    public String m_apply_no = "";

    public LongOrderCreate parse(JSONObject jsonObj) {
        this.m_apply_no = jsonObj.optString("apply_no");
        return this;
    }
}
