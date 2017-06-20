package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.DataCache;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.CitySite;
import com.haofeng.apps.dst.bean.ResCitySite;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.adapter.CitySiteAdapter;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * 获取城市
 */
public class CitySiteActivity extends BaseActivity implements View.OnClickListener{
    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private ListView mLvConent;
    private CitySiteAdapter mAdapter;
    private ArrayList<CitySite> mDatas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_site);
        initViews();
        setListeners();
        initData();
    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mLvConent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("city", mAdapter.getItem(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLvConent = (ListView) findViewById(R.id.lv_content);
        mAdapter = new CitySiteAdapter(this);
        mLvConent.setAdapter(mAdapter);
    }

    private void initData() {
        mDatas = DataCache.getInstance().getCitySite();
        if(mDatas != null) {
            mAdapter.updateData(mDatas);
        }

        execGetCarSite();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    private void execGetCarSite() {
        if(mDatas == null) {
            showProgress("加载中...");
        }
        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/get_city_list")
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                ResCitySite res = BeanParser.parseCitySite(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    mAdapter.updateData(res.mDatas);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));
    }

    public static void intentMe(BaseActivity act, int requestCode) {
        Intent intent = new Intent(act, CitySiteActivity.class);
        act.startActivityForResult(intent, requestCode);
    }
}
