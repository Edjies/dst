package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.alipay.AliPayActivity;
import com.haofeng.apps.dst.wxapi.WXPayActivity;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.haofeng.apps.dst.R.id.activity_weizhang_pay_payway_layout;

/**
 * 违章支付页面
 *
 * @author WIN10
 */
public class WeiZhangPayActivity extends BaseActivity {
    private TextView faKuanView;
    private TextView koufenView;
    private TextView servicePayView;
    private Button payView;
    private String fen;
    private String fakuan;
    private String serviceMoney = "20";
    private TextView backView;
    private Button okPayView;
    private final String WXPAY = "1";
    private final String ALiPAY = "0";
    private String currentPay;
    private String plate_number;
    private String date;
    private TextView payWayTextview;
    private ImageView payWayImageView;
    private RadioGroup radioGroup;
    private RadioButton alipayRadioButton;
    private RadioButton weixinRadioButton;
    private LinearLayout payWayLayout;
    private static final String TAG = "WeiZhangPayActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

       addActivity(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_weizhang_pay);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.weizhangpay_top_layout);
        setTopLayoutPadding(topLayout);
        faKuanView = (TextView) findViewById(R.id.activity_weizhang_fakuan);
        koufenView = (TextView) findViewById(R.id.activity_weizhang_koufeng);
        servicePayView = (TextView) findViewById(R.id.activity_weizhang_service_pay);
        koufenView = (TextView) findViewById(R.id.activity_weizhang_koufeng);
        backView = (TextView) findViewById(R.id.weizhangpay_back);
        okPayView = (Button) findViewById(R.id.activity_weizhang_pay_ok);
        payWayTextview = (TextView) findViewById(R.id.activity_weizhang_pay_payway);
        payWayImageView = (ImageView) findViewById(R.id.activity_weizhang_pay_image);
        radioGroup = (RadioGroup) findViewById(R.id.activity_weizhang_redio_group);
        alipayRadioButton = (RadioButton) findViewById(R.id.activity_weizhang_zhifubao);
        weixinRadioButton = (RadioButton) findViewById(R.id.activity_weizhang_weixin);
        payWayLayout = (LinearLayout) findViewById(activity_weizhang_pay_payway_layout);
        payWayTextview.setText("支付宝支付");
        radioGroup.check(R.id.activity_weizhang_zhifubao);
        payWayImageView.setBackgroundResource(R.drawable.image_down);
        currentPay = ALiPAY;
    }

    private void initListener() {
        // TODO Auto-generated method stub
        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        // 支付
        okPayView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = null;
                if (currentPay == WXPAY) {
                    intent = new Intent(WeiZhangPayActivity.this, WXPayActivity.class);
                } else {
                    intent = new Intent(WeiZhangPayActivity.this, AliPayActivity.class);
                }
                intent.putExtra("infor", "违章支付");
                intent.putExtra("infor2", "地上铁App违章支付");
                intent.putExtra("type", "pay_weizhang");
                intent.putExtra("money", Float.parseFloat(fakuan) + Float.parseFloat(serviceMoney) + "");
                intent.putExtra("car_no", plate_number);
                try {
                    intent.putExtra("date", URLEncoder.encode(date,"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }

        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.activity_weizhang_zhifubao) {  //支付宝
                    payWayTextview.setText("支付宝支付");
                    currentPay = ALiPAY;
                } else {  //微信
                    payWayTextview.setText("微信支付");
                    currentPay = WXPAY;
                }
            }
        });
        payWayLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioGroup.getVisibility() == View.VISIBLE) {
                    radioGroup.setVisibility(View.GONE);
                    payWayImageView.setBackgroundResource(R.drawable.image_right);

                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                    payWayImageView.setBackgroundResource(R.drawable.image_down);
                }
            }
        });
    }

    private void initData() {
        fen = getIntent().getStringExtra("fen");
        fakuan = getIntent().getStringExtra("money");
        plate_number = getIntent().getStringExtra("plate_number");
        date = getIntent().getStringExtra("date");
        Log.e(TAG, "initData: " + fen + "--" + fakuan + "--" + plate_number + "--" + date);
        koufenView.setText(fen);
        faKuanView.setText("￥" + fakuan);
        servicePayView.setText("￥" + serviceMoney);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
