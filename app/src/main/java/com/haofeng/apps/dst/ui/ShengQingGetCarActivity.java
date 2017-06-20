package com.haofeng.apps.dst.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.haofeng.apps.dst.bean.CarDetail;
import com.haofeng.apps.dst.bean.OrderParams;
import com.haofeng.apps.dst.bean.ResOrderCreate;
import com.haofeng.apps.dst.bean.ResOrderPay;
import com.haofeng.apps.dst.bean.StateConst;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.view.CarInfoViewHolder;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.UNumber;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;
import com.haofeng.apps.dst.wxapi.WXPayActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;


/**
 * 申请提车
 */
public class ShengQingGetCarActivity extends BaseActivity implements View.OnClickListener {
    public final static String TAG = ShengQingGetCarActivity.class.getSimpleName();
    private TextView mTvStatusBar;
    private ImageView mIvBack;
    /* 汽车信息*/
    private CarInfoViewHolder mVhCarInfo;

    /* 费用信息*/
    private LinearLayout mLlAmountInfo;


    /* 选择服务*/
    private CheckBox mCbInsurance;
    private CheckBox mCbDeduction;
    /* 支付方式*/
    private CheckBox mCbZFB;
    private CheckBox mCbWX;

    private TextView mTvConfirm;
    private TextView mTvRequiredAmount;


    private CarDetail mCar;
    private OrderParams mOrderParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheng_qing_get_car);
        initIntentData();
        setViews();
        setListeners();
        initData();


    }

    private void initIntentData() {
        mCar = (CarDetail) getIntent().getExtras().getSerializable("car");
        mOrderParams = (OrderParams) getIntent().getExtras().getSerializable("orderParams");
    }

    private void initData() {
        // 汽车信息
        mVhCarInfo.updateData(mCar);
        // 费用明细
        mLlAmountInfo.removeAllViews();
        mLlAmountInfo.addView(getAmountItemView("预计租金", mOrderParams.m_yu_ji_zu_jing));
        mLlAmountInfo.addView(getAmountItemView("汽车押金", mCar.getDeposit()));
        mLlAmountInfo.addView(getAmountItemView("需充值押金", "" + getRechargeAmount()));
        // 选择服务
        mCbInsurance.setChecked(true);
        mCbDeduction.setChecked(true);
        mCbInsurance.setText("¥ " + mCar.m_insurance_expense + "/天");
        mCbDeduction.setText("¥ " + mCar.m_insurance_bjmp + "/天");
        // 支付方式
        mCbZFB.setChecked(true);
        // 底部
        mTvRequiredAmount.setText("¥" + getRechargeAmount());

    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);



        mCbInsurance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO
                if(isChecked) {
                    mOrderParams.m_is_safe = "0";
                }else {
                    mOrderParams.m_is_safe = "1";
                }

            }
        });

        mCbDeduction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO
                if(isChecked) {
                    mOrderParams.m_is_iop = "0";
                }else {
                    mOrderParams.m_is_iop = "1";
                }
            }
        });

        mCbZFB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCbWX.setChecked(false);
                }
            }
        });

        mCbWX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mCbZFB.setChecked(false);
                }
            }
        });
    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        /* 汽车信息*/
        mVhCarInfo = new CarInfoViewHolder(this, getWindow().getDecorView());
        /* 费用明细*/
        mLlAmountInfo = (LinearLayout) findViewById(R.id.ll_amount_info);
        /* 选择服务*/
        mCbInsurance = (CheckBox) findViewById(R.id.cb_insurance);
        mCbDeduction = (CheckBox) findViewById(R.id.cb_deduction);
        /* 支付方式*/
        mCbZFB = (CheckBox) findViewById(R.id.cb_zhifubao);
        mCbWX = (CheckBox) findViewById(R.id.cb_weixin);

        /* 底部*/
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mTvRequiredAmount = (TextView) findViewById(R.id.tv_required_amount);
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
        int id = v.getId();
        if(id == R.id.iv_back) {
            finish();
        }

        else if(id == R.id.tv_confirm) {
            //gotoOrderList();
            //gotoPay();
            execCreateOrder();
        }


    }

    private void execCreateOrder() {
        final ProgressDialog pg = new ProgressDialog(this);
        pg.show();
        mOrderParams.m_car_type_id = mCar.m_id;

        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/order/create_order")
                .append("type", mOrderParams.m_type)
                .append("qc_time", mOrderParams.m_qc_time)
                .append("hc_time", mOrderParams.m_hc_time)
                .append("store_id", mOrderParams.m_store_id)
                .append("is_safe", mOrderParams.m_is_safe)
                .append("is_iop", mOrderParams.m_is_iop)
                .append("car_type_id", mOrderParams.m_car_type_id)
                .append("way", mOrderParams.m_way)
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();

        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pg.dismiss();
                ULog.e(TAG, response.toString());
                ResOrderCreate res = BeanParser.parseOrderCreate(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    Toast.makeText(ShengQingGetCarActivity.this, "订单提交成功", Toast.LENGTH_SHORT).show();
                    // 押金足够跳转列表，否则跳转押金支付
                    float amount = getRechargeAmount();
                    if(amount > 0) {
                        gotoPay(String.valueOf(amount));
                    }else {
                        gotoOrderList();
                    }

                }else {
                    Toast.makeText(ShengQingGetCarActivity.this, res.mMessage, Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pg.dismiss();
            }
        }));
    }

    private void execCreateOrderPay(String order_no, String pay_way) {
        final ProgressDialog pg = new ProgressDialog(this);
        pg.show();

        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/create/order_pay")
                .append("order_no", order_no)
                .append("pay_way", pay_way)
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();

        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pg.dismiss();
                ULog.e(TAG, response.toString());
                ResOrderPay res = BeanParser.parseOrderPay(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    Toast.makeText(ShengQingGetCarActivity.this, "订单支付创建成功", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(ShengQingGetCarActivity.this, "订单支付创建失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pg.dismiss();
            }
        }));
    }

    /**
     * 获取需要支付的押金
     * @return
     */
    private float getRechargeAmount() {
        float carDeposit = UNumber.getFloat(mCar.getDeposit(), 0);
        float userAmount = UNumber.getFloat(UserDataManager.getInstance().getAccount().m_foregift_acount, 0);
        return UNumber.getFloat(UNumber.formatFloat(Math.max(carDeposit - userAmount, 0), "0.00"), 0);
    }

    private String getPayWay() {
        if(mCbZFB.isChecked()) {
            return StateConst.PAY_WAY_ZFB;
        }else {
            return StateConst.PAY_WAY_WX;
        }
    }

    private void gotoPay(String money) {
        Intent intent = null;
        if (mCbWX.isChecked()) {
            intent = new Intent(this, WXPayActivity.class);
        }
        else {
            intent = new Intent(this, AliPayActivity.class);

        }

        if(intent != null) {
            intent.putExtra("type", "rent_car_yajin");
            intent.putExtra("money", money);
            String infor = "租车押金充值";
            String infor2 = "地上铁APP租车押金充值";
            if (infor != null && infor2 != null) {
                intent.putExtra("infor", infor);
                intent.putExtra("infor2", infor2);
            }
            startActivity(intent);
        }

    }

    private void gotoOrderList() {
        MainActivity.intentToOrderListActivity(this);
    }

    private View getAmountItemView(String name, String value) {
        View view = getLayoutInflater().inflate(R.layout.item_amount_detail, null);
        TextView mTvName = (TextView) view.findViewById(R.id.tv_name);
        TextView mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
        mTvName.setText(name);
        mTvAmount.setText("¥" + value);
        return view;
    }

}
