package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
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

public class SuggestActivity extends BaseActivity implements OnClickListener {
    // private static String TAG = "SuggestActivity";
    private FrameLayout topLayout;
    private EditText contentEditText;
    private Button okButton;
    private TextView backTextView, titlTextView;
    private String typeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        ((MyApplication) getApplication()).addActivity(this);
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
        topLayout = (FrameLayout) findViewById(R.id.suggest_top_layout);
        setTopLayoutPadding(topLayout);
        contentEditText = (EditText) findViewById(R.id.suggest_mark);
        okButton = (Button) findViewById(R.id.suggest_okbutton);
        backTextView = (TextView) findViewById(R.id.suggest_back);
        titlTextView = (TextView) findViewById(R.id.suggest_title);
        backTextView.setOnClickListener(this);
        okButton.setOnClickListener(this);
        typeString = getIntent().getStringExtra("type");

        if ("jiucuo".equals(typeString)) {
            titlTextView.setText("充电站信息纠错");
            contentEditText.setHint("请描述这个充电站出了什么问题");
        } else {
            titlTextView.setText("意见反馈");
            contentEditText.setHint("您有什么意见或者建议，请告诉地上铁吧！");
        }
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (contentEditText.getText().toString().length() >= 10) {
                    GradientDrawable gradientDrawable = (GradientDrawable) okButton.getBackground();
                    gradientDrawable.setColor(getResources().getColor(R.color.textgreen));
                } else {
                    GradientDrawable gradientDrawable = (GradientDrawable) okButton.getBackground();
                    gradientDrawable.setColor(getResources().getColor(R.color.gray));
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                case Constent.ID_ADDSUGGEST:
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

    private int httprequesterror = 0x1900;
    private int httprequestsuccess = 0x1901;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg != null && msg.what == httprequesterror) {
                if (msg.obj != null) {
                    PublicUtil.showToast(SuggestActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                if (jsonObject != null) {

                    try {
                        PublicUtil.showToast(SuggestActivity.this, jsonObject
                                .get("msg").toString(), false);
                        if ("0".equals(jsonObject.get("error").toString())) {
                            finish();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(SuggestActivity.this,
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
            case R.id.suggest_back:
                finish();
                break;
            case R.id.suggest_okbutton:

                // TODO Auto-generated method stub
                String content = contentEditText.getText().toString();
                if (content == null || TextUtils.isEmpty(content)) {
                    PublicUtil.showToast(SuggestActivity.this, "请输入意见内容", false);
                    return;
                }
                if (content.length() < 10) {
                    PublicUtil.showToast(this, "至少输入10个字符", false);
                    return;
                }
                String phone = PublicUtil.getStorage_string(SuggestActivity.this,
                        "phone", "-1");
                if ("-1".equals(phone)) {

                    PublicUtil.showToast(SuggestActivity.this,
                            "用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);
                    return;

                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ADDSUGGEST);
                map.put("mobile", phone);
                map.put("content", content);
                map.put("ver", Constent.VER);

                AnsynHttpRequest.httpRequest(SuggestActivity.this,
                        AnsynHttpRequest.POST, callBack, Constent.ID_ADDSUGGEST,
                        map, false, true, true);

                break;

            default:
                break;
        }

    }

}
