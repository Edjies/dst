package com.haofeng.apps.dst.utils.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 单例模式的Volley， 封装Volley的一些常用功能
 * Created by hubble on 16-5-16.
 */
public class UVolley {

    private RequestQueue mReqQueue;

    private static UVolley mVolley;

    private Context mContext;

    private String cacheName;

    private UVolley(final Context context) {
        mContext = context;
        mReqQueue = getRequestQueue();
        cacheName = "cache";
    }


    public static UVolley getVolley(Context context) {
        if(mVolley == null) {
            mVolley = new UVolley(context);
        }
        return mVolley;
    }

    public RequestQueue getRequestQueue() {
        if(mReqQueue == null) {
            mReqQueue = Volley.newRequestQueue(mContext);
        }
        return mReqQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}
