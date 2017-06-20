package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.alipay.AliPayActivity;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.ResAccount;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.UNumber;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;
import com.haofeng.apps.dst.wxapi.WXPayActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import static com.haofeng.apps.dst.R.id.recharge_zhifu_ok;

/**
 * 充值/支付选择界面
 *
 * @author Administrator
 */
public class RechargeActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "RechargeActivity";
    private FrameLayout topLayout;
    private TextView backView;
    private EditText editText;
    private TextView yibaiView, erbaiView, wubaiView, yiqianView;
    private RadioButton wxRadioButton, zfbRadioButton;
    private RadioGroup radioGroup;
    private FrameLayout zhifufangshiLayout;
    private TextView zhifufsView;
    private ImageView zhifufsImageView;
    private TextView okTextView;
    private TextView mTvRemainAmount;

    private String moneyNumber = "0";// 默认一百
    private String rechargeType;
    private String infor;
    private String infor2;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        addActivity(this);
        init();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);// 友盟统计开始
        execGetAccountInfo();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);// 友盟统计结束
    }

    private void execGetAccountInfo() {

        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/member/info")
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ULog.e(TAG, response.toString());
                ResAccount res = BeanParser.parseAccount(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    UserDataManager.getInstance().setAccount(res.mAccount);
                    mTvRemainAmount.setText(UserDataManager.getInstance().getAccount().m_foregift_acount);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));


    }

    private void init() {
        mTvRemainAmount = (TextView) findViewById(R.id.tv_remain_amount);
        topLayout = (FrameLayout) findViewById(R.id.recharge_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.recharge_back);
        editText = (EditText) findViewById(R.id.recharge_edit);
        yibaiView = (TextView) findViewById(R.id.recharge_yibai);
        erbaiView = (TextView) findViewById(R.id.recharge_erbai);
        wubaiView = (TextView) findViewById(R.id.recharge_wubai);
        yiqianView = (TextView) findViewById(R.id.recharge_yiqian);
        wxRadioButton = (RadioButton) findViewById(R.id.recharge_zhifu_radio_weixin);
        zfbRadioButton = (RadioButton) findViewById(R.id.recharge_zhifu_radio_zhifubao);
        radioGroup = (RadioGroup) findViewById(R.id.recharge_zhifu_radiogroup);
        zhifufangshiLayout = (FrameLayout) findViewById(R.id.recharge_zhifu_layout);
        zhifufsView = (TextView) findViewById(R.id.recharge_zhifu_view);
        zhifufsImageView = (ImageView) findViewById(R.id.recharge_zhifu_image);
        okTextView = (TextView) findViewById(R.id.recharge_zhifu_ok);
        titleTextView = (TextView) findViewById(R.id.recharge_title);
        mTvRemainAmount.setText(UserDataManager.getInstance().getAccount().m_foregift_acount);

        backView.setOnClickListener(this);
        yibaiView.setOnClickListener(this);
        erbaiView.setOnClickListener(this);
        wubaiView.setOnClickListener(this);
        yiqianView.setOnClickListener(this);
        zhifufangshiLayout.setOnClickListener(this);
        okTextView.setOnClickListener(this);
        initMoney();
        if (getIntent().getStringExtra("amount") != null) {
            editText.setText(getIntent().getStringExtra("amount"));
        }
        if (getIntent().getStringExtra("type") != null) {
            rechargeType = getIntent().getStringExtra("type");
        }
        if (getIntent().getStringExtra("infor") != null) {
            infor = getIntent().getStringExtra("infor");
        }
        if (getIntent().getStringExtra("infor2") != null) {
            infor2 = getIntent().getStringExtra("infor2");
        }
        if (getIntent().getDoubleExtra("yajinNeed", 0) != 0) {
            if (getIntent().getDoubleExtra("yajinNeed", 0) >= 20) {
                editText.setText(getIntent().getDoubleExtra("yajinNeed", 0) + "");
            } else {
                editText.setText(20 + "");
            }

        }
        //设置标题
        if (rechargeType != null && rechargeType.equals("rent_car_yajin")) {
            titleTextView.setText("押金充值");
        } else {
            titleTextView.setText("钱包充值");
        }
        zhifufsView.setText("支付宝支付");
        zhifufsImageView.setBackgroundResource(R.drawable.image_down);
        zfbRadioButton.setChecked(true);
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                String input = editText.getText().toString();
                if ("100".equals(input) || "200".equals(input) || input.equals("500") || input.equals("1000")) {
                    return;
                } else {
                    initMoney();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub


            }
        });

        zfbRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
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

            case R.id.recharge_back:

                finish();

                break;
            case R.id.recharge_yibai:
                initMoney();
                yibaiView.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
                yibaiView.setTextColor(getResources().getColor(R.color.white));
                moneyNumber = "100";
                editText.setText(moneyNumber);

                break;
            case R.id.recharge_erbai:
                initMoney();
                erbaiView.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
                erbaiView.setTextColor(getResources().getColor(R.color.white));
                moneyNumber = "200";
                editText.setText(moneyNumber);
                break;
            case R.id.recharge_wubai:
                initMoney();
                wubaiView.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
                wubaiView.setTextColor(getResources().getColor(R.color.white));
                moneyNumber = "500";
                editText.setText(moneyNumber);
                break;
            case R.id.recharge_yiqian:
                initMoney();
                yiqianView.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
                yiqianView.setTextColor(getResources().getColor(R.color.white));
                moneyNumber = "1000";
                editText.setText(moneyNumber);
                break;
            case R.id.recharge_zhifu_layout:

                if (radioGroup.getVisibility() == View.VISIBLE) {
                    radioGroup.setVisibility(View.GONE);
                    zhifufsImageView.setBackgroundResource(R.drawable.image_right);
                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                    zhifufsImageView.setBackgroundResource(R.drawable.image_down);
                }

                break;

            case recharge_zhifu_ok:

                moneyNumber = editText.getText().toString();
                if (moneyNumber.equals("")) {
                    PublicUtil.showToast(this, "请输入金额", false);
                    return;
                }
                if (moneyNumber.startsWith(".")) {
                    PublicUtil.showToast(this, "输入格式不正确", false);
                    return;
                }

                // 如果大于10000editText
                float my_foregift_account = UNumber.getFloat(UserDataManager.getInstance().getAccount().m_foregift_acount, 0);
                float charge_amount = UNumber.getFloat(editText.getText().toString(), 0);
                if(my_foregift_account + charge_amount > 10000) {
                    Toast.makeText(this, "最多还能充值¥" + UNumber.formatFloat(10000 - my_foregift_account,"0.00") , Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (Double.parseDouble(moneyNumber) < 20) {
//                    PublicUtil.showToast(this, "最少充值20元", false);
//                    return;
//                }
                Intent intent = null;
                if (wxRadioButton.isChecked()) {
                    intent = new Intent(RechargeActivity.this, WXPayActivity.class);

                }
                if (zfbRadioButton.isChecked()) {
                    intent = new Intent(RechargeActivity.this, AliPayActivity.class);

                }
                intent.putExtra("type", rechargeType);
                intent.putExtra("money", PublicUtil.toTwo(Float.parseFloat(moneyNumber)) + "");
                if (infor != null && infor2 != null) {
                    intent.putExtra("infor", infor);
                    intent.putExtra("infor2", infor2);
                }
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    private void initMoney() {

        yibaiView.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
        yibaiView.setTextColor(getResources().getColor(R.color.gray));
        erbaiView.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
        erbaiView.setTextColor(getResources().getColor(R.color.gray));
        wubaiView.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
        wubaiView.setTextColor(getResources().getColor(R.color.gray));
        yiqianView.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
        yiqianView.setTextColor(getResources().getColor(R.color.gray));

    }

}
