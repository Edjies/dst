package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import static com.haofeng.apps.dst.R.id.userinfor_change_pay_pwd;

/**
 * 个人资料
 *
 * @author Administrator
 */
public class UserInforActivity extends BaseActivity implements OnClickListener {
    private FrameLayout topLayout;
    private TextView backView;
    private TextView nameView, phoneView, workView, name2View;
    private FrameLayout changepwdLayout;
    private Button logoutButton;
    private FrameLayout changePayPwdLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfor);
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
        topLayout = (FrameLayout) findViewById(R.id.userinfor_top_layout);
      setTopLayoutPadding(topLayout);
        backView = (TextView) findViewById(R.id.userinfor_backtextview);
        nameView = (TextView) findViewById(R.id.userinfor_name);
        phoneView = (TextView) findViewById(R.id.userinfor_phone);
        workView = (TextView) findViewById(R.id.userinfor_work);
        name2View = (TextView) findViewById(R.id.userinfor_name2);
        changepwdLayout = (FrameLayout) findViewById(R.id.userinfor_changepwd);
        changePayPwdLayout = (FrameLayout) findViewById(userinfor_change_pay_pwd);

        logoutButton = (Button) findViewById(R.id.userinfor_logout);
        backView.setOnClickListener(this);
        changepwdLayout.setOnClickListener(this);
        workView.setOnClickListener(this);
        name2View.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        changePayPwdLayout.setOnClickListener(this);
        nameView.setText(getIntent().getStringExtra("name"));

        name2View.setText(PublicUtil.getStorage_string(UserInforActivity.this,
                "user_nicheng", ""));

        workView.setText(PublicUtil.getStorage_string(UserInforActivity.this,
                "user_danwei", ""));

        phoneView.setText(PublicUtil.getStorage_string(UserInforActivity.this,
                "phone", ""));

        if (!TextUtils.isEmpty(getIntent().getStringExtra("name"))) {
            nameView.setText(getIntent().getStringExtra("name"));
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == 31 && data != null) {

            if ("1".equals(data.getStringExtra("type"))) {
                workView.setText(data.getStringExtra("name"));
            } else {
                name2View.setText(data.getStringExtra("name"));
            }

        }
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        Intent intent;
        switch (arg0.getId()) {

            case R.id.userinfor_backtextview:
                finish();
                break;
            case R.id.userinfor_work:
                intent = new Intent(UserInforActivity.this, NichengBJActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("name", workView.getText().toString());
                startActivityForResult(intent, 0);
                break;
            case R.id.userinfor_name2:
                intent = new Intent(UserInforActivity.this, NichengBJActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("name", name2View.getText().toString());
                startActivityForResult(intent, 0);
                break;
            case R.id.userinfor_changepwd:
                intent = new Intent(UserInforActivity.this, ChangePwdActivity.class);
                startActivity(intent);

                break;

            case R.id.userinfor_logout:
                PublicUtil.removeStorage_string(UserInforActivity.this, "islogin");
                PublicUtil.removeStorage_string(UserInforActivity.this, "phone");
                PublicUtil.removeStorage_string(UserInforActivity.this, "pwd");
                PublicUtil
                        .removeStorage_string(UserInforActivity.this, "loginkey2");
                PublicUtil.removeStorage_string(UserInforActivity.this, "userid");
                PublicUtil.removeStorage_string(UserInforActivity.this,
                        "user_danwei");
                PublicUtil.removeStorage_string(UserInforActivity.this,
                        "user_nicheng");
                PublicUtil.removeStorage_string(UserInforActivity.this,
                        "mapsearchkey");
                PublicUtil.removeStorage_string(UserInforActivity.this,
                        "id_card_url");
                PublicUtil.removeStorage_string(UserInforActivity.this,
                        "id_card_url1");
                PublicUtil.removeStorage_string(UserInforActivity.this,
                        "driving_card_url");
                PublicUtil.removeStorage_string(UserInforActivity.this,
                        "business_license_url");
                PublicUtil.removeStorage_string(UserInforActivity.this, "order_no");
                PublicUtil.removeStorage(UserInforActivity.this);

                AnsynHttpRequest.loginkey = null;
                AnsynHttpRequest.userid = null;
                intent = new Intent(UserInforActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
            case R.id.userinfor_change_pay_pwd:  //修改支付密码
                intent = new Intent(UserInforActivity.this, SettingPayPwdActivity.class);
                startActivity(intent);

                break;
        }

    }
}
