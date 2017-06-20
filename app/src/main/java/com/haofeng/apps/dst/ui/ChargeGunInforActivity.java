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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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

public class ChargeGunInforActivity extends BaseActivity implements
		OnClickListener {
	private static String TAG = "ChargeStationInforActivity";
	private FrameLayout topLayout;
	private TextView titleView;
	private Button chargeButton;
	private TextView backView, statusView, dcacView, typeView, edvView,
			edaView, edwView;
	private String poleid = null, mpn = null, vcr_id = null;
	private String phone;
	private CheckBox checkBox;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chargeguninfor);
		addActivity(this);
		init();

	}

	/**
	 * 初始化
	 */
	@SuppressLint("NewApi")
	private void init() {

		topLayout = (FrameLayout) findViewById(R.id.chargeguninfor_top_layout);
		setTopLayoutPadding(topLayout);
		chargeButton = (Button) findViewById(R.id.chargeguninfor_chongdian);
		chargeButton.setOnClickListener(this);
		backView = (TextView) findViewById(R.id.chargeguninfor_back);
		backView.setOnClickListener(this);

		statusView = (TextView) findViewById(R.id.chargeguninfor_status);
		dcacView = (TextView) findViewById(R.id.chargeguninfor_fangshi);
		typeView = (TextView) findViewById(R.id.chargeguninfor_jiekou);
		edaView = (TextView) findViewById(R.id.chargeguninfor_eda);
		edvView = (TextView) findViewById(R.id.chargeguninfor_edv);
		edwView = (TextView) findViewById(R.id.chargeguninfor_edw);
		titleView = (TextView) findViewById(R.id.chargeguninfor_title);
		checkBox = (CheckBox) findViewById(R.id.chargeguninfor_checkbox);

		phone = PublicUtil.getStorage_string(ChargeGunInforActivity.this,
				"phone", "-1");

		if (!"-1".equals(phone)) {
			if ("saoma".equals(getIntent().getStringExtra("main"))
					|| "saomainput".equals(getIntent().getStringExtra("main"))) {// 扫码界面进来

				if (getIntent().getStringExtra("msg") != null) {
					if ("saomainput".equals(getIntent().getStringExtra("main"))) {// 扫码界面输入进来
						Map<String, String> map = new HashMap<String, String>();
						map.put("mobile", phone);
						map.put("gun_code", getIntent().getStringExtra("msg"));
						map.put("ver", Constent.VER);
						map.put("act", Constent.ACT_GETCHARGEID);
						AnsynHttpRequest
								.httpRequest(ChargeGunInforActivity.this,
										AnsynHttpRequest.POST, callBack,
										Constent.ID_GETCHARGEID, map, false,
										true, true);
					} else {// 扫码界面扫码进来
						try {
							JSONObject jsonObject = new JSONObject(getIntent()
									.getStringExtra("msg"));
							poleid = jsonObject.getString("pole_Id");
							mpn = jsonObject.getString("measuring_point");
							Map<String, String> map = new HashMap<String, String>();
							map.put("mobile", phone);
							map.put("pole_id", poleid);
							map.put("mpn", mpn);
							map.put("ver", Constent.VER);
							map.put("act", Constent.ACT_GETCHARGEID);
							AnsynHttpRequest.httpRequest(
									ChargeGunInforActivity.this,
									AnsynHttpRequest.POST, callBack,
									Constent.ID_GETCHARGEID, map, false, true,
									true);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else {
					PublicUtil.showToast(ChargeGunInforActivity.this,
							"扫码结果解析有误，无法进行下一步操作，返回重试", false);
				}

			} else {
				poleid = getIntent().getStringExtra("id");
				mpn = getIntent().getStringExtra("mpn");
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", phone);
				map.put("pole_id", poleid);
				map.put("mpn", mpn);
				map.put("ver", Constent.VER);
				map.put("act", Constent.ACT_GETCHARGEID);
				AnsynHttpRequest.httpRequest(ChargeGunInforActivity.this,
						AnsynHttpRequest.POST, callBack,
						Constent.ID_GETCHARGEID, map, false, true, true);
			}
		} else {
			PublicUtil.showToast(ChargeGunInforActivity.this, "用户未登录，请先登录",
					false);
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		MobclickAgent.onResume(this);

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
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

	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {

		case R.id.chargeguninfor_back:
			finish();

			break;
		case R.id.chargeguninfor_chongdian:

			if (phone == null || "-1".equals(phone)) {
				PublicUtil.showToast(ChargeGunInforActivity.this,
						"用户还未登录，请先登录", false);
				Intent intent = new Intent(ChargeGunInforActivity.this,
						LoginActivity.class);
				intent.putExtra("clicktype", "main");
				startActivity(intent);
				finish();

			} else {
				if (!checkBox.isChecked()) {

					PublicUtil.showToast(ChargeGunInforActivity.this,
							"请同意地上铁服务条款", false);
					return;
				}

				Map<String, String> map = new HashMap<String, String>();
				map.put("act", Constent.ACT_WALLET);
				map.put("mobile", phone);
				map.put("ver", Constent.VER);

				AnsynHttpRequest.httpRequest(ChargeGunInforActivity.this,
						AnsynHttpRequest.POST, callBack, Constent.ID_WALLET,
						map, false, true, true);

			}

			break;
		default:
			break;
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
			case Constent.ID_STARTCHARGE:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_start);

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
			case Constent.ID_GETCHARGEID:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_getchargeid);

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
			case Constent.ID_WALLET:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_getwallet);

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

	private int httprequesterror = 0x9010;
	private int httprequestsuccess_getchargeid = 0x9011;// 查询单个电桩信息
	private int httprequestsuccess_start = 0x9012;// 开始充电
	private int httprequestsuccess_getwallet = 0x9013;// 获取余额
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(ChargeGunInforActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null
					&& msg.what == httprequestsuccess_getchargeid) {
				if (jsonObject != null) {

					try {
						if ("0".equals(jsonObject.get("error").toString())) {

							JSONObject dataJsonObject = new JSONObject(
									jsonObject.get("data").toString());
							if (dataJsonObject != null) {
								poleid = dataJsonObject.getString("pole_id");
								mpn = dataJsonObject.getString("mpn");

								String gunnameString = dataJsonObject
										.getJSONObject("gun_info").getString(
												"name");
								if (TextUtils.isEmpty(gunnameString)) {
									titleView.setText(dataJsonObject
											.getString("logic_addr"));
								} else {

									if (gunnameString.contains("A")) {
										titleView.setText(dataJsonObject
												.getString("logic_addr") + "A");
									} else {
										titleView.setText(dataJsonObject
												.getString("logic_addr") + "B");
									}

								}

								statusView.setText(dataJsonObject
										.getString("gun_status_text"));
								dcacView.setText(dataJsonObject
										.getString("charge_pattern_text") + "充");
								typeView.setText(dataJsonObject
										.getString("connection_type_text"));
								edvView.setText(dataJsonObject
										.getString("rated_output_voltage")
										+ " V");
								edaView.setText(dataJsonObject
										.getString("rated_output_current")
										+ " A");
								edwView.setText(dataJsonObject
										.getString("rated_output_power") + " W");

								String stationnameString = dataJsonObject
										.getString("cs_name");
								PublicUtil.setStorage_string(
										ChargeGunInforActivity.this,
										"stationname", stationnameString);

							}

						} else {
							PublicUtil.showToast(ChargeGunInforActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();

					}

				}

			} else if (msg != null && msg.what == httprequestsuccess_start) {
				if (jsonObject != null) {

					try {

						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {

							JSONObject dataJsonObject = new JSONObject(
									jsonObject.get("data").toString());
							if (dataJsonObject != null) {

								vcr_id = dataJsonObject.get("vcr_id")
										.toString();
								showNotificationInfor();

							}

						} else {
							PublicUtil.showToast(ChargeGunInforActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			} else if (msg != null && msg.what == httprequestsuccess_getwallet) {
				if (jsonObject != null) {

					try {

						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {

							JSONObject dataJsonObject = new JSONObject(
									jsonObject.get("data").toString());
							if (dataJsonObject != null) {
								PublicUtil.logDbug(TAG, dataJsonObject
										.getString("money_acount"), 0);
								if (Float.valueOf(dataJsonObject
										.getString("money_acount")) < (float) 50) {
									showInforDialog();
								} else {
									Map<String, String> map = new HashMap<String, String>();
									map.put("mobile", phone);
									map.put("pole_id", poleid);
									map.put("mpn", mpn);
									map.put("ver", Constent.VER);
									map.put("act", Constent.ACT_STARTCHARGE);
									AnsynHttpRequest.httpRequest(
											ChargeGunInforActivity.this,
											AnsynHttpRequest.POST, callBack,
											Constent.ID_STARTCHARGE, map,
											false, true, true);
								}
							}

						} else {
							PublicUtil.showToast(ChargeGunInforActivity.this,
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

	/**
	 * 余额不足提示
	 */
	@SuppressLint("NewApi")
	private void showInforDialog() {
		View view = LayoutInflater.from(ChargeGunInforActivity.this).inflate(
				R.layout.view_inforshow_dialog, null);

		TextView ok = (TextView) view
				.findViewById(R.id.view_inforshow_dialog_phoneing);
		TextView cancel = (TextView) view
				.findViewById(R.id.view_inforshow_dialog_cancel);
		TextView infor = (TextView) view
				.findViewById(R.id.view_inforshow_dialog_infor);
		infor.setText(getResources().getString(R.string.chargingyuebuzu));
		ok.setText(getResources().getString(R.string.lijichongzhi));

		final AlertDialog alertDialog = new AlertDialog.Builder(
				ChargeGunInforActivity.this, R.style.dialog_nostroke).show();
		alertDialog.addContentView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				Intent intent = new Intent(ChargeGunInforActivity.this,
						RechargeActivity.class);
				startActivity(intent);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", phone);
				map.put("pole_id", poleid);
				map.put("mpn", mpn);
				map.put("ver", Constent.VER);
				map.put("act", Constent.ACT_STARTCHARGE);
				AnsynHttpRequest.httpRequest(ChargeGunInforActivity.this,
						AnsynHttpRequest.POST, callBack,
						Constent.ID_STARTCHARGE, map, false, true, true);
			}
		});
	}

	/**
	 * 充电须知提醒
	 */
	private void showNotificationInfor() {

		View view = LayoutInflater.from(ChargeGunInforActivity.this).inflate(
				R.layout.view_notificationinforshow_dialog, null);
		TextView infor = (TextView) view
				.findViewById(R.id.view_notificationinforshow_dialog_infor);
		TextView ok = (TextView) view
				.findViewById(R.id.view_notificationinforshow_dialog_ok);
		infor.setText("安全提示：充电时请拔除车钥匙，人员离开车辆，谢谢合作！");
		ok.setText("确定");
		final AlertDialog alertDialog = new AlertDialog.Builder(
				ChargeGunInforActivity.this, R.style.dialog_nostroke).show();
		alertDialog.addContentView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);

		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				Intent intent = new Intent(ChargeGunInforActivity.this,
						ChargeActivity.class);
				intent.putExtra("vcr_id", vcr_id);
				startActivity(intent);
				finish();
			}
		});

	}

}
