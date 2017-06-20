package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyPayActivity extends BaseActivity {
	private FrameLayout topLayout;
	private TextView subjectTextView, introduceTextView, priceTextView,
			backTextView;
	private String phone = null, moneynumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addActivity(this);
		setContentView(R.layout.activity_mypay);
		init();
	}

	public void init() {
		topLayout = (FrameLayout) findViewById(R.id.mypay_top_layout);
		setTopLayoutPadding(topLayout);
		Button payButton = (Button) findViewById(R.id.mypay_okbutton);
		payButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		backTextView = (TextView) findViewById(R.id.mypay_backtextview);
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		subjectTextView = (TextView) findViewById(R.id.mypay_product_subject);
		introduceTextView = (TextView) findViewById(R.id.mypay_product_introduce);
		priceTextView = (TextView) findViewById(R.id.mypay_product_price);
		moneynumber = getIntent().getStringExtra("money");
		priceTextView.setText(moneynumber);
		subjectTextView.setText(getIntent().getStringExtra("infor"));
		introduceTextView.setText(getIntent().getStringExtra("infor2"));
		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_CREATE_ORDER_PAY);
		map.put("pay_way", "alipay");
		map.put("ver", Constent.VER);
		map.put("order_no", getIntent().getStringExtra("order_no"));
		AnsynHttpRequest.httpRequest(MyPayActivity.this, AnsynHttpRequest.GET,
				callBack, Constent.ID_CREATE_ORDER_PAY, map, false, true, true);

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
			case Constent.ID_CREATE_ORDER_PAY:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(success_httprequest_order_pay);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = error_httprequest_order_pay;
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

	private int error_httprequest_order_pay = 0x7820;
	private int success_httprequest_order_pay = 0x7821;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == error_httprequest_order_pay) {
				if (msg.obj != null) {
					PublicUtil.showToast(MyPayActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest_order_pay) {
				if (jsonObject != null) {

					try {
						PublicUtil.showToast(MyPayActivity.this, jsonObject
								.get("msg").toString(), false);
						Intent intent = new Intent(MyPayActivity.this,
								CarRestlistActivity.class);
						startActivity(intent);
						finish();

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			}

		};
	};

}
