package com.haofeng.apps.dst.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.DataCache;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.Car;
import com.haofeng.apps.dst.bean.CitySite;
import com.haofeng.apps.dst.bean.LongOrderDetail;
import com.haofeng.apps.dst.bean.ResCarList;
import com.haofeng.apps.dst.bean.ResCitySite;
import com.haofeng.apps.dst.bean.ResLongOrderDetail;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.UDate;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * 
 * Created by WIN10 on 2017/6/16.
 */

public class LongOrderDetailActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = LongOrderDetailActivity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;

    private ScrollView mSvContent;
    private TextView mTvOrderNo;
    private TextView mTvOrderTime;
    private TextView mTvMessage;
    private TextView mTvResult;

    private TextView mTvPickSite;
    private TextView mTvPickTime;

    private LinearLayout mLlCarType;
    private LinearLayout mLlContact;

    private String mOrderNo = "";

    private ArrayList<Car> mCars;
    private ArrayList<CitySite> mCitys;
    private LongOrderDetail mOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_order_detail);
        initIntentData();
        setViews();
        setListeners();
        initData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
        }
    }

    private void initIntentData() {
        mOrderNo = getIntent().getExtras().getString("apply_id");
    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);

        mSvContent = (ScrollView) findViewById(R.id.sv_content);
        mTvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        mTvOrderTime = (TextView) findViewById(R.id.tv_order_time);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mTvResult = (TextView) findViewById(R.id.tv_result);

        mTvPickSite = (TextView) findViewById(R.id.tv_pick_site);
        mTvPickTime = (TextView) findViewById(R.id.tv_pick_time);

        mLlCarType = (LinearLayout) findViewById(R.id.ll_car_type);
        mLlContact = (LinearLayout) findViewById(R.id.ll_contact);

        mSvContent.setVisibility(View.GONE);

        // test
        mLlCarType.addView(getKeyValueView("车辆型号", "比亚迪"));
        mLlCarType.addView(getKeyValueView("需求数量", "1"));

        mLlContact.addView(getKeyValueView("企业名称", "abc"));
        mLlContact.addView(getKeyValueView("联系人手机", "19042342"));

    }


    private View getKeyValueView(String key, String value) {
        View view = getLayoutInflater().inflate(R.layout.item_key_value_message, null);
        TextView mTvKey = (TextView) view.findViewById(R.id.tv_key);
        TextView mTvValue = (TextView) view.findViewById(R.id.tv_value);
        mTvKey.setText(key);
        mTvValue.setText(value);
        return view;
    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
    }

    private void initData() {
        mCars = DataCache.getInstance().getLongRentCars();
        mCitys = DataCache.getInstance().getCitySite();
        execGetData();
    }


    /**
     * 获取租车列表中的车辆信息(根据城市id获取该城市的列表)
     */
    private void execGetCarList() {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/long_car_type")
                    .append("city_id", "77")
                    .append("user_id", PublicUtil.getStorage_string(this , "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(this , "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ResCarList res = BeanParser.parseLongCarList(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    mCars = res.mDatas;
                    onDataReceived();
                }else {
                    hideProgress();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));
    }


    private void execGetCitySite() {

        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/get_city_list")
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ResCitySite res = BeanParser.parseCitySite(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    mCitys = res.mDatas;
                    onDataReceived();
                }else {
                    hideProgress();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));
    }

    public void execGetOrderDetail() {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/long_order_scan")
                    .append("apply_id", mOrderNo)
                    .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ULog.e(TAG, response.toString());

                if(isPageResumed) {
                    ResLongOrderDetail res = BeanParser.parseLongOrderDetail(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mOrder = res.mOrder;
                        onDataReceived();
                    }else {
                        hideProgress();
                        Toast.makeText(LongOrderDetailActivity.this, res.mMessage, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));
    }


    private void execGetData() {
        showProgress("加载中...");
        if(mCars == null) {
            execGetCarList();
        }

//        if(mCitys == null) {
//            execGetCitySite();
//        }

        execGetOrderDetail();
    }

    private void onDataReceived() {
        if(mCars != null && mOrder != null) {
            hideProgress();
            execRefreshPage(mOrder);
        }
    }

    private void execRefreshPage(LongOrderDetail item) {
        mSvContent.setVisibility(View.VISIBLE);
        mTvOrderNo.setText("需求单号: " + item.m_apply_no);
        mTvOrderTime.setText(item.m_order_time);
        mTvPickTime.setText(UDate.getFormatDate(item.m_es_take_car_time, "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm"));
        String status = item.m_status;
        // 响应状态，0等待响应，1已经响应指派了服务专员
        if("0".equals(status)) {
            mTvMessage.setVisibility(View.GONE);
            mTvResult.setText("等待响应...");
            mTvResult.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }else {
            mTvMessage.setVisibility(View.VISIBLE);
            mTvMessage.setText("地上铁服务专员：" + item.m_sales);
            mTvResult.setText(item.m_sales_mobile);
            mTvResult.setCompoundDrawablesWithIntrinsicBounds(R.drawable.changzuzhuanyuandianhua, 0, 0, 0);
        }
        // 城市
        mTvPickSite.setText(item.m_city);

        // 车型需求列表
        mLlCarType.removeAllViews();
        for(int i = 0; i < item.m_car_models.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.view_long_rent_car_detail, null);
            TextView tvDemand = (TextView) view.findViewById(R.id.tv_demand);
            TextView tvCarType = (TextView) view.findViewById(R.id.tv_car_type);
            TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
            TextView tvDuration = (TextView) view.findViewById(R.id.tv_duration);
            LongOrderDetail.CarModel model = item.m_car_models.get(i);
            tvDemand.setText("车型需求("+ (i+1) +")");
            for(int j = 0; j < mCars.size(); j++) {
                Car car = mCars.get(j);
                if(car.mId == model.m_car_type_id) {
                    tvCarType.setText(car.mBrandName + car.mCarModelName);
                    break;
                }
            }
            tvCarType.setText(model.m_car_type_id);
            tvNumber.setText(model.m_num);
            tvDuration.setText(model.m_use_time + "个月");
            mLlCarType.addView(view);
        }
        // 联系方式
        mLlContact.removeAllViews();
        mLlContact.addView(getKeyValueView("企业名称", item.m_company_name));
        mLlContact.addView(getKeyValueView("联系人姓名", item.m_contact_name));
        mLlContact.addView(getKeyValueView("联系人手机", item.m_contact_mobile));
        mLlContact.addView(getKeyValueView("电子邮箱", item.m_contact_email));
    }
    public final static void intentMe(Activity activity, String order_no) {
        Intent intent = new Intent(activity, LongOrderDetailActivity.class);
        Bundle extra = new Bundle();
        extra.putString("apply_id", order_no);
        intent.putExtras(extra);
        activity.startActivity(intent);
    }
}
