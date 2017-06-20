package com.haofeng.apps.dst.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Created by WIN10 on 2017/6/10.
 */

public class ResCarSite extends BaseRes{
    /**
     * {"error":0,"msg":"\u83b7\u53d6\u95e8\u5e97\u6210\u529f\uff01","data":[{"id":"13","name":"\u4e2d\u7535\u7eff\u6e90"},{"id":"14","name":"\u7ea2\u6811\u6e7e"},{"id":"15","name":"\u51a0\u4e30\u6e90"},{"id":"16","name":"\u5730\u4e0a\u94c1\u603b\u90e8"},{"id":"49","name":"\u6656\u8c31"},{"id":"72","name":"\u6a2a\u5c97"},{"id":"120","name":"\u6e05\u6e56"},{"id":"154","name":"\u4e2d\u7535\u7eff\u6e90\u5145\u7535\u7ad9"}],"total":8}
     */
    public ArrayList<CarSite> mDatas = new ArrayList<>();

    public ResCarSite parse(JSONArray jsonArr) {
        for(int i = 0; i < jsonArr.length(); i++) {
            JSONObject jsonObj = jsonArr.optJSONObject(i);
            if(jsonObj != null) {
                mDatas.add(new CarSite().parse(jsonObj));
            }
        }
        return this;
    }
}
