package com.haofeng.apps.dst.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.MyCarAdapter;
import com.haofeng.apps.dst.application.MyApplication;
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
 * 我的车辆
 *
 * @author WIN10
 */
public class MyCarAssociationActivity extends BaseActivity implements OnClickListener {

    private TextView addCarView;
    private AlertDialog dialog;
    private Button goRentView;
    private TextView myCurrentCarLine;
    private TextView currentCatShowView;
    private TextView myHistoryCarLine;
    private TextView historyCatShowView;
    private TextView backView;
    private PullToRefreshListView pullToRefreshListView;
    private ListView freshListView;
    private List<Map<String, String>> currentCarList = new ArrayList<Map<String, String>>(); //当前车辆
    private List<Map<String, String>> historyCarList = new ArrayList<Map<String, String>>(); //过往车辆
    private static final String TAG = "MyCarAssociationActivit";
    private int type = 0; //0，当前车辆   1.过往车辆
    private AlertDialog alertDialog;
    private MyCarAdapter myCarAdapter;
    private JSONObject currentCarJsonObject;
    private FrameLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ((MyApplication) getApplication()).addActivity(this);
        initView();
        initListener();
    }

    private void initView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_mycar_association);
        addCarView = (TextView) findViewById(R.id.acitivty_mycar_add_car);
        backView = (TextView) findViewById(R.id.my_mycar_back);
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.acitivty_mycar_association_pullfresh);
        View emptyView = LayoutInflater.from(this).inflate(R.layout.view_mycar_emptyshow, null);
        goRentView = (Button) emptyView.findViewById(R.id.empty_go_rent);
        pullToRefreshListView.setEmptyView(emptyView);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
        ILoadingLayout startLabels = pullToRefreshListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        freshListView = pullToRefreshListView.getRefreshableView();
        myCurrentCarLine = (TextView) findViewById(R.id.acitivty_mycar_association_current_car_line);
        myHistoryCarLine = (TextView) findViewById(R.id.acitivty_mycar_association_history_car_line);
        currentCatShowView = (TextView) findViewById(R.id.acitivty_mycar_association_current_car);
        historyCatShowView = (TextView) findViewById(R.id.acitivty_mycar_association_history_car);
        topLayout = (FrameLayout) findViewById(R.id.acitivty_mycar_association_top_layout);
        setTopLayoutPadding(topLayout);
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
    protected void onStart() {
        super.onStart();
        type = 0;
        myCurrentCarLine.setVisibility(View.VISIBLE);
        myHistoryCarLine.setVisibility(View.GONE);
        currentCatShowView.setTextColor(getResources().getColor(R.color.textgreen));
        historyCatShowView.setTextColor(getResources().getColor(R.color.gray4));
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 1000);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshListView.setRefreshing();
            }
        }, 200);
    }

    private void initListener() {
        // TODO Auto-generated method stub
        addCarView.setOnClickListener(this);
        goRentView.setOnClickListener(this);
        currentCatShowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 颜色字体
                type = 0;
                myCurrentCarLine.setVisibility(View.VISIBLE);
                myHistoryCarLine.setVisibility(View.GONE);
                currentCatShowView.setTextColor(getResources().getColor(R.color.textgreen));
                historyCatShowView.setTextColor(getResources().getColor(R.color.gray4));
                pullToRefreshListView.setRefreshing();
            }
        });
        historyCatShowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 颜色字体
                type = 1;
                myCurrentCarLine.setVisibility(View.GONE);
                myHistoryCarLine.setVisibility(View.VISIBLE);
                currentCatShowView.setTextColor(getResources().getColor(R.color.gray4));
                historyCatShowView.setTextColor(getResources().getColor(R.color.textgreen));
                pullToRefreshListView.setRefreshing();
            }
        });

        backView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MyCarAssociationActivity.this, UserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (type == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MyCarAssociationActivity.this);
                    builder.setTitle("取消车辆关联")
                            .setMessage("确定吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("act", Constent.ACT_GET_CONCEL_ASSOCIATE);
                                    try {
                                        map.put("car_no", currentCarJsonObject.getString("car_no"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    AnsynHttpRequest.httpRequest(MyCarAssociationActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_GET_CONCEL_ASSOCIATE, map, false, false, true);
                                }
                            })
                            .setNegativeButton("否", null);
                    alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MyCarAssociationActivity.this, UserActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 获取数据
     */
    public void getData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_MY_CAR_LIST);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_MY_CAR_LIST, map, false,
                false, true);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.acitivty_mycar_add_car: // 添加车辆
                Intent addCarIntent = new Intent(this, CarAssociationActivity.class);
                startActivity(addCarIntent);
                break;
            case R.id.empty_go_rent: // 立即租车
                Intent intent = new Intent(this, DuanzuActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_ACT_MY_CAR_LIST: // 查询车辆列表
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestSuccess) {
                                freshListView.setAdapter(null);
                                currentCarList.clear();
                                historyCarList.clear();
                                if (!isString) {
                                    try {
                                        String backstr = jsonArray.getString(1);
                                        JSONObject jsonObjcet = new JSONObject(backstr);
                                        if (jsonObjcet.getString("errcode").equals("0")) {
                                            JSONObject dataObject = jsonObjcet.optJSONObject("data");
                                            if (dataObject != null) {
                                                currentCarJsonObject = dataObject.optJSONObject("new");
                                                JSONArray historyCarJsonArray = dataObject.optJSONArray("old");
                                                if (currentCarJsonObject != null && type == 0) {  //当前车辆
                                                    Map<String, String> map = new HashMap<String, String>();
                                                    map.put("car_type", currentCarJsonObject.getString("car_type"));
                                                    map.put("car_no", currentCarJsonObject.getString("car_no"));
                                                    map.put("start_time", PublicUtil.stampToDate(currentCarJsonObject.getString("start_time")));
                                                    map.put("img", currentCarJsonObject.getString("img"));
                                                    currentCarList.add(map);
                                                    myCarAdapter = new MyCarAdapter(currentCarList, type, MyCarAssociationActivity.this);
                                                    freshListView.setAdapter(myCarAdapter);
                                                } else if (type == 1 && historyCarJsonArray != null && historyCarJsonArray.length() > 0) {  //过往车辆
                                                    for (int i = 0; i < historyCarJsonArray.length(); i++) {
                                                        Map<String, String> map = new HashMap<String, String>();
                                                        JSONObject historySingleCarDetailObject = (JSONObject) historyCarJsonArray.get(i);
                                                        map.put("car_type", historySingleCarDetailObject.getString("car_type"));
                                                        map.put("car_no", historySingleCarDetailObject.getString("car_no"));
                                                        map.put("start_time", PublicUtil.stampToDate(historySingleCarDetailObject.getString("start_time")));
                                                        map.put("img", historySingleCarDetailObject.getString("img"));
                                                        map.put("end_time", PublicUtil.stampToDate(historySingleCarDetailObject.getString("end_time")));
                                                        historyCarList.add(map);
                                                    }
                                                    freshListView.setAdapter(new MyCarAdapter(historyCarList, type, MyCarAssociationActivity.this));
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                }
                            }
                            pullToRefreshListView.onRefreshComplete();
                        }
                    });
                    break;
                case Constent.ID_ACT_GET_CONCEL_ASSOCIATE:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backStr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backStr);
                                if (jsonObject.getString("errcode").equals("0")) {
                                    handler.sendEmptyMessage(1);
                                } else {
                                    handler.sendEmptyMessage(2);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                default:
                    break;
            }

        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Toast.makeText(MyCarAssociationActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
                freshListView.setAdapter(null);
            } else if (msg.what == 2) {
                Toast.makeText(MyCarAssociationActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        }
    };

}
