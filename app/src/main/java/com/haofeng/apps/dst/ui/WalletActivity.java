package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
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

public class WalletActivity extends BaseActivity implements OnClickListener {
	private final String TAG = "WalletActivity";
	private FrameLayout topLayout;
	private TextView balanceTextView, backTextView;
	private FrameLayout chongzhiLayout, tixianLayout, mingxiLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallet);
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

	private void init() {

		topLayout = (FrameLayout) findViewById(R.id.wallet_top_layout);
		if (android.os.Build.VERSION.SDK_INT < 19) {
			topLayout.setPadding(0, 0, 0, 0);
		}
		balanceTextView = (TextView) findViewById(R.id.wallet_money);
		backTextView = (TextView) findViewById(R.id.wallet_back);
		chongzhiLayout = (FrameLayout) findViewById(R.id.wallet_chongzhi_layout);
		tixianLayout = (FrameLayout) findViewById(R.id.wallet_tixian_layout);
		mingxiLayout = (FrameLayout) findViewById(R.id.wallet_mingxi_layout);
		chongzhiLayout.setOnClickListener(this);
		tixianLayout.setOnClickListener(this);
		mingxiLayout.setOnClickListener(this);
		backTextView.setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_MEMBER_INFOR);
		map.put("ver", Constent.VER);

		AnsynHttpRequest.httpRequest(WalletActivity.this, AnsynHttpRequest.GET,
				callBack, Constent.ID_MEMBER_INFOR, map, false, false, true);
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

	private int error_httprequest_userinfor = 0x6010;
	private int success_httprequest_userinfor = 0x6011;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == error_httprequest_userinfor) {
				if (msg.obj != null) {
					PublicUtil.showToast(WalletActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest_userinfor) {
				if (jsonObject != null) {

					try {
						if ("0".equals(jsonObject.getString("errcode"))) {

							JSONObject dataJsonObject = jsonObject
									.getJSONObject("data");
							if (dataJsonObject != null) {

								balanceTextView.setText(dataJsonObject
										.getString("amount"));

							}

						} else {
							PublicUtil.showToast(WalletActivity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {

								Intent intent = new Intent(WalletActivity.this,
										LoginActivity.class);
								startActivity(intent);

							}

						}
					} catch (Exception e) {
						// TODO: handle exception

					}

				}

			}

		};
	};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.wallet_mingxi_layout:
			intent = new Intent(WalletActivity.this, WalletlistActivity.class);
			startActivity(intent);

			break;
		case R.id.wallet_chongzhi_layout:
			intent = new Intent(WalletActivity.this, RechargeActivity.class);
			startActivity(intent);

			break;
		case R.id.wallet_tixian_layout:
			PublicUtil.showToast(WalletActivity.this, "暂未开通", false);

			break;
		case R.id.wallet_back:
			intent = new Intent(WalletActivity.this, MainActivity.class);
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
			Intent intent = new Intent(WalletActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}
}
