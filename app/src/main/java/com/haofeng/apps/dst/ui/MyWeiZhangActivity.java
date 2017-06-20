package com.haofeng.apps.dst.ui;


import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.WeiZhangInfoAdapter;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的违章
 *
 * @author zlm
 */
public class MyWeiZhangActivity extends BaseActivity {

    private FrameLayout weichuliLayout;
    private FrameLayout yichuliLayout;
    private TextView weichuliTextView;
    private TextView yichuliTextView;
    private TextView weiichuliLineTextView;
    private TextView yichuliLineLineTextView;
    private PullToRefreshListView freshView;
    private final String NODEAL = "0";
    private final String YESDEAL = "1";
    private String dealStatus = NODEAL;   //处理状态
    private ListView refreshListView;
    private TextView backView;
    private static final String TAG = "MyWeiZhangActivity";
    List<Map<String, String>> wzInfoDealList = new ArrayList<Map<String, String>>();
    List<Map<String, String>> wzInfoNoDealList = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addActivity(this);
        initView();
        initListener();
    }


    private void initView() {
        setContentView(R.layout.activity_myweizhang);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.acitivty_myweizhang_top_layout);
        setTopLayoutPadding(topLayout);
        weichuliLayout = (FrameLayout) findViewById(R.id.acitivty_myweizhang_weichuli_layout);
        yichuliLayout = (FrameLayout) findViewById(R.id.acitivty_myweizhang_yichuli_layout);
        weichuliTextView = (TextView) findViewById(R.id.acitivty_myweizhang_weichuli);
        yichuliTextView = (TextView) findViewById(R.id.acitivty_myweizhang_yichuli);
        weiichuliLineTextView = (TextView) findViewById(R.id.acitivty_myweizhang_weichuli_line);
        yichuliLineLineTextView = (TextView) findViewById(R.id.acitivty_myweizhang_yichuli_line);
        backView = (TextView) findViewById(R.id.my_weizhang_back);

        freshView = (PullToRefreshListView) findViewById(R.id.activity_my_weizhang_pull_fresh);
        refreshListView = freshView.getRefreshableView();
        View emptyView = View.inflate(this, R.layout.view_listview_empty_show_weizhang, null);
        freshView.setEmptyView(emptyView);
        freshView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);

        ILoadingLayout startLabels = freshView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        freshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
            }
        });

        weichuliTextView.setTextColor(getResources().getColor(R.color.textgreen));
        weiichuliLineTextView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                freshView.setRefreshing();
            }
        }, 300);

    }


    private void initListener() {
        weichuliLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weichuliTextView.setTextColor(getResources().getColor(R.color.textgreen));
                yichuliTextView.setTextColor(getResources().getColor(R.color.gray));
                weiichuliLineTextView.setVisibility(View.VISIBLE);
                yichuliLineLineTextView.setVisibility(View.GONE);
                dealStatus = NODEAL;
                freshView.setRefreshing();

            }
        });
        yichuliLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weichuliTextView.setTextColor(getResources().getColor(R.color.gray));
                yichuliTextView.setTextColor(getResources().getColor(R.color.black));
                weiichuliLineTextView.setVisibility(View.GONE);
                yichuliLineLineTextView.setVisibility(View.VISIBLE);
                dealStatus = YESDEAL;
                freshView.setRefreshing();
            }
        });
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    private void getData() {
        Map<String, String> map = new HashMap<>();
        map.put("act", Constent.ACT_MEMBER_MY_WZ);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_MEMBER_MY_WZ, map, false, false, true);
    }

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backStr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backStr);
                                if (jsonObject.getString("errcode").equals("0")) {
                                    JSONArray dataArray = jsonObject.optJSONArray("data");
                                    if (dataArray != null && dataArray.length() > 0) {
                                        wzInfoNoDealList.clear();
                                        wzInfoNoDealList.clear();
                                        for (int i = 0; i < dataArray.length(); i++) {
                                            Map<String, String> wzinfoMap = new HashMap<>();
                                            JSONObject wzInfos = (JSONObject) dataArray.get(i);
                                            wzinfoMap.put("act", wzInfos.getString("act"));
                                            wzinfoMap.put("date", wzInfos.getString("date"));
                                            wzinfoMap.put("area", wzInfos.getString("area"));
                                            wzinfoMap.put("money", wzInfos.getString("money"));
                                            wzinfoMap.put("plate_number", wzInfos.getString("plate_number"));
                                            wzinfoMap.put("fen", wzInfos.getString("fen"));
                                            wzinfoMap.put("status", wzInfos.getString("status"));
                                            if (wzInfos.getString("status").equals("2")) { //未处理
                                                wzInfoNoDealList.add(wzinfoMap);
                                            } else {
                                                wzInfoDealList.add(wzinfoMap);
                                            }
                                        }
                                        if (dealStatus.equals(NODEAL)) {       //未处理
                                            refreshListView.setAdapter(null);
                                            if (wzInfoNoDealList.size() > 0) {
                                                refreshListView.setAdapter(new WeiZhangInfoAdapter(wzInfoNoDealList, MyWeiZhangActivity.this, NODEAL));
                                            }

                                        } else if (dealStatus.equals(YESDEAL)) {   //已处理
                                            refreshListView.setAdapter(null);
                                            if (wzInfoDealList.size() > 0) {
                                                refreshListView.setAdapter(new WeiZhangInfoAdapter(wzInfoDealList, MyWeiZhangActivity.this, YESDEAL));
                                            }
                                        }
                                    } else {
                                        refreshListView.setAdapter(null);
                                    }
                                }
                                freshView.onRefreshComplete();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            });

        }
    };
}
