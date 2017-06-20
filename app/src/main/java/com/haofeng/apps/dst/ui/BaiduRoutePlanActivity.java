package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.utils.DrivingRouteOverlay;
import com.haofeng.apps.dst.utils.OverlayManager;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;

/**
 * 路线显示
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SdCardPath")
public class BaiduRoutePlanActivity extends BaseActivity implements
		BaiduMap.OnMapClickListener, OnGetRoutePlanResultListener {
	private final String TAG = "BaiduRoutePlanActivity";
	// private RouteLine route = null;
	private OverlayManager routeOverlay = null;
	private boolean useDefaultIcon = false;
	private LocationClient mLocClient;
	private double stlat, stlon, enlat, enlon;
	private String endAddr = "终点";

	// 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
	// 如果不处理touch事件，则无需继承，直接使用MapView即可
	private MapView mMapView = null; // 地图View
	private BaiduMap mBaidumap = null;
	// 搜索相关
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private ImageView daohangView;

	// private String type = "map";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routeplan);
		((MyApplication) getApplication()).addActivity(this);
//		PushAgent.getInstance(this).onAppStart();
		// CharSequence titleLable = "路线规划功能";
		// setTitle(titleLable);
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.map_routeplanmap);
		daohangView = (ImageView) findViewById(R.id.map_routeplan_daohang);
		mMapView.showZoomControls(false);// 缩放按钮
		mBaidumap = mMapView.getMap();
		// 地图点击事件处理
		mBaidumap.setOnMapClickListener(this);
		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);
		// LatLng p = new LatLng(b.getDouble("y"), b.getDouble("x"));
		// mMapView = new MapView(this,
		// new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(
		// p).build()));

		// mBaidumap.setMyLocationEnabled(true);// 开启定位图层
		// mBaidumap.setMyLocationConfigeration(new MyLocationConfiguration(
		// LocationMode.NORMAL, true, null));
		//
		// mLocClient = new LocationClient(BaiduRoutePlanActivity.this);
		// mLocClient.registerLocationListener(new LocationListenner());
		// LocationClientOption option = new LocationClientOption();
		// option.setOpenGps(true);// 打开gps
		// option.setCoorType("bd09ll");// 设置坐标类型
		// option.setScanSpan(99);
		// mLocClient.setLocOption(option);
		// mLocClient.start();
		daohangView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// openBaiduMap();

				if (isInstallPackage("com.baidu.BaiduMap")) {
					Log.e(TAG, "百度地图客户端已经安装");
					openBaiduMap();

				} else if (isInstallPackage("com.autonavi.minimap")) {
					Log.e(TAG, "没有安装百度地图客户端");
					openGaoDeMap();
				} else {
					PublicUtil.showToast(BaiduRoutePlanActivity.this,
							"未检测到第三方导航软件", false);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		Intent paramIntent = getIntent();
		stlat = paramIntent.getDoubleExtra("stlat", -1);
		stlon = paramIntent.getDoubleExtra("stlon", -1);
		enlat = paramIntent.getDoubleExtra("enlat", -1);
		enlon = paramIntent.getDoubleExtra("enlon", -1);
		// type = paramIntent.getStringExtra("type");
		if (stlat == -1.0) {
			stlat = Double.parseDouble(PublicUtil.getStorage_string(this,
					"mylat", "-1"));
		}

		if (stlon == -1.0) {
			stlon = Double.parseDouble(PublicUtil.getStorage_string(this,
					"mylon", "-1"));
		}

		Log.d("stlat", stlat + "");
		Log.d("stlon", stlon + "");
		if (stlat != -1.0 && stlon != -1.0) {
			LatLng ll = new LatLng(stlat, stlon);
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaidumap.animateMapStatus(u);
		}

		if (stlat == -1.0 || stlon == -1.0 || enlat == -1.0 || enlon == -1.0) {

			PublicUtil.showToast(this, "地图获取参数失败，无法规划线路，退出重试", false);
			return;
		} else {
			daohangView.setVisibility(View.VISIBLE);
			LatLng stLng = new LatLng(stlat, stlon);
			LatLng enLng = new LatLng(enlat, enlon);
			Process(stLng, enLng);
		}

	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
		MobclickAgent.onResume(this);

	}

	/**
	 * 发起路线规划搜索示例
	 */
	public void Process(LatLng stLng, LatLng enLng) {
		// 重置浏览节点的路线数据
		// route = null;
		mBaidumap.clear();

		PlanNode stNode = PlanNode.withLocation(stLng);
		PlanNode enNode = PlanNode.withLocation(enLng);
		mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(
				enNode));
	}

	/**
	 * 切换路线图标，刷新地图使其生效 注意： 起终点图标使用中心对齐.
	 */
	public void changeRouteIcon() {
		if (routeOverlay == null) {
			return;
		}
		if (useDefaultIcon) {
			Toast.makeText(this, "将使用系统起终点图标", Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(this, "将使用自定义起终点图标", Toast.LENGTH_SHORT).show();

		}
		useDefaultIcon = !useDefaultIcon;
		routeOverlay.removeFromMap();
		routeOverlay.addToMap();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(BaiduRoutePlanActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			// route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
			routeOverlay = overlay;
			mBaidumap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			overlay.addToMap();
			overlay.zoomToSpan();
		}
	}

	@Override
	public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

	}

	@Override
	public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

		public MyDrivingRouteOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
			}
			return null;
		}

		@Override
		public BitmapDescriptor getTerminalMarker() {
			if (useDefaultIcon) {
				return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
			}
			return null;
		}
	}

	@Override
	public void onMapClick(LatLng point) {
		mBaidumap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi poi) {
		return false;
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {

		if (mLocClient != null) {
			// 退出时销毁定位
			mLocClient.stop();
		}

		// 关闭定位图层
		mBaidumap.setMyLocationEnabled(false);
		mSearch.destroy();
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub

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

	// /**
	// * 开始导航
	// *
	// */
	//
	// public void startNavi() {
	//
	// LatLng startLatLng = new LatLng(stlat, stlon);
	// LatLng endLatLng = new LatLng(enlat, enlon);
	// // 构建 导航参数
	// NaviParaOption para = new NaviParaOption();
	// para.startName("起点");
	// para.startPoint(startLatLng);
	// para.endName("终点");
	// para.endPoint(endLatLng);
	//
	// try {
	//
	// BaiduMapNavigation.openBaiduMapNavi(para, this);
	//
	// } catch (Exception e) {
	//
	// }
	// }

	/**
	 * 打开百度地图导航，用intent方式
	 * 
	 */
	@SuppressWarnings({ "deprecation" })
	private void openBaiduMap() {

		try {
			String intentString = "intent://map/direction?origin=name:起点|latlng:"
					+ stlat
					+ ","
					+ stlon
					+ "&destination=name:"
					+ endAddr
					+ "|latlng:"
					+ enlat
					+ ","
					+ enlon
					+ "&mode=driving&src=地上铁公司|地上铁#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
			Log.e(TAG, intentString);
			Intent intent = Intent.getIntent(intentString);
			startActivity(intent); // 启动调用

		} catch (Exception e) {
			e.printStackTrace();
			PublicUtil
					.showToast(BaiduRoutePlanActivity.this, "打开百度导航失败", false);
		}
	}

	@SuppressWarnings("deprecation")
	private void openGaoDeMap() {
		try {
			double[] gd_lat_lon = bdToGaoDe(enlat, enlon);
			String intentString = "androidamap://navi?sourceApplication=地上铁&lat="
					+ gd_lat_lon[1]
					+ "&lon="
					+ gd_lat_lon[0]
					+ "&dev=0&style=2";

			Log.e(TAG, intentString);
			Intent intent = Intent.getIntent(intentString);
			startActivity(intent); // 启动调用
		} catch (Exception e) {
			e.printStackTrace();
			PublicUtil
					.showToast(BaiduRoutePlanActivity.this, "打开高德导航失败", false);
		}
	}

	private double[] bdToGaoDe(double bd_lat, double bd_lon) {
		double[] gd_lat_lon = new double[2];
		double PI = 3.14159265358979324 * 3000.0 / 180.0;
		double x = bd_lon - 0.0065, y = bd_lat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
		double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
		gd_lat_lon[0] = z * Math.cos(theta);
		gd_lat_lon[1] = z * Math.sin(theta);
		return gd_lat_lon;
	}

	// private double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
	// double[] bd_lat_lon = new double[2];
	// double PI = 3.14159265358979324 * 3000.0 / 180.0;
	// double x = gd_lon, y = gd_lat;
	// double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
	// double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
	// bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
	// bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
	// return bd_lat_lon;
	// }

	private boolean isInstallPackage(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

}
