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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.AdderListAdapter;
import com.haofeng.apps.dst.adapter.AdderListAdapter.AddrViewHolder;
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

public class AddrListActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "AddrListActivity";
    private FrameLayout topLayout;
    private TextView backView;
    private ListView listView;
    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private AdderListAdapter adapter;
    private TextView mycityView;
    private LinearLayout cityLayout;
    private String type = "zuche";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrlist);
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

        topLayout = (FrameLayout) findViewById(R.id.addrlist_top_layout);
        setTopLayoutPadding(topLayout);

        backView = (TextView) findViewById(R.id.addrlist_back);

        cityLayout = (LinearLayout) findViewById(R.id.addrlist_citylayout);
        mycityView = (TextView) findViewById(R.id.addrlist_mycity);

        listView = (ListView) findViewById(R.id.addrlist_list2);

        backView.setOnClickListener(this);
        mycityView.setOnClickListener(this);

        String city_name = PublicUtil.getStorage_string(AddrListActivity.this,
                "city_name", "深圳");

        mycityView.setText(city_name);

        type = getIntent().getStringExtra("type");

        adapter = new AdderListAdapter(AddrListActivity.this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub

                AddrViewHolder viewHolder = (AddrViewHolder) arg1.getTag();

                String id = viewHolder.idView.getText().toString();
                String name = viewHolder.addrView.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                AddrListActivity.this.setResult(11, intent);
                finish();
            }
        });
        backView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getdata();
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.addrlist_back:
                finish();
                break;

            case R.id.addrlist_mycity:
                String id = PublicUtil.getStorage_string(AddrListActivity.this,
                        "city_id", "77");
                String name = PublicUtil.getStorage_string(AddrListActivity.this,
                        "city_name", "深圳");

                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                AddrListActivity.this.setResult(11, intent);
                finish();
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
                case Constent.ID_REGION_INDEX:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                if (!TextUtils.isEmpty(backstr)) {
                                    jsonObject = new JSONObject(backstr);
                                    handler.sendEmptyMessage(success_http_getcardetail);
                                }

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getcardetail;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;

                default:
                    break;
            }

        }
    };

    private int error_http_getcardetail = 0x5500;
    private int success_http_getcardetail = 0x5501;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_http_getcardetail) {
                if (msg.obj != null) {
                    PublicUtil.showToast(AddrListActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getcardetail) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONArray dataArray = jsonObject
                                    .getJSONArray("data");

                            if (dataArray != null && dataArray.length() > 0) {
                                flushlist(dataArray);
                            }

                        } else if ("1002".equals(jsonObject
                                .getString("errcode"))) {
                            PublicUtil.showToast(AddrListActivity.this,
                                    jsonObject.getString("msg"), false);
                            Intent intent = new Intent(AddrListActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);

                        } else {
                            PublicUtil.showToast(AddrListActivity.this,
                                    jsonObject.getString("msg"), false);
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

    /**
     * 获取数据
     *
     */
    private void getdata() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_REGION_INDEX);

        if (!"map".equals(type)) {

            map.put("rental", "1");
        }

        AnsynHttpRequest.httpRequest(AddrListActivity.this,
                AnsynHttpRequest.GET, callBack, Constent.ID_REGION_INDEX, map,
                false, true, true);
    }

    private void flushlist(JSONArray dataArray) {
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();

                map.put("ischoose", "1");
                map.put("id", object.getString("id"));
                map.put("name", object.getString("name"));
                map.put("type", object.getString("type"));

                list.add(map);

            }

            adapter.setData(list);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
