package com.haofeng.apps.dst.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.haofeng.apps.dst.ui.LoginActivity;
import com.haofeng.apps.dst.ui.MyWalletActivity;
import com.haofeng.apps.dst.ui.NewOrderListActivity;
import com.haofeng.apps.dst.utils.PublicUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class AliPayActivity extends BaseActivity {
    private final String TAG = "AliPayActivity";
    private FrameLayout topLayout;

    // 商户PID
    public static final String PARTNER = "2088121509984966";
    // 商户收款账号
    public static final String SELLER = "easnchen@126.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPUDKRLDQ+fIbqZu"
            + "u9uugSRCGSkWNmMe/80iPDLaUYy9EjyjPflyMuLlLz3BRV2LsRPtv2TTtCe6DLG2"
            + "WjuoCMyk4di60p2HgDnAnS06Dix9Pzodc74tJzFaNWk/SkeU40rrds2KpP6pc7yZ"
            + "3ulastOhpT1dZKNRYQLmKn840/yfAgMBAAECgYAygP1FbdSggXM1gVvd06GxHKLd"
            + "EkQRuIEYAreP7+qhbRPGRgvvUDfnB0DupNksiC/vILG0EeNv0Ozu8Ny2sVNmnE6a"
            + "/sj+atgiG24ytUDp0P0ayHETHdAyikSfkPcTG2GW6SN2i4SR8A73P6kQnVOq/B4e"
            + "LFfA7et8y4bOt8LUAQJBAP6t34iITldTYeBlbY4QDNrB1T2gv8rFhQpfJd1F+OqL"
            + "gb9M67Ja/8nwCBIc6eUdXribotPwkmYdujaoUOgJup8CQQD2SHP33dpOPSkRPIpf"
            + "/XWAImYdW83zCtrdLzNDkgkSNyXYQ27pV5filhTJPoPqKTACztok+ulWg4fsLRID"
            + "EH4BAkBV/X8sKlnJQm4ZxiVngiPU+P13AX/Ah1CUX+v4+ldBWjlEzkQGxJL5LAKN"
            + "mxWeNnx0+JfR17iWohLtwYcP0bkhAkEAkhP3dRWJ3s44BZk7GiRkBVRh7Rf+2CGg"
            + "H4FXjsSXzM4ZDGUy7ASgei06twL1i3ZSm9fFXBnXPRFXFabGADtQAQJBAMXuVKA8"
            + "0Tg/cGr8OmgtgLFcc9xIvIjmvikaqFl1sQwRClrWaXdats3MVeW3xGkZNgjtQK44y2HUiYV9e/mOBJg=";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "";
    private TextView subjectTextView, introduceTextView, priceTextView;
    private String rechargetype = "recharge";// 默认为钱包充值
    private String moneynumber;// 钱包充值
    private String trade_no, notify_url;
    private TextView backTextView;
    private String car_no;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        addActivity(this);
        init();
    }

    public void init() {
        topLayout = (FrameLayout) findViewById(R.id.alipay_top_layout);
        setTopLayoutPadding(topLayout);
        Button payButton = (Button) findViewById(R.id.alipay_paybutton);
        payButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                check();
            }
        });

        backTextView = (TextView) findViewById(R.id.alipay_backtextview);
        backTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        subjectTextView = (TextView) findViewById(R.id.alipay_product_subject);
        introduceTextView = (TextView) findViewById(R.id.alipay_product_introduce);
        priceTextView = (TextView) findViewById(R.id.alipay_product_price);
        if (getIntent().getStringExtra("type") != null) {
            rechargetype = getIntent().getStringExtra("type");
        }
        if (getIntent().getStringExtra("car_no") != null) {
            car_no = getIntent().getStringExtra("car_no");
        }
        if (getIntent().getStringExtra("date") != null) {
            date = getIntent().getStringExtra("date");
        }
        moneynumber = getIntent().getStringExtra("money");

        if ("recharge".equals(rechargetype)) {
            priceTextView.setText(moneynumber);
            subjectTextView.setText("钱包充值");
            introduceTextView.setText("地上铁APP钱包充值");
        } else {
            subjectTextView.setText(getIntent().getStringExtra("infor"));
            introduceTextView.setText(getIntent().getStringExtra("infor2"));
        }
        Map<String, String> map = new HashMap<String, String>();

        if ("pay_weizhang".equals(rechargetype)) {//违章支付

            map.put("act", Constent.ACT_WEI_ZHANG_ORDER);
            map.put("pay_way", "alipay");
            map.put("car_no", car_no);
            map.put("date", date);
            AnsynHttpRequest.httpRequest(AliPayActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_WEI_ZHANG_ORDER,
                    map, false, true, true);
        } else if ("pay".equals(rechargetype)) {// 预付款
            map.put("act", Constent.ACT_CREATE_ORDER_PAY);
            map.put("pay_way", "alipay");
            map.put("ver", Constent.VER);
            map.put("order_no", getIntent().getStringExtra("order_no"));
            AnsynHttpRequest.httpRequest(AliPayActivity.this, AnsynHttpRequest.GET, callBack,
                    Constent.ID_CREATE_ORDER_PAY, map, false, true, true);
        } else if ("rent_car_yajin".equals(rechargetype)) {   //押金充值
            map.put("act", Constent.ACT_YAJINCONGZHI);
            map.put("pay_way", "alipay");
            map.put("money", getIntent().getStringExtra("money"));
            priceTextView.setText(getIntent().getStringExtra("money"));
            AnsynHttpRequest.httpRequest(AliPayActivity.this,
                    AnsynHttpRequest.GET, callBack,
                    Constent.ID_ACT_YAJINCONGZHI, map, false, true, true);
        } else {// 钱包充值
            map.put("act", Constent.ACT_CREATE_RECHARGE);
            map.put("pay_way", "alipay");
            map.put("ver", Constent.VER);
            map.put("money", PublicUtil.toUTF(getIntent().getStringExtra("money")));
            AnsynHttpRequest.httpRequest(AliPayActivity.this, AnsynHttpRequest.GET, callBack,
                    Constent.ID_CREATE_RECHARGE, map, false, true, true);
        }

    }

    private JSONObject jsonObject = null;
    /**
     * http请求回调
     */
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @SuppressLint("NewApi")
        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_CREATE_ORDER_PAY:// 获取支付交易号
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_order_pay);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_order_pay;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case Constent.ID_ACT_WEI_ZHANG_ORDER:// 获取违章支付交易号
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_order_pay_wz);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_order_pay_wz;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case Constent.ID_CREATE_RECHARGE:     //钱包
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_recharge);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_recharge;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;

                case Constent.ID_CHECK_IS_PAY_SUCCESS:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_yanzheng);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_yanzheng;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;
                case Constent.ID_ACT_YAJINCONGZHI:    //押金充值
                    if (!isString) {
                        try {
                            String backstr = jsonArray.getString(1);
                            final JSONObject jsonObject = new JSONObject(backstr);
                            if ("0".equals(jsonObject.getString("errcode"))) {
                                JSONObject dataObject = jsonObject.getJSONObject("data");
                                if (dataObject != null) {
                                    trade_no = dataObject.getString("trade_no");
                                    notify_url = dataObject.getString("notify_url");
                                    moneynumber = dataObject.getString("total_fee");
                                    notify_url = notify_url.replace("\\", "");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            priceTextView.setText(moneynumber);
                                        }
                                    });
                                } else {
                                    PublicUtil.showToast(AliPayActivity.this, "配置解析数据错误，请退出重试", false);
                                }

                            } else {
                                PublicUtil.showToast(AliPayActivity.this,
                                        jsonObject.getString("msg"), false);
                                if ("1002".equals(jsonObject.getString("errcode"))) {
                                    Intent intent = new Intent(AliPayActivity.this,
                                            LoginActivity.class);
                                    startActivity(intent);

                                }

                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();

                        }
                    }
                    break;
                default:
                    break;
            }

        }
    };

    private int error_httprequest_order_pay = 0x7800;
    private int success_httprequest_order_pay = 0x7801;
    private int SDK_PAY_FLAG = 0x7802;
    private int SDK_CHECK_FLAG = 0x7803;
    private int error_httprequest_recharge = 0x7804;
    private int success_httprequest_recharge = 0x7805;
    private int error_httprequest_yanzheng = 0x7806;
    private int success_httprequest_yanzheng = 0x7807;
    private int error_httprequest_order_pay_wz = 0x7808;
    private int success_httprequest_order_pay_wz = 0x7809;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == SDK_PAY_FLAG) {
                PayResult payResult = new PayResult((String) msg.obj);

                // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                @SuppressWarnings("unused")
                String resultInfo = payResult.getResult();

                String resultStatus = payResult.getResultStatus();

                // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                if (TextUtils.equals(resultStatus, "9000")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("act", Constent.ACT_CHECK_IS_PAY_SUCCESS);
                    map.put("code", trade_no);
                    //充值成功跳转

                    if ("recharge".equals(rechargetype) || "rent_car_yajin".equals(rechargetype)) { //押金和电卡充值成功
                        Intent intent = new Intent(AliPayActivity.this,
                                MyWalletActivity.class);
                        startActivity(intent);
                        finish();
                    } else if ("pay".equals(rechargetype)) {  //预付款
                        Intent intent = new Intent(AliPayActivity.this,
                                NewOrderListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if ("pay_weizhang".equals(rechargetype)) {
                        map.put("type", "wz_pay");
                    } else if ("pay".equals(rechargetype)) {
                        map.put("type", "order_pay");
                    } else {
                        map.put("type", "recharge_pay");
                    }

                    AnsynHttpRequest.httpRequest(AliPayActivity.this, AnsynHttpRequest.GET, callBack,
                            Constent.ID_CHECK_IS_PAY_SUCCESS, map, false, true, true);

                } else {
                    // 判断resultStatus 为非“9000”则代表可能支付失败
                    // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                    if (TextUtils.equals(resultStatus, "8000")) {

                        PublicUtil.showToast(AliPayActivity.this, "支付结果确认中", false);

                    } else {
                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        PublicUtil.showToast(AliPayActivity.this, "支付失败", false);
                        if ("recharge".equals(rechargetype) || "rent_car_yajin".equals(rechargetype)) { //押金和电卡充值成功
                            Intent intent = new Intent(AliPayActivity.this,
                                    MyWalletActivity.class);
                            startActivity(intent);
                            finish();
                        } else if ("pay".equals(rechargetype)) {  //预付款
                            Intent intent = new Intent(AliPayActivity.this,
                                    NewOrderListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            } else if (msg != null && msg.what == SDK_CHECK_FLAG) {
                PublicUtil.logDbug(TAG, "检查结果为：" + msg.obj, 0);
                if ("true".equals(msg.obj.toString())) {

                    if (trade_no == null) {
                        PublicUtil.showToast(AliPayActivity.this, "当前订单号获取失败。请退出重新请求", false);
                        return;
                    }
                    if (notify_url == null) {
                        PublicUtil.showToast(AliPayActivity.this, "服务器端连接请求失败，请退出后重新请求", false);
                        return;
                    }
                    pay();

                } else {
                    PublicUtil.showToast(AliPayActivity.this, "未检测到支付宝客户端，无法操作。请下载", false);
                }
            }
            if (msg != null && msg.what == error_httprequest_order_pay_wz) {
                if (msg.obj != null) {
                    PublicUtil.showToast(AliPayActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_order_pay_wz) {
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);
                        if ("0".equals(jsonObject.getString("errcode"))) {
                            PublicUtil.logDbug(TAG, jsonObject.get("data") + "", 0);
                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            if (dataobject != null) {
                                trade_no = dataobject.getString("trade_no");
                                notify_url = dataobject.getString("notify_url");
                                moneynumber = dataobject.getString("total_fee");
                                notify_url = notify_url.replace("\\", "");
                                priceTextView.setText(moneynumber);

                            } else {
                                PublicUtil.showToast(AliPayActivity.this, "配置解析数据错误，请退出重试", false);
                            }

                        } else {
                            PublicUtil.showToast(AliPayActivity.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(AliPayActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == error_httprequest_order_pay) {
                if (msg.obj != null) {
                    PublicUtil.showToast(AliPayActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_order_pay) {
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);
                        if ("0".equals(jsonObject.getString("errcode"))) {
                            PublicUtil.logDbug(TAG, jsonObject.get("data") + "", 0);
                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            if (dataobject != null) {
                                trade_no = dataobject.getString("trade_no");
                                notify_url = dataobject.getString("notify_url");
                                moneynumber = dataobject.getString("total_fee");
                                notify_url = notify_url.replace("\\", "");
                                priceTextView.setText(moneynumber);
                            }

                        } else {
                            PublicUtil.showToast(AliPayActivity.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(AliPayActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == error_httprequest_recharge) {
                if (msg.obj != null) {
                    PublicUtil.showToast(AliPayActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_recharge) {
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);
                        if ("0".equals(jsonObject.getString("errcode"))) {
                            PublicUtil.logDbug(TAG, jsonObject.get("data") + "", 0);
                            JSONObject dataobject = jsonObject.getJSONObject("data");

                            if (dataobject != null) {
                                trade_no = dataobject.getString("trade_no");
                                notify_url = dataobject.getString("notify_url");
                                moneynumber = dataobject.getString("total_fee");
                                notify_url = notify_url.replace("\\", "");
                                PublicUtil.logDbug(TAG, notify_url, 0);

                            } else {
                                PublicUtil.showToast(AliPayActivity.this, "配置解析数据错误，请退出重试", false);
                            }

                        } else {
                            PublicUtil.showToast(AliPayActivity.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(AliPayActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(AliPayActivity.this, "配置解析数据错误，请退出重试", false);
                    }

                }

            } else if (msg != null && msg.what == error_httprequest_yanzheng) {
                if (msg.obj != null) {
                    PublicUtil.showToast(AliPayActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_yanzheng) {
                if (jsonObject != null) {
                    try {
                        if (jsonObject.getString("errcode").equals("0")) {
                            //支付成功
                            Toast.makeText(AliPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }
    };

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        String subject = subjectTextView.getText().toString();
        String body = introduceTextView.getText().toString();
        String price = priceTextView.getText().toString();
        if (price == null) {
            PublicUtil.showToast(AliPayActivity.this, "支付信息获取失败，请退出后重新操作", false);
            return;

        }
        // 订单
        String orderInfo = getOrderInfo(subject, body, price);

        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(AliPayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check() {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(AliPayActivity.this);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();
                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                handler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + trade_no + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        // orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        PublicUtil.logDbug(TAG, orderInfo, 0);

        return orderInfo;
    }

    // /**
    // * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
    // *
    // */
    // public String getOutTradeNo() {
    // SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
    // Locale.getDefault());
    // Date date = new Date();
    // String key = format.format(date);
    //
    // Random r = new Random();
    // key = key + r.nextInt();
    // key = key.substring(0, 15);
    // return key;
    // }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    /* 押金充值调用*/
    public static void intentMeToChargeDeposit(BaseActivity act, String money) {
        Intent intent = new Intent(act, AliPayActivity.class);
        intent.putExtra("type", "rent_car_yajin");
        intent.putExtra("infor", "租车押金充值");
        intent.putExtra("infor2", "地上铁APP租车押金充值");
        intent.putExtra("money", money);
        act.startActivity(intent);
    }

    /* 租金支付*/
    public static void intentMeToRentPay(BaseActivity act, String order_no) {
        Intent intent = new Intent(act, AliPayActivity.class);
        intent.putExtra("type", "pay");
        intent.putExtra("infor", "租车押金充值");
        intent.putExtra("infor2", "地上铁APP租车支付");
        intent.putExtra("order_no", order_no);
        act.startActivity(intent);
    }

}
