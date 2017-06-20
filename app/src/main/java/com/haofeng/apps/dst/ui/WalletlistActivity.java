package com.haofeng.apps.dst.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.AddPowerOrderAdapter;
import com.haofeng.apps.dst.adapter.AllDetailAdapter;
import com.haofeng.apps.dst.adapter.ConcelRentCarAdapter;
import com.haofeng.apps.dst.adapter.HuanCheKouKuanAdapter;
import com.haofeng.apps.dst.adapter.PowerCarCongZhiAdapter;
import com.haofeng.apps.dst.adapter.RentCarOrderAdapter;
import com.haofeng.apps.dst.adapter.ReturnYaJinReCordAdapter;
import com.haofeng.apps.dst.adapter.WeiZhangKouKuanAdapter;
import com.haofeng.apps.dst.adapter.YaJinCongZhiAdapter;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.inflate;
import static com.haofeng.apps.dst.R.id.view_list_view_empty_congzhi;
import static com.haofeng.apps.dst.R.id.view_pop_detail_power_card_get_money;
import static com.haofeng.apps.dst.R.id.walletlist_rent_order;

/**
 * 钱包余额记录
 *
 * @author Administrator
 */
public class WalletlistActivity extends BaseActivity implements OnClickListener {
    private final String TAG = "WalletlistActivity";
    private FrameLayout topLayout;
    private TextView backView;
    private TextView allTextView;
    private TextView yajincongzhiTextView;
    private TextView allTextLineTextView;
    private TextView congdiandingdangTextView;
    private TextView congdiandingdangLineTextView;
    private ImageButton moreDetailImageButton;
    private TextView diankacongzhiTextView;
    private TextView diankacongzhiLineTextView;
    private TextView powercardtixianTextView;
    private TextView yajintuikuanTextView;
    private TextView huanchekoukuanTextView;
    private TextView weizhangkoukuanTextView;
    private TextView concelrentcarTextView;
    private TextView moredetailTextView;
    private TextView moredetailLineTextView;
    private TextView rentcarTextView;
    private TextView rentcarLineTextView;

    private FrameLayout allLayout;
    private FrameLayout moreLayout;
    private FrameLayout congdianorderLayout;
    private FrameLayout diankacongzhiLayout;
    private FrameLayout rentcarLayout;
    private int type = 0;   //查询的类型   0,全部 1,电卡充值 2,租车下单 3,押金充值 4,还车扣款 5,违章扣款，6，取消租车，7,押金退款，8,充电订单，9，电卡提现
    private ListView pullListView;
    private Button emptyCongzhi;
    private TextView showTextTextView;
    private PullToRefreshListView freshView;
    // 6,取消租车 7,押金退款

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
       addActivity(this);
        initView();
        initListener();
    }

    private void initView() {
        setContentView(R.layout.activity_walletlist);
        topLayout = (FrameLayout) findViewById(R.id.walletlist_top_layout);
      setTopLayoutPadding(topLayout);
        //初始化宽度
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();

        allLayout = (FrameLayout) findViewById(R.id.walletlist_all_layout);
        moreLayout = (FrameLayout) findViewById(R.id.walletlist_more_detail_layout);
        congdianorderLayout = (FrameLayout) findViewById(R.id.walletlist_congdian_order_layout);
        diankacongzhiLayout = (FrameLayout) findViewById(R.id.walletlist_dianka_congzhi_layout);
        rentcarLayout = (FrameLayout) findViewById(R.id.walletlist_rent_order_layout);
        //设置宽度
        initViewWidth(allLayout, screenWidth);
        initViewWidth(moreLayout, screenWidth);
        initViewWidth(congdianorderLayout, screenWidth);
        initViewWidth(diankacongzhiLayout, screenWidth);
        initViewWidth(rentcarLayout, screenWidth);

        backView = (TextView) findViewById(R.id.walletlist_back);

        allTextView = (TextView) findViewById(R.id.walletlist_all);//全部记录
        allTextLineTextView = (TextView) findViewById(R.id.walletlist_all_line);
        //更多
        moredetailTextView = (TextView) findViewById(R.id.walletlist_more_detail);
        moredetailLineTextView = (TextView) findViewById(R.id.walletlist_more_detail_line);

        congdiandingdangTextView = (TextView) findViewById(R.id.walletlist_congdian_order);  //充电电单记录
        congdiandingdangLineTextView = (TextView) findViewById(R.id.walletlist_congdian_order_line);

        diankacongzhiTextView = (TextView) findViewById(R.id.walletlist_dianka_congzhi);
        diankacongzhiLineTextView = (TextView) findViewById(R.id.walletlist_dianka_congzhi_line);//电卡充值

        rentcarTextView = (TextView) findViewById(walletlist_rent_order); //租车订单
        rentcarLineTextView = (TextView) findViewById(R.id.walletlist_rent_order_line);

        moreDetailImageButton = (ImageButton) findViewById(R.id.activity_wallet_list_more);

        freshView = (PullToRefreshListView) findViewById(R.id.activity_wallet_detail_flush);
        pullListView = freshView.getRefreshableView();
        pullListView.setDividerHeight(0);
        View emptyView = View.inflate(this, R.layout.view_listview_empty_show_congzhi, null);
        emptyCongzhi = (Button) emptyView.findViewById(view_list_view_empty_congzhi);
        showTextTextView = (TextView) emptyView.findViewById(R.id.view_listview_emptyshow_text);
        if (type == 0) {
            emptyCongzhi.setVisibility(View.VISIBLE);
            showTextTextView.setText("您还没有进行交易呢");
        }
        freshView.setEmptyView(emptyView);
        freshView.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);

        ILoadingLayout startLabels = freshView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        freshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 1000);
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                freshView.setRefreshing();
            }
        },200);
    }

    //获取交易记录
    private void getData() {
        if (type == 7) {
            //获取押金退款记录
            Map<String, String> map = new HashMap<>();
            map.put("act", Constent.ACT_YAJINTIXIANRECORD);
            AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callback, Constent.ID_ACT_YAJINTIXIANRECORD, map, false, false, true);
        } else if (type == 8) { //充电订单
            Map<String, String> map = new HashMap<>();
            map.put("act", Constent.ACT_CHARGELIST);
            String phone = PublicUtil.getStorage_string(WalletlistActivity.this, "phone", "");
            if (!phone.equals("")) {
                map.put("mobile", phone);
                AnsynHttpRequest.httpRequest(WalletlistActivity.this, AnsynHttpRequest.POST, callback, Constent.ID_CHARGELIST, map, false, false, true);
            }
        } else if (type == 9) {   //电卡提现
            freshView.onRefreshComplete();
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("act", Constent.ACT_MY_GET_MONEY_DETAIL);
            if (type != 0) {
                map.put("type", type + "");
                AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callback
                        , Constent.ID_ACT_MY_GET_MONEY_DETAIL, map, false, false, true);
            } else {//全部订单
                AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callback
                        , Constent.ID_ACT_MY_GET_MONEY_DETAIL, map, false, false, true);
            }

        }
    }

    private HttpRequestCallBack callback = new HttpRequestCallBack() {
        @Override
        public void back(final int backId, final boolean isRequestSuccess, final boolean isString, String data, final JSONArray jsonArray) {
            runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  switch (backId) {
                                      case Constent.ID_ACT_MY_GET_MONEY_DETAIL:
                                          if (isRequestSuccess) {
                                              if (!isString) {
                                                  try {
                                                      String backStr = jsonArray.getString(1);
                                                      JSONObject jsonObject = new JSONObject(backStr);
                                                      String errorCode = jsonObject.getString("errcode");
                                                      if ("0".equals(errorCode)) {
                                                          JSONObject dataObject = jsonObject.getJSONObject("data");
                                                          if (dataObject != null) {
                                                              String total = dataObject.getString("total");
                                                              switch (type) {
                                                                  case 0:
                                                                      if (Integer.parseInt(total) > 0) {
                                                                          List<Map<String, String>> allCarList = new ArrayList<Map<String, String>>();
                                                                          JSONArray dataArray = dataObject.getJSONArray("list");
                                                                          for (int i = 0; i < dataArray.length(); i++) {
                                                                              Map<String, String> map = new HashMap<>();
                                                                              map.put("order_no", dataArray.getJSONObject(i).getString("order_no"));
                                                                              map.put("money", dataArray.getJSONObject(i).getString("money"));
                                                                              map.put("create_time", PublicUtil.stampToDate(dataArray.getJSONObject(i).getString("create_time")));
                                                                              map.put("pay_way", dataArray.getJSONObject(i).getString("pay_way"));
                                                                              map.put("explanation", dataArray.getJSONObject(i).getString("explanation"));
                                                                              map.put("trading_type", dataArray.getJSONObject(i).getString("trading_type"));
                                                                              allCarList.add(map);
                                                                          }
                                                                          pullListView.setAdapter(new AllDetailAdapter(allCarList, WalletlistActivity.this));
                                                                      } else {
                                                                          emptyCongzhi.setVisibility(View.VISIBLE);
                                                                          showTextTextView.setText("您还没有进行交易呢");
                                                                          pullListView.setAdapter(null);
                                                                      }
                                                                      freshView.onRefreshComplete();
                                                                      break;
                                                                  case 1:     //电卡充值
                                                                      if (Integer.parseInt(total) > 0) {
                                                                          List<Map<String, String>> powerCarList = new ArrayList<Map<String, String>>();
                                                                          JSONArray dataArray = dataObject.getJSONArray("list");
                                                                          for (int i = 0; i < dataArray.length(); i++) {
                                                                              Map<String, String> map = new HashMap<>();
                                                                              map.put("order_no", dataArray.getJSONObject(i).getString("order_no"));
                                                                              map.put("money", dataArray.getJSONObject(i).getString("money"));
                                                                              map.put("create_time", PublicUtil.stampToDate(dataArray.getJSONObject(i).getString("create_time")));
                                                                              map.put("pay_way", dataArray.getJSONObject(i).getString("pay_way"));
                                                                              powerCarList.add(map);
                                                                          }
                                                                          pullListView.setAdapter(new PowerCarCongZhiAdapter(powerCarList, WalletlistActivity.this));
                                                                      } else {
                                                                          pullListView.setAdapter(null);
                                                                      }

                                                                      freshView.onRefreshComplete();
                                                                      break;
                                                                  case 2:     //租车订单
                                                                      if (Integer.parseInt(total) > 0) {
                                                                          List<Map<String, String>> rentCarList = new ArrayList<Map<String, String>>();
                                                                          JSONArray dataArray = dataObject.getJSONArray("list");
                                                                          for (int i = 0; i < dataArray.length(); i++) {
                                                                              Map<String, String> map = new HashMap<>();
                                                                              map.put("order_no", dataArray.getJSONObject(i).getString("order_no"));
                                                                              map.put("money", dataArray.getJSONObject(i).getString("money"));
                                                                              map.put("create_time", PublicUtil.stampToDate(dataArray.getJSONObject(i).getString("create_time")));
                                                                              map.put("pay_way", dataArray.getJSONObject(i).getString("pay_way"));
                                                                              String explanation = dataArray.getJSONObject(i).getString("explanation");
                                                                              map.put("explanation", dataArray.getJSONObject(i).getString("explanation"));
                                                                              rentCarList.add(map);
                                                                          }
                                                                          pullListView.setAdapter(new RentCarOrderAdapter(rentCarList, WalletlistActivity.this));
                                                                      } else {
                                                                          pullListView.setAdapter(null);
                                                                      }
                                                                      freshView.onRefreshComplete();
                                                                      break;
                                                                  case 3:    //押金充值

                                                                      if (Integer.parseInt(total) > 0) {
                                                                          List<Map<String, String>> yajincongzhiList = new ArrayList<Map<String, String>>();
                                                                          JSONArray dataArray = dataObject.getJSONArray("list");
                                                                          for (int i = 0; i < dataArray.length(); i++) {
                                                                              Map<String, String> map = new HashMap<>();
                                                                              map.put("order_no", dataArray.getJSONObject(i).getString("order_no"));
                                                                              map.put("money", dataArray.getJSONObject(i).getString("money"));
                                                                              map.put("create_time", PublicUtil.stampToDate(dataArray.getJSONObject(i).getString("create_time")));
                                                                              map.put("pay_way", dataArray.getJSONObject(i).getString("pay_way"));
                                                                              yajincongzhiList.add(map);
                                                                          }
                                                                          pullListView.setAdapter(new YaJinCongZhiAdapter(yajincongzhiList, WalletlistActivity.this));
                                                                      } else {
                                                                          pullListView.setAdapter(null);
                                                                      }

                                                                      freshView.onRefreshComplete();

                                                                      break;
                                                                  case 4:    //还车扣款

                                                                      if (Integer.parseInt(total) > 0) {
                                                                          List<Map<String, String>> huancekoukuanList = new ArrayList<Map<String, String>>();
                                                                          JSONArray dataArray = dataObject.getJSONArray("list");
                                                                          for (int i = 0; i < dataArray.length(); i++) {
                                                                              Map<String, String> map = new HashMap<>();
                                                                              map.put("order_no", dataArray.getJSONObject(i).getString("order_no"));
                                                                              map.put("money", dataArray.getJSONObject(i).getString("money"));
                                                                              map.put("create_time", PublicUtil.stampToDate(dataArray.getJSONObject(i).getString("create_time")));
                                                                              huancekoukuanList.add(map);
                                                                          }
                                                                          pullListView.setAdapter(new HuanCheKouKuanAdapter(huancekoukuanList, WalletlistActivity.this));

                                                                      } else {
                                                                          pullListView.setAdapter(null);
                                                                      }
                                                                      freshView.onRefreshComplete();
                                                                      break;
                                                                  case 5:    //违章扣款

                                                                      if (Integer.parseInt(total) > 0) {
                                                                          List<Map<String, String>> weiZhangPayList = new ArrayList<Map<String, String>>();
                                                                          JSONArray dataArray = dataObject.getJSONArray("list");
                                                                          for (int i = 0; i < dataArray.length(); i++) {
                                                                              Map<String, String> map = new HashMap<>();
                                                                              map.put("order_no", dataArray.getJSONObject(i).getString("order_no"));
                                                                              map.put("money", dataArray.getJSONObject(i).getString("money"));
                                                                              map.put("create_time", PublicUtil.stampToDate(dataArray.getJSONObject(i).getString("create_time")));
                                                                              map.put("explanation", dataArray.getJSONObject(i).getString("explanation"));
                                                                              weiZhangPayList.add(map);

                                                                          }

                                                                          pullListView.setAdapter(new WeiZhangKouKuanAdapter(weiZhangPayList, WalletlistActivity.this));

                                                                      } else {
                                                                          pullListView.setAdapter(null);
                                                                      }
                                                                      freshView.onRefreshComplete();

                                                                      break;
                                                                  case 6:    //取消租车

                                                                      if (Integer.parseInt(total) > 0) {
                                                                          List<Map<String, String>> concelRentCarlist = new ArrayList<Map<String, String>>();
                                                                          JSONArray dataArray = dataObject.getJSONArray("list");
                                                                          for (int i = 0; i < dataArray.length(); i++) {
                                                                              Map<String, String> map = new HashMap<>();
                                                                              map.put("order_no", dataArray.getJSONObject(i).getString("order_no"));
                                                                              map.put("money", dataArray.getJSONObject(i).getString("money"));
                                                                              map.put("create_time", PublicUtil.stampToDate(dataArray.getJSONObject(i).getString("create_time")));
                                                                              map.put("pay_way", dataArray.getJSONObject(i).getString("pay_way"));
                                                                              concelRentCarlist.add(map);
                                                                          }
                                                                          pullListView.setAdapter(new ConcelRentCarAdapter(concelRentCarlist, WalletlistActivity.this));
                                                                      } else {
                                                                          pullListView.setAdapter(null);
                                                                      }
                                                                      freshView.onRefreshComplete();

                                                                      break;
                                                              }
                                                          }

                                                      } else if ("1002".equals(errorCode)) {
                                                          Intent intent = new Intent(WalletlistActivity.this, LoginActivity.class);
                                                          startActivity(intent);
                                                          finish();
                                                      }
                                                  } catch (JSONException e) {
                                                      e.printStackTrace();
                                                  }
                                              }
                                              break;

                                          }
                                      case Constent.ID_ACT_YAJINTIXIANRECORD:
                                          if (isRequestSuccess) {
                                              if (!isString) {
                                                  try {

                                                      String backStr = jsonArray.getString(1);
                                                      JSONObject jsonObject = new JSONObject(backStr);
                                                      if (jsonObject.getString("errcode").equals("0")) {
                                                          JSONArray dataArray = jsonObject.getJSONArray("data");
                                                          List<Map<String, String>> recordList = new ArrayList<Map<String, String>>();
                                                          if (dataArray != null && dataArray.length() > 0) {
                                                              for (int i = 0; i < dataArray.length(); i++) {
                                                                  Map<String, String> map = new HashMap<>();
                                                                  String amount = dataArray.getJSONObject(i).getString("amount");
                                                                  map.put("amount", amount);
                                                                  String status = dataArray.getJSONObject(i).getString("status");
                                                                  map.put("status", status);
                                                                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                                                  String apply_time = sdf.format(new Date(Long.parseLong(dataArray.getJSONObject(i)
                                                                          .getString("apply_time")) * 1000));
                                                                  map.put("apply_time", apply_time);
                                                                  String confirm_time = sdf.format(new Date(Long.parseLong(dataArray.getJSONObject(i)
                                                                          .getString("confirm_time")) * 1000));
                                                                  map.put("confirm_time", confirm_time);
                                                                  String refund_way = dataArray.getJSONObject(i).getString("refund_way");
                                                                  map.put("refund_way", refund_way);
                                                                  recordList.add(map);
                                                              }
                                                              ReturnYaJinReCordAdapter returnYaJinReCordAdapter =
                                                                      new ReturnYaJinReCordAdapter(recordList, WalletlistActivity.this);
                                                              pullListView.setAdapter(returnYaJinReCordAdapter);

                                                          } else {
                                                              pullListView.setAdapter(null);
                                                          }
                                                          freshView.onRefreshComplete();
                                                      }
                                                  } catch (JSONException e) {
                                                      e.printStackTrace();
                                                  }

                                              }

                                          }
                                          break;
                                      case Constent.ID_CHARGELIST:
                                          pullListView.setAdapter(null);
                                          if (isRequestSuccess) {
                                              if (!isString) {
                                                  try {
                                                      String backStr = jsonArray.getString(1);
                                                      JSONObject jsonObject = new JSONObject(backStr);
                                                      if (jsonObject.getString("error").equals("0")) {
                                                          JSONArray dataArray = jsonObject.getJSONArray("data");
                                                          List<Map<String, String>> addPowerRecordList = new ArrayList<Map<String, String>>();
                                                          if (dataArray != null && dataArray.length() > 0) {
                                                              for (int i = 0; i < dataArray.length(); i++) {
                                                                  Map<String, String> map = new HashMap<>();
                                                                  String orderNumber = dataArray.getJSONObject(i).getString("DEAL_NO");
                                                                  map.put("orderNumber", orderNumber);
                                                                  String startTime = dataArray.getJSONObject(i).getString("DEAL_START_DATE");
                                                                  map.put("startTime", startTime);
                                                                  String endTime = dataArray.getJSONObject(i).getString("DEAL_END_DATE");
                                                                  map.put("endTime", endTime);
                                                                  String stationName = dataArray.getJSONObject(i).getString("cs_name");
                                                                  map.put("stationName", stationName);
                                                                  String statusText = dataArray.getJSONObject(i).getString("status_text");
                                                                  map.put("statusText", statusText);
                                                                  String payMoney = dataArray.getJSONObject(i).getString("c_amount");
                                                                  map.put("payMoney", payMoney);
                                                                  addPowerRecordList.add(map);
                                                              }
                                                              AddPowerOrderAdapter addPowerOrderAdapter =
                                                                      new AddPowerOrderAdapter(addPowerRecordList, WalletlistActivity.this);
                                                              pullListView.setAdapter(addPowerOrderAdapter);
                                                          }
                                                      }
                                                  } catch (JSONException e) {
                                                      e.printStackTrace();
                                                  }
                                              }

                                          }
                                          freshView.onRefreshComplete();
                                          break;
                                  }
                              }
                          }

            );
        }
    };

    //初始化控件宽度
    private void initViewWidth(FrameLayout layout, int screenWidth) {
        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
        layoutParams.width = screenWidth / 4;
        allLayout.setLayoutParams(layoutParams);

    }

    private void initListener() {
        backView.setOnClickListener(this);
        allTextView.setOnClickListener(this);
        moredetailTextView.setOnClickListener(this);
        congdiandingdangTextView.setOnClickListener(this);
        diankacongzhiTextView.setOnClickListener(this);
        moreDetailImageButton.setOnClickListener(this);
        rentcarTextView.setOnClickListener(this);
        emptyCongzhi.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.walletlist_back:
                finish();
                break;
            case R.id.walletlist_all:   //全部
                type = 0;
                initLineShow();

                allTextView.setTextColor(getResources().getColor(R.color.textgreen));
                allTextLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.walletlist_more_detail:   //更多
                switch (moredetailTextView.getText().toString()) {
                    case "电卡提现":
                        break;
                    case "押金充值":
                        type = 3;
                        break;
                    case "押金退款":
                        type = 7;
                        break;
                    case "还车扣款":
                        type = 4;
                        break;
                    case "违章扣款":
                        type = 5;
                        break;
                    case "取消租车":
                        type = 6;
                        break;
                }
                initLineShow();
                moredetailTextView.setTextColor(getResources().getColor(R.color.textgreen));
                moredetailLineTextView.setVisibility(View.VISIBLE);
                if (type == 7) {
                    freshView.setRefreshing();
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //获取押金退款记录
                            Looper.prepare();
                            Map<String, String> map1 = new HashMap<>();
                            map1.put("act", Constent.ACT_YAJINTIXIANRECORD);
                            AnsynHttpRequest.httpRequest(WalletlistActivity.this, AnsynHttpRequest.GET, callback, Constent.ID_ACT_YAJINTIXIANRECORD, map1, false, false, true);
                            Looper.loop();
                        }
                    }, 1000);
                } else {
                    freshView.setRefreshing();
                }
                break;
            case R.id.walletlist_congdian_order:     //充电订单
                initLineShow();
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 8;
                congdiandingdangTextView.setTextColor(getResources().getColor(R.color.textgreen));
                congdiandingdangLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.walletlist_dianka_congzhi:
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 1;
                initLineShow();
                diankacongzhiTextView.setTextColor(getResources().getColor(R.color.textgreen));
                diankacongzhiLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.walletlist_rent_order:
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 2;
                initLineShow();
                rentcarTextView.setTextColor(getResources().getColor(R.color.textgreen));
                rentcarLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.activity_wallet_list_more:   //显示更多
                View view = inflate(this, R.layout.view_pop_detail, null);
                powercardtixianTextView = (TextView) view.findViewById(view_pop_detail_power_card_get_money);
                yajintuikuanTextView = (TextView) view.findViewById(R.id.view_pop_detail_yajin_tuikuan);
                huanchekoukuanTextView = (TextView) view.findViewById(R.id.view_pop_detail_huanche_koukuan);
                weizhangkoukuanTextView = (TextView) view.findViewById(R.id.view_pop_detail_weizhang_koukuan);
                concelrentcarTextView = (TextView) view.findViewById(R.id.view_pop_detail_concel_rent_car);
                yajincongzhiTextView = (TextView) view.findViewById(R.id.view_pop_detail_yajin_congzhi);
                powercardtixianTextView.setOnClickListener(this);
                yajincongzhiTextView.setOnClickListener(this);
                yajintuikuanTextView.setOnClickListener(this);
                huanchekoukuanTextView.setOnClickListener(this);
                weizhangkoukuanTextView.setOnClickListener(this);
                concelrentcarTextView.setOnClickListener(this);

                PopupWindow popupWindow = new PopupWindow(view, 300
                        , ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.xinzeng_frame));
                popupWindow.showAsDropDown(moreDetailImageButton);
                break;

            case R.id.view_pop_detail_power_card_get_money:   //电卡提现
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                initLineShow();
                type = 9;
                moreLayout.setVisibility(View.VISIBLE);
                moredetailTextView.setText("电卡提现");
                moredetailTextView.setTextColor(getResources().getColor(R.color.textgreen));
                moredetailLineTextView.setVisibility(View.VISIBLE);
                pullListView.setAdapter(null);
                freshView.setRefreshing();
                break;
            case R.id.view_pop_detail_yajin_congzhi:   //押金充值
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 3;
                initLineShow();
                moreLayout.setVisibility(View.VISIBLE);
                moredetailTextView.setText("押金充值");
                moredetailTextView.setTextColor(getResources().getColor(R.color.textgreen));
                moredetailLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.view_pop_detail_yajin_tuikuan:   //押金退款
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 7;
                initLineShow();
                moreLayout.setVisibility(View.VISIBLE);
                moredetailTextView.setText("押金退款");
                moredetailTextView.setTextColor(getResources().getColor(R.color.textgreen));
                moredetailLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.view_pop_detail_huanche_koukuan:   //还车扣款
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 4;
                initLineShow();
                moreLayout.setVisibility(View.VISIBLE);
                moredetailTextView.setText("还车扣款");
                moredetailTextView.setTextColor(getResources().getColor(R.color.textgreen));
                moredetailLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.view_pop_detail_weizhang_koukuan:   //违章扣款
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 5;
                initLineShow();
                moreLayout.setVisibility(View.VISIBLE);
                moredetailTextView.setText("违章扣款");
                moredetailTextView.setTextColor(getResources().getColor(R.color.textgreen));
                moredetailLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.view_pop_detail_concel_rent_car:   //取消租车
                emptyCongzhi.setVisibility(View.GONE);
                showTextTextView.setText("暂无数据");
                type = 6;
                initLineShow();
                moreLayout.setVisibility(View.VISIBLE);
                moredetailTextView.setText("取消租车");
                moredetailTextView.setTextColor(getResources().getColor(R.color.textgreen));
                moredetailLineTextView.setVisibility(View.VISIBLE);
                freshView.setRefreshing();
                break;
            case R.id.view_list_view_empty_congzhi:  //去充值
                Intent intent = new Intent(this, RechargeActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void initLineShow() {
        allTextView.setTextColor(getResources().getColor(R.color.gray4));
        congdiandingdangTextView.setTextColor(getResources().getColor(R.color.gray4));
        diankacongzhiTextView.setTextColor(getResources().getColor(R.color.gray4));
        moredetailTextView.setTextColor(getResources().getColor(R.color.gray4));
        rentcarTextView.setTextColor(getResources().getColor(R.color.gray4));

        allTextLineTextView.setVisibility(View.GONE);
        congdiandingdangLineTextView.setVisibility(View.GONE);
        diankacongzhiLineTextView.setVisibility(View.GONE);
        moredetailLineTextView.setVisibility(View.GONE);
        rentcarLineTextView.setVisibility(View.GONE);
    }
}
