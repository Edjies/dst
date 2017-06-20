package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 保险声明
 */
public class BaoXianDetailActivity extends BaseActivity {

    private WebView showWebView;
    private TextView backView;
    private FrameLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_baoxian_detail);
        showWebView = (WebView) findViewById(R.id.baoxian_detail_show);
        topLayout = (FrameLayout) findViewById(R.id.baoxian_detail_top_layout);
        setTopLayoutPadding(topLayout);
        showWebView.getSettings().setJavaScriptEnabled(true);
        backView = (TextView) findViewById(R.id.baoxian_detail_back);
        showWebView.loadUrl("http://app.dstcar.com/app/terms/insurance");
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPause(this);
    }
}
