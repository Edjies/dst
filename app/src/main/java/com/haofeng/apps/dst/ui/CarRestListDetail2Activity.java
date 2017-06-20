package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import static java.lang.Float.parseFloat;

/**
 * 其他状态的订单详情界面
 *
 * @author qtds
 */
public class CarRestListDetail2Activity extends BaseActivity implements OnClickListener {
    private final String TAG = "CarRestListDetailActivity2";
    private LinearLayout contentLayout;
    private LinearLayout topimageLayout;
    private FrameLayout topLayout;
    private TextView backView;
    private ImageView topImageView3, topImageView4, topImageView5;
    private TextView diandanhaoView, dingdanjineView;
    private ImageView dingdanstatusImageView;
    private TextView qucheshijianView, haicheshijianView, haicheshijianView2, yongcheshichangView, chaochushijianView,
            shijilichengView, jianmianView;
    private TextView zuchefeiyongView, xuzuzujinView, chaoshizujinView, baoxianView, lichengfeiyongView,
            qitafeiyongView, feiyonghejiView, feiyonghelpView;
    private TextView cheliangyajinView, weizhangyajinView, cheliangyajinView_status, weizhangyajinView_status;
    private TextView ok_moneyView, okView, ok_moneyinforView;
    private LinearLayout bottomLayout;
    private LinearLayout dingdanfeiyongLayout;
    private TextView yufukuanView, yingfujieView, bujiaojineView, yingtuijineView, helpView;
    private FrameLayout bujiaoLayout, yingtuiLayout;
    private String order_no;
    private float feiyongheji, zhujinfei, weizhangyajin, zucheyajin;

    private String order_status = "1";// 订单状态默认待取车
    private String feiyongqueren = "0";// 默认未确认
    private String wz_foregift_state = "0";// 违章押金结算状态
    private LinearLayout chesunLayout;
    private TextView chesunMoneyTexView;
    private TextView chesunInfoTexView;
    private RelativeLayout yajinNoConfirmLayout;
    private LinearLayout yajinConfirmLayout;
    private CheckBox isReturnYajinCheckbox;
    private float cheshun = 0;
    private String cheshunRemark;
    private FrameLayout feiyongtotalLayout;
    private float zc_amount;
    private TextView yajinInfoTextView;
    private TextView xuzhuStatusTextView;
    private TextView caoshiStatusTextView;
    private TextView lichenStatusTextView;
    private TextView qitaStatusTextView;
    private TextView cheshunStatusTextView;
    private RelativeLayout xuzhuLayout;
    private RelativeLayout chashiLayout;
    private RelativeLayout lichenLayout;
    private RelativeLayout qitaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_carrestlistdetail2);
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
        topLayout = (FrameLayout) findViewById(R.id.carrestlistdetail2_top_layout);
        setTopLayoutPadding(topLayout);
        topimageLayout = (LinearLayout) findViewById(R.id.carrestlistdetail2_top_image_layout);
        contentLayout = (LinearLayout) findViewById(R.id.carrestlistdetail2_content_layout);
        backView = (TextView) findViewById(R.id.carrestlistdetail2_back);
        topImageView3 = (ImageView) findViewById(R.id.carrestlistdetail2_top_image3);
        topImageView4 = (ImageView) findViewById(R.id.carrestlistdetail2_top_image4);
        topImageView5 = (ImageView) findViewById(R.id.carrestlistdetail2_top_image5);
        diandanhaoView = (TextView) findViewById(R.id.carrestlistdetail2_dingdanhao);
        dingdanjineView = (TextView) findViewById(R.id.carrestlistdetail2_dingdanjine);
        dingdanstatusImageView = (ImageView) findViewById(R.id.carrestlistdetail2_dingdantype_image);
        qucheshijianView = (TextView) findViewById(R.id.carrestlistdetail2_qucheshijian);
        haicheshijianView = (TextView) findViewById(R.id.carrestlistdetail2_haicheshijian);
        haicheshijianView2 = (TextView) findViewById(R.id.carrestlistdetail2_haicheshijian_shiji);
        yongcheshichangView = (TextView) findViewById(R.id.carrestlistdetail2_yongcheshichang);
        chaochushijianView = (TextView) findViewById(R.id.carrestlistdetail2_chaochushijian);
        shijilichengView = (TextView) findViewById(R.id.carrestlistdetail2_shijixingshilicheng);
        jianmianView = (TextView) findViewById(R.id.carrestlistdetail2_jianmianlicheng);

        zuchefeiyongView = (TextView) findViewById(R.id.carrestlistdetail2_zuchefeiyong);
        xuzuzujinView = (TextView) findViewById(R.id.carrestlistdetail2_xuzuzujin);
        chaoshizujinView = (TextView) findViewById(R.id.carrestlistdetail2_chaoshizujin);
        baoxianView = (TextView) findViewById(R.id.carrestlistdetail2_baoxianjine);
        lichengfeiyongView = (TextView) findViewById(R.id.carrestlistdetail2_lichengfeiyong);
        qitafeiyongView = (TextView) findViewById(R.id.carrestlistdetail2_qitafeiyong);
        feiyonghejiView = (TextView) findViewById(R.id.carrestlistdetail2_feiyongheji);
        feiyonghelpView = (TextView) findViewById(R.id.carrestlistdetail2_feiyong_help);
        ok_moneyView = (TextView) findViewById(R.id.carrestlistdetail2_ok_money);
        okView = (TextView) findViewById(R.id.carrestlistdetail2_ok);

        bottomLayout = (LinearLayout) findViewById(R.id.carrestlistdetail2_bottomlayout);

        dingdanfeiyongLayout = (LinearLayout) findViewById(R.id.carrestlistdetail2_dingdanfeiyonglayout);
        yufukuanView = (TextView) findViewById(R.id.carrestlistdetail2_dingdanfeiyong_yufukuan);
        yingfujieView = (TextView) findViewById(R.id.carrestlistdetail2_dingdanfeiyong_yingfujine);
        bujiaojineView = (TextView) findViewById(R.id.carrestlistdetail2_dingdanfeiyong_bujiaojine);
        yingtuijineView = (TextView) findViewById(R.id.carrestlistdetail2_dingdanfeiyong_yingtuijine);
        bujiaoLayout = (FrameLayout) findViewById(R.id.carrestlistdetail2_dingdanfeiyong_bujiaojine_layout);
        yingtuiLayout = (FrameLayout) findViewById(R.id.carrestlistdetail2_dingdanfeiyong_yingtuijine_layout);
        ok_moneyinforView = (TextView) findViewById(R.id.carrestlistdetail2_ok_money_infor);
        yajinInfoTextView = (TextView) findViewById(R.id.carrestlistdetail2_yajin_info);


        chesunMoneyTexView = (TextView) findViewById(R.id.carrestlistdetail2_cheshun_money);
        chesunInfoTexView = (TextView) findViewById(R.id.carrestlistdetail2_cheshun_info);

        //押金
        yajinConfirmLayout = (LinearLayout) findViewById(R.id.carrestlistdetail2_zucheyajin_confirm_layout);

        //订单合计
        feiyongtotalLayout = (FrameLayout) findViewById(R.id.carrestlistdetail2_feiyongheji_layout);
        helpView = (TextView) findViewById(R.id.carrestlistdetail2_dingdanfeiyong_help);
        helpView.setOnClickListener(this);
        //初始化显示状态
        yajinConfirmLayout.setVisibility(View.GONE);
        feiyongtotalLayout.setVisibility(View.GONE);

        xuzhuLayout = (RelativeLayout) findViewById(R.id.carrestlistdetail2_xuzu_layout);
        chashiLayout = (RelativeLayout) findViewById(R.id.carrestlistdetail2_chaoshi_layout);
        lichenLayout = (RelativeLayout) findViewById(R.id.carrestlistdetail2_lichen_layout);
        qitaLayout = (RelativeLayout) findViewById(R.id.carrestlistdetail2_qita_layout);
        chesunLayout = (LinearLayout) findViewById(R.id.carrestlistdetail2_cheshun_layout);

        xuzhuStatusTextView = (TextView) findViewById(R.id.xuzhu_nopay);
        caoshiStatusTextView = (TextView) findViewById(R.id.caoshi_nopay);
        lichenStatusTextView = (TextView) findViewById(R.id.licheng_nopay);
        qitaStatusTextView = (TextView) findViewById(R.id.qita_nopay);
        cheshunStatusTextView = (TextView) findViewById(R.id.chesun_nopay);

        xuzhuStatusTextView.setVisibility(View.GONE);
        caoshiStatusTextView.setVisibility(View.GONE);
        lichenStatusTextView.setVisibility(View.GONE);
        qitaStatusTextView.setVisibility(View.GONE);
        cheshunStatusTextView.setVisibility(View.GONE);


        backView.setOnClickListener(this);
        okView.setOnClickListener(this);
        feiyonghelpView.setOnClickListener(this);
        order_no = getIntent().getStringExtra("order_no");
        //设置押金
        diandanhaoView.setText(order_no);
        initTopimage(2);

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_ORDER_GET_DETAIL);
        map.put("order_no", order_no);

        AnsynHttpRequest.httpRequest(CarRestListDetail2Activity.this, AnsynHttpRequest.GET, callBack,
                Constent.ID_ORDER_GET_DETAIL, map, false, true, true);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.carrestlistdetail2_back:

                finish();
                break;

            case R.id.carrestlistdetail2_ok:
                Map<String, String> map = new HashMap<String, String>();
                map.put("act", Constent.ACT_ORDER_AMOUNT_CONFIRMT);
                map.put("order_no", order_no);
                AnsynHttpRequest.httpRequest(CarRestListDetail2Activity.this, AnsynHttpRequest.GET, callBack,
                        Constent.ID_ORDER_AMOUNT_CONFIRMT, map, false, true, true);
                break;

            case R.id.carrestlistdetail2_dingdanfeiyong_help:
                feiyongSM();
                break;
            case R.id.carrestlistdetail2_weizhanchakan:
                Intent intent = new Intent(CarRestListDetail2Activity.this, WeiZhangjiesuanActivity.class);
                intent.putExtra("order_no", order_no);
                intent.putExtra("wz_foregift_state", wz_foregift_state);
                startActivity(intent);
                break;

            case R.id.carrestlistdetail2_feiyong_help:
                feiyongSM2();
                break;

            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;

        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    private JSONObject jsonObject = null;
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {

            switch (backId) {
                case Constent.ID_ORDER_AMOUNT_CONFIRMT:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_httprequest_queren);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_httprequest_queren;
                            message.obj = data;
                            handler.sendMessage(message);
                        }
                    }
                    break;

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

                default:
                    break;
            }
            // TODO Auto-generated method stub

        }
    };

    private int error_http_getcardetail = 0x7500;
    private int success_http_getcardetail = 0x7501;
    private int error_httprequest_queren = 0x7502;
    private int success_httprequest_queren = 0x7503;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg != null && msg.what == error_http_getcardetail) {
                if (msg.obj != null) {
                    PublicUtil.showToast(CarRestListDetail2Activity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_http_getcardetail) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            JSONObject dataObject = jsonObject.getJSONObject("data").getJSONObject("order");

                            if (dataObject != null) {

                                qucheshijianView.setText(dataObject.getString("qc_time").replace("-", "/"));
                                haicheshijianView.setText(dataObject.getString("hc_time").replace("-", "/"));

                                String shijihaiche = dataObject.getString("hc_real_time");
                                if ("null".equals(shijihaiche)) {
                                    shijihaiche = "";
                                }
                                haicheshijianView2.setText(shijihaiche.replace("-", "/"));

                                if (!TextUtils.isEmpty(dataObject.getString("qc_time"))
                                        && !TextUtils.isEmpty(dataObject.getString("hc_time"))) {

                                    yongcheshichangView.setText(PublicUtil.timeWithTwoTime(
                                            dataObject.getString("qc_time"), dataObject.getString("hc_time")));

                                }

                                if (!TextUtils.isEmpty(dataObject.getString("hc_time"))
                                        && !TextUtils.isEmpty(shijihaiche)) {

                                    chaochushijianView.setText(
                                            PublicUtil.timeWithTwoTime(dataObject.getString("hc_time"), shijihaiche));
                                }

                                float hc_mileage = 0, qc_mileage = 0;
                                if (!TextUtils.isEmpty(dataObject.getString("hc_mileage"))) {
                                    hc_mileage = parseFloat(dataObject.getString("hc_mileage"));
                                }

                                if (!TextUtils.isEmpty(dataObject.getString("qc_mileage"))) {
                                    qc_mileage = parseFloat(dataObject.getString("qc_mileage"));
                                }

                                if ((hc_mileage - qc_mileage) > 0) {
                                    shijilichengView.setText(PublicUtil.roundByScale(hc_mileage - qc_mileage, 2) + "KM");
                                } else {
                                    shijilichengView.setText("0.00KM");
                                }

                                jianmianView.setText(PublicUtil.roundByScale(Double.parseDouble(dataObject.getString("free_mileage")), 2) + "KM");
                                chaoshizujinView.setText("￥" + PublicUtil.roundByScale(Double.parseDouble(dataObject.getString("overtime_amount")), 2));
                                qitafeiyongView.setText("￥" + PublicUtil.roundByScale(Double.parseDouble(dataObject.getString("other_amount")), 2));

                                float baoxianfeiyong = 0;
                                if (!TextUtils.isEmpty(dataObject.getString("zc_amount"))) {
                                    zhujinfei = parseFloat(dataObject.getString("zc_amount"));

                                }

                                if (!TextUtils.isEmpty(dataObject.getString("safe_amount"))) {
                                    baoxianfeiyong = parseFloat(dataObject.getString("safe_amount"));

                                }
                                if (!TextUtils.isEmpty(dataObject.getString("iop_amount"))) {
                                    baoxianfeiyong = baoxianfeiyong
                                            + parseFloat(dataObject.getString("iop_amount"));

                                }
                                baoxianView.setText("￥" + PublicUtil.roundByScale(baoxianfeiyong, 2));
                                //车损
                                if (!TextUtils.isEmpty(dataObject.getString("chesun_amount"))) {
                                    cheshun = Float.parseFloat(dataObject.getString("chesun_amount"));
                                    if (cheshun > 0) {
                                        chesunLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        chesunLayout.setVisibility(View.GONE);
                                    }
                                }
                                if (!TextUtils.isEmpty(dataObject.getString("chesun_remark"))) {
                                    cheshunRemark = dataObject.getString("chesun_remark");
                                }

                                lichengfeiyongView.setText("￥" + dataObject.getString("mileage_amount"));
                                if (!TextUtils.isEmpty(dataObject.getString("zc_amount"))) {
                                    feiyongheji = feiyongheji + parseFloat(dataObject.getString("zc_amount"));   //租车费用
                                }
                                zuchefeiyongView.setText("￥" + PublicUtil.roundByScale(feiyongheji, 2));
                                //                                if (!TextUtils.isEmpty(dataObject.getString("actual_foregift_amount"))) {
                                //                                    feiyongheji = feiyongheji +parseFloat(dataObject.getString("actual_foregift_amount"));   //租车押金
                                //                                }
                                if (!TextUtils.isEmpty(dataObject.getString("relet_amount"))) {
                                    if (parseFloat(dataObject.getString("relet_amount")) == 0) {
                                        xuzhuLayout.setVisibility(View.GONE);
                                    }
                                    feiyongheji = feiyongheji + parseFloat(dataObject.getString("relet_amount"));    //续租费用
                                    xuzuzujinView.setText("￥" + PublicUtil.roundByScale(Double.parseDouble(dataObject.getString("relet_amount")), 2));

                                }

                                if (!TextUtils.isEmpty(dataObject.getString("overtime_amount"))) {   //超时费用
                                    if (parseFloat(dataObject.getString("overtime_amount")) == 0) {
                                        chashiLayout.setVisibility(View.GONE);
                                    }
                                    feiyongheji = feiyongheji
                                            + parseFloat(dataObject.getString("overtime_amount"));

                                }

                                if (!TextUtils.isEmpty(dataObject.getString("safe_amount"))) {
                                    feiyongheji = feiyongheji + parseFloat(dataObject.getString("safe_amount"));  //保险费用

                                }
                                if (!TextUtils.isEmpty(dataObject.getString("iop_amount"))) {           //不计免赔费用
                                    feiyongheji = feiyongheji + parseFloat(dataObject.getString("iop_amount"));

                                }

                                if (!TextUtils.isEmpty(dataObject.getString("mileage_amount"))) {   //里程费用
                                    if (parseFloat(dataObject.getString("mileage_amount")) == 0) {
                                        lichenLayout.setVisibility(View.GONE);
                                    }
                                    feiyongheji = feiyongheji
                                            + parseFloat(dataObject.getString("mileage_amount"));

                                }

                                if (!TextUtils.isEmpty(dataObject.getString("other_amount"))) {    //  其他费用
                                    if (parseFloat(dataObject.getString("other_amount")) == 0) {
                                        qitaLayout.setVisibility(View.GONE);
                                    }
                                    feiyongheji = feiyongheji + parseFloat(dataObject.getString("other_amount"));

                                }
                                if (hc_mileage > 0) {  //确认还车之前
                                    feiyongheji = feiyongheji + cheshun;
                                }

                                feiyongheji = PublicUtil.toTwo(feiyongheji);

                                feiyonghejiView.setText("￥" + PublicUtil.roundByScale(feiyongheji, 2));
                                zucheyajin = parseFloat(dataObject.getString("actual_foregift_amount"));
                                yajinInfoTextView.setText("￥" + PublicUtil.roundByScale(zucheyajin, 2));

                                dingdanjineView.setText("￥" + PublicUtil.roundByScale(feiyongheji, 2));

                                yufukuanView.setText("￥" + PublicUtil.roundByScale(zhujinfei + baoxianfeiyong, 2));
                                yingfujieView.setText("￥" + PublicUtil.roundByScale(feiyongheji, 2));

                                feiyongqueren = dataObject.getString("amount_confirm");
                                order_status = dataObject.getString("order_state");
                                bottomLayout.setVisibility(View.GONE);
                                dingdanfeiyongLayout.setVisibility(View.GONE);

                                String amount_confirm = dataObject.getString("amount_confirm");

                                // 0待付定金,1已付定金,2已提车,3已还车,4已完结,5已取消   6，已还车
                                if ("1".equals(order_status)) {// 待提车
                                    dingdanstatusImageView.setBackgroundResource(R.drawable.daitiche);
                                    contentLayout.setPadding(0, 0, 0, 0);

                                } else if ("2".equals(order_status)) {// 已提车
                                    dingdanstatusImageView.setBackgroundResource(R.drawable.yitiche);
                                    initTopimage(3);

                                    if (hc_mileage > 0) {
                                        String confirmStatus = getIntent().getStringExtra("confirmMoney"); //是查看详情还是确认还车

                                        feiyongtotalLayout.setVisibility(View.GONE);
                                        if (cheshun == 0) {
                                            chesunLayout.setVisibility(View.GONE);
                                        } else {
                                            chesunLayout.setVisibility(View.VISIBLE);
                                            chesunMoneyTexView.setText("￥" + PublicUtil.roundByScale(cheshun, 2));
                                            chesunInfoTexView.setText(cheshunRemark);
                                        }
                                        dingdanfeiyongLayout.setVisibility(View.VISIBLE);
                                        if (confirmStatus != null && confirmStatus.equals("1")) {
                                            yajinConfirmLayout.setVisibility(View.VISIBLE);
                                            if (amount_confirm.equals("1")) { //已确认费用
                                                contentLayout.setPadding(0, 0, 0, 0);
                                            } else { //未确认费用
                                                //确认还车
                                                xuzhuStatusTextView.setVisibility(View.VISIBLE);
                                                caoshiStatusTextView.setVisibility(View.VISIBLE);
                                                lichenStatusTextView.setVisibility(View.VISIBLE);
                                                qitaStatusTextView.setVisibility(View.VISIBLE);
                                                cheshunStatusTextView.setVisibility(View.VISIBLE);
                                                bottomLayout.setVisibility(View.VISIBLE);

                                                okView.setText("费用确认");
                                                Double huanCarMoney = Double.parseDouble(dataObject.getString("relet_amount"))
                                                        + Double.parseDouble(dataObject.getString("overtime_amount")) +
                                                        Double.parseDouble(dataObject.getString("mileage_amount")) +
                                                        Double.parseDouble(dataObject.getString("other_amount")) + cheshun;
                                                ok_moneyView.setText("还车费用:￥" + PublicUtil.roundByScale(huanCarMoney, 2));
                                                ok_moneyinforView.setVisibility(View.VISIBLE);
                                                ok_moneyinforView.setText("还车时当面确认车损等各项费用合计");
                                                ok_moneyView.setTextColor(getResources().getColor(R.color.textgreen));
                                            }
                                        } else {
                                            //查看订单详情
                                            contentLayout.setPadding(0, 0, 0, 0);
                                        }
                                    } else {
                                        contentLayout.setPadding(0, 0, 0, 0);
                                    }

                                } else if ("6".equals(order_status) || "3".equals(order_status)) {//已还车
                                    initTopimage(4);
                                    if (cheshun == 0) {
                                        chesunLayout.setVisibility(View.GONE);
                                    } else {
                                        chesunLayout.setVisibility(View.VISIBLE);
                                        chesunMoneyTexView.setText("￥" + PublicUtil.roundByScale(cheshun, 2));
                                        chesunInfoTexView.setText(cheshunRemark);
                                    }
                                    feiyongtotalLayout.setVisibility(View.GONE);
                                    dingdanstatusImageView.setBackgroundResource(R.drawable.yihuanche);
                                    contentLayout.setPadding(0, 0, 0, 0);

                                } else if ("4".equals(order_status)) {// 完结
                                    initTopimage(5);
                                    if (cheshun == 0) {
                                        chesunLayout.setVisibility(View.GONE);
                                    } else {
                                        chesunLayout.setVisibility(View.VISIBLE);
                                        chesunMoneyTexView.setText("￥" + PublicUtil.roundByScale(cheshun, 2));
                                        chesunInfoTexView.setText(cheshunRemark);
                                    }
                                    feiyongtotalLayout.setVisibility(View.GONE);
                                    dingdanstatusImageView.setBackgroundResource(R.drawable.finished);
                                    contentLayout.setPadding(0, 0, 0, 0);
                                } else {// 已取消
                                    topimageLayout.setVisibility(View.GONE);
                                    contentLayout.setPadding(0, 0, 0, 0);
                                }

                            }

                        } else {

                            PublicUtil.showToast(CarRestListDetail2Activity.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(CarRestListDetail2Activity.this, LoginActivity.class);
                                startActivity(intent);

                            }

                        }
                    } catch (Exception e) {
                        // TODO: handle exception

                        e.printStackTrace();
                    }

                }

            } else if (msg != null && msg.what == error_httprequest_queren) {

                if (msg.obj != null) {
                    PublicUtil.showToast(CarRestListDetail2Activity.this, msg.obj.toString(), false);

                }
            } else if (msg != null && msg.what == success_httprequest_queren) {
                if (jsonObject != null) {
                    try {

                        if ("0".equals(jsonObject.getString("errcode"))) {
                            bottomLayout.setVisibility(View.GONE);
                            contentLayout.setPadding(0, 0, 0, 0);
                            xuzhuStatusTextView.setVisibility(View.GONE);
                            caoshiStatusTextView.setVisibility(View.GONE);
                            lichenStatusTextView.setVisibility(View.GONE);
                            qitaStatusTextView.setVisibility(View.GONE);
                            cheshunStatusTextView.setVisibility(View.GONE);
                            if (cheshun > 0) {
                                Toast.makeText(CarRestListDetail2Activity.this, "已在个人押金账户扣除" + cheshun + "元车损", Toast.LENGTH_SHORT).show();
                            } else {
                                PublicUtil.showToast(CarRestListDetail2Activity.this, jsonObject.getString("msg"), false);
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

    /**
     * 设置头部图片显示
     */
    private void initTopimage(int type) {

        if (type == 5) {
            topImageView3.setBackgroundResource(R.drawable.tuoyuan2);
            topImageView4.setBackgroundResource(R.drawable.tuoyuan2);
            topImageView5.setBackgroundResource(R.drawable.tuoyuan2);
        } else if (type == 4) {
            topImageView3.setBackgroundResource(R.drawable.tuoyuan2);
            topImageView4.setBackgroundResource(R.drawable.tuoyuan2);
            topImageView5.setBackgroundResource(R.drawable.tuoyuan);
        } else if (type == 3) {
            topImageView3.setBackgroundResource(R.drawable.tuoyuan2);
            topImageView4.setBackgroundResource(R.drawable.tuoyuan);
            topImageView5.setBackgroundResource(R.drawable.tuoyuan);
        } else if (type == 2) {
            topImageView3.setBackgroundResource(R.drawable.tuoyuan);
            topImageView4.setBackgroundResource(R.drawable.tuoyuan);
            topImageView5.setBackgroundResource(R.drawable.tuoyuan);
        }

    }

    /**
     * 订单费用说明
     */

    private void feiyongSM() {

        View view = LayoutInflater.from(this).inflate(R.layout.view_feiyongshuoming_dialog, null);
        TextView cancelView;

        cancelView = (TextView) view.findViewById(R.id.view_feiyongshuoming_cancel);

        final AlertDialog alertDialog = new AlertDialog.Builder(CarRestListDetail2Activity.this).setView(view).show();

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        cancelView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

    }

    /**
     * 租车费用说明
     */
    private void feiyongSM2() {

        View view = LayoutInflater.from(this).inflate(R.layout.view_feiyongshuoming_dialog2, null);
        TextView cancelView;

        cancelView = (TextView) view.findViewById(R.id.view_feiyongshuoming_cancel2);

        final AlertDialog alertDialog = new AlertDialog.Builder(CarRestListDetail2Activity.this).setView(view).show();

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);

        cancelView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

    }
}
