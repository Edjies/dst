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

public class CommonQuestionActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = CommonQuestionActivity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chang_jian_wen_ti);
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

        else if(R.id.rl_xin_shou_zhi_nan == id) {

        }

        else if(R.id.rl_zu_che_wen_ti == id) {

        }

        else if(R.id.rl_chong_dian_wen_ti == id) {

        }

        else if(R.id.rl_fu_wu_wen_ti == id) {

        }

        else if(R.id.rl_wang_dian_wen_ti == id) {

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
