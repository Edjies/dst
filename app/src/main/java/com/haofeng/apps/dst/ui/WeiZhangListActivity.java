package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.WeiZhangListAdapter;
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
 * 我的违章列表
 *
 * @author qtds
 */
public class WeiZhangListActivity extends BaseActivity implements
        OnClickListener {
    private static String TAG = "WeiZhangListActivity";
    private FrameLayout topLayout;
    private ListView listView;
    private PullToRefreshListView pullToRefreshListView;
    private WeiZhangListAdapter weiListAdapter;
    private TextView backTextView;
    private boolean isDownFlush = false;// 是否是下拉刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weizhanglist);
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
        topLayout = (FrameLayout) findViewById(R.id.weizhanglist_top_layout);
        setTopLayoutPadding(topLayout);
        backTextView = (TextView) findViewById(R.id.weizhanglist__back);
        backTextView.setOnClickListener(this);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.weizhanglist_listview);
        View view = LayoutInflater.from(this).inflate(
                R.layout.view_listview_empty_show_congzhi, null);
        TextView emptyinforView = (TextView) view
                .findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无违章记录");
        pullToRefreshListView.setEmptyView(view);
        pullToRefreshListView.setMode(Mode.BOTH);
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

                // data.clear();
                //
                // weizhanglistAdapter.notifyDataSetChanged();//
                // 这里下拉重新加载数据时，清楚原来数据的动作放在那？需要考虑
                isDownFlush = true;
                pullToRefreshListView.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

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
                pullToRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                        // 我们已经更新完成

                    }
                }, 1000);

            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        weiListAdapter = new WeiZhangListAdapter(WeiZhangListActivity.this);
        listView.setAdapter(weiListAdapter);

        getData(true);
    }

    private String phone;

    /**
     * 获取数据
     */
    public void getData(boolean isShowDialog) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_MEMBER_MY_WZ);

        AnsynHttpRequest.httpRequest(WeiZhangListActivity.this,
                AnsynHttpRequest.GET, callBack, Constent.ID_MEMBER_MY_WZ, map,
                false, isShowDialog, true);

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
                case Constent.ID_MEMBER_MY_WZ:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(httprequestsuccess);

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

    private int httprequesterror = 0x8600;
    private int httprequestsuccess = 0x8601;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg != null && msg.what == httprequesterror) {
                pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                if (msg.obj != null) {
                    PublicUtil.showToast(WeiZhangListActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                if (jsonObject != null) {

                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            // if (jsonObject.get("total").toString() != null) {
                            // allPage = PublicUtil.getAllPage(jsonObject.get(
                            // "total").toString());
                            // }
                            JSONArray array = jsonObject.getJSONArray("data");
                            if (array != null) {
                                flushList(array);
                            }

                        } else {
                            PublicUtil.showToast(WeiZhangListActivity.this,
                                    jsonObject.get("msg").toString(), false);
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

    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

    /**
     * 刷新列表
     *
     * @param array
     */
    public void flushList(JSONArray array) {
        JSONObject object;

        try {
            if (isDownFlush) {
                isDownFlush = false;
                data.clear();
            }

            for (int i = 0; i < array.length(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                object = array.getJSONObject(i);

                map.put("date", object.getString("date"));

                map.put("area", object.getString("area"));
                map.put("money", object.getString("money"));
                map.put("plate_number", object.getString("plate_number"));
                map.put("order_no", object.getString("order_no"));
                map.put("act", object.getString("act"));
                map.put("wz_foregift_state",
                        object.getString("wz_foregift_state"));

                PublicUtil.logDbug(TAG, map.toString(), 0);
                data.add(map);
            }
            weiListAdapter.setData(data);
            weiListAdapter.notifyDataSetChanged();
            pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
            // 我们已经更新完成

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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
            case R.id.weizhanglist__back:

                finish();
                break;

            default:
                break;
        }

    }

}
