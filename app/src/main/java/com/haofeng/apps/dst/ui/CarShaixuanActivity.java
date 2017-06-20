package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.CarShaixuanRJListAdapter;
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

public class CarShaixuanActivity extends BaseActivity implements
        OnClickListener {
    private final String TAG = "CarShaixuanActivity";
    private FrameLayout topLayout;
    private TextView backView;
    private TextView storeView;
    private FrameLayout storeLayout;
    private LinearLayout qucheLayout, haicheLayout;
    private TextView quchetimeView, haichetimeView, qucheweekView,
            haicheweekView, qucheinforView, haicheinforView;
    private ListView listView;
    private LinearLayout listLayout;
    private GridView rongjiGridView;

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

    private String store_id = "";// 门店id
    private String pay_cube = "";// 货箱容积
    private SimpleAdapter simpleAdapter;
    private CarShaixuanRJListAdapter carShaixuanRJListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carshaixuan);
        addActivity(this);
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

        topLayout = (FrameLayout) findViewById(R.id.carshaixuan_top_layout);
        if (android.os.Build.VERSION.SDK_INT < 19) {
            topLayout.setPadding(0, 0, 0, 0);
        }

        backView = (TextView) findViewById(R.id.carshaixuan_back);
        storeView = (TextView) findViewById(R.id.carshaixuan_mendian);
        storeLayout = (FrameLayout) findViewById(R.id.carshaixuan_mendian_layout);
        qucheLayout = (LinearLayout) findViewById(R.id.carshaixuan_quche_layout);
        haicheLayout = (LinearLayout) findViewById(R.id.carshaixuan_haiche_layout);
        quchetimeView = (TextView) findViewById(R.id.carshaixuan_quche_time);
        haichetimeView = (TextView) findViewById(R.id.carshaixuan_haiche_time);
        qucheweekView = (TextView) findViewById(R.id.carshaixuan_quche_week);
        haicheweekView = (TextView) findViewById(R.id.carshaixuan_haiche_week);
        qucheinforView = (TextView) findViewById(R.id.carshaixuan_quche_infor);
        haicheinforView = (TextView) findViewById(R.id.carshaixuan_haiche_infor);
        rongjiGridView = (GridView) findViewById(R.id.carshaixuan_rongji_list);

        listView = (ListView) findViewById(R.id.carshaixuan_listview);
        listLayout = (LinearLayout) findViewById(R.id.carshaixuan_listview_layout);

        okView = (TextView) findViewById(R.id.carshaixuan_ok);

        dateLayout = (FrameLayout) findViewById(R.id.carshaixuan_date_layout);
        datecancelView = (TextView) findViewById(R.id.carshaixuan_date_cancel);
        datetypeView = (TextView) findViewById(R.id.carshaixuan_date_type);
        dateokView = (TextView) findViewById(R.id.carshaixuan_date_ok);
        pickLayout = (LinearLayout) findViewById(R.id.carshaixuan_date_picklayout);
        pickLayout2 = (LinearLayout) findViewById(R.id.carshaixuan_date_picklayout2);
        backView.setOnClickListener(this);
        storeLayout.setOnClickListener(this);
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
                + ":" + fen, 0);

        wheelPicker = new WheelView(CarShaixuanActivity.this);
        wheelPicker2 = new WheelView(CarShaixuanActivity.this);

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
                // super.onSelected(selectedIndex);
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

        carShaixuanRJListAdapter = new CarShaixuanRJListAdapter(
                CarShaixuanActivity.this);
        rongjiGridView.setAdapter(carShaixuanRJListAdapter);

        rongjiGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                for (int i = 0; i < rongjiList.size(); i++) {
                    Map<String, String> map = rongjiList.get(i);
                    // map.remove("choose");

                    if (i == arg2) {

                        if ("1".equals(map.get("choose"))) {
                            map.remove("choose");
                            map.put("choose", "0");
                            pay_cube = "";
                        } else {
                            map.remove("choose");
                            map.put("choose", "1");
                            pay_cube = map.get("value");
                        }
                    } else {
                        map.remove("choose");
                        map.put("choose", "0");
                    }

                    rongjiList.remove(i);
                    rongjiList.add(i, map);
                }

                carShaixuanRJListAdapter.setData(rongjiList);
                carShaixuanRJListAdapter.notifyDataSetChanged();

            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                store_id = list.get(arg2).get("id");
                String name = list.get(arg2).get("store_name");
                String addr = list.get(arg2).get("addr");

                storeView.setText(name);
                listLayout.setVisibility(View.GONE);
            }
        });

        simpleAdapter = new SimpleAdapter(CarShaixuanActivity.this, list,
                R.layout.listview_item_citystores2, new String[]{"id",
                "store_name"}, new int[]{
                R.id.list_item_citystors2_id,
                R.id.list_item_citystors2_name});
        listView.setAdapter(simpleAdapter);

        String city_id = getIntent().getStringExtra("city_id");
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_CAR_GET_STORE_LIST);
        map.put("city_id", city_id);

        AnsynHttpRequest.httpRequest(CarShaixuanActivity.this,
                AnsynHttpRequest.GET, callBack, Constent.ID_CAR_GET_STORE_LIST,
                map, false, true, true);

        map = new HashMap<String, String>();
        map.put("act", Constent.ACT_CAR_GET_PAY_CUBE_LIST);

        AnsynHttpRequest.httpRequest(CarShaixuanActivity.this,
                AnsynHttpRequest.GET, callBack,
                Constent.ID_CAR_GET_PAY_CUBE_LIST, map, false, false, true);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.carshaixuan_back:

                Intent intent = new Intent();
                intent.putExtra("store_id", "");
                intent.putExtra("pay_cube", "");
                intent.putExtra("qc_time", "");
                intent.putExtra("hc_time", "");
                CarShaixuanActivity.this.setResult(14, intent);
                finish();
                break;

            case R.id.carshaixuan_quche_layout:
                choosetimetype = 0;
                datetypeView.setText("取车时间");
                setDate();
                setTime();
                if (dateLayout.getVisibility() == View.GONE) {
                    dateLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.carshaixuan_haiche_layout:

                if (qucheyear == 0 || quchemonth == 0 || qucheday == 0) {
                    PublicUtil.showToast(CarShaixuanActivity.this, "请先选择取车时间",
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
            case R.id.carshaixuan_mendian_layout:
                if (list.size() > 0) {
                    if (listLayout.getVisibility() == View.VISIBLE) {
                        listLayout.setVisibility(View.GONE);
                    } else {
                        listLayout.setVisibility(View.VISIBLE);
                    }
                }

                break;

            case R.id.carshaixuan_ok:
                String start = "",
                        end = "";
                if (qucheyear != 0 || qucheday != 0 || quchemonth != 0) {
                    start = qucheyear + "-" + oneTo2(quchemonth) + "-"
                            + oneTo2(qucheday) + "+" + oneTo2(quchehour) + ":"
                            + oneTo2(quchefen) + ":00";// 这里的加号是因为编码问题，代替空格
                }

                if (haicheyear != 0 || haichemonth != 0 || haicheday != 0) {
                    end = haicheyear + "-" + oneTo2(haichemonth) + "-"
                            + oneTo2(haicheday) + "+" + oneTo2(haichehour) + ":"
                            + oneTo2(haichefen) + ":00";
                }

                intent = new Intent();

                intent.putExtra("store_id", store_id);
                intent.putExtra("pay_cube", pay_cube);
                intent.putExtra("qc_time", start);
                intent.putExtra("hc_time", end);
                CarShaixuanActivity.this.setResult(14, intent);
                finish();

                break;
            case R.id.carshaixuan_date_layout:

                break;
            case R.id.carshaixuan_date_cancel:
                if (dateLayout.getVisibility() == View.VISIBLE) {
                    dateLayout.setVisibility(View.GONE);
                }

                break;

            case R.id.carshaixuan_date_ok:

                int datecount = wheelPicker.getSeletedIndex();
                int timecount = wheelPicker2.getSeletedIndex();

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

                    quchetimeView.setText(oneTo2(quchemonth) + "/"
                            + oneTo2(qucheday) + "  " + oneTo2(quchehour) + ":"
                            + oneTo2(quchefen));
                    qucheweekView.setText((CharSequence) arrayList.get(datecount)
                            .get("week"));
                    qucheinforView.setVisibility(View.GONE);
                    quchetimeView.setVisibility(View.VISIBLE);
                    qucheweekView.setVisibility(View.VISIBLE);
                    haicheinforView.setVisibility(View.VISIBLE);
                    qucheLayout.setBackgroundResource(R.drawable.image_date_green);
                    haicheLayout.setBackgroundResource(R.drawable.image_05);
                    haichetimeView.setText("");
                    haicheweekView.setText("");
                    haichetimeView.setVisibility(View.GONE);
                    haicheweekView.setVisibility(View.GONE);

                    haicheyear = 0;
                    haichemonth = 0;
                    haicheday = 0;
                    haichehour = 0;
                    haichefen = 0;

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

                    haichetimeView.setText(oneTo2(haichemonth) + "/"
                            + oneTo2(haicheday) + "  " + oneTo2(haichehour) + ":"
                            + oneTo2(haichefen));
                    haicheweekView.setText((CharSequence) arrayList.get(datecount)
                            .get("week"));
                    haicheinforView.setVisibility(View.GONE);
                    haichetimeView.setVisibility(View.VISIBLE);
                    haicheweekView.setVisibility(View.VISIBLE);
                    haicheLayout.setBackgroundResource(R.drawable.image_date_green);

                    PublicUtil.logDbug(TAG, qucheyear + ":" + quchemonth + ":"
                            + qucheday + ":" + quchehour + ":" + quchefen, 0);
                    PublicUtil.logDbug(TAG, haicheyear + ":" + haichemonth + ":"
                            + haicheday + ":" + haichehour + ":" + haichefen, 0);
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

                Intent intent = new Intent();
                intent.putExtra("store_id", "");
                intent.putExtra("pay_cube", "");
                intent.putExtra("qc_time", "");
                intent.putExtra("hc_time", "");
                CarShaixuanActivity.this.setResult(14, intent);
                finish();
                return true;
            }

        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private JSONObject jsonObject = null, rongjiJsonObject;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub

            switch (backId) {
                case Constent.ID_CAR_GET_STORE_LIST:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
                                handler.sendEmptyMessage(success_http_getstore);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getstore;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;

                case Constent.ID_CAR_GET_PAY_CUBE_LIST:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                rongjiJsonObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
                                handler.sendEmptyMessage(success_http_getrongji);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getrongji;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;
                default:
                    break;
            }

        }
    };

    private int error_http_getstore = 0x8006;
    private int success_http_getstore = 0x8007;
    private int error_http_getrongji = 0x9000;
    private int success_http_getrongji = 0x9001;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg != null && msg.what == error_http_getrongji) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarShaixuanActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getrongji) {
                if (rongjiJsonObject != null) {
                    try {

                        if ("0".equals(rongjiJsonObject.getString("errcode"))) {

                            JSONObject dataObject = rongjiJsonObject
                                    .getJSONObject("data");

                            JSONArray listArray = dataObject
                                    .getJSONArray("list");

                            if (listArray != null) {
                                flashRongji(listArray);
                                // flushlist(dataArray);
                            }

                        } else {
                            PublicUtil.showToast(CarShaixuanActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(
                                        CarShaixuanActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            }
            if (msg != null && msg.what == error_http_getstore) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarShaixuanActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getstore) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");

                            if (dataArray != null) {
                                flushlist(dataArray);
                            }

                        } else {
                            PublicUtil.showToast(CarShaixuanActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(
                                        CarShaixuanActivity.this,
                                        LoginActivity.class);
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

    private List<Map<String, String>> rongjiList = new ArrayList<Map<String, String>>();

    /**
     * 容积布局
     *
     * @param array
     */
    private void flashRongji(JSONArray array) {

        try {
            JSONObject jsonObject;
            for (int i = 0; i < array.length(); i++) {
                jsonObject = array.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();
                map.put("value", jsonObject.getString("value"));
                map.put("text", jsonObject.getString("text"));
                map.put("choose", "0");

                rongjiList.add(map);

            }
            carShaixuanRJListAdapter.setData(rongjiList);
            carShaixuanRJListAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

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

            if (hour >= 16) {
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
                    map.put("infor", nowmonth + "月" + i + "日        "
                            + getWeekByYearMonth(nowyear, nowmonth, i));
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

                    map.put("infor", nowmonth + "月" + i + "日        "
                            + getWeekByYearMonth(nowyear, nowmonth, i));
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
        if (choosetimetype == 0) {// 取车

            if (hour >= 16) {
                hourstart = 0;
            } else {

                int nowhour = hour + 2;

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
            hourstart = (quchehour - 9) * 2;
            if (quchefen == 30) {
                hourstart = hourstart + 1;
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

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();

    private void flushlist(JSONArray dataArray) {
        list.clear();
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();

                map.put("id", object.getString("id"));
                map.put("store_name", object.getString("store_name"));
                map.put("addr", object.getString("addr"));

                list.add(map);
            }
            simpleAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
