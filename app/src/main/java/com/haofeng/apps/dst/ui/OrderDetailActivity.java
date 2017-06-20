package com.haofeng.apps.dst.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.CarDetail;
import com.haofeng.apps.dst.bean.Order;
import com.haofeng.apps.dst.bean.OrderDetail;
import com.haofeng.apps.dst.bean.ResCarDetail;
import com.haofeng.apps.dst.bean.ResOrderDetail;
import com.haofeng.apps.dst.bean.StateConst;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.view.CarInfoViewHolder;
import com.haofeng.apps.dst.ui.view.RentTimeInfoViewHolder;
import com.haofeng.apps.dst.ui.view.StepView;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.UNumber;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Arrays;

/**
 * 订单详情
 */
public class OrderDetailActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = OrderDetailActivity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;

    private ScrollView mSvContent;
    private LinearLayout mLlBottomTab;

    private LinearLayout mLlStep;
    private StepView mCvStep;

    /* 状态信息*/
    TextView mTvOrderState;
    ImageView mIvType;
    TextView mTvOrderNo;

    /* 时间信息*/
    private RentTimeInfoViewHolder mVhRentTimeInfo;

    /* 门店信息*/
    private TextView mTvPickSite;
    private TextView mTvReturnSite;

    /* 车辆信息*/
    private CarInfoViewHolder mVhCarInfo;

    /* 费用信息*/
    private LinearLayout mLlAmountInfo;

    /* 底部状态*/
    private TextView mTvConfirm;
    private TextView mTvSummaryTitle;
    private TextView mTvSummaryAmount;


    private OrderDetail mOrder;
    private CarDetail mCar;
    private String mOrderNo = "";

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initIntentData();
        setViews();
        setListeners();
        initData();
    }

    public void initIntentData() {
        mOrderNo = getIntent().getExtras().getString("order_no");
    }

    public void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);


        mSvContent = (ScrollView) findViewById(R.id.sv_content);
        mLlBottomTab = (LinearLayout) findViewById(R.id.ll_bottom_tab);
        mSvContent.setVisibility(View.GONE);
        mLlBottomTab.setVisibility(View.GONE);
        /* 订单状态流*/
        mLlStep = (LinearLayout) findViewById(R.id.ll_step);
        mCvStep = (StepView) findViewById(R.id.cv_step);
        mCvStep.setStepText(Arrays.asList(getResources().getStringArray(R.array.array_short_order_states)));

        mCvStep.setCurrentStep(2);
        /* 状态信息*/
        mTvOrderState = (TextView) findViewById(R.id.tv_state);
        mIvType = (ImageView) findViewById(R.id.tv_type);
        mTvOrderNo = (TextView) findViewById(R.id.tv_order_no);
        /* 时间信息*/
        mVhRentTimeInfo = new RentTimeInfoViewHolder(this, getWindow().getDecorView());
        /* 门店信息*/
        mTvPickSite = (TextView) findViewById(R.id.tv_pick_site);
        mTvReturnSite = (TextView) findViewById(R.id.tv_return_site);
        /* 车辆信息*/
        mVhCarInfo = new CarInfoViewHolder(this, getWindow().getDecorView());
        /* 费用信息*/
        mLlAmountInfo = (LinearLayout) findViewById(R.id.ll_amount_info);
        /* 底部控件*/
        mTvConfirm = (TextView) findViewById(R.id.tv_confirm);
        mTvSummaryAmount = (TextView) findViewById(R.id.tv_summary_amount);
        mTvSummaryTitle = (TextView) findViewById(R.id.tv_summary_title);


    }

    public void setListeners() {
        mIvBack.setOnClickListener(this);
    }

    public void initData() {
        execGetOrderDetail();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
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

    public void execGetOrderDetail() {
        showProgress("正在加载中...");
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/order/get_detail")
                    .append("order_no", mOrderNo)
                    .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    ResOrderDetail res = BeanParser.parseOrderDetail(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mOrder = res.mOrder;
                        execGetCarDetail();
                    }else {
                        hideProgress();
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

    public void execGetCarDetail() {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/get_detail")
                    .append("id", mOrder.m_car_type_id)
                    .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", Constent.VER)
                    .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    ResCarDetail res = BeanParser.parseCarDetail(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        mCar = res.mCar;
                        execRefreshPage(mOrder, mCar);
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


    private void execRefreshPage(Order item, CarDetail car) {
        mSvContent.setVisibility(View.VISIBLE);
        mLlBottomTab.setVisibility(View.VISIBLE);
        // 计步控件
        if(StateConst.ORDER_STATE_6.equals(item.m_order_state)) {
            mLlStep.setVisibility(View.GONE);
        }else {
            mLlStep.setVisibility(View.VISIBLE);
            mCvStep.setCurrentStep(UNumber.getInt(item.m_order_state, 0) + 1);
        }

        // 订单状态
         mTvOrderState.setText(item.getOrderStateName());
        // 类型 租车类型:0，时租 1，日租
        if(StateConst.ORDER_TYPE_HOUR.equals(item.m_type)) {
             mIvType.setImageResource(R.drawable.shizu);
        }else {
             mIvType.setImageResource(R.drawable.rizu);
        }
        // 订单号
         mTvOrderNo.setText(item.m_order_no);
        // 租车时间信息
        mVhRentTimeInfo.updateData(item);
        // 门店信息
        mTvPickSite.setText(item.m_qc_store_id); // TODO 门店名称
        mTvReturnSite.setText(item.m_hc_store_id); // TODO 门店名称
        // 车辆信息
        if(car != null) {
            mVhCarInfo.updateData(car);
        }
        // 费用明细
        refreshAmountView(item);
        // 底部状态
        refreshBottomTab(item);

    }

    private void refreshBottomTab(final Order order) {

        // 押金未付
        if(StateConst.ORDER_STATE_0.equals(order.m_order_state)) {
            mTvSummaryTitle.setText("充值押金");
            mTvSummaryAmount.setText(getRechargeAmount());
            mTvConfirm.setText("充值押金");
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderPayActivity.intentMe(OrderDetailActivity.this, order);
                }
            });
        }

        // 日租的已提车
        else if(StateConst.ORDER_STATE_2.equals(order.m_order_state) && StateConst.ORDER_TYPE_DAY.equals(order.m_type)) {
            // 立即支付
            if(StateConst.ORDER_AMOUNT_CONFIRM_0.equals(order.m_amount_confirm)) {
                mTvSummaryTitle.setText("已产生租金");
                mTvSummaryAmount.setText(order.getPayAmount());
                mTvConfirm.setText("立即支付");
                mTvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OrderPayActivity.intentMe(OrderDetailActivity.this, order);
                    }
                });
            }
            // 费用确认
            else {
                mTvSummaryTitle.setText("车损金额");
                mTvSummaryAmount.setText(order.m_chesun_amount); //
                mTvConfirm.setText("确认费用");
                mTvConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 费用确认
                        execCheckAmount();
                    }
                });
            }

        }

        // 时租的已提车
        else if(StateConst.ORDER_STATE_2.equals(order.m_order_state) && StateConst.ORDER_TYPE_HOUR.equals(order.m_type) && !StateConst.ORDER_AMOUNT_CONFIRM_0.equals(order.m_amount_confirm)) {
            mTvSummaryTitle.setText("车损金额");
            mTvSummaryAmount.setText(order.m_chesun_amount); //
            mTvConfirm.setText("确认费用");
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 费用确认
                    execCheckAmount();
                }
            });
        }

        // 已还车
        else if(StateConst.ORDER_STATE_3.equals(order.m_order_state)) {

            mTvSummaryTitle.setText("待支付租金");
            mTvSummaryAmount.setText(order.getPayAmount());
            mTvConfirm.setText("立即支付");
            mTvConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderPayActivity.intentMe(OrderDetailActivity.this, order);
                }
            });
        }

        else {
            mLlBottomTab.setVisibility(View.GONE);
        }
    }

    // 确认费用
    private void execCheckAmount() {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/order/amount_confirm")
                    .append("order_no", mOrderNo)
                    .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        showProgress("");
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    BaseRes res = BeanParser.parseBaseRes(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        finish();
                    }else {
                        Toast.makeText(OrderDetailActivity.this, res.mMessage, Toast.LENGTH_SHORT).show();
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

    // 刷新费用明细
    private void refreshAmountView(Order order) {
        mLlAmountInfo.removeAllViews();
        // 押金未付 、 未提车、 已取消 为 预计租车费用 和 租车押金,
        if(StateConst.ORDER_STATE_0.equals(order.m_order_state) || StateConst.ORDER_STATE_1.equals(order.m_order_state) || StateConst.ORDER_STATE_6.equals(order.m_order_state)) {
            mLlAmountInfo.addView(getAmountItemView("预计租车费用", order.m_rent_amount));
            mLlAmountInfo.addView(getAmountItemView("租车押金", order.m_foregift_amount));
        }
        // 日租的已提车状态
        else if(StateConst.ORDER_STATE_2.equals(order.m_order_state) && StateConst.ORDER_TYPE_DAY.equals(order.m_type)) {
            mLlAmountInfo.addView(getAmountItemView("预计租车费用", order.m_rent_amount));
            mLlAmountInfo.addView(getAmountItemView("租车押金", order.m_foregift_amount));
            mLlAmountInfo.addView(getAmountItemView("已产生租车费用", order.m_rent_real_amount));
            mLlAmountInfo.addView(getAmountItemView("已缴纳租车费用", order.m_has_pay_amount));
            mLlAmountInfo.addView(getAmountItemView("未支付租车费用", order.getPayAmount()));
        }
        // 时租的已提车状态
        else if(StateConst.ORDER_STATE_2.equals(order.m_order_state) && StateConst.ORDER_TYPE_HOUR.equals(order.m_type)) {
            mLlAmountInfo.addView(getAmountItemView("预计租车费用", order.m_rent_amount));
            mLlAmountInfo.addView(getAmountItemView("租车押金", order.m_foregift_amount));
            mLlAmountInfo.addView(getAmountItemView("已产生租车费用", order.m_rent_real_amount));
        }
        // 已还车
        else if(StateConst.ORDER_STATE_3.equals(order.m_order_state)) {
            mLlAmountInfo.addView(getAmountItemView("结算费用", order.getPayAmount()));
        }
        // 已付款 和 已完结
        else if(StateConst.ORDER_STATE_4.equals(order.m_order_state) || StateConst.ORDER_STATE_5.equals(order.m_order_state)) {
            mLlAmountInfo.addView(getAmountItemView("已产生租车费用", order.m_rent_real_amount));
            mLlAmountInfo.addView(getAmountItemView("已支付租车费用", order.m_has_pay_amount));
            mLlAmountInfo.addView(getAmountItemView("租车押金", order.m_foregift_amount));
        }

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
     * 获取需要支付的押金
     * @return
     */
    private String getRechargeAmount() {
        float carDeposit = UNumber.getFloat(mOrder.m_foregift_amount, 0);
        float userAmount = UNumber.getFloat(UserDataManager.getInstance().getAccount().m_foregift_acount, 0);
        return UNumber.formatFloat(Math.max(carDeposit - userAmount, 0), "0.00");
    }




    public final static void intentMe(Activity activity, String order_no) {
        Intent intent = new Intent(activity, OrderDetailActivity.class);
        Bundle extra = new Bundle();
        extra.putString("order_no", order_no);
        intent.putExtras(extra);
        activity.startActivity(intent);
    }
}
