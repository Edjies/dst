package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

/**
 * 
 * Created by WIN10 on 2017/6/16.
 */

public class TemplateActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = TemplateActivity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
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

    private void setListeners() {
        mIvBack.setOnClickListener(this);
    }

    private void initData() {

    }
}
