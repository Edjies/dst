package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class NichengBJActivity extends BaseActivity implements OnClickListener {
    private TextView titleView, backTextView, okView;
    private FrameLayout topLayout;
    private ImageView cleanView;
    private EditText editText;
    private String type = "1";
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nichengbj);
        addActivity(this);
        topLayout = (FrameLayout) findViewById(R.id.nichengbj_top_layout);
        setTopLayoutPadding(topLayout);
        titleView = (TextView) findViewById(R.id.nichengbj_title);
        backTextView = (TextView) findViewById(R.id.nichengbj_back);
        okView = (TextView) findViewById(R.id.nichengbj_ok);
        cleanView = (ImageView) findViewById(R.id.nichengbj_clean);
        editText = (EditText) findViewById(R.id.nichengbj_edit);
        type = getIntent().getStringExtra("type");
        editText.setText(getIntent().getStringExtra("name"));
        if ("2".equals(type)) {
            titleView.setText("社区昵称");
        } else {
            titleView.setText("工作单位");
        }
        backTextView.setOnClickListener(this);
        cleanView.setOnClickListener(this);
        okView.setOnClickListener(this);
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

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.nichengbj_clean:
                editText.setText("");

                break;
            case R.id.nichengbj_back:
                finish();

                break;
            case R.id.nichengbj_ok:
                name = editText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    if ("2".equals(type)) {
                        PublicUtil.showToast(NichengBJActivity.this, "社区昵称不能为空",
                                false);
                    } else {
                        PublicUtil.showToast(NichengBJActivity.this, "工作单位不能为空",
                                false);
                    }

                    return;
                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_MEMBER_UPDATE_INFOR);

                if ("2".equals(type)) {
                    map.put("nickname", PublicUtil.toUTF(name));

                } else {
                    map.put("work_units", PublicUtil.toUTF(name));
                }

                AnsynHttpRequest.httpRequest(NichengBJActivity.this,
                        AnsynHttpRequest.GET, callBack,
                        Constent.ID_MEMBER_UPDATE_INFOR, map, false, true, true);

                break;

            default:
                break;
        }

    }

    private JSONObject jsonObject = null;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess,
                         boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub

            switch (backId) {
                case Constent.ID_MEMBER_UPDATE_INFOR:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
                                handler.sendEmptyMessage(success_http);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;

                default:
                    break;
            }

        }
    };

    private int error_http = 0x9200;
    private int success_http = 0x9201;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_http) {
                if (msg.obj != null) {
                    PublicUtil.showToast(NichengBJActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            if ("2".equals(type)) {
                                PublicUtil.setStorage_string(
                                        NichengBJActivity.this, "user_nicheng",
                                        name);
                            } else {
                                PublicUtil.setStorage_string(
                                        NichengBJActivity.this, "user_danwei",
                                        name);
                            }

                            Intent intent = new Intent();
                            intent.putExtra("type", type);
                            intent.putExtra("name", name);
                            NichengBJActivity.this.setResult(31, intent);
                            finish();

                        } else {
                            PublicUtil.showToast(NichengBJActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(
                                        NichengBJActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            }

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

}
