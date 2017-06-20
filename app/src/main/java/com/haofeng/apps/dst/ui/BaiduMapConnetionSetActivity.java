package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.MapConnectionSetConnectListAdapter;
import com.haofeng.apps.dst.adapter.MapConnectionSetPatternListAdapter;
import com.haofeng.apps.dst.adapter.MapConnectionSetStausListAdapter;
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

public class BaiduMapConnetionSetActivity extends BaseActivity {
    private final String TAG = "BaiduMapConnetionSetActivity";
    private FrameLayout topLayout;
    private SeekBar seekBar;
    private TextView seekTextView, backTextView, okTextView;
    private List<Map<String, String>> connectiontypeList = new ArrayList<Map<String, String>>();// 存放充电桩接口类型
    private List<Map<String, String>> patternList = new ArrayList<Map<String, String>>();// 存放充电桩接口类型
    private List<Map<String, String>> statusList = new ArrayList<Map<String, String>>();// 存放充电桩接口类型
    private String connecttype = "";// 获取的电桩接口类型
    private String charge_pattern = "";// 获取的电桩充电类型
    private String pole_status = "";// 获取的电桩状态
    private String distance = "";
    private GridView connectGridView, pattenGridView, statusGridView;
    private TextView connectTextView, pattenTextView, statusTextView;
    private MapConnectionSetPatternListAdapter patternListAdapter;
    private MapConnectionSetConnectListAdapter connectListAdapter;
    private MapConnectionSetStausListAdapter stausListAdapter;
    private LinearLayout seekLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidumapconnectionset);
        addActivity(this);
        init();
    }

    private void init() {

        topLayout = (FrameLayout) findViewById(R.id.baidumapconnectionset_top_layout);
        setTopLayoutPadding(topLayout);
        seekBar = (SeekBar) findViewById(R.id.baidumapconnectionset_seekbar);

        seekTextView = (TextView) findViewById(R.id.baidumapconnectionset_seektext);
        connectGridView = (GridView) findViewById(R.id.baidumapconnectionset_connectionggridview);
        pattenGridView = (GridView) findViewById(R.id.baidumapconnectionset_patterngridview);
        statusGridView = (GridView) findViewById(R.id.baidumapconnectionset_statusgridview);
        connectTextView = (TextView) findViewById(R.id.baidumapconnectionset_connectiongtext);
        pattenTextView = (TextView) findViewById(R.id.baidumapconnectionset_patterntext);
        statusTextView = (TextView) findViewById(R.id.baidumapconnectionset_statustext);
        backTextView = (TextView) findViewById(R.id.baidumapconnectionset_back);
        seekLayout = (LinearLayout) findViewById(R.id.baidumapconnectionset_seeklayout);
        okTextView = (TextView) findViewById(R.id.baidumapconnectionset_ok);
        okTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("connecttype", connecttype);
                intent.putExtra("charge_pattern", charge_pattern);
                intent.putExtra("pole_status", pole_status);
                intent.putExtra("distance", distance);
                setResult(1, intent);
                finish();
            }
        });
        backTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        connecttype = getIntent().getStringExtra("connecttype");
        charge_pattern = getIntent().getStringExtra("charge_pattern");
        pole_status = getIntent().getStringExtra("pole_status");
        if ("list".equals(getIntent().getStringExtra("type"))) {
            seekLayout.setVisibility(View.GONE);
        } else {
            seekLayout.setVisibility(View.VISIBLE);
            distance = getIntent().getStringExtra("distance");
            if (TextUtils.isEmpty(distance)) {
                distance = "5";
            } else {
                if (Integer.parseInt(distance) > 30) {
                    distance = "30";
                }
            }
            seekBar.setMax(30);
            seekBar.setProgress(Integer.parseInt(distance));
            seekTextView.setText(distance + "KM");
            seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onProgressChanged(SeekBar arg0, int arg1,
                                              boolean arg2) {
                    if (arg1 == 0) {
                        arg1 = 1;
                    }
                    seekTextView.setText(arg1 + "KM");
                    distance = String.valueOf(arg1);
                }
            });
        }
        connectListAdapter = new MapConnectionSetConnectListAdapter(this);
        connectGridView.setAdapter(connectListAdapter);               //接口类型
        patternListAdapter = new MapConnectionSetPatternListAdapter(this);
        pattenGridView.setAdapter(patternListAdapter);            //充电模式
        stausListAdapter = new MapConnectionSetStausListAdapter(this);
        statusGridView.setAdapter(stausListAdapter);   //充电桩状态
        getConnecttype();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 获取界面电桩配置项
     */
    public void getConnecttype() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_GETMAPCONNECTIONTYPE);
        map.put("ver", Constent.VER);
        AnsynHttpRequest.httpRequest(BaiduMapConnetionSetActivity.this,
                AnsynHttpRequest.POST, callBack,
                Constent.ID_GETMAPCONNECTIONTYPE, map, false, true, true);
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
                case Constent.ID_GETMAPCONNECTIONTYPE:

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

    private int httprequesterror = 0x4800;
    private int httprequestsuccess = 0x4801;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == httprequesterror) {
                if (msg.obj != null) {
                    PublicUtil.showToast(BaiduMapConnetionSetActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                if (jsonObject != null) {

                    try {
                        if ("0".equals(jsonObject.get("error").toString())) {
                            PublicUtil.logDbug(TAG,
                                    jsonObject.get("data") + "", 0);

                            JSONArray array = jsonObject.getJSONArray("data");

                            if (array != null && array.length() > 0) {
                                setConnectiontype(array);
                            } else {
                                PublicUtil.showToast(
                                        BaiduMapConnetionSetActivity.this,
                                        "无法获取配置类型", false);
                            }

                        } else {
                            PublicUtil.showToast(
                                    BaiduMapConnetionSetActivity.this,
                                    jsonObject.get("msg").toString(), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        PublicUtil.showToast(BaiduMapConnetionSetActivity.this,
                                "配置解析数据错误，请退出重试", false);
                    }
                }
            }

        }

        ;
    };

    private List<Map<String, Object>> listems_connectList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listems_patternList = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> listems_statusList = new ArrayList<Map<String, Object>>();

    /**
     * 设置配置类型
     */
    public void setConnectiontype(JSONArray dataarray) {

        try {

            JSONObject dataJsonObject;
            JSONArray array = null, array2 = null, array3 = null;
            for (int i = 0; i < dataarray.length(); i++) {

                dataJsonObject = dataarray.getJSONObject(i);

                if ("contype".equals(dataJsonObject.getString("key"))) {

                    connectTextView.setText(dataJsonObject.getString("text"));
                    array = dataJsonObject.getJSONArray("item");

                } else if ("charge_pattern".equals(dataJsonObject
                        .getString("key"))) {
                    pattenTextView.setText(dataJsonObject.getString("text"));
                    array2 = dataJsonObject.getJSONArray("item");
                } else if ("pole_status"
                        .equals(dataJsonObject.getString("key"))) {
                    statusTextView.setText(dataJsonObject.getString("text"));
                    array3 = dataJsonObject.getJSONArray("item");
                }

            }

            if (array != null && array.length() > 0) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject dataobject;
                for (int j = 0; j < array.length(); j++) {
                    dataobject = array.getJSONObject(j);
                    map = new HashMap<String, String>();
                    map.put("value", dataobject.getString("value"));
                    map.put("text", dataobject.getString("text"));
                    connectiontypeList.add(map);
                }
                setinfor_connect();
                connectGridView
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                // TODO Auto-generated method stub
                                connecttype = connectiontypeList.get(arg2).get(
                                        "value");
                                setinfor_connect();
                            }
                        });
            }

            if (array2 != null && array2.length() > 0) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject object2;
                for (int j = 0; j < array2.length(); j++) {
                    object2 = array2.getJSONObject(j);
                    map = new HashMap<String, String>();
                    map.put("value", object2.getString("value"));
                    map.put("text", object2.getString("text"));
                    patternList.add(map);
                }
                setinfor_pattern();
                pattenGridView
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                // TODO Auto-generated method stub
                                charge_pattern = patternList.get(arg2).get(
                                        "value");
                                setinfor_pattern();
                            }
                        });
            }

            if (array3 != null && array3.length() > 0) {
                Map<String, String> map = new HashMap<String, String>();
                JSONObject object3;
                for (int j = 0; j < array3.length(); j++) {
                    object3 = array3.getJSONObject(j);
                    map = new HashMap<String, String>();
                    map.put("value", object3.getString("value"));
                    map.put("text", object3.getString("text"));
                    statusList.add(map);
                }
                setinfor_status();
                statusGridView
                        .setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0,
                                                    View arg1, int arg2, long arg3) {
                                // TODO Auto-generated method stub
                                pole_status = statusList.get(arg2).get("value");
                                setinfor_status();
                            }
                        });
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    /**
     * 设置充电桩状态
     */
    private void setinfor_status() {

        listems_statusList.clear();
        for (int i = 0; i < statusList.size(); i++) {
            Map<String, Object> listem = new HashMap<String, Object>();

            listem.put("title", statusList.get(i).get("text"));
            listem.put("value", statusList.get(i).get("value"));

            listem.put("titlecolor", "gray");
            listem.put("image", R.drawable.cdzxiangqing4_1_12);

            if ("".equals(pole_status)
                    && "".equals(statusList.get(i).get("value"))) {
                listem.put("image", R.drawable.cdzxiangqing4_1_07);
                listem.put("titlecolor", "green");
            } else if ("1".equals(pole_status)
                    && "1".equals(statusList.get(i).get("value"))) {
                listem.put("image", R.drawable.cdzxiangqing4_1_07);
                listem.put("titlecolor", "green");
            }

            listems_statusList.add(listem);
        }
        stausListAdapter.setData(listems_statusList);
        stausListAdapter.notifyDataSetChanged();

    }

    /**
     * 设置充电类型
     */
    private void setinfor_pattern() {

        listems_patternList.clear();
        for (int i = 0; i < patternList.size(); i++) {
            Map<String, Object> listem = new HashMap<String, Object>();

            listem.put("title", patternList.get(i).get("text"));
            listem.put("value", patternList.get(i).get("value"));
            listem.put("titlecolor", "gray");
            listem.put("image", R.drawable.type_frame2);

            if ("".equals(charge_pattern)
                    && "".equals(patternList.get(i).get("value"))) {
                listem.put("image", R.drawable.cdzxiangqing4_1_07);
                listem.put("titlecolor", "white");
            } else if ("FAST_CHARGE".equals(charge_pattern)
                    && "FAST_CHARGE".equals(patternList.get(i).get("value"))) {
                listem.put("image", R.drawable.cdzxiangqing4_1_07);
                listem.put("titlecolor", "white");
            } else if ("SLOW_CHARGE".equals(charge_pattern)
                    && "SLOW_CHARGE".equals(patternList.get(i).get("value"))) {
                listem.put("image", R.drawable.cdzxiangqing4_1_07);
                listem.put("titlecolor", "white");
            }

            listems_patternList.add(listem);
        }

        patternListAdapter.setData(listems_patternList);
        patternListAdapter.notifyDataSetChanged();

    }

    /**
     * 设置接口类型
     */
    private void setinfor_connect() {
        PublicUtil.logDbug(TAG, connecttype, 0);

        listems_connectList.clear();
        for (int i = 0; i < connectiontypeList.size(); i++) {
            Map<String, Object> listem = new HashMap<String, Object>();

            listem.put("title", connectiontypeList.get(i).get("text"));
            listem.put("value", connectiontypeList.get(i).get("value"));
            listem.put("color", R.color.gray);
            if ("GB".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.gb);
            } else if ("BYD".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.bya);
            } else if ("TESLA".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.tsl);
            } else if ("".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.bx);
            }

            if ("GB".equals(connecttype)
                    && "GB".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.gdc);
                listem.put("color", R.color.textgreen);
            } else if ("BYD".equals(connecttype)
                    && "BYD".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.bycc);
                listem.put("color", R.color.textgreen);
            } else if ("TESLA".equals(connecttype)
                    && "TESLA".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.tslc);
                listem.put("color", R.color.textgreen);
            } else if ("".equals(connecttype)
                    && "".equals(connectiontypeList.get(i).get("value"))) {
                listem.put("image", R.drawable.bxc);
                listem.put("color", R.color.textgreen);
            }

            listems_connectList.add(listem);
        }

        connectListAdapter.setData(listems_connectList);
        connectListAdapter.notifyDataSetChanged();

    }

}
