package com.haofeng.apps.dst.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.utils.PublicUtil;

/**
 * 设置支付密码
 */
public class SettingPayPwdActivity extends BaseActivity {

    private EditText inputPwdText;
    private EditText inputConfirmPwdText;
    private EditText getYZMText;
    private Button getYZMButton;
    private TextView toPhoneInfoView;
    private TextView getYZMProblem;
    private Button confirmSettingButton;
    private FrameLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_setting_pay_pwd);
        inputPwdText = (EditText) findViewById(R.id.activity_setting_pay_pwd_number);
        topLayout = (FrameLayout) findViewById(R.id.activity_setting_pay_pwd_top_layout);
        inputConfirmPwdText = (EditText) findViewById(R.id.activity_setting_pay_pwd_number_confirm);
        getYZMText = (EditText) findViewById(R.id.activity_setting_pay_pwd_yzm);
        getYZMButton = (Button) findViewById(R.id.activity_setting_pay_pwd_get_yzm);
        toPhoneInfoView = (TextView) findViewById(R.id.activity_setting_pay_pwd_to_phone_info);
        getYZMProblem = (TextView) findViewById(R.id.activity_setting_pay_pwd_get_yzm_problem);
        confirmSettingButton = (Button) findViewById(R.id.activity_setting_pay_pwd_confirm_setting);
        setTopLayoutPadding(topLayout);
        String phone = PublicUtil.getStorage_string(this, "phone", "null");
        if (!phone.equals("null")) {
            toPhoneInfoView.setText("将发送给:" + phone);
        }
    }

    private void initListener() {
        getYZMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        getYZMProblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View getYZMVAlertView = View.inflate(SettingPayPwdActivity.this, R.layout.alert_not_get_yzm, null);

                AlertDialog.Builder builder = new
                        AlertDialog.Builder(SettingPayPwdActivity.this);
                builder.setView(getYZMVAlertView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                getYZMVAlertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        confirmSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
