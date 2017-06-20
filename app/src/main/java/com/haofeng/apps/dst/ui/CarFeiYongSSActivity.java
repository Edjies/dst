package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.ui.view.WheelView;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarFeiYongSSActivity extends BaseActivity implements
		OnClickListener {
	private final String TAG = "CarFeiYongSSActivity";
	private FrameLayout topLayout;
	private TextView backView;
	private TextView car_leixingView;
	private LinearLayout car_leixingLayout;
	private LinearLayout qucheLayout, haicheLayout;
	private TextView quchetimeView, haichetimeView, qucheweekView,
			haicheweekView, qucheinforView, haicheinforView;
	private TextView car_timeView, car_feiyongView;
	private EditText car_lichengText;
	private TextView moneyView, moneyinforView;
	private TextView okView;
	private FrameLayout dateLayout;
	private TextView datecancelView, datetypeView, dateokView;
	private WheelView wheelPicker, wheelPicker2;
	private LinearLayout pickLayout, pickLayout2;

	private int year, month, day, hour, fen;
	private int qucheyear, quchemonth, qucheday, quchehour, quchefen;
	private int haicheyear, haichemonth, haicheday, haichehour, haichefen;
	private Calendar calendar = Calendar.getInstance();
	private int choosetimetype = 0;// 0 取车时间 1还车时间
	private List<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arrayList2 = new ArrayList<Map<String, Object>>();
	private List<Map<String, Object>> arrayList3 = new ArrayList<Map<String, Object>>();// 全部时间
	private String[] hours = { "00:00", "00:30", "01:00", "01:30", "02:00",
			"02:30", "03:00", "03:30", "04:00", "04:30", "05:00", "05:30",
			"06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00",
			"09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30",
			"13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00",
			"16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30",
			"20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00",
			"23:30" };

	private int dataindex = 0;// 检测日期滑动的标志

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_carfeiyongshisuan);
		((MyApplication) getApplication()).addActivity(this);
		// PushAgent.getInstance(this).onAppStart();
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

		topLayout = (FrameLayout) findViewById(R.id.carfeiyongss_top_layout);
		if (android.os.Build.VERSION.SDK_INT < 19) {
			topLayout.setPadding(0, 0, 0, 0);
		}

		backView = (TextView) findViewById(R.id.carfeiyongss_back);
		car_leixingView = (TextView) findViewById(R.id.carfeiyongss_car_leixing);
		car_leixingLayout = (LinearLayout) findViewById(R.id.carfeiyongss_car_leixing_layout);
		qucheLayout = (LinearLayout) findViewById(R.id.carfeiyongss_quche_layout);
		haicheLayout = (LinearLayout) findViewById(R.id.carfeiyongss_haiche_layout);
		quchetimeView = (TextView) findViewById(R.id.carfeiyongss_quche_time);
		haichetimeView = (TextView) findViewById(R.id.carfeiyongss_haiche_time);
		qucheweekView = (TextView) findViewById(R.id.carfeiyongss_quche_week);
		haicheweekView = (TextView) findViewById(R.id.carfeiyongss_haiche_week);
		qucheinforView = (TextView) findViewById(R.id.carfeiyongss_quche_infor);
		haicheinforView = (TextView) findViewById(R.id.carfeiyongss_haiche_infor);
		car_timeView = (TextView) findViewById(R.id.carfeiyongss_car_time);
		car_feiyongView = (TextView) findViewById(R.id.carfeiyongss_car_zuchefeiyong);
		car_lichengText = (EditText) findViewById(R.id.carfeiyongss_car_gongli);

		moneyView = (TextView) findViewById(R.id.carfeiyongss_infor_money);
		moneyinforView = (TextView) findViewById(R.id.carfeiyongss_infor_infor);
		okView = (TextView) findViewById(R.id.carfeiyongss_lijixuanche);

		dateLayout = (FrameLayout) findViewById(R.id.carfeiyongss_date_layout);
		datecancelView = (TextView) findViewById(R.id.carfeiyongss_date_cancel);
		datetypeView = (TextView) findViewById(R.id.carfeiyongss_date_type);
		dateokView = (TextView) findViewById(R.id.carfeiyongss_date_ok);
		pickLayout = (LinearLayout) findViewById(R.id.carfeiyongss_date_picklayout);
		pickLayout2 = (LinearLayout) findViewById(R.id.carfeiyongss_date_picklayout2);

		backView.setOnClickListener(this);
		car_leixingLayout.setOnClickListener(this);
		qucheLayout.setOnClickListener(this);
		haicheLayout.setOnClickListener(this);
		okView.setOnClickListener(this);
		datecancelView.setOnClickListener(this);
		dateokView.setOnClickListener(this);
		dateLayout.setOnClickListener(this);
		quchetimeView.setVisibility(View.GONE);
		haichetimeView.setVisibility(View.GONE);
		qucheweekView.setVisibility(View.GONE);
		haicheweekView.setVisibility(View.GONE);
		qucheinforView.setVisibility(View.VISIBLE);
		haicheinforView.setVisibility(View.VISIBLE);

		calendar.setTimeInMillis(System.currentTimeMillis());

		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		hour = calendar.get(Calendar.HOUR_OF_DAY);
		fen = calendar.get(Calendar.MINUTE);
		PublicUtil.logDbug(TAG, year + ":" + month + ":" + day + ":" + hour
				+ ":" + fen, 0);
		if (hour < 9) {
			hour = 9;
			fen = 0;
		}
		wheelPicker = new WheelView(CarFeiYongSSActivity.this);
		wheelPicker2 = new WheelView(CarFeiYongSSActivity.this);

		for (int i = 0; i < hours.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("infor", hours[i]);
			map.put("hour", hours[i].split(":")[0]);
			map.put("fen", hours[i].split(":")[1]);

			arrayList3.add(map);

		}

		wheelPicker.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
			@Override
			public void onSelected(int selectedIndex) {
				// TODO Auto-generated method stub
				// super.onSelected(selectedIndex);
				if (wheelPicker.getSeletedIndex() == 0) {
					wheelPicker2.setDate(arrayList2);
					pickLayout2.removeAllViews();
					pickLayout2.addView(wheelPicker2);
				} else if (wheelPicker.getSeletedIndex() != 0) {
					wheelPicker2.setDate(arrayList3);
					pickLayout2.removeAllViews();
					pickLayout2.addView(wheelPicker2);
				}

			}
		});

		// Map<String, String> map = new HashMap<String, String>();
		// map.put("act", Constent.ACT_CAR_DETAIL);
		// map.put("ver", Constent.VER);
		// map.put("car_no",
		// PublicUtil.toUTF(getIntent().getStringExtra("car_no")));
		//
		// AnsynHttpRequest.httpRequest(CarOrderMoneyActivity.this,
		// AnsynHttpRequest.GET, callBack, Constent.ID_CAR_DETAIL, map,
		// false, true, true);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.carfeiyongss_back:

			finish();
			break;

		case R.id.carfeiyongss_quche_layout:
			choosetimetype = 0;
			datetypeView.setText("取车时间");
			setDate();
			setTime();
			if (dateLayout.getVisibility() == View.GONE) {
				dateLayout.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.carfeiyongss_haiche_layout:

			if (qucheyear == 0 || quchemonth == 0 || qucheday == 0) {
				PublicUtil.showToast(CarFeiYongSSActivity.this, "请先选择取车时间",
						false);
			} else {
				choosetimetype = 1;
				datetypeView.setText("还车时间");
				setDate();
				setTime();
				if (dateLayout.getVisibility() == View.GONE) {
					dateLayout.setVisibility(View.VISIBLE);
				}
			}

			break;
		case R.id.carfeiyongss_car_leixing_layout:

			break;
		case R.id.carfeiyongss_date_layout:

			break;
		case R.id.carfeiyongss_date_cancel:
			if (dateLayout.getVisibility() == View.VISIBLE) {
				dateLayout.setVisibility(View.GONE);
			}

			break;

		case R.id.carfeiyongss_date_ok:

			int datecount = wheelPicker.getSeletedIndex();
			int timecount = wheelPicker2.getSeletedIndex();

			if (choosetimetype == 0) {
				qucheyear = (Integer) arrayList.get(datecount).get("year");
				quchemonth = (Integer) arrayList.get(datecount).get("month");
				qucheday = (Integer) arrayList.get(datecount).get("day");
				if (datecount == 0) {
					quchehour = Integer.parseInt((String) arrayList2.get(
							timecount).get("hour"));
					quchefen = Integer.parseInt((String) arrayList2.get(
							timecount).get("fen"));
				} else {
					quchehour = Integer.parseInt((String) arrayList3.get(
							timecount).get("hour"));
					quchefen = Integer.parseInt((String) arrayList3.get(
							timecount).get("fen"));
				}

				quchetimeView.setText(oneTo2(quchemonth) + "/"
						+ oneTo2(qucheday) + "  " + oneTo2(quchehour) + ":"
						+ oneTo2(quchefen));
				qucheweekView.setText((CharSequence) arrayList.get(datecount)
						.get("week"));
				qucheinforView.setVisibility(View.GONE);
				quchetimeView.setVisibility(View.VISIBLE);
				qucheweekView.setVisibility(View.VISIBLE);
				haicheinforView.setVisibility(View.VISIBLE);
				haichetimeView.setText("");
				haicheweekView.setText("");
				haichetimeView.setVisibility(View.GONE);
				haicheweekView.setVisibility(View.GONE);

				haicheyear = 0;
				haichemonth = 0;
				haicheday = 0;
				haichehour = 0;
				haichefen = 0;

			} else {

				haicheyear = (Integer) arrayList.get(datecount).get("year");
				haichemonth = (Integer) arrayList.get(datecount).get("month");
				haicheday = (Integer) arrayList.get(datecount).get("day");
				if (datecount == 0) {
					haichehour = Integer.parseInt((String) arrayList2.get(
							timecount).get("hour"));
					haichefen = Integer.parseInt((String) arrayList2.get(
							timecount).get("fen"));
				} else {
					haichehour = Integer.parseInt((String) arrayList3.get(
							timecount).get("hour"));
					haichefen = Integer.parseInt((String) arrayList3.get(
							timecount).get("fen"));
				}

				haichetimeView.setText(oneTo2(haichemonth) + "/"
						+ oneTo2(haicheday) + "  " + oneTo2(haichehour) + ":"
						+ oneTo2(haichefen));
				haicheweekView.setText((CharSequence) arrayList.get(datecount)
						.get("week"));
				haicheinforView.setVisibility(View.GONE);
				haichetimeView.setVisibility(View.VISIBLE);
				haicheweekView.setVisibility(View.VISIBLE);
			}

			dateLayout.setVisibility(View.GONE);

			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (dateLayout.getVisibility() == View.VISIBLE) {
				dateLayout.setVisibility(View.GONE);
				return true;
			} else {

				finish();
				return true;
			}

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

			if (backId == Constent.ID_CAR_DETAIL) {

				if (isRequestSuccess) {
					if (!isString) {

						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							// backArray = new JSONArray(backstr);
							// Log.d(TAG, backArray.length() + "");
							handler.sendEmptyMessage(success_http_getcardetail);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = error_http_getcardetail;
								message.obj = "服务器端返回数据解析错误，请退出后重试";
								handler.sendMessage(message);
							}
						}
					}
				} else {
					Message message = new Message();
					message.what = error_http_getcardetail;
					message.obj = data;
					handler.sendMessage(message);
				}

			}

		}
	};

	private int error_http_getcardetail = 0x5700;
	private int success_http_getcardetail = 0x5701;

	private int chageviewpage = 0x5702;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == error_http_getcardetail) {
				if (msg.obj != null) {
					PublicUtil.showToast(CarFeiYongSSActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_http_getcardetail) {
				if (jsonObject != null) {
					try {

						if ("0".equals(jsonObject.getString("errcode"))) {

							JSONObject dataObject = jsonObject
									.getJSONObject("data");

							if (dataObject != null) {
								moneyView.setText(dataObject
										.getString("hour_price") + "/时");
								// moneyView2.setText(dataObject
								// .getString("day_price") + "/天");
								// moneyView3.setText(dataObject
								// .getString("day_price") + "/月");
								// moneyinforView.setText("里程费用:起步价"
								// + dataObject.getString("lowest_price")
								// + "元("
								// + dataObject
								// .getString("lowest_mileage")
								// + "公里内)+"
								// + dataObject.getString("mileage_price")
								// + "元/公里");
								// carnameView.setText(dataObject
								// .getString("car_name"));
								// cartypeView2.setText("载重量:"
								// + dataObject.getString("dead_weight"));
								// cartypeView.setText(dataObject
								// .getString("car_type"));
								// carzuoweiView.setText("轴距:"
								// + dataObject.getString("wheelbase"));
								// carrongjiView.setText("货箱容积:"
								// + dataObject.getString("pay_cube"));
								// carxuhangView.setText("续航:"
								// + dataObject
								// .getString("endurance_mileage")
								// + "KM["
								// + dataObject.getString("dump_energy")
								// + "%]");
								// carchicunView.setText("车身尺寸:"
								// + dataObject.getString("car_size"));
								// caraddrView.setText("取车网点:"
								// + dataObject.getString("addr"));
								// baoxianmoneyView.setText("￥"
								// + dataObject.getString("safe_amount"));
								// zuyong_moneyView.setText(dataObject
								// .getString("hour_price") + "/天");
								// zuyong_moneyView2.setText(dataObject
								// .getString("hour_price")
								// + "/天;"
								// + dataObject.getString("day_price")
								// + "/月");
								Map<String, String> map = new HashMap<String, String>();
								map.put("image_path", Constent.URLHEAD_IMAGE
										+ dataObject.getString("car_img_url"));

							}

						} else if ("1002".equals(jsonObject
								.getString("errcode"))) {
							PublicUtil.showToast(CarFeiYongSSActivity.this,
									jsonObject.getString("msg"), false);
							Intent intent = new Intent(
									CarFeiYongSSActivity.this,
									LoginActivity.class);
							startActivity(intent);

						} else {
							PublicUtil.showToast(CarFeiYongSSActivity.this,
									jsonObject.getString("msg"), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(CarFeiYongSSActivity.this,
								"配置解析数据错误，请退出重试", false);
						e.printStackTrace();
					}

				}

			}
		};
	};

	/**
	 * 根据年 月 获取对应的月份 天数
	 * */
	private int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 根据年 月 日 获取星期几
	 * */
	private String getWeekByYearMonth(int year, int month, int day) {

		String str = "";

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, day);
		a.roll(Calendar.DAY_OF_WEEK, -1);
		int firstweek = a.get(Calendar.DAY_OF_WEEK);

		switch (firstweek) {

		case 1:
			str = "周一";
			break;
		case 2:
			str = "周二";
			break;
		case 3:
			str = "周三";
			break;
		case 4:
			str = "周四";
			break;
		case 5:
			str = "周五";
			break;
		case 6:
			str = "周六";
			break;
		case 7:
			str = "周日";
			break;

		default:
			break;
		}

		return str;
	}

	/**
	 * 设置还取车日期
	 * 
	 */
	private void setDate() {
		arrayList.clear();
		int nowmonth;
		int nowyear;
		int nowday;
		if (choosetimetype == 0) {// 取车
			nowyear = year;
			nowmonth = month;
			nowday = day;
			int alldays = getDaysByYearMonth(nowyear, nowmonth);

			for (int i = nowday; i <= alldays; i++) {
				Map<String, Object> map = new HashMap<String, Object>();

				if (i == day) {
					map.put("infor", nowmonth + "月" + i + "日        今天");
					map.put("week", "今天");
				} else {
					map.put("infor", nowmonth + "月" + i + "日        "
							+ getWeekByYearMonth(nowyear, nowmonth, i));
					map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
				}
				map.put("year", nowyear);
				map.put("month", nowmonth);
				map.put("day", i);

				arrayList.add(map);

			}

			nowmonth = nowmonth + 1;
			if (nowmonth > 12) {
				nowmonth = 1;
				nowyear = nowyear + 1;

			}

			int alldays2 = getDaysByYearMonth(nowyear, nowmonth);

			for (int i = 1; i <= alldays2; i++) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("infor", nowmonth + "月" + i + "日        "
						+ getWeekByYearMonth(nowyear, nowmonth, i));
				map.put("year", nowyear);
				map.put("month", nowmonth);
				map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
				map.put("day", i);
				arrayList.add(map);
			}

			nowmonth = nowmonth + 1;
			if (nowmonth > 12) {
				nowmonth = 1;
				nowyear = nowyear + 1;
			}
			int alldays3 = getDaysByYearMonth(nowyear, nowmonth);

			if ((alldays + alldays2 + alldays3) > 90) {
				alldays3 = 90 - alldays - alldays2;
			}

			for (int i = 1; i <= alldays3; i++) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("infor", nowmonth + "月" + i + "日        "
						+ getWeekByYearMonth(nowyear, nowmonth, i));
				map.put("year", nowyear);
				map.put("month", nowmonth);
				map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
				map.put("day", i);
				arrayList.add(map);
			}

			if ((alldays + alldays2 + alldays3) < 90) {
				nowmonth = nowmonth + 1;
				if (nowmonth > 12) {
					nowmonth = 1;
					nowyear = nowyear + 1;
				}
				int alldays4 = getDaysByYearMonth(nowyear, nowmonth);

				if ((alldays + alldays2 + alldays3 + alldays4) > 90) {
					alldays4 = 90 - alldays - alldays2 - alldays3;
				}
				for (int i = 1; i <= alldays4; i++) {

					Map<String, Object> map = new HashMap<String, Object>();

					map.put("infor", nowmonth + "月" + i + "日        "
							+ getWeekByYearMonth(nowyear, nowmonth, i));
					map.put("year", nowyear);
					map.put("month", nowmonth);
					map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
					map.put("day", i);
					arrayList.add(map);
				}

			}
		} else {// 还车

			nowyear = qucheyear;
			nowmonth = quchemonth;
			nowday = qucheday;

			int alldays = getDaysByYearMonth(nowyear, nowmonth);

			for (int i = nowday; i <= alldays; i++) {
				Map<String, Object> map = new HashMap<String, Object>();

				if (nowmonth == month && i == day) {
					map.put("infor", nowmonth + "月" + i + "日        今天");
					map.put("week", "今天");
				} else {
					map.put("infor", nowmonth + "月" + i + "日        "
							+ getWeekByYearMonth(nowyear, nowmonth, i));
					map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
				}
				map.put("year", nowyear);
				map.put("month", nowmonth);
				map.put("day", i);
				arrayList.add(map);

			}

			nowmonth = nowmonth + 1;
			if (nowmonth > 12) {
				nowmonth = 1;
				nowyear = nowyear + 1;

			}

			int alldays2 = getDaysByYearMonth(nowyear, nowmonth);

			for (int i = 1; i <= alldays2; i++) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("infor", nowmonth + "月" + i + "日        "
						+ getWeekByYearMonth(nowyear, nowmonth, i));
				map.put("year", nowyear);
				map.put("month", nowmonth);
				map.put("day", i);
				map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
				arrayList.add(map);
			}

			nowmonth = nowmonth + 1;
			if (nowmonth > 12) {
				nowmonth = 1;
				nowyear = nowyear + 1;
			}
			int alldays3 = getDaysByYearMonth(nowyear, nowmonth);

			if ((alldays + alldays2 + alldays3) > 90) {
				alldays3 = 90 - alldays - alldays2;
			}

			for (int i = 1; i <= alldays3; i++) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("infor", nowmonth + "月" + i + "日        "
						+ getWeekByYearMonth(nowyear, nowmonth, i));
				map.put("year", nowyear);
				map.put("month", nowmonth);
				map.put("day", i);
				map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
				arrayList.add(map);
			}

			if ((alldays + alldays2 + alldays3) < 90) {
				nowmonth = nowmonth + 1;
				if (nowmonth > 12) {
					nowmonth = 1;
					nowyear = nowyear + 1;
				}
				int alldays4 = getDaysByYearMonth(nowyear, nowmonth);

				if ((alldays + alldays2 + alldays3 + alldays4) > 90) {
					alldays4 = 90 - alldays - alldays2 - alldays3;
				}
				for (int i = 1; i <= alldays4; i++) {

					Map<String, Object> map = new HashMap<String, Object>();

					map.put("infor", nowmonth + "月" + i + "日        "
							+ getWeekByYearMonth(nowyear, nowmonth, i));
					map.put("year", nowyear);
					map.put("month", nowmonth);
					map.put("day", i);
					map.put("week", getWeekByYearMonth(nowyear, nowmonth, i));
					arrayList.add(map);
				}

			}

		}
		pickLayout.removeAllViews();
		wheelPicker.setDate(arrayList);
		pickLayout.addView(wheelPicker);

	}

	/**
	 * 设置还取车时间
	 * 
	 */
	private void setTime() {
		arrayList2.clear();
		int hourstart;
		if (choosetimetype == 0) {// 取车

			if (hour == 24) {
				hour = 0;
			}

			hourstart = hour * 2;
			if (fen < 30) {
				hourstart = hourstart + 1;
			} else {
				hourstart = hourstart + 2;
			}

			if (hourstart >= hours.length) {
				hourstart = 0;
			}

			for (int i = hourstart; i < hours.length; i++) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("infor", hours[i]);
				map.put("hour", hours[i].split(":")[0]);
				map.put("fen", hours[i].split(":")[1]);

				arrayList2.add(map);

			}

		} else {// 还车
			hourstart = quchehour * 2;
			if (quchefen == 30) {
				hourstart = hourstart + 1;
			}

			for (int i = hourstart; i < hours.length; i++) {

				Map<String, Object> map = new HashMap<String, Object>();

				map.put("infor", hours[i]);
				map.put("hour", hours[i].split(":")[0]);
				map.put("fen", hours[i].split(":")[1]);

				arrayList2.add(map);

			}

		}
		pickLayout2.removeAllViews();
		wheelPicker2.setDate(arrayList2);
		pickLayout2.addView(wheelPicker2);
	}

	/**
	 * 
	 * @param in
	 * @return
	 */

	private String oneTo2(int in) {
		String str;

		if (in < 10) {
			str = "0" + in;
		} else {
			str = String.valueOf(in);
		}

		return str;
	}
}
