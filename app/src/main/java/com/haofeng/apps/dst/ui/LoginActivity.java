package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends BaseActivity implements OnClickListener {
    private EditText phoneEditText, pwdEditText;
    private TextView registTextView, forgetTextView;
    private Button loginButton;
    private String phone, pwd;
    private TextView loginBackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

        phoneEditText = (EditText) findViewById(R.id.login_phoneedit);
        pwdEditText = (EditText) findViewById(R.id.login_pwdedit);
        loginButton = (Button) findViewById(R.id.login_okbutton);
        registTextView = (TextView) findViewById(R.id.login_registtextview);
        forgetTextView = (TextView) findViewById(R.id.login_forgettextview);
        loginButton.setOnClickListener(this);
        registTextView.setOnClickListener(this);
        forgetTextView.setOnClickListener(this);
        phoneEditText.setText(PublicUtil.getStorage_string(LoginActivity.this,
                "phone", ""));
        pwdEditText.setText(PublicUtil.getStorage_string(LoginActivity.this,
                "pwd", ""));
        loginBackTextView = (TextView) findViewById(R.id.login_back);
        loginBackTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent = null;
        switch (arg0.getId()) {
            case R.id.login_okbutton:
                phone = phoneEditText.getText().toString();
                pwd = pwdEditText.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    PublicUtil.showToast(this, "手机号不能为空", false);
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    PublicUtil.showToast(this, "密码不能为空", false);
                    return;
                }

                if (!PublicUtil.isMobileNO(phone)) {
                    PublicUtil.showToast(this, "手机号码格式错误,请重新输入",
                            false);
                    return;

                }

                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_LOGIN_INDEX);
                map.put("mobile", phone);
                map.put("pwd", pwd);
                AnsynHttpRequest.httpRequest(LoginActivity.this,
                        AnsynHttpRequest.GET, callBack, Constent.ID_LOGIN_INDEX,
                        map, false, true, true);

                break;

            case R.id.login_registtextview:
                phone = phoneEditText.getText().toString();

                intent = new Intent(this, RegistActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login_forgettextview:
                intent = new Intent(this, ForgetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.login_back:
                finish();
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

            if (backId == Constent.ID_LOGIN_INDEX) {

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
                    Message message = new Message();
                    message.what = httprequesterror;
                    message.obj = data;
                    handler.sendMessage(message);
                }

            }

        }
    };

    private int httprequesterror = 0x5200;
    private int httprequestsuccess = 0x5201;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == httprequesterror) {
                if (msg.obj != null) {
                    PublicUtil.showToast(LoginActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                if (jsonObject != null) {
                    try {
                        PublicUtil.showToast(LoginActivity.this, jsonObject
                                .get("msg").toString(), false);
                        if ("0".equals(jsonObject.getString("errcode"))) {
                            ((MyApplication) getApplication()).setIsregstToken(false);
                            PublicUtil.setStorage_string(LoginActivity.this,
                                    "islogin", "1");

                            PublicUtil.setStorage_string(LoginActivity.this,
                                    "phone", phone);
                            PublicUtil.setStorage_string(LoginActivity.this,
                                    "pwd", pwd);
                            PublicUtil.setStorage_string(
                                    LoginActivity.this,
                                    "loginkey2",
                                    jsonObject.getJSONObject("data").getString(
                                            "loginkey"));
                            AnsynHttpRequest.loginkey = jsonObject
                                    .getJSONObject("data")
                                    .getString("loginkey");

                            PublicUtil.setStorage_string(LoginActivity.this,
                                    "userid", jsonObject.getJSONObject("data")
                                            .getString("user_id"));
                            AnsynHttpRequest.userid = jsonObject.getJSONObject(
                                    "data").getString("user_id");
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(LoginActivity.this,
                                "配置解析数据错误，请退出重试", false);
                    }

                }

            }
        }

        ;
    };

}
