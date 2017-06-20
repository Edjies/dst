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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违章结算
 * 
 * @author qtds
 * 
 */
public class WeiZhangjiesuanActivity extends BaseActivity implements
		OnClickListener {
	private final String TAG = "WeiZhangjiesuanActivity";
	private FrameLayout topLayout;
	private TextView backView;
	private TextView inforTextView;
	private TextView yajinView, yingjiaoView, bujiaoView, tuikuanView;
	private TextView bujiao_statusView, tuikuan_statusView;
	private ListView inforListView;
	private FrameLayout bujiaoLayout, yingtuiLayout;

	private String order_no;
	private String wz_foregift_state = "0";// 0未支付，1 已结算 其他 已支付

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weizhangjiesuan);
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

		topLayout = (FrameLayout) findViewById(R.id.weizhangjiesuan_top_layout);
		setTopLayoutPadding(topLayout);

		backView = (TextView) findViewById(R.id.weizhangjiesuan_back);
		inforTextView = (TextView) findViewById(R.id.weizhangjiesuan_infor);

		yajinView = (TextView) findViewById(R.id.weizhangjiesuan_weizhangyajin);
		yingjiaoView = (TextView) findViewById(R.id.weizhangjiesuan_yingjiaokoukuan);
		bujiaoView = (TextView) findViewById(R.id.weizhangjiesuan_bujiaofeiyong);
		tuikuanView = (TextView) findViewById(R.id.weizhangjiesuan_yingtuijine);
		bujiao_statusView = (TextView) findViewById(R.id.weizhangjiesuan_bujiaofeiyong_status);
		tuikuan_statusView = (TextView) findViewById(R.id.weizhangjiesuan_yingtuijine_status);
		inforListView = (ListView) findViewById(R.id.weizhangjiesuan_infor_list);
		bujiaoLayout = (FrameLayout) findViewById(R.id.weizhangjiesuan_bujiaofeiyong_layout);
		yingtuiLayout = (FrameLayout) findViewById(R.id.weizhangjiesuan_yingtuijine_layout);

		backView.setOnClickListener(this);

		order_no = getIntent().getStringExtra("order_no");
		wz_foregift_state = getIntent().getStringExtra("wz_foregift_state");

		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_ORDER_WZ_RECORD);
		map.put("order_no", order_no);

		AnsynHttpRequest.httpRequest(WeiZhangjiesuanActivity.this,
				AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_WZ_RECORD,
				map, false, true, true);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.weizhangjiesuan_back:

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

			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	private JSONObject jsonObject = null;
	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@Override
		public void back(int backId, boolean isRequestSuccess,
				boolean isString, String data, JSONArray jsonArray) {

			switch (backId) {

			case Constent.ID_ORDER_WZ_RECORD:
				if (isRequestSuccess) {
					if (!isString) {

						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							// backArray = new JSONArray(backstr);
							// Log.d(TAG, backArray.length() + "");
							handler.sendEmptyMessage(success_http_get);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}
				} else {
					Message message = new Message();
					message.what = error_http_get;
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

	private int error_http_get = 0x8900;
	private int success_http_get = 0x8901;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == error_http_get) {
				if (msg.obj != null) {
					PublicUtil.showToast(WeiZhangjiesuanActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_http_get) {
				if (jsonObject != null) {
					try {

						if ("0".equals(jsonObject.getString("errcode"))) {

							JSONObject dataObject = jsonObject
									.getJSONObject("data");

							if (dataObject != null) {

								float wz_money = 0, total_money = 0;
								if (!TextUtils.isEmpty(dataObject
										.getString("wz_money"))) {
									wz_money = Float.parseFloat(dataObject
											.getString("wz_money"));

								}

								if (!TextUtils.isEmpty(dataObject
										.getString("total_money"))
										&& !"WAIT".equals(dataObject
												.getString("total_money"))) {
									total_money = Float.parseFloat(dataObject
											.getString("total_money"));

								}
								yajinView.setText("￥" + wz_money);

								JSONArray array = dataObject
										.getJSONArray("list");
								if (array != null && array.length() > 0) {
									inforListView.setVisibility(View.VISIBLE);
									inforTextView.setVisibility(View.GONE);

									if ("WAIT".equals(dataObject
											.getString("total_money"))) {
										yingjiaoView.setText("请耐心等待");
										tuikuanView.setText("请耐心等待");
										tuikuan_statusView.setText("等待中");

										yingtuiLayout
												.setVisibility(View.VISIBLE);
										bujiaoLayout.setVisibility(View.GONE);
									} else {
										yingjiaoView.setText("￥" + total_money);

										if ((wz_money - total_money) < 0) {

											bujiaoView
													.setText("￥"
															+ PublicUtil
																	.toTwo((total_money - wz_money)));

											if ("1".equals(wz_foregift_state)) {
												bujiao_statusView
														.setText("已结算");
												bujiao_statusView
														.setBackgroundResource(R.drawable.button_106);
											} else {
												bujiao_statusView
														.setText("待补缴");
												bujiao_statusView
														.setBackgroundResource(R.drawable.button_106red);
											}

											yingtuiLayout
													.setVisibility(View.GONE);
											bujiaoLayout
													.setVisibility(View.VISIBLE);

										} else {

											tuikuanView
													.setText("￥"
															+ PublicUtil
																	.toTwo((wz_money - total_money)));

											if ("1".equals(wz_foregift_state)) {

												tuikuan_statusView
														.setText("已退还");
												tuikuan_statusView
														.setBackgroundResource(R.drawable.button_106);
											} else {
												tuikuan_statusView
														.setText("待结算");
												tuikuan_statusView
														.setBackgroundResource(R.drawable.button_106red);
											}

											yingtuiLayout
													.setVisibility(View.VISIBLE);
											bujiaoLayout
													.setVisibility(View.GONE);

										}

									}

									setList(array);

								} else {
									inforListView.setVisibility(View.GONE);
									inforTextView.setVisibility(View.VISIBLE);

									if ("WAIT".equals(dataObject
											.getString("total_money"))) {
										inforTextView
												.setText("用车期间如有违章产生，请您在20日内自行缴纳罚款、清除违章记录，并向我们提供处理凭证，否则因此产生的罚金及相关费用将会从违章押金中自动扣除。");
										yingjiaoView.setText("请耐心等待");
										tuikuanView.setText("请耐心等待");
										tuikuan_statusView.setText("等待中");

										yingtuiLayout
												.setVisibility(View.VISIBLE);
										bujiaoLayout.setVisibility(View.GONE);

									} else {
										inforTextView
												.setText("您在用车期间没有违章行为，违章押金将会在3个工作日内退还至您的支付账户中，请留意提醒短信。");

										yingjiaoView.setText("￥" + total_money);

										if ((wz_money - total_money) < 0) {
											bujiaoView
													.setText("￥"
															+ PublicUtil
																	.toTwo((total_money - wz_money)));
											if ("1".equals(wz_foregift_state)) {
												bujiao_statusView
														.setText("已结算");
												bujiao_statusView
														.setBackgroundResource(R.drawable.button_106);
											} else {
												bujiao_statusView
														.setText("待补缴");
												bujiao_statusView
														.setBackgroundResource(R.drawable.button_106red);
											}
											yingtuiLayout
													.setVisibility(View.GONE);
											bujiaoLayout
													.setVisibility(View.VISIBLE);

										} else {
											tuikuanView
													.setText("￥"
															+ PublicUtil
																	.toTwo((wz_money - total_money)));
											if ("1".equals(wz_foregift_state)) {

												tuikuan_statusView
														.setText("已退还");
												tuikuan_statusView
														.setBackgroundResource(R.drawable.button_106);
											} else {
												tuikuan_statusView
														.setText("待结算");
												tuikuan_statusView
														.setBackgroundResource(R.drawable.button_106red);
											}
											yingtuiLayout
													.setVisibility(View.VISIBLE);
											bujiaoLayout
													.setVisibility(View.GONE);

										}

									}

								}

							}

						} else {
							PublicUtil.showToast(WeiZhangjiesuanActivity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {

								Intent intent = new Intent(
										WeiZhangjiesuanActivity.this,
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
		};
	};

	/**
	 * 设置违章列表
	 * 
	 * @param array
	 */

	private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

	private void setList(JSONArray array) {
		dataList.clear();
		try {
			JSONObject itemJsonObject;

			for (int i = 0; i < array.length(); i++) {

				itemJsonObject = array.getJSONObject(i);

				Map<String, String> map = new HashMap<String, String>();
				map.put("area", itemJsonObject.getString("area"));
				map.put("act", itemJsonObject.getString("act"));
				map.put("money", "￥" + itemJsonObject.getString("money"));
				map.put("date", itemJsonObject.getString("date"));
				dataList.add(map);

			}

			SimpleAdapter simpleAdapter = new SimpleAdapter(
					WeiZhangjiesuanActivity.this, dataList,
					R.layout.listview_item_weizhangjiesuanlist, new String[] {
							"area", "act", "money", "date" }, new int[] {
							R.id.weizhangjiesuanlist_listitem_infor,
							R.id.weizhangjiesuanlist_listitem_infor2,
							R.id.weizhangjiesuanlist_listitem_money,
							R.id.weizhangjiesuanlist_listitem_time });

			inforListView.setAdapter(simpleAdapter);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
