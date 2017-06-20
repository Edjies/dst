package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 退款中。。。
 */
public class ReturnMoneyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_return_money);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.activity_return_money_top_layout);
        setTopLayoutPadding(topLayout);
        Button returnMoneyOkButton = (Button) findViewById(R.id.activity_return_money_ok);
        returnMoneyOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReturnMoneyActivity.this, MyWalletActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView returnMoneyRecordTextView = (TextView) findViewById(R.id.activity_return_money_record);
        returnMoneyRecordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转押金退款记录
                Intent intent = new Intent(ReturnMoneyActivity.this, ReturnMoneyRecordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        TextView backTextView = (TextView) findViewById(R.id.my_return_money_back);
        backTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
