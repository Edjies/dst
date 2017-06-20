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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
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

public class HuiYuanRZActivity extends BaseActivity implements OnClickListener {
	private final String TAG = "HuiYuanRZActivity";
	private final int REQUESTCODE = 0x6310;
	private FrameLayout topLayout;
	private TextView backView, zanburenzhengView;
	private LinearLayout gerenLayout, qiyeLayout;
	private ImageView gerenImageView, qiyeImageView;
	private TextView gerenView, qiyeView;
	private LinearLayout gerencontentLayout, qiyecontentLayout;
	private EditText geren_nameEditText, qiye_nameEditText, qiye_farenEditText;
	private TextView geren_shenfenView, geren_jiazhaoView, qiye_shenfenView,
			qiye_yinyeView;
	private ImageView geren_shenfenImageView, geren_jiazhaoImageView,
			qiye_shenfenImageView, qiye_yingyeImageView;
	private TextView geren_okView, qiye_okView;
	private FrameLayout geren_shenfenLayout, geren_jiazhaoLayout,
			qiye_shenfenLayout, qiye_yingyeLayout;
	private LinearLayout topLinearLayout;
	private CheckBox checkBox;
	private boolean isCheck = true;// 是否同意租车条款
	private String id_auth = "3", drive_auth = "3", business_auth = "3";// 权限
	private int type = 0;

	private String user_name = "", qiye_name = "", faren_name = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_huiyuanrz);
		((MyApplication) getApplication()).addActivity(this);
		init();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

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

		topLayout = (FrameLayout) findViewById(R.id.huiyuanrz_top_layout);
		setTopLayoutPadding(topLayout);

		backView = (TextView) findViewById(R.id.huiyuanrz_back);

		zanburenzhengView = (TextView) findViewById(R.id.huiyuanrz_zanburenzheng);
		gerenLayout = (LinearLayout) findViewById(R.id.huiyuanrz_gerenlayout);
		qiyeLayout = (LinearLayout) findViewById(R.id.huiyuanrz_qiyelayout);
		gerenImageView = (ImageView) findViewById(R.id.huiyuanrz_gerenimage);
		qiyeImageView = (ImageView) findViewById(R.id.huiyuanrz_qiyeimage);
		gerenView = (TextView) findViewById(R.id.huiyuanrz_geren);
		qiyeView = (TextView) findViewById(R.id.huiyuanrz_qiye);
		gerencontentLayout = (LinearLayout) findViewById(R.id.huiyuanrz_geren_contentlayout);
		qiyecontentLayout = (LinearLayout) findViewById(R.id.huiyuanrz_qiye_contentlayout);
		geren_nameEditText = (EditText) findViewById(R.id.huiyuanrz_geren_name);
		qiye_nameEditText = (EditText) findViewById(R.id.huiyuanrz_qiye_name);
		qiye_farenEditText = (EditText) findViewById(R.id.huiyuanrz_qiye_farenname);
		geren_shenfenView = (TextView) findViewById(R.id.huiyuanrz_geren_shenfen);
		geren_jiazhaoView = (TextView) findViewById(R.id.huiyuanrz_geren_jiazhao);
		qiye_shenfenView = (TextView) findViewById(R.id.huiyuanrz_qiye_shenfen);
		qiye_yinyeView = (TextView) findViewById(R.id.huiyuanrz_qiye_yingye);
		geren_shenfenImageView = (ImageView) findViewById(R.id.huiyuanrz_geren_shenfen_image);
		geren_jiazhaoImageView = (ImageView) findViewById(R.id.huiyuanrz_geren_jiazhao_image);
		qiye_shenfenImageView = (ImageView) findViewById(R.id.huiyuanrz_qiye_shenfen_image);
		qiye_yingyeImageView = (ImageView) findViewById(R.id.huiyuanrz_qiye_yingye_image);
		geren_okView = (TextView) findViewById(R.id.huiyuanrz_geren_ok);
		qiye_okView = (TextView) findViewById(R.id.huiyuanrz_qiye_ok);
		geren_shenfenLayout = (FrameLayout) findViewById(R.id.huiyuanrz_geren_shenfenlayout);
		geren_jiazhaoLayout = (FrameLayout) findViewById(R.id.huiyuanrz_geren_jiazhaolayout);
		qiye_shenfenLayout = (FrameLayout) findViewById(R.id.huiyuanrz_qiye_shenfenlayout);
		qiye_yingyeLayout = (FrameLayout) findViewById(R.id.huiyuanrz_qiye_yingyelayout);
		topLinearLayout = (LinearLayout) findViewById(R.id.huiyuanrz_toplayout);
		checkBox = (CheckBox) findViewById(R.id.huiyuanrz_qiye_checkbox);

		zanburenzhengView.setOnClickListener(this);
		backView.setOnClickListener(this);
		gerenLayout.setOnClickListener(this);
		qiyeLayout.setOnClickListener(this);
		geren_okView.setOnClickListener(this);
		qiye_okView.setOnClickListener(this);
		geren_shenfenLayout.setOnClickListener(this);
		geren_jiazhaoLayout.setOnClickListener(this);
		qiye_shenfenLayout.setOnClickListener(this);
		qiye_yingyeLayout.setOnClickListener(this);

		gerencontentLayout.setVisibility(View.VISIBLE);
		qiyecontentLayout.setVisibility(View.GONE);
		gerenImageView.setBackgroundResource(R.drawable.selected);
		qiyeImageView.setBackgroundResource(R.drawable.unselected);
		gerenView.setTextColor(getResources().getColor(R.color.textgreen));
		qiyeView.setTextColor(getResources().getColor(R.color.gray));

		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					isCheck = true;
				} else {
					isCheck = false;
				}

			}
		});

		if ("regist".equals(getIntent().getStringExtra("from"))) {
			topLinearLayout.setVisibility(View.VISIBLE);
			((MyApplication) getApplication()).setSelectiontab(3);
			zanburenzhengView.setVisibility(View.VISIBLE);
		} else {
			topLinearLayout.setVisibility(View.GONE);
			zanburenzhengView.setVisibility(View.GONE);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_MEMBER_INFOR);

		AnsynHttpRequest.httpRequest(HuiYuanRZActivity.this,
				AnsynHttpRequest.GET, callBack, Constent.ID_MEMBER_INFOR, map,
				false, false, true);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		Map<String, String> map;
		// String name, qiye_namenow, qiye_farenname;
		switch (arg0.getId()) {
		case R.id.huiyuanrz_zanburenzheng:
		case R.id.huiyuanrz_back:

			finish();
			break;

		case R.id.huiyuanrz_gerenlayout:
			type = 0;
			gerencontentLayout.setVisibility(View.VISIBLE);
			qiyecontentLayout.setVisibility(View.GONE);
			gerenImageView.setBackgroundResource(R.drawable.selected);
			qiyeImageView.setBackgroundResource(R.drawable.unselected);
			gerenView.setTextColor(getResources().getColor(R.color.textgreen));
			qiyeView.setTextColor(getResources().getColor(R.color.gray));
			break;
		case R.id.huiyuanrz_qiyelayout:
			type = 1;
			gerencontentLayout.setVisibility(View.GONE);
			qiyecontentLayout.setVisibility(View.VISIBLE);
			gerenImageView.setBackgroundResource(R.drawable.unselected);
			qiyeImageView.setBackgroundResource(R.drawable.selected);
			gerenView.setTextColor(getResources().getColor(R.color.gray));
			qiyeView.setTextColor(getResources().getColor(R.color.textgreen));
			break;
		case R.id.huiyuanrz_geren_ok:

			if (TextUtils.isEmpty(user_name)) {
				String name = geren_nameEditText.getText().toString();
				if (TextUtils.isEmpty(name)) {
					PublicUtil.showToast(HuiYuanRZActivity.this, "请填写您的真实姓名",
							false);
					return;
				}

				map = new HashMap<String, String>();
				map.put("act", Constent.ACT_AUTH_INDEX);
				map.put("category", "1");
				map.put("name", PublicUtil.toUTF(name));

				AnsynHttpRequest.httpRequest(HuiYuanRZActivity.this,
						AnsynHttpRequest.GET, callBack, Constent.ID_AUTH_INDEX,
						map, false, true, true);
			} else {
				PublicUtil.showToast(HuiYuanRZActivity.this,
						"您已完成用户姓名认证，不能重复认证哦！", false);
			}

			break;
		case R.id.huiyuanrz_qiye_ok:

			if (!TextUtils.isEmpty(qiye_name) && !TextUtils.isEmpty(faren_name)) {
				PublicUtil.showToast(HuiYuanRZActivity.this,
						"您已完成企业名称和法人代表姓名认证，不能重复认证哦！", false);
			} else {

				String qiye_namenow = qiye_nameEditText.getText().toString();
				if (TextUtils.isEmpty(qiye_namenow)) {
					PublicUtil.showToast(HuiYuanRZActivity.this, "请填写您的企业名称",
							false);
					return;
				}
				String qiye_farenname = qiye_farenEditText.getText().toString();
				if (TextUtils.isEmpty(qiye_farenname)) {
					PublicUtil.showToast(HuiYuanRZActivity.this, "请填写企业法人真实姓名",
							false);
					return;
				}
				if (!isCheck) {
					PublicUtil.showToast(HuiYuanRZActivity.this, "请同意租车服务条款",
							false);
					return;
				}

				map = new HashMap<String, String>();
				map.put("act", Constent.ACT_AUTH_INDEX);
				map.put("category", "2");
				map.put("name", PublicUtil.toUTF(qiye_farenname));
				map.put("enterprise_name", PublicUtil.toUTF(qiye_namenow));

				AnsynHttpRequest.httpRequest(HuiYuanRZActivity.this,
						AnsynHttpRequest.GET, callBack, Constent.ID_AUTH_INDEX,
						map, false, true, true);

			}

			break;
		case R.id.huiyuanrz_geren_shenfenlayout:

			if (TextUtils.isEmpty(user_name)) {
				PublicUtil.showToast(HuiYuanRZActivity.this, "请先提交认证用户姓名",
						false);
				return;
			}

			intent = new Intent(HuiYuanRZActivity.this, ShenFenRZActivity.class);
			intent.putExtra("id_auth", id_auth);
			startActivityForResult(intent, REQUESTCODE);
			break;
		case R.id.huiyuanrz_geren_jiazhaolayout:

			if (TextUtils.isEmpty(user_name)) {
				PublicUtil.showToast(HuiYuanRZActivity.this, "请先提交认证用户姓名",
						false);
				return;
			}
			intent = new Intent(HuiYuanRZActivity.this, JiaZhaoRZActivity.class);
			intent.putExtra("drive_auth", drive_auth);
			startActivityForResult(intent, REQUESTCODE);
			break;
		case R.id.huiyuanrz_qiye_shenfenlayout:
			if (TextUtils.isEmpty(qiye_name)) {
				PublicUtil.showToast(HuiYuanRZActivity.this, "请先提交认证您的企业名称",
						false);
				return;
			}
			if (TextUtils.isEmpty(faren_name)) {
				PublicUtil.showToast(HuiYuanRZActivity.this, "请先提交认证企业法人真实姓名",
						false);
				return;
			}
			intent = new Intent(HuiYuanRZActivity.this, ShenFenRZActivity.class);
			intent.putExtra("id_auth", id_auth);
			startActivityForResult(intent, REQUESTCODE);
			break;
		case R.id.huiyuanrz_qiye_yingyelayout:
			if (TextUtils.isEmpty(qiye_name)) {
				PublicUtil.showToast(HuiYuanRZActivity.this, "请先提交认证您的企业名称",
						false);
				return;
			}
			if (TextUtils.isEmpty(faren_name)) {
				PublicUtil.showToast(HuiYuanRZActivity.this, "请先提交认证企业法人真实姓名",
						false);
				return;
			}
			intent = new Intent(HuiYuanRZActivity.this, YingYeRZActivity.class);
			intent.putExtra("business_auth", business_auth);
			startActivityForResult(intent, REQUESTCODE);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (data != null) {
			if (type == 1) {
				if (resultCode == 21) {// 从身份认证界面回来
					if ("success".equals(data.getStringExtra("result"))) {
						qiye_shenfenImageView.setVisibility(View.GONE);
						qiye_shenfenView.setText("已提交");
					}

				} else if (resultCode == 23) {// 从营业执照认证界面回来
					if ("success".equals(data.getStringExtra("result"))) {
						qiye_yingyeImageView.setVisibility(View.GONE);
						qiye_yinyeView.setText("已提交");
					}
				}

			} else {
				if (resultCode == 21) {// 从身份认证界面回来
					if ("success".equals(data.getStringExtra("result"))) {
						geren_shenfenImageView.setVisibility(View.GONE);
						geren_shenfenView.setText("已提交");
					}
				} else if (resultCode == 22) {// 从驾照认证界面回来
					if ("success".equals(data.getStringExtra("result"))) {
						geren_jiazhaoImageView.setVisibility(View.GONE);
						geren_jiazhaoView.setText("已提交");
					}
				}
			}
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
			// TODO Auto-generated method stub

			switch (backId) {
			case Constent.ID_AUTH_INDEX:

				if (isRequestSuccess) {
					if (!isString) {

						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(success_http_auth);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}
				} else {
					Message message = new Message();
					message.what = error_http_auth;
					message.obj = data;
					handler.sendMessage(message);
				}

				break;
			case Constent.ID_MEMBER_INFOR:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							if (handler != null) {
								handler.sendEmptyMessage(success_httprequest_userinfor);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = error_httprequest_userinfor;
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

	private int error_http_auth = 0x6300;
	private int success_http_auth = 0x6301;
	private int error_httprequest_userinfor = 0x6302;
	private int success_httprequest_userinfor = 0x6303;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == error_http_auth) {
				if (msg.obj != null) {
					PublicUtil.showToast(HuiYuanRZActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_http_auth) {
				if (jsonObject != null) {
					try {

						if ("0".equals(jsonObject.getString("errcode"))) {

							PublicUtil.showToast(HuiYuanRZActivity.this,
									"请继续完成照片信息认证", false);
							if (type == 0) {
								user_name = geren_nameEditText.getText()
										.toString();
								geren_nameEditText.setEnabled(false);
								geren_okView
										.setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
							} else {
								qiye_name = qiye_nameEditText.getText()
										.toString();
								faren_name = qiye_farenEditText.getText()
										.toString();
								qiye_nameEditText.setEnabled(false);
								qiye_farenEditText.setEnabled(false);
								qiye_okView
										.setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
							}

							// finish();

						} else {
							PublicUtil.showToast(HuiYuanRZActivity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {

								Intent intent = new Intent(
										HuiYuanRZActivity.this,
										LoginActivity.class);
								startActivity(intent);

							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();

					}

				}

			} else if (msg != null && msg.what == error_httprequest_userinfor) {
				if (msg.obj != null) {
					PublicUtil.showToast(HuiYuanRZActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest_userinfor) {
				if (jsonObject != null) {

					try {

						if ("0".equals(jsonObject.getString("errcode"))) {

							JSONObject dataJsonObject = jsonObject
									.getJSONObject("data");
							if (dataJsonObject != null) {

								if (!TextUtils.isEmpty(dataJsonObject
										.getString("id_card_url"))
										&& !"null".equals(dataJsonObject
												.getString("id_card_url"))) {
									PublicUtil
											.setStorage_string(
													HuiYuanRZActivity.this,
													"id_card_url",
													Constent.URLHEAD_IMAGE
															+ dataJsonObject
																	.getString("id_card_url"));
								}

								if (!TextUtils.isEmpty(dataJsonObject
										.getString("id_card_url1"))
										&& !"null".equals(dataJsonObject
												.getString("id_card_url1"))) {
									PublicUtil
											.setStorage_string(
													HuiYuanRZActivity.this,
													"id_card_url1",
													Constent.URLHEAD_IMAGE
															+ dataJsonObject
																	.getString("id_card_url1"));
								}

								if (!TextUtils.isEmpty(dataJsonObject
										.getString("driving_card_url"))
										&& !"null".equals(dataJsonObject
												.getString("driving_card_url"))) {
									PublicUtil
											.setStorage_string(
													HuiYuanRZActivity.this,
													"driving_card_url",
													Constent.URLHEAD_IMAGE
															+ dataJsonObject
																	.getString("driving_card_url"));
								}

								if (!TextUtils.isEmpty(dataJsonObject
										.getString("business_license_url"))
										&& !"null"
												.equals(dataJsonObject
														.getString("business_license_url"))) {
									PublicUtil
											.setStorage_string(
													HuiYuanRZActivity.this,
													"business_license_url",
													Constent.URLHEAD_IMAGE
															+ dataJsonObject
																	.getString("business_license_url"));
								}

								gerenLayout.setVisibility(View.VISIBLE);
								qiyeLayout.setVisibility(View.VISIBLE);
								if ("1".equals(dataJsonObject
										.getString("category"))) {// 个人
									type = 0;
									qiyeLayout.setVisibility(View.GONE);
									qiyecontentLayout.setVisibility(View.GONE);
									gerencontentLayout
											.setVisibility(View.VISIBLE);

									id_auth = dataJsonObject
											.getString("id_card_auth");
									drive_auth = dataJsonObject
											.getString("driving_card_auth");

									if ("2".equals(id_auth)) {// 不通过
										geren_shenfenImageView
												.setVisibility(View.VISIBLE);
										geren_shenfenView.setText("未通过");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03);

									} else if ("1".equals(id_auth)) {// 已认证
										geren_shenfenImageView
												.setVisibility(View.GONE);
										geren_shenfenView.setText("已认证");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
									} else if ("0".equals(id_auth)) {//
										geren_shenfenImageView
												.setVisibility(View.GONE);
										geren_shenfenView.setText("审核中");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
									} else {// 待认证
										geren_shenfenImageView
												.setVisibility(View.VISIBLE);
										geren_shenfenView.setText("未认证");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03);
									}

									if ("2".equals(drive_auth)) {// 不通过
										geren_jiazhaoImageView
												.setVisibility(View.VISIBLE);
										geren_jiazhaoView.setText("未通过");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03);
									} else if ("1".equals(drive_auth)) {// 已认证
										geren_jiazhaoImageView
												.setVisibility(View.GONE);
										geren_jiazhaoView.setText("已认证");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
									} else if ("0".equals(drive_auth)) {// 审核中
										geren_jiazhaoImageView
												.setVisibility(View.GONE);
										geren_jiazhaoView.setText("审核中");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
									} else {// 待认证
										geren_jiazhaoImageView
												.setVisibility(View.VISIBLE);
										geren_jiazhaoView.setText("未认证");
										// geren_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03);
									}

									if (!TextUtils.isEmpty(dataJsonObject
											.getString("name"))
											&& !"null".equals(dataJsonObject
													.getString("name"))) {

										user_name = dataJsonObject
												.getString("name");
										geren_nameEditText.setText(user_name);
										geren_nameEditText.setEnabled(false);
										geren_okView
												.setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);

									} else {
										geren_okView
												.setBackgroundResource(R.drawable.chongdianyemian5_1_03);
									}

								} else if ("2".equals(dataJsonObject
										.getString("category"))) {// 企业

									qiyeImageView
											.setBackgroundResource(R.drawable.selected);
									qiyeView.setTextColor(getResources()
											.getColor(R.color.textgreen));
									type = 1;
									gerenLayout.setVisibility(View.GONE);
									gerencontentLayout.setVisibility(View.GONE);
									qiyecontentLayout
											.setVisibility(View.VISIBLE);
									if (!TextUtils.isEmpty(dataJsonObject
											.getString("name"))
											&& !"null".equals(dataJsonObject
													.getString("name"))) {

										faren_name = dataJsonObject
												.getString("name");
										qiye_farenEditText.setText(faren_name);
										qiye_farenEditText.setEnabled(false);
									}

									if (!TextUtils.isEmpty(dataJsonObject
											.getString("enterprise_name"))
											&& !"null"
													.equals(dataJsonObject
															.getString("enterprise_name"))) {
										qiye_name = dataJsonObject
												.getString("enterprise_name");
										qiye_nameEditText.setText(qiye_name);
										qiye_nameEditText.setEnabled(false);
										checkBox.setVisibility(View.GONE);
									}

									id_auth = dataJsonObject
											.getString("id_card_auth");
									business_auth = dataJsonObject
											.getString("business_license_auth");

									if ("2".equals(id_auth)) {// 不通过
										qiye_shenfenImageView
												.setVisibility(View.VISIBLE);
										qiye_shenfenView.setText("未通过");

									} else if ("1".equals(id_auth)) {// 已认证
										qiye_shenfenImageView
												.setVisibility(View.GONE);
										qiye_shenfenView.setText("已认证");
									} else if ("0".equals(id_auth)) {// 审核中
										qiye_shenfenImageView
												.setVisibility(View.GONE);
										qiye_shenfenView.setText("审核中");

									} else {// 待认证
										qiye_shenfenImageView
												.setVisibility(View.VISIBLE);
										qiye_shenfenView.setText("未认证");
									}

									if ("2".equals(business_auth)) {// 不通过
										qiye_yingyeImageView
												.setVisibility(View.VISIBLE);
										qiye_yinyeView.setText("未通过");
										// qiye_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03);
									} else if ("1".equals(business_auth)) {// 已认证
										qiye_yingyeImageView
												.setVisibility(View.GONE);
										qiye_yinyeView.setText("已认证");
										// qiye_okView
										// .setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
									} else if ("0".equals(business_auth)) {// 审核中
										qiye_yingyeImageView
												.setVisibility(View.GONE);
										qiye_yinyeView.setText("审核中");

									} else {// 待认证
										qiye_yingyeImageView
												.setVisibility(View.VISIBLE);
										qiye_yinyeView.setText("未认证");

									}

									if (!TextUtils.isEmpty(qiye_name)
											&& !TextUtils.isEmpty(faren_name)) {
										qiye_okView
												.setBackgroundResource(R.drawable.chongdianyemian5_1_03gary);
									} else {
										qiye_okView
												.setBackgroundResource(R.drawable.chongdianyemian5_1_03);

									}

								} else {

									id_auth = dataJsonObject
											.getString("id_card_auth");
									business_auth = dataJsonObject
											.getString("business_license_auth");
									drive_auth = dataJsonObject
											.getString("driving_card_auth");

									gerencontentLayout
											.setVisibility(View.VISIBLE);
									qiyecontentLayout.setVisibility(View.GONE);

									qiye_yingyeImageView
											.setVisibility(View.VISIBLE);
									qiye_shenfenImageView
											.setVisibility(View.VISIBLE);

									geren_jiazhaoImageView
											.setVisibility(View.VISIBLE);

									geren_shenfenImageView
											.setVisibility(View.VISIBLE);

									qiye_yinyeView.setText("未认证");
									geren_jiazhaoView.setText("未认证");
									qiye_shenfenView.setText("未认证");
									geren_shenfenView.setText("未认证");

								}

							}

						} else {
							PublicUtil.showToast(HuiYuanRZActivity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {
								Intent intent = new Intent(
										HuiYuanRZActivity.this,
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

}
