package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.haofeng.apps.dst.R;
import com.umeng.analytics.MobclickAgent;

public class OftenProblemActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout guideProblemLayout;
    private RelativeLayout rentCarProblemLayout;
    private RelativeLayout addPowerProblemLayout;
    private RelativeLayout carServiceProblemLayout;
    private RelativeLayout carLocationProblemLayout;
    private FrameLayout topLayout;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_common_problem);
        topLayout = (FrameLayout) findViewById(R.id.activity_often_problem_top_layout);
        setTopLayoutPadding(topLayout);
        guideProblemLayout = (RelativeLayout) findViewById(R.id.activity_often_problem_guide_layout);
        rentCarProblemLayout = (RelativeLayout) findViewById(R.id.activity_often_problem_rent_car_layout);
        addPowerProblemLayout = (RelativeLayout) findViewById(R.id.activity_often_problem_add_power_layout);
        carServiceProblemLayout = (RelativeLayout) findViewById(R.id.activity_often_problem_car_service_layout);
        carLocationProblemLayout = (RelativeLayout) findViewById(R.id.activity_often_problem_location_layout);
        backButton = (ImageButton) findViewById(R.id.activity_often_problem_back);

        backButton.setOnClickListener(this);
        guideProblemLayout.setOnClickListener(this);
        rentCarProblemLayout.setOnClickListener(this);
        addPowerProblemLayout.setOnClickListener(this);
        carServiceProblemLayout.setOnClickListener(this);
        carLocationProblemLayout.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.activity_often_problem_guide_layout:

                break;
            case R.id.activity_often_problem_rent_car_layout:
                break;
            case R.id.activity_often_problem_add_power_layout:
                break;
            case R.id.activity_often_problem_car_service_layout:
                break;
            case R.id.activity_often_problem_location_layout:
                break;
            case R.id.activity_often_problem_back:
                finish();
                break;
        }


    }
}
