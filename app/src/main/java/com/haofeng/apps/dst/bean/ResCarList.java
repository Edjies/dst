package com.haofeng.apps.dst.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Created by WIN10 on 2017/6/10.
 */

public class ResCarList extends BaseRes{
    public ArrayList<Car> mDatas = new ArrayList<>();

    public ResCarList parse(JSONArray jsonArr) {
        for(int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.optJSONObject(i);
            if(jsonObj != null) {
                mDatas.add(new Car().parse(jsonObj));
            }
        }
        return this;
    }
}
