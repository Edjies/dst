package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.haofeng.apps.dst.R.id.activity_new_order_detail_top_layout;
import static com.haofeng.apps.dst.httptools.Constent.ID_ACT_NEW_ORDER_DETAIL;

/**
 * 订单详情页面
 */
class NewVersionOrderDetailActivity extends BaseActivity {

    private TextView carYaJinView;
    private TextView yichangshenRentMoney;
    private TextView yujiRentMoney;
    private RelativeLayout hasMadeRentMoneyLayout;
    private TextView rongJiView;
    private TextView xuHangView;
    private TextView carTypeView;
    private ImageView carImageView;
    private TextView hcAdressView;
    private LinearLayout statusLayout0;
    private LinearLayout statusLayout1;
    private LinearLayout statusLayout2;
    private LinearLayout statusLayout3;
    private LinearLayout statusLayout4;
    private LinearLayout statusLayout5;
    private TextView backView;
    private TextView statusView;
    private ImageView typeView;
    private TextView orderNoView;
    private TextView qcDateView;
    private TextView qctimeView;
    private TextView hcDateView;
    private TextView hcTimeView;
    private TextView rentTotalTimeView;
    private TextView yujiRentAmountView;
    private TextView qcAdressView;
    private TextView congzhiYajinView;
    private Button lijiPayedButton;
    private LinearLayout showStatusControlLayout;
    private TextView chesunMoney;
    private ImageButton chesunShowDetail;
    private Button confirmMoneyButton;
    private RelativeLayout payButtomLayout;
    private RelativeLayout confirmMoneyLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        initView();
        initData();
        initListener();
    }


    private void initView() {
        setContentView(R.layout.activity_new_order_detail);
        FrameLayout topLayout = (FrameLayout) findViewById(activity_new_order_detail_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.activity_new_order_detail_back);
        showStatusControlLayout = (LinearLayout) findViewById(R.id.activity_new_order_detail_status_show_controll);
        statusLayout0 = (LinearLayout) findViewById(R.id.activity_new_order_detail_status_0);
        statusLayout1 = (LinearLayout) findViewById(R.id.activity_new_order_detail_status_1);
        statusLayout2 = (LinearLayout) findViewById(R.id.activity_new_order_detail_status_2);
        statusLayout3 = (LinearLayout) findViewById(R.id.activity_new_order_detail_status_3);
        statusLayout4 = (LinearLayout) findViewById(R.id.activity_new_order_detail_status_4);
        statusLayout5 = (LinearLayout) findViewById(R.id.activity_new_order_detail_status_5);
        statusView = (TextView) findViewById(R.id.activity_new_order_detail_status);
        typeView = (ImageView) findViewById(R.id.activity_new_order_detail_type);
        orderNoView = (TextView) findViewById(R.id.activity_new_order_detail_order_no);
        qcDateView = (TextView) findViewById(R.id.activity_new_order_detail_qc_date);
        qctimeView = (TextView) findViewById(R.id.activity_new_order_detail_qc_time);
        hcDateView = (TextView) findViewById(R.id.activity_new_order_detail_hc_date);
        hcTimeView = (TextView) findViewById(R.id.activity_new_order_detail_hc_time);
        rentTotalTimeView = (TextView) findViewById(R.id.activity_new_order_detail_rent_total_time);
        yujiRentAmountView = (TextView) findViewById(R.id.activity_new_order_detail_rent_yuji_rent_amount);
        qcAdressView = (TextView) findViewById(R.id.activity_new_order_detail_qc_adress);
        hcAdressView = (TextView) findViewById(R.id.activity_new_order_detail_hc_adress);
        carImageView = (ImageView) findViewById(R.id.activity_new_order_detail_car_image);
        carTypeView = (TextView) findViewById(R.id.activity_new_order_detail_car_type);
        xuHangView = (TextView) findViewById(R.id.activity_new_order_detail_car_xuhang);
        rongJiView = (TextView) findViewById(R.id.activity_new_order_detail_car_rongji);
        hasMadeRentMoneyLayout = (RelativeLayout) findViewById(R.id.activity_new_order_detail_has_changsheng_rent_money_layout);
        yujiRentMoney = (TextView) findViewById(R.id.activity_new_order_detail_yuji_rent_money);
        yichangshenRentMoney = (TextView) findViewById(R.id.activity_new_order_detail_yichangsheng_rent_money);
        carYaJinView = (TextView) findViewById(R.id.activity_new_order_detail_car_yajin);
        congzhiYajinView = (TextView) findViewById(R.id.activity_new_order_detail_congzhi_yajin_show);
        lijiPayedButton = (Button) findViewById(R.id.activity_new_order_detail_liji_pay);
        chesunMoney = (TextView) findViewById(R.id.activity_new_order_chesun_money);
        chesunShowDetail = (ImageButton) findViewById(R.id.activity_new_order_chesun_show_detail);
        confirmMoneyButton = (Button) findViewById(R.id.activity_new_order_chesun_confirm_money);
        payButtomLayout = (RelativeLayout) findViewById(R.id.activity_new_order_pay_layout);
        confirmMoneyLayout = (RelativeLayout) findViewById(R.id.activity_new_order_confirm_money_layout);
    }

    private void initListener() {

    }

    private void initData() {
        String order_no = getIntent().getStringExtra("order_no");
        //获取订单号
        Map<String, String> map = new HashMap<>();
        map.put("act", Constent.ACT_NEW_ORDER_DETAIL);
        map.put("order_no", order_no);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callback, ID_ACT_NEW_ORDER_DETAIL, map, false, true, true);

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

    private HttpRequestCallBack callback = new HttpRequestCallBack() {
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            if (backId == Constent.ID_ACT_NEW_ORDER_DETAIL) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isRequestSuccess) {
                            if (!isString) {
                                try {
                                    String backStr = jsonArray.getString(1);
                                    JSONObject jsonObject = new JSONObject(backStr);
                                    if (jsonObject.getString("errcode").equals("0")) {
                                        JSONObject orderObject = jsonObject.optJSONObject("data").optJSONObject("order");
                                        if (orderObject != null) {
                                            String type = orderObject.getString("type");
                                            initTopStatusView(type);
                                            String order_no = orderObject.getString("order_no");
                                            orderNoView.setText(order_no);
                                            String order_state = orderObject.getString("order_state");
                                            //initStatusView();
                                            String qc_time = orderObject.getString("qc_time");
                                            String hc_time = orderObject.getString("hc_time");
                                            String qc_store_id = orderObject.getString("qc_store_id");
                                            String hc_store_id = orderObject.getString("qc_store_id");
                                            String amount_confirm = orderObject.getString("amount_confirm");
                                            String rent_amount = orderObject.getString("rent_amount");

                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

            }
        }
    };

    /**
     * 初始化订单显示
     */
    private void initStatusView(String order_state) {

    }


    /**
     * 初始化状态
     */
    private void initTopStatusView(String type) {
        showStatusControlLayout.setVisibility(View.VISIBLE);
        switch (type) {
            case "0":
                statusLayout0.setVisibility(View.VISIBLE);
                break;
            case "1":
                statusLayout0.setVisibility(View.VISIBLE);
                statusLayout1.setVisibility(View.VISIBLE);
                break;
            case "2":
                statusLayout0.setVisibility(View.VISIBLE);
                statusLayout1.setVisibility(View.VISIBLE);
                statusLayout2.setVisibility(View.VISIBLE);
                break;
            case "3":
                statusLayout0.setVisibility(View.VISIBLE);
                statusLayout1.setVisibility(View.VISIBLE);
                statusLayout2.setVisibility(View.VISIBLE);
                statusLayout3.setVisibility(View.VISIBLE);
                break;
            case "4":
                statusLayout0.setVisibility(View.VISIBLE);
                statusLayout1.setVisibility(View.VISIBLE);
                statusLayout2.setVisibility(View.VISIBLE);
                statusLayout3.setVisibility(View.VISIBLE);
                statusLayout4.setVisibility(View.VISIBLE);
                break;
            case "5":
                statusLayout0.setVisibility(View.VISIBLE);
                statusLayout1.setVisibility(View.VISIBLE);
                statusLayout2.setVisibility(View.VISIBLE);
                statusLayout3.setVisibility(View.VISIBLE);
                statusLayout4.setVisibility(View.VISIBLE);
                statusLayout5.setVisibility(View.VISIBLE);
                break;
        }
    }
}
