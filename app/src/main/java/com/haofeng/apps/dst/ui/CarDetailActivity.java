package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.CarDetail;
import com.haofeng.apps.dst.bean.ResCarDetail;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 *
 * Created by WIN10 on 2017/6/16.
 */

public class CarDetailActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = CarDetailActivity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private String mCarId = "";
    private CarDetail mCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
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
        mCarId = getIntent().getExtras().getString("carId");
    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);

    }


    public void execGetCarDetail() {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/get_detail")
                    .append("id", mCarId)
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
                hideProgress();
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    ResCarDetail res = BeanParser.parseCarDetail(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mCar = res.mCar;

                    }else {

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


    private void setListeners() {
        mIvBack.setOnClickListener(this);
    }

    private void initData() {

    }

    public static void intentMe(BaseActivity act, String carId) {
        Intent intent = new Intent(act, CarDetailActivity.class);
        intent.putExtra("carId", carId);
        act.startActivity(intent);
    }
}
