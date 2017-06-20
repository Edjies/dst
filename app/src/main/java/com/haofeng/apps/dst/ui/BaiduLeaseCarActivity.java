package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.model.LatLng;
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

/**
 * 用来显示租赁的汽车位置
 * 
 * @author Administrator
 * 
 */
public class BaiduLeaseCarActivity extends BaseActivity implements
		OnClickListener {
	private static String TAG = "BaiduLeaseCarActivity";

	public MapView mapView;
	private BaiduMap mBaiduMap;

	// 地图标记用到
	private double mylat, mylon;// 定位后自己的经纬度
	private double carlat, carlon;// 车辆经纬度
	private String phone = "-1", vin;
	private BitmapDescriptor carbd = null, mybd = null;
	private TextView backTextView, chepaihaoTextView, zujinTextView,
			moter_generatrix_currentTextView, update_datetimeTextView,
			speedTextView, directionTextView, data_sourceTextView,
			moter_speedTextView, moter_temperatureTextView,
			moter_controller_temperatureTextView,
			total_driving_mileageTextView, battery_package_socTextView,
			car_current_statusTextView, moter_currentTextView,
			moter_voltageTextView;
	private ImageView typeImageView;
	private boolean isfirstget = true;

	// private Timer timer = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baiduleasecar);
		((MyApplication) getApplication()).addActivity(this);
//		PushAgent.getInstance(this).onAppStart();
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {

		mapView = (MapView) findViewById(R.id.leasecar_baidumap);
		mapView.showScaleControl(false);// 比例尺
		mapView.showZoomControls(false);// 缩放按钮

		mBaiduMap = mapView.getMap();

		backTextView = (TextView) findViewById(R.id.leasecar_back);

		backTextView.setOnClickListener(this);

		chepaihaoTextView = (TextView) findViewById(R.id.leasecar_carinfor_chepaihao);
		zujinTextView = (TextView) findViewById(R.id.leasecar_carinfor_zujin);
		moter_generatrix_currentTextView = (TextView) findViewById(R.id.leasecar_carinfor_moter_generatrix_current);
		update_datetimeTextView = (TextView) findViewById(R.id.leasecar_carinfor_update_datetime);
		speedTextView = (TextView) findViewById(R.id.leasecar_carinfor_speed);
		directionTextView = (TextView) findViewById(R.id.leasecar_carinfor_direction);
		data_sourceTextView = (TextView) findViewById(R.id.leasecar_carinfor_data_source);
		moter_speedTextView = (TextView) findViewById(R.id.leasecar_carinfor_moter_speed);
		moter_temperatureTextView = (TextView) findViewById(R.id.leasecar_carinfor_moter_temperature);
		moter_controller_temperatureTextView = (TextView) findViewById(R.id.leasecar_carinfor_moter_controller_temperature);
		total_driving_mileageTextView = (TextView) findViewById(R.id.leasecar_carinfor_total_driving_mileage);
		battery_package_socTextView = (TextView) findViewById(R.id.leasecar_carinfor_battery_package_soc);
		car_current_statusTextView = (TextView) findViewById(R.id.leasecar_carinfor_car_current_status);
		moter_currentTextView = (TextView) findViewById(R.id.leasecar_carinfor_moter_current);
		moter_voltageTextView = (TextView) findViewById(R.id.leasecar_carinfor_moter_voltage);
		typeImageView = (ImageView) findViewById(R.id.leasecar_carinfor_typeimage);

		chepaihaoTextView.setText(getIntent().getStringExtra("chepai"));
		zujinTextView.setText("￥" + getIntent().getStringExtra("zujin"));

		if ("RC".equals(getIntent().getStringExtra("image"))) {
			typeImageView.setImageResource(R.drawable.list_ruichi);
		} else if ("BEV".equals(getIntent().getStringExtra("image"))) {
			typeImageView.setImageResource(R.drawable.list_beiqi);
		} else if ("BYD".equals(getIntent().getStringExtra("image"))) {
			typeImageView.setImageResource(R.drawable.list_byd);
		} else if ("DF".equals(getIntent().getStringExtra("image"))) {
			typeImageView.setImageResource(R.drawable.list_dongfeng);
		} else {
			typeImageView.setImageResource(R.drawable.list_qita);
		}
		mylat = Double.parseDouble(PublicUtil.getStorage_string(this, "mylat",
				"-1"));

		mylon = Double.parseDouble(PublicUtil.getStorage_string(this, "mylon",
				"-1"));
		if (mylat != -1.0 && mylon != -1.0) {// 画中心
			MapStatus ms = new MapStatus.Builder()
					.target(new LatLng(mylat, mylon)).zoom(9).build();
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
		}

		phone = PublicUtil.getStorage_string(BaiduLeaseCarActivity.this,
				"phone", "0");
		vin = getIntent().getStringExtra("vin");

		carbd = BitmapDescriptorFactory.fromResource(R.drawable.lent_car_icon);
		mybd = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		if ("0".equals(phone)) {
			PublicUtil.setStorage_string(BaiduLeaseCarActivity.this, "islogin",
					"0");

			PublicUtil.showToast(BaiduLeaseCarActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			getdata(true);
		}

	}

	public void getdata(boolean isshow) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_GETLEASECARINFOR);
		map.put("mobile", phone);
		map.put("vin", vin);
		map.put("ver", Constent.VER);

		AnsynHttpRequest.httpRequest(BaiduLeaseCarActivity.this,
				AnsynHttpRequest.POST, callBack, Constent.ID_GETLEASECARINFOR,
				map, false, isshow, true);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		MobclickAgent.onResume(this);
		mapView.onResume();

	}

	@Override
	public void onPause() {
		mapView.onPause();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onDestroy() {

		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
		if (carbd != null) {
			carbd.recycle();
		}

		if (mybd != null) {
			mybd.recycle();
		}

		// if (handler != null) {
		// handler.removeMessages(getcarinfor);
		// }
		//
		// if (timer != null) {
		// timer.purge();
		// timer.cancel();
		// timer = null;
		// }
	}

	OnMapLoadedCallback mapLoadedCallback = new OnMapLoadedCallback() {

		@Override
		public void onMapLoaded() {
			// TODO Auto-generated method stub
			LatLng carll = new LatLng(carlat, carlon);

			MarkerOptions options = new MarkerOptions().position(carll)
					.icon(carbd).zIndex(9).flat(true);
			// 掉下动画
			options.animateType(MarkerAnimateType.drop);
			mBaiduMap.addOverlay(options);

			if (mylat != -1.0 && mylon != -1.0) {
				LatLng myll = new LatLng(mylat, mylon);

				MarkerOptions my = new MarkerOptions().position(myll)
						.icon(mybd).zIndex(9).draggable(true);

				mBaiduMap.addOverlay(my);
			}
		}
	};

	// public void draw() {
	// mBaiduMap.clear();
	// LatLng carll = new LatLng(carlat, carlon);
	// MarkerOptions options = new MarkerOptions().position(carll).icon(carbd)
	// .zIndex(9).flat(true);
	// mBaiduMap.addOverlay(options);
	//
	// if (mylat != -1.0 && mylon != -1.0) {
	// LatLng myll = new LatLng(mylat, mylon);
	// MarkerOptions my = new MarkerOptions().position(myll).icon(mybd)
	// .zIndex(9).draggable(true);
	//
	// mBaiduMap.addOverlay(my);
	// }
	//
	// }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.leasecar_back:
			finish();
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
			case Constent.ID_GETLEASECARINFOR:
				PublicUtil.logDbug(TAG, isRequestSuccess + "", 0);
				PublicUtil.logDbug(TAG, backId + "", 0);
				PublicUtil.logDbug(TAG, isString + "", 0);
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

	private int httprequesterror = 0x3900;
	private int httprequestsuccess = 0x3901;
	// private int getcarinfor = 0x3902;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			// if (msg != null && msg.what == getcarinfor) {
			// getdata(false);
			// }

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(BaiduLeaseCarActivity.this,
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
								carlat = dataObject.getDouble("latitude_value");
								carlon = dataObject
										.getDouble("longitude_value");

								if (isfirstget) {
									isfirstget = false;
									// 画中心
									MapStatus ms = new MapStatus.Builder()
											.target(new LatLng(carlat, carlon))
											.zoom(11).build();
									mBaiduMap
											.animateMapStatus(MapStatusUpdateFactory
													.newMapStatus(ms));

									mBaiduMap
											.setOnMapLoadedCallback(mapLoadedCallback);

								} else {
									// draw();
								}

								car_current_statusTextView.setText(dataObject
										.getString("car_current_status"));
								moter_generatrix_currentTextView
										.setText(dataObject
												.getString("moter_generatrix_current"));
								update_datetimeTextView.setText("更新时间："
										+ PublicUtil.getTime(dataObject
												.getString("update_datetime")));
								speedTextView.setText(dataObject
										.getString("speed"));
								directionTextView.setText(dataObject
										.getString("direction"));
								data_sourceTextView.setText(dataObject
										.getString("data_source"));
								moter_speedTextView.setText(dataObject
										.getString("moter_speed"));
								moter_temperatureTextView.setText(dataObject
										.getString("moter_temperature"));
								moter_controller_temperatureTextView
										.setText(dataObject
												.getString("moter_controller_temperature"));
								total_driving_mileageTextView
										.setText(dataObject
												.getString("total_driving_mileage"));
								battery_package_socTextView.setText(dataObject
										.getString("battery_package_soc"));

								moter_currentTextView.setText(dataObject
										.getString("moter_current"));
								moter_voltageTextView.setText(dataObject
										.getString("moter_voltage"));

								// if ("行驶".equals(dataObject
								// .getString("car_current_status"))) {
								// timer = new Timer();
								// timer.schedule(new TimerTask() {
								//
								// @Override
								// public void run() {
								// // TODO Auto-generated method stub
								// handler.sendEmptyMessage(getcarinfor);
								// }
								// }, 0, 30 * 1000);
								//
								// }

							} else {
								PublicUtil.showToast(
										BaiduLeaseCarActivity.this, "解析数据错误",
										false);
							}

						} else {
							PublicUtil.showToast(BaiduLeaseCarActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(BaiduLeaseCarActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

}
