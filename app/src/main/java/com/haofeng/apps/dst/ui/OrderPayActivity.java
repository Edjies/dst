package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.alipay.AliPayActivity;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.Order;
import com.haofeng.apps.dst.bean.ResCarDetail;
import com.haofeng.apps.dst.bean.StateConst;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.view.CarInfoViewHolder;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.UNumber;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;
import com.haofeng.apps.dst.wxapi.WXPayActivity;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by WIN10 on 2017/6/14.
 */

public class OrderPayActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = OrderPayActivity.class.getSimpleName();
    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private TextView mTvTitle;

    /* 车辆信息*/
    private CarInfoViewHolder mVhCarInfo;

    /* 费用信息*/
    private LinearLayout mLlAmountInfo;

    /* 支付方式*/
    private CheckBox mCbZFB;
    private CheckBox mCbWX;

    /* 底部状态*/
    private TextView mTvConfirm;
    private TextView mTvSummaryTitle;
    private TextView mTvSummaryAmount;

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pay);
        initIntentData();
        setViews();
        setListeners();
        initData();
    }

    private void initIntentData() {
        mOrder = (Order) getIntent().getExtras().getSerializable("order");
    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvTitle = (TextView) findViewById(R.id.tv_title);

        mVhCarInfo = new CarInfoViewHolder(this, getWindow().getDecorView());
        mLlAmountInfo = (LinearLayout) findViewById(R.id.ll_amount_info);

        /* 支付方式*/
        mCbZFB = (CheckBox) findViewById(R.id.cb_zhifubao);
        mCbWX = (CheckBox) findViewById(R.id.cb_weixin);

        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mTvSummaryAmount = (TextView) findViewById(R.id.tv_summary_amount);
        mTvSummaryTitle = (TextView) findViewById(R.id.tv_summary_title);
    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mTvConfirm.setOnClickListener(this);

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

    private void initData() {
        mCbZFB.setChecked(true);
        execRefreshPage(mOrder);
        execGetCarDetail();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
        }

        else if(R.id.tv_confirm == id) {
            // TODO sth
        }
    }

    private void execRefreshPage(final Order order) {
        //  TODO 车辆信息
        //mVhCarInfo.updateData(order);

        mLlAmountInfo.removeAllViews();
        // 押金未付
        if(StateConst.ORDER_STATE_0.equals(order.m_order_state)) {
            mTvTitle.setText("充值押金");// 标题栏

            mLlAmountInfo.addView(getAmountItemView("租车押金", order.m_foregift_amount));
            mLlAmountInfo.addView(getAmountItemView("账户可用押金", UserDataManager.getInstance().getAccount().m_foregift_acount)); // TODO 账户可用押金
            final String pay_amount = UNumber.formatFloat(
                    // (租车押金 - 可用押金) 和 0 相比取最大值
                    Math.max(UNumber.getFloat(order.m_foregift_amount, 0) - UNumber.getFloat(UserDataManager.getInstance().getAccount().m_foregift_acount, 0),0)
            , "0.00");
            mLlAmountInfo.addView(getAmountItemView("需要充值押金", pay_amount));

            mTvSummaryTitle.setText("充值押金");
            mTvSummaryAmount.setText(pay_amount);
            mTvConfirm.setText("充值押金");
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCbZFB.isChecked()) {
                        AliPayActivity.intentMeToChargeDeposit(OrderPayActivity.this, pay_amount);
                    }else {
                        WXPayActivity.intentMeToChargeDeposit(OrderPayActivity.this, pay_amount);
                    }

                }
            });
        }
        // 日租的已提车
        else if(StateConst.ORDER_STATE_2.equals(order.m_order_state) && StateConst.ORDER_TYPE_DAY.equals(order.m_type)) {
            mTvTitle.setText("租金支付");

            mLlAmountInfo.addView(getAmountItemView("结算费用", order.getPayAmount()));

            mTvSummaryTitle.setText("已产生租金");
            mTvSummaryAmount.setText(order.getPayAmount());
            mTvConfirm.setText("立即支付");
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCbZFB.isChecked()) {
                        AliPayActivity.intentMeToRentPay(OrderPayActivity.this, order.m_order_no);
                    }else {
                        WXPayActivity.intentMeToRentPay(OrderPayActivity.this, order.m_order_no);
                    }
                }
            });
        }
        // 已还车
        else if(StateConst.ORDER_STATE_3.equals(order.m_order_state)) {
            mTvTitle.setText("租金支付");

            mLlAmountInfo.addView(getAmountItemView("结算费用", order.getPayAmount()));

            mTvSummaryTitle.setText("待支付租金");
            mTvSummaryAmount.setText(order.getPayAmount());
            mTvConfirm.setText("立即支付");
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCbZFB.isChecked()) {
                        AliPayActivity.intentMeToRentPay(OrderPayActivity.this, order.m_order_no);
                    }else {
                        WXPayActivity.intentMeToRentPay(OrderPayActivity.this, order.m_order_no);
                    }
                }
            });
        }

    }

    public void execGetCarDetail() {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/get_detail")
                    .append("id", mOrder.m_car_type_id)
                    .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        showProgress("加载中...");
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    ResCarDetail res = BeanParser.parseCarDetail(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mVhCarInfo.updateData(res.mCar);
                    }else {

                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));
    }

    private View getAmountItemView(String name, String value) {
        View view = getLayoutInflater().inflate(R.layout.item_amount_detail, null);
        TextView mTvName = (TextView) view.findViewById(R.id.tv_name);
        TextView mTvAmount = (TextView) view.findViewById(R.id.tv_amount);
        mTvName.setText(name);
        mTvAmount.setText("¥" + value);
        return view;
    }

    /**
     * 改页面仅在订单的以下几种状态（押金未付， 日租的已提车， 已还车）三种才能被调用
     * @param act
     * @param order
     */
    public static void intentMe(BaseActivity act, Order order) {
        Intent intent = new Intent(act, OrderPayActivity.class);
        intent.putExtra("order", order);
        act.startActivity(intent);
    }
}
