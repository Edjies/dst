package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * 修改密码
 * 
 * @author Administrator
 * 
 */
public class ChangePwdActivity extends BaseActivity implements OnClickListener {
	private FrameLayout topLayout;
	private EditText oldpwdEditText, newpwdEditText, newpwdEditText2;
	private Button okButton;
	private TextView backTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changepwd);
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
		topLayout = (FrameLayout) findViewById(R.id.changepwd_top_layout);
		setTopLayoutPadding(topLayout);
		oldpwdEditText = (EditText) findViewById(R.id.changepwd_oldedit);
		newpwdEditText = (EditText) findViewById(R.id.changepwd_newedit);
		newpwdEditText2 = (EditText) findViewById(R.id.changepwd_newedit2);
		okButton = (Button) findViewById(R.id.changepwd_okbutton);
		backTextView = (TextView) findViewById(R.id.changepwd_backtextview);
		okButton.setOnClickListener(this);
		backTextView.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.changepwd_okbutton:
			String phone = PublicUtil.getStorage_string(ChangePwdActivity.this,
					"phone", "0");

			if ("0".equals(phone)) {
				PublicUtil.showToast(ChangePwdActivity.this, "用户手机号码获取错误，无法操作",
						false);
				return;
			}
			String oldpwd = oldpwdEditText.getText().toString();
			String newpwd = newpwdEditText.getText().toString();
			String newpwd2 = newpwdEditText2.getText().toString();

			if (oldpwd == null || TextUtils.isEmpty(oldpwd)) {
				PublicUtil.showToast(ChangePwdActivity.this, "原密码不能为空", false);
				return;
			}
			if (newpwd == null || TextUtils.isEmpty(newpwd)) {
				PublicUtil.showToast(ChangePwdActivity.this, "请输入新的密码", false);
				return;
			}

			if (newpwd2 == null || TextUtils.isEmpty(newpwd2)) {
				PublicUtil.showToast(ChangePwdActivity.this, "请重复输入密码", false);
				return;
			}
			if (newpwd.length() < 6) {
				PublicUtil.showToast(ChangePwdActivity.this, "密码长度最少6位", false);
				return;
			}
			if (!newpwd.equals(newpwd2)) {
				PublicUtil
						.showToast(ChangePwdActivity.this, "两次输入密码不一致", false);
				newpwdEditText2.setText("");
				return;
			}

			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_MEMBER_ALTER_PWD);
			map.put("old_pwd", oldpwd);
			map.put("new_pwd", newpwd);
			map.put("ver", Constent.VER);

			AnsynHttpRequest.httpRequest(ChangePwdActivity.this,
					AnsynHttpRequest.GET, callBack,
					Constent.ID_MEMBER_ALTER_PWD, map, false, true, true);

			break;
		case R.id.changepwd_backtextview:
			finish();
			break;

		default:
			break;
		}

	}

	private JSONObject jsonObject = null;
	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@Override
		public void back(int backId, boolean isRequestSuccess,
				boolean isString, String data, JSONArray jsonArray) {
			// TODO Auto-generated method stub

			if (backId == Constent.ID_MEMBER_ALTER_PWD) {

				if (isRequestSuccess) {
					if (!isString) {

						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							// backArray = new JSONArray(backstr);
							// Log.d(TAG, backArray.length() + "");
							handler.sendEmptyMessage(success_httprequest);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = error_httprequest;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								handler.sendMessage(message);
							}
						}
					}
				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = error_httprequest;
						message.obj = data;
						handler.sendMessage(message);
					}

				}

			}

		}
	};

	private int error_httprequest = 0x7600;
	private int success_httprequest = 0x7601;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == error_httprequest) {
				if (msg.obj != null) {
					PublicUtil.showToast(ChangePwdActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest) {
				if (jsonObject != null) {
					try {

						if ("0".equals(jsonObject.getString("errcode"))) {
							PublicUtil.showToast(ChangePwdActivity.this,
									"修改密码成功", false);
							finish();

						} else {
							PublicUtil.showToast(ChangePwdActivity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {

								Intent intent = new Intent(
										ChangePwdActivity.this,
										LoginActivity.class);
								startActivity(intent);

							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PublicUtil.showToast(ChangePwdActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

}
