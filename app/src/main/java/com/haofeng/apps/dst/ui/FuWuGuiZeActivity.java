package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

/**
 * 
 * Created by WIN10 on 2017/6/16.
 */

public class FuWuGuiZeActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = FuWuGuiZeActivity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fu_wu_gui_ze);
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

        else if(R.id.rl_zu_che_sheng_ming == id) {
            Intent intent = new Intent(this,ZucheTiaokuanActivity.class);
            startActivity(intent);
        }

        else if(R.id.rl_chong_dian_sheng_ming == id) {
            Intent intent = new Intent(this,ZucheTiaokuanActivity.class);
            startActivity(intent);
        }

        else if(R.id.rl_li_pei_sheng_ming == id) {

        }

        else if(R.id.rl_shou_hou_sheng_ming == id) {

        }

        else if(R.id.rl_yong_hu_xie_yi == id) {

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
