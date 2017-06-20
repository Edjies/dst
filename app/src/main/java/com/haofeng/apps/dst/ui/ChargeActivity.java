package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import java.util.Timer;
import java.util.TimerTask;

/**
 * 充电界面
 * 
 * @author Administrator
 * 
 */
public class ChargeActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "SearchPowerActivity";
	private FrameLayout topLayout;
	private TextView backTextView, nameTextView, inforleftTextView,
			inforrightTextView;
	private ImageView typeImageView;
	private String vcr_id = null;
	private AnimationDrawable animationDrawable;
	private TextView socTextView, starttimeTextView, chargetimeTextView,
			endtimeTextView, acsocTextView;
	private String phone;
	private Timer timer = null;
	private Button returnbButton, endButton;
	private LinearLayout chargedc2Layout;
	private FrameLayout chargedcLayout, chargeacLayout;
	private AlertDialog alertDialog;
	private int nowsoc = -1;
	private boolean animhasstart = false;
	private String typeString;
	private boolean isshowtoast = true;
	private ImageView acImageView;
	private Animation operatingAnim;
	private String stationnameString;
	private boolean isshownonettoast = true;
	private boolean isfinsh = false;
	private int endcount = 1;
	private ProgressDialog endDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_charge);
		addActivity(this);
		init();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// 友盟统计开始
		if (!isshownonettoast) {
			isshownonettoast = true;
		}

		if (isfinsh) {
			Intent intent = new Intent(ChargeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// 友盟统计结束
	}

	public void init() {

		topLayout = (FrameLayout) findViewById(R.id.charge_top_layout);
		setTopLayoutPadding(topLayout);
		backTextView = (TextView) findViewById(R.id.charge_back);
		nameTextView = (TextView) findViewById(R.id.charge_name);
		inforleftTextView = (TextView) findViewById(R.id.charge_inforleft);
		typeImageView = (ImageView) findViewById(R.id.charge_connection);
		inforrightTextView = (TextView) findViewById(R.id.charge_inforright);
		socTextView = (TextView) findViewById(R.id.charge_soc);
		starttimeTextView = (TextView) findViewById(R.id.charge_starttime);
		chargetimeTextView = (TextView) findViewById(R.id.charge_chargetime);
		endtimeTextView = (TextView) findViewById(R.id.charge_endtime);
		returnbButton = (Button) findViewById(R.id.charge_returnbvutton);
		endButton = (Button) findViewById(R.id.charge_endbutton);
		chargeacLayout = (FrameLayout) findViewById(R.id.charge_chargeaclayout);
		chargedc2Layout = (LinearLayout) findViewById(R.id.charge_chargedc2layout);
		chargedcLayout = (FrameLayout) findViewById(R.id.charge_chargedclayout);
		acImageView = (ImageView) findViewById(R.id.charge_chargeacimage);
		acsocTextView = (TextView) findViewById(R.id.charge_socac);
		backTextView.setOnClickListener(this);
		endButton.setOnClickListener(this);
		returnbButton.setOnClickListener(this);
		phone = PublicUtil
				.getStorage_string(ChargeActivity.this, "phone", "-1");
		stationnameString = PublicUtil.getStorage_string(ChargeActivity.this,
				"stationname", "-1");

		if (!"-1".equals(stationnameString)) {
			nameTextView.setText(stationnameString);
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(ChargeActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);
			return;
		}
		chargedc2Layout.setVisibility(View.VISIBLE);
		chargeacLayout.setVisibility(View.GONE);
		chargedcLayout.setVisibility(View.GONE);

		vcr_id = getIntent().getStringExtra("vcr_id");

		if (vcr_id == null || TextUtils.isEmpty(vcr_id)) {
			PublicUtil.showToast(ChargeActivity.this, "充电信息获取失败", false);
			return;
		} else {
			alertDialog = PublicUtil.showDialog(ChargeActivity.this);

			timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(getchargeinfor);
				}
			}, 0, 5 * 1000);
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.charge_back:
			intent = new Intent(ChargeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();

			break;
		case R.id.charge_endbutton:
			isEndCharing();

			break;
		case R.id.charge_returnbvutton:
			intent = new Intent(ChargeActivity.this, MainActivity.class);
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
			Intent intent = new Intent(ChargeActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.purge();
			timer.cancel();
			timer = null;
		}
		if (handler != null) {
			handler.removeMessages(getchargeinfor);
		}

	}

	private JSONObject jsonObject = null;
	private JSONObject jsonObject2 = null;
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
			case Constent.ID_ENDCHARGE:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_end);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = httprequesterror;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								handler.sendMessage(message);
							}
						}
					}

				} else {
					if (timer == null) {
						timer = new Timer();
						timer.schedule(new TimerTask() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								handler.sendEmptyMessage(getchargeinfor);
							}
						}, 0, 5 * 1000);
					}

					if (handler != null) {
						Message message = new Message();
						message.what = httprequesterror;
						message.obj = data;
						handler.sendMessage(message);
					}
				}
				break;

			case Constent.ID_GETCHARGEINFOR:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject2 = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_getchargeinginfor);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = httprequesterror;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								handler.sendMessage(message);
							}
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

	private int httprequesterror = 0x4000;
	private int httprequestsuccess_end = 0x4001;// 结束充电
	private int httprequestsuccess_getchargeinginfor = 0x4002;// 定时获取充电信息成功
	private int getchargeinfor = 0x4003;// 定时获取充电状态信息
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == getchargeinfor) {

				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", phone);
				map.put("vcr_id", vcr_id);
				map.put("ver", Constent.VER);
				map.put("act", Constent.ACT_GETCHARGEINFOR);
				AnsynHttpRequest.httpRequest(ChargeActivity.this,
						AnsynHttpRequest.POST, callBack,
						Constent.ID_GETCHARGEINFOR, map, false, false,
						isshownonettoast);
			}

			if (msg != null && msg.what == httprequesterror) {

				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				if (msg.obj != null) {
					PublicUtil.showToast(ChargeActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess_end) {
				if (jsonObject != null) {

					try {
						if ("0".equals(jsonObject.get("error").toString())) {

							if (animationDrawable != null) {
								animationDrawable.stop();
							}
							if (operatingAnim != null) {
								acImageView.clearAnimation();

							}
							jsonObject2 = null;
							handler.removeMessages(getchargeinfor);
							handler.removeMessages(httprequestsuccess_getchargeinginfor);
							if (timer != null) {
								timer.purge();
								timer.cancel();
								timer = null;
							}

							if (endDialog != null) {
								endDialog.dismiss();
								endDialog = null;
							}

							JSONObject dataJsonObject = jsonObject
									.getJSONObject("data");

							Intent intent = new Intent(ChargeActivity.this,
									ChargeEndActivity.class);
							intent.putExtra("fm_id",
									dataJsonObject.getString("fm_id"));
							intent.putExtra("fm_end_id",
									dataJsonObject.getString("fm_end_id"));
							intent.putExtra("name", nameTextView.getText()
									.toString());
							intent.putExtra("infor", inforrightTextView
									.getText().toString());
							intent.putExtra("phone", phone);
							startActivity(intent);
							finish();

						} else {
							if (animationDrawable != null) {
								animationDrawable.stop();
							}
							if (operatingAnim != null) {
								acImageView.clearAnimation();

							}
							if (timer != null) {
								timer.purge();
								timer.cancel();
								timer = null;
							}
							jsonObject2 = null;
							handler.removeMessages(getchargeinfor);
							handler.removeMessages(httprequestsuccess_getchargeinginfor);
							endcount++;
							if (endcount < 4) {

								if (endDialog == null) {
									endDialog = ProgressDialog.show(
											ChargeActivity.this, null,
											"充电桩没有响应，正在重试，请稍等");
									endDialog
											.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置风格为圆形进度条
									endDialog.setCancelable(true);// 设置点击返回键对话框消失
									endDialog.setCanceledOnTouchOutside(true);// 设置点击进度对话框外的区域对话框消失

								}

								// PublicUtil.showToast(SearchPowerActivity.this,
								// "充电桩没有响应，正在重试，请稍等", false);
								Map<String, String> map = new HashMap<String, String>();
								map.put("mobile", phone);
								map.put("vcr_id", vcr_id);
								map.put("ver", Constent.VER);
								map.put("act", Constent.ACT_ENDCHARGE);
								AnsynHttpRequest.httpRequest(
										ChargeActivity.this,
										AnsynHttpRequest.POST, callBack,
										Constent.ID_ENDCHARGE, map, false,
										false, true);

							} else {
								if (endDialog != null) {
									endDialog.dismiss();
									endDialog = null;
								}
								showEndErrorDialog();
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PublicUtil.showToast(ChargeActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			} else if (msg != null
					&& msg.what == httprequestsuccess_getchargeinginfor) {
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				if (jsonObject2 != null) {

					try {
						if ("0".equals(jsonObject2.get("error").toString())) {

							JSONObject dataJsonObject = new JSONObject(
									jsonObject2.get("data").toString());
							vcr_id = dataJsonObject.getString("vcr_id");
							PublicUtil.logDbug(TAG, dataJsonObject.toString(),
									0);
							if (dataJsonObject != null) {
								typeString = dataJsonObject
										.getString("gun_type");

								if ("DC".equals(typeString)) {
									chargedcLayout.setVisibility(View.VISIBLE);
									chargeacLayout.setVisibility(View.GONE);
									chargedc2Layout.setVisibility(View.GONE);
									String socString = dataJsonObject
											.getString("soc");

									if (socString != null
											&& !TextUtils.isEmpty(socString)) {

										socTextView.setText(socString + "%");
										String endtimeString = dataJsonObject
												.get("estimate_time")
												.toString();
										if (endtimeString != null
												&& !TextUtils
														.isEmpty(endtimeString)) {
											endtimeTextView
													.setText(dataJsonObject
															.getString("estimate_time"));
										}

										if (Integer.parseInt(socString) == 100) {
											if (timer != null) {
												timer.purge();
												timer.cancel();
												timer = null;
											}
											jsonObject2 = null;
											handler.removeMessages(getchargeinfor);
											handler.removeMessages(httprequestsuccess_getchargeinginfor);

											Map<String, String> map = new HashMap<String, String>();
											map.put("mobile", phone);
											map.put("vcr_id", vcr_id);
											map.put("ver", Constent.VER);
											map.put("act",
													Constent.ACT_ENDCHARGE);
											AnsynHttpRequest.httpRequest(
													ChargeActivity.this,
													AnsynHttpRequest.POST,
													callBack,
													Constent.ID_ENDCHARGE, map,
													false, true, true);
											return;
										}

										int soc = Integer.parseInt(socString) / 10;
										PublicUtil.logDbug(TAG, soc + ":"
												+ nowsoc, 0);
										if (soc > nowsoc) {
											nowsoc = soc;
											dcAnim();
										}

									}
								} else {

									chargeacLayout.setVisibility(View.VISIBLE);
									chargedcLayout.setVisibility(View.GONE);
									chargedc2Layout.setVisibility(View.GONE);
									endtimeTextView.setText("交流桩暂时无法预计结束时间");

									if (dataJsonObject
											.getString("charge_electricity") != null
											&& !TextUtils
													.isEmpty(dataJsonObject
															.getString("charge_electricity"))) {
										acsocTextView
												.setText(dataJsonObject
														.getString("charge_electricity"));
									}

									if (!animhasstart) {
										animhasstart = true;
										operatingAnim = AnimationUtils
												.loadAnimation(
														ChargeActivity.this,
														R.anim.charging);
										LinearInterpolator lin = new LinearInterpolator();
										operatingAnim.setInterpolator(lin);
										acImageView
												.startAnimation(operatingAnim);
									}
								}

								if ("1".equals(dataJsonObject
										.getString("charge_complete"))) {
									if (timer != null) {
										timer.purge();
										timer.cancel();
										timer = null;
									}
									jsonObject2 = null;
									handler.removeMessages(getchargeinfor);
									handler.removeMessages(httprequestsuccess_getchargeinginfor);
									Map<String, String> map = new HashMap<String, String>();
									map.put("mobile", phone);
									map.put("vcr_id", vcr_id);
									map.put("ver", Constent.VER);
									map.put("act", Constent.ACT_ENDCHARGE);
									AnsynHttpRequest.httpRequest(
											ChargeActivity.this,
											AnsynHttpRequest.POST, callBack,
											Constent.ID_ENDCHARGE, map, false,
											true, true);
									return;
								}

								// animationDrawable = (AnimationDrawable)
								// chargingView
								// .getDrawable();
								// animationDrawable.start();
								if (dataJsonObject.getString("cs_name") != null
										&& !TextUtils.isEmpty(dataJsonObject
												.getString("cs_name"))) {
									nameTextView.setText(dataJsonObject
											.getString("cs_name"));
								}

								if ("BYD".equals(dataJsonObject
										.getString("connection_type"))) {
									typeImageView
											.setBackgroundResource(R.drawable.xiangqing_byd);
								} else if ("TESLA".equals(dataJsonObject
										.getString("connection_type"))) {
									typeImageView
											.setBackgroundResource(R.drawable.xiangqing_tesila);
								} else if ("GB".equals(dataJsonObject
										.getString("connection_type"))) {
									typeImageView
											.setBackgroundResource(R.drawable.xiangqing_guoji);
								} else {
									typeImageView
											.setBackgroundResource(R.drawable.xiangqing_other);
								}

								// String leftinforString = dataJsonObject
								// .getString("gun_name");
								// if (!TextUtils.isEmpty(leftinforString)) {
								// leftinforString = leftinforString
								// + "  枪号："
								// + dataJsonObject
								// .getString("gun_code");
								// } else {
								// leftinforString = dataJsonObject
								// .getString("gun_code");
								// }

								inforleftTextView.setText("枪号："
										+ dataJsonObject
												.getString("logic_addr")
										+ dataJsonObject.getString("gun_name"));

								String charge_pattern_text = dataJsonObject
										.getString("charge_pattern_text");
								if (!TextUtils.isEmpty(charge_pattern_text)) {
									charge_pattern_text = charge_pattern_text
											+ "充";
								}

								charge_pattern_text = charge_pattern_text
										+ " "
										+ dataJsonObject
												.getString("gun_type_text");

								inforrightTextView.setText(charge_pattern_text);
								chargetimeTextView.setText("已充时间："
										+ dataJsonObject
												.getString("already_time"));

								String starttime = dataJsonObject.get(
										"start_time").toString();
								if (starttime != null
										&& !TextUtils.isEmpty(starttime)) {
									starttimeTextView.setText(starttime);
								}

							}

						} else if ("1".equals(jsonObject2.get("error")
								.toString())) {
							PublicUtil.showToast(ChargeActivity.this,
									jsonObject2.get("msg").toString(), false);
							if (animationDrawable != null) {
								animationDrawable.stop();
							}

							if (operatingAnim != null) {
								acImageView.clearAnimation();

							}

							jsonObject2 = null;
							handler.removeMessages(getchargeinfor);
							handler.removeMessages(httprequestsuccess_getchargeinginfor);
							if (timer != null) {
								timer.purge();
								timer.cancel();
								timer = null;
							}
							Intent intent = new Intent(ChargeActivity.this,
									ChargeListActivity.class);
							startActivity(intent);
							finish();

						} else if ("2".equals(jsonObject2.get("error")
								.toString())) {

							if (isshowtoast) {
								isshowtoast = false;
								PublicUtil.showToast(ChargeActivity.this,
										jsonObject2.get("msg").toString(),
										false);
							}

							JSONObject dataJsonObject = new JSONObject(
									jsonObject2.get("data").toString());
							vcr_id = dataJsonObject.getString("vcr_id");
							PublicUtil.logDbug(TAG, dataJsonObject.toString(),
									0);

							String endtimeString = dataJsonObject.get(
									"estimate_time").toString();
							if (endtimeString != null
									&& !TextUtils.isEmpty(endtimeString)) {
								endtimeTextView.setText(dataJsonObject
										.getString("estimate_time"));
							}

							nameTextView.setText(dataJsonObject
									.getString("cs_name"));

							if ("BYD".equals(dataJsonObject
									.getString("connection_type"))) {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_byd);
							} else if ("TESLA".equals(dataJsonObject
									.getString("connection_type"))) {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_tesila);
							} else if ("GB".equals(dataJsonObject
									.getString("connection_type"))) {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_guoji);
							} else {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_other);
							}

							// String leftinforString = dataJsonObject
							// .getString("gun_name");
							// if (!TextUtils.isEmpty(leftinforString)) {
							// leftinforString = leftinforString + "  枪号："
							// + dataJsonObject.getString("gun_code");
							// } else {
							// leftinforString = dataJsonObject
							// .getString("gun_code");
							// }

							inforleftTextView.setText("枪号："
									+ dataJsonObject.getString("logic_addr")
									+ dataJsonObject.getString("gun_name"));

							String charge_pattern_text = dataJsonObject
									.getString("charge_pattern_text");
							if (!TextUtils.isEmpty(charge_pattern_text)) {
								charge_pattern_text = charge_pattern_text + "充";
							}

							charge_pattern_text = charge_pattern_text + "    "
									+ dataJsonObject.getString("gun_type_text");

							inforrightTextView.setText(charge_pattern_text);
							chargetimeTextView.setText("已充时间："
									+ dataJsonObject.getString("already_time"));

							String starttime = dataJsonObject.get("start_time")
									.toString();
							if (starttime != null
									&& !TextUtils.isEmpty(starttime)) {
								starttimeTextView.setText(starttime);
							}

						} else {

							PublicUtil.showToast(ChargeActivity.this,
									jsonObject2.get("msg").toString(), false);

							JSONObject dataJsonObject = new JSONObject(
									jsonObject2.get("data").toString());
							vcr_id = dataJsonObject.getString("vcr_id");
							PublicUtil.logDbug(TAG, dataJsonObject.toString(),
									0);

							String endtimeString = dataJsonObject.get(
									"estimate_time").toString();
							if (endtimeString != null
									&& !TextUtils.isEmpty(endtimeString)) {
								endtimeTextView.setText(dataJsonObject
										.getString("estimate_time"));
							}

							nameTextView.setText(dataJsonObject
									.getString("cs_name"));

							if ("BYD".equals(dataJsonObject
									.getString("connection_type"))) {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_byd);
							} else if ("TESLA".equals(dataJsonObject
									.getString("connection_type"))) {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_tesila);
							} else if ("GB".equals(dataJsonObject
									.getString("connection_type"))) {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_guoji);
							} else {
								typeImageView
										.setBackgroundResource(R.drawable.xiangqing_other);
							}

							// String leftinforString = dataJsonObject
							// .getString("gun_name");
							// if (!TextUtils.isEmpty(leftinforString)) {
							// leftinforString = leftinforString + "  枪号："
							// + dataJsonObject.getString("gun_code");
							// } else {
							// leftinforString = dataJsonObject
							// .getString("gun_code");
							// }

							inforleftTextView.setText("枪号："
									+ dataJsonObject.getString("logic_addr")
									+ dataJsonObject.getString("gun_name"));

							String charge_pattern_text = dataJsonObject
									.getString("charge_pattern_text");
							if (!TextUtils.isEmpty(charge_pattern_text)) {
								charge_pattern_text = charge_pattern_text + "充";
							}

							charge_pattern_text = charge_pattern_text + "    "
									+ dataJsonObject.getString("gun_type_text");

							inforrightTextView.setText(charge_pattern_text);
							chargetimeTextView.setText("已充时间："
									+ dataJsonObject.getString("already_time"));

							String starttime = dataJsonObject.get("start_time")
									.toString();
							if (starttime != null
									&& !TextUtils.isEmpty(starttime)) {
								starttimeTextView.setText(starttime);
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PublicUtil.showToast(ChargeActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

	/**
	 * 直流时
	 */
	private void dcAnim() {

		try {
			if (nowsoc == 0) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation);
			} else if (nowsoc == 1) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation2);
			} else if (nowsoc == 2) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation3);
			} else if (nowsoc == 3) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation4);
			} else if (nowsoc == 4) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation5);
			} else if (nowsoc == 5) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation6);
			} else if (nowsoc == 6) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation7);
			} else if (nowsoc == 7) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation8);
			} else if (nowsoc == 8) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation9);
			} else if (nowsoc == 9) {
				chargedcLayout
						.setBackgroundResource(R.drawable.chargeanimation10);
			}
			animationDrawable = (AnimationDrawable) chargedcLayout
					.getBackground();
			if (animationDrawable != null) {
				animationDrawable.start();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 充电结束失败提示
	 */
	@SuppressLint("NewApi")
	private void showEndErrorDialog() {
		View view = LayoutInflater.from(ChargeActivity.this).inflate(
				R.layout.view_inforshow_dialog2, null);

		TextView ok = (TextView) view
				.findViewById(R.id.view_inforshow_dialog2_ok);

		final AlertDialog alertDialog = new AlertDialog.Builder(
				ChargeActivity.this, R.style.dialog_nostroke).show();
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
				Intent intent = new Intent(ChargeActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	/**
	 * 是否停止充电
	 */
	@SuppressLint("NewApi")
	private void isEndCharing() {

		View view = LayoutInflater.from(ChargeActivity.this).inflate(
				R.layout.view_delet_dialog, null);
		TextView title = (TextView) view.findViewById(R.id.view_dialog_title);
		TextView ok = (TextView) view.findViewById(R.id.view_dialog_ok);
		TextView cancel = (TextView) view.findViewById(R.id.view_dialog_cancel);
		title.setText("是否停止充电？");

		final AlertDialog alertDialog = new AlertDialog.Builder(
				ChargeActivity.this, R.style.dialog_nostroke).show();
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
				jsonObject2 = null;
				handler.removeMessages(getchargeinfor);
				handler.removeMessages(httprequestsuccess_getchargeinginfor);
				endcount = 1;
				endDialog = null;
				Map<String, String> map = new HashMap<String, String>();
				map.put("mobile", phone);
				map.put("vcr_id", vcr_id);
				map.put("ver", Constent.VER);
				map.put("act", Constent.ACT_ENDCHARGE);
				AnsynHttpRequest.httpRequest(ChargeActivity.this,
						AnsynHttpRequest.POST, callBack, Constent.ID_ENDCHARGE,
						map, false, true, true);

			}
		});

		cancel.setOnClickListener(new OnClickListener() {

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
