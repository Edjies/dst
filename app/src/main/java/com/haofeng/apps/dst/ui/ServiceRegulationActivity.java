package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.haofeng.apps.dst.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 服务规则
 */
public class ServiceRegulationActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout topLayout;
    private RelativeLayout rentCarRegulaLaout;
    private RelativeLayout addPowerRegulaLayout;
    private RelativeLayout baoxianRegulaLayout;
    private RelativeLayout shouhouRegulaLayout;
    private RelativeLayout userRegulaLayout;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_service_regulation);
        topLayout = (FrameLayout) findViewById(R.id.activity_service_regulation_top_layout);
        setTopLayoutPadding(topLayout);
        rentCarRegulaLaout = (RelativeLayout) findViewById(R.id.acitivity_service_regula_rent_car_layout);
        addPowerRegulaLayout = (RelativeLayout) findViewById(R.id.acitivity_service_regula_add_power_layout);
        baoxianRegulaLayout = (RelativeLayout) findViewById(R.id.acitivity_service_regula_baoxian_layout);
        shouhouRegulaLayout = (RelativeLayout) findViewById(R.id.acitivity_service_regula_shouhou_layout);
        userRegulaLayout = (RelativeLayout) findViewById(R.id.acitivity_service_regula_use_layout);
        backButton = (ImageButton) findViewById(R.id.activity_service_regulation_back);

        rentCarRegulaLaout.setOnClickListener(this);
        addPowerRegulaLayout.setOnClickListener(this);
        baoxianRegulaLayout.setOnClickListener(this);
        shouhouRegulaLayout.setOnClickListener(this);
        userRegulaLayout.setOnClickListener(this);
        backButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.acitivity_service_regula_rent_car_layout:
                intent = new Intent(this,ZucheTiaokuanActivity.class);
                startActivity(intent);
                break;
            case R.id.acitivity_service_regula_add_power_layout:
                intent = new Intent(this,ZucheTiaokuanActivity.class);
                startActivity(intent);
                break;
            case R.id.acitivity_service_regula_baoxian_layout:
                break;
            case R.id.acitivity_service_regula_shouhou_layout:
                break;
            case R.id.acitivity_service_regula_use_layout:
                break;
            case R.id.activity_service_regulation_back:
                finish();
                break;

        }
    }
}
