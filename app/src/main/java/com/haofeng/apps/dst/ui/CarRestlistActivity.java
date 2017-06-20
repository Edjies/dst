package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.CarRestListAdapter;
import com.haofeng.apps.dst.adapter.CarRestListAdapter.Callback;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租车订单列表
 *
 * @author Administrator
 */
public class CarRestlistActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "CarRestlistActivity";
    private FrameLayout topLayout;
    private TextView backTextView;
    private TextView allView, daifukuanView, daiticheView, yiticheView, yiquxiaoView;
    private TextView allView_line, daifukuanView_line, daiticheView_line, yiticheView_line, yiquxiaoView_line;
    private PullToRefreshListView pView_all, pView_daifukuan, pView_daitiche, pView_yitiche, pView_yiquxiao;
    private ListView listView_all, listView_daifukuan, listView_daitiche, listView_yitiche, listView_yiquxiao;
    private CarRestListAdapter allAdapter, daifukuanAdapter, daiticheAdapter, yiticheAdapter, yiquxiaoAdapter;

    private int type = 0;// 默认 0全部 1待付款 2待提车 3已提车 4已取消
    private List<Map<String, String>> data_all = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> data_daifukuan = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> data_daitiche = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> data_yitiche = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> data_yiquxiao = new ArrayList<Map<String, String>>();
    private int allPage_all = 1, allPage_daifukuan = 1, allPage_daitiche = 1, allPage_yitiche = 1, allPage_yiquxiao = 1;
    private int nowPage_all = 1, nowPage_daifukuan = 1, nowPage_daitiche = 1, nowPage_yitiche = 1, nowPage_yiquxiao = 1;
    private boolean isdaifukuanfrist = true, isdaitichefrist = true, isyitichefrist = true, isyiquxiaofrist = true;
    private boolean isDownFlush_all = false, isDownFlush_daifukuan = false, isDownFlush_daitiche = false,
            isDownFlush_yitiche = false, isDownFlush_yiquxiao = false;// 是否是下拉刷新
    private ViewPager viewPager;
    private List<View> listViews = new ArrayList<View>(); // Tab页面列表
    private boolean isYiTiCheHas = false; //是否有已提成订单
    private boolean isDaiTiCheHas = false;  //是否有待提车订单
    private boolean isDaiFuKuan = false;  //是否有待付款订单
    private Runnable runnable;
    private String qc_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrestlist);
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

    public void init() {
        topLayout = (FrameLayout) findViewById(R.id.carrestlist_top_layout);
        setTopLayoutPadding(topLayout);
        backTextView = (TextView) findViewById(R.id.carrestlist_back);
        allView = (TextView) findViewById(R.id.carrestlist_all);
        daifukuanView = (TextView) findViewById(R.id.carrestlist_daifukuan);
        daiticheView = (TextView) findViewById(R.id.carrestlist_daitiche);
        yiticheView = (TextView) findViewById(R.id.carrestlist_yitiche);
        yiquxiaoView = (TextView) findViewById(R.id.carrestlist_yiquxiao);
        allView_line = (TextView) findViewById(R.id.carrestlist_all_line);
        daifukuanView_line = (TextView) findViewById(R.id.carrestlist_daifukuan_line);
        daiticheView_line = (TextView) findViewById(R.id.carrestlist_daitiche_line);
        yiticheView_line = (TextView) findViewById(R.id.carrestlist_yitiche_line);
        yiquxiaoView_line = (TextView) findViewById(R.id.carrestlist_yiquxiao_line);
        backTextView.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.carrestlist_vpager);
        // 这里开始属于tab滑动

        View view1 = this.getLayoutInflater().inflate(R.layout.carrestlist_listview_all, null);
        View view2 = this.getLayoutInflater().inflate(R.layout.carrestlist_listview_daifukuan, null);
        View view3 = this.getLayoutInflater().inflate(R.layout.carrestlist_listview_daitiche, null);
        View view4 = this.getLayoutInflater().inflate(R.layout.carrestlist_listview_yitiche, null);
        View view5 = this.getLayoutInflater().inflate(R.layout.carrestlist_listview_yiquxiao, null);
        pView_all = (PullToRefreshListView) view1.findViewById(R.id.carrestlist_listview_all);
        pView_daifukuan = (PullToRefreshListView) view2.findViewById(R.id.carrestlist_listview_daifukuan);
        pView_daitiche = (PullToRefreshListView) view3.findViewById(R.id.carrestlist_listview_daitiche);
        pView_yitiche = (PullToRefreshListView) view4.findViewById(R.id.carrestlist_listview_yitiche);
        pView_yiquxiao = (PullToRefreshListView) view5.findViewById(R.id.carrestlist_listview_yiquxiao);

        allAdapter = new CarRestListAdapter(CarRestlistActivity.this, inforCallback, tarendqCallback, lijifkCallback,
                xuzuCallback, shenqinghcCallback, quxiaodingdanCallback);
        daifukuanAdapter = new CarRestListAdapter(CarRestlistActivity.this, inforCallback, tarendqCallback,
                lijifkCallback, xuzuCallback, shenqinghcCallback, quxiaodingdanCallback);
        daiticheAdapter = new CarRestListAdapter(CarRestlistActivity.this, inforCallback, tarendqCallback,
                lijifkCallback, xuzuCallback, shenqinghcCallback, quxiaodingdanCallback);
        yiticheAdapter = new CarRestListAdapter(CarRestlistActivity.this, inforCallback, tarendqCallback,
                lijifkCallback, xuzuCallback, shenqinghcCallback, quxiaodingdanCallback);
        yiquxiaoAdapter = new CarRestListAdapter(CarRestlistActivity.this, inforCallback, tarendqCallback,
                lijifkCallback, xuzuCallback, shenqinghcCallback, quxiaodingdanCallback);

        View view = LayoutInflater.from(this).inflate(R.layout.view_listview_empty_show_congzhi, null);
        TextView emptyinforView = (TextView) view.findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无订单");
        flushOrederStatus(); // 是否有已提车订单
        // 全部列表
        pView_all.setEmptyView(view);
        pView_all.setMode(Mode.BOTH);
        ILoadingLayout startLabels = pView_all.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pView_all.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        pView_all.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub

                isDownFlush_all = true;
                nowPage_all = 1;

                pView_all.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getData(false);
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                nowPage_all++;
                pView_all.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (nowPage_all <= allPage_all) {
                            getData(false);
                        } else {
                            nowPage_all--;
                            if (nowPage_all < 1) {
                                nowPage_all = 1;

                            }
                            PublicUtil.showToast(CarRestlistActivity.this, "没有更多订单了", false);
                            pView_all.onRefreshComplete();// 通知RefreshListView
                            // 我们已经更新完成
                        }
                    }
                }, 1000);

            }
        });

        listView_all = pView_all.getRefreshableView();

        listView_all.setAdapter(allAdapter);

        // 待付款
        view = LayoutInflater.from(this).inflate(R.layout.view_listview_empty_show_congzhi, null);
        emptyinforView = (TextView) view.findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无订单");
        pView_daifukuan.setEmptyView(view);
        pView_daifukuan.setMode(Mode.BOTH);
        startLabels = pView_daifukuan.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        endLabels = pView_daifukuan.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        pView_daifukuan.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub

                isDownFlush_daifukuan = true;
                nowPage_daifukuan = 1;

                pView_daifukuan.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getData(false);
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                // collectionListAdapter.notifyDataSetChanged();
                nowPage_daifukuan++;
                pView_daifukuan.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (nowPage_daifukuan <= allPage_daifukuan) {
                            getData(false);
                        } else {
                            nowPage_daifukuan--;
                            if (nowPage_daifukuan < 1) {
                                nowPage_daifukuan = 1;
                            }
                            PublicUtil.showToast(CarRestlistActivity.this, "没有更多订单了", false);
                            pView_daifukuan.onRefreshComplete();// 通知RefreshListView
                            // 我们已经更新完成
                        }
                    }
                }, 1000);

            }
        });

        listView_daifukuan = pView_daifukuan.getRefreshableView();

        listView_daifukuan.setAdapter(daifukuanAdapter);

        // 待提车
        view = LayoutInflater.from(this).inflate(R.layout.view_listview_empty_show_congzhi, null);
        emptyinforView = (TextView) view.findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无订单");
        pView_daitiche.setEmptyView(view);
        pView_daitiche.setMode(Mode.BOTH);
        startLabels = pView_daitiche.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        endLabels = pView_daitiche.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        pView_daitiche.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub

                isDownFlush_daitiche = true;
                nowPage_daitiche = 1;

                pView_daitiche.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getData(false);
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                // collectionListAdapter.notifyDataSetChanged();

                nowPage_daitiche++;
                pView_daitiche.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (nowPage_daitiche <= allPage_daitiche) {
                            getData(false);
                        } else {
                            nowPage_daitiche--;
                            if (nowPage_daitiche < 1) {
                                nowPage_daitiche = 1;

                            }
                            PublicUtil.showToast(CarRestlistActivity.this, "没有更多订单了", false);
                            pView_daitiche.onRefreshComplete();// 通知RefreshListView
                            // 我们已经更新完成
                        }
                    }
                }, 1000);

            }
        });

        listView_daitiche = pView_daitiche.getRefreshableView();
        listView_daitiche.setAdapter(daiticheAdapter);

        // 已提车
        view = LayoutInflater.from(this).inflate(R.layout.view_listview_empty_show_congzhi, null);
        emptyinforView = (TextView) view.findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无订单");
        pView_yitiche.setEmptyView(view);
        pView_yitiche.setMode(Mode.BOTH);
        startLabels = pView_yitiche.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        endLabels = pView_yitiche.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        pView_yitiche.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub

                isDownFlush_yitiche = true;
                nowPage_yitiche = 1;

                pView_yitiche.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getData(false);
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                nowPage_yitiche++;
                pView_yitiche.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (nowPage_yitiche <= allPage_yitiche) {
                            getData(false);
                        } else {
                            nowPage_yitiche--;
                            if (nowPage_yitiche < 1) {
                                nowPage_yitiche = 1;

                            }
                            PublicUtil.showToast(CarRestlistActivity.this, "没有更多订单了", false);
                            pView_yitiche.onRefreshComplete();// 通知RefreshListView
                            // 我们已经更新完成
                        }
                    }
                }, 1000);

            }
        });

        listView_yitiche = pView_yitiche.getRefreshableView();
        listView_yitiche.setAdapter(yiticheAdapter);

        // 已取消
        view = LayoutInflater.from(this).inflate(R.layout.view_listview_empty_show_congzhi, null);
        emptyinforView = (TextView) view.findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无订单");
        pView_yiquxiao.setEmptyView(view);
        pView_yiquxiao.setMode(Mode.BOTH);
        startLabels = pView_yiquxiao.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        endLabels = pView_yiquxiao.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        pView_yiquxiao.setOnRefreshListener(new OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub

                isDownFlush_yiquxiao = true;
                nowPage_yiquxiao = 1;

                pView_yiquxiao.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getData(false);
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                // collectionListAdapter.notifyDataSetChanged();

                nowPage_yiquxiao++;
                pView_yiquxiao.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (nowPage_yiquxiao <= allPage_yiquxiao) {
                            getData(false);
                        } else {
                            nowPage_yiquxiao--;
                            if (nowPage_yiquxiao < 1) {
                                nowPage_yiquxiao = 1;

                            }
                            PublicUtil.showToast(CarRestlistActivity.this, "没有更多订单了", false);
                            pView_yiquxiao.onRefreshComplete();// 通知RefreshListView
                            // 我们已经更新完成
                        }
                    }
                }, 1000);

            }
        });

        listView_yiquxiao = pView_yiquxiao.getRefreshableView();
        listView_yiquxiao.setAdapter(yiquxiaoAdapter);
        // 几个ListView
        listViews.add(view1);
        listViews.add(view2);
        listViews.add(view3);
        listViews.add(view4);
        listViews.add(view5);

        viewPager.setAdapter(new MyPagerAdapter(listViews));
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
        allView.setOnClickListener(new topTabClick(0));
        daifukuanView.setOnClickListener(new topTabClick(1));
        daiticheView.setOnClickListener(new topTabClick(2));
        yiticheView.setOnClickListener(new topTabClick(3));
        yiquxiaoView.setOnClickListener(new topTabClick(4));

        SystemClock.sleep(500);
        if (isDaiFuKuan) {
            type = 1;
            daifukuanView.setTextColor(getResources().getColor(R.color.textgreen));
            daifukuanView_line.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(1);
        } else if (isDaiTiCheHas) {
            type = 2;
            daiticheView.setTextColor(getResources().getColor(R.color.textgreen));
            daiticheView_line.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(2);
        } else if (isYiTiCheHas) {
            type = 3; // 已提车
            yiticheView.setTextColor(getResources().getColor(R.color.textgreen));
            yiticheView_line.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(3);
        } else {
            type = 0;
            allView.setTextColor(getResources().getColor(R.color.textgreen));
            allView_line.setVisibility(View.VISIBLE);
            viewPager.setCurrentItem(0);
        }
        getData(true);

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        if (type == 2) {
            isDownFlush_daitiche = true;
            nowPage_daitiche = 1;
        } else if (type == 0) {
            isDownFlush_all = true;
            nowPage_all = 1;
        } else if (type == 1) {
            isDownFlush_daifukuan = true;
            nowPage_daifukuan = 1;
        } else if (type == 3) {
            isDownFlush_yitiche = true;
            nowPage_yitiche = 1;
        } else if (type == 4) {
            isDownFlush_yiquxiao = true;
            nowPage_yiquxiao = 1;
        }

        getData(true);
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            if (resultCode == 41) {
                if ("success".equals(data.getStringExtra("result"))) {
                    if (type == 2) {
                        isDownFlush_daitiche = true;
                        nowPage_daitiche = 1;
                    } else if (type == 0) {
                        isDownFlush_all = true;
                        nowPage_all = 1;
                    } else if (type == 1) {
                        isDownFlush_daifukuan = true;
                        nowPage_daifukuan = 1;
                    } else if (type == 3) {
                        isDownFlush_yitiche = true;
                        nowPage_yitiche = 1;
                    } else if (type == 4) {
                        isDownFlush_yiquxiao = true;
                        nowPage_yiquxiao = 1;
                    }

                    getData(true);
                }

            }

            if (resultCode == 42) {
                if ("success".equals(data.getStringExtra("result"))) {
                    if (type == 2) {
                        isDownFlush_daitiche = true;
                        nowPage_daitiche = 1;
                    } else if (type == 0) {
                        isDownFlush_all = true;
                        nowPage_all = 1;
                    } else if (type == 1) {
                        isDownFlush_daifukuan = true;
                        nowPage_daifukuan = 1;
                    } else if (type == 3) {
                        isDownFlush_yitiche = true;
                        nowPage_yitiche = 1;
                    } else if (type == 4) {
                        isDownFlush_yiquxiao = true;
                        nowPage_yiquxiao = 1;
                    }

                    getData(true);
                }

            }

        }

    }

    /**
     * 获取数据
     */
    public void getData(boolean isShowDialog) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_ORDER_LIST);
        map.put("rows", PublicUtil.PAGESIZE);
        if (type == 4) {
            map.put("order_state", "5");
            map.put("page", String.valueOf(nowPage_yiquxiao));
        } else if (type == 3) {
            map.put("order_state", "2");
            map.put("page", String.valueOf(nowPage_yitiche));
        } else if (type == 2) {
            map.put("order_state", "1");
            map.put("page", String.valueOf(nowPage_daitiche));
        } else if (type == 1) {
            map.put("order_state", "0");
            map.put("page", String.valueOf(nowPage_daifukuan));
        } else {
            map.put("page", String.valueOf(nowPage_all));
        }

        AnsynHttpRequest.httpRequest(CarRestlistActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_LIST,
                map, false, isShowDialog, true);// 获取订单列表

    }


    private JSONObject jsonObject = null;
    /**
     * http请求回调
     */
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @SuppressLint("NewApi")
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_ORDER_LIST:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case Constent.ID_ORDER_LIST2:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("list");
                                if (array.length() == 0) {
                                    isYiTiCheHas = false;
                                    PublicUtil.setStorage_string(CarRestlistActivity.this, "isYiTiChe", "0");
                                } else {
                                    isYiTiCheHas = true;
                                    PublicUtil.setStorage_string(CarRestlistActivity.this, "isYiTiChe", "1");
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    }
                    break;
                case Constent.ID_ORDER_LIST3:   //待提车
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("list");
                                if (array.length() == 0) {
                                    isDaiTiCheHas = false;
                                } else {
                                    isDaiTiCheHas = true;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                    break;
                case Constent.ID_ORDER_LIST4:   //待付款
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("list");
                                if (array.length() == 0) {
                                    isDaiFuKuan = false;
                                } else {
                                    isDaiFuKuan = true;
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                    }
                    break;

                case Constent.ID_ORDER_AGENT:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_daiqu);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_daiqu;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case Constent.ID_ORDER_CANCEL_ORDER:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_quxiao);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_quxiao;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case Constent.ID_ORDER_GET_DETAIL:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestSuccess) {
                                if (!isString) {
                                    String backstr = null;
                                    try {
                                        backstr = jsonArray.getString(1);
                                        JSONObject jsonObject = new JSONObject(backstr);
                                        if (jsonObject.getString("errcode").equals("0")) {
                                            JSONObject orderObject = jsonObject.optJSONObject("data").optJSONObject("order");
                                            if (orderObject != null) {
                                                String hc_mileage = orderObject.getString("hc_mileage");
                                                if (Float.parseFloat(hc_mileage) > 0) {
                                                    PublicUtil.setStorage_string(CarRestlistActivity.this, "isConfirmHuanCar", "1");
                                                    handler.removeCallbacks(runnable);
                                                } else {
                                                    PublicUtil.setStorage_string(CarRestlistActivity.this, "isConfirmHuanCar", "0");
                                                }
                                            }
                                            yiticheAdapter.notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    });

                    break;
                default:
                    break;
            }

        }
    };

    private int error_httprequest = 0x6100;
    private int success_httprequest = 0x6101;

    private int error_httprequest_daiqu = 0x6104;
    private int success_httprequest_daiqu = 0x6105;

    private int error_httprequest_quxiao = 0x6108;
    private int success_httprequest_quxiao = 0x6109;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg != null && msg.what == error_httprequest) {

                if (type == 4) {
                    pView_yiquxiao.onRefreshComplete();
                } else if (type == 3) {
                    pView_yitiche.onRefreshComplete();// 通知RefreshListView
                } else if (type == 2) {
                    pView_daitiche.onRefreshComplete();// 通知RefreshListView

                } else if (type == 1) {
                    pView_daifukuan.onRefreshComplete();// 通知RefreshListView
                } else {
                    pView_all.onRefreshComplete();// 通知RefreshListView
                }

                if (msg.obj != null) {
                    PublicUtil.showToast(CarRestlistActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest) {
                if (type == 4) {// 取消
                    pView_yiquxiao.onRefreshComplete();
                } else if (type == 3) { // 已提车
                    pView_yitiche.onRefreshComplete();// 通知RefreshListView
                } else if (type == 2) {
                    pView_daitiche.onRefreshComplete();// 通知RefreshListView
                } else if (type == 1) {
                    pView_daifukuan.onRefreshComplete();// 通知RefreshListView
                } else {
                    pView_all.onRefreshComplete();// 通知RefreshListView
                }
                if (jsonObject != null) {
                    try {
                        if ("0".equals(jsonObject.getString("errcode"))) {
                            String back = jsonObject.toString();
                            if (jsonObject.getJSONObject("data").getString("total") != null) {
                                if (type == 4) {
                                    allPage_yiquxiao = PublicUtil
                                            .getAllPage(jsonObject.getJSONObject("data").getString("total"));
                                } else if (type == 3) {
                                    allPage_yitiche = PublicUtil
                                            .getAllPage(jsonObject.getJSONObject("data").getString("total"));
                                } else if (type == 2) {
                                    allPage_daitiche = PublicUtil
                                            .getAllPage(jsonObject.getJSONObject("data").getString("total"));

                                } else if (type == 1) {
                                    allPage_daifukuan = PublicUtil
                                            .getAllPage(jsonObject.getJSONObject("data").getString("total"));
                                } else {
                                    allPage_all = PublicUtil
                                            .getAllPage(jsonObject.getJSONObject("data").getString("total"));
                                }
                                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("list");

                                if (array != null) {
                                    flushList(array);
                                }

                            }

                        } else {
                            PublicUtil.showToast(CarRestlistActivity.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(CarRestlistActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == error_httprequest_daiqu) {

                if (msg.obj != null) {
                    PublicUtil.showToast(CarRestlistActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_daiqu) {
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            PublicUtil.showToast(CarRestlistActivity.this, "代取车人信息提交成功！", false);
                            if (type == 2) {
                                isDownFlush_daitiche = true;
                                nowPage_daitiche = 1;
                            } else if (type == 0) {
                                isDownFlush_all = true;
                                nowPage_all = 1;
                            }

                            getData(false);
                        } else {
                            PublicUtil.showToast(CarRestlistActivity.this, jsonObject.getString("msg"), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == error_httprequest_quxiao) {

                if (msg.obj != null) {
                    PublicUtil.showToast(CarRestlistActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_quxiao) {
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);
                        PublicUtil.showToast(CarRestlistActivity.this, jsonObject.getString("msg"), false);
                        if ("0".equals(jsonObject.getString("errcode"))) {
                            if (type == 2) {
                                isDownFlush_daitiche = true;
                                nowPage_daitiche = 1;
                            } else if (type == 1) {
                                isDownFlush_daifukuan = true;
                                nowPage_daifukuan = 1;
                            } else if (type == 0) {
                                isDownFlush_all = true;
                                nowPage_all = 1;
                            }

                            getData(false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(CarRestlistActivity.this, "配置解析数据错误，请退出重试", false);
                        e.printStackTrace();
                    }

                }

            }

        }

        ;
    };

    /**
     * 刷新列表
     *
     * @param array
     */
    public void flushList(JSONArray array) {
        JSONObject object;

        try {
            data_all.clear();
            data_daifukuan.clear();
            data_daitiche.clear();
            data_yitiche.clear();
            data_yiquxiao.clear();

            int all_count = data_all.size();
            int daifukuan_count = data_daifukuan.size();
            int daitiche_count = data_daitiche.size();
            int yitiche_count = data_yitiche.size();
            int yiquxiao_count = data_yiquxiao.size();

            for (int i = 0; i < array.length(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                object = array.getJSONObject(i);

                if (type == 4) {
                    map.put("i", String.valueOf(i + yiquxiao_count));

                } else if (type == 3) {
                    map.put("i", String.valueOf(i + yitiche_count));
                } else if (type == 2) {
                    map.put("i", String.valueOf(i + daitiche_count));
                } else if (type == 1) {
                    map.put("i", String.valueOf(i + daifukuan_count));
                } else {
                    map.put("i", String.valueOf(i + all_count));
                }
                map.put("type", String.valueOf(type));
                map.put("order_no", object.getString("order_no"));
                map.put("member_id", object.getString("member_id"));
                map.put("order_time", object.getString("order_time"));
                map.put("car_no", object.getString("car_no"));

                map.put("qc_store_id", object.getString("qc_store_id"));
                map.put("hc_store_id", object.getString("hc_store_id"));
                map.put("qc_mileage", object.getString("qc_mileage"));
                map.put("hc_mileage", object.getString("hc_mileage"));
                map.put("free_mileage", object.getString("free_mileage"));
                map.put("overtime_amount", object.getString("overtime_amount"));
                map.put("safe_amount", object.getString("safe_amount"));
                map.put("mileage_amount", object.getString("mileage_amount"));
                map.put("change_car_status", object.getString("change_car_status"));
                map.put("zc_amount", object.getString("zc_amount"));
                map.put("zc_foregift_amount", object.getString("zc_foregift_amount"));
                map.put("wz_foregift_amount", object.getString("wz_foregift_amount"));
                map.put("relet_amount", object.getString("relet_amount"));
                map.put("hour_price", object.getString("hour_price"));
                map.put("total_amount", object.getString("total_amount"));
                map.put("order_state", object.getString("order_state"));
                map.put("refund_state", object.getString("refund_state"));
                map.put("iop_amount", object.getString("iop_amount"));
                map.put("actual_foregift_amount", object.getString("actual_foregift_amount"));
                map.put("amount_confirm", object.getString("amount_confirm"));

                float zujin = 0;
                if (!TextUtils.isEmpty(object.getString("zc_amount"))) {
                    zujin = Float.parseFloat(object.getString("zc_amount"));

                }
                if (!TextUtils.isEmpty(object.getString("actual_foregift_amount"))) {
                    zujin = zujin + Float.parseFloat(object.getString("actual_foregift_amount"));

                }

                if (!TextUtils.isEmpty(object.getString("safe_amount"))) {
                    zujin = zujin + Float.parseFloat(object.getString("safe_amount"));

                }
                if (!TextUtils.isEmpty(object.getString("iop_amount"))) {
                    zujin = zujin + Float.parseFloat(object.getString("iop_amount"));

                }

                BigDecimal b = new BigDecimal(zujin);
                zujin = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 两位小数，四舍五入
                map.put("heji", String.valueOf(zujin));

                map.put("zc_foregift_pay_type", object.getString("zc_foregift_pay_type"));
                map.put("zc_foregift_state", object.getString("zc_foregift_state"));
                map.put("wz_foregift_state", object.getString("wz_foregift_state"));
                map.put("take_car_person_name", object.getString("take_car_person_name"));
                map.put("take_car_person_mobile", object.getString("take_car_person_mobile"));
                map.put("amount_confirm", object.getString("amount_confirm"));

                map.put("zc_type", object.getString("zc_type"));

                map.put("lowest_price", object.getString("lowest_price"));
                map.put("lowest_mileage", object.getString("lowest_mileage"));
                map.put("mileage_price", object.getString("mileage_price"));
                map.put("qc_time", timetotime(object.getString("qc_time"), object.getString("zc_type")));
                map.put("hc_time", timetotime(object.getString("hc_time"), object.getString("zc_type")));

                map.put("hc_time2", object.getString("hc_time"));
                map.put("qc_time2", object.getString("qc_time"));
                if (!"null".equals(object.getString("hc_real_time"))
                        && !TextUtils.isEmpty(object.getString("hc_real_time"))) {
                    map.put("hc_real_time", timetotime(object.getString("hc_real_time"), object.getString("zc_type")));
                } else {
                    map.put("hc_real_time", object.getString("hc_real_time"));
                }

                map.put("timepading",
                        PublicUtil.timeWithTwoTime(object.getString("qc_time"), object.getString("hc_time")));
                if (type == 4) {
                    data_yiquxiao.add(map);
                } else if (type == 3) {
                    data_yitiche.add(map);
                } else if (type == 2) {
                    data_daitiche.add(map);
                } else if (type == 1) {
                    data_daifukuan.add(map);
                } else {
                    data_all.add(map);
                }
            }
            if (type == 4) {
                yiquxiaoAdapter.setData(data_yiquxiao);
                yiquxiaoAdapter.notifyDataSetChanged();
            } else if (type == 3) {
                yiticheAdapter.setData(data_yitiche);
                yiticheAdapter.notifyDataSetChanged();


            } else if (type == 2) {
                daiticheAdapter.setData(data_daitiche);
                daiticheAdapter.notifyDataSetChanged();

            } else if (type == 1) {
                daifukuanAdapter.setData(data_daifukuan);
                daifukuanAdapter.notifyDataSetChanged();
            } else {
                allAdapter.setData(data_all);
                allAdapter.notifyDataSetChanged();
            }

            if (type == 4) {
                pView_yiquxiao.onRefreshComplete();
            } else if (type == 3) {
                pView_yitiche.onRefreshComplete();// 通知RefreshListView
            } else if (type == 2) {
                pView_daitiche.onRefreshComplete();// 通知RefreshListView
            } else if (type == 1) {
                pView_daifukuan.onRefreshComplete();// 通知RefreshListView
            } else {
                pView_all.onRefreshComplete();// 通知RefreshListView
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 按照类型转换时间格式
     *
     * @param time
     * @param type
     * @return
     */
    private String timetotime(String time, String type) {

        time = time.replace(" ", "&");

        String datestr = time.split("&")[0];
        String timestr = time.split("&")[1];

        String year = datestr.split("-")[0];
        String month = datestr.split("-")[1];
        String day = datestr.split("-")[2];

        String hour = timestr.split(":")[0];
        String fen = timestr.split(":")[1];

        if ("0".equals(type)) {
            time = month + "/" + day + " " + hour + ":" + fen;

        } else if ("1".equals(type)) {
            time = month + "/" + day;

        } else {
            time = year + "/" + month + "/" + day;
        }

        return time;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;

        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

        switch (arg0.getId()) {
            case R.id.carrestlist_back:
                finish();
                break;

            default:
                break;
        }

    }

    /**
     * 顶部选择按键
     *
     * @author qtds
     */
    private class topTabClick implements OnClickListener {
        private int index = 0;

        public topTabClick(int i) {
            index = i;
        }

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            viewPager.setCurrentItem(index);
        }
    }

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

    /**
     * 页面滑动回调
     *
     * @author qtds
     */
    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            // Animation animation = null;
            initChooseView();
            switch (arg0) {
                case 0:
                    type = 0;
                    allView.setTextColor(getResources().getColor(R.color.textgreen));
                    allView_line.setVisibility(View.VISIBLE);
                    getData(true);
                    stopConfirm();
                    break;
                case 1:
                    type = 1;
                    isdaifukuanfrist = false;
                    getData(true);
                    daifukuanView.setTextColor(getResources().getColor(R.color.textgreen));
                    daifukuanView_line.setVisibility(View.VISIBLE);
                    stopConfirm();
                    break;
                case 2:
                    type = 2;
                    getData(true);
                    daiticheView.setTextColor(getResources().getColor(R.color.textgreen));
                    daiticheView_line.setVisibility(View.VISIBLE);
                    stopConfirm();
                    break;
                case 3:
                    type = 3;
                    yiticheView.setTextColor(getResources().getColor(R.color.textgreen));
                    yiticheView_line.setVisibility(View.VISIBLE);
                    getData(true);
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (data_yitiche.size() > 0) {
                                Map<String, String> yiticheMap = data_yitiche.get(0);  //根据还车里程跳转
                                //获取订单号码
                                final String order_no = yiticheMap.get("order_no");
                                Map<String, String> map = new HashMap<>();
                                map.put("act", Constent.ACT_ORDER_GET_DETAIL);
                                map.put("order_no", order_no);
                                AnsynHttpRequest.httpRequest(CarRestlistActivity.this, AnsynHttpRequest.GET,
                                        callBack, Constent.ID_ORDER_GET_DETAIL, map, false, false, false);

                            }
                            handler.postDelayed(this, 1200);
                        }

                    };
                    handler.postDelayed(runnable, 1200);
                    break;
                case 4:
                    type = 4;
                    getData(true);
                    yiquxiaoView.setTextColor(getResources().getColor(R.color.textgreen));
                    yiquxiaoView_line.setVisibility(View.VISIBLE);
                    stopConfirm();
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    /**
     * 停止访问状态
     */
    private void stopConfirm() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
            runnable = null;
        }
    }

    /**
     * 初始化头部的五个按钮
     */
    private void initChooseView() {

        allView.setTextColor(getResources().getColor(R.color.gray));
        allView_line.setVisibility(View.GONE);
        daifukuanView.setTextColor(getResources().getColor(R.color.gray));
        daifukuanView_line.setVisibility(View.GONE);
        daiticheView.setTextColor(getResources().getColor(R.color.gray));
        daiticheView_line.setVisibility(View.GONE);
        yiticheView.setTextColor(getResources().getColor(R.color.gray));
        yiticheView_line.setVisibility(View.GONE);
        yiquxiaoView.setTextColor(getResources().getColor(R.color.gray));
        yiquxiaoView_line.setVisibility(View.GONE);

    }

    /**
     * 查看详情
     */
    private Callback inforCallback = new Callback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {
                int i = Integer.parseInt((String) v.getTag());
                Map<String, String> map;

                if (type == 4) {
                    map = data_yiquxiao.get(i);
                } else if (type == 3) {
                    map = data_yitiche.get(i);
                } else if (type == 2) {
                    map = data_daitiche.get(i);
                } else if (type == 1) {
                    map = data_daifukuan.get(i);
                } else {
                    map = data_all.get(i);
                }
                Intent intent = new Intent(CarRestlistActivity.this, CarRestListDetailActivity.class);
                if ("0".equals(map.get("order_state"))) { // 待支付
                    intent.putExtra("type", "list");
                } else { // 已支付
                    intent = new Intent(CarRestlistActivity.this, CarRestListDetail2Activity.class);
                }
                PublicUtil.setStorage_string(CarRestlistActivity.this, "order_no", map.get("order_no"));
                intent.putExtra("order_no", map.get("order_no"));
                startActivity(intent);
            }

        }
    };

    /**
     * 续租申请
     */
    private Callback xuzuCallback = new Callback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {
                int i = Integer.parseInt((String) v.getTag());
                String danhao = null;
                String zujin = null;
                String hc_time = null;

                if (type == 4) {
                    danhao = data_yiquxiao.get(i).get("order_no");
                    zujin = data_yiquxiao.get(i).get("hour_price");
                    hc_time = data_yiquxiao.get(i).get("hc_time2");
                } else if (type == 3) {
                    danhao = data_yitiche.get(i).get("order_no");
                    zujin = data_yitiche.get(i).get("hour_price");
                    hc_time = data_yitiche.get(i).get("hc_time2");

                } else if (type == 2) {
                    danhao = data_daitiche.get(i).get("order_no");
                    zujin = data_daitiche.get(i).get("hour_price");
                    hc_time = data_daitiche.get(i).get("hc_time2");
                } else if (type == 1) {
                    danhao = data_daifukuan.get(i).get("order_no");
                    zujin = data_daifukuan.get(i).get("hour_price");
                    hc_time = data_daifukuan.get(i).get("hc_time2");
                } else {
                    danhao = data_all.get(i).get("order_no");
                    zujin = data_all.get(i).get("hour_price");
                    hc_time = data_all.get(i).get("hc_time2");
                }
                if (!TextUtils.isEmpty(danhao)) {
                    Intent intent = new Intent(CarRestlistActivity.this, CarxuzuActivity.class);
                    intent.putExtra("danhao", danhao);
                    intent.putExtra("zujin", zujin);
                    intent.putExtra("hc_time", hc_time);
                    startActivityForResult(intent, 0);
                }

            }
        }
    };

    /**
     * 他人代取
     */
    private Callback tarendqCallback = new Callback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {
                int i = Integer.parseInt((String) v.getTag());
                String danhao = null;
                String take_car_person_name = "", take_car_person_mobile = "";
                if (type == 4) {
                    danhao = data_yiquxiao.get(i).get("order_no");
                    take_car_person_name = data_yiquxiao.get(i).get("take_car_person_name");
                    take_car_person_mobile = data_yiquxiao.get(i).get("take_car_person_mobile");
                } else if (type == 3) {
                    danhao = data_yitiche.get(i).get("order_no");
                    take_car_person_name = data_yitiche.get(i).get("take_car_person_name");
                    take_car_person_mobile = data_yitiche.get(i).get("take_car_person_mobile");
                } else if (type == 2) {
                    danhao = data_daitiche.get(i).get("order_no");
                    take_car_person_name = data_daitiche.get(i).get("take_car_person_name");
                    take_car_person_mobile = data_daitiche.get(i).get("take_car_person_mobile");
                } else if (type == 1) {
                    danhao = data_daifukuan.get(i).get("order_no");
                    take_car_person_name = data_daifukuan.get(i).get("take_car_person_name");
                    take_car_person_mobile = data_daifukuan.get(i).get("take_car_person_mobile");
                } else {
                    danhao = data_all.get(i).get("order_no");
                    take_car_person_name = data_all.get(i).get("take_car_person_name");
                    take_car_person_mobile = data_all.get(i).get("take_car_person_mobile");
                }
                if (!TextUtils.isEmpty(danhao)) {

                    if (TextUtils.isEmpty(take_car_person_mobile) || TextUtils.isEmpty(take_car_person_name)) {
                        daiqu(danhao);
                    } else {
                        daiquInfor(danhao, take_car_person_name, take_car_person_mobile);
                    }

                }

            }
        }
    };
    /**
     * 立即付款
     */
    private Callback lijifkCallback = new Callback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {
                int i = Integer.parseInt((String) v.getTag());
                String danhao = null;
                Map<String, String> map;

                if (type == 4) {
                    map = data_yiquxiao.get(i);
                } else if (type == 3) {
                    map = data_yitiche.get(i);
                } else if (type == 2) {
                    map = data_daitiche.get(i);
                } else if (type == 1) {
                    map = data_daifukuan.get(i);
                } else {
                    map = data_all.get(i);
                }

                danhao = map.get("order_no");

                Intent intent = new Intent(CarRestlistActivity.this, CarRestListDetailActivity.class);
                if ("0".equals(map.get("order_state"))) {
                    intent.putExtra("type", "list");
                } else {
                    intent = new Intent(CarRestlistActivity.this, CarRestListDetail2Activity.class);
                }
                intent.putExtra("order_no", danhao);
                startActivity(intent);

            }
        }
    };
    /**
     * 换车
     */
    private Callback shenqinghcCallback = new Callback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub

            if (v.getTag() != null) {
                int i = Integer.parseInt((String) v.getTag());
                String danhao = null, hc_time;
                if (type == 4) {
                    danhao = data_yiquxiao.get(i).get("order_no");
                    hc_time = data_yiquxiao.get(i).get("hc_time2");
                } else if (type == 3) {
                    danhao = data_yitiche.get(i).get("order_no");
                    hc_time = data_yitiche.get(i).get("hc_time2");
                    qc_time = data_yitiche.get(i).get("qc_time2");

                } else if (type == 2) {
                    danhao = data_daitiche.get(i).get("order_no");
                    hc_time = data_daitiche.get(i).get("hc_time2");
                } else if (type == 1) {
                    danhao = data_daifukuan.get(i).get("order_no");
                    hc_time = data_daifukuan.get(i).get("hc_time2");
                } else {
                    danhao = data_all.get(i).get("order_no");
                    hc_time = data_all.get(i).get("hc_time2");
                }

                if (!TextUtils.isEmpty(danhao)) {
                    Intent intent = new Intent(CarRestlistActivity.this, CarhuancheActivity.class);
                    intent.putExtra("danhao", danhao);
                    intent.putExtra("hc_time", hc_time);
                    intent.putExtra("qc_time2", qc_time);
                    startActivityForResult(intent, 0);
                }

            }

        }
    };

    /**
     * 取消d订单
     */
    private Callback quxiaodingdanCallback = new Callback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub

            if (v.getTag() != null) {
                int i = Integer.parseInt((String) v.getTag());
                String danhao = null;
                if (type == 4) {
                    danhao = data_yiquxiao.get(i).get("order_no");
                } else if (type == 3) {
                    danhao = data_yitiche.get(i).get("order_no");
                } else if (type == 2) {
                    danhao = data_daitiche.get(i).get("order_no");
                } else if (type == 1) {
                    danhao = data_daifukuan.get(i).get("order_no");
                } else {
                    danhao = data_all.get(i).get("order_no");
                }
                if (!TextUtils.isEmpty(danhao)) {
                    quxiao(danhao);
                }

            }

        }
    };

    /**
     * 取消订单
     *
     * @param danhao
     */
    private void quxiao(final String danhao) {

        View view = LayoutInflater.from(this).inflate(R.layout.view_quxiaodingdan_dialog, null);
        TextView danhaoView;
        TextView cancelView, okView;

        danhaoView = (TextView) view.findViewById(R.id.view_quxiaodingdan_dialog_danhao);
        cancelView = (TextView) view.findViewById(R.id.view_quxiaodingdan_dialog_cancel);
        okView = (TextView) view.findViewById(R.id.view_quxiaodingdan_dialog_ok);

        danhaoView.setText(danhao);

        final AlertDialog alertDialog = new AlertDialog.Builder(CarRestlistActivity.this).setView(view).show();

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        cancelView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
        okView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                if (alertDialog != null) {
                    alertDialog.dismiss();
                }

                // 这里差取消订单的协议

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ORDER_CANCEL_ORDER);
                map.put("order_no", PublicUtil.toUTF(danhao));

                AnsynHttpRequest.httpRequest(CarRestlistActivity.this, AnsynHttpRequest.GET, callBack,
                        Constent.ID_ORDER_CANCEL_ORDER, map, false, true, true);

            }
        });

    }

    /**
     * 他人代取
     *
     * @param danhao
     */

    private void daiqu(final String danhao) {

        View view = LayoutInflater.from(this).inflate(R.layout.view_tarendaiqu_dialog, null);
        TextView danhaoView;
        TextView cancelView, okView;
        final EditText nameEditText, phoneEditText;

        danhaoView = (TextView) view.findViewById(R.id.view_tarendaiqu_dialog_danhao);
        cancelView = (TextView) view.findViewById(R.id.view_tarendaiqu_dialog_cancel);
        okView = (TextView) view.findViewById(R.id.view_tarendaiqu_dialog_ok);
        nameEditText = (EditText) view.findViewById(R.id.view_tarendaiqu_dialog_qucheren);
        phoneEditText = (EditText) view.findViewById(R.id.view_tarendaiqu_dialog_phone);

        danhaoView.setText(danhao);

        final AlertDialog alertDialog = new AlertDialog.Builder(CarRestlistActivity.this).setView(view).show();

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        cancelView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });
        okView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub

                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                if (TextUtils.isEmpty(name)) {

                    PublicUtil.showToast(CarRestlistActivity.this, "请输入代取车人姓名", false);
                    return;

                }

                if (TextUtils.isEmpty(phone)) {

                    PublicUtil.showToast(CarRestlistActivity.this, "请输入代取车人联系电话", false);
                    return;

                }

                if (!PublicUtil.isMobileNO(phone)) {

                    PublicUtil.showToast(CarRestlistActivity.this, "手机号格式有误，请重新输入", false);
                    return;

                }
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ORDER_AGENT);
                map.put("order_no", PublicUtil.toUTF(danhao));
                map.put("name", PublicUtil.toUTF(name));
                map.put("mobile", PublicUtil.toUTF(phone));

                AnsynHttpRequest.httpRequest(CarRestlistActivity.this, AnsynHttpRequest.GET, callBack,
                        Constent.ID_ORDER_AGENT, map, false, true, true);

            }
        });

    }

    private void flushOrederStatus() {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_ORDER_LIST);
        map.put("order_state", "2");
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_LIST2, map, false, false,  //已提车
                true);

        Map<String, String> map1 = new HashMap<String, String>();                 //待提车
        map1.put("act", Constent.ACT_ORDER_LIST);
        map1.put("order_state", "1");
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_LIST3, map1, false, false,
                true);
        Map<String, String> map2 = new HashMap<String, String>();                 //待付款
        map2.put("act", Constent.ACT_ORDER_LIST);
        map2.put("order_state", "0");
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_LIST4, map2, false, false,
                true);
    }

    /**
     * 他人代取信息展示
     *
     * @param danhao
     */

    private void daiquInfor(String danhao, String name, String phone) {

        View view = LayoutInflater.from(this).inflate(R.layout.view_tarendaiquinfor_dialog, null);
        TextView danhaoView, nameView, phoneView;
        TextView okView;

        danhaoView = (TextView) view.findViewById(R.id.view_tarendaiquinfor_dialog_danhao);
        nameView = (TextView) view.findViewById(R.id.view_tarendaiquinfor_dialog_qucheren);
        phoneView = (TextView) view.findViewById(R.id.view_tarendaiquinfor_dialog_phone);
        okView = (TextView) view.findViewById(R.id.view_tarendaiquinfor_dialog_ok);

        danhaoView.setText(danhao);
        nameView.setText(name);
        phoneView.setText(phone);

        final AlertDialog alertDialog = new AlertDialog.Builder(CarRestlistActivity.this).setView(view).show();

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        okView.setOnClickListener(new OnClickListener() {

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
