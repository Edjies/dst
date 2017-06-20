package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.umeng.analytics.MobclickAgent;

public class CarChangzuFinshActivity extends BaseActivity implements
        OnClickListener {
    private final String TAG = "CarChangzuFinshActivity";

    private FrameLayout topLayout;
    private TextView backView;

    private TextView okView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carchangzufinish);
        addActivity(this);
        init();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);// 友盟统计开始
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);// 友盟统计结束

    }

    /*
     * 初始化组件
     */
    public void init() {

        topLayout = (FrameLayout) findViewById(R.id.changzufinish_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.changzufinish_back);

        okView = (TextView) findViewById(R.id.changzufinish_ok);

        backView.setOnClickListener(this);

        okView.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        switch (arg0.getId()) {
            case R.id.changzufinish_back:

                finish();
                break;

            case R.id.changzufinish_ok:
                finish();

                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;

        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

}
