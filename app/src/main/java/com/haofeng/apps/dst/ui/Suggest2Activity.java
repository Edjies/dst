package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;

import org.json.JSONObject;

/**
 * 
 * Created by WIN10 on 2017/6/16.
 */

public class Suggest2Activity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = Suggest2Activity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;

    private TextView mTvConfirm;
    private EditText mEtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_2);
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

        else if(R.id.tv_confirm == id) {
            execSubmit();
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
        mEtContent = (EditText) findViewById(R.id.et_content);
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);

    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 10) {
                    mTvConfirm.setEnabled(true);
                }else {
                    mTvConfirm.setEnabled(false);
                }
            }
        });
    }

    private void initData() {

    }

    private void execSubmit() {
        showProgress("提交中...");
        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/feedback/add")
                .append("type", getIntent().getStringExtra("type"))
                .append("content", mEtContent.getText().toString())
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
                BaseRes res = BeanParser.parseBaseRes(response);
                if(isPageResumed) {
                    if(res.mCode == BaseRes.RESULT_OK) {
                        Toast.makeText(Suggest2Activity.this, "提交成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(Suggest2Activity.this, res.mMessage, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));
    }

    /**
     *
     * @param act
     * @param type   0 是其他， 1是充电
     */
    public static void intentMe(BaseActivity act, String type) {
        Intent intent = new Intent(act, Suggest2Activity.class);
        intent.putExtra("type", type);
        act.startActivity(intent);
    }
}
