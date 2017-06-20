package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.CarduanzuListAdapter;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.ResCarList;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
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
 * 租车页面
 *
 * @author WIN10
 */
public class DuanzuActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "DuanzuActivity";
    private final int REQUESTCODE = 0x9310;

    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private TextView mTvCity;
    private String city_name = "深圳", city_id = "77";// 默认是深圳
    private boolean isDownFlush = false;// 是否是下拉刷新

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private CarduanzuListAdapter carDuanzuListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duanzu);
        addActivity(this);
        init();
    }

    private void init() {

        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
//        topLayout = (FrameLayout) findViewById(R.id.duanzu_toplayout);
//        setTopLayoutPadding(topLayout);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mTvCity.setText(PublicUtil.getStorage_string(this, "mycity", "深圳"));
        //dizhiLayout = (LinearLayout) findViewById(R.id.duanzu_dizhi_layout);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.duanzu_listview);
        mIvBack.setOnClickListener(this);
        //系统没有车辆显示
        View emptyView = LayoutInflater.from(DuanzuActivity.this).inflate(
                R.layout.view_listview_empty_show_congzhi, null);
        TextView emptyinforView = (TextView) emptyView
                .findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无车辆");

        pullToRefreshListView.setEmptyView(emptyView);
        pullToRefreshListView.setMode(Mode.PULL_DOWN_TO_REFRESH);
        ILoadingLayout startLabels = pullToRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabels = pullToRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub

                isDownFlush = true;

                pullToRefreshListView.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getData();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub

                pullToRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        PublicUtil.showToast(DuanzuActivity.this, "没有更多车辆了",
                                false);
                        pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                        // 我们已经更新完成
                    }
                }, 1000);

            }
        });


        listView = pullToRefreshListView.getRefreshableView();
        carDuanzuListAdapter = new CarduanzuListAdapter(DuanzuActivity.this);
        listView.setAdapter(carDuanzuListAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                String islogin = PublicUtil.getStorage_string(
                        DuanzuActivity.this, "islogin", "0");
                Intent intent;
                if ("1".equals(islogin)) {
                    //已经登录
                    //Map<String, String> map = data.get((int) arg3);
                    intent = new Intent(DuanzuActivity.this,
                            CarInforActivity.class);
                    intent.putExtra("id", carDuanzuListAdapter.getItem((int) arg3).mId);
                } else {
                    PublicUtil.showToast(DuanzuActivity.this, "请登录", false);
                    intent = new Intent(DuanzuActivity.this,
                            LoginActivity.class);
                }
                startActivity(intent);
            }
        });

        getData();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 11 && data != null) {
            city_id = data.getStringExtra("id");
            city_name = data.getStringExtra("name");
            mTvCity.setText(city_name);
            isDownFlush = true;
            getData();

        }
    }




    /**
     * 获取租车列表中的车辆信息(根据城市id获取该城市的列表)
     */
    private void getData() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_NEW_GET_CAR_LIST);

        AnsynHttpRequest.httpRequest(DuanzuActivity.this, AnsynHttpRequest.GET,
                callBack, Constent.ID_GET_NEW_CAR_LIST, map, false, true,
                true);
    }

    private JSONObject jsonObject = null;
    /**
     * http请求回调
     */
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @SuppressLint("NewApi")
        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_GET_NEW_CAR_LIST:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_http_getcarlist);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_http_getcarlist;
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

    private int error_http_getcarlist = 0x9300;
    private int success_http_getcarlist = 0x9301;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg != null && msg.what == error_http_getcarlist) {
                pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                if (msg.obj != null) {
                    PublicUtil.showToast(DuanzuActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getcarlist) {
                pullToRefreshListView.onRefreshComplete();// 通知RefreshListView

                if (jsonObject != null) {
                    ResCarList res = BeanParser.parseCarList(jsonObject);
                    refreshData(res);
//                    try {
//                        if ("0".equals(jsonObject.getString("errcode"))) {
//                            JSONObject dataObj = jsonObject.optJSONObject("data");
//                            JSONArray array = dataObj.getJSONArray("list");
//                            if (array != null) {
//                                flushList(array);
//                            }
//
//                        } else {
//                            PublicUtil.showToast(DuanzuActivity.this,
//                                    jsonObject.get("msg").toString(), false);
//                        }
//                    } catch (Exception e) {
//                        // TODO: handle exception
//                        e.printStackTrace();
//                    }

                }

            }

        }

        ;
    };

    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

    private void refreshData(ResCarList res) {
        if (isDownFlush) {
            isDownFlush = false;
        }

        pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
        if(res.mCode == BaseRes.RESULT_OK) {
            carDuanzuListAdapter.updateData(res.mDatas);
        }else {
            PublicUtil.showToast(DuanzuActivity.this, res.mMessage, false);
        }


    }

    /**
     * 刷新列表
     *
     * @param array
     */
    private void flushList(JSONArray array) {
        JSONObject object;

        try {
            if (isDownFlush) {
                isDownFlush = false;
                data.clear();
            }
            //将从服务器中获得的城市车辆信息显示到view
            for (int i = 0; i < array.length(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                object = array.getJSONObject(i);
                map.put("id", object.getString("id"));
                map.put("brand_name", object.getString("brand_name"));

                map.put("car_model_name", object.getString("car_model_name"));
                map.put("car_type_name", object.getString("car_type_name"));
                map.put("endurance_mileage", object.getString("endurance_mileage"));
                map.put("time_price", object.getString("time_price"));
                map.put("day_price", object.getString("day_price"));
                map.put("car_full_img", object.getString("car_full_img"));
                map.put("cubage", object.getString("cubage"));
                data.add(map);

            }
            carDuanzuListAdapter.setData(data);
            carDuanzuListAdapter.notifyDataSetChanged();
            pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
            // 我们已经更新完成

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (arg0.getId()) {
            case R.id.duanzu_dizhi_layout:
                intent = new Intent(DuanzuActivity.this, AddrListActivity.class);
                startActivityForResult(intent, REQUESTCODE);
                break;

            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }

    }

}
