package com.haofeng.apps.dst.ui.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.Car;
import com.haofeng.apps.dst.bean.ResCarList;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.LongRentActivity;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.UNumber;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by WIN10 on 2017/6/18.
 */

public class LongRentCarTypeViewHolder implements View.OnClickListener{
    private LongRentActivity mContext;
    private View mParent;
    private TextView mTvDemand;
    private TextView mTvCarType;
    private TextView mTvDuration;
    private ImageView mIvSub;
    private ImageView mIvAdd;
    private EditText mEtNumber;
    private TextView mTvDelete;

    private String mCityId = "77";
    public static ArrayList<Car> mCars;
    private Car mCar;
    private String mRentDuration = "";

     public LongRentCarTypeViewHolder(LongRentActivity context, View parent, boolean deleteVisible, final OnDeleteListener onDeleteListener) {
         this.mContext = context;
         this.mParent = parent;
         this.mTvDemand = (TextView) mParent.findViewById(R.id.tv_demand);
         this.mTvCarType = (TextView) mParent.findViewById(R.id.tv_car_type);
         this.mTvDuration = (TextView) mParent.findViewById(R.id.tv_duration);
         this.mIvSub = (ImageView) mParent.findViewById(R.id.iv_sub);
         this.mIvAdd = (ImageView) mParent.findViewById(R.id.iv_add);
         this.mEtNumber = (EditText) mParent.findViewById(R.id.et_number);
         this.mTvDelete = (TextView) mParent.findViewById(R.id.tv_delete);
         if(deleteVisible) {
             this.mTvDelete.setVisibility(View.VISIBLE);
         }else {
             this.mTvDelete.setVisibility(View.GONE);
         }

         this.mIvAdd.setOnClickListener(this);
         this.mIvSub.setOnClickListener(this);
         this.mTvCarType.setOnClickListener(this);
         this.mTvDuration.setOnClickListener(this);
         this.mTvDelete.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(onDeleteListener != null) {
                     onDeleteListener.onDelete(LongRentCarTypeViewHolder.this);
                 }
             }
         });
     }

    public void setPosition(int position) {
        mTvDemand.setText("车型需求("+position+")");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.tv_car_type == id) {
            if(mCars == null) {
                execGetCarList();
            }else {
                showCarSelectPopView(mCars);
            }
        }

        else if(R.id.iv_sub == id) {
            int number = UNumber.getInt(mEtNumber.getText().toString(), 0);
            number = number - 1;
            if( number < 1) {
                number = 1;
            }
            mEtNumber.setText(String.valueOf(number));
        }

        else if(R.id.iv_add == id) {
            int number = UNumber.getInt(mEtNumber.getText().toString(), 0);
            number = number + 1;
            mEtNumber.setText(String.valueOf(number));
        }

        else if(R.id.tv_duration == id) {
            new DurationPopWindow(mContext, mTvDuration, new DurationPopWindow.Callback() {
                @Override
                public void onSelected(String infor, String value) {
                    mRentDuration = value;
                    mTvDuration.setText(infor);
                }
            }).show();
        }
    }

    public View getView() {
        return mParent;
    }

    public void setCityId(String id) {
        if(!mCityId.equals(id)) {
            this.mCityId = id;
            mCar = null;
            mTvCarType.setText("");
        }
    }



    /**
     * 获取租车列表中的车辆信息(根据城市id获取该城市的列表)
     */
    private void execGetCarList() {
        mContext.showProgress("加载中...");
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/long_car_type")
                    .append("city_id", mCityId)
                    .append("user_id", PublicUtil.getStorage_string(mContext, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(mContext, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(mContext.TAG, url);
        UVolley.getVolley(mContext).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mContext.hideProgress();
                ULog.e(mContext.TAG, response.toString());
                if(mContext.isPageResumed) {
                    ResCarList res = BeanParser.parseLongCarList(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        // 用户
                        mCars = res.mDatas;
                        showCarSelectPopView(mCars);
                    }else {
                        Toast.makeText(mContext, res.mMessage, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mContext.hideProgress();
            }
        }));
    }


    public void showCarSelectPopView(ArrayList<Car> datas) {
        new CarTypePopWindow(mContext, mParent, datas, new CarTypePopWindow.Callback() {
            @Override
            public void onSelected(Car car) {
                mCar = car;
                mTvCarType.setText(mCar.mBrandName + mCar.mCarModelName);
            }
        }).show();
    }

    public boolean isParamsReady() {
        return mCar != null && !TextUtils.isEmpty(mEtNumber.getText().toString()) && !TextUtils.isEmpty(mRentDuration);
    }

    public JSONObject getParams() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("car_type_id", mCar.mId);
            jsonObj.put("num", mEtNumber.getText().toString());
            jsonObj.put("use_time", mRentDuration);
        } catch (Exception e) {

        }
        return  jsonObj;
    }



    public interface OnDeleteListener {
        public void onDelete(LongRentCarTypeViewHolder vh);
    }

}
