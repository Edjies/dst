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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * 忘记密码
 *
 * @author Administrator
 */
public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
    // private static String TAG = "RegistActivity";
    private FrameLayout topLayout;
    private EditText phoneEditText, pwdEditText, yzmEditText, pwd2EditText;
    private Button yzmButton, nextButton, okButton;
    private String phone, pwd, yzm, pwd2;
    private int time = 80;
    private Timer timer = null;
    private boolean hasget = false;
    private TextView backTextView, titleTextView;
    private LinearLayout layout, layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);
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
        topLayout = (FrameLayout) findViewById(R.id.forgetpwd_top_layout);
        setTopLayoutPadding(topLayout);
        backTextView = (TextView) findViewById(R.id.forgetpwd_backtextview);
        titleTextView = (TextView) findViewById(R.id.forgetpwd_title);
        phoneEditText = (EditText) findViewById(R.id.forgetpwd_phone);

        yzmEditText = (EditText) findViewById(R.id.forgetpwd_yzm);
        yzmButton = (Button) findViewById(R.id.forgetpwd_yzm_btn);
        nextButton = (Button) findViewById(R.id.forgetpwd_yzm_next);
        pwdEditText = (EditText) findViewById(R.id.forgetpwd_pwd);
        pwd2EditText = (EditText) findViewById(R.id.forgetpwd_pwd2);
        okButton = (Button) findViewById(R.id.forgetpwd_ok);
        layout = (LinearLayout) findViewById(R.id.forgetpwd_layout);
        layout2 = (LinearLayout) findViewById(R.id.forgetpwd_layout2);

        nextButton.setOnClickListener(this);
        yzmButton.setOnClickListener(this);
        backTextView.setOnClickListener(this);
        okButton.setOnClickListener(this);
        layout2.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        yzmButton.setText(getResources().getString(R.string.getyzm));
        titleTextView.setText(getResources().getString(R.string.forgetpws));

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (handler != null) {
            handler.removeMessages(getyazm);
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.forgetpwd_ok:
                phone = phoneEditText.getText().toString();
                pwd = pwdEditText.getText().toString();
                yzm = yzmEditText.getText().toString();
                pwd2 = pwd2EditText.getText().toString();

                if (phone == null || TextUtils.isEmpty(phone)) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "手机号不能为空", false);
                    return;
                }
                if (yzm == null || TextUtils.isEmpty(yzm)) {
                    PublicUtil
                            .showToast(ForgetPwdActivity.this, "短信验证码不能为空", false);
                    return;
                }

                if (pwd == null || TextUtils.isEmpty(pwd)) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "密码不能为空", false);
                    return;
                }

                if (!PublicUtil.isMobileNO(phone)) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "手机号码格式错误,请重新输入",
                            false);
                    return;
                }

                if (pwd.length() < 6) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "密码长度最少6位", false);
                    return;
                }

                if (!pwd.equals(pwd2)) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "密码输入不一致", false);
                    return;
                }

                Map<String, String> map = new HashMap<String, String>();

                map.put("act", Constent.ACT_MEMBER_FORGET_PWD);
                map.put("mobile", phone);
                map.put("pwd", pwd);
                map.put("code", yzm);

                AnsynHttpRequest.httpRequest(ForgetPwdActivity.this,
                        AnsynHttpRequest.GET, callBack,
                        Constent.ID_MEMBER_FORGET_PWD, map, false, true, true);

                break;
            case R.id.forgetpwd_yzm_btn:

                if (hasget) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "请不要重复请求", false);
                    return;
                }

                phone = phoneEditText.getText().toString();
                if (phone == null || TextUtils.isEmpty(phone)) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "手机号不能为空", false);
                    return;
                }
                if (!PublicUtil.isMobileNO(phone)) {
                    PublicUtil.showToast(ForgetPwdActivity.this, "手机号码格式错误,请重新输入",
                            false);
                    return;
                }

                hasget = true;

                timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (handler != null) {
                            handler.sendEmptyMessage(getyazm);
                        }

                    }
                }, 0, 1000);

                map = new HashMap<String, String>();

                map.put("act", Constent.ACT_SMS_CODE_INDEX);
                map.put("mobile", phone);
                map.put("code_type", "changePWD");

                AnsynHttpRequest.httpRequest(ForgetPwdActivity.this,
                        AnsynHttpRequest.GET, callBack, Constent.ID_GETYZM, map,
                        false, true, true);

                break;
            case R.id.forgetpwd_backtextview:

                if (layout2.getVisibility() == View.VISIBLE) {
                    layout2.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }

                break;
            case R.id.forgetpwd_yzm_next:
                phone = phoneEditText.getText().toString();
                yzm = yzmEditText.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    PublicUtil.showToast(this, "手机号不能为空", false);
                    return;
                }
                if (!PublicUtil.isMobileNO(phone)) {
                    PublicUtil.showToast(this, "手机号码格式错误,请重新输入",
                            false);
                    return;
                }

                if (TextUtils.isEmpty(yzm)) {
                    PublicUtil.showToast(this, "请输入验证码", false);
                    return;
                }
                if (hasget == false) {
                    PublicUtil.showToast(this, "请获取验证码", false);
                    return;
                }
                layout2.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                titleTextView.setText(getResources().getString(
                        R.string.shezhixinmima));

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

            if (backId == Constent.ID_MEMBER_FORGET_PWD) {

                if (isRequestSuccess) {

                    if (!isString) {

                        try {
                            String backstr = jsonArray.getString(1);
                            jsonObject = new JSONObject(backstr);
                            // backArray = new JSONArray(backstr);
                            // Log.d(TAG, backArray.length() + "");
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

            } else if (backId == Constent.ID_SMS_CODE_INDEX) {

                if (isRequestSuccess) {

                    if (!isString) {

                        try {
                            String backstr = jsonArray.getString(1);
                            jsonObject = new JSONObject(backstr);
                            // backArray = new JSONArray(backstr);
                            // Log.d(TAG, backArray.length() + "");
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

            }

        }
    };

    private int httprequesterror = 0x8400;
    private int getyazm = 0x8401;
    private int httprequestsuccess = 0x8402;
    private int httprequestsuccess_getyzm = 0x8403;
    private int httprequesterror_getyzm = 0x8404;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg != null && msg.what == getyazm) {
                time--;
                if (time > -1) {
                    yzmButton.setText(time + "s");
                } else {
                    if (handler != null) {
                        handler.removeMessages(getyazm);
                    }
                    if (timer != null) {
                        timer.purge();
                        timer.cancel();
                        timer = null;
                    }
                    yzmButton.setText(getResources().getString(R.string.getyzm));
                    time = 80;
                    hasget = false;
                }

            }

            if (msg != null && msg.what == httprequesterror) {
                if (msg.obj != null) {
                    PublicUtil.showToast(ForgetPwdActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                if (jsonObject != null) {
                    try {
                        PublicUtil.showToast(ForgetPwdActivity.this, jsonObject
                                .get("msg").toString(), false);
                        if ("0".equals(jsonObject.getString("errcode"))) {

                            PublicUtil.setStorage_string(
                                    ForgetPwdActivity.this, "phone", phone);
                            PublicUtil.setStorage_string(
                                    ForgetPwdActivity.this, "pwd", pwd);
                            Intent intent = new Intent(ForgetPwdActivity.this,
                                    LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(ForgetPwdActivity.this,
                                "配置解析数据错误，请退出重试", false);
                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == httprequestsuccess_getyzm) {
                if (jsonObject != null) {
                    try {
                        if ("0".equals(jsonObject.getString("errcode"))) {

                        } else {
                            PublicUtil.showToast(ForgetPwdActivity.this,
                                    "获取验证码失败"
                                            + jsonObject.get("msg").toString(),
                                    false);
                            yzmInit();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        PublicUtil.showToast(ForgetPwdActivity.this, "获取验证码失败",
                                false);
                        yzmInit();
                    }

                }

            } else if (msg != null && msg.what == httprequesterror_getyzm) {
                if (msg.obj != null) {
                    PublicUtil.showToast(ForgetPwdActivity.this,
                            msg.obj.toString(), false);

                }
                yzmInit();
            }

        }
    };

    /**
     * 初始化获取验证码的操作
     */
    public void yzmInit() {
        if (handler != null) {
            handler.removeMessages(getyazm);
        }
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }

        yzmButton.setText(getResources().getString(R.string.getyzm));
        time = 80;
        hasget = false;

    }

    /**
     * 请求验证码
     */
    // public void requestYzm() {
    // new Thread(new Runnable() {
    //
    // @Override
    // public void run() {
    // // TODO Auto-generated method stub
    // try {
    // InputStream is = null;
    // OutputStream os = null;
    // StringBuffer body = null;
    // byte[] data = body.toString().getBytes();
    // URL url = new URL(Constent.URLHEAD);
    // // 获得URL对象
    // HttpsURLConnection connection = (HttpsURLConnection) url
    // .openConnection();
    // // 获得HttpURLConnection对象
    //
    // connection.setRequestProperty("Content-Type",
    // "application/x-www-form-urlencoded");
    // connection.setRequestProperty("Content-Length",
    // String.valueOf(data.length));
    // connection.setRequestProperty("Cookie", "");
    // // 设置请求头里的信息
    //
    // connection.setRequestMethod("POST");
    // // 设置请求方法为post
    // connection.setUseCaches(false);
    // // 不使用缓存
    // connection.setConnectTimeout(10000);
    // // 设置超时时间
    // connection.setReadTimeout(10000);
    // // 设置读取超时时间
    // connection.setDoInput(true);
    // // 设置是否从httpUrlConnection读入，默认情况下是true;
    // connection.setDoOutput(true);
    // // 设置为true后才能写入参数
    //
    // os = connection.getOutputStream();
    // os.write(data);
    // // 写入参数
    // if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
    // // 相应码是否为200
    // is = connection.getInputStream();
    // // 获得输入流
    // BufferedReader reader = new BufferedReader(
    // new InputStreamReader(is, "UTF-8"));
    // // 包装字节流为字符流
    // StringBuilder response = new StringBuilder();
    // String line;
    // while ((line = reader.readLine()) != null) {
    // response.append(line);
    // }
    // }
    //
    // } catch (Exception e) {
    // // TODO: handle exception
    // }
    // }
    // }).start();
    //
    // }

}
