package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.alipay.AliPayActivity;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.wxapi.WXPayActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.haofeng.apps.dst.R.id.carrestlistdetail_zujinheji;
import static java.lang.Float.parseFloat;

/**
 * 预付款订单详情界面
 *
 * @author qtds
 */
public class CarRestListDetailActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "CarRestListDetailActivity";
    private FrameLayout topLayout;
    private TextView backView;
    private TextView diandanhaoView, qucheshijianView, haicheshijianView;
    private TextView yongcheshichangView, zujinhejiView, zujinyajinnView, lichengfeiyongView, baoxianfuwuView;
    private LinearLayout lichengfeiyongLayout;
    private TextView hejiyufukuanView, qianbaoyueView, qianbaoyueView2;
    private RadioButton yueRadioButton, wxRadioButton, zfbRadioButton;
    private RadioGroup radioGroup;
    private FrameLayout zhifufangshiLayout;
    private TextView zhifufsView;
    private ImageView zhifufsImageView;
    private TextView ok_moneyView, okView, ok_timeView;
    private String order_no;
    private String type = "list";// 默认从订单列表过来
    private float zujin = 0, yajin = 0, zongji = 0;
    private float money = 0;
    private String HEAD = "请在", FOOT = "分钟内完成付款";

    private int zhifu_time = 30;
    private JSONObject dataObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrestlistdetail);
       addActivity(this);
        init();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    /*
     * 初始化组件
     */
    public void init() {

        topLayout = (FrameLayout) findViewById(R.id.carrestlistdetail_top_layout);
        setTopLayoutPadding(topLayout);

        backView = (TextView) findViewById(R.id.carrestlistdetail_back);
        diandanhaoView = (TextView) findViewById(R.id.carrestlistdetail_dingdanhao);
        qucheshijianView = (TextView) findViewById(R.id.carrestlistdetail_qucheshijian);
        haicheshijianView = (TextView) findViewById(R.id.carrestlistdetail_haicheshijian);

        yongcheshichangView = (TextView) findViewById(R.id.carrestlistdetail_yongcheshichang);
        zujinhejiView = (TextView) findViewById(carrestlistdetail_zujinheji);
        zujinyajinnView = (TextView) findViewById(R.id.carrestlistdetail_zujinyajin);
        lichengfeiyongView = (TextView) findViewById(R.id.carrestlistdetail_lichengfeiyong);
        baoxianfuwuView = (TextView) findViewById(R.id.carrestlistdetail_baoxianfuwu);
        lichengfeiyongLayout = (LinearLayout) findViewById(R.id.carrestlistdetail_lichengfeiyong_layout);

        hejiyufukuanView = (TextView) findViewById(R.id.carrestlistdetail_hejiyufukuan);
        qianbaoyueView = (TextView) findViewById(R.id.carrestlistdetail_qianbaoyue);
        qianbaoyueView2 = (TextView) findViewById(R.id.carrestlistdetail_qianbaoyue2);
        yueRadioButton = (RadioButton) findViewById(R.id.carrestlistdetail_zhifu_radio_yue);
        zfbRadioButton = (RadioButton) findViewById(R.id.carrestlistdetail_zhifu_radio_zhifubao);
        wxRadioButton = (RadioButton) findViewById(R.id.carrestlistdetail_zhifu_radio_weixin);
        radioGroup = (RadioGroup) findViewById(R.id.carrestlistdetail_zhifu_radiogroup);
        zhifufangshiLayout = (FrameLayout) findViewById(R.id.carrestlistdetail_zhifu_layout);
        zhifufsView = (TextView) findViewById(R.id.carrestlistdetail_zhifu_view);
        zhifufsImageView = (ImageView) findViewById(R.id.carrestlistdetail_zhifu_image);

        ok_moneyView = (TextView) findViewById(R.id.carrestlistdetail_ok_money);
        ok_timeView = (TextView) findViewById(R.id.carrestlistdetail_ok_time);
        okView = (TextView) findViewById(R.id.carrestlistdetail_ok);
        backView.setOnClickListener(this);
        zhifufangshiLayout.setOnClickListener(this);
        okView.setOnClickListener(this);

        zhifufsView.setText("支付宝支付");
        zhifufsImageView.setBackgroundResource(R.drawable.image_down);

        ok_timeView.setText(HEAD + zhifu_time + FOOT);

        order_no = getIntent().getStringExtra("order_no");
        type = getIntent().getStringExtra("type");
        diandanhaoView.setText(getIntent().getStringExtra("order_no"));

        if (!TextUtils.isEmpty(getIntent().getStringExtra("licheng"))) {
            lichengfeiyongView.setText(getIntent().getStringExtra("licheng"));
            lichengfeiyongLayout.setVisibility(View.VISIBLE);
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_ORDER_GET_DETAIL);
        map.put("order_no", order_no);

        AnsynHttpRequest.httpRequest(CarRestListDetailActivity.this, AnsynHttpRequest.GET, callBack,
                Constent.ID_ORDER_GET_DETAIL, map, false, true, true);

        zfbRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    zhifufsView.setText("支付宝支付");
                }

            }
        });

        wxRadioButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    zhifufsView.setText("微信支付");
                }

            }
        });

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.carrestlistdetail_back:
                if ("new".equals(type)) {
                    Intent intent = new Intent(CarRestListDetailActivity.this, CarRestlistActivity.class);
                    intent.putExtra("isPayed", "0");
                    startActivity(intent);
                }
                finish();
                break;

            case R.id.carrestlistdetail_zhifu_layout:
                if (radioGroup.getVisibility() == View.VISIBLE) {
                    radioGroup.setVisibility(View.GONE);
                    zhifufsImageView.setBackgroundResource(R.drawable.image_right);
                } else {
                    radioGroup.setVisibility(View.VISIBLE);
                    zhifufsImageView.setBackgroundResource(R.drawable.image_down);
                }

                break;
            case R.id.carrestlistdetail_ok:

                Intent intent = null;

                PublicUtil.setStorage_string(CarRestListDetailActivity.this, "order_no", order_no);

                if (wxRadioButton.isChecked()) {
                    intent = new Intent(CarRestListDetailActivity.this, WXPayActivity.class);
                } else {
                    intent = new Intent(CarRestListDetailActivity.this, AliPayActivity.class);
                }

                intent.putExtra("money", String.valueOf(zongji));
                intent.putExtra("type", "pay");
                intent.putExtra("order_no", order_no);
                intent.putExtra("infor", "租车预付款支付");
                intent.putExtra("infor2", "地上铁APP租车预付款支付");
                startActivity(intent);
                finish();

                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if ("new".equals(type)) {
                Intent intent = new Intent(CarRestListDetailActivity.this, CarRestlistActivity.class);
                startActivity(intent);

            }
            finish();
            return true;

        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private JSONObject jsonObject = null, moneyObject;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            // TODO Auto-generated method stub

            switch (backId) {
                case Constent.ID_ORDER_GET_DETAIL:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_http_getcardetail);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getcardetail;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;
                case Constent.ID_MEMBER_INFOR:

                    if (isRequestSuccess) {
                        if (!isString) {

                            try {
                                String backstr = jsonArray.getString(1);
                                moneyObject = new JSONObject(backstr);
                                // backArray = new JSONArray(backstr);
                                // Log.d(TAG, backArray.length() + "");
                                handler.sendEmptyMessage(success_http_getmoney);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }
                    } else {
                        Message message = new Message();
                        message.what = error_http_getmoney;
                        message.obj = data;
                        handler.sendMessage(message);
                    }

                    break;
                case Constent.ID_GET_CAR_DETAIL:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isRequestSuccess) {
                                if (!isString) {
                                    try {
                                        String backstr = jsonArray.getString(1);
                                        JSONObject jsonObject = new JSONObject(backstr);
                                        if (jsonObject.getString("errcode").equals("0")) {
                                            float zhucheyajin = Float.parseFloat(jsonObject.optJSONObject("data").optJSONObject("foregift").getString("payment"));
                                            zujinyajinnView.setText("￥" + PublicUtil.roundByScale(zhucheyajin,2));
                                            float baoxian = 0;
                                            if (!TextUtils.isEmpty(dataObject.getString("safe_amount"))) {
                                                baoxian = Float.parseFloat(dataObject.getString("safe_amount"));
                                            }
                                            if (!TextUtils.isEmpty(dataObject.getString("iop_amount"))) {
                                                baoxian = baoxian + Float.parseFloat(dataObject.getString("iop_amount"));
                                            }
                                            baoxianfuwuView.setText("￥" + PublicUtil.roundByScale(baoxian,2));
                                            if (!TextUtils.isEmpty(dataObject.getString("zc_amount"))) {
                                                zujin = Float.parseFloat(dataObject.getString("zc_amount"));
                                            }

                                            if (!TextUtils.isEmpty(dataObject.getString("order_time"))
                                                    && !"null".equals(dataObject.getString("order_time"))) {
                                                setTime(dataObject.getString("order_time"));

                                            }

                                            zongji = zhucheyajin + zujin + baoxian;

                                            BigDecimal b = new BigDecimal(zongji);
                                            zongji = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();// 两位小数，四舍五入
                                            zujinhejiView.setText("￥"+PublicUtil.roundByScale(zujin,2));
                                            hejiyufukuanView.setText("￥" + PublicUtil.roundByScale(zongji,2));
                                            ok_moneyView.setText("￥" + PublicUtil.roundByScale(zongji,2));

                                        }
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();

                                    }
                                }
                            }

                        }
                    });

                    break;

                default:
                    break;
            }

        }
    };

    private int error_http_getcardetail = 0x7500;
    private int success_http_getcardetail = 0x7501;
    private int error_http_getmoney = 0x7502;
    private int success_http_getmoney = 0x7503;

    private int changetime = 0x7504;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == changetime) {
                zhifu_time = zhifu_time - 1;
                if (zhifu_time < 0) {
                    zhifu_time = 0;
                    if (timer != null) {
                        timer.purge();
                        timer.cancel();
                        timer = null;
                    }
                }

                if (zhifu_time > 0) {
                    ok_timeView.setText(HEAD + zhifu_time + FOOT);
                } else {
                    ok_timeView.setText("支付超时");
                }

            }

            if (msg != null && msg.what == error_http_getcardetail) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarRestListDetailActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getcardetail) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {

                            dataObject = jsonObject.getJSONObject("data").getJSONObject("order");

                            if (dataObject != null) {
                                qucheshijianView.setText(dataObject.getString("qc_time").replace("-", "/"));
                                haicheshijianView.setText(dataObject.getString("hc_time").replace("-", "/"));
                                String car_type_id = dataObject.getString("car_type_id");
                                Map<String, String> map = new HashMap<>();
                                map.put("act", Constent.ACT_GET_CAR_DETAIL);
                                map.put("car_type_id", car_type_id);
                                AnsynHttpRequest.httpRequest(CarRestListDetailActivity.this, AnsynHttpRequest.GET, callBack, Constent.ID_GET_CAR_DETAIL, map, false, false, true);
                            }

                        } else {
                            PublicUtil.showToast(CarRestListDetailActivity.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(CarRestListDetailActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            }

            if (msg != null && msg.what == error_http_getmoney) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarRestListDetailActivity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getmoney) {
                if (moneyObject != null) {
                    try {

                        if ("0".equals(moneyObject.getString("errcode"))) {

                            JSONObject dataObject = moneyObject.getJSONObject("data");

                            if (dataObject != null) {

                                if (!TextUtils.isEmpty(dataObject.getString("amount"))) {
                                    money = parseFloat(dataObject.getString("amount"));
                                    qianbaoyueView.setText("￥" + money);
                                    if (money < zongji) {
                                        qianbaoyueView2.setText("余额不足");

                                    }
                                }
                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }

                }

            }
        }
    };

    private Timer timer;

    /**
     * 设置倒计时
     *
     * @param start
     */
    private void setTime(String start) {

        zhifu_time = PublicUtil.compareHourTime(start);

        zhifu_time = 30 - zhifu_time;

        if (zhifu_time > 1) {
            ok_timeView.setText(HEAD + zhifu_time + FOOT);
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    handler.sendEmptyMessage(changetime);
                }
            }, 60 * 1000, 60 * 1000);
        } else {
            zhifu_time = 0;
            ok_timeView.setText("支付超时");
        }

    }
}
