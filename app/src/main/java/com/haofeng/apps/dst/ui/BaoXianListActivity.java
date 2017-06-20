package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ui.baoxian.AccountBaoXianActivity;
import com.haofeng.apps.dst.ui.baoxian.AdultBaoXianActivity;
import com.haofeng.apps.dst.ui.baoxian.BabyBaoXianActivity;
import com.haofeng.apps.dst.ui.baoxian.FemaleBaoXianActivity;
import com.haofeng.apps.dst.ui.baoxian.LoveBaoXianActivity;
import com.umeng.analytics.MobclickAgent;

public class BaoXianListActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_xian_list);
        ImageButton backImageButtn = (ImageButton) findViewById(R.id.activity_baoxian_list_back);
        LinearLayout adultAccidentLayout = (LinearLayout) findViewById(R.id.activity_baoxian_list_adult_accident_layout);
        LinearLayout femaleLayout = (LinearLayout) findViewById(R.id.activity_baoxian_list_female_layout);
        LinearLayout zhenloveLayout = (LinearLayout) findViewById(R.id.activity_baoxian_list_zhen_love_layout);
        LinearLayout babyLayout = (LinearLayout) findViewById(R.id.activity_baoxian_baby_layout);
        LinearLayout accountSafeLayout = (LinearLayout) findViewById(R.id.activity_baoxian_account_safe_layout);

        backImageButtn.setOnClickListener(this);
        adultAccidentLayout.setOnClickListener(this);
        femaleLayout.setOnClickListener(this);
        zhenloveLayout.setOnClickListener(this);
        babyLayout.setOnClickListener(this);
        accountSafeLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.activity_baoxian_list_back:
                finish();
                break;
            case R.id.activity_baoxian_list_adult_accident_layout:  //成人险
                intent = new Intent(this, AdultBaoXianActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_baoxian_list_female_layout:  //女性险
                intent = new Intent(this, FemaleBaoXianActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_baoxian_list_zhen_love_layout:  //臻爱医疗险
                intent = new Intent(this, LoveBaoXianActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_baoxian_baby_layout:  //少儿健康险
                intent = new Intent(this, BabyBaoXianActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_baoxian_account_safe_layout:  //账户安全
                intent = new Intent(this, AccountBaoXianActivity.class);
                startActivity(intent);
                break;
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
}
