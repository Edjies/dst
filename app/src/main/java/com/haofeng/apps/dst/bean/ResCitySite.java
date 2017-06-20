package com.haofeng.apps.dst.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Created by WIN10 on 2017/6/10.
 */

public class ResCitySite extends BaseRes{
    /**
     *
     */
    public ArrayList<CitySite> mDatas = new ArrayList<>();

    public ResCitySite parse(JSONArray jsonArr) {
        for(int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.optJSONObject(i);
            if(jsonObj != null) {
                mDatas.add(new CitySite().parse(jsonObj));
            }
        }
        return this;
    }
}
