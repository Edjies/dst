package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * 注册，忘记密码都可以共用此activity
 *
 * @author Administrator
 */
public class RegistActivity extends BaseActivity implements OnClickListener {
    // private static String TAG = "RegistActivity";
    private EditText phoneEditText, pwdEditText, yzmEditText;
    private Button registButton, yzmButton;
    private String phone, pwd, yzm;
    private int time = 60;
    private Timer timer = null;
    private boolean hasget = false;
    private TextView backTextView;
    private CheckBox checkBox;
    private TextView topBackTextView;
    private TextView mTvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
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
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.recharge_top_layout);
        phoneEditText = (EditText) findViewById(R.id.regist_phoneedit);
        pwdEditText = (EditText) findViewById(R.id.regist_pwdedit);
        yzmEditText = (EditText) findViewById(R.id.regist_yzmedit);
        registButton = (Button) findViewById(R.id.regist_okbutton);
        yzmButton = (Button) findViewById(R.id.regist_getyzmbutton);
        checkBox = (CheckBox) findViewById(R.id.regist_checkbox);
        topBackTextView = (TextView) findViewById(R.id.regist_back);
        topBackTextView.setOnClickListener(this);
        backTextView = (TextView) findViewById(R.id.regist_backtextview);
        registButton.setOnClickListener(this);
        yzmButton.setOnClickListener(this);
        backTextView.setOnClickListener(this);
        yzmButton.setText(getResources().getString(R.string.getyzm));
        mTvContact = (TextView) findViewById(R.id.tv_contact);
        mTvContact.setOnClickListener(this);
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
            case R.id.regist_okbutton:
                phone = phoneEditText.getText().toString();
                pwd = pwdEditText.getText().toString();
                yzm = yzmEditText.getText().toString();

                if (phone == null || TextUtils.isEmpty(phone)) {
                    PublicUtil.showToast(RegistActivity.this, "手机号不能为空", false);
                    return;
                }
                if (yzm == null || TextUtils.isEmpty(yzm)) {
                    PublicUtil.showToast(RegistActivity.this, "请输入手机短信验证码", false);
                    return;
                }

                if (pwd == null || TextUtils.isEmpty(pwd)) {
                    PublicUtil.showToast(RegistActivity.this, "密码不能为空", false);
                    return;
                }

                if (!PublicUtil.isMobileNO(phone)) {
                    PublicUtil.showToast(RegistActivity.this, "手机号码格式错误,请重新输入",
                            false);
                    return;
                }

                if (pwd.length() < 6) {
                    PublicUtil.showToast(RegistActivity.this, "密码长度最少6位", false);
                    return;
                }

                if (!checkBox.isChecked()) {
                    PublicUtil.showToast(RegistActivity.this, "请同意会员服务条款", false);
                    return;
                }

                Map<String, String> map = new HashMap<String, String>();

                map.put("act", Constent.ACT_REGISTER_INDEX);
                map.put("ver", Constent.VER);
                map.put("mobile", phone);
                map.put("pwd", pwd);
                map.put("code", yzm);
                map.put("type", "0");
                AnsynHttpRequest.httpRequest(RegistActivity.this,
                        AnsynHttpRequest.GET, callBack, Constent.ID_REGISTER_INDEX,
                        map, false, true, true);

                break;
            case R.id.regist_getyzmbutton:

                if (hasget) {
                    PublicUtil.showToast(RegistActivity.this, "请不要重复请求", false);
                    return;
                }

                phone = phoneEditText.getText().toString();
                if (phone == null || TextUtils.isEmpty(phone)) {
                    PublicUtil.showToast(RegistActivity.this, "手机号不能为空", false);
                    return;
                }
                if (!PublicUtil.isMobileNO(phone)) {
                    PublicUtil.showToast(RegistActivity.this, "手机号码格式错误,请重新输入",
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
                map.put("ver", Constent.VER);
                map.put("mobile", phone);
                map.put("code_type", "reg");
                AnsynHttpRequest.httpRequest(RegistActivity.this,
                        AnsynHttpRequest.GET, callBack, Constent.ID_GETYZM, map,
                        false, true, true);

                break;
            case R.id.regist_backtextview:
            case R.id.regist_back:
                Intent loginIntent = new Intent(this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
                break;
            case R.id.tv_contact:
                new AlertDialog.Builder(this).setTitle("提示").setMessage("收不到验证码请拨打客服电话获取验证码~")
                        .setPositiveButton("客服电话", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:400-860-4558"));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null).show();
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

            if (backId == Constent.ID_REGISTER_INDEX) {

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

            } else if (backId == Constent.ID_GETYZM) {

                if (isRequestSuccess) {

                    if (!isString) {

                        try {
                            String backstr = jsonArray.getString(1);
                            jsonObject = new JSONObject(backstr);
                            // backArray = new JSONArray(backstr);
                            // Log.d(TAG, backArray.length() + "");
                            handler.sendEmptyMessage(httprequestsuccess_getyzm);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            if (handler != null) {
                                Message message = new Message();
                                message.what = httprequesterror_getyzm;
                                message.obj = "获取验证码失败";
                                handler.sendMessage(message);
                            }
                        }
                    }

                } else {
                    if (handler != null) {
                        Message message = new Message();
                        message.what = httprequesterror_getyzm;
                        message.obj = "获取验证码失败:" + data;
                        handler.sendMessage(message);
                    }

                }

            }

        }
    };

    private int httprequesterror = 0x5400;
    private int getyazm = 0x5401;
    private int httprequestsuccess = 0x5402;
    private int httprequestsuccess_getyzm = 0x5403;
    private int httprequesterror_getyzm = 0x5404;

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

                    yzmButton
                            .setText(getResources().getString(R.string.getyzm));
                    time = 80;
                    hasget = false;
                }

            }

            if (msg != null && msg.what == httprequesterror) {
                if (msg.obj != null) {
                    PublicUtil.showToast(RegistActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == httprequestsuccess) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            ((MyApplication) getApplication())
                                    .setIsregstToken(false);
                            PublicUtil.showToast(RegistActivity.this,
                                    "注册成功，请完成认证", false);
                            PublicUtil.setStorage_string(RegistActivity.this,
                                    "islogin", "1");

                            PublicUtil.setStorage_string(RegistActivity.this,
                                    "phone", phone);
                            PublicUtil.setStorage_string(RegistActivity.this,
                                    "pwd", pwd);
                            PublicUtil.setStorage_string(
                                    RegistActivity.this,
                                    "loginkey2",
                                    jsonObject.getJSONObject("data").getString(
                                            "loginkey"));
                            AnsynHttpRequest.loginkey = jsonObject
                                    .getJSONObject("data")
                                    .getString("loginkey");

                            PublicUtil.setStorage_string(RegistActivity.this,
                                    "userid", jsonObject.getJSONObject("data")
                                            .getString("user_id"));
                            AnsynHttpRequest.userid = jsonObject.getJSONObject(
                                    "data").getString("user_id");

                            Intent intent = new Intent(RegistActivity.this,
                                    HuiYuanRZActivity.class);
                            intent.putExtra("from", "regist");
                            startActivity(intent);
                            finish();
                        } else {
                            PublicUtil.showToast(RegistActivity.this,
                                    jsonObject.get("msg").toString(), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == httprequestsuccess_getyzm) {
                if (jsonObject != null) {
                    try {
                        if ("0".equals(jsonObject.getString("errcode"))) {

                        } else {
                            PublicUtil.showToast(RegistActivity.this, "获取验证码失败"
                                    + jsonObject.get("msg").toString(), false);
                            yzmInit();
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        PublicUtil.showToast(RegistActivity.this, "获取验证码失败",
                                false);
                        yzmInit();
                    }

                }

            } else if (msg != null && msg.what == httprequesterror_getyzm) {
                if (msg.obj != null) {
                    PublicUtil.showToast(RegistActivity.this,
                            msg.obj.toString(), false);

                }
                yzmInit();
            }

        }

        ;
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
}
