package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.ReturnYaJinReCordAdapter;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 押金退款记录
 */
public class ReturnMoneyRecordActivity extends BaseActivity {
    private List<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
    private ReturnYaJinReCordAdapter myRecordAdapter;
    private ListView showRecordListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_return_money_record);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.my_return_money_top_layout);
        setTopLayoutPadding(topLayout);
        TextView backView = (TextView) findViewById(R.id.my_return_money_record_back);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showRecordListView = (ListView) findViewById(R.id.activity_return_money_record_show);

        //获取押金退款记录
        Map<String, String> map = new HashMap<>();
        map.put("act", Constent.ACT_YAJINTIXIANRECORD);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_YAJINTIXIANRECORD, map, false, true, true);

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




    private HttpRequestCallBack callBack = new HttpRequestCallBack() {
        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
            switch (backId) {
                case Constent.ID_ACT_YAJINTIXIANRECORD:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backStr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backStr);
                                if (jsonObject.getString("errcode").equals("0")) {
                                    JSONArray dataArray = jsonObject.getJSONArray("data");
                                    recordList = new ArrayList<Map<String,String>>();
                                    if (dataArray != null && dataArray.length() > 0) {
                                        for (int i = 0; i < dataArray.length(); i++) {
                                            Map<String, String> map = new HashMap<>();
                                            String amount = dataArray.getJSONObject(i).getString("amount");
                                            map.put("amount", amount);
                                            String status = dataArray.getJSONObject(i).getString("status");
                                            map.put("status", status);
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                            String apply_time = sdf.format(new Date(Long.parseLong(dataArray.getJSONObject(i)
                                                    .getString("apply_time")) * 1000));
                                            map.put("apply_time", apply_time);
                                            String confirm_time = sdf.format(new Date(Long.parseLong(dataArray.getJSONObject(i)
                                                    .getString("confirm_time")) * 1000));
                                            map.put("confirm_time", confirm_time);
                                            String refund_way = dataArray.getJSONObject(i).getString("refund_way");
                                            map.put("refund_way", refund_way);
                                            recordList.add(map);
                                        }
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myRecordAdapter = new ReturnYaJinReCordAdapter(recordList,ReturnMoneyRecordActivity.this);
                                            showRecordListView.setAdapter(myRecordAdapter);
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
            }

        }
    };


}
