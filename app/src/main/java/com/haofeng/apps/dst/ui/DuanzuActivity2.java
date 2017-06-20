package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.CitySite;
import com.haofeng.apps.dst.bean.ResCarList;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.adapter.CarListAdapter;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * 租车页面
 *
 * @author WIN10
 */
public class DuanzuActivity2 extends BaseActivity implements OnClickListener {
    private static final String TAG = "DuanzuActivity";
    private final int REQUESTCODE = 0x9310;
    private final int CITY_REQUEST_CODE = 400;

    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private TextView mTvCity;
    private CitySite mCitySite = new CitySite();
    private boolean isDownFlush = false;// 是否是下拉刷新

    private PullToRefreshListView pullToRefreshListView;
    private ListView listView;
    private CarListAdapter mAdapter;

    private Handler mHandler = new Handler();

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

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvCity = (TextView) findViewById(R.id.tv_city);
        mTvCity.setText(PublicUtil.getStorage_string(this, "mycity", "深圳"));
        mTvCity.setOnClickListener(this);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.duanzu_listview);
        mIvBack.setOnClickListener(this);
        //系统没有车辆显示
//        View emptyView = LayoutInflater.from(DuanzuActivity2.this).inflate(R.layout.view_listview_empty_show_congzhi, null);
//        TextView emptyinforView = (TextView) emptyView.findViewById(R.id.view_listview_emptyshow_text);
//        emptyinforView.setText("暂无车辆");
//
//        pullToRefreshListView.setEmptyView(emptyView);
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
        pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                isDownFlush = true;
                execGetCarList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });


        listView = pullToRefreshListView.getRefreshableView();
        mAdapter = new CarListAdapter(DuanzuActivity2.this);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String islogin = PublicUtil.getStorage_string(DuanzuActivity2.this, "islogin", "0");
                Intent intent;
                if ("1".equals(islogin)) {
                    //已经登录
                    intent = new Intent(DuanzuActivity2.this, CarInforActivity.class);
                    intent.putExtra("id", mAdapter.getItem((int) arg3).mId);
                } else {
                    PublicUtil.showToast(DuanzuActivity2.this, "请登录", false);
                    intent = new Intent(DuanzuActivity2.this, LoginActivity.class);
                }
                startActivity(intent);
            }
        });

        execGetCarList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);// 友盟统计开始
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);// 友盟统计结束
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CitySite city = (CitySite) data.getSerializableExtra("city");
            this.mCitySite = city;
            mTvCity.setText(city.m_region_name);
            execGetCarList();

        }
//        if (resultCode == 11 && data != null) {
//            city_id = data.getStringExtra("id");
//            city_name = data.getStringExtra("name");
//            mTvCity.setText(city_name);
//            isDownFlush = true;
//            execGetCarList();
//
//        }
    }



    /**
     * 获取租车列表中的车辆信息(根据城市id获取该城市的列表)
     */
    private void execGetCarList() {
        showProgress("加载中...");
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/get_list")
                    .append("city_id", mCitySite.m_region_id)
                    .append("user_id", PublicUtil.getStorage_string(this, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(this, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(TAG, url);
        UVolley.getVolley(this).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                pullToRefreshListView.onRefreshComplete();
                ULog.e(TAG, response.toString());
                if(isPageResumed) {
                    ResCarList res = BeanParser.parseCarList(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        Toast.makeText(DuanzuActivity2.this, "获取车辆列表成功", Toast.LENGTH_SHORT).show();
                        // 用户
                        mAdapter.updateData(res.mDatas);
                    }else {
                        Toast.makeText(DuanzuActivity2.this, res.mMessage, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pullToRefreshListView.onRefreshComplete();
                hideProgress();
            }
        }));
    }


    @Override
    public void onClick(View arg0) {
        Intent intent = null;
        switch (arg0.getId()) {
            case R.id.tv_city:
                CitySiteActivity.intentMe(this, CITY_REQUEST_CODE);
                break;
            case R.id.duanzu_dizhi_layout:
                intent = new Intent(DuanzuActivity2.this, AddrListActivity.class);
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
