package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "OrderActivity";

	private TextView dateTextView, startTextView, endTextView;
	private EditText markEditText;
	private Button okButton;
	private int year, month, day, hour, minute, starthour, startminute;
	private DatePickerDialog datePickerDialog;
	private TimePickerDialog timePickerDialog;
	private long times;
	private String type = "add", chargerid, contype, time_start, time_end,
			mark, caid, appointed_date;
	private TextView backTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
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
		times = new Date().getTime();
		dateTextView = (TextView) findViewById(R.id.order_datetextview);
		startTextView = (TextView) findViewById(R.id.order_starttextview);
		endTextView = (TextView) findViewById(R.id.order_endtextview);
		markEditText = (EditText) findViewById(R.id.order_markeditview);

		okButton = (Button) findViewById(R.id.order_okbutton);
		backTextView = (TextView) findViewById(R.id.order_back);

		dateTextView.setOnClickListener(this);
		startTextView.setOnClickListener(this);
		endTextView.setOnClickListener(this);
		okButton.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		Time time = new Time();
		time.setToNow();
		year = time.year;
		month = time.month;
		day = time.monthDay;
		hour = time.hour;
		minute = time.minute;

		type = getIntent().getStringExtra("type");
		chargerid = getIntent().getStringExtra("id");
		contype = getIntent().getStringExtra("connection_type");

		if ("update".equals(type)) {
			appointed_date = getIntent().getStringExtra("appointed_date");
			time_start = getIntent().getStringExtra("time_start");
			time_end = getIntent().getStringExtra("time_end");
			mark = getIntent().getStringExtra("mark");
			caid = getIntent().getStringExtra("caid");
			dateTextView.setText(appointed_date);
			startTextView.setText(time_start);
			endTextView.setText(time_end);
			markEditText.setText(mark);
			starthour = Integer.parseInt(time_start.split(":")[0]);
			startminute = Integer.parseInt(time_start.split(":")[1]);
		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.order_back:

			finish();

			break;
		case R.id.order_datetextview:

			datePickerDialog = new DatePickerDialog(OrderActivity.this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker arg0, int arg1,
								int arg2, int arg3) {
							// TODO Auto-generated method stub

							PublicUtil.logDbug(TAG, "arg1" + arg1, 0);// 年
							PublicUtil.logDbug(TAG, "arg2" + arg2, 0);// 月。需要arg2+1
							PublicUtil.logDbug(TAG, "arg3" + arg3, 0);// 日
							arg2 = arg2 + 1;

							dateTextView
									.setText(arg1 + "-" + arg2 + "-" + arg3);

						}
					}, year, month, day);
			datePickerDialog.getDatePicker().setMinDate(times);
			datePickerDialog.show();

			break;
		case R.id.order_starttextview:
			timePickerDialog = new TimePickerDialog(OrderActivity.this,
					new OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker arg0, int arg1,
								int arg2) {
							// TODO Auto-generated method stub
							PublicUtil.logDbug(TAG, "arg1" + arg1, 0);
							PublicUtil.logDbug(TAG, "arg2" + arg2, 0);

							if (arg1 < hour) {

								PublicUtil.showToast(OrderActivity.this,
										"预约时间不可早于当前时间", false);

							} else if (arg1 == hour) {
								if (arg2 < minute) {
									PublicUtil.showToast(OrderActivity.this,
											"预约时间不可早于当前时间", false);
								} else {
									starthour = arg1;
									startminute = arg2;
									startTextView.setText(arg1 + ":" + arg2);
								}

							} else {
								starthour = arg1;
								startminute = arg2;
								startTextView.setText(arg1 + ":" + arg2);
							}

						}
					}, hour, minute, true);
			timePickerDialog.show();

			break;
		case R.id.order_endtextview:

			if (starthour == 0 || startminute == 0) {
				PublicUtil.showToast(OrderActivity.this, "请先选择开始时间", false);
				return;
			}
			timePickerDialog = new TimePickerDialog(OrderActivity.this,
					new OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker arg0, int arg1,
								int arg2) {
							// TODO Auto-generated method stub
							PublicUtil.logDbug(TAG, "arg1" + arg1, 0);
							PublicUtil.logDbug(TAG, "arg2" + arg2, 0);

							if (arg1 < starthour) {

								PublicUtil.showToast(OrderActivity.this,
										"结束时间不可小于开始时间", false);

							} else if (arg1 == starthour) {
								if (arg2 <= startminute) {
									PublicUtil.showToast(OrderActivity.this,
											"结束时间不可小于开始时间", false);

								} else {
									endTextView.setText(arg1 + ":" + arg2);
								}

							} else {
								endTextView.setText(arg1 + ":" + arg2);
							}

						}
					}, starthour, startminute, true);
			timePickerDialog.show();

			break;
		case R.id.order_okbutton:

			String phone = PublicUtil.getStorage_string(OrderActivity.this,
					"phone", "-1");
			if ("-1".equals(phone)) {

				PublicUtil.showToast(OrderActivity.this, "无法获取用户手机号码，请退出重新登陆",
						false);
				return;

			}

			String date = dateTextView.getText().toString();
			String stime = startTextView.getText().toString();
			String etime = endTextView.getText().toString();

			if (date == null || TextUtils.isEmpty(date)) {

				PublicUtil.showToast(OrderActivity.this, "请选择预约日期", false);
				return;

			}
			if (stime == null || TextUtils.isEmpty(stime)) {

				PublicUtil.showToast(OrderActivity.this, "请选择预约开始时间", false);
				return;

			}
			if (etime == null || TextUtils.isEmpty(etime)) {

				PublicUtil.showToast(OrderActivity.this, "请选择预约结束时间", false);
				return;

			}
			mark = markEditText.getText().toString();

			Map<String, String> map = new HashMap<String, String>();
			map.put("chargerid", chargerid);// 电桩id
			map.put("mobile", phone);
			map.put("contype", contype);// 充电连接类型
			map.put("date", date);// 日期
			map.put("starttime", stime);// 开始时间
			map.put("endtime", etime);// 结束时间
			map.put("mark", mark);// 备注
			map.put("ver", Constent.VER);
			if ("update".equals(type)) {
				map.put("act", Constent.ACT_ORDER);
				map.put("caid", caid);// 该预约的id
			} else {
				map.put("act", Constent.ACT_ORDER);
			}

			AnsynHttpRequest.httpRequest(OrderActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_ORDER, map,
					false, true, true);

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
			switch (backId) {
			case Constent.ID_ORDER:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							if (handler != null) {
								handler.sendEmptyMessage(httprequestsuccess);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = httprequesterror;
								message.obj = "数据解析错误";
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

	private int httprequesterror = 0x7000;
	private int httprequestsuccess = 0x7001;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(OrderActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {

					try {
						if ("0".equals(jsonObject.get("error").toString())) {

							Intent intent = new Intent(OrderActivity.this,
									MyOrderActivity.class);
							startActivity(intent);
							finish();

						} else {
							PublicUtil.showToast(OrderActivity.this, jsonObject
									.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(OrderActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

}
