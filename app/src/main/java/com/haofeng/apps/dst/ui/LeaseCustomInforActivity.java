package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
 * 企业客户信息
 * 
 * @author Administrator
 * 
 */
public class LeaseCustomInforActivity extends BaseActivity implements
		OnClickListener {
	private static String TAG = "LeaseCustomInforActivity";
	private TextView backTextView, carlisTextView;
	private TextView companyTextView, numberTextView, instalTextView,
			introTextView;
	private LinearLayout contentLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leasecustominfor);
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
		backTextView = (TextView) findViewById(R.id.leasecustom_back);
		carlisTextView = (TextView) findViewById(R.id.leasecustom_carlist);
		companyTextView = (TextView) findViewById(R.id.leasecustom_company);
		numberTextView = (TextView) findViewById(R.id.leasecustom_number);
		instalTextView = (TextView) findViewById(R.id.leasecustom_instal);
		introTextView = (TextView) findViewById(R.id.leasecustom_intro);
		contentLayout = (LinearLayout) findViewById(R.id.leasecustom_contentlayout);
		backTextView.setOnClickListener(this);
		carlisTextView.setOnClickListener(this);

		if (phone == null) {
			phone = PublicUtil.getStorage_string(LeaseCustomInforActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(LeaseCustomInforActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_GETLEASECUSTOM);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);

			AnsynHttpRequest.httpRequest(LeaseCustomInforActivity.this,
					AnsynHttpRequest.POST, callBack,
					Constent.ID_GETLEASECUSTOM, map, false, true, true);
		}

	}

	private String phone;

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
			case Constent.ID_GETLEASECUSTOM:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess);

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

	private int httprequesterror = 0x3700;
	private int httprequestsuccess = 0x3701;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(LeaseCustomInforActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {

					try {
						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {
							PublicUtil.logDbug(TAG,
									jsonObject.get("data") + "", 0);
							JSONObject dataObject = (JSONObject) jsonObject
									.get("data");

							if (dataObject != null) {
								contentLayout.setVisibility(View.VISIBLE);
								companyTextView.setText(dataObject
										.getString("company_name"));
								numberTextView.setText("（"
										+ dataObject.getString("number") + "）");
								instalTextView.setText(dataObject
										.getString("company_addr"));
								introTextView.setText(dataObject
										.getString("company_brief"));
							} else {
								PublicUtil.showToast(
										LeaseCustomInforActivity.this,
										"解析数据错误", false);
							}

						} else {
							PublicUtil.showToast(LeaseCustomInforActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(LeaseCustomInforActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(LeaseCustomInforActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.leasecustom_back:
			intent = new Intent(LeaseCustomInforActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.leasecustom_carlist:
			intent = new Intent(LeaseCustomInforActivity.this,
					LeaseCarslistActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}

	}

}
