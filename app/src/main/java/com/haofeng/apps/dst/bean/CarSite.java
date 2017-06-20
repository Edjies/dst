package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

/**
 * {"id":"13","name":"\u4e2d\u7535\u7eff\u6e90"}
 * 门店item
 */

public class CarSite {
    public String mId = "";
    public String mName = "";

    public CarSite parse(JSONObject jsonObj) {
        this.mId = jsonObj.optString("id");
        this.mName = jsonObj.optString("name");
        return this;
    }
}
