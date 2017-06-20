package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 押金声明
 */
public class YaJinDetailActivity extends BaseActivity {

    private TextView backView;
    private WebView showDetailWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_yajin_detail);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.yajin_detail_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.yajin_detail_back);
        showDetailWebView = (WebView) findViewById(R.id.yajin_detail_show);
        showDetailWebView.loadUrl("http://app.dstcar.com/app/terms/foregift");
        backView.setOnClickListener(new View.OnClickListener() {
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
