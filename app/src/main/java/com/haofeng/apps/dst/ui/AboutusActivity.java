package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AboutusActivity extends BaseActivity {
	private TextView aboutTextView, backTextView, yearTextView;
	private FrameLayout topLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
		addActivity(this);
		topLayout = (FrameLayout) findViewById(R.id.aboutus_top_layout);
		setTopLayoutPadding(topLayout);
		aboutTextView = (TextView) findViewById(R.id.aboutus_textview);
		backTextView = (TextView) findViewById(R.id.aboutus_back);
		yearTextView = (TextView) findViewById(R.id.aboutus_year);
		yearTextView.setText("DST@2016-2020");
		backTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
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


	private JSONObject jsonObject = null;
	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@Override
		public void back(int backId, boolean isRequestSuccess,
				boolean isString, String data, JSONArray jsonArray) {
			// TODO Auto-generated method stub

			if (backId == Constent.ID_ADDSUGGEST) {

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
					Message message = new Message();
					message.what = httprequesterror;
					message.obj = data;
					handler.sendMessage(message);
				}

			}

		}
	};
	private int httprequesterror = 0x1000;
	private int httprequestsuccess = 0x1001;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(AboutusActivity.this,
							msg.obj.toString(), false);
				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {
					try {
						PublicUtil.showToast(AboutusActivity.this, jsonObject
								.get("msg").toString(), false);

						if ("0".equals(jsonObject.get("error").toString())) {
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(AboutusActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}
		};
	};

}
