package com.haofeng.apps.dst.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.haofeng.apps.dst.ui.MyWalletActivity;
import com.haofeng.apps.dst.ui.NewOrderListActivity;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements
        IWXAPIEventHandler {

    private final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private FrameLayout topLayout;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpayentry);
        addActivity(this);
        topLayout = (FrameLayout) findViewById(R.id.wxpayentry_top_layout);
        setTopLayoutPadding(topLayout);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        String rechargetype = PublicUtil.getStorage_string(
                WXPayEntryActivity.this, "rechargetype", "recharge");
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            PublicUtil.logDbug(TAG, "rechargetype = " + rechargetype, 0);
            if (resp.errCode >= 0) {
                if ("recharge".equals(rechargetype) || "rent_car_yajin".equals(rechargetype)) { //押金和电卡充值成功
                    Intent intent = new Intent(WXPayEntryActivity.this,
                            MyWalletActivity.class);
                    startActivity(intent);
                } else if ("pay".equals(rechargetype)) {  //预付款
                    Intent intent = new Intent(WXPayEntryActivity.this,
                            NewOrderListActivity.class);
                    startActivity(intent);
                }
                PublicUtil.showToast(WXPayEntryActivity.this, "支付成功", false);
                finish();
            }

        } else {
            if ("recharge".equals(rechargetype) || "rent_car_yajin".equals(rechargetype)) { //押金和电卡充值成功
                Intent intent = new Intent(WXPayEntryActivity.this,
                        MyWalletActivity.class);
                startActivity(intent);

            } else if ("pay".equals(rechargetype)) {  //预付款
                Intent intent = new Intent(WXPayEntryActivity.this,
                        NewOrderListActivity.class);
                startActivity(intent);
            }
            PublicUtil.showToast(WXPayEntryActivity.this, "支付失败", false);
            finish();
        }

    }
}