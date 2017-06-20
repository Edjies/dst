package com.haofeng.apps.dst.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationData;
import com.haofeng.apps.dst.utils.PublicUtil;

/**
 * 刚进入应用，进行一次定位
 * 
 * @author Administrator
 * 
 */
public class LocationService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// 定位相关
	private LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setIsNeedAddress(true);
		option.setCoorType("bd09ll");// 设置坐标类型
		option.setScanSpan(99);// ＜1000只定位一次
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		super.onDestroy();
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@SuppressLint("NewApi")
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null) {
				stopSelf();
				return;
			}

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			String mylat = String.valueOf(locData.latitude);
			String mylon = String.valueOf(locData.longitude);

			if (!"4.9E-324".equals(mylat) && !"4.9E-324".equals(mylon)) {
				PublicUtil.setStorage_string(getApplicationContext(), "mylat",
						mylat);
				PublicUtil.setStorage_string(getApplicationContext(), "mylon",
						mylon);
				PublicUtil.setStorage_string(getApplicationContext(), "mycity",
						location.getCity());

			}

			stopSelf();

		}

		@Override
		public void onConnectHotSpotMessage(String s, int i) {

		}

	}

}
