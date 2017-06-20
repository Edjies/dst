package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.bean.Account;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.CarDetail;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.haofeng.apps.dst.R.drawable.bannerdotg;
import static com.haofeng.apps.dst.R.drawable.icon_dian_white;

/**
 * 车辆详情页
 *
 * @author WIN10
 */
public class CarInforActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "CarInforActivity";
    private FrameLayout topLayout;
    private TextView backView;
    private ViewPager imageViewPager;
    private TextView moneyView, moneyView2, moneyinforView;
    private TextView carnameView, cartypeView, cartypeView2, carzuoweiView, carrongjiView, carxuhangView, carchicunView;
    private TextView zuyong_moneyView, zuyong_moneyView2, zuyong_okView;
    private List<Map<String, String>> imageList = new ArrayList<Map<String, String>>();
    private Timer timer;
    private int nowcount = 0;
    private Bundle dataBundle = null;
    private boolean isAuth = false;// 是否认证通过
    private ImageView[] pointImageViews;// 管理头部滑动广告的点点
    private int number = 1; // 车型库存
    private LinearLayout pointLayout;
    private String remark;
    private float amount;
    private float foregiftAccount;
    private TextView carTypeTextView;

    private CarDetail mCar = new CarDetail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinfor);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 获取会员信息
        Map<String, String> map = new HashMap<String, String>();
        map = new HashMap<String, String>();
        map.put("act", Constent.ACT_MEMBER_INFOR);
        AnsynHttpRequest.httpRequest(CarInforActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_MEMBER_INFOR,
                map, false, false, true);
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

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    /*
     * 初始化组件
     */
    public void init() {

        topLayout = (FrameLayout) findViewById(R.id.carinfor_top_layout);
        setTopLayoutPadding(topLayout);

        backView = (TextView) findViewById(R.id.carinfor_back);
        imageViewPager = (ViewPager) findViewById(R.id.carinfor_top_images_vPager);

        moneyView = (TextView) findViewById(R.id.carinfor_money);
        moneyView2 = (TextView) findViewById(R.id.carinfor_money2);
        moneyinforView = (TextView) findViewById(R.id.carinfor_money_infor);

        carnameView = (TextView) findViewById(R.id.carinfor_infor_name);
        cartypeView = (TextView) findViewById(R.id.carinfor_infor_type);
        cartypeView2 = (TextView) findViewById(R.id.carinfor_infor_type2);
        carzuoweiView = (TextView) findViewById(R.id.carinfor_infor_zuowei);
        carrongjiView = (TextView) findViewById(R.id.carinfor_infor_rongji);
        carxuhangView = (TextView) findViewById(R.id.carinfor_infor_xuhang);
        carchicunView = (TextView) findViewById(R.id.carinfor_infor_chicun);

        zuyong_moneyView = (TextView) findViewById(R.id.carinfor_zuyong_money);
        zuyong_moneyView2 = (TextView) findViewById(R.id.carinfor_zuyong_money2);
        zuyong_okView = (TextView) findViewById(R.id.carinfor_zuyong_ok);
        pointLayout = (LinearLayout) findViewById(R.id.carinfor_top_images_point_layout);
        carTypeTextView = (TextView) findViewById(R.id.car_infor_car_type);
        backView.setOnClickListener(this);
        zuyong_okView.setOnClickListener(this);
        // 根据车辆的id获取车辆详情
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_NEW_GET_CAR_DETAIL);
        map.put("id", getIntent().getStringExtra("id"));
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_GET_NEW_CAR_DETAIL, map, false, true,
                true);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.carinfor_back:
                finish();
                break;
            case R.id.carinfor_zuyong_ok:
                Intent intent = null;
                if ("1".equals(PublicUtil.getStorage_string(CarInforActivity.this, "islogin", "0"))) {
                    if (dataBundle == null) {
                        PublicUtil.showToast(CarInforActivity.this, "车辆相关数据读取失败，无法进行下一步操作", false);
                    } else {
                        // TODO hubble
                        //isAuth = true;
                        if (isAuth) { // 已经身份认证
                            //判断押金
                            if (foregiftAccount >= 0) {
                                //判断库存
                                if (number > 0) {  //有库存
                                    //有无已付款未还车订单
                                    if ("WAITTING_BACK_CAR".equals(remark)) {
                                        PublicUtil.showToast(this, "您当前有未还车车辆，请还车后继续租车", false);
                                        return;
                                    } else {
                                        intent = new Intent(this, RentCarActivity.class); // 跳转到车辆详情
                                        intent.putExtras(dataBundle);
                                        intent.putExtra("car", mCar);
                                        startActivity(intent);
                                    }
                                } else {
                                    PublicUtil.showToast(this, "该车型库存不足,我们正在积极备车中", false);
                                    return;
                                }
                            } else {
                                PublicUtil.showToast(this, "您的押金存在" + (-foregiftAccount) + "元欠款，欠款未缴纳的欠款下您无法继续租车", false);
                                zuyong_okView.setText("立即缴纳");
                                zuyong_okView.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // TODO Auto-generated method stub
                                        Intent intent = new Intent(CarInforActivity.this, RechargeActivity.class);
                                        intent.putExtra("infor", "租车押金充值");
                                        intent.putExtra("infor2", "地上铁App租车押金充值");
                                        intent.putExtra("type", "rent_car_yajin");
                                        intent.putExtra("yajinNeed", ((double) -foregiftAccount));
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        } else {
                            showInforDialog();
                        }
                    }
                } else {
                    PublicUtil.showToast(CarInforActivity.this, "请登录", false);
                    intent = new Intent(CarInforActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }

    }

    private JSONObject jsonObject = null, memberObject;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_GET_NEW_CAR_DETAIL: // 车辆详情
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_http_getcardetail);
                            } catch (JSONException e) {
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
                case Constent.ID_MEMBER_INFOR: // 会员信息
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                memberObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_http_memberinfor);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_memberinfor;
                        message.obj = data;
                        handler.sendMessage(message);
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private int error_http_getcardetail = 0x5700;
    private int success_http_getcardetail = 0x5701;
    private int chageviewpage = 0x5702;
    private int error_http_memberinfor = 0x5705;
    private int success_http_memberinfor = 0x5706;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            if (msg != null && msg.what == chageviewpage) {
                try {
                    nowcount++;
                    imageViewPager.setCurrentItem(nowcount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg != null && msg.what == error_http_getcardetail) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarInforActivity.this, msg.obj.toString(), false);
                }
            } else if (msg != null && msg.what == success_http_getcardetail) {
                if (jsonObject != null) {
                    try {
                        mCar = BeanParser.parseCarDetail(jsonObject).mCar;
                        if ("0".equals(jsonObject.getString("error"))) {
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            if (dataObject != null) {
                                moneyView.setText("￥" + dataObject.getString("time_price") + "/时");
                                moneyView2.setText("￥" + dataObject.getString("day_price") + "/天");

                                zuyong_moneyView.setText("￥" + dataObject.getString("time_price"));
                                zuyong_moneyView2.setText("￥" + dataObject.getString("day_price"));

                                moneyinforView.setText("里程费用:起步价" + dataObject.getString("starting_price") + "元("
                                        + "???" + "公里内)+"
                                        + "???" + "元/公里");
                                carnameView.setText(dataObject.getString("brand_name") + dataObject.getString("car_model_name"));
                                carTypeTextView.setText("车辆类型:" + dataObject.getString("car_type_name"));

                                cartypeView2.setText("载重量:" + dataObject.getString("check_mass") + "kg");
                                carzuoweiView.setText("轴距:" + dataObject.getString("shaft_distance") + "mm");
                                carrongjiView.setText("货箱容积:" + dataObject.getString("cubage") + "m³");
                                carxuhangView.setText("续航:" + dataObject.getString("endurance_mileage") + "km");
                                carchicunView.setText("车身尺寸:" + dataObject.getString("outside_long")
                                        + "*" + dataObject.getString("outside_width") + "*" + dataObject.getString("outside_height") + "mm");
                                String imags = dataObject.optString("imgs");
                                if (imags != null && !imags.equals("")) {
                                    if (imags.contains(",")) {
                                        String[] imagUrl = imags.split(",");
                                        for (int i = 0; i < imagUrl.length; i++) {
                                            Map<String, String> map = new HashMap<String, String>();
                                            map.put("image_path", Constent.URLHEAD_NEW_IMAGE +imagUrl[i]);
                                            imageList.add(map);
                                        }
                                    } else {
                                        Map<String, String> map = new HashMap<String, String>();
                                        map.put("image_path", Constent.URLHEAD_NEW_IMAGE+imags);
                                        imageList.add(map);
                                    }
                                    if (imageList.size() > 0) {
                                        // 车辆图片的轮播
                                        setAdv(imageList);
                                    }

                                }
                                //
                                //                                // 获取服务器传送过来的车辆图片
                                //                                if (imageArray != null && imageArray.length() > 0) {
                                //                                    for (int i = 0; i < imageArray.length(); i++) {
                                //                                        Map<String, String> map = new HashMap<String, String>();
                                //                                        map.put("image_path", Constent.URLHEAD_IMAGE + imageArray.getString(i));
                                //                                        imageList.add(map);
                                //                                    }
                                //
                                //                                } else {
                                //                                    Map<String, String> map = new HashMap<String, String>();
                                //                                    map.put("image_path", Constent.URLHEAD_IMAGE + dataObject.getString("image"));
                                //                                    imageList.add(map);
                                //                                }
                                //
                                // 保存服务器返回的车辆详情的信息
                                dataBundle = new Bundle();
                                dataBundle.putString("car_yajin", String.valueOf(Double.parseDouble(dataObject.getString("wz_deposit"))
                                        + Double.parseDouble(dataObject.getString("deposit"))));  //车辆押金
                                dataBundle.putString("insurance_expense", dataObject.getString("insurance_expense"));  //保险费用
                                dataBundle.putString("insurance_bjmp", dataObject.getString("insurance_bjmp"));  //不计免赔
                                dataBundle.putString("time_price", dataObject.getString("time_price"));  //时租
                                dataBundle.putString("day_price", dataObject.getString("day_price"));  //日租
                                dataBundle.putString("id", dataObject.getString("id")); // id

                            }

                        } else if ("1002".equals(jsonObject.getString("error"))) {
                            PublicUtil.showToast(CarInforActivity.this, jsonObject.getString("msg"), false);
                            Intent intent = new Intent(CarInforActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else if ("1051".equals(jsonObject.getString("error"))) {  //库存不足
                            number = 0;
                        } else if ("1100".equals(jsonObject.getString("error"))) {  //未还车订单/待提车订单
                            remark = "WAITTING_BACK_CAR";
                        } else {
                            PublicUtil.showToast(CarInforActivity.this, jsonObject.getString("msg"), false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }

            if (msg != null && msg.what == error_http_memberinfor) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarInforActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_memberinfor) {
                if (memberObject != null) {
                    // 检测企业或者个人是否认证
                    try {
                        if ("0".equals(memberObject.getString("errcode"))) {
                            UserDataManager.getInstance().setAccount(new Account().parse(memberObject.optJSONObject("data")));
                            JSONObject dataJsonObject = memberObject.getJSONObject("data");
                            if (dataJsonObject != null) {
                                amount = PublicUtil.toTwo(Float.parseFloat(dataJsonObject.getString("foregift_acount")));
                                if ("1".equals(dataJsonObject.getString("category"))) {// 个人

                                    String id_auth = dataJsonObject.getString("id_card_auth"); // 身份认证
                                    String drive_auth = dataJsonObject.getString("driving_card_auth"); // 驾驶证认证

                                    if ("1".equals(id_auth) && "1".equals(drive_auth)) {
                                        isAuth = true;
                                    } else {
                                        isAuth = false;
                                    }

                                } else if ("2".equals(dataJsonObject.getString("category"))) {// 企业

                                    String id_auth = dataJsonObject.getString("id_card_auth");
                                    String business_auth = dataJsonObject.getString("business_license_auth");

                                    if ("1".equals(id_auth) && "1".equals(business_auth)) {
                                        isAuth = true;
                                    } else {
                                        isAuth = false;
                                    }
                                } else {
                                    isAuth = false;
                                }

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ;
    };
    private Double freeze;
    private TextView carTypeView3;

    /**
     * 汽车图片设置
     */
    private void setAdv(List<Map<String, String>> imageList) {
        List<ImageView> listViews = new ArrayList<ImageView>(); // Tab页面列表
        pointImageViews = new ImageView[imageList.size()];
        for (int i = 0; i < imageList.size(); i++) {

            ImageView imageView = new ImageView(this);

            imageView.setLayoutParams(
                    new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(0, 0, 0, 0);
            imageView.setBackgroundColor(getResources().getColor(R.color.user_gray));
            ImageLoader.getInstance().displayImage(imageList.get(i).get("image_path"), imageView);

            listViews.add(imageView);

            ImageView point = new ImageView(CarInforActivity.this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 10, 0);
            point.setLayoutParams(layoutParams);
            point.setScaleType(ImageView.ScaleType.FIT_XY);
            point.setPadding(0, 0, 10, 0);
            if (i == 0) {
                point.setBackgroundResource(bannerdotg);
            } else {
                point.setBackgroundResource(icon_dian_white);
            }

            pointLayout.addView(point);
            pointImageViews[i] = point;

        }

        imageViewPager.setAdapter(new MyPagerAdapter(listViews));
        imageViewPager.setCurrentItem(nowcount);
        imageViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        if (imageList.size() > 1) {
            timerStart();
        }

    }

    /**
     * ViewPager适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        public List<ImageView> mListViews;

        public MyPagerAdapter(List<ImageView> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {

            return Integer.MAX_VALUE;// 为了实现循环滚动，这里需要这样处理设置成最大，使用户看不到边界

            // return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub

            // Warning：不要在这里调用removeView
            // super.destroyItem(container, position, object);
            // ((ViewPager) container).removeView(mListViews.get(position
            // % mListViews.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            // 对ViewPager页号求模取出View列表中要显示的项
            position %= mListViews.size();
            if (position < 0) {
                position = mListViews.size() + position;
            }
            ImageView view = mListViews.get(position);
            // 如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            // add listeners here if necessary
            return view;

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

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            nowcount = arg0;
            setPoint();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 切换广告上的点点
     */
    private void setPoint() {

        for (int i = 0; i < pointImageViews.length; i++) {

            if (i == (nowcount % imageList.size())) {
                pointImageViews[i].setBackgroundResource(R.drawable.bannerdotg);
            } else {
                pointImageViews[i].setBackgroundResource(R.drawable.icon_dian_white);
            }
        }
    }

    /**
     * 定时器开始
     */
    private void timerStart() {

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                handler.sendEmptyMessage(chageviewpage);
            }
        }, 6 * 1000, 6 * 1000);
    }

    private void showInforDialog() {
        View view = LayoutInflater.from(CarInforActivity.this).inflate(R.layout.view_inforshow_dialog, null);

        TextView ok = (TextView) view.findViewById(R.id.view_inforshow_dialog_phoneing);
        TextView cancel = (TextView) view.findViewById(R.id.view_inforshow_dialog_cancel);
        TextView infor = (TextView) view.findViewById(R.id.view_inforshow_dialog_infor);
        ok.setText(getResources().getString(R.string.ok));
        cancel.setText(getResources().getString(R.string.quxiao));
        infor.setText(getResources().getString(R.string.renzhenginfor));

        final AlertDialog alertDialog = new AlertDialog.Builder(CarInforActivity.this, R.style.dialog_nostroke).show();
        alertDialog.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                Intent intent = new Intent(CarInforActivity.this, HuiYuanRZActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }

            }
        });
    }

}
