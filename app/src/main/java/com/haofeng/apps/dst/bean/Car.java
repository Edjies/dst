package com.haofeng.apps.dst.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * 车辆信息
 * Created by WIN10 on 2017/6/10.
 */

public class Car implements Serializable{
    /**
     * {"brand_name":"\u6bd4\u4e9a\u8fea",
     * "id":"23",
     * "car_model_name":"T3",
     * "car_type_name":"\u7eaf\u7535\u52a8\u8f7f\u8f66",
     * "endurance_mileage":"888",
     * "car_full_img":"uploads\/image\/fault\/5931092c985de.jpg",
     * "cubage":"0.00",
     * "time_price":"50",
     * "day_price":"300"}
     */

    public String mBrandName = "";
    public String mId = "";
    public String mCarModelName = "";
    public String mCarTypeName = "";
    public String mEnduranceMileage = "";
    public String mCarFullImg = "";
    public String mCubage = "";
    public String mTimePrice = "";
    public String mDayPrice = "";

    public Car parse(JSONObject jsonObj) {
        this.mBrandName = jsonObj.optString("brand_name");
        this.mId = jsonObj.optString("id");
        this.mCarModelName = jsonObj.optString("car_model_name");
        this.mCarTypeName = jsonObj.optString("car_type_name");
        this.mEnduranceMileage = jsonObj.optString("endurance_mileage");
        this.mCarFullImg = jsonObj.optString("car_full_img");
        this.mCubage = jsonObj.optString("cubage");
        this.mTimePrice = jsonObj.optString("time_price");
        this.mDayPrice = jsonObj.optString("day_price");
        return this;
    }

}
