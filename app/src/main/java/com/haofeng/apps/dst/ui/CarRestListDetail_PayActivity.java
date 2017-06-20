package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.alipay.AliPayActivity;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.wxapi.WXPayActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * 违章押金支付界面
 *
 * @author qtds
 */
public class CarRestListDetail_PayActivity extends BaseActivity implements
        OnClickListener {
    private final String TAG = "CarRestListDetail_PayActivity";
    private FrameLayout topLayout;
    private TextView backView;
    private TextView diandanhaoView, feiyongshuomingView,
            feiyongshuoming_inforView;
    private TextView zhifufeiyongView, zhifufeiyong_moneyView;
    private RadioButton wxRadioButton, zfbRadioButton;
    private TextView zhifufsView;
    private TextView ok_moneyView, okView;
    private String order_no;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carrestlistdetail_pay);
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

        topLayout = (FrameLayout) findViewById(R.id.carrestlistdetail_pay_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.carrestlistdetail_pay_back);
        diandanhaoView = (TextView) findViewById(R.id.carrestlistdetail_pay_danhao);
        feiyongshuomingView = (TextView) findViewById(R.id.carrestlistdetail_pay_feiyongshuoming);
        feiyongshuoming_inforView = (TextView) findViewById(R.id.carrestlistdetail_pay_feiyongshuoming_infor);
        zhifufeiyongView = (TextView) findViewById(R.id.carrestlistdetail_pay_zhifufeiyong);
        zhifufeiyong_moneyView = (TextView) findViewById(R.id.carrestlistdetail_pay_zhifufeiyong_money);
        zfbRadioButton = (RadioButton) findViewById(R.id.carrestlistdetail_pay_zhifubao);
        wxRadioButton = (RadioButton) findViewById(R.id.carrestlistdetail_pay_weixin);

        zhifufsView = (TextView) findViewById(R.id.carrestlistdetail_pay_zhifufangshi);

        ok_moneyView = (TextView) findViewById(R.id.carrestlistdetail_pay_okmoney);
        okView = (TextView) findViewById(R.id.carrestlistdetail_pay_ok);
        backView.setOnClickListener(this);
        okView.setOnClickListener(this);

        zhifufsView.setText("支付宝支付");

        order_no = getIntent().getStringExtra("order_no");
        money = getIntent().getStringExtra("money");

        diandanhaoView.setText(getIntent().getStringExtra("order_no"));

        feiyongshuomingView.setText("违章押金说明");
        feiyongshuoming_inforView
                .setText("如您在用车时间段内发生违章,将从违章押金中扣除交通违章罚款费用进行结算,一般在15-20日内完成结算");
        zhifufeiyongView.setText("违章押金:");
        zhifufeiyong_moneyView.setText("￥" + money);
        ok_moneyView.setText("￥" + money);

        zfbRadioButton
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton arg0,
                                                 boolean arg1) {
                        // TODO Auto-generated method stub
                        if (arg1) {
                            zhifufsView.setText("支付宝支付");
                        }

                    }
                });

        wxRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    zhifufsView.setText("微信支付");
                }

            }
        });

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.carrestlistdetail_pay_back:

                finish();
                break;

            case R.id.carrestlistdetail_pay_ok:

                Intent intent;

                PublicUtil.setStorage_string(CarRestListDetail_PayActivity.this,
                        "order_no", order_no);
                if (wxRadioButton.isChecked()) {
                    intent = new Intent(CarRestListDetail_PayActivity.this,
                            WXPayActivity.class);
                } else {
                    intent = new Intent(CarRestListDetail_PayActivity.this,
                            AliPayActivity.class);
                }

                intent.putExtra("money", money);
                intent.putExtra("order_no", order_no);
                intent.putExtra("type", "pay_weizhang");
                intent.putExtra("infor", "违章押金支付");
                intent.putExtra("infor2", "地上铁APP租车违章押金支付");

                startActivity(intent);
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
