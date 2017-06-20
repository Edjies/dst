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
import android.widget.EditText;
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

import static com.haofeng.apps.dst.R.id.changzu_ok;

public class CarChangzuActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "CarChangzuActivity";
    private final int REQUESTCODE = 0x7410;
    private FrameLayout topLayout;
    private TextView backView;
    private TextView quchecityView, quchedateView, zuchetimeView,
            zuchenumberView, cheliangpingpaiView, cheliangleixingView;
    private EditText nameEditText, phoneEditText;

    private TextView okView;
    private FrameLayout dateLayout;
    private TextView datecancelView, dateokView;
    private WheelView wheelPicker, wheelPicker2, wheelPicker3;
    private LinearLayout pickLayout, pickLayout2, pickLayout3;
    private TextView jiaView, jianView;

    private int year, month, day, hour, fen;
    private int qucheyear, quchemonth, qucheday, quchehour, quchefen;

    private Calendar calendar = Calendar.getInstance();

    private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();// 全部时间
    private String[] hours = {"09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30",
            "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00"};

    private List<Map<String, Object>> timearrayList = new ArrayList<Map<String, Object>>();// 全部时间
    private List<Map<String, Object>> pingpaiarrayList = new ArrayList<Map<String, Object>>();// pingpai
    private List<Map<String, Object>> leixingarrayList = new ArrayList<Map<String, Object>>();// 类型

    private List<Map<String, String>> allcartypesList = new ArrayList<Map<String, String>>();// 车辆所有类型

    private String[] times = {"一年", "十一个月", "十个月", "九个月", "八个月", "七个月", "六个月",
            "五个月", "四个月"};

    private String[] brands = {"比亚迪", "瑞驰", "北汽新能源", "东风特种车"};

    private String city_id, city_name;
    private int number = 1;
    private int picktype = 1;// 1 时间 2 时长 3 品牌 4 类型
    private String zc_time = "", car_type = "", car_brand = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carchangzu);
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

        topLayout = (FrameLayout) findViewById(R.id.changzu_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.changzu_back);
        quchecityView = (TextView) findViewById(R.id.changzu_quche_city);
        quchedateView = (TextView) findViewById(R.id.changzu_quche_date);
        zuchetimeView = (TextView) findViewById(R.id.changzu_zuche_time);
        zuchenumberView = (TextView) findViewById(R.id.changzu_zuche_number);
        cheliangpingpaiView = (TextView) findViewById(R.id.changzu_zuche_pingpai);
        cheliangleixingView = (TextView) findViewById(R.id.changzu_zuche_chexing);
        jiaView = (TextView) findViewById(R.id.changzu_zuche_number_jia);
        jianView = (TextView) findViewById(R.id.changzu_zuche_number_jian);

        nameEditText = (EditText) findViewById(R.id.changzu_zuche_name);
        phoneEditText = (EditText) findViewById(R.id.changzu_zuche_phone);
        okView = (TextView) findViewById(changzu_ok);

        dateLayout = (FrameLayout) findViewById(R.id.changzu_date_layout);
        datecancelView = (TextView) findViewById(R.id.changzu_date_cancel);
        dateokView = (TextView) findViewById(R.id.changzu_date_ok);
        pickLayout = (LinearLayout) findViewById(R.id.changzu_date_picklayout);
        pickLayout2 = (LinearLayout) findViewById(R.id.changzu_date_picklayout2);
        pickLayout3 = (LinearLayout) findViewById(R.id.changzu_date_picklayout3);

        zuchenumberView.setText(number + "");

        backView.setOnClickListener(this);

        okView.setOnClickListener(this);
        datecancelView.setOnClickListener(this);
        dateokView.setOnClickListener(this);
        dateLayout.setOnClickListener(this);

        quchecityView.setOnClickListener(this);
        quchedateView.setOnClickListener(this);
        zuchetimeView.setOnClickListener(this);
        cheliangpingpaiView.setOnClickListener(this);
        cheliangleixingView.setOnClickListener(this);
        jiaView.setOnClickListener(this);
        jianView.setOnClickListener(this);

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

        wheelPicker = new WheelView(CarChangzuActivity.this);
        wheelPicker2 = new WheelView(CarChangzuActivity.this);
        wheelPicker3 = new WheelView(CarChangzuActivity.this);

        for (int i = 0; i < hours.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("infor", hours[i]);
            map.put("hour", hours[i].split(":")[0]);
            map.put("fen", hours[i].split(":")[1]);

            arrayList3.add(map);

        }

        for (int i = 0; i < times.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("infor", times[i]);

            timearrayList.add(map);

        }

        for (int i = 0; i < brands.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("infor", brands[i]);

            pingpaiarrayList.add(map);

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

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_CAR_GET_CAR_BRABD);

        AnsynHttpRequest.httpRequest(CarChangzuActivity.this,
                AnsynHttpRequest.GET, callBack, Constent.ID_CAR_GET_CAR_BRABD,
                map, false, false, true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE && resultCode == 11 && data != null) {

            PublicUtil.logDbug(TAG, "" + resultCode + "", 0);
            PublicUtil.logDbug(TAG, "" + data.getStringExtra("name") + "", 0);

            city_id = data.getStringExtra("id");
            city_name = data.getStringExtra("name");
            quchecityView.setText(city_name);

        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        switch (arg0.getId()) {
            case R.id.changzu_back:

                finish();
                break;

            case R.id.changzu_quche_city:
                Intent intent = new Intent(CarChangzuActivity.this,
                        AddrListActivity.class);
                startActivityForResult(intent, REQUESTCODE);
                break;

            case R.id.changzu_zuche_time:
                picktype = 2;
                if (timearrayList.size() > 0) {
                    wheelPicker3.setDate(timearrayList);
                    pickLayout3.removeAllViews();
                    pickLayout3.addView(wheelPicker3);
                    dateLayout.setVisibility(View.VISIBLE);
                    pickLayout.setVisibility(View.GONE);
                    pickLayout2.setVisibility(View.GONE);
                    pickLayout3.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.changzu_zuche_number_jia:
                number++;
                zuchenumberView.setText(number + "");
                break;
            case R.id.changzu_zuche_number_jian:
                number--;
                if (number < 1) {
                    number = 1;
                }
                zuchenumberView.setText(number + "");
                break;
            case R.id.changzu_zuche_pingpai:
                picktype = 3;
                if (pingpaiarrayList.size() > 0) {
                    wheelPicker3.setDate(pingpaiarrayList);
                    pickLayout3.removeAllViews();
                    pickLayout3.addView(wheelPicker3);
                    dateLayout.setVisibility(View.VISIBLE);
                    pickLayout.setVisibility(View.GONE);
                    pickLayout2.setVisibility(View.GONE);
                    pickLayout3.setVisibility(View.VISIBLE);
                    if (!cheliangleixingView.getText().toString().equals("请选择")) {
                        cheliangleixingView.setText("请选择");
                        car_type = "";
                    }
                }

                break;
            case R.id.changzu_zuche_chexing:
                picktype = 4;
                if (TextUtils.isEmpty(car_brand)) {
                    PublicUtil
                            .showToast(CarChangzuActivity.this, "请先选择车辆品牌", false);
                    return;
                }

                leixingarrayList.clear();
                if ("比亚迪".equals(car_brand)) {

                    for (int i = 0; i < allcartypesList.size(); i++) {
                        Map<String, String> map = allcartypesList.get(i);

                        if ("byd".equals(map.get("type"))) {
                            Map<String, Object> typemap = new HashMap<String, Object>();

                            typemap.put("infor", map.get("value"));

                            leixingarrayList.add(typemap);

                        }

                    }

                } else if ("瑞驰".equals(car_brand)) {

                    for (int i = 0; i < allcartypesList.size(); i++) {
                        Map<String, String> map = allcartypesList.get(i);

                        if ("rc".equals(map.get("type"))) {
                            Map<String, Object> typemap = new HashMap<String, Object>();

                            typemap.put("infor", map.get("value"));

                            leixingarrayList.add(typemap);

                        }

                    }

                } else if ("北汽新能源".equals(car_brand)) {

                    for (int i = 0; i < allcartypesList.size(); i++) {
                        Map<String, String> map = allcartypesList.get(i);

                        if ("bq".equals(map.get("type"))) {
                            Map<String, Object> typemap = new HashMap<String, Object>();

                            typemap.put("infor", map.get("value"));

                            leixingarrayList.add(typemap);

                        }

                    }

                } else if ("东风特种车".equals(car_brand)) {

                    for (int i = 0; i < allcartypesList.size(); i++) {
                        Map<String, String> map = allcartypesList.get(i);

                        if ("df".equals(map.get("type"))) {
                            Map<String, Object> typemap = new HashMap<String, Object>();

                            typemap.put("infor", map.get("value"));

                            leixingarrayList.add(typemap);

                        }

                    }

                }

                if (leixingarrayList.size() > 0) {
                    wheelPicker3.setDate(leixingarrayList);
                    pickLayout3.removeAllViews();
                    pickLayout3.addView(wheelPicker3);
                    dateLayout.setVisibility(View.VISIBLE);
                    pickLayout.setVisibility(View.GONE);
                    pickLayout2.setVisibility(View.GONE);
                    pickLayout3.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.changzu_quche_date:
                picktype = 1;
                setDate();
                setTime();
                dateLayout.setVisibility(View.VISIBLE);
                pickLayout.setVisibility(View.VISIBLE);
                pickLayout2.setVisibility(View.VISIBLE);
                pickLayout3.setVisibility(View.GONE);
                break;

            case changzu_ok:

                if (qucheyear == 0 || qucheday == 0 || quchemonth == 0) {
                    PublicUtil.showToast(CarChangzuActivity.this, "请选择取车日期", false);
                    return;
                }

                String start = qucheyear + "-" + oneTo2(quchemonth) + "-"
                        + oneTo2(qucheday) + "+" + oneTo2(quchehour) + ":"
                        + oneTo2(quchefen) + ":00";
                if (TextUtils.isEmpty(city_name)) {
                    PublicUtil.showToast(CarChangzuActivity.this, "请选择取车城市", false);
                    return;
                }

                if (TextUtils.isEmpty(zc_time)) {
                    PublicUtil.showToast(CarChangzuActivity.this, "请选择租车时长", false);
                    return;
                }

                if (TextUtils.isEmpty(car_type)) {
                    PublicUtil.showToast(CarChangzuActivity.this, "请选择车辆类型", false);
                    return;
                }
                if (TextUtils.isEmpty(car_brand)) {
                    PublicUtil.showToast(CarChangzuActivity.this, "请选择车辆品牌", false);
                    return;
                }
                String qc_name = nameEditText.getText().toString();
                if (TextUtils.isEmpty(qc_name)) {
                    PublicUtil.showToast(CarChangzuActivity.this, "联系人姓名不能为空",
                            false);
                    return;
                }

                String qc_phone = phoneEditText.getText().toString();
                if (TextUtils.isEmpty(qc_name)) {
                    PublicUtil.showToast(CarChangzuActivity.this, "联系人电话不能为空",
                            false);
                    return;
                }
                if (!PublicUtil.isMobileNO(qc_phone)) {
                    PublicUtil.showToast(CarChangzuActivity.this, "联系人电话号码格式错误",
                            false);
                    return;
                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_LONG_RENT);

                map.put("car_type", PublicUtil.toUTF(car_type));
                map.put("car_brand", PublicUtil.toUTF(car_brand));

                map.put("zc_time", PublicUtil.toUTF(zc_time));
                map.put("zc_num", String.valueOf(number));
                map.put("contact_name", PublicUtil.toUTF(qc_name));
                map.put("contact_mobile", qc_phone);

                map.put("qc_city", PublicUtil.toUTF(city_name));
                map.put("qc_date", start);

                AnsynHttpRequest.httpRequest(CarChangzuActivity.this,
                        AnsynHttpRequest.GET, callBack, Constent.ID_LONG_RENT, map,
                        false, true, true);

                break;
            case R.id.changzu_date_layout:

                break;
            case R.id.changzu_date_cancel:
                if (dateLayout.getVisibility() == View.VISIBLE) {
                    dateLayout.setVisibility(View.GONE);
                }

                break;

            case R.id.changzu_date_ok:

                if (picktype == 1) {
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

                    quchedateView.setText(qucheyear + "-" + oneTo2(quchemonth)
                            + "-" + oneTo2(qucheday) + " " + oneTo2(quchehour)
                            + ":" + oneTo2(quchefen) + ":00");

                } else if (picktype == 2) {
                    int datecount = wheelPicker3.getSeletedIndex();
                    zc_time = (String) timearrayList.get(datecount).get("infor");
                    zuchetimeView.setText(zc_time);
                } else if (picktype == 3) {
                    int datecount = wheelPicker3.getSeletedIndex();
                    car_brand = (String) pingpaiarrayList.get(datecount).get(
                            "infor");
                    cheliangpingpaiView.setText(car_brand);
                } else if (picktype == 4) {
                    int datecount = wheelPicker3.getSeletedIndex();
                    car_type = (String) leixingarrayList.get(datecount)
                            .get("infor");
                    cheliangleixingView.setText(car_type);
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
                case Constent.ID_CAR_GET_CAR_BRABD:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
                                handler.sendEmptyMessage(success_http_getbrand);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getbrand;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;
                case Constent.ID_LONG_RENT:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
                                handler.sendEmptyMessage(success_http_long);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_long;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;
                default:
                    break;
            }

        }
    };

    private int error_http_getbrand = 0x9100;
    private int success_http_getbrand = 0x9101;
    private int error_http_long = 0x9103;
    private int success_http_long = 0x9104;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_http_getbrand) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarChangzuActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getbrand) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONObject dataObject = jsonObject
                                    .getJSONObject("data");

                            if (dataObject != null) {

                                JSONArray bydJsonArray = dataObject
                                        .getJSONArray("比亚迪");
                                if (bydJsonArray != null) {
                                    for (int i = 0; i < bydJsonArray.length(); i++) {

                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("type", "byd");
                                        map.put("value",
                                                bydJsonArray.getString(i));
                                        allcartypesList.add(map);
                                    }

                                }

                                bydJsonArray = dataObject.getJSONArray("瑞驰");
                                if (bydJsonArray != null) {
                                    for (int i = 0; i < bydJsonArray.length(); i++) {

                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("type", "rc");
                                        map.put("value",
                                                bydJsonArray.getString(i));
                                        allcartypesList.add(map);
                                    }

                                }

                                bydJsonArray = dataObject.getJSONArray("北汽新能源");
                                if (bydJsonArray != null) {
                                    for (int i = 0; i < bydJsonArray.length(); i++) {

                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("type", "bq");
                                        map.put("value",
                                                bydJsonArray.getString(i));
                                        allcartypesList.add(map);
                                    }

                                }

                                bydJsonArray = dataObject.getJSONArray("东风特种车");
                                if (bydJsonArray != null) {
                                    for (int i = 0; i < bydJsonArray.length(); i++) {

                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("type", "df");
                                        map.put("value",
                                                bydJsonArray.getString(i));
                                        allcartypesList.add(map);
                                    }

                                }

                            }

                        } else {
                            PublicUtil.showToast(CarChangzuActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(
                                        CarChangzuActivity.this,
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

            if (msg != null && msg.what == error_http_long) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarChangzuActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_long) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            Intent intent = new Intent(CarChangzuActivity.this,
                                    CarChangzuFinshActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            PublicUtil.showToast(CarChangzuActivity.this,
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

        if (hour >= 16) {
            hourstart = 0;
        } else if (hour < 9) {
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

}
