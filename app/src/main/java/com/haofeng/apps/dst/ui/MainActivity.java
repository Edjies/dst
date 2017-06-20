package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.ResAccount;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.UpdateAppManager;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.haofeng.apps.dst.R.id.activity_main_version_update_layout;
import static com.haofeng.apps.dst.utils.PublicUtil.getStorage_string;


public class MainActivity extends BaseActivity implements OnClickListener {
    public final static int WHAT_INTENT_TO_ORDER_LIST = 1;
    private static String TAG = "MainActivity";
    private String islogin = "0";
    private String token = "", phone = "";
    private TextView mTvStatusBar;
    private RelativeLayout topLayout;
    private LinearLayout saomaLayout, diangdanLayout, duanzuLayout;
    private TextView weatherView, weatherinforView;
    private LinearLayout chargeLayout;
    private String vcr_id, id, measuring_point;
    private ImageView weatherImageView;
    private ViewPager topViewPager;
    private Timer timer;
    private int nowcount = 0;
    private int images[] = {R.drawable.main_lunbo0, R.drawable.main_lunbo2, R.drawable.main_lunbo3,
            R.drawable.main_lubo4};
    private LinearLayout viewpagecountLayout;
    private ImageView[] pointImageViews;// 管理头部滑动广告的点点
    private TextView tempView;
    private LinearLayout searchPowerLayout;
    private LinearLayout saoMaAddPowerLayout;
    private LinearLayout myWzLayout;
    private LinearLayout rentCarOrderLayout;
    private LinearLayout shengQingLongRentLayout;
    private LinearLayout fengshiRentCarLayout;
    private ImageButton slidingSwitchButton;
    private ImageButton messageButton;
    private ImageButton userButton;
    private DrawerLayout drawerLayout;
    private FrameLayout slidingMenuLayout;
    private LinearLayout serviceRegulaLayout;
    private LinearLayout commonProblemLayout;
    private LinearLayout returnSuggestionLayout;
    private RelativeLayout versionUpdateLayout;
    private LinearLayout cleanCacheLayout;
    private LinearLayout withUsLayout;
    private LinearLayout tellUsLayout;
    private LinearLayout shareFriendLayout;
    private LinearLayout showCleanCacheLayout;
    private String dstNewVersion;
    private TextView showVersionInfoView;
    private String updatePath;
    private boolean hasNewVersion = false;
    private String updateContent;
    private TextView userNameView;
    private TextView userPhoneView;
    private TextView userGradeView;
    private ImageView userImageView;
    private LinearLayout h5TestLayout;
    private LinearLayout mMenuTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addActivity(this);
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object arg0, int arg1) {
                // TODO Auto-generated method stub
                if (arg0 != null) {
                    token = String.valueOf(arg0);
                    regstToken();
                }
            }

            @Override
            public void onFail(Object arg0, int arg1, String arg2) {
                // TODO Auto-generated method stub

            }
        });
        init();
        initListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        parseIntent();
    }

    private void initListener() {
        drawerLayout.setDrawerListener(new MyDrawerListener());
        shengQingLongRentLayout.setOnClickListener(this);
        fengshiRentCarLayout.setOnClickListener(this);
        searchPowerLayout.setOnClickListener(this);
        saoMaAddPowerLayout.setOnClickListener(this);
        myWzLayout.setOnClickListener(this);
        rentCarOrderLayout.setOnClickListener(this);
        slidingSwitchButton.setOnClickListener(this);
        messageButton.setOnClickListener(this);
        userButton.setOnClickListener(this);
        serviceRegulaLayout.setOnClickListener(this);
        commonProblemLayout.setOnClickListener(this);
        returnSuggestionLayout.setOnClickListener(this);
        versionUpdateLayout.setOnClickListener(this);
        withUsLayout.setOnClickListener(this);
        tellUsLayout.setOnClickListener(this);
        shareFriendLayout.setOnClickListener(this);
        cleanCacheLayout.setOnClickListener(this);
        h5TestLayout.setOnClickListener(this);
        mMenuTest.setOnClickListener(this);
    }

    private void regstToken() {
        phone = getStorage_string(MainActivity.this, "phone", "");
        if (!TextUtils.isEmpty(phone)) {
            XGPushManager.registerPush(MainActivity.this, phone);
            ((MyApplication) getApplication()).setIsregstToken(true);
        }

    }

    /**
     * 初始化
     */
    public void init() {

        execGetAccountInfo();
        chechUpdate();
        String cityname = getStorage_string(MainActivity.this, "mycity", "");
        if (!TextUtils.isEmpty(cityname)) {
            cityname = cityname.replace("市", "");
            Map<String, String> map = new HashMap<String, String>();
            map.put("act", Constent.ACT_REGION_GET_ID);
            map.put("city_name", PublicUtil.toUTF(cityname));
            AnsynHttpRequest.httpRequest(MainActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_REGION_GET_ID,
                    map, false, false, true);
        }
        islogin = getStorage_string(MainActivity.this, "islogin", "0");

        setContentView(R.layout.tab_home_view);
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        topLayout = (RelativeLayout) findViewById(R.id.tab_home_toplayout);
        //setTopLayoutPadding(topLayout);

//        SwitchButton switchButton = (SwitchButton) findViewById(R.id.switch_button);
//        String isDebug = PublicUtil.getStorage_string(MainActivity.this, "isdebug", "1");
//        if (isDebug.equals("1")) {
//            switchButton.setChecked(false);
//        } else {
//            switchButton.setChecked(true);
//        }
//        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                if (isChecked) { //正式
//                    Constent.URLHEAD_GET = "http://app.dstcar.com/version2.2.0/app/";
//                    Constent.URLHEAD_IMAGE = "http://app.dstcar.com/version2.2.0/app/";
//                    PublicUtil.setStorage_string(MainActivity.this, "isdebug", "0");
//                } else { //测试
//                    Constent.URLHEAD_GET = "http://192.168.1.107/dstzc_new/version2.2.0/app/";
//                    Constent.URLHEAD_IMAGE = "http://192.168.1.107/dstzc_new/";
//                    PublicUtil.setStorage_string(MainActivity.this, "isdebug", "1");
//                }
//                PublicUtil.removeStorage_string(MainActivity.this, "islogin");
//                PublicUtil.removeStorage_string(MainActivity.this, "phone");
//                PublicUtil.removeStorage_string(MainActivity.this, "pwd");
//                PublicUtil
//                        .removeStorage_string(MainActivity.this, "loginkey2");
//                PublicUtil.removeStorage_string(MainActivity.this, "userid");
//                PublicUtil.removeStorage_string(MainActivity.this,
//                        "user_danwei");
//                PublicUtil.removeStorage_string(MainActivity.this,
//                        "user_nicheng");
//                PublicUtil.removeStorage_string(MainActivity.this,
//                        "mapsearchkey");
//                PublicUtil.removeStorage_string(MainActivity.this,
//                        "id_card_url");
//                PublicUtil.removeStorage_string(MainActivity.this,
//                        "id_card_url1");
//                PublicUtil.removeStorage_string(MainActivity.this,
//                        "driving_card_url");
//                PublicUtil.removeStorage_string(MainActivity.this,
//                        "business_license_url");
//                PublicUtil.removeStorage_string(MainActivity.this, "order_no");
//                AnsynHttpRequest.loginkey = null;
//                AnsynHttpRequest.userid = null;
//            }
//        });
        saomaLayout = (LinearLayout) findViewById(R.id.tab_home_saomalayout);
        diangdanLayout = (LinearLayout) findViewById(R.id.tab_home_dingdanlayout);
        duanzuLayout = (LinearLayout) findViewById(R.id.tab_home_duanzulayout);
        viewpagecountLayout = (LinearLayout) findViewById(R.id.tab_home_top_point_layout);
        weatherView = (TextView) findViewById(R.id.tab_home_weather);
        weatherinforView = (TextView) findViewById(R.id.tab_home_weatherinfor);
        chargeLayout = (LinearLayout) findViewById(R.id.tab_home_chargeing);
        weatherImageView = (ImageView) findViewById(R.id.tab_home_weatherimage);
        topViewPager = (ViewPager) findViewById(R.id.tab_home_top_images_vPager);
        weatherinforView = (TextView) findViewById(R.id.tab_home_weatherinfor);
        tempView = (TextView) findViewById(R.id.tab_home_temp);
        userImageView = (ImageView) findViewById(R.id.activity_main_user_image);

        //寻找电桩
        searchPowerLayout = (LinearLayout) findViewById(R.id.activity_main_search_power_layout);
        //扫码充电
        saoMaAddPowerLayout = (LinearLayout) findViewById(R.id.activity_main_saoma_addpower_layout);
        //我的违章
        myWzLayout = (LinearLayout) findViewById(R.id.activity_main_my_wz_layout);
        //租车订单
        rentCarOrderLayout = (LinearLayout) findViewById(R.id.activity_main_rentcar_order_layout);
        //侧栏
        serviceRegulaLayout = (LinearLayout) findViewById(R.id.activity_main_service_detail_layout);//服务规则
        commonProblemLayout = (LinearLayout) findViewById(R.id.activity_main_common_problem_layout);  //常见问题
        returnSuggestionLayout = (LinearLayout) findViewById(R.id.activity_main_return_suggestion_layout);  //意见反馈
        versionUpdateLayout = (RelativeLayout) findViewById(activity_main_version_update_layout);   //升级
        cleanCacheLayout = (LinearLayout) findViewById(R.id.activity_main_clean_cache_layout);//缓存清理
        withUsLayout = (LinearLayout) findViewById(R.id.activity_main_with_us_layout);    //关于我们
        h5TestLayout = (LinearLayout) findViewById(R.id.activity_main_h5_test_layout);
        mMenuTest = (LinearLayout) findViewById(R.id.menu_test);

        userNameView = (TextView) findViewById(R.id.activity_main_user_name);
        userPhoneView = (TextView) findViewById(R.id.activity_main_user_phone);
        userGradeView = (TextView) findViewById(R.id.activity_main_user_grade);

        shengQingLongRentLayout = (LinearLayout) findViewById(R.id.activity_main_shengqing_long_rent_layout); //申请长租
        fengshiRentCarLayout = (LinearLayout) findViewById(R.id.activity_main_fengshi_rentcar_layout);//分时租赁
        slidingSwitchButton = (ImageButton) findViewById(R.id.activity_main_sliding_switch);  //侧拉
        messageButton = (ImageButton) findViewById(R.id.activity_main_message); //消息列表
        userButton = (ImageButton) findViewById(R.id.activity_main_user);//用户
        drawerLayout = (DrawerLayout) findViewById(R.id.acitivty_main_drawerLayout);
        showVersionInfoView = (TextView) findViewById(R.id.activity_main_show_update_version);
        slidingMenuLayout = (FrameLayout) findViewById(R.id.activity_main_sliding_menu_layout);
        tellUsLayout = (LinearLayout) findViewById(R.id.activity_main_tell_us_layout);
        shareFriendLayout = (LinearLayout) findViewById(R.id.activity_main_share_friend_layout);
        showCleanCacheLayout = (LinearLayout) findViewById(R.id.activity_main_progresslayout);

        List<ImageView> listViews = new ArrayList<ImageView>();
        pointImageViews = new ImageView[images.length];
        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(this);

            imageView.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setBackgroundResource(images[i]);
            listViews.add(imageView);

            ImageView point = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 10, 0);
            point.setLayoutParams(layoutParams);
            point.setScaleType(ImageView.ScaleType.FIT_XY);
            point.setPadding(0, 0, 10, 0);
            pointImageViews[i] = point;

            if (i == 0) {
                point.setBackgroundResource(R.drawable.icon_dian_white);
            } else {
                point.setBackgroundResource(R.drawable.icon_dian_gray);
            }
            viewpagecountLayout.addView(point);
        }
        topViewPager.setAdapter(new MyPagerAdapter(listViews));
        topViewPager.setCurrentItem(nowcount);
        topViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        timerStart();  //切换图片
        String city = PublicUtil.getStorage_string(this, "mycity", "深圳");
        //获取天气信息
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Constent.URLHEAD_GET + "weather?city=" + city).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String backStr = response.body().string();
                        //解析String
                        try {
                            JSONObject jsonObject = new JSONObject(backStr);
                            if (jsonObject.getString("errcode").equals("0")) {
                                JSONArray HeWeather5Array = jsonObject.optJSONObject("data").optJSONArray("HeWeather5");
                                JSONObject wetherObject = HeWeather5Array.optJSONObject(0);
                                String wetherStatus = wetherObject.getJSONObject("now").getJSONObject("cond").getString("txt");  //天气状况
                                String tmp = wetherObject.getJSONObject("now").getString("tmp");    //温度
                                String hasSeeDistance = wetherObject.getJSONObject("now").getString("vis");    //能见度
                                Message msg = new Message();
                                Map<String, String> wetherInfo = new HashMap<String, String>();
                                wetherInfo.put("wetherStatus", wetherStatus);
                                wetherInfo.put("tmp", tmp);
                                wetherInfo.put("hasSeeDistance", hasSeeDistance);
                                msg.obj = wetherInfo;
                                msg.what = 1;
                                handler.sendMessage(msg);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    class MyDrawerListener implements DrawerLayout.DrawerListener {

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {
            if ("0".equals(islogin)) {
                userNameView.setText("");
                userPhoneView.setText("");
                userGradeView.setText("");
                userImageView.setBackgroundResource(R.drawable.lvka);
            } else {
                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_MEMBER_INFOR);
                AnsynHttpRequest.httpRequest(MainActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_MEMBER_INFOR, map,
                        false, false, true);
            }
        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        islogin = getStorage_string(this, "islogin", "0");
        if ("1".equals(islogin)) {
            phone = getStorage_string(this, "phone", "");
            if (!TextUtils.isEmpty(phone)) {
                Map<String, String> map = new HashMap<String, String>();
                map = new HashMap<String, String>();
                map.put("act", Constent.ACT_GETINDEX);
                map.put("mobile", phone);
                map.put("ver", Constent.VER);
                AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.POST, callBack, Constent.ID_GETINDEX, map,
                        false, false, true);
            }

        }

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }


    /**
     * 检查版本更新
     */
    private void chechUpdate() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_CHECKUPDATE);
        map.put("version", Constent.VER);
        AnsynHttpRequest.httpRequest(MainActivity.this, AnsynHttpRequest.POST, callBack, Constent.ID_CHECKUPDATE, map,
                false, false, true);
    }

    private long nowtime = -1;
    private boolean isfirst = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isfirst) {
                isfirst = false;
                nowtime = System.currentTimeMillis();
                PublicUtil.showToast(MainActivity.this, "再按一次退出程序", false);
            } else {
                if ((System.currentTimeMillis() - nowtime) < 3000) {
                    ((MyApplication) getApplication()).removeAllActivitys();
                    System.exit(1);

                } else {
                    isfirst = true;
                }
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


    private JSONObject updatejsonObject = null, cityJsonObject, jsonObject;

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {

                case Constent.ID_CHECKUPDATE:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                updatejsonObject = new JSONObject(backstr);
                                if (handler != null) {
                                    handler.sendEmptyMessage(httprequestsuccess_checkupdate);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    }
                    break;

                case Constent.ID_REGION_GET_ID:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                cityJsonObject = new JSONObject(backstr);
                                if (handler != null) {
                                    handler.sendEmptyMessage(httprequestsuccess_cityname);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    }
                    break;
                case Constent.ID_GETINDEX:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                if (handler != null) {
                                    handler.sendEmptyMessage(success_httprequest_getindex);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    }
                    break;
                case Constent.ID_MEMBER_INFOR:
                    if (isRequestSuccess) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!isString) {
                                    String backstr = null;
                                    try {
                                        backstr = jsonArray.getString(1);
                                        jsonObject = new JSONObject(backstr);
                                        if (jsonObject != null && jsonObject.getString("errcode").equals("0")) {
                                            JSONObject dataJsonObject = jsonObject.optJSONObject("data");
                                            if (dataJsonObject != null) {
                                                String client = dataJsonObject.getString("client");
                                                String mobile = dataJsonObject.getString("mobile");
                                                userNameView.setText(client);
                                                userPhoneView.setText(mobile);
                                                int score = Integer.parseInt(dataJsonObject.getString("score"));
                                                if (score <= 100) {  //绿卡
                                                    userGradeView.setText("绿卡会员");
                                                    userImageView.setBackgroundResource(R.drawable.lvka);
                                                } else if (100 < score && score <= 200) {  //银卡
                                                    userGradeView.setText("银卡会员");
                                                    userImageView.setBackgroundResource(R.drawable.yinka);
                                                } else if (200 < score && score <= 300) {  //金卡
                                                    userGradeView.setText("金卡会员");
                                                    userImageView.setBackgroundResource(R.drawable.jinka);
                                                } else if (300 < score) {  //钻石
                                                    userGradeView.setText("钻石会员");
                                                    userImageView.setBackgroundResource(R.drawable.zhuanshi);
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });

                    }
                    break;
            }

        }
    };

    private int error_httprequest_weather = 0x8100;
    private int success_httprequest_getindex = 0x8102;
    private int chageviewpage = 0x8103;
    private int httprequestsuccess_checkupdate = 0x4102;
    private int httprequestsuccess_cityname = 0x8302;
    private int hide_clean_cacha = 0x8306;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == httprequestsuccess_checkupdate) {
                if (updatejsonObject != null) {
                    try {
                        if ("0".equals(updatejsonObject.get("error").toString())) {
                            JSONObject dataObject = updatejsonObject.optJSONObject("data");
                            dstNewVersion = dataObject.optString("version");
                            updatePath = dataObject.optString("path");
                            hasNewVersion = dataObject.optBoolean("has_new_ver");
                            updateContent = dataObject.optString("update_content");
                            if (!hasNewVersion) {
                                showVersionInfoView.setText("当前为最新版本");
                            } else {
                                showVersionInfoView.setText("可升级至:" + dstNewVersion.replace("android", ""));
                            }

                            if (dataObject != null && dataObject.getBoolean("has_new_ver")
                                    && dataObject.getString("path") != null) {
                                UpdateAppManager updateAppManager = new UpdateAppManager(MainActivity.this,
                                        dataObject.getString("version"), dataObject.getString("path"),
                                        dataObject.getString("update_content"));
                                updateAppManager.checkUpdateInfo();
                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }

                }

            } else if (msg != null && msg.what == httprequestsuccess_cityname) {
                if (cityJsonObject != null) {
                    try {

                        if ("0".equals(cityJsonObject.getString("errcode"))) {

                            JSONObject dataObject = cityJsonObject.getJSONObject("data");

                            if (dataObject != null) {
                                if (!TextUtils.isEmpty(dataObject.getString("region_id"))) {
                                    PublicUtil.setStorage_string(MainActivity.this, "city_id",
                                            dataObject.getString("region_id"));
                                }

                                if (!TextUtils.isEmpty(dataObject.getString("region_name"))) {
                                    PublicUtil.setStorage_string(MainActivity.this, "city_name",
                                            dataObject.getString("region_name"));
                                }

                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }
                }
            } else if (msg != null && msg.what == chageviewpage) {
                try {
                    nowcount++;
                    PublicUtil.logDbug(TAG, "nowcount:" + nowcount, 0);
                    topViewPager.setCurrentItem(nowcount);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            } else if (msg != null && msg.what == 1) {
                Map<String, String> wetherInfo = (Map<String, String>) msg.obj;
                weatherView.setSelected(true);
                tempView.setText(wetherInfo.get("wetherStatus") + "  " + wetherInfo.get("tmp") + "℃  " + "能见度  " + wetherInfo.get("hasSeeDistance") + "KM");
//                if (Float.parseFloat(wetherInfo.get("tmp")) > 30) {
//                    weatherView.setVisibility(View.VISIBLE);
//                    weatherView.setText(getResources().getString(R.string.weather_infor_top));
//                } else if (Float.parseFloat(wetherInfo.get("tmp")) < 0) {
//                    weatherView.setVisibility(View.VISIBLE);
//                    weatherView.setText(getResources().getString(R.string.weather_infor_low));
//                } else {
//                    weatherView.setVisibility(View.VISIBLE);
//                    weatherView.setText(getResources().getString(R.string.weather_infor_low));
//                }
            } else if (msg != null && msg.what == error_httprequest_weather) {
                if (msg.obj != null) {
                    PublicUtil.showToast(MainActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_getindex) {
                try {
                    if ("0".equals(jsonObject.get("error").toString())) {

                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        if (dataObject != null) {
                            JSONObject chargeObject = new JSONObject(dataObject.get("charging").toString());
                            if (chargeObject.getString("pole_id") != null
                                    && !TextUtils.isEmpty(chargeObject.getString("pole_id"))) {

                                vcr_id = chargeObject.getString("vcr_id");
                                id = chargeObject.getString("pole_id");
                                measuring_point = chargeObject.getString("measuring_point");

                                if (chargeLayout.getVisibility() == View.GONE) {
                                    chargeLayout.setVisibility(View.VISIBLE);
                                }
                            } else {
                                chargeLayout.setVisibility(View.GONE);
                            }

                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            } else if (msg.what == hide_clean_cacha) {
                showCleanCacheLayout.setVisibility(View.GONE);
            }

        }
    };

    /**
     * ViewPager适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        public List<ImageView> mImageViews;

        public MyPagerAdapter(List<ImageView> mListViews) {
            this.mImageViews = mListViews;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;// 为了实现循环滚动，这里需要这样处理设置成最大，使用户看不到边界

        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            // 对ViewPager页号求模取出View列表中要显示的项
            position %= mImageViews.size();
            if (position < 0) {
                position = mImageViews.size() + position;
            }
            ImageView view = mImageViews.get(position);
            // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            return view;
        }
    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();

        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            nowcount = position;
            setPoint();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 切换广告上的点点
     */
    private void setPoint() {
        for (int i = 0; i < pointImageViews.length; i++) {
            if (i == (nowcount % images.length)) {
                pointImageViews[i].setBackgroundResource(R.drawable.icon_dian_white);
            } else {
                pointImageViews[i].setBackgroundResource(R.drawable.icon_dian_gray);
            }
        }
    }

    /**
     * 定时器开始
     */
    private void timerStart() {

        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                handler.sendEmptyMessage(chageviewpage);
            }
        }, 5 * 1000, 5 * 1000);
    }


    @Override
    public void onClick(View view) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (view.getId()) {

            case R.id.activity_main_saoma_addpower_layout:
                if ("1".equals(islogin)) {
                    intent = new Intent(this, CaptureActivity.class);
                } else {
                    PublicUtil.showToast(this, "请登录", false);
                    intent = new Intent(this, LoginActivity.class);
                }
                startActivity(intent);

                break;
            case R.id.activity_main_search_power_layout:
                intent = new Intent(this, SearchPowerActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_my_wz_layout:    //我的违章
                if ("1".equals(islogin)) {
                    intent = new Intent(this, MyWeiZhangActivity.class);
                } else {
                    PublicUtil.showToast(this, "请登录", false);
                    intent = new Intent(this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.activity_main_rentcar_order_layout:  //我的订单
                islogin = PublicUtil.getStorage_string(this, "islogin", "0");
                if ("1".equals(islogin)) {
                    //intent = new Intent(this, CarRestlistActivity.class);
                    intent = new Intent(this, NewOrderListActivity.class);
                } else {
                    PublicUtil.showToast(this, "请登录", false);
                    intent = new Intent(this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.tab_home_duanzulayout:
                intent = new Intent(this, DuanzuActivity2.class);
                startActivity(intent);
                break;
            case R.id.tab_home_chargeing:
                intent = new Intent(this, ChargeActivity.class);
                intent.putExtra("vcr_id", vcr_id);
                intent.putExtra("pole_id", id);
                intent.putExtra("measuring_point", measuring_point);
                startActivity(intent);
                break;
            case R.id.activity_main_shengqing_long_rent_layout:  //申请长租
                //intent = new Intent(this, CarChangzuActivity.class);
                intent = new Intent(this, LongRentActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_fengshi_rentcar_layout:  //分时租赁
                intent = new Intent(this, DuanzuActivity2.class);
                startActivity(intent);
                break;
            case R.id.activity_main_sliding_switch:   //侧拉
                drawerLayout.openDrawer(slidingMenuLayout);
                break;
            case R.id.activity_main_user:   //个人中心
                if (islogin.equals("1")) {
                    intent = new Intent(this, UserCenterActivity.class);
                } else {
                    intent = new Intent(this, LoginActivity.class);
                }
                startActivity(intent);
                break;
            case R.id.activity_main_message:   //消息列表
                intent = new Intent(this, NewsCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_service_detail_layout:   //服务规则
                intent = new Intent(this, FuWuGuiZeActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_common_problem_layout:   //常见问题
                intent = new Intent(this, CommonQuestionActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_return_suggestion_layout:   //意见反馈
                Suggest2Activity.intentMe(this, "0");
                break;
            case R.id.activity_main_version_update_layout:   //版本升级
                if (!TextUtils.isEmpty(dstNewVersion) && !TextUtils.isEmpty(updatePath)) {
                    UpdateAppManager updateAppManager = new UpdateAppManager(MainActivity.this,
                            dstNewVersion, updatePath,
                            updateContent);
                    updateAppManager.checkUpdateInfo();
                } else {
                    PublicUtil.showToast(this, "当前为最新版本", false);
                }
                break;
            case R.id.activity_main_clean_cache_layout:   //缓存清理
                if (showCleanCacheLayout.getVisibility() == View.GONE) {
                    showCleanCacheLayout.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageAtTime(hide_clean_cacha, SystemClock.uptimeMillis() + 4000);
                }
                break;
            case R.id.activity_main_with_us_layout:   //关于我们
                intent = new Intent(this, AboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_main_tell_us_layout:   //联系客服
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4008604558"));
                startActivity(intent);
                break;
            case R.id.activity_main_share_friend_layout:   //分享
                break;
            case R.id.activity_main_h5_test_layout:
                intent = new Intent(this, H5TestActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_test:
                intent = new Intent(this, TestActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void parseIntent() {
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            int what = intent.getIntExtra("what", -1);
            switch (what) {
                case WHAT_INTENT_TO_ORDER_LIST:
                    Intent i = new Intent(this, NewOrderListActivity.class);
                    startActivity(i);
                    break;
                default:
            }
            return;
        }
    }

    private void execGetAccountInfo() {

        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/member/info")
                .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                .build();
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ULog.e(TAG, response.toString());
                ResAccount res = BeanParser.parseAccount(response);
                if(res.mCode == BaseRes.RESULT_OK) {
                    UserDataManager.getInstance().setAccount(res.mAccount);
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));


    }


    public static void intentToOrderListActivity(Activity act) {
        Intent intent = new Intent(act, MainActivity.class);
        intent.putExtra("what", WHAT_INTENT_TO_ORDER_LIST);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        act.startActivity(intent);

    }
}
