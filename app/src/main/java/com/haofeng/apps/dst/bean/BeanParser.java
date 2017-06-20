package com.haofeng.apps.dst.bean;

import com.haofeng.apps.dst.DataCache;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * Created by WIN10 on 2017/6/10.
 */

public class BeanParser {
    /* 解析BaseRes*/
    public static BaseRes parseBaseRes(JSONObject jsonObj) {
        BaseRes res = new BaseRes();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");
        return res;
    }

    public static ResSystime parseSystime(JSONObject jsonObj) {
        ResSystime res = new ResSystime();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {
            JSONObject jsonData = jsonObj.optJSONObject("data");
            if(jsonData != null) {
                res.mTime.parse(jsonData);
            }
        }
        return res;
    }

    public static ResRentConfig parseRentConfig(JSONObject jsonObj) {
        ResRentConfig res = new ResRentConfig();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");
        if(res.mCode == BaseRes.RESULT_OK) {
            JSONObject jsonData = jsonObj.optJSONObject("data");
            if(jsonData != null) {
                res.mConfig.parse(jsonData);
            }
        }
        return res;

    }

    /* 解析会员信息*/
    public static ResAccount parseAccount(JSONObject jsonObj) {
        ResAccount res = new ResAccount();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {
            JSONObject jsonData = jsonObj.optJSONObject("data");
            res.mAccount.parse(jsonData);
        }
        return res;
    }

    public static ResCarList parseCarList(JSONObject jsonObj) {
        ResCarList res = new ResCarList();
        res.mCode = jsonObj.optInt("error");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            // list
            JSONArray jsonData = jsonObj.optJSONArray("data");
            if (jsonData == null) {
                return res;
            }

            // content
            res.parse(jsonData);

        }

        return res;

    }

    public static ResCarList parseLongCarList(JSONObject jsonObj) {
        ResCarList res = new ResCarList();
        res.mCode = jsonObj.optInt("error");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            // list
            JSONArray jsonData = jsonObj.optJSONArray("data");
            if (jsonData == null) {
                return res;
            }

            // content
            res.parse(jsonData);

            DataCache.getInstance().setLongRentCars(res.mDatas);

        }

        return res;

    }



    public static ResCarSite parseCarSite(JSONObject jsonObj) {
        ResCarSite res = new ResCarSite();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            // list
            JSONArray jsonData = jsonObj.optJSONArray("data");
            if (jsonData == null) {
                return res;
            }

            // content
            res.parse(jsonData);
        }

        return res;

    }

    public static ResCitySite parseCitySite(JSONObject jsonObj) {
        ResCitySite res = new ResCitySite();
        res.mCode = jsonObj.optInt("error");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            // list
            JSONArray jsonData = jsonObj.optJSONArray("data");
            if (jsonData == null) {
                return res;
            }

            // content
            res.parse(jsonData);

            // 缓存数据
            DataCache.getInstance().setCity(res.mDatas);
        }

        return res;

    }

    public static ResCarDetail parseCarDetail(JSONObject jsonObj) {
        ResCarDetail res = new ResCarDetail();
        res.mCode = jsonObj.optInt("error");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            // list
            JSONObject jsonData = jsonObj.optJSONObject("data");
            if (jsonData == null) {
                return res;
            }

            // content
            res.mCar.parse(jsonData);
        }
        return res;
    }

    public static ResLongOrderCreate parseLongOrderCreate(JSONObject jsonObj) {
        ResLongOrderCreate res = new ResLongOrderCreate();
        res.mCode = jsonObj.optInt("error");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {
            JSONObject jsonData = jsonObj.optJSONObject("data");
            res.mOrderCreate.parse(jsonData);
        }
        return res;
    }

    public static ResLongOrderDetail parseLongOrderDetail(JSONObject jsonObj) {
        ResLongOrderDetail res = new ResLongOrderDetail();
        res.mCode = jsonObj.optInt("error");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            JSONObject jsonData = jsonObj.optJSONObject("data");
            if (jsonData != null) {
                res.mOrder.parse(jsonData);
            }
        }
        return res;
    }

    public static ResLongOrder parseLongOrderList(JSONObject jsonObj) {
        ResLongOrder res = new ResLongOrder();
        res.mCode = jsonObj.optInt("error");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            JSONArray jsonData = jsonObj.optJSONArray("data");
            if (jsonData == null) {
                return res;
            }

            // content
            for(int i = 0; i < jsonData.length(); i++) {
                JSONObject jsonItem = jsonData.optJSONObject(i);
                if(jsonItem != null) {
                    res.mDatas.add(new LongOrder().parse(jsonItem));
                }
            }
        }
        return res;
    }


    public static ResOrderCreate parseOrderCreate(JSONObject jsonObj) {
        ResOrderCreate res = new ResOrderCreate();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {
            JSONObject jsonData = jsonObj.optJSONObject("data");
            res.mOrderCreate.parse(jsonData);
        }
        return res;
    }

    public static ResOrderDetail parseOrderDetail(JSONObject jsonObj) {
        ResOrderDetail res = new ResOrderDetail();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {

            JSONObject jsonData = jsonObj.optJSONObject("data");
            if (jsonData == null) {
                res.mCode = BaseRes.RESULT_EMPTY_CONTENT;
                res.mMessage = "empty order content";
                return res;
            }
            JSONObject jsonOrder = jsonData.optJSONObject("order");
            if(jsonOrder == null) {
                res.mCode = BaseRes.RESULT_EMPTY_CONTENT;
                res.mMessage = "empty order content";
                return res;
            }

            // content
            res.mOrder.parse(jsonOrder);
        }
        return res;
    }


    public static ResOrderList parseOrderList(JSONObject jsonObj) {
        ResOrderList res = new ResOrderList();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {
            // data
            JSONObject jsonData = jsonObj.optJSONObject("data");
            if(jsonData == null) {
                return res;
            }
            // list
            JSONArray jsonList = jsonData.optJSONArray("list");
            if (jsonList == null) {
                return res;
            }

            // content
            for(int i = 0; i < jsonList.length(); i++) {
                JSONObject jsonItem = jsonList.optJSONObject(i);
                if(jsonItem != null) {
                    res.mDatas.add(new Order().parse(jsonItem));
                }
            }
        }
        return res;
    }

    public static ResOrderPay parseOrderPay(JSONObject jsonObj) {
        ResOrderPay res = new ResOrderPay();
        res.mCode = jsonObj.optInt("errcode");
        res.mMessage = jsonObj.optString("msg");

        if(res.mCode == BaseRes.RESULT_OK) {


            JSONObject jsonData = jsonObj.optJSONObject("data");
            if (jsonData == null) {
                res.mCode = BaseRes.RESULT_EMPTY_CONTENT;
                res.mMessage = "empty order pay content";
                return res;
            }
            res.mOrderPay.parse(jsonData);
        }
        return res;
    }

}
