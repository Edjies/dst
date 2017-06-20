package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
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
 * 我的订单
 */
public class MyRentCarOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton backImageButton;
    private LinearLayout fenshiTopLayout;
    private LinearLayout changzhuTopLayout;
    private TextView changzhuTopLine;
    private TextView fenshiTopLine;
    private PullToRefreshListView pullToRefreshListView;
    private ListView refreshListView;
    private ILoadingLayout startLabels;
    private List<Map<String, String>> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_my_rent_car_order);
        backImageButton = (ImageButton) findViewById(R.id.activity_my_rent_car_order_back);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.activity_my_rent_car_order_top_layout);
        setTopLayoutPadding(topLayout);
        fenshiTopLayout = (LinearLayout) findViewById(R.id.activity_my_rent_car_order_fenshi_top_layout);
        fenshiTopLine = (TextView) findViewById(R.id.activity_my_rent_car_order_fenshi_top_line);
        changzhuTopLayout = (LinearLayout) findViewById(R.id.activity_my_rent_car_order_changzhu_top_layout);
        changzhuTopLine = (TextView) findViewById(R.id.activity_my_rent_car_order_changzhu_top_line);
        fenshiTopLine.setVisibility(View.VISIBLE);
        changzhuTopLine.setVisibility(View.INVISIBLE);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        refreshListView = pullToRefreshListView.getRefreshableView();
        View View1 = View.inflate(this, R.layout.myrentorder_listview_duanzhu, null);
        View View2 = View.inflate(this, R.layout.myrentorder_listview_changzhu, null);

        PullToRefreshListView duanZhuRefreshView = (PullToRefreshListView) View1.findViewById(R.id.myrentorder_listview_duanzhu_refresh_view);
        PullToRefreshListView changZhuRefreshView = (PullToRefreshListView) View2.findViewById(R.id.myrentorder_listview_changzhu_refresh_view);

        startLabels = duanZhuRefreshView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_rent_car_order_layout, null);
        duanZhuRefreshView.setEmptyView(emptyView);

        ILoadingLayout startLabels = changZhuRefreshView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        changZhuRefreshView.setEmptyView(emptyView);
        ListView duanRentListVeiw = duanZhuRefreshView.getRefreshableView();
        ListView changRentListVeiw = changZhuRefreshView.getRefreshableView();

        duanZhuRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData(false);
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        changZhuRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_my_rent_car_order_view_pager);
    }

    /**
     * 获取订单列表数据
     */
    private void getData(boolean isShowDialog) {
        Map<String, String> map = new HashMap<>();
        map.put("act", Constent.ACT_NEW_ORDER_LIST);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET
                , callback, Constent.ID_ACT_NEW_ORDER_LIST, map, false, isShowDialog, true);

    }

    private HttpRequestCallBack callback = new HttpRequestCallBack() {
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            if (backId == Constent.ID_ACT_NEW_ORDER_LIST) {  //订单列表
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isRequestSuccess) {
                            if (!isString) {
                                try {
                                    String backStr = jsonArray.getString(1);
                                    JSONObject jsonObject = new JSONObject(backStr);
                                    if (jsonObject.getString("errcode").equals("0")) {
                                        JSONObject dataObject = jsonObject.optJSONObject("data");
                                        if (dataObject != null) {
                                            JSONArray listArray = dataObject.optJSONArray("list");
                                            if (listArray.length() > 0) {
                                                orderList = new ArrayList<>();
                                                for (int i = 0; i < listArray.length(); i++) {
                                                    JSONObject singleOrderJsonObject = (JSONObject) listArray.get(i);
                                                    Map<String, String> singleOrderMap = new HashMap<String, String>();
                                                    singleOrderMap.put("order_no"
                                                            , singleOrderJsonObject.getString("order_no"));
                                                    singleOrderMap.put("type"
                                                            , singleOrderJsonObject.getString("type"));
                                                    String qc_time = singleOrderJsonObject.getString("qc_time");
                                                    singleOrderMap.put("qc_time"
                                                            , qc_time);
                                                    singleOrderMap.put("hc_time"
                                                            , singleOrderJsonObject.getString("hc_time"));
                                                    singleOrderMap.put("qc_real_time"
                                                            , singleOrderJsonObject.getString("qc_real_time"));
                                                    singleOrderMap.put("hc_real_time"
                                                            , singleOrderJsonObject.getString("hc_real_time"));
                                                    singleOrderMap.put("qc_store_id"
                                                            , singleOrderJsonObject.getString("qc_store_id"));
                                                    singleOrderMap.put("hc_store_id"
                                                            , singleOrderJsonObject.getString("hc_store_id"));
                                                    singleOrderMap.put("amount_confirm"
                                                            , singleOrderJsonObject.getString("amount_confirm"));
                                                    singleOrderMap.put("rent_amount"
                                                            , singleOrderJsonObject.getString("rent_amount"));
                                                    singleOrderMap.put("rent_real_amount"
                                                            , "￥" + singleOrderJsonObject.getString("rent_real_amount"));
                                                    singleOrderMap.put("has_pay_amount"
                                                            , "￥" + singleOrderJsonObject.getString("has_pay_amount"));
                                                    String system_time = dataObject.getString("system_time");
                                                    String order_time = singleOrderJsonObject.getString("order_time");
                                                    //押金未付时间
                                                    int totalMiao = PublicUtil.getTimeFromDate(system_time) - PublicUtil.getTimeFromDate(order_time);
                                                    int yajinWeiFuFen = totalMiao / 60;
                                                    int yajinWeiFuMiao = totalMiao % 60;
                                                    singleOrderMap.put("yajin_notpay_time"
                                                            , yajinWeiFuFen + "分" + yajinWeiFuMiao + "秒");
                                                    //日租距离下次结算时间
                                                    int JieShuanFen = PublicUtil.compareFen(system_time, system_time.substring(0, 10) + " 23:59:59");
                                                    int jieShuanHour = JieShuanFen / 60;
                                                    int jieShuanFen = JieShuanFen % 60;
                                                    singleOrderMap.put("jieshuan_time_has"
                                                            , jieShuanHour + "小时" + jieShuanFen + "分钟");
                                                    orderList.add(singleOrderMap);
                                                }
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

            }
        }
    };

    private void initListener() {
        backImageButton.setOnClickListener(this);
        fenshiTopLayout.setOnClickListener(this);
        changzhuTopLayout.setOnClickListener(this);
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
        switch (v.getId()) {
            case R.id.activity_my_rent_car_order_back:
                finish();
                break;
            case R.id.activity_my_rent_car_order_fenshi_top_layout:
                fenshiTopLine.setVisibility(View.VISIBLE);
                changzhuTopLine.setVisibility(View.INVISIBLE);
                break;
            case R.id.activity_my_rent_car_order_changzhu_top_layout:
                fenshiTopLine.setVisibility(View.INVISIBLE);
                changzhuTopLine.setVisibility(View.VISIBLE);
                break;
        }
    }

}
