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
import android.widget.LinearLayout;
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

import java.util.HashMap;
import java.util.Map;

/**
 * 充电结束界面以及充电列表详情界面
 * 
 * @author Administrator
 * 
 */
public class ChargeEndActivity extends BaseActivity implements OnClickListener {

	private FrameLayout topLayout;
	private TextView chargestarttimeTextView, chargeendtimeTextView,
			chargepowerTextView, chargemonerTextView, chargeendmoneyTextView,
			chargeendallmoneyTextView, chargenumberTextView,
			chargefuwumoneyTextView, chargetingchemoneyTextView, backTextView,
			titleTextView;
	private TextView statusView, shichangView;
	private TextView dianzhanmingchenView, dianzhuangbianhaoView;
	private String fm_id, fm_end_id, phone, money;
	private LinearLayout bottomLayout;
	private TextView feiyongquerenView, feiyongquerenmoneyView;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chargeend);
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

	public void init() {

		topLayout = (FrameLayout) findViewById(R.id.chargeend_top_layout);
		setTopLayoutPadding(topLayout);
		backTextView = (TextView) findViewById(R.id.chargeend_back);
		titleTextView = (TextView) findViewById(R.id.chargeend_title);
		bottomLayout = (LinearLayout) findViewById(R.id.chargeend_feiyongqueren_layout);
		feiyongquerenView = (TextView) findViewById(R.id.chargeend_feiyongqueren);
		feiyongquerenmoneyView = (TextView) findViewById(R.id.chargeend_feiyongqueren_money);
		chargepowerTextView = (TextView) findViewById(R.id.chargeend_power);
		chargemonerTextView = (TextView) findViewById(R.id.chargeend_money);
		statusView = (TextView) findViewById(R.id.chargeend_status);
		shichangView = (TextView) findViewById(R.id.chargeend_shichang);
		dianzhanmingchenView = (TextView) findViewById(R.id.chargeend_dianzhanmingchen);
		dianzhuangbianhaoView = (TextView) findViewById(R.id.chargeend_dianzhuangbianhao);

		chargestarttimeTextView = (TextView) findViewById(R.id.chargeend_chargestarttime);
		chargeendtimeTextView = (TextView) findViewById(R.id.chargeend_chargeendtime);

		chargeendallmoneyTextView = (TextView) findViewById(R.id.chargeend_chargemoney);
		chargeendmoneyTextView = (TextView) findViewById(R.id.chargeend_chargeendmoney);
		chargenumberTextView = (TextView) findViewById(R.id.chargeend_chargenumber);
		chargefuwumoneyTextView = (TextView) findViewById(R.id.chargeend_money_fuwu);
		chargetingchemoneyTextView = (TextView) findViewById(R.id.chargeend_money_tingche);

		feiyongquerenView.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		fm_id = getIntent().getStringExtra("fm_id");
		fm_end_id = getIntent().getStringExtra("fm_end_id");
		phone = getIntent().getStringExtra("phone");
		type = getIntent().getStringExtra("type");
		if ("list".equals(type)) {
			backTextView.setVisibility(View.VISIBLE);
			titleTextView.setText("充电详情");
			bottomLayout.setVisibility(View.GONE);

		} else {
			backTextView.setVisibility(View.GONE);
			titleTextView.setText("充电结束");
			bottomLayout.setVisibility(View.VISIBLE);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("fm_id", fm_id);
		map.put("fm_end_id", fm_end_id);
		map.put("ver", Constent.VER);
		map.put("act", Constent.ACT_GETCHARGEENDINFOR);
		AnsynHttpRequest.httpRequest(ChargeEndActivity.this,
				AnsynHttpRequest.POST, callBack, Constent.ID_GETCHARGEENDINFOR,
				map, false, true, true);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {

		case R.id.chargeend_feiyongqueren:

			intent = new Intent(ChargeEndActivity.this,
					ChargeListActivity.class);
			startActivity(intent);
			finish();

			break;
		case R.id.chargeend_back:

			if ("list".equals(type)) {
				finish();

			} else {
				intent = new Intent(ChargeEndActivity.this,
						ChargeListActivity.class);
				startActivity(intent);
				finish();
			}

			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("list".equals(type)) {
				finish();

			} else {
				Intent intent = new Intent(ChargeEndActivity.this,
						ChargeListActivity.class);
				startActivity(intent);
				finish();
			}

			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

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
			case Constent.ID_GETCHARGEENDINFOR:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = httprequesterror;
						message.obj = data;
						handler.sendMessage(message);
					}
				}
				break;

			default:
				break;
			}

		}
	};

	private int httprequesterror = 0x4200;
	private int httprequestsuccess = 0x4201;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(ChargeEndActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {

					try {

						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {

							JSONObject dataJsonObject = new JSONObject(
									jsonObject.get("data").toString());
							if (dataJsonObject != null) {

								money = dataJsonObject.get("c_amount")
										.toString();

								String fuwufeiString = dataJsonObject.get(
										"service_rate").toString();
								String tingchefeiString = dataJsonObject.get(
										"STOP_FEE").toString();

								chargefuwumoneyTextView.setText("￥"
										+ fuwufeiString);
								chargetingchemoneyTextView.setText("￥"
										+ tingchefeiString);

								float all = 0;
								if (money != null && !TextUtils.isEmpty(money)) {
									all = Float.parseFloat(money);
								}

								if (fuwufeiString != null
										&& !TextUtils.isEmpty(fuwufeiString)) {
									all = all + Float.parseFloat(fuwufeiString);
								}

								if (tingchefeiString != null
										&& !TextUtils.isEmpty(tingchefeiString)) {
									all = all
											+ Float.parseFloat(tingchefeiString);
								}

								chargeendallmoneyTextView.setText("￥" + all);

								chargepowerTextView.setText(dataJsonObject.get(
										"c_deal_dl").toString()
										+ "KW.H");
								chargemonerTextView.setText("￥" + money);
								feiyongquerenmoneyView.setText("￥" + money);

								dianzhanmingchenView.setText(dataJsonObject
										.get("cs_name").toString());
								dianzhuangbianhaoView.setText(dataJsonObject
										.get("DEV_ADDR").toString());

								String start = dataJsonObject.get(
										"DEAL_START_DATE").toString();
								String end = dataJsonObject
										.get("DEAL_END_DATE").toString();

								chargestarttimeTextView.setText(start);
								chargeendtimeTextView.setText(end);

								if (!TextUtils.isEmpty(start)
										&& !TextUtils.isEmpty(end)) {
									int fen = PublicUtil.compareMin(start, end);
									if (fen > 0) {
										shichang(fen);
									}

								}

								chargeendmoneyTextView
										.setText("￥"
												+ dataJsonObject.get(
														"REMAIN_AFTER_DEAL")
														.toString());
								chargenumberTextView.setText(dataJsonObject
										.get("DEAL_NO").toString());

							}

						} else if ("1".equals(jsonObject.get("error")
								.toString())) {

							if ("list".equals(type)) {
								chargeendallmoneyTextView.setText(jsonObject
										.get("msg").toString());
							}

						} else {
							PublicUtil.showToast(ChargeEndActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {

						// TODO: handle exception

						e.printStackTrace();

					}

				}

			}

		};
	};

	private void shichang(int fen) {
		if (fen < 60) {
			shichangView.setText(fen + "分钟");
		} else {
			int day = fen / 1440;

			int hour = (fen - day * 1440) / 24;

			int m = fen - day * 1440 - hour * 24;

			String str = "";

			if (day > 0) {
				str = day + "天";
			}

			if (hour > 0) {
				str = str + hour + "小时";
			}

			if (m > 0) {
				str = str + m + "分钟";
			}

			shichangView.setText(str);

		}

	}

}
