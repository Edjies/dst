package com.haofeng.apps.dst.wxapi;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Xml;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.haofeng.apps.dst.ui.LoginActivity;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WXPayActivity extends BaseActivity {
    private final String TAG = WXPayActivity.class.getSimpleName();
    private FrameLayout topLayout;

    private StringBuffer sb;
    private Map<String, String> resultunifiedorder;
    private PayReq req;
    private IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private ProgressDialog dialog;
    private TextView subjectTextView, introduceTextView, priceTextView,
            backTextView;
    private String moneynumber, notify_url, trade_no;// 默认钱包充值
    private String rechargetype = "recharge";// 默认钱包充值
    private String car_no;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        addActivity(this);
        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(Constants.APP_ID);
        init();
    }

    public void init() {
        topLayout = (FrameLayout) findViewById(R.id.wxpay_top_layout);
       setTopLayoutPadding(topLayout);
        Button payButton = (Button) findViewById(R.id.wxpay_paybutton);
        payButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Pay();
            }
        });
        backTextView = (TextView) findViewById(R.id.wxpay_backtextview);
        backTextView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        subjectTextView = (TextView) findViewById(R.id.wxpay_product_subject);
        introduceTextView = (TextView) findViewById(R.id.wxpay_product_introduce);
        priceTextView = (TextView) findViewById(R.id.wxpay_product_price);
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

        PublicUtil.setStorage_string(WXPayActivity.this, "rechargetype",
                rechargetype);

        if ("recharge".equals(rechargetype)) {
            priceTextView.setText(moneynumber);
            subjectTextView.setText("钱包充值");
            introduceTextView.setText("地上铁APP钱包充值");
        } else {
            subjectTextView.setText(getIntent().getStringExtra("infor"));
            introduceTextView.setText(getIntent().getStringExtra("infor2"));
        }

        if (!isWeixinAvilible()) {

            PublicUtil.showToast(WXPayActivity.this,
                    "温馨提示：当前未检测到微信客户端，将会导致支付失败", false);
        }

        Map<String, String> map = new HashMap<String, String>();
        if ("pay_weizhang".equals(rechargetype)) {// 违章支付
            map.put("act", Constent.ACT_WEI_ZHANG_ORDER);
            map.put("pay_way", "wechat");
            map.put("car_no", car_no);
            map.put("date", date);
            AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_ACT_WEI_ZHANG_ORDER,
                    map, false, true, true);
        } else if ("pay".equals(rechargetype)) {// 预付款

            map.put("act", Constent.ACT_CREATE_ORDER_PAY);
            map.put("pay_way", "wechat");
            map.put("order_no", getIntent().getStringExtra("order_no"));
            AnsynHttpRequest.httpRequest(WXPayActivity.this,
                    AnsynHttpRequest.GET, callBack,
                    Constent.ID_CREATE_ORDER_PAY, map, false, false, true);
        } else if ("rent_car_yajin".equals(rechargetype)) {  //押金充值
            map.put("act", Constent.ACT_YAJINCONGZHI);
            map.put("pay_way", "wechat");
            map.put("money", getIntent().getStringExtra("money"));
            AnsynHttpRequest.httpRequest(WXPayActivity.this,
                    AnsynHttpRequest.GET, callBack,
                    Constent.ID_ACT_YAJINCONGZHI, map, false, false, true);

        } else {// 钱包充值
            map.put("act", Constent.ACT_CREATE_RECHARGE);
            map.put("pay_way", "wechat");
            map.put("money", getIntent().getStringExtra("money"));
            AnsynHttpRequest.httpRequest(WXPayActivity.this,
                    AnsynHttpRequest.GET, callBack,
                    Constent.ID_CREATE_RECHARGE, map, false, false, true);
        }

    }

    /**
     * 是否安装了微信app
     *
     * @return
     */
    public boolean isWeixinAvilible() {
        PackageManager packageManager = this.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }

        return false;
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
                case Constent.ID_CREATE_RECHARGE:
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
                                    PublicUtil.showToast(WXPayActivity.this, "配置解析数据错误，请退出重试", false);
                                }

                            } else {
                                PublicUtil.showToast(WXPayActivity.this,
                                        jsonObject.getString("msg"), false);
                                if ("1002".equals(jsonObject.getString("errcode"))) {
                                    Intent intent = new Intent(WXPayActivity.this,
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

    private int error_httprequest_order_pay = 0x7900;
    private int success_httprequest_order_pay = 0x7901;
    private int error_httprequest_order_pay_wz = 0x7902;
    private int success_httprequest_order_pay_wz = 0x7903;
    private int error_httprequest_recharge = 0x7904;
    private int success_httprequest_recharge = 0x7905;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_httprequest_order_pay_wz) {
                if (msg.obj != null) {
                    PublicUtil.showToast(WXPayActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null
                    && msg.what == success_httprequest_order_pay_wz) {
                if (jsonObject != null) {

                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            PublicUtil.logDbug(TAG,
                                    jsonObject.get("data") + "", 0);
                            JSONObject object = jsonObject
                                    .getJSONObject("data");

                            if (object != null) {
                                trade_no = object.getString("trade_no");
                                notify_url = object.getString("notify_url");
                                moneynumber = object.getString("total_fee");
                                notify_url = notify_url.replace("\\", "");
                                priceTextView.setText(moneynumber);
                            } else {
                                PublicUtil.showToast(WXPayActivity.this,
                                        "配置解析数据错误，请退出重试", false);
                                finish();
                            }

                        } else {
                            PublicUtil.showToast(WXPayActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(WXPayActivity.this,
                                        LoginActivity.class);
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
                    PublicUtil.showToast(WXPayActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_recharge) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            PublicUtil.logDbug(TAG,
                                    jsonObject.get("data") + "", 0);
                            JSONObject object = jsonObject
                                    .getJSONObject("data");

                            if (object != null) {
                                trade_no = object.getString("trade_no");
                                notify_url = object.getString("notify_url");
                                moneynumber = object.getString("total_fee");
                                notify_url = notify_url.replace("\\", "");
                                // Log.d(TAG, notify_url);
                            } else {
                                PublicUtil.showToast(WXPayActivity.this,
                                        "配置解析数据错误，请退出重试", false);
                                finish();
                            }

                        } else {
                            PublicUtil.showToast(WXPayActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(WXPayActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        PublicUtil.showToast(WXPayActivity.this,
                                "配置解析数据错误，请退出重试", false);
                    }

                }

            }
            if (msg != null && msg.what == error_httprequest_order_pay) {
                if (msg.obj != null) {
                    PublicUtil.showToast(WXPayActivity.this,
                            msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_order_pay) {
                if (jsonObject != null) {

                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            PublicUtil.logDbug(TAG,
                                    jsonObject.get("data") + "", 0);
                            JSONObject object = jsonObject
                                    .getJSONObject("data");

                            if (object != null) {
                                trade_no = object.getString("trade_no");
                                notify_url = object.getString("notify_url");
                                moneynumber = object.getString("total_fee");
                                notify_url = notify_url.replace("\\", "");
                                priceTextView.setText(moneynumber);
                                // Log.d(TAG, notify_url);
                            } else {
                                PublicUtil.showToast(WXPayActivity.this,
                                        "配置解析数据错误，请退出重试", false);
                                finish();
                            }

                        } else {
                            PublicUtil.showToast(WXPayActivity.this,
                                    jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(WXPayActivity.this,
                                        LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                }

            }

        }

        ;
    };

    /**
     * 微信支付开始
     */
    public void Pay() {
        // 生成prepay_id
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
        PublicUtil.setStorage_string(this, "rechargetype", rechargetype);
    }


    //这里就是返回prepay_id的
    private class GetPrepayIdTask extends
            AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(WXPayActivity.this, null,
                    "正在获取预支付订单...");
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            // if (dialog != null) {
            // dialog.dismiss();
            // }
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");

            resultunifiedorder = result;
            // 生成签名参数
            genPayReq();

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String
                    .format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();

            PublicUtil.logDbug("orion", entity, 0);

            byte[] buf = Util.httpPost(url, entity);

            String content = new String(buf);
            PublicUtil.logDbug("orion", content, 0);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }

    private String genProductArgs() {

        PublicUtil.logDbug(TAG, trade_no, 0);
        int total_fee = (int) (Float.parseFloat(moneynumber) * 100);

        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams
                    .add(new BasicNameValuePair("appid", Constants.APP_ID));
            if ("1".equals(rechargetype)) {

                packageParams.add(new BasicNameValuePair("body", "zhifu"));// 商品描述

            } else {
                packageParams.add(new BasicNameValuePair("body", "账户充值"));// 商品描述

            }

            packageParams
                    .add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", notify_url));
            packageParams.add(new BasicNameValuePair("out_trade_no", trade_no));
            packageParams.add(new BasicNameValuePair("spbill_create_ip",
                    "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", String
                    .valueOf(total_fee)));// 商品金额,以分为单位
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);
            return new String(xmlstring.toString().getBytes(), "ISO8859-1");
            // return xmlstring;

        } catch (Exception e) {
            PublicUtil.logDbug(TAG,
                    "genProductArgs fail, ex = " + e.getMessage(), 0);
            return null;
        }

    }

    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            PublicUtil.logDbug("orion", e.toString(), 2);
        }
        return null;

    }

    @SuppressWarnings("unused")
    private String genOutTradNo() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
                .getBytes());
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        PublicUtil.logDbug("orion4", sb.toString(), 0);
        return sb.toString();
    }

    /**
     * 生成签名
     */

    @SuppressLint("DefaultLocale")
    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        PublicUtil.logDbug("orion3", packageSign, 0);
        return packageSign;
    }

    /**
     * 生成签名参数
     */
    private void genPayReq() {

        if (dialog != null) {
            dialog.setMessage("生成签名参数");
        }

        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);

        sb.append("sign\n" + req.sign + "\n\n");
        PublicUtil.logDbug("orion1", signParams.toString(), 0);
        // 支付
        sendPayReq();

    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    @SuppressLint("DefaultLocale")
    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes())
                .toUpperCase();
        PublicUtil.logDbug("orion2", appSign, 0);
        return appSign;
    }

    /**
     * 支付
     */
    private void sendPayReq() {
        if (dialog != null) {
            dialog.setMessage("正在支付");
        }

        msgApi.registerApp(Constants.APP_ID);
        msgApi.sendReq(req);

        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /* 押金充值调用*/
    public static void intentMeToChargeDeposit(BaseActivity act, String money) {
        Intent intent = new Intent(act, WXPayActivity.class);
        intent.putExtra("type", "rent_car_yajin");
        intent.putExtra("infor", "租车押金充值");
        intent.putExtra("infor2", "地上铁APP租车押金充值");
        intent.putExtra("money", money);
        act.startActivity(intent);
    }

    /* 租金支付*/
    public static void intentMeToRentPay(BaseActivity act, String order_no) {
        Intent intent = new Intent(act, WXPayActivity.class);
        intent.putExtra("type", "pay");
        intent.putExtra("infor", "租车押金充值");
        intent.putExtra("infor2", "地上铁APP租车支付");
        intent.putExtra("order_no", order_no);
        act.startActivity(intent);
    }

}
