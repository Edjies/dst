package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

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

public class CityStoresListActivity extends BaseActivity implements
        OnClickListener {
    private final String TAG = "CityStoresListActivity";
    private FrameLayout topLayout;
    private FrameLayout dizhiLayout;
    private TextView dizhiView;
    private TextView backView;
    private ListView listView;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private String city_id, city_name;
    private final int REQUESTCODE = 0x8010;
    private SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_citystoreslist);
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

    /*
     * 初始化组件
     */
    public void init() {
        topLayout = (FrameLayout) findViewById(R.id.citystore_top_layout);
        setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.citystore_backtextview);
        listView = (ListView) findViewById(R.id.citystore_listview);
        dizhiLayout = (FrameLayout) findViewById(R.id.citystore_dizhi_layout);
        dizhiView = (TextView) findViewById(R.id.citystore_dizhiview);

        backView.setOnClickListener(this);
        dizhiLayout.setOnClickListener(this);
        city_id = PublicUtil.getStorage_string(CityStoresListActivity.this,
                "city_id", "77");
        city_name = PublicUtil.getStorage_string(CityStoresListActivity.this,
                "city_name", "深圳");
        if (!TextUtils.isEmpty(city_name)) {
            dizhiView.setText(city_name);
        } else {
            dizhiView.setText("请选择");
        }

        if (!TextUtils.isEmpty(city_id)) {
            getdata(city_id);
        }

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                String id = list.get((int) arg3).get("id");
                String name = list.get((int) arg3).get("store_name");
                String addr = list.get((int) arg3).get("addr");

                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("addr", addr);
                CityStoresListActivity.this.setResult(12, intent);
                finish();
            }
        });

        simpleAdapter = new SimpleAdapter(CityStoresListActivity.this, list,
                R.layout.listview_item_citystores, new String[]{"id",
                "store_name", "addr"}, new int[]{
                R.id.list_item_citystors_id,
                R.id.list_item_citystors_name,
                R.id.list_item_citystors_addr});
        listView.setAdapter(simpleAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUESTCODE) {

            if (resultCode == 11) {

                city_id = data.getStringExtra("id");
                city_name = data.getStringExtra("name");
                list.clear();
                getdata(city_id);
                dizhiView.setText(city_name);

            }

        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.citystore_backtextview:

                finish();
                break;
            case R.id.citystore_dizhi_layout:
                Intent intent = new Intent(CityStoresListActivity.this,
                        AddrListActivity.class);
                startActivityForResult(intent, REQUESTCODE);
                break;

            default:
                break;
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

    private JSONObject jsonObject = null;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_CAR_GET_STORE_LIST:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_http_getstores);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                if (handler != null) {
                                    Message message = new Message();
                                    message.what = error_http_getstores;
                                    message.obj = "服务器端返回数据解析错误，请退出后重试";
                                    handler.sendMessage(message);
                                }
                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getstores;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;

                default:
                    break;
            }

        }
    };

    private int error_http_getstores = 0x8000;
    private int success_http_getstores = 0x8001;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_http_getstores) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CityStoresListActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getstores) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");

                            if (dataArray != null) {
                                flushlist(dataArray);
                            }

                        } else if ("1002".equals(jsonObject
                                .getString("errcode"))) {
                            PublicUtil.showToast(CityStoresListActivity.this,
                                    jsonObject.getString("msg"), false);
                            Intent intent = new Intent(
                                    CityStoresListActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);

                        } else {
                            PublicUtil.showToast(CityStoresListActivity.this,
                                    jsonObject.getString("msg"), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(CityStoresListActivity.this,
                                "配置解析数据错误，请退出重试", false);
                        e.printStackTrace();
                    }

                }

            }

        }

        ;
    };

    /**
     * 获取数据
     *
     * @param id
     */
    private void getdata(String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_CAR_GET_STORE_LIST);
        map.put("ver", Constent.VER);
        map.put("city_id", id);

        AnsynHttpRequest.httpRequest(CityStoresListActivity.this,
                AnsynHttpRequest.GET, callBack, Constent.ID_CAR_GET_STORE_LIST,
                map, false, true, true);
    }

    private void flushlist(JSONArray dataArray) {
        list.clear();
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();

                map.put("id", object.getString("id"));
                map.put("store_name", object.getString("store_name"));
                map.put("addr", object.getString("addr"));

                list.add(map);

            }
            simpleAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
