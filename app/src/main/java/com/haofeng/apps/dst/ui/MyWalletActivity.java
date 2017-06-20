package com.haofeng.apps.dst.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * 我的钱包
 *
 * @author WIN10
 */
public class MyWalletActivity extends BaseActivity implements OnClickListener {
    private Button powerCongZhiView;
    private Button powerTiXianView;
    private Button yajinCongZhiView;
    private Button yajinTiXianView;
    private TextView yajinAccountView;
    private TextView powerAccountView;
    private TextView detailView;
    private TextView backView;
    private boolean isHasWeiZhangNoDeal = false;  //是否有违章未处理
    private boolean isHasNoHuancheOrder = false;
    private String yiTiCarOrder;
    private double foregift_acount;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        // TODO Auto-generated method stub
        //获取押金账户
        Map<String, String> map = new HashMap<>();
        map.put("act", Constent.ACT_MEMBER_INFOR);
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_MEMBER_INFOR, map, false, false, true);


    }

    private void initView() {
        // TODO Auto-generated method stub
        setContentView(R.layout.activity_mywallet);
        powerCongZhiView = (Button) findViewById(R.id.activity_mywallet_power_congzhi);
        powerTiXianView = (Button) findViewById(R.id.activity_mywallet_power_tixian);
        yajinCongZhiView = (Button) findViewById(R.id.activity_mywallet_yajin_congzhi);
        yajinTiXianView = (Button) findViewById(R.id.activity_mywallet_yajin_tixian);
        detailView = (TextView) findViewById(R.id.activity_mywallet_detail);
        backView = (TextView) findViewById(R.id.wallet_back);
        yajinAccountView = (TextView) findViewById(R.id.activity_mywallet_yajin_account);
        powerAccountView = (TextView) findViewById(R.id.activity_mywallet_power_account);
        FrameLayout topLayout = (FrameLayout) findViewById(R.id.wallet_top_layout);
        setTopLayoutPadding(topLayout);
    }

    private void initListener() {
        // TODO Auto-generated method stub
        powerCongZhiView.setOnClickListener(this);
        powerTiXianView.setOnClickListener(this);
        yajinCongZhiView.setOnClickListener(this);
        yajinTiXianView.setOnClickListener(this);
        backView.setOnClickListener(this);
        detailView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent intent = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        switch (v.getId()) {
            case R.id.activity_mywallet_power_congzhi: // 电卡充值
                intent = new Intent(this, RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.activity_mywallet_power_tixian: // 电卡提现
                Toast.makeText(this, "该功能暂未开启，敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_mywallet_yajin_congzhi: // 押金充值
                intent = new Intent(this, RechargeActivity.class);
                intent.putExtra("type", "rent_car_yajin");
                intent.putExtra("infor", "租车押金充值");
                intent.putExtra("infor2", "地上铁App租车押金充值");
                if (yajinAccountView.getText().toString() != null) {
                    Double yajinAccount = Double.parseDouble(yajinAccountView.getText().toString());
                    if (yajinAccount < 0) {
                        intent.putExtra("yajinNeed", -yajinAccount);
                    }
                }
                startActivity(intent);
                break;
            case R.id.activity_mywallet_yajin_tixian: // 押金提现
                //押金退款
                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_YAJINTIXIAN);
                AnsynHttpRequest.httpRequest(MyWalletActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_YAJINTIXIAN, map, false, false, true);
                break;
            case R.id.activity_mywallet_detail: // 明细
                intent = new Intent(this, WalletlistActivity.class);
                startActivity(intent);
                break;
            case R.id.wallet_back: // 返回
                finish();
                break;
            default:
                break;
        }
    }

    private HttpRequestCallBack callBack = new HttpRequestCallBack() {
        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {

            switch (backId) {
                case Constent.ID_MEMBER_INFOR:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backStr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backStr);
                                if ("0".equals(jsonObject.getString("errcode"))) {
                                    JSONObject dataObject = jsonObject.getJSONObject("data");
                                    final String amount = dataObject.getString("amount");  //钱包账户余额
                                    //押金账户余额
                                    foregift_acount = Double.parseDouble(dataObject.getString("foregift_acount"));
                                    name = dataObject.getString("name");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            powerAccountView.setText(PublicUtil.roundByScale(Double.parseDouble(amount), 2));
                                            yajinAccountView.setText(PublicUtil.roundByScale(foregift_acount, 2));
                                        }
                                    });

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    break;
                case Constent.ID_ACT_GET_ORDERlIST:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backStr = jsonArray.getString(1);
                                JSONObject jsonObject = new JSONObject(backStr);
                                if ("0".equals(jsonObject.getString("errcode"))) {
                                    JSONObject dataObject = jsonObject.getJSONObject("data");

                                    if (dataObject != null) {
                                        //获取有没有已还车状态之前的订单
                                        int total = Integer.parseInt(dataObject.getString("total"));
                                        if (total != 0) {
                                            isHasNoHuancheOrder = true;
                                            JSONArray listArray = dataObject.getJSONArray("list");
                                            yiTiCarOrder = listArray.getJSONObject(0).getString("order_no");
                                        }
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case Constent.ID_ORDER_GET_DETAIL:  //订单详情
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestSuccess) {
                                if (!isString) {
                                    String backStr = null;
                                    try {
                                        backStr = jsonArray.getString(1);
                                        JSONObject jsonObject = new JSONObject(backStr);
                                        JSONObject orderObject = jsonObject.optJSONObject("data").optJSONObject("order");
                                        if (orderObject != null) {
                                            //获取订单状态
                                            String orderState = orderObject.getString("order_state");
                                            String order_no = orderObject.getString("order_no");
                                            AlertDialog dialog = new AlertDialog.Builder(MyWalletActivity.this).create();
                                            View view = View.inflate(MyWalletActivity.this, R.layout.view_yajin_no_huanche, null);
                                            dialog.setView(view);
                                            TextView detailShow = (TextView) view.findViewById(R.id.view_yajin_no_huan_che_show);
                                            if (orderState.equals("1")) {  //待提车
                                                detailShow.setText("很抱歉，暂时无法退还押金\n原因：\n" + "      订单 " + order_no + "待提车，取消订单即可提现");
                                            } else {  //未还车
                                                detailShow.setText("很抱歉，暂时无法退还押金\n原因：\n" + "      订单 " + order_no + "未还车，还车后即可提现");
                                            }

                                            dialog.show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    });
                    break;
                case Constent.ID_ACT_YAJINTIXIAN:   //押金提现
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestSuccess) {
                                if (!isString) {
                                    try {
                                        String backStr = jsonArray.getString(1);
                                        JSONObject jsonObject = new JSONObject(backStr);
                                        JSONObject dataObject = jsonObject.optJSONObject("data");

                                        AlertDialog.Builder builder = new AlertDialog.Builder(MyWalletActivity.this);
                                        final AlertDialog dialog = builder.create();
                                        if ("1112".equals(jsonObject.getString("errcode"))) {   //1.判断押金金额 2.判断订单 3.判断违章
                                            String remark = dataObject.getString("remark");
                                            if (remark != null && remark.equals("ARREARS")) {
                                                if (foregift_acount == 0) {
                                                    Toast.makeText(MyWalletActivity.this, "押金账户为0，无法提现", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    View view = View.inflate(MyWalletActivity.this, R.layout.view_yajin_lower_zero, null);
                                                    dialog.setView(view);
                                                    TextView detailTextView = (TextView) view.findViewById(R.id.view_yajin_lower_zero_show);
                                                    detailTextView.setText("亲爱的" + name + "用户您好！\n" + "您当前押金存在欠款，为了\n您下次依然可以使用地上铁\n车辆，请立即前往充值缴纳");
                                                    TextView congzhiTextView = (TextView) view.findViewById(R.id.view_yajin_lower_zero_congzhi);
                                                    congzhiTextView.setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(MyWalletActivity.this, RechargeActivity.class);
                                                            intent.putExtra("type", "rent_car_yajin");
                                                            intent.putExtra("infor", "租车押金充值");
                                                            intent.putExtra("infor2", "地上铁App租车押金充值");
                                                            intent.putExtra("yajinNeed", -foregift_acount);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                    dialog.show();
                                                }
                                            } else if (remark != null && remark.equals("WAITTING_BACK_CAR")) {
                                                //有未还车订单
                                                String order_no = dataObject.getString("order_no");
                                                Map<String, String> map = new HashMap<String, String>();
                                                map.put("act", Constent.ACT_ORDER_GET_DETAIL);
                                                map.put("order_no", order_no);
                                                AnsynHttpRequest.httpRequest(MyWalletActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_GET_DETAIL, map, false, false, true);

                                            } else if (remark != null && remark.equals("WZ_NO_DISPOSE")) {
                                                View view = View.inflate(MyWalletActivity.this, R.layout.view_yajin_no_huanche, null);
                                                dialog.setView(view);
                                                TextView detailTextView = (TextView) view.findViewById(R.id.view_yajin_no_huan_che_show);
                                                detailTextView.setText("很抱歉，暂时无法退还押金\n原因：\n" + "      订单 " + dataObject.getString("order_no") + "违章尚未确认，您可以联系地上铁客服咨询");
                                                dialog.show();
                                            }
                                        } else if ("0".equals(jsonObject.getString("errcode"))) {
                                            Intent intent = new Intent(MyWalletActivity.this, ReturnMoneyActivity.class);
                                            startActivity(intent);
                                            //                                            View view = View.inflate(MyWalletActivity.this, R.layout.view_yajintixian, null);
                                            //                                            dialog.setView(view);
                                            //                                            Button tuikuanView = (Button) view.findViewById(R.id.view_yajintixian_yajin_tuikuan);
                                            //                                            Button concelView = (Button) view.findViewById(R.id.view_yajintixian_concel);
                                            //                                            tuikuanView.setOnClickListener(new OnClickListener() {
                                            //                                                @Override
                                            //                                                public void onClick(View v) {
                                            //                                                    // TODO Auto-generated method stub
                                            //                                                    //押金退款
                                            //                                                    Intent intent = new Intent(MyWalletActivity.this, ReturnMoneyActivity.class);
                                            //                                                    startActivity(intent);
                                            //                                                }
                                            //                                            });
                                            //                                            concelView.setOnClickListener(new OnClickListener() {
                                            //
                                            //                                                @Override
                                            //                                                public void onClick(View v) {
                                            //                                                    // TODO Auto-generated method stub
                                            //                                                    dialog.dismiss();
                                            //                                                }
                                            //                                            });
                                            //                                            dialog.show();
                                            //                                            finish();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    break;
            }
        }
    };

}
