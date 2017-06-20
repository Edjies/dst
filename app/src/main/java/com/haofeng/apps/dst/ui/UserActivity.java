package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.UserListAdapter;
import com.haofeng.apps.dst.broadcastreceiver.MessageReceiver;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.view.MyScrollView;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户中心
 */
public class UserActivity extends BaseActivity implements View.OnClickListener{
    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private ImageView mIvMessage;
    private TextView newsnumberView;
    private View view;
    private TextView toplineView;
    private MyScrollView scrollView;
    private HorizontalScrollView topHorizontalScrollView;
    private TextView qianbaoView, jifenView, dingdanView;
    private LinearLayout qianbaoLayout, jifenLayout, dingdanLayout;
    private ListView listView, listView2, listView3, listView4;


    private TextView nameView;
    private TextView lineView, lineView2, lineView3, lineView4;
    private TextView dengjiView, dengjiView2, dengjiView3, dengjiView4;
    private TextView bigdengjiView, bigdengjiView2, bigdengjiView3, bigdengjiView4;
    private ImageView userView, userView2, userView3, userView4;
    private String[] title = {"充电记录", "收藏电站"};
    private int[] image = {R.drawable.chongdianjilu, R.drawable.shoucangdianzhuang};
    private String[] title2 = {"我的车辆", "违章查询", "我的违章"};
    private int[] image2 = {R.drawable.wodecar, R.drawable.weizhangchaxun, R.drawable.wodeweizhang};
    private String[] title3 = {"个人资料", "会员认证", "积分兑换"};
    private int[] image3 = {R.drawable.wodeziliao, R.drawable.huiyuanrenzheng, R.drawable.jifenduihuan};

    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
    private UserListAdapter listAdapter, listAdapter2, listAdapter3, listAdapter4;
    private String notice_time = "0";
    private String associationCarNumber = ""; // 关联的车牌号
    private static final String TAG = "UserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        view = View.inflate(this, R.layout.tab_user_view, null);
        setContentView(view);
        FrameLayout topLayout = (FrameLayout) view.findViewById(R.id.tab_user_toplayout);
//        setTopLayoutPadding(topLayout);
        // 获取车辆关联
//        Map<String, String> value = new HashMap<String, String>();
//        value.put("act", Constent.ACT_MY_CAR_LIST);
//        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_MY_CAR_LIST, value,
//                false, false, true);
//        setTopLayoutPadding(topLayout);
        init();
    }

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
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
        }

        else if(R.id.iv_message == id) {
            if (!"0".equals(notice_time) && !TextUtils.isEmpty(notice_time)) {
                PublicUtil.setStorage_string(UserActivity.this, "notice_time", notice_time);
            }
            newsnumberView.setVisibility(View.GONE);
            Intent intent = new Intent(UserActivity.this, NewsCenterActivity.class);
            startActivity(intent);
        }
    }

    private void init() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(this);
        mIvMessage = (ImageView) findViewById(R.id.iv_message);
        mIvMessage.setOnClickListener(this);
        toplineView = (TextView) view.findViewById(R.id.tab_user_top_line);
        scrollView = (MyScrollView) view.findViewById(R.id.tab_user_ScrollView);

        topHorizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.tab_user_horizontalScrollView);

        qianbaoView = (TextView) view.findViewById(R.id.tab_user_qianbao);
        jifenView = (TextView) view.findViewById(R.id.tab_user_jifen);
        dingdanView = (TextView) view.findViewById(R.id.tab_user_dingdan);
        listView = (ListView) view.findViewById(R.id.tab_user_listview);
        listView2 = (ListView) view.findViewById(R.id.tab_user_listview2);
        listView3 = (ListView) view.findViewById(R.id.tab_user_listview3);

        newsnumberView = (TextView) view.findViewById(R.id.tab_user_news_number);

        nameView = (TextView) view.findViewById(R.id.tab_user_username);
        lineView = (TextView) view.findViewById(R.id.tab_user_dengji_line);
        lineView2 = (TextView) view.findViewById(R.id.tab_user_dengji_line2);
        lineView3 = (TextView) view.findViewById(R.id.tab_user_dengji_line3);
        lineView4 = (TextView) view.findViewById(R.id.tab_user_dengji_line4);
        dengjiView = (TextView) view.findViewById(R.id.tab_user_dengji_image);
        dengjiView2 = (TextView) view.findViewById(R.id.tab_user_dengji_image2);
        dengjiView3 = (TextView) view.findViewById(R.id.tab_user_dengji_image3);
        dengjiView4 = (TextView) view.findViewById(R.id.tab_user_dengji_image4);
        bigdengjiView = (TextView) view.findViewById(R.id.tab_user_dengji_bigimage);
        bigdengjiView2 = (TextView) view.findViewById(R.id.tab_user_dengji_bigimage2);
        bigdengjiView3 = (TextView) view.findViewById(R.id.tab_user_dengji_bigimage3);
        bigdengjiView4 = (TextView) view.findViewById(R.id.tab_user_dengji_bigimage4);
        userView = (ImageView) view.findViewById(R.id.tab_user_dengji_userimage);
        userView2 = (ImageView) view.findViewById(R.id.tab_user_dengji_userimage2);
        userView3 = (ImageView) view.findViewById(R.id.tab_user_dengji_userimage3);
        userView4 = (ImageView) view.findViewById(R.id.tab_user_dengji_userimage4);

        qianbaoLayout = (LinearLayout) view.findViewById(R.id.tab_user_qianbao_layout);
        jifenLayout = (LinearLayout) view.findViewById(R.id.tab_user_jifen_layout);
        dingdanLayout = (LinearLayout) view.findViewById(R.id.tab_user_dingdan_layout);
        initdengji();

        qianbaoLayout.setOnClickListener(onClickListener);
        jifenLayout.setOnClickListener(onClickListener);
        dingdanLayout.setOnClickListener(onClickListener);
        userView.setOnClickListener(onClickListener);
        userView2.setOnClickListener(onClickListener);
        userView3.setOnClickListener(onClickListener);
        userView4.setOnClickListener(onClickListener);


        for (int i = 0; i < title.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("i", i);
            map.put("image", image[i]);
            map.put("title", title[i]);
            map.put("inforwithimage", "-1");
            map.put("infor", "-1");
            list.add(map);

        }
        for (int i = 0; i < title2.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("i", i);
            map.put("image", image2[i]);
            map.put("title", title2[i]);
            map.put("inforwithimage", "-1");

            if (!"".equals(associationCarNumber) && i == 0) {
                // 有车牌号
                map.put("association_car_no", associationCarNumber);
                map.put("infor", "7");
            } else {
                map.put("infor", "-1");
            }
            list2.add(map);

        }
        for (int i = 0; i < title3.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("i", i);
            map.put("image", image3[i]);
            map.put("title", title3[i]);
            map.put("inforwithimage", "-1");
            map.put("infor", "-1");
            list3.add(map);
        }

        listAdapter = new UserListAdapter(this);
        listAdapter.setData(list);
        listView.setAdapter(listAdapter);

        listAdapter2 = new UserListAdapter(this);
        listAdapter2.setData(list2);
        listView2.setAdapter(listAdapter2);

        listAdapter3 = new UserListAdapter(this);
        listAdapter3.setData(list3);
        listView3.setAdapter(listAdapter3);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (arg2 == 0) {
                    Intent intent = new Intent(UserActivity.this, ChargeListActivity.class);
                    startActivity(intent);
                } else if (arg2 == 1) {
                    Intent intent = new Intent(UserActivity.this, CollectionActivity.class);
                    startActivity(intent);
                }
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (arg2 == 0) {
                    Intent intent = new Intent(UserActivity.this, MyCarAssociationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (arg2 == 1) {
                    Intent intent = new Intent(UserActivity.this, WeizhangChaxunActivity.class);
                    startActivity(intent);
                } else if (arg2 == 2) {
                    Intent intent = new Intent(UserActivity.this, MyWeiZhangActivity.class);
                    startActivity(intent);
                }
            }
        });

        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (arg2 == 0) {
                    Intent intent = new Intent(UserActivity.this, UserInforActivity.class);
                    intent.putExtra("name", nameView.getText().toString());
                    startActivity(intent);
                } else if (arg2 == 1) {
                    Intent intent = new Intent(UserActivity.this, HuiYuanRZActivity.class);
                    intent.putExtra("from", "user");
                    startActivity(intent);
                } else if (arg2 == 2) {
                    PublicUtil.showToast(UserActivity.this, "该功能暂未开启，敬请期待", false);
                }
            }
        });
    }

    public void showdengji(int i) {
        initdengji();
        switch (i) {
            case 1:// 绿卡
                lineView.setVisibility(View.VISIBLE);
                dengjiView.setVisibility(View.GONE);
                bigdengjiView.setVisibility(View.VISIBLE);
                bigdengjiView.setBackgroundResource(R.drawable.vip_2);
                userView.setVisibility(View.VISIBLE);
                userView.setImageResource(R.drawable.lvka);
                break;
            case 2:// 银卡
                lineView.setVisibility(View.VISIBLE);
                lineView2.setVisibility(View.VISIBLE);

                dengjiView.setBackgroundResource(R.drawable.vip_1);

                dengjiView2.setVisibility(View.GONE);
                bigdengjiView2.setVisibility(View.VISIBLE);
                bigdengjiView2.setBackgroundResource(R.drawable.vip_2);

                userView2.setVisibility(View.VISIBLE);
                userView2.setImageResource(R.drawable.yinka);

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        topHorizontalScrollView.scrollTo(450, 0);
                    }
                });
                break;
            case 3:// 金卡
                lineView.setVisibility(View.VISIBLE);
                lineView2.setVisibility(View.VISIBLE);
                lineView3.setVisibility(View.VISIBLE);
                dengjiView.setBackgroundResource(R.drawable.vip_1);
                dengjiView2.setBackgroundResource(R.drawable.vip_1);

                dengjiView3.setVisibility(View.GONE);
                bigdengjiView3.setVisibility(View.VISIBLE);
                bigdengjiView3.setBackgroundResource(R.drawable.vip_2);
                userView3.setVisibility(View.VISIBLE);
                userView3.setImageResource(R.drawable.jinka);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        topHorizontalScrollView.scrollTo(650, 0);
                    }
                });
                break;
            case 4:// 钻石
                lineView.setVisibility(View.VISIBLE);
                lineView2.setVisibility(View.VISIBLE);
                lineView3.setVisibility(View.VISIBLE);
                lineView4.setVisibility(View.VISIBLE);
                dengjiView.setBackgroundResource(R.drawable.vip_1);
                dengjiView2.setBackgroundResource(R.drawable.vip_1);
                dengjiView3.setBackgroundResource(R.drawable.vip_1);

                dengjiView4.setVisibility(View.GONE);
                bigdengjiView4.setVisibility(View.VISIBLE);
                bigdengjiView4.setBackgroundResource(R.drawable.vip_2);
                userView4.setVisibility(View.VISIBLE);
                userView4.setImageResource(R.drawable.zhuanshi);

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        topHorizontalScrollView.scrollTo(850, 0);
                    }
                });

                break;

            default:
                break;
        }

    }

    /**
     * 初始化等级图片
     */
    private void initdengji() {
        lineView.setVisibility(View.GONE);
        lineView2.setVisibility(View.GONE);
        lineView3.setVisibility(View.GONE);
        lineView4.setVisibility(View.GONE);

        dengjiView.setVisibility(View.VISIBLE);
        dengjiView2.setVisibility(View.VISIBLE);
        dengjiView3.setVisibility(View.VISIBLE);
        dengjiView4.setVisibility(View.VISIBLE);
        dengjiView.setBackgroundResource(R.drawable.vip_3);
        dengjiView2.setBackgroundResource(R.drawable.vip_3);
        dengjiView3.setBackgroundResource(R.drawable.vip_3);
        dengjiView4.setBackgroundResource(R.drawable.vip_3);

        bigdengjiView.setVisibility(View.GONE);
        bigdengjiView2.setVisibility(View.GONE);
        bigdengjiView3.setVisibility(View.GONE);
        bigdengjiView4.setVisibility(View.GONE);
        userView4.setVisibility(View.GONE);
        userView3.setVisibility(View.GONE);
        userView2.setVisibility(View.GONE);
        userView.setVisibility(View.GONE);
    }

    private JSONObject inforJsonObject = null, noticeJsonObject, mycarJsonObject, mynewcarJsonObject;

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_MEMBER_INFOR:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                inforJsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_userinfor);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_userinfor;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;

                case Constent.ID_MEMBER_LATEST_NOTICE:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                noticeJsonObject = new JSONObject(backstr);
                                if (handler != null) {
                                    handler.sendEmptyMessage(success_httprequest_last_notice);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_last_notice;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case Constent.ID_ACT_MY_CAR_LIST:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                mycarJsonObject = new JSONObject(backstr);

                                if (handler != null) {
                                    handler.sendEmptyMessage(success_httprequest_get_car);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_get_car;
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

    private int error_httprequest_userinfor = 0x6000;
    private int success_httprequest_userinfor = 0x6001;
    private int error_httprequest_last_notice = 0x9500;
    private int success_httprequest_last_notice = 0x9501;
    private int success_httprequest_get_car = 0x9502;
    private int error_httprequest_get_car = 0x9503;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg != null && msg.what == Constent.PUSHHANLDER_CODE) {
                newsnumberView.setVisibility(View.VISIBLE);
            }

            if (msg != null && msg.what == error_httprequest_last_notice) {
                if (msg.obj != null) {
                    PublicUtil.showToast(UserActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_last_notice) {
                if (noticeJsonObject != null) {

                    try {

                        if ("0".equals(noticeJsonObject.getString("errcode"))) {

                            JSONObject dataJsonObject = noticeJsonObject.getJSONObject("data");
                            if (dataJsonObject != null) {
                                notice_time = dataJsonObject.getString("time");
                                PublicUtil.logDbug(TAG, notice_time, 0);

                                if (!TextUtils.isEmpty(notice_time) && !"0".equals(notice_time)) {

                                    if (!notice_time
                                            .equals(PublicUtil.getStorage_string(UserActivity.this, "notice_time", "-1"))) {
                                        newsnumberView.setVisibility(View.VISIBLE);

                                    } else {
                                        newsnumberView.setVisibility(View.GONE);
                                    }

                                }

                            }

                        } else if ("1002".equals(noticeJsonObject.getString("errcode"))) {
                            PublicUtil.showToast(UserActivity.this, noticeJsonObject.getString("msg"), false);
                            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            PublicUtil.showToast(UserActivity.this, noticeJsonObject.getString("msg"), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }

                }

            }
            if (msg != null && msg.what == error_httprequest_get_car) {
                if (msg.obj != null) {
                    PublicUtil.showToast(UserActivity.this, msg.obj.toString(), false);
                }
            } else if (msg != null && msg.what == success_httprequest_get_car) {
                if (mycarJsonObject != null) {
                    try {
                        if ("0".equals(mycarJsonObject.getString("errcode"))) {
                            if (mycarJsonObject.optJSONObject("data") != null) {
                                mynewcarJsonObject = mycarJsonObject.optJSONObject("data").optJSONObject("new");
                                if (mynewcarJsonObject != null) {
                                    associationCarNumber = mynewcarJsonObject.getString("car_no");
                                }
                            }
                        }


                        init();
                        MessageReceiver.sethandler(handler);
                        String islogin = PublicUtil.getStorage_string(UserActivity.this, "islogin", "0");
                        if ("1".equals(islogin)) {
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("act", Constent.ACT_MEMBER_INFOR);
                            AnsynHttpRequest.httpRequest(UserActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_MEMBER_INFOR, map,
                                    false, false, true);
                            // 获取最后一个通知时间
                            map = new HashMap<String, String>();
                            map.put("act", Constent.ACT_MEMBER_LATEST_NOTICE);
                            AnsynHttpRequest.httpRequest(UserActivity.this, AnsynHttpRequest.GET, callBack,
                                    Constent.ID_MEMBER_LATEST_NOTICE, map, false, false, true);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }

                }

            }

            if (msg != null && msg.what == error_httprequest_userinfor) {
                if (msg.obj != null) {
                    PublicUtil.showToast(UserActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_userinfor) {

                if (inforJsonObject != null) {
                    try {
                        if ("0".equals(inforJsonObject.getString("errcode"))) {
                            JSONObject dataJsonObject = inforJsonObject.getJSONObject("data");
                            if (dataJsonObject != null) {
                                if (!TextUtils.isEmpty(dataJsonObject.getString("name"))
                                        && !"null".equals(dataJsonObject.getString("name"))) {
                                    String userName = dataJsonObject.getString("name");
                                    nameView.setText(userName);
                                    PublicUtil.setStorage_string(UserActivity.this, "name", userName);
                                }
                                Double totalAccount = Double.parseDouble(dataJsonObject.getString("amount"))
                                        + Double.parseDouble(dataJsonObject.getString("foregift_acount"));
                                qianbaoView.setText(String.valueOf(PublicUtil.roundByScale(totalAccount, 2)));
                                jifenView.setText(dataJsonObject.getString("score"));
                                dingdanView.setText(dataJsonObject.getString("ongoing_count"));

                                if (!TextUtils.isEmpty(dataJsonObject.getString("nickname"))
                                        && !"null".equals(dataJsonObject.getString("nickname"))) {
                                    PublicUtil.setStorage_string(UserActivity.this, "user_nicheng",
                                            dataJsonObject.getString("nickname"));
                                }

                                if (!TextUtils.isEmpty(dataJsonObject.getString("work_units"))
                                        && !"null".equals(dataJsonObject.getString("work_units"))) {
                                    PublicUtil.setStorage_string(UserActivity.this, "user_danwei",
                                            dataJsonObject.getString("work_units"));
                                }

                                String id_auth = dataJsonObject.getString("id_card_auth");
                                String drive_auth = dataJsonObject.getString("driving_card_auth");
                                String business_auth = dataJsonObject.getString("business_license_auth");

                                if ("1".equals(dataJsonObject.getString("category"))) {// 个人
                                    Map<String, Object> map = list3.get(1);
                                    map.remove("infor");
                                    if ("1".equals(id_auth) && "1".equals(drive_auth)) {
                                        map.put("infor", id_auth);
                                    } else if ("3".equals(id_auth) && "3".equals(drive_auth)) {
                                        map.put("infor", id_auth);
                                    } else if ("0".equals(id_auth) && "0".equals(drive_auth)) {
                                        map.put("infor", id_auth);
                                    } else {
                                        map.put("infor", "2");
                                    }

                                    list3.remove(1);
                                    list3.add(1, map);

                                    listAdapter3.setData(list3);
                                    listAdapter3.notifyDataSetChanged();
                                } else if ("2".equals(dataJsonObject.getString("category"))) {// 企业
                                    Map<String, Object> map = list3.get(1);
                                    map.remove("infor");
                                    if ("1".equals(id_auth) && "1".equals(business_auth)) {
                                        map.put("infor", id_auth);
                                    } else if ("3".equals(id_auth) && "3".equals(business_auth)) {
                                        map.put("infor", id_auth);
                                    } else if ("0".equals(id_auth) && "0".equals(business_auth)) {
                                        map.put("infor", id_auth);
                                    } else {
                                        map.put("infor", "2");
                                    }

                                    list3.remove(1);
                                    list3.add(1, map);

                                    listAdapter3.setData(list3);
                                    listAdapter3.notifyDataSetChanged();
                                } else {
                                    Map<String, Object> map = list3.get(1);
                                    map.remove("infor");
                                    map.put("infor", "3");

                                    list3.remove(1);
                                    list3.add(1, map);

                                    listAdapter3.setData(list3);
                                    listAdapter3.notifyDataSetChanged();
                                }

                                if (!TextUtils.isEmpty(dataJsonObject.getString("score"))) {
                                    int score1 = Integer.parseInt(dataJsonObject.getString("score"));
                                    if (score1 <= 100) {
                                        showdengji(1);
                                    } else if (100 < score1 && score1 <= 200) {
                                        showdengji(2);
                                    } else if (200 < score1 && score1 <= 300) {
                                        showdengji(3);
                                    } else if (300 < score1) {
                                        showdengji(4);
                                    }

                                } else {
                                    showdengji(1);
                                }

                            }

                        } else if ("1002".equals(inforJsonObject.getString("errcode"))) {
                            PublicUtil.showToast(UserActivity.this, inforJsonObject.getString("msg"), false);
                            Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else {
                            PublicUtil.showToast(UserActivity.this, inforJsonObject.getString("msg"), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }

                }

            }

        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent;
            switch (arg0.getId()) {
                case R.id.tab_user_qianbao_layout:
                    intent = new Intent(UserActivity.this, MyWalletActivity.class);
                    startActivity(intent);
                    break;

                case R.id.tab_user_jifen_layout:
                    PublicUtil.showToast(UserActivity.this, "该功能暂未开启，敬请期待", false);

                    break;

                case R.id.tab_user_dingdan_layout:
                    intent = new Intent(UserActivity.this, CarRestlistActivity.class);
                    startActivity(intent);

                    break;
                case R.id.tab_user_dengji_userimage:
                case R.id.tab_user_dengji_userimage2:
                case R.id.tab_user_dengji_userimage3:
                case R.id.tab_user_dengji_userimage4:
                    intent = new Intent(UserActivity.this, UserInforActivity.class);
                    startActivity(intent);

                    break;

                default:
                    break;
            }

        }
    };

}
