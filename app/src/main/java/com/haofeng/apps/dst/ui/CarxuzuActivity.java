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

public class CarxuzuActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "CarxuzuActivity";
    private FrameLayout dateLayout;
    private TextView datecancelView, dateokView;
    private WheelView myWheelView, myWheelView2;
    private LinearLayout pickLayout, pickLayout2;
    private TextView danhaoView;

    private TextView cancelView, okView;
    private TextView shijianView, moneyView;

    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();// 全部时间
    private String[] hours = {"09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};

    private String danhao = "", hc_time;
    private float money;
    private int st_y, st_m, st_d, st_h, st_f;
    private int hc_y, hc_m, hc_d, hc_h, hc_f;
    private int xz_y, xz_m, xz_d, xz_h, xz_f;
    private String xz_time;
    boolean isfristSelect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carxuzu);
        ((MyApplication) getApplication()).addActivity(this);
        // PushAgent.getInstance(this).onAppStart();
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

        dateLayout = (FrameLayout) findViewById(R.id.view_xuzu_dialog_date_layout);
        datecancelView = (TextView) findViewById(R.id.view_xuzu_dialog_date_cancel);

        dateokView = (TextView) findViewById(R.id.view_xuzu_dialog_date_ok);
        pickLayout = (LinearLayout) findViewById(R.id.view_xuzu_dialog_date_picklayout);
        pickLayout2 = (LinearLayout) findViewById(R.id.view_xuzu_dialog_date_picklayout2);

        danhaoView = (TextView) findViewById(R.id.view_xuzu_dialog_danhao);

        cancelView = (TextView) findViewById(R.id.view_xuzu_dialog_cancel);
        okView = (TextView) findViewById(R.id.view_xuzu_dialog_ok);
        shijianView = (TextView) findViewById(R.id.view_xuzu_dialog_time);
        moneyView = (TextView) findViewById(R.id.view_xuzu_dialog_money);

        datecancelView.setOnClickListener(this);
        dateokView.setOnClickListener(this);
        dateLayout.setOnClickListener(this);
        cancelView.setOnClickListener(this);
        okView.setOnClickListener(this);
        shijianView.setOnClickListener(this);
        danhao = getIntent().getStringExtra("danhao");
        hc_time = getIntent().getStringExtra("hc_time");

        if (!TextUtils.isEmpty(getIntent().getStringExtra("zujin"))) {
            money = Float.parseFloat(getIntent().getStringExtra("zujin"));
        }

        String hc_date = hc_time.split(" ")[0];
        String hc_time_time = hc_time.split(" ")[1];

        hc_y = Integer.parseInt(hc_date.split("-")[0]);
        hc_m = Integer.parseInt(hc_date.split("-")[1]);
        hc_d = Integer.parseInt(hc_date.split("-")[2]);

        hc_h = Integer.parseInt(hc_time_time.split(":")[0]);
        hc_f = Integer.parseInt(hc_time_time.split(":")[1]);


        st_y = hc_y;
        st_m = hc_m;
        st_d = hc_d;
        st_h = hc_h;
        st_f = hc_f;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int fen = calendar.get(Calendar.MINUTE);
        if (PublicUtil.getTimeFromDate(hc_y + "-" + oneTo2(hc_m) + "-"
                + oneTo2(hc_d) + " " + oneTo2(hc_h) + ":" + oneTo2(hc_f)
                + ":00") < (System.currentTimeMillis() / 1000)) {

            st_y = year;
            st_m = month;
            st_d = day;
            if (hour >= 9 && hour <= 17) {
                st_h = hour;
                st_f = fen;

            }

        }

        moneyView.setText("￥" + money);

        danhaoView.setText(danhao);

        for (int i = 0; i < hours.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("value", hours[i]);
            map.put("infor", hours[i] + "小时");

            arrayList.add(map);

        }

        myWheelView = new WheelView(CarxuzuActivity.this);
        myWheelView2 = new WheelView(CarxuzuActivity.this);

        for (int i = 0; i < hours.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("infor", hours[i]);
            map.put("hour", hours[i].split(":")[0]);
            map.put("fen", hours[i].split(":")[1]);

            arrayList3.add(map);

        }

        myWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex) {
                // TODO Auto-generated method stub
                // super.onSelected(selectedIndex);
                if (myWheelView.getSeletedIndex() == 0) {
                    myWheelView2.setDate(arrayList2);
                    pickLayout2.removeAllViews();
                    pickLayout2.addView(myWheelView2);
                } else if (myWheelView.getSeletedIndex() != 0) {
                    myWheelView2.setDate(arrayList3);
                    pickLayout2.removeAllViews();
                    pickLayout2.addView(myWheelView2);
                }

            }
        });

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.view_xuzu_dialog_cancel:

                finish();
                break;

            case R.id.view_xuzu_dialog_time:
                setDate();
                setTime();
                if (dateLayout.getVisibility() == View.GONE) {
                    dateLayout.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.view_xuzu_dialog_ok:

                if (TextUtils.isEmpty(xz_time)) {
                    PublicUtil.showToast(CarxuzuActivity.this, "请选择续租时间", false);
                    return;
                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ORDER_RELET);
                map.put("order_no", PublicUtil.toUTF(danhao));
                map.put("relet_time", xz_time);

                AnsynHttpRequest.httpRequest(CarxuzuActivity.this,
                        AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_RELET,
                        map, false, true, true);

                break;
            case R.id.view_xuzu_dialog_date_layout:

                break;
            case R.id.view_xuzu_dialog_date_cancel:
                if (dateLayout.getVisibility() == View.VISIBLE) {
                    dateLayout.setVisibility(View.GONE);
                }

                break;

            case R.id.view_xuzu_dialog_date_ok:

                int datecount = myWheelView.getSeletedIndex();
                int timecount = myWheelView2.getSeletedIndex();

                xz_y = (Integer) arrayList.get(datecount).get("year");
                xz_m = (Integer) arrayList.get(datecount).get("month");
                xz_d = (Integer) arrayList.get(datecount).get("day");

                if (datecount == 0) {
                    xz_h = Integer.parseInt((String) arrayList2.get(timecount).get(
                            "hour"));
                    xz_f = Integer.parseInt((String) arrayList2.get(timecount).get(
                            "fen"));
                } else {
                    xz_h = Integer.parseInt((String) arrayList3.get(timecount).get(
                            "hour"));
                    xz_f = Integer.parseInt((String) arrayList3.get(timecount).get(
                            "fen"));
                }

                xz_time = xz_y + "-" + oneTo2(xz_m) + "-" + oneTo2(xz_d) + "+"
                        + oneTo2(xz_h) + ":" + oneTo2(xz_f) + ":00";
                shijianView.setText(xz_y + "-" + oneTo2(xz_m) + "-" + oneTo2(xz_d)
                        + " " + oneTo2(xz_h) + ":" + oneTo2(xz_f));

                jisuanZujin_shijan();

                dateLayout.setVisibility(View.GONE);

                break;

            default:
                break;
        }

    }

    /**
     * 根据不同情况计算租金和时间
     */
    private void jisuanZujin_shijan() {

        if (hc_y == 0 || hc_m == 0 || hc_d == 0 || xz_y == 0 || xz_m == 0
                || xz_d == 0) {
            return;
        }

        String start = hc_y + "-" + oneTo2(hc_m) + "-" + oneTo2(hc_d) + " "
                + oneTo2(hc_h) + ":" + oneTo2(hc_f);
        String end = xz_y + "-" + oneTo2(xz_m) + "-" + oneTo2(xz_d) + " "
                + oneTo2(xz_h) + ":" + oneTo2(xz_f);

        int alltime = PublicUtil.compareFen(start, end);

        if (alltime <= 30) {
            alltime = 1;
        } else {

            if (alltime % 60 != 0) {
                alltime = alltime / 60 + 1;
            } else {
                alltime = alltime / 60;
            }

        }

        moneyView.setText("￥" + PublicUtil.toTwo((money * alltime)));

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
                case Constent.ID_ORDER_RELET:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_xuzu);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_xuzu;
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

    private int error_httprequest_xuzu = 0x6600;
    private int success_httprequest_xuzu = 0x6601;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_httprequest_xuzu) {

                if (msg.obj != null) {
                    PublicUtil.showToast(CarxuzuActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_xuzu) {
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);
                        PublicUtil.showToast(CarxuzuActivity.this,
                                jsonObject.getString("msg"), false);
                        if ("0".equals(jsonObject.getString("errcode"))) {
                            Intent intent = new Intent();
                            intent.putExtra("result", "success");
                            CarxuzuActivity.this.setResult(41, intent);
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
        int nowmonth = st_m;
        int nowyear = st_y;
        int nowday = st_d;

        int alldays = getDaysByYearMonth(nowyear, nowmonth);
        if (st_h >= 18 || (st_h == 17 && st_f >= 30)) {// 17:30以后就换一天
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

        for (int i = nowday; i <= alldays; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("infor", nowmonth + "月" + i + "日      "
                    + getWeekByYearMonth(nowyear, nowmonth, i));
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

            map.put("infor", nowmonth + "月" + i + "日      "
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

            map.put("infor", nowmonth + "月" + i + "日      "
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

                map.put("infor", nowmonth + "月" + i + "日      "
                        + getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("year", nowyear);
                map.put("month", nowmonth);
                map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
                map.put("day", i);
                arrayList.add(map);
            }

        }

        pickLayout.removeAllViews();
        myWheelView.setDate(arrayList);
        pickLayout.addView(myWheelView);

    }

    /**
     * 设置还取车时间
     */
    private void setTime() {
        arrayList2.clear();
        int hourstart;

        if (isfristSelect) {
            st_h = st_h + 1;
            isfristSelect = false;
        }

        if (st_h >= 18 || (st_h == 17 && st_f >= 30)) {// 17:30以后就换一天
            hourstart = 0;
        } else {

            int nowhour = st_h;

            hourstart = (nowhour - 9) * 2;
            if (st_f > 0 && st_f <= 30) {
                hourstart = hourstart + 1;
            } else if (st_f > 30) {
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

        pickLayout2.removeAllViews();
        myWheelView2.setDate(arrayList2);
        pickLayout2.addView(myWheelView2);
    }

}
