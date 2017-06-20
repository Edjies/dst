package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.bean.Account;

/**
 * 
 * Created by WIN10 on 2017/6/16.
 */

public class UserCenterActivity extends BaseActivity implements View.OnClickListener{
    public final static String TAG = UserCenterActivity.class.getSimpleName();

    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private ImageView mIvMessage;
    private TextView mTvMessageNotice;
    private TextView mTvName;

    private TextView mTvAmount;
    private TextView mTvScore;
    private TextView mTvOrder;

    private RelativeLayout mRlChongDianJiLu;
    private RelativeLayout mRlShouCangDianZhan;
    private RelativeLayout mRlGeRenZiLiao;
    private RelativeLayout mRlHuiYuanRenZhen;
    private RelativeLayout mRlJiFenDuiHuan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);
        initIntentData();
        setViews();
        setListeners();
        initData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
        }

        else if(R.id.iv_message == id) {
            Intent intent = new Intent(this, NewsCenterActivity.class);
            startActivity(intent);
        }

        else if(R.id.rl_chongidanjilu == id) {
            Intent intent = new Intent(this, ChargeListActivity.class);
            startActivity(intent);
        }

        else if(R.id.rl_shoucangdianzhan == id) {
            Intent intent = new Intent(this, CollectionActivity.class);
            startActivity(intent);
        }

        else if(R.id.rl_gerenziliao == id) {
            Intent intent = new Intent(this, UserInforActivity.class);
            intent.putExtra("name", mTvName.getText().toString());
            startActivity(intent);
        }

        else if(R.id.rl_huiyuanrenzheng == id) {
            Intent intent = new Intent(this, HuiYuanRZActivity.class);
            intent.putExtra("from", "user");
            startActivity(intent);
        }

        else if(R.id.rl_jifenduihuan == id) {
            Toast.makeText(this, "该功能暂未开启，敬请期待", Toast.LENGTH_SHORT).show();
        }

        else if(R.id.rl_amount == id) {
            Intent intent = new Intent(UserCenterActivity.this, MyWalletActivity.class);
            startActivity(intent);
        }

        else if(R.id.rl_order == id) {
            Intent intent = new Intent(UserCenterActivity.this, NewOrderListActivity.class);
            startActivity(intent);
        }
    }

    private void initIntentData() {

    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvMessage = (ImageView) findViewById(R.id.iv_message);

        mTvName = (TextView) findViewById(R.id.tv_name);
        mTvAmount = (TextView) findViewById(R.id.tv_amount);
        mTvScore = (TextView) findViewById(R.id.tv_score);
        mTvOrder = (TextView) findViewById(R.id.tv_order);

        mRlChongDianJiLu = (RelativeLayout) findViewById(R.id.rl_chongidanjilu);
        mRlShouCangDianZhan = (RelativeLayout) findViewById(R.id.rl_shoucangdianzhan);
        mRlGeRenZiLiao = (RelativeLayout) findViewById(R.id.rl_gerenziliao);
        mRlHuiYuanRenZhen = (RelativeLayout) findViewById(R.id.rl_huiyuanrenzheng);
        mRlJiFenDuiHuan = (RelativeLayout) findViewById(R.id.rl_jifenduihuan);

    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mIvMessage.setOnClickListener(this);
        mRlChongDianJiLu.setOnClickListener(this);
        mRlShouCangDianZhan.setOnClickListener(this);
        mRlGeRenZiLiao.setOnClickListener(this);
        mRlHuiYuanRenZhen.setOnClickListener(this);
        mRlJiFenDuiHuan.setOnClickListener(this);
    }

    private void initData() {

        Account account = UserDataManager.getInstance().getAccount();
        mTvName.setText(account.m_name);
        mTvAmount.setText(account.m_amount);
        mTvScore.setText(account.m_score);
        mTvOrder.setText(account.m_order_total);
    }
}
