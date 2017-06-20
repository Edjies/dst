package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.service.LocationService;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActivity extends BaseActivity {

    private final String TAG = "WelcomeActivity";
    private String phone, pwd, token;
    private ViewPager viewPager;
    private List<View> listViews = new ArrayList<View>(); // Tab页面列表
    private int imageres[] = {R.drawable.yindao1, R.drawable.yindao2,
            R.drawable.yindao3};
    private FrameLayout welcomeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeLayout = (FrameLayout) findViewById(R.id.welcome_layout);
        //startAnima(welcomeLayout);
        addActivity(this);
        viewPager = (ViewPager) findViewById(R.id.welcome_images_vPager);
        PublicUtil.removeStorage_string(WelcomeActivity.this, "islogin");
        PublicUtil.removeStorage_string(WelcomeActivity.this, "user_danwei");
        PublicUtil.removeStorage_string(WelcomeActivity.this, "user_nicheng");
        PublicUtil.removeStorage_string(WelcomeActivity.this, "order_no");
        PublicUtil.removeStorage_string(WelcomeActivity.this, "id_card_url");
        PublicUtil.removeStorage_string(WelcomeActivity.this, "id_card_url1");
        PublicUtil.removeStorage_string(WelcomeActivity.this,
                "driving_card_url");
        PublicUtil.removeStorage_string(WelcomeActivity.this,
                "business_license_url");
        PublicUtil.removeStorage_string(WelcomeActivity.this, "loginkey2");
        PublicUtil.removeStorage_string(WelcomeActivity.this, "userid");
    }

    private void startAnima(View welcomeLayout) {
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnima = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnima.setDuration(700l);
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(700l);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(700l);
        set.addAnimation(alphaAnimation);
        set.addAnimation(scaleAnima);
        set.addAnimation(rotateAnimation);
        welcomeLayout.startAnimation(set);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();

        setVersion(WelcomeActivity.this);
        Intent intent = new Intent(WelcomeActivity.this, LocationService.class);
        startService(intent);

        phone = PublicUtil
                .getStorage_string(WelcomeActivity.this, "phone", "0");
        pwd = PublicUtil.getStorage_string(WelcomeActivity.this, "pwd", "0");
        if (!"0".equals(phone) && !"0".equals(pwd)) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("act", Constent.ACT_LOGIN_INDEX);
            map.put("mobile", phone);
            map.put("pwd", pwd);

            AnsynHttpRequest.httpRequest(WelcomeActivity.this,
                    AnsynHttpRequest.GET, callBack, Constent.ID_LOGIN_INDEX,
                    map, false, false, true);
        }
        if (!"1".equals(PublicUtil.getStorage_string(WelcomeActivity.this,
                "isfirst", "0"))) {

            PublicUtil.setStorage_string(WelcomeActivity.this, "isfirst", "1");

            for (int i = 0; i < imageres.length; i++) {
                TextView textView = new TextView(this);

                textView.setLayoutParams(new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setBackgroundResource(imageres[i]);
                if (i == (imageres.length - 1)) {
                    textView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(WelcomeActivity.this,
                                    MainActivity.class);
                            WelcomeActivity.this.startActivity(intent);
                            WelcomeActivity.this.finish();
                        }
                    });
                }
                listViews.add(textView);
            }

            viewPager.setAdapter(new MyPagerAdapter(listViews));
            viewPager.setCurrentItem(0);
            viewPager.setVisibility(View.VISIBLE);
        } else {
            handler.sendEmptyMessageAtTime(start,
                    SystemClock.uptimeMillis() + 3 * 1000);
        }
        MobclickAgent.onResume(this);// 友盟统计开始
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);// 友盟统计结束
    }

    /**
     * 获取并设置版本号
     *
     * @param context
     */
    public void setVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            @SuppressWarnings("static-access")
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),
                    manager.GET_CONFIGURATIONS);
            String ver = info.versionName;
            if (ver != null) {
                Constent.VERSON = ver;
                Constent.VER = "android" + ver;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private JSONObject jsonObject = null;

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_LOGIN_INDEX:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                if (handler != null) {
                                    handler.sendEmptyMessage(httprequestsuccess);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = httprequesterror;
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

    private int httprequesterror = 0x5200;
    private int httprequestsuccess = 0x5201;
    private int start = 0x1009;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == start) {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                //				if (!TextUtils.isEmpty(getIntent().getStringExtra("tick"))) {
                //					intent.putExtra("tick", getIntent().getStringExtra("tick"));
                //					intent.putExtra("order_no",
                //							getIntent().getStringExtra("order_no"));
                //					intent.putExtra("wz_foregift_state", getIntent()
                //							.getStringExtra("wz_foregift_state"));
                //				}
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();
            }

            if (msg != null && msg.what == httprequesterror) {
                if (msg.obj != null) {
                    PublicUtil.showToast(WelcomeActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                if (jsonObject != null) {

                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            PublicUtil.setStorage_string(WelcomeActivity.this,
                                    "islogin", "1");

                            PublicUtil.setStorage_string(WelcomeActivity.this,
                                    "phone", phone);
                            PublicUtil.setStorage_string(WelcomeActivity.this,
                                    "pwd", pwd);
                            PublicUtil.setStorage_string(
                                    WelcomeActivity.this,
                                    "loginkey2",
                                    jsonObject.getJSONObject("data").getString(
                                            "loginkey"));
                            AnsynHttpRequest.loginkey = jsonObject
                                    .getJSONObject("data")
                                    .getString("loginkey");

                            PublicUtil.setStorage_string(WelcomeActivity.this,
                                    "userid", jsonObject.getJSONObject("data")
                                            .getString("user_id"));
                            AnsynHttpRequest.userid = jsonObject.getJSONObject(
                                    "data").getString("user_id");

                        } else {
                            PublicUtil.removeStorage_string(
                                    WelcomeActivity.this, "islogin");

                            PublicUtil.removeStorage_string(
                                    WelcomeActivity.this, "phone");
                            PublicUtil.removeStorage_string(
                                    WelcomeActivity.this, "pwd");
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
     * ViewPager适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }

}
