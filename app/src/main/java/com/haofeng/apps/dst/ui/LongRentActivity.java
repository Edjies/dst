package com.haofeng.apps.dst.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.LongOrderParams;
import com.haofeng.apps.dst.bean.RentConfig;
import com.haofeng.apps.dst.bean.ResLongOrderCreate;
import com.haofeng.apps.dst.bean.ResSystime;
import com.haofeng.apps.dst.bean.Systime;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.view.LongRentViewHolder1;
import com.haofeng.apps.dst.ui.view.LongRentViewHolder2;
import com.haofeng.apps.dst.ui.view.LongRentViewHolder3;
import com.haofeng.apps.dst.ui.view.StepView;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * 
 * Created by WIN10 on 2017/6/16.
 */

public class LongRentActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = LongRentActivity.class.getSimpleName();
    private final int REQUESTCODE = 0x7410;

    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private ImageView mIvPhone;

    private StepView mCvStep;
    private TextView mTvNext;

    private LongRentViewHolder1 mVh1;
    private LongRentViewHolder2 mVh2;
    private LongRentViewHolder3 mVh3;


    public RentConfig mConfig = new RentConfig();
    public Systime mSystime = new Systime();

    private int mCurStep = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_long_rent);
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

        else if(R.id.tv_next == id) {
            execNextStep();
        }

        else if(R.id.iv_contact == id) {
            new AlertDialog.Builder(this).setTitle("地上铁客服竭诚为您服务").setMessage("拨打电话 400-860-4558")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:400-860-4558"));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }

    private void initIntentData() {

    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvPhone = (ImageView) findViewById(R.id.iv_contact);
        mCvStep = (StepView) findViewById(R.id.cv_step);
        mCvStep.setStepText(Arrays.asList(getResources().getStringArray(R.array.array_long_rent_progress)));
        mCvStep.setCurrentStep(mCurStep);
        mTvNext = (TextView) findViewById(R.id.tv_next);

        View view1 = findViewById(R.id.view_1);
        View view2 = findViewById(R.id.view_2);
        View view3 = findViewById(R.id.view_3);
        mVh1 = new LongRentViewHolder1(this, view1);
        mVh2 = new LongRentViewHolder2(this, view2);
        mVh3 = new LongRentViewHolder3(this, view3);
    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mTvNext.setOnClickListener(this);
        mIvPhone.setOnClickListener(this);
    }

    private void initData() {

    }



    private void execGetSystime() {
        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/system/time")
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    ResSystime res = BeanParser.parseSystime(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mSystime = res.mTime;
                    }else {
                    }
                    // 更新页面
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }));
    }

    private void execNextStep() {
        if(mCurStep == 1) {
            try{
                mVh1.checkParams();
            }catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            mCurStep += 1;
            mVh1.hide();
            mVh2.show();
            mVh3.hide();
            mCvStep.setCurrentStep(mCurStep);


        }else if(mCurStep == 2) {
            if(mVh2.checkParams()) {
                LongOrderParams params = new LongOrderParams();
                mVh1.putOrderParams(params);
                mVh2.putOrderParams(params);
                execSubmit(params);
            }
//            mCurStep += 1;
//            mVh1.hide();
//            mVh2.hide();
//            mVh3.show();
//            mCvStep.setCurrentStep(mCurStep);
        }

    }

    private void execSubmit(LongOrderParams params) {
        showProgress("提交中...");
        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/submit_long_order")
                .append(params.getParams())
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                ResLongOrderCreate res = BeanParser.parseLongOrderCreate(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    mCurStep += 1;
                    mVh1.hide();
                    mVh2.hide();
                    mVh3.show();
                    mCvStep.setCurrentStep(mCurStep);
                    mTvNext.setVisibility(View.GONE);
                    mVh3.setOrderNo(res.mOrderCreate.m_apply_no);
                }else {
                    Toast.makeText(LongRentActivity.this, res.mMessage, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mVh1.onActivityResult(requestCode, resultCode, data);

    }
}
