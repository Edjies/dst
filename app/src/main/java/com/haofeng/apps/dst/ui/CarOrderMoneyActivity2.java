package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.view.WheelView;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haofeng.apps.dst.R.id.carordermoney2_agree_detail;
import static java.lang.Integer.parseInt;

/**
 * 短租计费界面
 *
 * @author qtds
 */
public class CarOrderMoneyActivity2 extends BaseActivity implements OnClickListener {
    private final String TAG = "CarOrderMoneyActivity2";
    private final int REQUESTCODE = 0x7410;
    private FrameLayout topLayout;
    private TextView backView;
    private LinearLayout qucheLayout, haicheLayout;
    private TextView quchetimeView, haichetimeView, qucheweekView, haicheweekView, qucheinforView, haicheinforView;
    private TextView car_timeView, car_feiyongView, car_lichengView, car_yajinView;
    private ImageView wangdianImageView;
    private LinearLayout wangdianLayout;
    private TextView moneyView, moneyinforView, moneyinforView2, moneyinforView3;
    private TextView okView;
    private FrameLayout dateLayout;
    private TextView datecancelView, datetypeView, dateokView;
    private WheelView wheelPicker, wheelPicker2;
    private LinearLayout pickLayout, pickLayout2;
    private TextView buJiMianPeiView;
    private int year, month, day, hour, fen;
    private int qucheyear, quchemonth, qucheday, quchehour, quchefen;
    private int haicheyear, haichemonth, haicheday, haichehour, haichefen;
    private Calendar calendar = Calendar.getInstance();
    private int choosetimetype = 0;// 0 取车时间 1还车时间
    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();// 全部时间
    private String[] hours = {"09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
            "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};
    private TextView showDetail;
    private float zuchezujin = 0;// 按时间计算出的租车租金
    private float yajin, zujin_hour, zujin_day, zujin_month;
    private String hc_store_id;
    private TextView baseBaoXianView;
    private CheckBox radioButtonBaseBaoXianView, radioButtonBuJiMianPeiView;
    private String is_safe_check = "1", is_bujimianpei_check = "0";// 保险是否购买,不计免赔是否购买
    private float basebaoxian = 0;
    private float bujimianpei = 0;
    private float basebaoxiantmp, bujimianpeitmp;
    private String car_type_id; // 车型
    private TextView baoXianTotalView;
    private TextView huanCheDingDangView;
    private TextView quCheDingDangView;
    private LinearLayout selectAdressLayout;
    private LinearLayout getAndHuanInfoLayout;
    private boolean isSelectTime = false; // 是否选择时间
    private double foregiftAcount; // 押金账户
    private int wzNum;
    private float amount;
    private TextView yajinDetail;
    private TextView baoxianDetail;
    private Double myyajin;
    private TextView agreeDetailTextView;
    private CheckBox agreeCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carordermoney2);
        ((MyApplication) getApplication()).addActivity(this);
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

        topLayout = (FrameLayout) findViewById(R.id.carordermoner2_top_layout);
        if (android.os.Build.VERSION.SDK_INT < 19) {
            topLayout.setPadding(0, 0, 0, 0);
        }
        backView = (TextView) findViewById(R.id.carordermoner2_back);
        qucheLayout = (LinearLayout) findViewById(R.id.carordermoner2_quche_layout);
        haicheLayout = (LinearLayout) findViewById(R.id.carordermoner2_haiche_layout);
        quchetimeView = (TextView) findViewById(R.id.carordermoner2_quche_time);
        haichetimeView = (TextView) findViewById(R.id.carordermoner2_haiche_time);
        qucheweekView = (TextView) findViewById(R.id.carordermoner2_quche_week);
        haicheweekView = (TextView) findViewById(R.id.carordermoner2_haiche_week);
        qucheinforView = (TextView) findViewById(R.id.carordermoner2_quche_infor);
        haicheinforView = (TextView) findViewById(R.id.carordermoner2_haiche_infor);

        wangdianImageView = (ImageView) findViewById(R.id.carordermoner2_car_wangdianimage);
        wangdianLayout = (LinearLayout) findViewById(R.id.carordermoner2_car_wangdianlayout);

        car_timeView = (TextView) findViewById(R.id.carordermoney_use_car_time); // 用车时长
        car_feiyongView = (TextView) findViewById(R.id.carordermoner2_rent_car_price); // 租车租金
        car_lichengView = (TextView) findViewById(R.id.carordermoner2_distance_price); // 里程费用

        moneyView = (TextView) findViewById(R.id.carordermoner2_infor_money);
        moneyinforView = (TextView) findViewById(R.id.carordermoner2_infor_infor);

        okView = (TextView) findViewById(R.id.carordermoner2_tijiaodindgan); // 提交订单
        car_yajinView = (TextView) findViewById(R.id.carordermoner2_rent_car_yajin); // 租车押金

        dateLayout = (FrameLayout) findViewById(R.id.carordermoner2_date_layout);
        datecancelView = (TextView) findViewById(R.id.carordermoner2_date_cancel);
        datetypeView = (TextView) findViewById(R.id.carordermoner2_date_type);
        dateokView = (TextView) findViewById(R.id.carordermoner2_date_ok);
        pickLayout = (LinearLayout) findViewById(R.id.carordermoner2_date_picklayout);
        pickLayout2 = (LinearLayout) findViewById(R.id.carordermoner2_date_picklayout2);

        baseBaoXianView = (TextView) findViewById(R.id.carordermoney2_base_baoxian);
        buJiMianPeiView = (TextView) findViewById(R.id.carordermoney2_bujimianpei);
        radioButtonBaseBaoXianView = (CheckBox) findViewById(R.id.carordermoney_cb_base_baoxian);
        radioButtonBuJiMianPeiView = (CheckBox) findViewById(R.id.activity_carordermoney2_cb_bujimianpei);
        baoXianTotalView = (TextView) findViewById(R.id.carordermoney_baoxian_total_price);

        huanCheDingDangView = (TextView) findViewById(R.id.carordermoney2_huanchewangdian);
        quCheDingDangView = (TextView) findViewById(R.id.carordermoney2_quchewangdian);
        selectAdressLayout = (LinearLayout) findViewById(R.id.ordermoney2_select_adress);
        getAndHuanInfoLayout = (LinearLayout) findViewById(R.id.carordermoney2_get_and_huan_info); // 还车取车
        showDetail = (TextView) findViewById(R.id.carordermoney_show_detail);
        //查看详情
        agreeDetailTextView = (TextView) findViewById(R.id.carordermoney2_agree_detail);
        agreeCheckBox = (CheckBox) findViewById(R.id.carordermoney2_agree);
        //保险声明
        yajinDetail = (TextView) findViewById(R.id.carordermoney_yajin_detail);
        baoxianDetail = (TextView) findViewById(R.id.carordermoney_baoxian_detail);
        baoxianDetail.setOnClickListener(this);
        yajinDetail.setOnClickListener(this);
        backView.setOnClickListener(this);
        qucheLayout.setOnClickListener(this);
        haicheLayout.setOnClickListener(this);
        okView.setOnClickListener(this);
        datecancelView.setOnClickListener(this);
        dateokView.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        wangdianLayout.setOnClickListener(this);
        showDetail.setOnClickListener(this);
        getAndHuanInfoLayout.setOnClickListener(this);
        agreeDetailTextView.setOnClickListener(this);


        quchetimeView.setVisibility(View.GONE);
        haichetimeView.setVisibility(View.GONE);
        qucheweekView.setVisibility(View.GONE);
        haicheweekView.setVisibility(View.GONE);
        qucheinforView.setVisibility(View.VISIBLE);
        haicheinforView.setVisibility(View.VISIBLE);

        //获取个人押金账户总额
        Map<String, String> value = new HashMap<>();
        value.put("act", Constent.ACT_MEMBER_INFOR);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET
                , callBack, Constent.ID_MEMBER_INFOR, value, false, false, true);

        if (!TextUtils.isEmpty(getIntent().getStringExtra("safe_amount"))) { // 每天基本保险费用
            basebaoxiantmp = Float.parseFloat(getIntent().getStringExtra("safe_amount"));
            basebaoxian = basebaoxiantmp;
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("iop_amount"))) { // 不计免赔费用
            bujimianpeitmp = Float.parseFloat(getIntent().getStringExtra("iop_amount"));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("car_type_foregift"))) {
            yajin = Float.parseFloat(getIntent().getStringExtra("car_type_foregift")); // 租车押金
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("hour_price"))) {
            zujin_hour = Float.parseFloat(getIntent().getStringExtra("hour_price")); // 每小时费用
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("day_price"))) {
            zujin_day = Float.parseFloat(getIntent().getStringExtra("day_price")); // 每天的费用
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("month_price"))) { // 每月费用
            zujin_month = Float.parseFloat(getIntent().getStringExtra("month_price"));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("id"))) { // 每月费用
            car_type_id = getIntent().getStringExtra("id");
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra("wz_num"))) { // 违章次数
            wzNum = parseInt(getIntent().getStringExtra("wz_num"));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("amount"))) {
            amount = Float.parseFloat(getIntent().getStringExtra("amount"));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("payment"))) {
            payment = Float.parseFloat(getIntent().getStringExtra("payment"));
        }
        if (wzNum > 2) {
            showDetail.setVisibility(View.GONE);
            car_yajinView.setText("您的待处理违章超过 2 次，暂时无法租车");
            car_yajinView.setTextColor(getResources().getColor(R.color.gray));
            car_yajinView.setTextSize(12);
            car_yajinView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CarOrderMoneyActivity2.this);
                    final AlertDialog detailDialog = builder.create();
                    View view = View.inflate(CarOrderMoneyActivity2.this, R.layout.view_show_detail_n, null);
                    ((TextView) view.findViewById(R.id.view_show_detail_wz_count)).setText(wzNum+"");
                    detailDialog.setView(view);
                    ((ImageView) view.findViewById(R.id.view_show_detail_close))
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    detailDialog.dismiss();
                                }
                            });
                    detailDialog.show();
                }
            });
        } else {
            showDetail.setVisibility(View.VISIBLE);
            car_yajinView.setText("￥" + PublicUtil.roundByScale(payment, 2));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("wz_num"))) { // 押金可用余额
            foregiftAcount = Double.parseDouble(getIntent().getStringExtra("foregift_acount"));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("freeze"))) { // 违章冻结
            freeze = Double.parseDouble(getIntent().getStringExtra("freeze"));
        }
        car_lichengView.setText(
                "起步" + getIntent().getStringExtra("lowest_price") + "元(" + getIntent().getStringExtra("lowest_mileage")
                        + "公里内)+" + getIntent().getStringExtra("mileage_price") + "元/公里");


        baseBaoXianView.setText("￥" + PublicUtil.roundByScale(basebaoxiantmp, 2) + "/天");
        buJiMianPeiView.setText("￥" + PublicUtil.roundByScale(bujimianpeitmp, 2) + "/天");
        radioButtonBaseBaoXianView.setChecked(true);
        is_safe_check = "1";
        radioButtonBuJiMianPeiView.setChecked(false);
        is_bujimianpei_check = "0";
        baoXianTotalView.setText("￥" + PublicUtil.roundByScale(basebaoxiantmp, 2) + "/天");
        radioButtonBaseBaoXianView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) { // 购买保险
                    is_safe_check = "1";
                    if ("1".equals(is_bujimianpei_check)) {
                        basebaoxian = basebaoxiantmp;
                        bujimianpei = bujimianpeitmp;
                    } else {
                        basebaoxian = basebaoxiantmp;
                        bujimianpei = 0;
                    }
                } else {
                    is_safe_check = "0";
                    if ("1".equals(is_bujimianpei_check)) {
                        basebaoxian = 0;
                        bujimianpei = bujimianpeitmp;
                    } else {
                        basebaoxian = 0;
                        bujimianpei = 0;
                    }
                }
                baoXianTotalView.setText("￥" + PublicUtil.roundByScale(basebaoxian + bujimianpei, 2) + "/天"); // 保险总数

                if (isSelectTime) {
                    setTotalMoneyDetail(basebaoxian, bujimianpei);
                }
            }

        });
        // 不计免赔
        radioButtonBuJiMianPeiView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    is_bujimianpei_check = "1";
                    if (is_safe_check.equals("1")) {
                        basebaoxian = basebaoxiantmp;
                        bujimianpei = bujimianpeitmp;
                    } else {
                        basebaoxian = 0;
                        bujimianpei = bujimianpeitmp;
                    }
                } else {
                    is_bujimianpei_check = "0";
                    if (is_safe_check.equals("1")) {
                        basebaoxian = basebaoxiantmp;
                        bujimianpei = 0;
                    } else {
                        basebaoxian = 0;
                        bujimianpei = 0;
                    }
                }
                baoXianTotalView.setText("￥" + PublicUtil.roundByScale(basebaoxian + bujimianpei, 2) + "/天"); // 保险总数
                if (isSelectTime) {
                    setTotalMoneyDetail(basebaoxian, bujimianpei);
                }
            }
        });

        calendar.setTimeInMillis(System.currentTimeMillis());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        fen = calendar.get(Calendar.MINUTE);
        if (hour < 9) {
            hour = 9;
            fen = 0;
        }
        wheelPicker = new WheelView(CarOrderMoneyActivity2.this);
        wheelPicker2 = new WheelView(CarOrderMoneyActivity2.this);
        for (int i = 0; i < hours.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("infor", hours[i]);
            map.put("hour", hours[i].split(":")[0]);
            map.put("fen", hours[i].split(":")[1]);
            arrayList3.add(map);
        }
        wheelPicker.setOnWheelViewListener(new WheelView.OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex) {
                // TODO Auto-generated method stub
                if (wheelPicker.getSeletedIndex() == 0) {
                    wheelPicker2.setDate(arrayList2);
                    pickLayout2.removeAllViews();
                    pickLayout2.addView(wheelPicker2);
                } else if (wheelPicker.getSeletedIndex() != 0) {
                    wheelPicker2.setDate(arrayList3);
                    pickLayout2.removeAllViews();
                    pickLayout2.addView(wheelPicker2);
                }
            }

        });
    }

    // 租车押金判断

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (resultCode == 12 && data != null) {
                selectAdressLayout.setVisibility(View.GONE);
                getAndHuanInfoLayout.setVisibility(View.VISIBLE);
                hc_store_id = data.getStringExtra("id");
                huanCheDingDangView.setText(data.getStringExtra("name") + "(" + data.getStringExtra("addr") + ")");
                quCheDingDangView.setText(data.getStringExtra("name") + "(" + data.getStringExtra("addr") + ")");
            }

        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        String start, end;
        switch (arg0.getId()) {
            case carordermoney2_agree_detail:  //查看详情
                Intent tiaokuanIntent = new Intent(this, ZucheTiaokuanActivity.class);
                startActivity(tiaokuanIntent);
                break;
            case R.id.carordermoner2_back: // 返回
                finish();
                break;
            case R.id.carordermoner2_quche_layout: // 取车
                choosetimetype = 0;
                datetypeView.setText("取车时间");
                setDate();
                setTime();
                if (dateLayout.getVisibility() == View.GONE) {
                    dateLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.carordermoner2_haiche_layout: // 还车

                if (qucheyear == 0 || quchemonth == 0 || qucheday == 0) {
                    PublicUtil.showToast(CarOrderMoneyActivity2.this, "请先选择取车时间", false);
                } else {
                    choosetimetype = 1;
                    datetypeView.setText("还车时间");
                    setDate();
                    setTime();
                    if (dateLayout.getVisibility() == View.GONE) {
                        dateLayout.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case R.id.carordermoner2_car_wangdianlayout: // 取车网店
                Intent intent = new Intent(CarOrderMoneyActivity2.this, CityStoresListActivity.class);
                startActivityForResult(intent, REQUESTCODE);
                break;

            case R.id.carordermoner2_tijiaodindgan: // 提交订单

                if (qucheyear == 0 || qucheday == 0 || quchemonth == 0) {
                    PublicUtil.showToast(CarOrderMoneyActivity2.this, "请选择取车日期", false);
                    return;
                }

                if (haicheyear == 0 || haichemonth == 0 || haicheday == 0) {
                    PublicUtil.showToast(CarOrderMoneyActivity2.this, "请选择还车日期", false);
                    return;
                }

                if (TextUtils.isEmpty(hc_store_id)) {
                    PublicUtil.showToast(CarOrderMoneyActivity2.this, "请选择取车/还车网点", false);
                    return;
                }
                if (!agreeCheckBox.isChecked()) {
                    PublicUtil.showToast(CarOrderMoneyActivity2.this, "请同意租车条款", false);
                    return;
                }

                start = qucheyear + "-" + oneTo2(quchemonth) + "-" + oneTo2(qucheday) + "+" + oneTo2(quchehour) + ":"
                        + oneTo2(quchefen) + ":00";
                end = haicheyear + "-" + oneTo2(haichemonth) + "-" + oneTo2(haicheday) + "+" + oneTo2(haichehour) + ":"
                        + oneTo2(haichefen) + ":00";

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ADD_ORDER);
                map.put("qc_time", start);
                map.put("hc_time", end);
                map.put("store_id", hc_store_id);
                map.put("is_safe", is_safe_check);
                map.put("car_type_id", car_type_id);
                map.put("is_iop", is_bujimianpei_check);

                AnsynHttpRequest.httpRequest(CarOrderMoneyActivity2.this, AnsynHttpRequest.GET, callBack,
                        Constent.ID_GET_ACT_ADD_ORDER, map, false, true, true);

                break;
            case R.id.carordermoner2_date_cancel:
                if (dateLayout.getVisibility() == View.VISIBLE) {
                    dateLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.carordermoney2_get_and_huan_info:  //取车还车
                if (getAndHuanInfoLayout.getVisibility() == View.VISIBLE) {
                    Intent getAdressIntent = new Intent(CarOrderMoneyActivity2.this, CityStoresListActivity.class);
                    startActivityForResult(getAdressIntent, REQUESTCODE);
                }
                break;
            case R.id.carordermoney_yajin_detail:  //押金声明
                Intent yajinDetailIntent = new Intent(this, YaJinDetailActivity.class);
                startActivity(yajinDetailIntent);
                break;
            case R.id.carordermoney_baoxian_detail:  //保险声明
                Intent baoxianDetailIntent = new Intent(this, BaoXianDetailActivity.class);
                startActivity(baoxianDetailIntent);
                break;
            case R.id.carordermoney_show_detail: // 查看详情
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog detailDialog = builder.create();
                if (wzNum == 0) {
                    if (payment == 0) {
                        View view = View.inflate(CarOrderMoneyActivity2.this, R.layout.view_show_detail_0_0, null);

                        ((TextView) view.findViewById(R.id.view_show_detail_yajin_count)).setText("￥" + PublicUtil.roundByScale(myyajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_car_yajin)).setText("￥" + PublicUtil.roundByScale(yajin, 2));
                        detailDialog.setView(view);
                        ((ImageView) view.findViewById(R.id.view_show_detail_close))
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        detailDialog.dismiss();
                                    }
                                });
                        detailDialog.show();

                    } else {
                        View view = View.inflate(CarOrderMoneyActivity2.this, R.layout.view_show_detail_0_1, null);
                        ((TextView) view.findViewById(R.id.view_show_detail_yajin_count)).setText("￥" + PublicUtil.roundByScale(myyajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_car_yajin)).setText("￥" + PublicUtil.roundByScale(yajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_fill_money))
                                .setText("￥" + PublicUtil.roundByScale(payment, 2));
                        detailDialog.setView(view);
                        ((ImageView) view.findViewById(R.id.view_show_detail_close))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        detailDialog.dismiss();
                                    }
                                });
                        detailDialog.show();
                    }
                } else if (wzNum == 1) {
                    if (payment == 0) {
                        View view = View.inflate(CarOrderMoneyActivity2.this, R.layout.view_show_detail_1_0, null);
                        ((TextView) view.findViewById(R.id.view_show_detail_yajin_count)).setText("￥" + PublicUtil.roundByScale(myyajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_car_yajin)).setText("￥" + PublicUtil.roundByScale(yajin, 2));
                        detailDialog.setView(view);
                        ((ImageView) view.findViewById(R.id.view_show_detail_close))
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        detailDialog.dismiss();
                                    }
                                });
                        detailDialog.show();
                    } else {
                        View view = View.inflate(CarOrderMoneyActivity2.this, R.layout.view_show_detail_1_1, null);
                        ((TextView) view.findViewById(R.id.view_show_detail_yajin_count)).setText("￥" + PublicUtil.roundByScale(myyajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_car_yajin)).setText("￥" + PublicUtil.roundByScale(yajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_fill_money)).setText("￥" + PublicUtil.roundByScale(payment, 2));
                        detailDialog.setView(view);
                        ((ImageView) view.findViewById(R.id.view_show_detail_close))
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        detailDialog.dismiss();
                                    }
                                });
                        detailDialog.show();
                    }
                } else if (wzNum == 2) {
                    if (payment == 0) {
                        View view = View.inflate(CarOrderMoneyActivity2.this, R.layout.view_show_detail_2_0, null);
                        ((TextView) view.findViewById(R.id.view_show_detail_yajin_count)).setText("￥" + PublicUtil.roundByScale(foregiftAcount, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_car_yajin)).setText("￥" + PublicUtil.roundByScale(yajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_wz_money)).setText("￥" + PublicUtil.roundByScale(freeze, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_fill_money)).setText("故本次不额外收取车辆押金");
                        detailDialog.setView(view);
                        ((ImageView) view.findViewById(R.id.view_show_detail_close))
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        detailDialog.dismiss();
                                    }
                                });
                        detailDialog.show();
                    } else {
                        View view = View.inflate(CarOrderMoneyActivity2.this, R.layout.view_show_detail_2_1, null);
                        ((TextView) view.findViewById(R.id.view_show_detail_yajin_count)).setText("￥" + PublicUtil.roundByScale(foregiftAcount, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_car_yajin)).setText("￥" + PublicUtil.roundByScale(yajin, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_wz_money)).setText("￥" + PublicUtil.roundByScale(freeze, 2));
                        ((TextView) view.findViewById(R.id.view_show_detail_fill_money)).setText("￥" + PublicUtil.roundByScale(payment, 2));
                        detailDialog.setView(view);
                        ((ImageView) view.findViewById(R.id.view_show_detail_close))
                                .setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        detailDialog.dismiss();
                                    }
                                });
                        detailDialog.show();
                    }
                }
                break;
            case R.id.carordermoner2_date_ok: // 确定选择的日期

                int datecount = wheelPicker.getSeletedIndex();
                int timecount = wheelPicker2.getSeletedIndex();

                if (choosetimetype == 0) {
                    qucheyear = (Integer) arrayList.get(datecount).get("year");
                    quchemonth = (Integer) arrayList.get(datecount).get("month");
                    qucheday = (Integer) arrayList.get(datecount).get("day");
                    if (datecount == 0) {
                        quchehour = parseInt((String) arrayList2.get(timecount).get("hour"));
                        quchefen = parseInt((String) arrayList2.get(timecount).get("fen"));
                    } else {
                        quchehour = parseInt((String) arrayList3.get(timecount).get("hour"));
                        quchefen = parseInt((String) arrayList3.get(timecount).get("fen"));
                    }
                    start = qucheyear + "-" + oneTo2(quchemonth) + "-" + oneTo2(qucheday) + " " + oneTo2(quchehour) + ":"
                            + oneTo2(quchefen) + ":00";

                    quchetimeView.setText(oneTo2(quchemonth) + "/" + oneTo2(qucheday) + "  " + oneTo2(quchehour) + ":"
                            + oneTo2(quchefen));
                    qucheweekView.setText((CharSequence) arrayList.get(datecount).get("week"));
                    qucheinforView.setVisibility(View.GONE);
                    quchetimeView.setVisibility(View.VISIBLE);
                    qucheweekView.setVisibility(View.VISIBLE);
                    qucheLayout.setBackgroundResource(R.drawable.image_date_green);

                    haicheinforView.setVisibility(View.VISIBLE);
                    haicheLayout.setBackgroundResource(R.drawable.image_05);
                    haichetimeView.setText("");
                    haicheweekView.setText("");
                    haichetimeView.setVisibility(View.GONE);
                    haicheweekView.setVisibility(View.GONE);

                    zuchezujin = 0;
                    car_timeView.setText("");
                    car_feiyongView.setText("");

                    haicheyear = 0;
                    haichemonth = 0;
                    haicheday = 0;
                    haichehour = 0;
                    haichefen = 0;
                } else {
                    isSelectTime = true;
                    haicheyear = (Integer) arrayList.get(datecount).get("year");
                    haichemonth = (Integer) arrayList.get(datecount).get("month");
                    haicheday = (Integer) arrayList.get(datecount).get("day");
                    if (datecount == 0) {
                        haichehour = parseInt((String) arrayList2.get(timecount).get("hour"));
                        haichefen = parseInt((String) arrayList2.get(timecount).get("fen"));
                    } else {
                        haichehour = parseInt((String) arrayList3.get(timecount).get("hour"));
                        haichefen = parseInt((String) arrayList3.get(timecount).get("fen"));
                    }

                    end = haicheyear + "-" + oneTo2(haichemonth) + "-" + oneTo2(haicheday) + " " + oneTo2(haichehour) + ":"
                            + oneTo2(haichefen) + ":00";
                    haichetimeView.setText(oneTo2(haichemonth) + "/" + oneTo2(haicheday) + "  " + oneTo2(haichehour) + ":"
                            + oneTo2(haichefen));
                    haicheweekView.setText((CharSequence) arrayList.get(datecount).get("week"));
                    haicheinforView.setVisibility(View.GONE);
                    haichetimeView.setVisibility(View.VISIBLE);
                    haicheweekView.setVisibility(View.VISIBLE);
                    haicheLayout.setBackgroundResource(R.drawable.image_date_green);
                    jisuanZujin_shijan();
                    setTotalMoneyDetail(basebaoxian, bujimianpei
                    );

                }

                dateLayout.setVisibility(View.GONE);

                break;

            default:
                break;
        }

    }


    private JSONObject jsonObject = null;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub

            switch (backId) {
                case Constent.ID_GET_ACT_ADD_ORDER:

                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_http_getcardetail);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getcardetail;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;
                case Constent.ID_MEMBER_INFOR:

                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backstr);
                                if (jsonObject.getString("errcode").equals("0")) {
                                    String yajin = PublicUtil.roundByScale(Double.parseDouble(jsonObject.optJSONObject("data").getString("foregift_acount")), 2);
                                    myyajin = Double.parseDouble(yajin);
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getcardetail;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;

                default:
                    break;
            }

        }
    };

    private int error_http_getcardetail = 0x9400;
    private int success_http_getcardetail = 0x9401;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_http_getcardetail) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarOrderMoneyActivity2.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getcardetail) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONObject dataObject = jsonObject.getJSONObject("data");

                            if (dataObject != null) {

                                Intent intent = new Intent(CarOrderMoneyActivity2.this,
                                        CarRestListDetailActivity.class);
                                intent.putExtra("type", "new");
                                intent.putExtra("licheng", car_lichengView.getText().toString());
                                intent.putExtra("order_no", dataObject.getString("order_no"));
                                startActivity(intent);
                            }

                        } else {
                            PublicUtil.showToast(CarOrderMoneyActivity2.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(CarOrderMoneyActivity2.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            }

        }

        ;
    };
    private double freeze;
    private int zc_month;
    private int zc_day;
    private int zc_hour;
    private float payment;

    /**
     * 根据年 月 获取对应的月份 天数
     */
    private int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据年 月 日 获取星期几
     */
    private String getWeekByYearMonth(int year, int month, int day) {

        String str = "";

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, day);
        a.roll(Calendar.DAY_OF_WEEK, -1);
        int firstweek = a.get(Calendar.DAY_OF_WEEK);

        switch (firstweek) {

            case 1:
                str = "周一";
                break;
            case 2:
                str = "周二";
                break;
            case 3:
                str = "周三";
                break;
            case 4:
                str = "周四";
                break;
            case 5:
                str = "周五";
                break;
            case 6:
                str = "周六";
                break;
            case 7:
                str = "周日";
                break;

            default:
                break;
        }

        return str;
    }

    /**
     * 设置还取车日期
     */
    private void setDate() {
        arrayList.clear();
        int nowmonth;
        int nowyear;
        int nowday;
        if (choosetimetype == 0) {// 取车
            nowyear = year;
            nowmonth = month;
            nowday = day;
            int alldays = getDaysByYearMonth(nowyear, nowmonth);
            nowday = nowday + 1;
            if (nowday > alldays) {
                nowday = 1;
                nowmonth = nowmonth + 1;
            }

            if (nowmonth > 12) {
                nowmonth = 1;
                nowyear = nowyear + 1;
            }

            alldays = getDaysByYearMonth(nowyear, nowmonth);

            int tianshu = alldays - nowday + 1;
            if (tianshu > 7) {
                alldays = nowday + 7 - 1;
            }

            for (int i = nowday; i <= alldays; i++) {
                Map<String, Object> map = new HashMap<String, Object>();

                if (nowmonth == month && i == day) {
                    map.put("infor", nowmonth + "月" + i + "日        今天");
                    map.put("week", "今天");
                } else {
                    map.put("infor", nowmonth + "月" + i + "日        " + getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                }
                map.put("year", nowyear);
                map.put("month", nowmonth);
                map.put("day", i);
                arrayList.add(map);

            }

            nowmonth = nowmonth + 1;
            if (nowmonth > 12) {
                nowmonth = 1;
                nowyear = nowyear + 1;

            }

            if (tianshu < 7) {
                int alldays2 = 7 - tianshu;

                for (int i = 1; i <= alldays2; i++) {

                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("infor", nowmonth + "月" + i + "日        " + getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("year", nowyear);
                    map.put("month", nowmonth);
                    map.put("day", i);
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                    arrayList.add(map);
                }

                nowmonth = nowmonth + 1;
                if (nowmonth > 12) {
                    nowmonth = 1;
                    nowyear = nowyear + 1;
                }
            }

        } else {// 还车

            nowyear = qucheyear;
            nowmonth = quchemonth;
            nowday = qucheday;
            int time = PublicUtil.getTimeFromDate(nowyear + "-" + PublicUtil.oneTo2(String.valueOf(nowmonth)) + "-"
                    + PublicUtil.oneTo2(String.valueOf(nowday)) + " 00:00:00");

            String timestr = PublicUtil.getTime2(String.valueOf(time));

            nowyear = parseInt(timestr.split("-")[0]);
            nowmonth = parseInt(timestr.split("-")[1]);
            nowday = parseInt(timestr.split("-")[2]);

            int alldays = getDaysByYearMonth(nowyear, nowmonth);
            for (int i = nowday; i <= alldays; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("infor", nowmonth + "月" + i + "日        " + getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("year", nowyear);
                map.put("month", nowmonth);
                map.put("day", i);

                arrayList.add(map);

            }
            alldays = alldays - nowday + 1;
            nowmonth = nowmonth + 1;
            if (nowmonth > 12) {
                nowmonth = 1;
                nowyear = nowyear + 1;

            }

            int alldays2 = getDaysByYearMonth(nowyear, nowmonth);

            for (int i = 1; i <= alldays2; i++) {

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("infor", nowmonth + "月" + i + "日        " + getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("year", nowyear);
                map.put("month", nowmonth);
                map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("day", i);
                arrayList.add(map);
            }

            nowmonth = nowmonth + 1;
            if (nowmonth > 12) {
                nowmonth = 1;
                nowyear = nowyear + 1;
            }
            int alldays3 = getDaysByYearMonth(nowyear, nowmonth);

            if ((alldays + alldays2 + alldays3) > 90) {
                alldays3 = 90 - alldays - alldays2;
            }

            for (int i = 1; i <= alldays3; i++) {

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("infor", nowmonth + "月" + i + "日        " + getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("year", nowyear);
                map.put("month", nowmonth);
                map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("day", i);
                arrayList.add(map);
            }

            if ((alldays + alldays2 + alldays3) < 90) {
                nowmonth = nowmonth + 1;
                if (nowmonth > 12) {
                    nowmonth = 1;
                    nowyear = nowyear + 1;
                }
                int alldays4 = getDaysByYearMonth(nowyear, nowmonth);

                if ((alldays + alldays2 + alldays3 + alldays4) > 90) {
                    alldays4 = 90 - alldays - alldays2 - alldays3;
                }
                for (int i = 1; i <= alldays4; i++) {

                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("infor", nowmonth + "月" + i + "日        " + getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("year", nowyear);
                    map.put("month", nowmonth);
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("day", i);
                    arrayList.add(map);
                }

            }

        }
        pickLayout.removeAllViews();
        wheelPicker.setDate(arrayList);
        pickLayout.addView(wheelPicker);

    }

    /**
     * 设置还取车时间
     */
    private void setTime() {
        arrayList2.clear();
        int hourstart;
        if (choosetimetype == 0) {// 取车
            //            if (hour >= 18) {
            //                hourstart = 0;
            //            } else if (hour < 9) {
            //                hourstart = 0;
            //            } else {
            //
            //                int nowhour = hour;
            //
            //                hourstart = (nowhour - 9) * 2;
            //                if (fen > 0 && fen <= 30) {
            //                    hourstart = hourstart + 1;
            //                } else if (fen > 30) {
            //                    hourstart = hourstart + 2;
            //                }
            //
            //            }

            for (int i = 0; i < hours.length; i++) {

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("infor", hours[i]);
                map.put("hour", hours[i].split(":")[0]);
                map.put("fen", hours[i].split(":")[1]);

                arrayList2.add(map);

            }

        } else {// 还车//短租一天起租
            hourstart = (quchehour - 8) * 2;
            for (int i = hourstart; i < hours.length; i++) {

                Map<String, Object> map = new HashMap<String, Object>();

                map.put("infor", hours[i]);
                map.put("hour", hours[i].split(":")[0]);
                map.put("fen", hours[i].split(":")[1]);

                arrayList2.add(map);

            }

        }
        pickLayout2.removeAllViews();
        wheelPicker2.setDate(arrayList2);
        pickLayout2.addView(wheelPicker2);
    }

    /**
     * 数据补0
     *
     * @param in
     * @return
     */

    private String oneTo2(int in) {
        String str;

        if (in < 10) {
            str = "0" + in;
        } else {
            str = String.valueOf(in);
        }

        return str;
    }

    /**
     * 根据不同情况计算租金和时间
     */
    private void jisuanZujin_shijan() {

        if (qucheyear == 0 || qucheday == 0 || quchemonth == 0 || haicheyear == 0 || haichemonth == 0
                || haicheday == 0) {
            return;
        }
        String start = qucheyear + "-" + oneTo2(quchemonth) + "-" + oneTo2(qucheday) + " " + oneTo2(quchehour) + ":"
                + oneTo2(quchefen);
        String end = haicheyear + "-" + oneTo2(haichemonth) + "-" + oneTo2(haicheday) + " " + oneTo2(haichehour) + ":"
                + oneTo2(haichefen);

        int alltime = PublicUtil.compareFen(start, end); // 两个时间之间的分钟

        if (alltime % 60 != 0) {
            alltime = alltime / 60 + 1; // 小时
        } else {
            alltime = alltime / 60;
        }
        zc_month = alltime / (24 * 30);
        zc_day = (alltime - zc_month * 24 * 30) / 24;
        zc_hour = alltime - zc_month * 24 * 30 - zc_day * 24;
        zuchezujin = zc_month * zujin_month + zc_day * zujin_day + zc_hour * zujin_hour;

        String str = "";
        if (zc_month > 0) {
            str = zc_month + "个月";
        }
        if (zc_day > 0) {
            str = str + zc_day + "天";
        }
        if (zc_hour > 0) {
            str = str + zc_hour + "小时";
        }
        if ("1".equals(is_safe_check)) { // 买了保险
            if (zc_hour > 0) {
                basebaoxian = basebaoxiantmp * zc_month * 30 + (zc_day + 1) * basebaoxiantmp;
            } else {
                basebaoxian = basebaoxiantmp * zc_month * 30 + zc_day * basebaoxiantmp;
            }
        }
        car_timeView.setText(str);
        zuchezujin = PublicUtil.toTwo(zuchezujin);
        basebaoxian = PublicUtil.toTwo(basebaoxian);
        car_feiyongView.setText("￥" + PublicUtil.roundByScale(zuchezujin, 2));
        moneyView.setText("￥" + PublicUtil.roundByScale(zuchezujin + payment + basebaoxian, 2));
        moneyinforView.setText("(含:租车费用" + PublicUtil.roundByScale(zuchezujin, 2) + "元,租车押金" + PublicUtil.roundByScale(payment, 2) + "元,保险" + PublicUtil.roundByScale(basebaoxian, 2) + "元)");

    }

    private void setTotalMoneyDetail(float basebaoxian, float bujimianpei) {
        // TODO Auto-generated method stub
        if (basebaoxian == 0 && bujimianpei == 0) {
            moneyinforView.setText("(含:租车费用" + PublicUtil.roundByScale(zuchezujin, 2) + "元,租车押金" + PublicUtil.roundByScale(payment, 2) + "元)");
            moneyView.setText("￥" + PublicUtil.roundByScale(zuchezujin + payment + basebaoxian + bujimianpei, 2));
        } else if (basebaoxian != 0 && bujimianpei != 0) {
            if (zc_hour > 0) {
                basebaoxian = basebaoxiantmp * zc_month * 30 + (zc_day + 1) * basebaoxiantmp;
                bujimianpei = bujimianpeitmp * zc_month * 30 + (zc_day + 1) * bujimianpeitmp;
            } else {
                basebaoxian = basebaoxiantmp * zc_month * 30 + zc_day * basebaoxiantmp;
                bujimianpei = bujimianpeitmp * zc_month * 30 + zc_day * bujimianpeitmp;
            }
            moneyinforView.setText(
                    "(含:租车费用" + PublicUtil.roundByScale(zuchezujin, 2) + "元,租车押金" + PublicUtil.roundByScale(payment, 2) + "元,保险" + PublicUtil.roundByScale(basebaoxian, 2) + "元,不计免赔" + PublicUtil.roundByScale(bujimianpei, 2) + "元)");
            moneyView.setText("￥" + PublicUtil.roundByScale(zuchezujin + payment + basebaoxian + bujimianpei, 2));
        } else if (basebaoxian != 0 && bujimianpei == 0) {
            if (zc_hour > 0) {
                basebaoxian = basebaoxiantmp * zc_month * 30 + (zc_day + 1) * basebaoxiantmp;
            } else {
                basebaoxian = basebaoxiantmp * zc_month * 30 + zc_day * basebaoxiantmp;
            }
            moneyinforView.setText("(含:租车费用" + PublicUtil.roundByScale(zuchezujin, 2) + "元,租车押金" + PublicUtil.roundByScale(payment, 2) + "元,保险" + PublicUtil.roundByScale(basebaoxian, 2) + "元");
            moneyView.setText("￥" + PublicUtil.roundByScale(zuchezujin + payment + basebaoxian + bujimianpei, 2));
        } else if (basebaoxian == 0 && bujimianpei != 0) {
            if (zc_hour > 0) {
                bujimianpei = bujimianpeitmp * zc_month * 30 + (zc_day + 1) * bujimianpeitmp;
            } else {
                bujimianpei = bujimianpeitmp * zc_month * 30 + zc_day * bujimianpeitmp;
            }
            PublicUtil.setStorage_string(CarOrderMoneyActivity2.this, "bujimianpei", bujimianpei + "");
            moneyinforView.setText("(含:租车费用" + PublicUtil.roundByScale(zuchezujin, 2) + "元,租车押金" + PublicUtil.roundByScale(payment, 2) + "元," + "不计免赔" + PublicUtil.roundByScale(bujimianpei, 2) + "元)");
            moneyView.setText("￥" + PublicUtil.roundByScale((zuchezujin + payment + basebaoxian + bujimianpei), 2));
        }
    }

}
