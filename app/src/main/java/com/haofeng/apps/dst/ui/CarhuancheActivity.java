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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.view.WheelView;
import com.haofeng.apps.dst.ui.view.WheelView.OnWheelViewListener;
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

public class CarhuancheActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "CarhuancheActivity";
    private FrameLayout dateLayout;
    private TextView datecancelView, dateokView;
    private WheelView wheelPicker, wheelPicker2;
    private LinearLayout pickLayout, pickLayout2;
    private TextView danhaoView;
    private TextView dianlibuzuView, cheliangguzhangView, qitaView;
    private TextView cancelView, okView;
    private TextView shijianView;

    private int year, month, day, hour, fen;
    private int qucheyear, quchemonth, qucheday, quchehour, quchefen;
    private Calendar calendar = Calendar.getInstance();
    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();// 日期
    private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();// 开始小时
    private List<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();// 全部时间
    private List<Map<String, Object>> arrayList4 = new ArrayList<Map<String, Object>>();// 结束小时
    private String[] hours = {"09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};

    // private int dataindex = 0;// 检测日期滑动的标志
    private String huancheyuanyin = "", danhao = "";
    private String hc_time;
    private int hc_y, hc_m, hc_d, hc_h, hc_f;
    private int n_d, n_m, n_y;
    private String qc_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carhuanche);
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

        dateLayout = (FrameLayout) findViewById(R.id.view_huanche_dialog_date_layout);
        datecancelView = (TextView) findViewById(R.id.view_huanche_dialog_date_cancel);

        dateokView = (TextView) findViewById(R.id.view_huanche_dialog_date_ok);
        pickLayout = (LinearLayout) findViewById(R.id.view_huanche_dialog_date_picklayout);
        pickLayout2 = (LinearLayout) findViewById(R.id.view_huanche_dialog_date_picklayout2);

        danhaoView = (TextView) findViewById(R.id.view_huanche_dialog_danhao);
        dianlibuzuView = (TextView) findViewById(R.id.view_huanche_dialog_dianlibuzu);
        cheliangguzhangView = (TextView) findViewById(R.id.view_huanche_dialog_chelaingguzhang);
        qitaView = (TextView) findViewById(R.id.view_huanche_dialog_qitayuanyin);
        cancelView = (TextView) findViewById(R.id.view_huanche_dialog_cancel);
        okView = (TextView) findViewById(R.id.view_huanche_dialog_ok);
        shijianView = (TextView) findViewById(R.id.view_huanche_dialog_shijian);

        datecancelView.setOnClickListener(this);
        dateokView.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        cancelView.setOnClickListener(this);
        okView.setOnClickListener(this);
        shijianView.setOnClickListener(this);
        dianlibuzuView.setOnClickListener(this);
        cheliangguzhangView.setOnClickListener(this);
        qitaView.setOnClickListener(this);
        danhao = getIntent().getStringExtra("danhao");
        hc_time = getIntent().getStringExtra("hc_time");
        qc_time = getIntent().getStringExtra("qc_time2");

        danhaoView.setText(danhao);

        String hc_date = hc_time.split(" ")[0];
        String hc_time_time = hc_time.split(" ")[1];

        hc_y = Integer.parseInt(hc_date.split("-")[0]);
        hc_m = Integer.parseInt(hc_date.split("-")[1]);
        hc_d = Integer.parseInt(hc_date.split("-")[2]);

        hc_h = Integer.parseInt(hc_time_time.split(":")[0]);
        hc_f = Integer.parseInt(hc_time_time.split(":")[1]);

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

        PublicUtil.logDbug(TAG, year + ":" + month + ":" + day + ":" + hour
                + ":" + fen + hc_time, 0);

        wheelPicker = new WheelView(CarhuancheActivity.this);
        wheelPicker2 = new WheelView(CarhuancheActivity.this);

        for (int i = 0; i < hours.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("infor", hours[i]);
            map.put("hour", hours[i].split(":")[0]);
            map.put("fen", hours[i].split(":")[1]);

            arrayList3.add(map);

        }

        wheelPicker.setOnWheelViewListener(new OnWheelViewListener() {

            @Override
            public void onSelected(int selectedIndex) {
                // TODO Auto-generated method stub

                if (wheelPicker.getSeletedIndex() == 0) {
                    if (arrayList2.size() > 0) {
                        wheelPicker2.setDate(arrayList2);
                        pickLayout2.removeAllViews();
                        pickLayout2.addView(wheelPicker2);
                    }

                } else if ((wheelPicker.getSeletedIndex() == (arrayList.size() - 1))) {
                    if (arrayList4.size() > 0) {
                        wheelPicker2.setDate(arrayList4);
                        pickLayout2.removeAllViews();
                        pickLayout2.addView(wheelPicker2);
                    }

                } else {
                    if (arrayList3.size() > 0) {
                        wheelPicker2.setDate(arrayList3);
                        pickLayout2.removeAllViews();
                        pickLayout2.addView(wheelPicker2);
                    }

                }

            }
        });

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.view_huanche_dialog_cancel:

                finish();
                break;

            case R.id.view_huanche_dialog_shijian:   //还车

                if (PublicUtil.getTimeFromDate(hc_time) <= PublicUtil
                        .getTimeFromDate(year + "-" + oneTo2(month) + "-"
                                + oneTo2(day) + " " + oneTo2(hour) + ":"
                                + oneTo2(fen) + ":00")) {
                    PublicUtil.showToast(CarhuancheActivity.this, "已超过还车日期，不能换车",
                            false);
                    return;
                }

                n_d = day;
                n_m = month;
                n_y = year;

                if (hour >= 17) {
                    n_d = n_d + 1;
                    int alldays = getDaysByYearMonth(year, month);

                    if (n_d > alldays) {
                        n_m = n_m + 1;
                        n_d = 1;

                    }
                    if (n_m > 12) {
                        n_y = n_y + 1;
                        n_m = 1;
                    }

                    if (hc_y == n_y && hc_m == n_m && n_d == hc_d) {

                        if (hc_h == 9 && hc_f == 0) {
                            PublicUtil.showToast(CarhuancheActivity.this,
                                    "临近还车时间，无法申请换车", false);
                            return;
                        }

                    } else {

                        if (hour < 18) {
                            n_d = day;
                            n_m = month;
                            n_y = year;
                        }

                    }
                } else {

                    if (PublicUtil.getTimeFromDate(hc_time) <= (PublicUtil
                            .getTimeFromDate(year + "-" + oneTo2(month) + "-"
                                    + oneTo2(day) + " " + oneTo2(hour) + ":"
                                    + oneTo2(fen) + ":00") + 3600)) {
                        PublicUtil.showToast(CarhuancheActivity.this,
                                "临近还车时间，无法申请换车", false);
                        return;
                    }

                }

                setDate();
                setTime();
                if (dateLayout.getVisibility() == View.GONE) {
                    dateLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.view_huanche_dialog_dianlibuzu:

                huancheyuanyin = "电力不足";
                initImage();
                dianlibuzuView.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
                dianlibuzuView.setTextColor(getResources().getColor(R.color.white));
                break;
            case R.id.view_huanche_dialog_chelaingguzhang:

                huancheyuanyin = "车辆故障";
                initImage();
                cheliangguzhangView
                        .setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
                cheliangguzhangView.setTextColor(getResources().getColor(
                        R.color.white));
                break;
            case R.id.view_huanche_dialog_qitayuanyin:

                huancheyuanyin = "其他原因";
                initImage();
                qitaView.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
                qitaView.setTextColor(getResources().getColor(R.color.white));
                break;

            case R.id.view_huanche_dialog_ok:

                if (qucheyear == 0 || qucheday == 0 || quchemonth == 0) {
                    PublicUtil.showToast(CarhuancheActivity.this, "请选择换车日期", false);
                    return;
                }

                String start = qucheyear + "-" + oneTo2(quchemonth) + "-"
                        + oneTo2(qucheday) + "+" + oneTo2(quchehour) + ":"
                        + oneTo2(quchefen) + ":00";

                if (TextUtils.isEmpty(huancheyuanyin)) {
                    PublicUtil.showToast(CarhuancheActivity.this, "请选择换车理由", false);
                    return;
                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ORDER_CHANGE_ADD);
                map.put("ver", Constent.VER);
                map.put("order_no", PublicUtil.toUTF(danhao));
                map.put("detail", PublicUtil.toUTF(huancheyuanyin));
                map.put("change_time", start);

                AnsynHttpRequest.httpRequest(CarhuancheActivity.this,
                        AnsynHttpRequest.GET, callBack,
                        Constent.ID_ORDER_CHANGE_ADD, map, false, true, true);

                break;
            case R.id.view_huanche_dialog_date_layout:

                break;
            case R.id.view_huanche_dialog_date_cancel:
                if (dateLayout.getVisibility() == View.VISIBLE) {
                    dateLayout.setVisibility(View.GONE);
                }

                break;

            case R.id.view_huanche_dialog_date_ok:

                try {
                    int datecount = wheelPicker.getSeletedIndex();
                    int timecount = wheelPicker2.getSeletedIndex();

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

                    shijianView.setText(qucheyear + "-" + oneTo2(quchemonth) + "-"
                            + oneTo2(qucheday) + " " + oneTo2(quchehour) + ":"
                            + oneTo2(quchefen) + ":00");

                    dateLayout.setVisibility(View.GONE);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

                break;

            default:
                break;
        }

    }

    private void initImage() {
        qitaView.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
        qitaView.setTextColor(getResources().getColor(R.color.gray));

        cheliangguzhangView
                .setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
        cheliangguzhangView.setTextColor(getResources().getColor(R.color.gray));
        dianlibuzuView.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
        dianlibuzuView.setTextColor(getResources().getColor(R.color.gray));
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
                case Constent.ID_ORDER_CHANGE_ADD:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_huanche);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_huanche;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private int error_httprequest_huanche = 0x6400;
    private int success_httprequest_huanche = 0x6401;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_httprequest_huanche) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarhuancheActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_huanche) {
                if (jsonObject != null) {
                    try {
                        PublicUtil.showToast(CarhuancheActivity.this,
                                jsonObject.getString("msg"), false);
                        if ("0".equals(jsonObject.getString("errcode"))) {

                            Intent intent = new Intent();
                            intent.putExtra("result", "success");
                            CarhuancheActivity.this.setResult(42, intent);
                            finish();
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
        String hc_date = hc_time.split(" ")[0];
        int qc_year = Integer.parseInt(qc_time.substring(0, 4));
        int qc_month = Integer.parseInt(qc_time.substring(5, 7));
        int qc_day = Integer.parseInt(qc_time.substring(8, 10));
        if (qucheyear > year) {
            year = qc_year;
            month = qc_month;
            day = qc_day;
        } else if (qc_year == year) {
            if (qc_month > month) {
                month = qc_month;
                day = qc_day;
            } else if (qc_month == month) {
                if (qc_day > day) {
                    day = qc_day;
                }
            }
        }
        String start_date = year + "-"
                + PublicUtil.oneTo2(String.valueOf(month)) + "-"
                + PublicUtil.oneTo2(String.valueOf(day));

        PublicUtil.logDbug(TAG, hc_date + "", 0);
        PublicUtil.logDbug(TAG, start_date + "", 0);

        int days = PublicUtil.compareDay(start_date, hc_date);
        days = days + 1;

        if (hc_h == 9 && hc_f == 0) {//第二天9点
            days = days - 1;
        }

        PublicUtil.logDbug(TAG, days + "", 0);
        int nowmonth = month;
        int nowyear = year;
        int nowday = day;

        int alldays = getDaysByYearMonth(nowyear, nowmonth);

        if (hour >= 18) {
            nowday = nowday + 1;
            days = days - 1;
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

        int tianshu = alldays - nowday + 1;// 本月剩余的天数
        if (tianshu >= days) {
            alldays = nowday + days - 1;

        }

        for (int i = nowday; i <= alldays; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            if (nowmonth == (calendar.get(Calendar.MONTH) + 1) && i == (calendar.get(Calendar.DAY_OF_MONTH))) {
                map.put("infor", nowmonth + "月" + i + "日      今天");
                map.put("week", "今天");
            } else {
                map.put("infor", nowmonth + "月" + i + "日      "
                        + getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
            }
            map.put("year", nowyear);
            map.put("month", nowmonth);
            map.put("day", i);
            arrayList.add(map);

        }

        if (tianshu < days) {

            boolean flag = true;

            while (flag) {

                nowmonth = nowmonth + 1;

                PublicUtil.logDbug(TAG, nowmonth + "", 0);
                if (nowmonth > 12) {
                    nowmonth = 1;
                    nowyear = nowyear + 1;

                }

                int alldays2 = getDaysByYearMonth(nowyear, nowmonth);
                PublicUtil.logDbug(TAG, (alldays2) + "", 0);
                PublicUtil.logDbug(TAG, (tianshu) + "", 0);
                if ((tianshu + alldays2) >= days) {
                    alldays2 = days - tianshu;

                    flag = false;
                }

                for (int i = 1; i <= alldays2; i++) {

                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("infor", nowmonth + "月" + i + "日      "
                            + getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("year", nowyear);
                    map.put("month", nowmonth);
                    map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                    map.put("day", i);
                    arrayList.add(map);
                }

                if ((tianshu + alldays2) < days) {
                    flag = true;
                    tianshu = tianshu + alldays2;

                }
                PublicUtil.logDbug(TAG, flag + "", 0);
            }

        }

        PublicUtil.logDbug(TAG, arrayList.size() + "", 0);

        if (arrayList.size() > 0) {
            pickLayout.removeAllViews();
            wheelPicker.setDate(arrayList);
            pickLayout.addView(wheelPicker);
        }

    }

    /**
     * 设置还取车时间
     */
    private void setTime() {
        arrayList2.clear();
        int hourstart, hourend;

        hourend = (hc_h - 9) * 2;
        if (hc_f < 30) {
            hourend = hourend - 1;
        }

        if (hourend < 0) {
            hourend = 0;
        }

        if (hc_h == 9 && hc_f == 0) {
            hourend = hours.length - 1;
        }

        if (hc_h == 18) {
            hourend = hours.length - 3;
        }
        for (int i = 0; i <= hourend; i++) {

            Map<String, Object> map = new HashMap<String, Object>();

            map.put("infor", hours[i]);
            map.put("hour", hours[i].split(":")[0]);
            map.put("fen", hours[i].split(":")[1]);

            arrayList4.add(map);

        }
        String qctime = qc_time;
        String hc_date = hc_time.split(" ")[0];
        hour = Integer.parseInt(qctime.split(" ")[1].split(":")[0]);
        int qc_year = Integer.parseInt(qc_time.substring(0, 4));
        int qc_month = Integer.parseInt(qc_time.substring(5, 7));
        int qc_day = Integer.parseInt(qc_time.substring(8, 10));
        int qc_hour =Integer.parseInt(qctime.split(" ")[1].split(":")[0]);
        if (qucheyear > year) {
            hour = qc_hour;
        } else if (qc_year == year) {
            if (qc_month > month) {
                hour = qc_hour;
            } else if (qc_month == month) {
                if (qc_day > day) {
                    hour = qc_hour;
                }
            }
        }
        if (hour >= 18) {
            hourstart = 0;
            if (n_y == hc_y && n_m == hc_m && n_d == hc_d) {

                for (int i = hourstart; i <= hourend; i++) {

                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("infor", hours[i]);
                    map.put("hour", hours[i].split(":")[0]);
                    map.put("fen", hours[i].split(":")[1]);

                    arrayList2.add(map);

                }
            } else {
                for (int i = hourstart; i < hours.length; i++) {

                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("infor", hours[i]);
                    map.put("hour", hours[i].split(":")[0]);
                    map.put("fen", hours[i].split(":")[1]);

                    arrayList2.add(map);

                }

            }

        } else {

            int nowhour = hour;

            hourstart = (nowhour - 9) * 2;
            if (fen > 0 && fen <= 30) {
                hourstart = hourstart + 1;
            } else if (fen > 30) {
                hourstart = hourstart + 2;
            }

            if (n_y == hc_y && n_m == hc_m && n_d == hc_d) {
                for (int i = hourstart; i <= hourend; i++) {

                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("infor", hours[i]);
                    map.put("hour", hours[i].split(":")[0]);
                    map.put("fen", hours[i].split(":")[1]);

                    arrayList2.add(map);

                }
            } else {
                for (int i = hourstart; i < hours.length; i++) {

                    Map<String, Object> map = new HashMap<String, Object>();

                    map.put("infor", hours[i]);
                    map.put("hour", hours[i].split(":")[0]);
                    map.put("fen", hours[i].split(":")[1]);

                    arrayList2.add(map);

                }

            }

        }

        if (arrayList2.size() > 0) {
            pickLayout2.removeAllViews();
            wheelPicker2.setDate(arrayList2);
            pickLayout2.addView(wheelPicker2);
        }

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

}
