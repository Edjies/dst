package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
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

/**
 * 时租计费界面
 *
 * @author qtds
 */
public class CarOrderMoneyActivity extends BaseActivity implements
        OnClickListener {
    private final String TAG = "CarOrderMoneyActivity";
    private final int REQUESTCODE = 0x7410;
    private FrameLayout topLayout;
    private TextView backView;
    private LinearLayout qucheLayout, haicheLayout;
    private TextView quchetimeView, haichetimeView, qucheweekView,
            haicheweekView, qucheinforView, haicheinforView;
    private TextView car_timeView, car_feiyongView, car_lichengView,
            car_yajinView;
    private TextView quchewangdianView, haichewangdianView;
    private TextView moneyView, moneyinforView, moneyinforView2,
            moneyinforView3;
    private TextView okView;
    private FrameLayout dateLayout;
    private TextView datecancelView, datetypeView, dateokView;
    private WheelView wheelPicker, wheelPicker2;
    private LinearLayout pickLayout, pickLayout2;

    private int year, month, day, hour, fen;
    private int qucheyear, quchemonth, qucheday, quchehour, quchefen;
    private int haicheyear, haichemonth, haicheday, haichehour, haichefen;
    private Calendar calendar = Calendar.getInstance();
    private int choosetimetype = 0;// 0 取车时间 1还车时间
    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();// 全部时间
    private String[] hours = {"09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};

    // private LinearLayout viewpagecountLayout;
    private int dataindex = 0;// 检测日期滑动的标志
    private float zuchezujin = 0;// 按时间计算出的租车租金
    private float yajin, zujin_hour, zujin_day, zujin_month;
    private String hc_store_id;
    private TextView baoxianmoneyView;
    private CheckBox radioButton;
    private String is_safe_check = "1";// 保险是否购买
    private float zongbaoxian = 0, baoxian_p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carordermoney);
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

        topLayout = (FrameLayout) findViewById(R.id.carordermoner_top_layout);
        if (android.os.Build.VERSION.SDK_INT < 19) {
            topLayout.setPadding(0, 0, 0, 0);
        }
        backView = (TextView) findViewById(R.id.carordermoner_back);
        qucheLayout = (LinearLayout) findViewById(R.id.carordermoner_quche_layout);
        haicheLayout = (LinearLayout) findViewById(R.id.carordermoner_haiche_layout);
        quchetimeView = (TextView) findViewById(R.id.carordermoner_quche_time);
        haichetimeView = (TextView) findViewById(R.id.carordermoner_haiche_time);
        qucheweekView = (TextView) findViewById(R.id.carordermoner_quche_week);
        haicheweekView = (TextView) findViewById(R.id.carordermoner_haiche_week);
        qucheinforView = (TextView) findViewById(R.id.carordermoner_quche_infor);
        haicheinforView = (TextView) findViewById(R.id.carordermoner_haiche_infor);
        car_timeView = (TextView) findViewById(R.id.carordermoner_car_time);
        car_feiyongView = (TextView) findViewById(R.id.carordermoner_car_zuchefeiyong);
        car_lichengView = (TextView) findViewById(R.id.carordermoner_car_lichengfeiyong);
        quchewangdianView = (TextView) findViewById(R.id.carordermoner_car_quchewangdian);
        haichewangdianView = (TextView) findViewById(R.id.carordermoner_car_haichewangdian);
        moneyView = (TextView) findViewById(R.id.carordermoner_infor_money);
        moneyinforView = (TextView) findViewById(R.id.carordermoner_infor_infor);
        moneyinforView2 = (TextView) findViewById(R.id.carordermoner_infor_infor2);
        moneyinforView3 = (TextView) findViewById(R.id.carordermoner_infor_infor3);
        okView = (TextView) findViewById(R.id.carordermoner_tijiaodindgan);
        car_yajinView = (TextView) findViewById(R.id.carordermoner_car_cheliangyajin);

        dateLayout = (FrameLayout) findViewById(R.id.carordermoner_date_layout);
        datecancelView = (TextView) findViewById(R.id.carordermoner_date_cancel);
        datetypeView = (TextView) findViewById(R.id.carordermoner_date_type);
        dateokView = (TextView) findViewById(R.id.carordermoner_date_ok);
        pickLayout = (LinearLayout) findViewById(R.id.carordermoner_date_picklayout);
        pickLayout2 = (LinearLayout) findViewById(R.id.carordermoner_date_picklayout2);

        backView.setOnClickListener(this);

        qucheLayout.setOnClickListener(this);
        haicheLayout.setOnClickListener(this);
        okView.setOnClickListener(this);
        datecancelView.setOnClickListener(this);
        dateokView.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        quchetimeView.setVisibility(View.GONE);
        haichetimeView.setVisibility(View.GONE);
        qucheweekView.setVisibility(View.GONE);
        haicheweekView.setVisibility(View.GONE);
        qucheinforView.setVisibility(View.VISIBLE);
        haicheinforView.setVisibility(View.VISIBLE);

        baoxianmoneyView = (TextView) findViewById(R.id.carinfor_baoxian_money);
        radioButton = (CheckBox) findViewById(R.id.carinfor_baoxian_open);

        if (!TextUtils.isEmpty(getIntent().getStringExtra("safe_amount"))) {
            baoxian_p = Float.parseFloat(getIntent().getStringExtra(
                    "safe_amount"));
        }

        baoxianmoneyView.setText("￥" + baoxian_p + "/天");

        radioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub

                PublicUtil.logDbug(TAG, "@@@@@@@+" + arg1, 0);
                if (arg1) {
                    is_safe_check = "1";
                    moneyView.setText("￥"
                            + PublicUtil
                            .toTwo((zuchezujin + yajin + zongbaoxian)));
                    moneyinforView.setText("(含:租车费用" + zuchezujin + "元,车辆押金"
                            + yajin + "元,保险" + zongbaoxian + "元)");
                } else {
                    is_safe_check = "0";
                    moneyView.setText("￥"
                            + PublicUtil.toTwo((zuchezujin + yajin)));
                    moneyinforView.setText("(含:租车费用" + zuchezujin + "元,车辆押金"
                            + yajin + "元,保险0元)");
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

        wheelPicker = new WheelView(CarOrderMoneyActivity.this);
        wheelPicker2 = new WheelView(CarOrderMoneyActivity.this);

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
                } else if (wheelPicker.getSeletedIndex() != 0) {
                    pickLayout2.addView(wheelPicker2);
                    wheelPicker2.setDate(arrayList3);
                    pickLayout2.removeAllViews();
                    pickLayout2.addView(wheelPicker2);
                }

            }
        });

        if (!TextUtils
                .isEmpty(getIntent().getStringExtra("zc_foregift_amount"))) {
            yajin = Float.parseFloat(getIntent().getStringExtra(
                    "zc_foregift_amount"));
        }

        if (!TextUtils.isEmpty(getIntent().getStringExtra("hour_price"))) {
            zujin_hour = Float.parseFloat(getIntent().getStringExtra(
                    "hour_price"));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("day_price"))) {
            zujin_day = Float.parseFloat(getIntent()
                    .getStringExtra("day_price"));
        }
        if (!TextUtils.isEmpty(getIntent().getStringExtra("month_price"))) {
            zujin_month = Float.parseFloat(getIntent().getStringExtra(
                    "month_price"));
        }

        quchewangdianView.setText(getIntent().getStringExtra("store_name")
                + "(" + getIntent().getStringExtra("addr") + ")");
        haichewangdianView.setText(getIntent().getStringExtra("store_name")
                + "(" + getIntent().getStringExtra("addr") + ")");
        car_lichengView.setText("起步"
                + getIntent().getStringExtra("lowest_price") + "元("
                + getIntent().getStringExtra("lowest_mileage") + "公里内)+"
                + getIntent().getStringExtra("mileage_price") + "元/公里");
        car_yajinView.setText("￥" + yajin);

        hc_store_id = getIntent().getStringExtra("store_id");// 默认还车到借车网店

        moneyView.setText("￥"
                + PublicUtil.toTwo(zuchezujin + yajin + zongbaoxian));
        moneyinforView.setText("(含:租车费用" + zuchezujin + "元,车辆押金" + yajin
                + "元,保险" + zongbaoxian + "元)");
        // moneyinforView2.setText("违章押金将在还车时支付");
        // moneyinforView3.setText("租车费用和里程费用将在还车后从车辆押金中扣除进行结算");

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_CAR_GET_DISPATCH_RECORD);
        map.put("car_no",
                PublicUtil.toUTF(getIntent().getStringExtra("car_no")));
        AnsynHttpRequest.httpRequest(CarOrderMoneyActivity.this,
                AnsynHttpRequest.GET, callBack,
                Constent.ID_CAR_GET_DISPATCH_RECORD, map, false, false, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE) {
            if (resultCode == 12 && data != null) {
                hc_store_id = data.getStringExtra("id");
                haichewangdianView.setText(data.getStringExtra("name") + "("
                        + data.getStringExtra("addr") + ")");
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        String start, end;
        switch (arg0.getId()) {
            case R.id.carordermoner_back:

                finish();
                break;

            case R.id.carordermoner_quche_layout:
                choosetimetype = 0;
                datetypeView.setText("取车时间");
                setDate();
                setTime();
                if (dateLayout.getVisibility() == View.GONE) {
                    dateLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.carordermoner_haiche_layout:

                if (qucheyear == 0 || quchemonth == 0 || qucheday == 0) {
                    PublicUtil.showToast(CarOrderMoneyActivity.this, "请先选择取车时间",
                            false);
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
            case R.id.carordermoner_car_haichewangdian:
                Intent intent = new Intent(CarOrderMoneyActivity.this,
                        CityStoresListActivity.class);
                startActivityForResult(intent, REQUESTCODE);
                break;

            case R.id.carordermoner_tijiaodindgan:

                if (qucheyear == 0 || qucheday == 0 || quchemonth == 0) {
                    PublicUtil.showToast(CarOrderMoneyActivity.this, "请选择取车日期",
                            false);
                    return;
                }

                if (haicheyear == 0 || haichemonth == 0 || haicheday == 0) {
                    PublicUtil.showToast(CarOrderMoneyActivity.this, "请选择还车日期",
                            false);
                    return;
                }

                start = qucheyear + "-" + oneTo2(quchemonth) + "-"
                        + oneTo2(qucheday) + "+" + oneTo2(quchehour) + ":"
                        + oneTo2(quchefen) + ":00";
                end = haicheyear + "-" + oneTo2(haichemonth) + "-"
                        + oneTo2(haicheday) + "+" + oneTo2(haichehour) + ":"
                        + oneTo2(haichefen) + ":00";

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ORDER_ADD);
                // map.put("zc_type", String.valueOf(timetype));
                map.put("car_no",
                        PublicUtil.toUTF(getIntent().getStringExtra("car_no")));
                map.put("qc_time", start);
                map.put("hc_time", end);
                map.put("hc_store_id", hc_store_id);
                map.put("is_safe", is_safe_check);

                AnsynHttpRequest.httpRequest(CarOrderMoneyActivity.this,
                        AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_ADD, map,
                        false, true, true);

                break;
            case R.id.carordermoner_date_layout:

                break;
            case R.id.carordermoner_date_cancel:
                if (dateLayout.getVisibility() == View.VISIBLE) {
                    dateLayout.setVisibility(View.GONE);
                }

                break;

            case R.id.carordermoner_date_ok:

                int datecount = wheelPicker.getSeletedIndex();
                int timecount = wheelPicker2.getSeletedIndex();

                PublicUtil.logDbug(TAG, "datecount" + datecount + ":timecount"
                        + timecount, 0);

                if (choosetimetype == 0) {
                    qucheyear = (Integer) arrayList.get(datecount).get("year");
                    quchemonth = (Integer) arrayList.get(datecount).get("month");
                    qucheday = (Integer) arrayList.get(datecount).get("day");
                    if (datecount == 0) {
                        quchehour = Integer.parseInt((String) arrayList2.get(
                                timecount).get("hour"));
                        quchefen = Integer.parseInt((String) arrayList2.get(
                                timecount).get("fen"));
                    } else {
                        quchehour = Integer.parseInt((String) arrayList3.get(
                                timecount).get("hour"));
                        quchefen = Integer.parseInt((String) arrayList3.get(
                                timecount).get("fen"));
                    }

                    start = qucheyear + "-" + oneTo2(quchemonth) + "-"
                            + oneTo2(qucheday) + " " + oneTo2(quchehour) + ":"
                            + oneTo2(quchefen) + ":00";

                    if (checkdate(start, 0)) {
                        quchetimeView.setText(oneTo2(quchemonth) + "/"
                                + oneTo2(qucheday) + "  " + oneTo2(quchehour) + ":"
                                + oneTo2(quchefen));
                        qucheweekView.setText((CharSequence) arrayList.get(
                                datecount).get("week"));
                        qucheinforView.setVisibility(View.GONE);
                        quchetimeView.setVisibility(View.VISIBLE);
                        qucheweekView.setVisibility(View.VISIBLE);
                        qucheLayout
                                .setBackgroundResource(R.drawable.image_date_green);
                    } else {
                        qucheyear = 0;
                        quchemonth = 0;
                        qucheday = 0;
                        quchehour = 0;
                        quchefen = 0;

                        PublicUtil.showToast(CarOrderMoneyActivity.this,
                                "在您所选择的时间段内，车辆已有预订记录，请调整您的租车时间", false);

                    }

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
                    zongbaoxian = 0;

                    moneyView.setText("￥"
                            + PublicUtil.toTwo(zuchezujin + yajin + zongbaoxian));
                    moneyinforView.setText("(含:租车费用" + zuchezujin + "元,车辆押金"
                            + yajin + "元,保险" + zongbaoxian + "元)");

                } else {

                    haicheyear = (Integer) arrayList.get(datecount).get("year");
                    haichemonth = (Integer) arrayList.get(datecount).get("month");
                    haicheday = (Integer) arrayList.get(datecount).get("day");
                    if (datecount == 0) {
                        haichehour = Integer.parseInt((String) arrayList2.get(
                                timecount).get("hour"));
                        haichefen = Integer.parseInt((String) arrayList2.get(
                                timecount).get("fen"));
                    } else {
                        haichehour = Integer.parseInt((String) arrayList3.get(
                                timecount).get("hour"));
                        haichefen = Integer.parseInt((String) arrayList3.get(
                                timecount).get("fen"));
                    }

                    end = haicheyear + "-" + oneTo2(haichemonth) + "-"
                            + oneTo2(haicheday) + " " + oneTo2(haichehour) + ":"
                            + oneTo2(haichefen) + ":00";

                    if (checkdate(end, 1)) {
                        haichetimeView.setText(oneTo2(haichemonth) + "/"
                                + oneTo2(haicheday) + "  " + oneTo2(haichehour)
                                + ":" + oneTo2(haichefen));
                        haicheweekView.setText((CharSequence) arrayList.get(
                                datecount).get("week"));
                        haicheinforView.setVisibility(View.GONE);
                        haichetimeView.setVisibility(View.VISIBLE);
                        haicheweekView.setVisibility(View.VISIBLE);
                        haicheLayout
                                .setBackgroundResource(R.drawable.image_date_green);

                        PublicUtil.logDbug(TAG, qucheyear + ":" + quchemonth + ":"
                                + qucheday + ":" + quchehour + ":" + quchefen, 0);
                        PublicUtil.logDbug(TAG, haicheyear + ":" + haichemonth
                                + ":" + haicheday + ":" + haichehour + ":"
                                + haichefen, 0);
                        jisuanZujin_shijan();

                    } else {
                        haicheyear = 0;
                        haichemonth = 0;
                        haicheday = 0;
                        haichehour = 0;
                        haichefen = 0;
                        PublicUtil.showToast(CarOrderMoneyActivity.this,
                                "在您所选择的时间段内，车辆已有预订记录，请调整您的租车时间", false);
                    }

                }

                dateLayout.setVisibility(View.GONE);

                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (dateLayout.getVisibility() == View.VISIBLE) {
                dateLayout.setVisibility(View.GONE);
                return true;
            } else {

                finish();
                return true;
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private JSONObject jsonObject = null;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub

            switch (backId) {
                case Constent.ID_ORDER_ADD:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
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
                case Constent.ID_CAR_GET_DISPATCH_RECORD:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
                                handler.sendEmptyMessage(success_http_getcartime);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getcartime;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;
                default:
                    break;
            }

        }
    };

    private int error_http_getcardetail = 0x7400;
    private int success_http_getcardetail = 0x7401;
    private int error_http_getcartime = 0x7403;
    private int success_http_getcartime = 0x7404;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_http_getcardetail) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarOrderMoneyActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getcardetail) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONObject dataObject = jsonObject
                                    .getJSONObject("data");

                            if (dataObject != null) {

                                Intent intent = new Intent(
                                        CarOrderMoneyActivity.this,
                                        CarRestListDetailActivity.class);
                                intent.putExtra("type", "new");
                                intent.putExtra("licheng", car_lichengView
                                        .getText().toString());
                                intent.putExtra("order_no",
                                        dataObject.getString("order_no"));
                                startActivity(intent);
                                finish();

                            }

                        } else {
                            PublicUtil.showToast(CarOrderMoneyActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(
                                        CarOrderMoneyActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(CarOrderMoneyActivity.this,
                                "配置解析数据错误，请退出重试", false);
                        e.printStackTrace();
                    }

                }

            }

            if (msg != null && msg.what == error_http_getcartime) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarOrderMoneyActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getcartime) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONArray dateArray = jsonObject
                                    .getJSONArray("data");

                            if (dateArray != null && dateArray.length() > 0) {

                                dayFree(dateArray);

                            }

                        } else {
                            PublicUtil.showToast(CarOrderMoneyActivity.this,
                                    jsonObject.getString("msg"), false);
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

            if (hour >= 18) {
                nowday = nowday + 1;
            }

            if (nowday > alldays) {
                nowday = 1;
                nowmonth = nowmonth + 1;
            }

            if (nowmonth > 12) {
                nowmonth = 1;
                nowyear = nowyear + 1;
            }

            alldays = getDaysByYearMonth(nowyear, nowmonth);

            int tianshu = alldays - nowday + 1; //剩余天数
            if (tianshu > 7) {
                alldays = nowday + 7 - 1;
            }

            for (int i = nowday; i <= alldays; i++) {
                Map<String, Object> map = new HashMap<String, Object>();
                if (nowmonth == month && i == day) {
                    map.put("infor", nowmonth + "月" + i + "日        今天");
                    map.put("week", "今天");
                } else {
                    map.put("infor", nowmonth + "月" + i + "日        "
                            + getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                }
                map.put("year", nowyear);
                map.put("month", nowmonth);
                map.put("day", i);
                arrayList.add(map);

            }

            if (tianshu < 7) {
                nowmonth = nowmonth + 1;
                if (nowmonth > 12) {
                    nowmonth = 1;
                    nowyear = nowyear + 1;
                }
                int alldays2 = 7 - tianshu;
                for (int i = 1; i <= alldays2; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("infor", nowmonth + "月" + i + "日        "
                            + getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("year", nowyear);
                    map.put("month", nowmonth);
                    map.put("day", i);
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                    arrayList.add(map);
                }
            }

        } else {// 还车

            nowyear = qucheyear;
            nowmonth = quchemonth;
            nowday = qucheday;

            if (quchehour == 18 || (quchehour == 17 && quchefen == 30)) {
                int time = PublicUtil.getTimeFromDate(nowyear + "-"
                        + PublicUtil.oneTo2(String.valueOf(nowmonth)) + "-"
                        + PublicUtil.oneTo2(String.valueOf(nowday))
                        + " 00:00:00");
                time = time + 86400;// 后一天

                String timestr = PublicUtil.getTime2(String.valueOf(time));

                nowyear = Integer.parseInt(timestr.split("-")[0]);
                nowmonth = Integer.parseInt(timestr.split("-")[1]);
                nowday = Integer.parseInt(timestr.split("-")[2]);
            }

            int alldays = getDaysByYearMonth(nowyear, nowmonth);
            for (int i = nowday; i <= alldays; i++) {
                Map<String, Object> map = new HashMap<String, Object>();

                if (i == day) {
                    map.put("infor", nowmonth + "月" + i + "日        今天");
                    map.put("week", "今天");
                } else {
                    map.put("infor", nowmonth + "月" + i + "日        "
                            + getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                }
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

                map.put("infor", nowmonth + "月" + i + "日        "
                        + getWeekByYearMonth(nowyear, nowmonth, i));
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

                map.put("infor", nowmonth + "月" + i + "日        "
                        + getWeekByYearMonth(nowyear, nowmonth, i));
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

                    map.put("infor", nowmonth + "月" + i + "日        "
                            + getWeekByYearMonth(nowyear, nowmonth, i));
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
        if (choosetimetype == 0) {//取车
            if (hour >= 18) {
                hourstart = 0;
            } else if (hour < 9) {
                hourstart = 0;
            } else {
                int nowhour = hour;
                hourstart = (nowhour - 9) * 2;
                if (fen > 0 && fen <= 30) {
                    hourstart = hourstart + 1;
                } else if (fen > 30) {
                    hourstart = hourstart + 2;
                }

            }

            for (int i = hourstart; i < hours.length; i++) {

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("infor", hours[i]);
                map.put("hour", hours[i].split(":")[0]);
                map.put("fen", hours[i].split(":")[1]);

                arrayList2.add(map);

            }

        } else {// 还车
            if (quchehour == 18) {
                hourstart = 0;
            } else if (quchehour == 17 && quchefen == 30) {
                hourstart = 0;
            } else {

                hourstart = (quchehour - 9) * 2;
                if (quchefen == 30) {
                    hourstart = hourstart + 1;
                }

                hourstart = hourstart + 2;
                if (hourstart >= hours.length) {
                    hourstart = hours.length - 1;
                }
            }

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

        if (qucheyear == 0 || qucheday == 0 || quchemonth == 0
                || haicheyear == 0 || haichemonth == 0 || haicheday == 0) {
            return;
        }

        String start = qucheyear + "-" + oneTo2(quchemonth) + "-"
                + oneTo2(qucheday) + " " + oneTo2(quchehour) + ":"
                + oneTo2(quchefen);
        String end = haicheyear + "-" + oneTo2(haichemonth) + "-"
                + oneTo2(haicheday) + " " + oneTo2(haichehour) + ":"
                + oneTo2(haichefen);

        int alltime = PublicUtil.compareFen(start, end);
        PublicUtil.logDbug(TAG, "@@@@@@" + alltime, 0);

        if (alltime <= 30) {
            alltime = 1;
            zuchezujin = alltime * zujin_hour;
            car_timeView.setText("1小时");

            zongbaoxian = baoxian_p;
        } else {

            if (alltime % 60 != 0) {
                alltime = alltime / 60 + 1;
            } else {
                alltime = alltime / 60;
            }

            int zc_month = alltime / (24 * 30);
            int zc_day = (alltime - zc_month * 24 * 30) / 24;
            int zc_hour = alltime - zc_month * 24 * 30 - zc_day * 24;
            zuchezujin = zc_month * zujin_month + zc_day * zujin_day + zc_hour
                    * zujin_hour;

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

            zongbaoxian = baoxian_p * zc_month * 30 + zc_day * baoxian_p;
            if (zc_hour > 0) {
                zongbaoxian = zongbaoxian + baoxian_p;
            }

            car_timeView.setText(str);

        }

        zuchezujin = PublicUtil.toTwo(zuchezujin);
        zongbaoxian = PublicUtil.toTwo(zongbaoxian);

        car_feiyongView.setText("￥" + zuchezujin);

        if ("1".equals(is_safe_check)) {
            moneyView.setText("￥"
                    + PublicUtil.toTwo(zuchezujin + yajin + zongbaoxian));
            moneyinforView.setText("(含:租车费用" + zuchezujin + "元,车辆押金" + yajin
                    + "元,保险" + zongbaoxian + "元)");
        } else {
            moneyView.setText("￥" + PublicUtil.toTwo(zuchezujin + yajin));
            moneyinforView.setText("(含:租车费用" + zuchezujin + "元,车辆押金" + yajin
                    + "元,保险0元)");

        }

    }

    private List<Map<String, Integer>> busyList = new ArrayList<Map<String, Integer>>();// 用来存放已经出车的时间段

    /**
     * 整理繁忙时段
     *
     * @return
     */
    private void dayFree(JSONArray dateArray) {

        try {

            for (int i = 0; i < dateArray.length(); i++) {

                String start = dateArray.getJSONObject(i).getString("qc_time");
                String end = dateArray.getJSONObject(i).getString("hc_time");
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("start", PublicUtil.getTimeFromDate(start));
                map.put("end", PublicUtil.getTimeFromDate(end));

                busyList.add(map);

            }

            PublicUtil.logDbug(TAG, busyList.toString(), 0);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * 检查取车还车时间段是否已被预约
     *
     * @param time
     * @return
     */
    private boolean checkdate(String time, int type) {
        boolean result = true;

        int time_time = PublicUtil.getTimeFromDate(time);

        int nowstart, nowend;
        PublicUtil.logDbug(TAG, time_time + ":" + time, 0);
        for (int i = 0; i < busyList.size(); i++) {

            Map<String, Integer> map = busyList.get(i);
            nowstart = map.get("start");
            nowend = map.get("end");

            if (type == 0) {// 取车

                if (time_time < nowstart || time_time > nowend) {
                    result = true;
                } else {
                    result = false;
                }

                if (!result) {
                    break;
                }

            } else {// 还车

                if (time_time < nowstart || time_time > nowend) {
                    result = true;
                } else {
                    result = false;
                }

                if (!result) {
                    break;
                }

            }

        }

        return result;

    }
}
