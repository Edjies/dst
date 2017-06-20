package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.bean.OrderParams;
import com.haofeng.apps.dst.bean.StateConst;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.view.WheelView;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.DateTimePicker;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.haofeng.apps.dst.R.id.activity_submit_rent_car_order_shengqing_tiche;
import static com.haofeng.apps.dst.utils.PublicUtil.getDaysByYearMonth;
import static com.haofeng.apps.dst.utils.PublicUtil.getWeekByYearMonth;
import static java.lang.Integer.parseInt;


/**
 * 提交分时租赁订单
 */
public class SubmitRentOrderActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout topLayout;
    private ImageButton backButton;
    private final String TIME_RENT_TYPE = "0";
    private final String DAY_RENT_TYPE = "1";
    private String rentCarType = TIME_RENT_TYPE;  //租车类型  0，分时租赁 1，日租
    private LinearLayout qcAdressLayout;
    private LinearLayout hcAdressLayout;
    private final int REQUESTCODE = 0x7410;
    private TextView qcAdressView;
    private TextView hcAdressView;
    private Button dayRentCarButton;
    private Button timeRentCarButton;
    private GradientDrawable timeButtonBack;
    private GradientDrawable dayButtonBack;
    private Button applyRentCarButton;
    private Double timePrice;
    private Double dayPrice;
    private String[] hours = new String[48];
    private static final String TAG = "SubmitRentOrderActivity";
    private LinearLayout timeRentPickDateLayout;
    private LinearLayout timeRentPickTimeLayout;
    private FrameLayout selectTimeLayout;
    private FrameLayout qcTimeLayout;
    private LinearLayout qcTimeSelectedLayout;
    private FrameLayout hcTimeLayout;
    private LinearLayout hcTimeSelectedLayout;
    private String currentTime;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day, hour, fen;
    private int longestTimeRent;  //最长时租时间
    private DateTimePicker dateTimePicker;
    private int startRentFen;
    private int startRentHour;
    private int endRentHour;
    private int endRentFen;
    private int longestDayRent;
    private int choosetimetype;  //0，取车时间，1，还车时间
    private int chooseRenttype = 0;  //0，时租，1，日租
    private FrameLayout sengQingDateLayout;
    private TextView dateConcelView;
    private TextView dateTypeView;
    private TextView dateOkView;
    private LinearLayout datePickerLayout;
    private LinearLayout timePickerLayout;
    private List<Map<String, Object>> dateList = new ArrayList<>();
    private List<Map<String, Object>> timeList = new ArrayList<>();
    private List<Map<String, Object>> timeManageList = new ArrayList<>();
    private int startDstHour;
    private int startDstFen;
    private int endDstHour;
    private int endDstFen;
    private WheelView datePicker;
    private WheelView timePicker;
    private String timeQcYear, timeQcMonth, timeQcDay, timeQcTime, timeQcWeek;
    private String timeHcYear, timeHcMonth, timeHcDay, timeHcTime, timeHcWeek;
    private String dayQcYear, dayQcMonth, dayQcDay, dayQcTime, dayQcWeek;
    private String dayHcYear, dayHcMonth, dayHcDay, dayHcTime, dayHcWeek;
    private TextView qcTimeSelectedDate;
    private TextView qcTimeSelectedWeek;
    private LinearLayout qcTimeNoSelectedLayout;
    private String timeQcHour;
    private String timeQcFen;
    private String timeHcHour;
    private String timeHcFen;
    private String dayQcHour;
    private String dayQcFen;
    private String dayHcHour;
    private String dayHcFen;
    private LinearLayout hcTimeNoSelectedLayout;
    private TextView hcTimeSelectedDate;
    private TextView hcTimeSelectedWeek;
    private TextView totalOrderTime;
    private TextView totalAmount;
    private TextView showDangWeiView;
    private Button shengQinTiche;

    private String mCarSiteId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_rent_car_order);
        addActivity(this);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_submit_rent_car_order);
        topLayout = (FrameLayout) findViewById(R.id.activity_submit_rent_car_order_top_layout);
        setTopLayoutPadding(topLayout);
        backButton = (ImageButton) findViewById(R.id.activity_submit_rent_car_order_back);
        qcAdressLayout = (LinearLayout) findViewById(R.id.activity_submit_rent_car_order_qc_adress_layout);
        hcAdressLayout = (LinearLayout) findViewById(R.id.activity_submit_rent_car_order_hc_adress_layout);
        qcAdressView = (TextView) findViewById(R.id.activity_submit_rent_car_order_selected_qc_adress);
        hcAdressView = (TextView) findViewById(R.id.activity_submit_rent_car_order_selected_hc_adress);
        //时租
        timeRentCarButton = (Button) findViewById(R.id.activity_submit_rent_car_order_rent_type_time_button);
        //日租
        dayRentCarButton = (Button) findViewById(R.id.activity_submit_rent_car_order_rent_type_day_button);
        applyRentCarButton = (Button) findViewById(activity_submit_rent_car_order_shengqing_tiche);
        initSelectAdressShow();
        qcTimeLayout = (FrameLayout) findViewById(R.id.activity_submit_rent_car_order_quche_time_layout);
        qcTimeSelectedLayout = (LinearLayout) findViewById(R.id.activity_submit_rent_car_order_quche_time_selected_layout);
        qcTimeNoSelectedLayout = (LinearLayout) findViewById(R.id.activity_submit_rent_car_order_quche_time_no_selected_layout);
        qcTimeSelectedDate = (TextView) findViewById(R.id.activity_submit_rent_car_order_quche_time_selected_date);
        qcTimeSelectedWeek = (TextView) findViewById(R.id.activity_submit_rent_car_order_quche_time_selected_week);
        hcTimeSelectedDate = (TextView) findViewById(R.id.activity_submit_rent_car_order_huanche_time_selected_date);
        hcTimeSelectedWeek = (TextView) findViewById(R.id.activity_submit_rent_car_order_huanche_time_selected_time);
        hcTimeLayout = (FrameLayout) findViewById(R.id.activity_submit_rent_car_order_huche_time_layout);
        hcTimeSelectedLayout = (LinearLayout) findViewById(R.id.activity_submit_rent_car_order_huanche_time_selected_layout);
        hcTimeNoSelectedLayout = (LinearLayout) findViewById(R.id.activity_submit_rent_car_order_huche_time_no_selected_layout);
        sengQingDateLayout = (FrameLayout) findViewById(R.id.car_shengqin_date_layout);
        dateConcelView = (TextView) findViewById(R.id.car_shengqin_date_cancel);
        dateTypeView = (TextView) findViewById(R.id.car_shengqin_date_type);
        dateOkView = (TextView) findViewById(R.id.car_shengqin_date_ok);
        datePickerLayout = (LinearLayout) findViewById(R.id.car_shengqin_date_pickLayout);
        timePickerLayout = (LinearLayout) findViewById(R.id.car_shengqin_time_pickLayout);
        totalOrderTime = (TextView) findViewById(R.id.activity_submit_rent_car_order_time_total);
        totalAmount = (TextView) findViewById(R.id.activity_submit_rent_car_order_total_amount);
        showDangWeiView = (TextView) findViewById(R.id.activity_submit_rent_car_order_show_dangwei);
        shengQinTiche = (Button) findViewById(R.id.activity_submit_rent_car_order_shengqing_tiche);
        datePicker = new WheelView(this);
        timePicker = new WheelView(this);
        datePicker.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex) {
                super.onSelected(selectedIndex);
                if (chooseRenttype == 0) { //时租
                    timePickerLayout.removeAllViews();
                    if (selectedIndex == 0) {
                        timePicker.setDate(timeList);
                    } else {
                        //营业时间
                        timePicker.setDate(timeManageList);
                    }
                    timePickerLayout.addView(timePicker);
                }
            }
        });
    }

    /**
     * 初始化地址选择显示
     */
    private void initSelectAdressShow() {
        timeButtonBack = (GradientDrawable) timeRentCarButton.getBackground();
        timeButtonBack.setColor(getResources().getColor(R.color.textdeepgreen));
        timeRentCarButton.setTextColor(getResources().getColor(R.color.white));
        dayButtonBack = (GradientDrawable) dayRentCarButton.getBackground();
        dayButtonBack.setColor(getResources().getColor(R.color.white));
        dayRentCarButton.setTextColor(getResources().getColor(R.color.textgreen));
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        bundle.getString("car_yajin");
        bundle.getString("insurance_expense");
        bundle.getString("insurance_bjmp");
        timePrice = Double.parseDouble(bundle.getString("time_price"));
        dayPrice = Double.parseDouble(bundle.getString("day_price"));
        timeRentCarButton.setText("时租(" + timePrice + "元/小时)");
        dayRentCarButton.setText("日租(" + dayPrice + "元/天)");
        addTimeHour();
    }

    /**
     * 添加48个时间刻度
     */
    private void addTimeHour() {
        for (int i = 0; i < 48; i++) {
            if (i < 20) {
                if (i % 2 == 0) {
                    hours[i] = "0" + i / 2 + ":" + "00";
                } else {
                    hours[i] = "0" + i / 2 + ":" + "30";
                }
            } else {
                if (i % 2 == 0) {
                    hours[i] = i / 2 + ":" + "00";
                } else {
                    hours[i] = i / 2 + ":" + "30";
                }
            }

        }
    }

    private void initListener() {
        backButton.setOnClickListener(this);
        timeRentCarButton.setOnClickListener(this);
        dayRentCarButton.setOnClickListener(this);
        qcAdressLayout.setOnClickListener(this);
        hcAdressLayout.setOnClickListener(this);
        qcAdressView.setOnClickListener(this);
        hcAdressView.setOnClickListener(this);
        applyRentCarButton.setOnClickListener(this);
        qcTimeLayout.setOnClickListener(this);
        hcTimeLayout.setOnClickListener(this);
        dateConcelView.setOnClickListener(this);
        dateOkView.setOnClickListener(this);
        dateConcelView.setOnClickListener(this);
        shengQinTiche.setOnClickListener(this);
    }

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {
        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
            switch (backId) {
                case Constent.ID_GET_ACT_SYSTEM_TIME:
                    if (isRequestSuccess) {
                        if (!isString) {
                            String backstr = null;
                            try {
                                backstr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backstr);
                                if (jsonObject != null && jsonObject.optString("errcode").equals("0")) {
                                    JSONObject dataObject = jsonObject.optJSONObject("data");
                                    currentTime = dataObject.optString("time");
                                    String date = PublicUtil.stampToDate(currentTime);  //2017-06-02 17:53:48
                                    year = parseInt(date.substring(0, 4));
                                    month = parseInt(date.substring(5, 7));
                                    day = parseInt(date.substring(8, 10));
                                    hour = parseInt(date.substring(11, 13));
                                    fen = parseInt(date.substring(14, 16));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    };


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
    protected void onStart() {
        super.onStart();
        //获取当前时间
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_SYSTEM_TIME);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_GET_ACT_SYSTEM_TIME, map, false, false, true);
        //获取租车时间配置
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Constent.URLHEAD_GET + Constent.ACT_RENT_CONFIG).build();
        Call newCall = okHttpClient.newCall(request);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String backStr = response.body().string();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(backStr);
                    if (jsonObject != null && jsonObject.optString("errcode").equals("0")) {
                        JSONObject dataObject = jsonObject.optJSONObject("data");
                        if (dataObject != null) {
                            startDstHour = parseInt(dataObject.getString("start_time").substring(0, 2));
                            startDstFen = parseInt(dataObject.getString("start_time").substring(3, 5));
                            endDstHour = parseInt(dataObject.getString("end_time").substring(0, 2));
                            endDstFen = parseInt(dataObject.getString("end_time").substring(3, 5));
                            longestTimeRent = parseInt(dataObject.getString("h_longest_rent"));
                            longestDayRent = parseInt(dataObject.getString("d_longest_rent"));
                            //营业时间点
                            initManageTime();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 初始化营业时间
     */
    private void initManageTime() {
        int hourStart;
        int hourEnd;
        if (startDstFen >= 30) {
            hourStart = (startDstHour + 2) * 2;
        } else if (startDstFen == 0) {
            hourStart = (startDstHour + 1) * 2;
        } else {
            hourStart = (startDstHour + 1) * 2 + 1;
        }
        if (endDstFen >= 30) {
            hourEnd = (endDstHour - 1) * 2 + 1;
        } else {
            hourEnd = (endDstHour - 1) * 2;
        }
        for (int i = hourStart; i <= hourEnd; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            Log.e(TAG, "hours[i]: " + i + hours[i]);
            map.put("infor", hours[i]);
            map.put("hour", hours[i].split(":")[0]);
            map.put("fen", hours[i].split(":")[1]);
            timeManageList.add(map);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_submit_rent_car_order_back:
                finish();
                break;
            case R.id.activity_submit_rent_car_order_qc_adress_layout:  //选择取车地址
            case R.id.activity_submit_rent_car_order_hc_adress_layout:  //选择还车地址
            case R.id.activity_submit_rent_car_order_selected_qc_adress:
            case R.id.activity_submit_rent_car_order_selected_hc_adress:
                Intent getCarintent = new Intent(this, GetCarAdressActivity.class);
                startActivityForResult(getCarintent, REQUESTCODE);
                break;
            case R.id.activity_submit_rent_car_order_rent_type_time_button:  //时租
                timeButtonBack.setColor(getResources().getColor(R.color.textdeepgreen));
                timeRentCarButton.setTextColor(getResources().getColor(R.color.white));
                dayButtonBack.setColor(getResources().getColor(R.color.white));
                dayRentCarButton.setTextColor(getResources().getColor(R.color.textgreen));
                chooseRenttype = 0;
                qcTimeSelectedLayout.setVisibility(View.GONE);
                qcTimeNoSelectedLayout.setVisibility(View.VISIBLE);
                hcTimeSelectedLayout.setVisibility(View.GONE);
                hcTimeNoSelectedLayout.setVisibility(View.VISIBLE);
                totalOrderTime.setText("");
                showDangWeiView.setText("小时");
                break;
            case R.id.activity_submit_rent_car_order_rent_type_day_button:  //日租
                dayButtonBack.setColor(getResources().getColor(R.color.textdeepgreen));
                dayRentCarButton.setTextColor(getResources().getColor(R.color.white));
                timeButtonBack.setColor(getResources().getColor(R.color.white));
                timeRentCarButton.setTextColor(getResources().getColor(R.color.textgreen));
                chooseRenttype = 1;
                qcTimeSelectedLayout.setVisibility(View.GONE);
                qcTimeNoSelectedLayout.setVisibility(View.VISIBLE);
                hcTimeSelectedLayout.setVisibility(View.GONE);
                hcTimeNoSelectedLayout.setVisibility(View.VISIBLE);
                totalOrderTime.setText("");
                showDangWeiView.setText("天");
                break;

            case R.id.activity_submit_rent_car_order_quche_time_layout:  //选择取车时间
                choosetimetype = 0;
                sengQingDateLayout.setVisibility(View.VISIBLE);
                setDate();
                setTime();
                break;
            case R.id.activity_submit_rent_car_order_huche_time_layout:   //选择还车时间
                if (chooseRenttype == 0) {
                    if (timeQcMonth == null && timeQcDay == null) {
                        PublicUtil.showToast(this, "请先选择取车时间", false);
                        return;
                    }
                } else {
                    if (dayQcMonth == null && dayQcDay == null) {
                        PublicUtil.showToast(this, "请先选择取车时间", false);
                        return;
                    }
                }
                choosetimetype = 1;
                setDate();
                setTime();
                sengQingDateLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.car_shengqin_date_ok:
                if (chooseRenttype == 0) { //时租
                    if (choosetimetype == 0) {  //取车
                        int dataSeletedIndex = datePicker.getSeletedIndex();
                        int timeSeletedIndex = timePicker.getSeletedIndex();
                        Map<String, Object> dateMap = dateList.get(dataSeletedIndex);
                        timeQcYear = String.valueOf(dateMap.get("year"));
                        timeQcMonth = String.valueOf(dateMap.get("month"));
                        timeQcDay = String.valueOf(dateMap.get("day"));
                        timeQcWeek = String.valueOf(dateMap.get("week"));
                        Map<String, Object> timeMap = null;
                        if (dataSeletedIndex == 0) {
                            timeMap = timeList.get(timeSeletedIndex);
                        } else {
                            timeMap = timeManageList.get(timeSeletedIndex);
                        }
                        timeQcTime = String.valueOf(timeMap.get("infor"));
                        timeQcHour = timeQcTime.split(":")[0];
                        timeQcFen = timeQcTime.split(":")[1];
                        qcTimeNoSelectedLayout.setVisibility(View.GONE);
                        qcTimeSelectedLayout.setVisibility(View.VISIBLE);
                        if (parseInt(timeQcMonth) < 10 && parseInt(timeQcDay) < 10) {
                            qcTimeSelectedDate.setText("0" + timeQcMonth + "-" + "0" + timeQcDay);
                        } else if (parseInt(timeQcMonth) < 10) {
                            qcTimeSelectedDate.setText("0" + timeQcMonth + "-" + timeQcDay);
                        } else if (parseInt(timeQcDay) < 10) {
                            qcTimeSelectedDate.setText(timeQcMonth + "-" + "0" + timeQcDay);
                        } else {
                            qcTimeSelectedDate.setText(timeQcMonth + "-" + timeQcDay);
                        }
                        qcTimeSelectedWeek.setText(timeQcWeek + " " + timeQcTime);
                        hcTimeNoSelectedLayout.setVisibility(View.VISIBLE);
                        hcTimeSelectedLayout.setVisibility(View.GONE);
                    } else {  //还车
                        int dataSeletedIndex = datePicker.getSeletedIndex();
                        int timeSeletedIndex = timePicker.getSeletedIndex();
                        Map<String, Object> dateMap = dateList.get(dataSeletedIndex);
                        timeHcYear = String.valueOf(dateMap.get("year"));
                        timeHcMonth = String.valueOf(dateMap.get("month"));
                        timeHcDay = String.valueOf(dateMap.get("day"));
                        timeHcWeek = String.valueOf(dateMap.get("week"));
                        Map<String, Object> timeMap = timeList.get(timeSeletedIndex);
                        timeHcTime = String.valueOf(timeMap.get("infor"));
                        timeHcHour = timeHcTime.split(":")[0];
                        timeHcFen = timeHcTime.split(":")[1];
                        hcTimeNoSelectedLayout.setVisibility(View.GONE);
                        hcTimeSelectedLayout.setVisibility(View.VISIBLE);
                        if (parseInt(timeQcMonth) < 10 && parseInt(timeQcDay) < 10) {
                            hcTimeSelectedDate.setText("0" + timeQcMonth + "-" + "0" + timeQcDay);
                        } else if (parseInt(timeQcMonth) < 10) {
                            hcTimeSelectedDate.setText("0" + timeQcMonth + "-" + timeQcDay);
                        } else if (parseInt(timeQcDay) < 10) {
                            hcTimeSelectedDate.setText(timeQcMonth + "-" + "0" + timeQcDay);
                        } else {
                            hcTimeSelectedDate.setText(timeQcMonth + "-" + timeQcDay);
                        }
                        hcTimeSelectedWeek.setText(timeQcWeek + " " + timeHcTime);
                        int totalRentTime = Integer.parseInt(timeHcHour) - Integer.parseInt(timeQcHour);
                        totalOrderTime.setText(totalRentTime + "");
                        totalAmount.setText(String.valueOf(PublicUtil.roundByScale(totalRentTime * timePrice, 2)));

                    }
                } else {//日租
                    if (choosetimetype == 0) {  //取车
                        int dataSeletedIndex = datePicker.getSeletedIndex();
                        int timeSeletedIndex = timePicker.getSeletedIndex();
                        Map<String, Object> dateMap = dateList.get(dataSeletedIndex);
                        dayQcYear = String.valueOf(dateMap.get("year"));
                        dayQcMonth = String.valueOf(dateMap.get("month"));
                        dayQcDay = String.valueOf(dateMap.get("day"));
                        dayQcWeek = String.valueOf(dateMap.get("week"));
                        Map<String, Object> timeMap = timeMap = timeList.get(timeSeletedIndex);
                        dayQcTime = String.valueOf(timeMap.get("infor"));
                        dayQcHour = dayQcTime.split(":")[0];
                        dayQcFen = dayQcTime.split(":")[1];
                        qcTimeNoSelectedLayout.setVisibility(View.GONE);
                        qcTimeSelectedLayout.setVisibility(View.VISIBLE);
                        if (parseInt(dayQcMonth) < 10 && parseInt(dayQcDay) < 10) {
                            qcTimeSelectedDate.setText("0" + dayQcMonth + "-" + "0" + dayQcDay);
                        } else if (parseInt(dayQcMonth) < 10) {
                            qcTimeSelectedDate.setText("0" + dayQcMonth + "-" + dayQcDay);
                        } else if (parseInt(timeQcDay) < 10) {
                            qcTimeSelectedDate.setText(dayQcMonth + "-" + "0" + dayQcDay);
                        } else {
                            qcTimeSelectedDate.setText(dayQcMonth + "-" + dayQcDay);
                        }
                        qcTimeSelectedWeek.setText(dayQcWeek + " " + dayQcTime);
                        hcTimeNoSelectedLayout.setVisibility(View.VISIBLE);
                        hcTimeSelectedLayout.setVisibility(View.GONE);
                    } else { //还车
                        int dataSeletedIndex = datePicker.getSeletedIndex();
                        int timeSeletedIndex = timePicker.getSeletedIndex();
                        Map<String, Object> dateMap = dateList.get(dataSeletedIndex);
                        dayHcYear = String.valueOf(dateMap.get("year"));
                        dayHcMonth = String.valueOf(dateMap.get("month"));
                        dayHcDay = String.valueOf(dateMap.get("day"));
                        dayHcWeek = String.valueOf(dateMap.get("week"));
                        Map<String, Object> timeMap = timeList.get(timeSeletedIndex);
                        dayHcTime = String.valueOf(timeMap.get("infor"));
                        dayHcHour = dayHcTime.split(":")[0];
                        dayHcFen = dayHcTime.split(":")[1];
                        hcTimeNoSelectedLayout.setVisibility(View.GONE);
                        hcTimeSelectedLayout.setVisibility(View.VISIBLE);
                        if (parseInt(dayHcMonth) < 10 && parseInt(dayHcDay) < 10) {
                            hcTimeSelectedDate.setText("0" + dayHcMonth + "-" + "0" + dayHcDay);
                        } else if (parseInt(dayQcMonth) < 10) {
                            hcTimeSelectedDate.setText("0" + dayHcMonth + "-" + dayHcDay);
                        } else if (parseInt(timeHcDay) < 10) {
                            hcTimeSelectedDate.setText(dayHcMonth + "-" + "0" + dayHcDay);
                        } else {
                            hcTimeSelectedDate.setText(dayHcMonth + "-" + dayHcDay);
                        }
                        hcTimeSelectedWeek.setText(dayHcWeek + " " + dayHcTime);
                        totalOrderTime.setText("1");
                        totalAmount.setText(dayPrice + "");
                    }
                }
                sengQingDateLayout.setVisibility(View.GONE);
                break;
            case R.id.car_shengqin_date_cancel:
                sengQingDateLayout.setVisibility(View.GONE);
                break;
            case R.id.activity_submit_rent_car_order_shengqing_tiche:
                // TODO 条件判断
                if (mCarSiteId == null || "".equals(mCarSiteId)) {
                  Toast.makeText(this, "请选择取车还车门店", Toast.LENGTH_SHORT).show();
                } else if(chooseRenttype == 0 && (timeQcMonth == null || timeHcMonth == null)) {
                    Toast.makeText(this, "请先选择时间", Toast.LENGTH_SHORT).show();
                } else if(chooseRenttype == 1 && (dayQcMonth == null || dayHcMonth == null)) {
                    Toast.makeText(this, "请先选择时间", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(this, ShengQingGetCarActivity.class);
                    intent.putExtra("car", getIntent().getSerializableExtra("car"));
                    OrderParams params = execGetOrderParams();
                    intent.putExtra("orderParams", params);
                    startActivity(intent);
                }
                break;
        }
    }

    private OrderParams execGetOrderParams() {
        OrderParams params = new OrderParams();

        params.m_type = String.valueOf(chooseRenttype + 1);
        if(StateConst.ORDER_TYPE_HOUR.equals(params.m_type)) {
            params.m_qc_time = timeQcYear + "-" + (timeQcMonth.length() < 2 ? "0" + timeQcMonth : timeQcMonth) + "-" + (timeQcDay.length() < 2 ? "0" + timeQcDay : timeQcDay) + " " + timeQcTime + ":00";
            params.m_hc_time = timeHcYear + "-" + (timeHcMonth.length() < 2 ? "0" + timeHcMonth : timeHcMonth) + "-" + (timeHcDay.length() < 2 ? "0" + timeHcDay : timeHcDay) + " " + timeHcTime + ":00";
        }else {
            params.m_qc_time = dayQcYear + "-" + (dayQcMonth.length() < 2 ? "0" + dayQcMonth : dayQcMonth) + "-" + (dayQcDay.length() < 2 ? "0" + dayQcDay : dayQcDay) + " " + dayQcTime + ":00";
            params.m_hc_time = dayHcYear + "-" + (dayHcMonth.length() < 2 ? "0" + dayHcMonth : dayHcMonth) + "-" + (dayHcDay.length() < 2 ? "0" + dayHcDay : dayHcDay) + " " + dayHcTime + ":00";
        }

        params.m_store_id = mCarSiteId;
        params.m_way = "1";

        params.m_yu_ji_zu_jing = totalAmount.getText().toString();
        return params;
    }



    /**
     * 设置还取车日期
     */
    private void setDate() {
        dateList.clear();
        int nowmonth;
        int nowyear;
        int nowday;
        if (chooseRenttype == 0) { //时租
            if (choosetimetype == 0) {
                nowyear = year;
                nowmonth = month;
                nowday = day;
                int rentEndFen;
                int alldays = getDaysByYearMonth(nowyear, nowmonth);
                if (endDstFen >= 30) {
                    rentEndFen = 30;
                } else {
                    rentEndFen = 0;
                }
                if ((endDstHour * 60 + rentEndFen) - (hour * 60 + fen) < 60) {
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
                    map.put("infor", nowmonth + "月" + i + "日        "
                            + PublicUtil.getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("year", nowyear);
                    map.put("month", nowmonth);
                    map.put("day", i);
                    dateList.add(map);

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
                        dateList.add(map);
                    }
                }
            } else { //还车
                //设置还车日期(还车日期==取车日期)
                Map<String, Object> map = new HashMap<>();
                map.put("infor", timeQcMonth + "月" + timeQcDay + "日        "
                        + getWeekByYearMonth(parseInt(timeQcYear), parseInt(timeQcMonth), parseInt(timeQcDay)));
                map.put("year", timeQcYear);
                map.put("month", timeQcMonth);
                map.put("day", timeQcDay);
                map.put("week", getWeekByYearMonth(parseInt(timeQcYear), parseInt(timeQcMonth), parseInt(timeQcDay)));
                dateList.add(map);
            }
        } else {  //日租
            nowyear = year;
            nowmonth = month;
            nowday = day;
            if (choosetimetype == 0) {
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

                int tianshu = alldays - nowday + 1; //剩余天数
                if (tianshu > longestDayRent) {
                    alldays = nowday + longestDayRent - 1;
                }
                for (int i = nowday; i <= alldays; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("infor", nowmonth + "月" + i + "日        "
                            + PublicUtil.getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("year", nowyear);
                    map.put("month", nowmonth);
                    map.put("day", i);
                    dateList.add(map);

                }
                if (tianshu < longestDayRent) {
                    nowmonth = nowmonth + 1;
                    if (nowmonth > 12) {
                        nowmonth = 1;
                        nowyear = nowyear + 1;
                    }
                    int alldays2 = longestDayRent - tianshu;
                    for (int i = 1; i <= alldays2; i++) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("infor", nowmonth + "月" + i + "日        "
                                + getWeekByYearMonth(nowyear, nowmonth, i));
                        map.put("year", nowyear);
                        map.put("month", nowmonth);
                        map.put("day", i);
                        map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                        dateList.add(map);
                    }
                }
            } else {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("infor", dayQcMonth + "月" + dayQcDay + "日        "
                        + getWeekByYearMonth(Integer.parseInt(dayQcYear), Integer.parseInt(dayQcMonth), Integer.parseInt(dayQcDay)));
                map.put("year", dayQcYear);
                map.put("month", dayQcMonth);
                map.put("day", dayQcDay);
                map.put("week", getWeekByYearMonth(Integer.parseInt(dayQcYear), Integer.parseInt(dayQcMonth), Integer.parseInt(dayQcDay)));
                dateList.add(map);
            }
        }
        datePickerLayout.removeAllViews();
        datePicker.setDate(dateList);
        datePickerLayout.addView(datePicker);

    }

    /**
     * 设置取还车时间
     */

    private void setTime() {
        timeList.clear();
        int hourstart = 0;
        int hourend = 0;
        if (chooseRenttype == 0) {  //时租
            if (choosetimetype == 0) {//取车
                int rentEndFen;
                int rentStartFen;
                int rentStartHour = 0;
                //超过营业时间
                if (endDstFen >= 30) {
                    rentEndFen = 30;
                } else {
                    rentEndFen = 0;
                }
                if (startDstFen >= 30) {
                    rentStartFen = 0;
                    rentStartHour = startDstHour + 1;
                } else {
                    rentStartFen = 30;
                    rentStartHour = startDstHour;
                }
                if (Math.abs((endDstHour * 60 + rentEndFen) - (hour * 60 + fen)) < 60
                        || Math.abs((rentStartHour * 60 + startDstFen) - (hour * 60 + fen)) < 60) {
                    if (rentStartFen == 0) {
                        hourstart = (startDstHour + 1) * 2;
                    } else {
                        hourstart = startDstHour * 2 + 1;
                    }
                } else {
                    //时间在营业期内
                    if (fen > 30) {
                        hourstart = (hour + 2) * 2;
                    } else {
                        hourstart = (hour + 1) * 2 + 1;
                    }
                }
                if (rentEndFen == 0) {
                    hourend = (endDstHour - 1) * 2;
                } else {
                    hourend = (endDstHour - 1) * 2 + 1;
                }
                for (int i = hourstart; i <= hourend; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    Log.e(TAG, "hours[i]: " + i + hours[i]);
                    map.put("infor", hours[i]);
                    map.put("hour", hours[i].split(":")[0]);
                    map.put("fen", hours[i].split(":")[1]);
                    timeList.add(map);
                }
            } else {//还车
                int startHcHour;
                int endHcHour;
                String hcFen;
                startHcHour = Integer.parseInt(timeQcHour) + 1;
                if (Integer.parseInt(timeQcFen) == 0) { //整点
                    if (startHcHour + longestTimeRent - 1 > endDstHour) {
                        //以营业时间为准
                        endHcHour = endDstHour;
                    } else {
                        //以最长时间为准
                        endHcHour = startHcHour + longestTimeRent - 1;
                    }
                    hcFen = "00";
                } else {
                    if (startHcHour + longestTimeRent - 1 > endDstHour) {
                        //以营业时间为准
                        endHcHour = endDstHour;
                    } else {
                        //以最长时间为准
                        endHcHour = startHcHour + longestTimeRent - 1;
                    }
                    hcFen = "30";
                }
                for (int i = startHcHour; i <= endHcHour; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();

                    if (i < 10) {
                        map.put("infor", "0" + i + ":" + hcFen);
                    } else {
                        map.put("infor", i + ":" + hcFen);
                    }
                    map.put("hour", i);
                    map.put("fen", hcFen);
                    timeList.add(map);
                }
            }
        } else { //日租
            if (choosetimetype == 0) {
                int startDayHour;
                String startDayFen;
                if (startDstFen > 30) {
                    startDayHour = startDstHour + 1;
                    startDayFen = "00";
                } else if (startDstFen == 0) {
                    startDayHour = startDstHour;
                    startDayFen = "00";
                } else {
                    startDayHour = startDstHour;
                    startDayFen = "30";
                }
                Map<String, Object> map = new HashMap<String, Object>();

                if (startDayHour < 10) {
                    map.put("infor", "0" + startDayHour + ":" + startDayFen);
                } else {
                    map.put("infor", startDayHour + ":" + startDayFen);
                }
                map.put("hour", startDayHour);
                map.put("fen", startDayFen);
                timeList.add(map);
            } else {
                int endDayHour = 0;
                String endDayFen = "";
                if (endDstFen > 30) {
                    endDayHour = endDstHour;
                    endDayFen = "30";
                } else if (endDstFen == 0) {
                    endDayHour = endDstHour;
                    endDayFen = "00";
                }
                Map<String, Object> map = new HashMap<String, Object>();
                if (endDayHour < 10) {
                    map.put("infor", "0" + endDayHour + ":" + endDayFen);
                } else {
                    map.put("infor", endDayHour + ":" + endDayFen);
                }
                map.put("hour", endDayHour);
                map.put("fen", endDayHour);
                timeList.add(map);
            }
        }
        timePickerLayout.removeAllViews();
        timePicker.setDate(timeList);
        timePickerLayout.addView(timePicker);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (resultCode == 12 && data != null) {
                qcAdressView.setVisibility(View.VISIBLE);
                hcAdressView.setVisibility(View.VISIBLE);
                qcAdressLayout.setVisibility(View.GONE);
                hcAdressLayout.setVisibility(View.GONE);
                qcAdressView.setText(data.getStringExtra("adressName"));
                hcAdressView.setText(data.getStringExtra("adressName"));
                mCarSiteId = data.getStringExtra("carAdressId");
            }
        }
    }
}
