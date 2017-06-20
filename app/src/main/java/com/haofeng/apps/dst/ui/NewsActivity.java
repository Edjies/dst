package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.haofeng.apps.dst.adapter.NewsListAdapter;
import com.haofeng.apps.dst.adapter.NewsListAdapter.Callback;
import com.haofeng.apps.dst.adapter.NewsList_ListviewAdapter.ItemCallback;
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
 * 资讯信息
 *
 * @author Administrator
 */
public class NewsActivity extends BaseActivity implements OnClickListener {
    private FrameLayout topLayout;
    private ListView listView;
    private String phone;
    private NewsListAdapter newsListAdapter;
    private PullToRefreshListView pullToRefreshListView;
    private TextView backTextView;
    private boolean isDownFlush = false;// 是否是下拉刷新
    private String last_search_day = "";// 获取数据不是根据条数，二十根据时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
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

    private void init() {
        topLayout = (FrameLayout) findViewById(R.id.news_top_layout);
        setTopLayoutPadding(topLayout);
        backTextView = (TextView) findViewById(R.id.news_back);
        backTextView.setOnClickListener(this);

        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.news_listview);
        View view = LayoutInflater.from(this).inflate(
                R.layout.view_listview_empty_show_congzhi, null);
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

                isDownFlush = true;
                last_search_day = "";

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
                        getData(false);
                    }
                }, 1000);

            }
        });

        listView = pullToRefreshListView.getRefreshableView();
        newsListAdapter = new NewsListAdapter(NewsActivity.this, topCallback,
                itemCallback);
        listView.setAdapter(newsListAdapter);

        getData(true);
        // flushList(null);

    }

    /**
     * 获取数据
     */
    public void getData(boolean isShowdialog) {
        if (phone == null) {
            phone = PublicUtil.getStorage_string(NewsActivity.this, "phone",
                    "-1");
        }

        if ("-1".equals(phone)) {

            PublicUtil.showToast(NewsActivity.this,
                    "用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("act", Constent.ACT_GETNOTICE);
            map.put("mobile", phone);
            map.put("ver", Constent.VER);
            map.put("last_search_day", last_search_day);

            AnsynHttpRequest.httpRequest(NewsActivity.this,
                    AnsynHttpRequest.POST, callBack, Constent.ID_GETNOTICE,
                    map, false, isShowdialog, true);
        }

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
                case Constent.ID_GETNOTICE:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(httprequestsuccess);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                if (handler != null) {
                                    Message message = new Message();
                                    message.what = httprequesterror;
                                    message.obj = "服务器端返回数据解析错误，请退出后重试";
                                    handler.sendMessage(message);
                                }
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

    private int httprequesterror = 0x2000;
    private int httprequestsuccess = 0x2001;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == httprequesterror) {
                pullToRefreshListView.onRefreshComplete();//
                // 通知RefreshListView
                if (msg.obj != null) {
                    PublicUtil.showToast(NewsActivity.this, msg.obj.toString(),
                            false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                pullToRefreshListView.onRefreshComplete();//
                // 通知RefreshListView
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);
                        if ("0".equals(jsonObject.get("error").toString())) {

                            // if (jsonObject.get("total").toString() != null) {
                            // allPage = PublicUtil.getAllPage(jsonObject.get(
                            // "total").toString());
                            //
                            // }
                            JSONArray array = (JSONArray) jsonObject
                                    .get("data");

                            if (array != null && array.length() > 0) {
                                flushList(array);
                            } else {
                                PublicUtil.showToast(NewsActivity.this,
                                        "解析数据错误", false);
                            }

                        } else {
                            PublicUtil.showToast(NewsActivity.this, jsonObject
                                    .get("msg").toString(), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(NewsActivity.this,
                                "配置解析数据错误，请退出重试", false);
                    }

                }

            }

        }

        ;
    };

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {

            case R.id.news_back:
                finish();
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 检查是否为返回事件，如果有网页历史记录
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    private List<Map<String, String>> itemdata = new ArrayList<Map<String, String>>();// 项目中listview中的列表总和

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
                itemdata.clear();
            }
            for (int i = 0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();

                JSONArray listArray = object.getJSONArray("list");

                if (listArray != null && listArray.length() > 0) {
                    JSONObject listObject;
                    map.put("date", object.getString("search_day"));
                    map.put("listcount", String.valueOf(listArray.length() - 1));
                    for (int j = 0; j < listArray.length(); j++) {
                        listObject = listArray.getJSONObject(j);
                        if (j == 0) {
                            map.put("i", String.valueOf(i));
                            map.put("id", listObject.getString("vn_id"));
                            map.put("topinfor",
                                    listObject.getString("vn_title"));
                            map.put("topimage",
                                    listObject.getString("vn_icon_path"));
                            map.put("urlString", listObject.getString("url"));
                        } else {

                            map.put("count" + (j - 1), String.valueOf(i));
                            map.put("list_id" + (j - 1),
                                    listObject.getString("vn_id"));
                            map.put("list_title" + (j - 1),
                                    listObject.getString("vn_title"));
                            map.put("list_image" + (j - 1),
                                    listObject.getString("vn_icon_path"));

                            Map<String, String> itemMap = new HashMap<String, String>();
                            itemMap.put("count",
                                    String.valueOf(itemdata.size()));// 因为是把全部子listview中的数据放在一个arraylist中，所以这里标志位按照list长度来设置
                            itemMap.put("list_id",
                                    listObject.getString("vn_id"));
                            itemMap.put("list_title",
                                    listObject.getString("vn_title"));
                            itemMap.put("list_image",
                                    listObject.getString("vn_icon_path"));
                            itemMap.put("urlString",
                                    listObject.getString("url"));
                            itemdata.add(itemMap);

                        }

                    }

                    if (i == array.length() - 1) {
                        last_search_day = object.getString("search_day");
                    }
                    data.add(map);
                }

            }

            newsListAdapter.setData(data);
            newsListAdapter.notifyDataSetChanged();
            pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
            // 我们已经更新完成

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 头部信息点击事件
     */
    private Callback topCallback = new Callback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {

                int i = Integer.parseInt((String) v.getTag());
                // System.out.println(i + "@@@@@" + data.get(i).get("id"));
                String urlString = data.get(i).get("urlString");

                Intent intent = new Intent(NewsActivity.this,
                        NewsInforActivity.class);
                intent.putExtra("urlString", urlString);
                startActivity(intent);
            }
        }
    };

    /**
     * list中的列表点击事件
     */
    private ItemCallback itemCallback = new ItemCallback() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub

            if (v.getTag() != null) {

                int i = Integer.parseInt((String) v.getTag());
                // System.out
                // .println(i + "@@@@@" + itemdata.get(i).get("list_id"));
                String urlString = itemdata.get(i).get("urlString");

                Intent intent = new Intent(NewsActivity.this,
                        NewsInforActivity.class);
                intent.putExtra("urlString", urlString);
                startActivity(intent);
            }
        }
    };

}
