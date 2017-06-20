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
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.CarRestList3Adapter;
import com.haofeng.apps.dst.application.MyApplication;
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
 * 长租订单详情界面
 * 
 * @author qtds
 * 
 */
public class CarRestListDetail3Activity extends BaseActivity implements
		OnClickListener {
	private final String TAG = "CarRestListDetailActivity3";
	private FrameLayout topLayout;
	private TextView backView;

	private TextView diandanhaoView, dingdanzongjineView, yifujineView,
			yajinView;
	private ListView listView;

	private TextView ok_moneyView, okView;
	private String order_no;
	private CarRestList3Adapter carRestList3Adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_carrestlistdetail3);
		((MyApplication) getApplication()).addActivity(this);
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

		topLayout = (FrameLayout) findViewById(R.id.carrestlistdetail3_top_layout);
		if (android.os.Build.VERSION.SDK_INT < 19) {
			topLayout.setPadding(0, 0, 0, 0);
		}

		backView = (TextView) findViewById(R.id.carrestlistdetail3_back);

		diandanhaoView = (TextView) findViewById(R.id.carrestlistdetail3_dingdanhao);
		dingdanzongjineView = (TextView) findViewById(R.id.carrestlistdetail3_dingdanjine);
		yifujineView = (TextView) findViewById(R.id.carrestlistdetail3_yifujine);
		yajinView = (TextView) findViewById(R.id.carrestlistdetail3_zucheyajin);
		listView = (ListView) findViewById(R.id.carrestlistdetail3_listview);

		ok_moneyView = (TextView) findViewById(R.id.carrestlistdetail3_ok_money);
		okView = (TextView) findViewById(R.id.carrestlistdetail3_ok);
		backView.setOnClickListener(this);
		okView.setOnClickListener(this);

		carRestList3Adapter = new CarRestList3Adapter(
				CarRestListDetail3Activity.this);
		listView.setAdapter(carRestList3Adapter);

		order_no = getIntent().getStringExtra("order_no");

		diandanhaoView.setText(getIntent().getStringExtra("order_no"));

		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_ORDER_GET_DETAIL);
		map.put("ver", Constent.VER);
		map.put("order_no", order_no);

		AnsynHttpRequest.httpRequest(CarRestListDetail3Activity.this,
				AnsynHttpRequest.GET, callBack, Constent.ID_ORDER_GET_DETAIL,
				map, false, true, true);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.carrestlistdetail3_back:

			finish();
			break;

		case R.id.carrestlistdetail3_ok:

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
			case Constent.ID_ORDER_AMOUNT_CONFIRMT:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(success_httprequest_fukuan);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = error_httprequest_fukuan;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								handler.sendMessage(message);
							}
						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = error_httprequest_fukuan;
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
							// backArray = new JSONArray(backstr);
							// Log.d(TAG, backArray.length() + "");
							handler.sendEmptyMessage(success_http_getcardetail);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = error_http_getcardetail;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								handler.sendMessage(message);
							}
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
	private int error_httprequest_fukuan = 0x7502;
	private int success_httprequest_fukuan = 0x7503;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == error_http_getcardetail) {
				if (msg.obj != null) {
					PublicUtil.showToast(CarRestListDetail3Activity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_http_getcardetail) {
				if (jsonObject != null) {
					try {

						if ("0".equals(jsonObject.getString("errcode"))) {

							JSONObject dataObject = jsonObject.getJSONObject(
									"data").getJSONObject("order");

							if (dataObject != null) {

								yajinView
										.setText("￥"
												+ dataObject
														.getString("zc_foregift_amount"));

								float zongjine = 0;
								if (!TextUtils.isEmpty(dataObject
										.getString("total_amount"))) {
									zongjine = Float.parseFloat(dataObject
											.getString("total_amount"));

								}
								dingdanzongjineView.setText("￥" + zongjine);
								ok_moneyView.setText("￥" + zongjine);

								if ("1".equals(dataObject
										.getString("order_state"))) {

									okView.setBackgroundColor(getResources()
											.getColor(R.color.gray));
									okView.setClickable(false);

								} else if ("2".equals(dataObject
										.getString("order_state"))) {

									okView.setBackgroundColor(getResources()
											.getColor(R.color.gray));
									okView.setClickable(false);

								} else if ("3".equals(dataObject
										.getString("order_state"))) {

									okView.setBackgroundColor(getResources()
											.getColor(R.color.yellow2));
									okView.setClickable(true);

								} else if ("4".equals(dataObject
										.getString("order_state"))) {

									okView.setBackgroundColor(getResources()
											.getColor(R.color.yellow2));
									okView.setClickable(true);
								}

							}

						} else {
							PublicUtil.showToast(
									CarRestListDetail3Activity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {

								Intent intent = new Intent(
										CarRestListDetail3Activity.this,
										LoginActivity.class);
								startActivity(intent);

							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(CarRestListDetail3Activity.this,
								"配置解析数据错误，请退出重试", false);
						e.printStackTrace();
					}

				}

			} else if (msg != null && msg.what == error_httprequest_fukuan) {

				if (msg.obj != null) {
					PublicUtil.showToast(CarRestListDetail3Activity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest_fukuan) {
				if (jsonObject != null) {

					try {
						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						PublicUtil.showToast(CarRestListDetail3Activity.this,
								jsonObject.getString("msg"), false);
						if ("0".equals(jsonObject.getString("errcode"))) {
							finish();

						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(CarRestListDetail3Activity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}
		};
	};

}
