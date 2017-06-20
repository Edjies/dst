package com.haofeng.apps.dst.ui;

public class BaiduMapActivity extends BaseActivity {
	// implements OnClickListener,
	// OnMapLoadedCallback, OnMapTouchListener {
	// private static String TAG = "BaiduMapActivity";
	// private FrameLayout chargestationinforLayout, routplanLayout;
	// private TextView infor_distanceTextView, infor_siteTextView,
	// infor_nameTextView;
	// private TextView chargestatusTextView;
	// private ImageView chargestatusImageView;
	// private TextView setTextView, backTextView, stationView;
	// private FrameLayout mapLayout;
	// private PullToRefreshListView pullToRefreshListView;
	// private ListView stationListView;
	// private EditText searchEditText;
	// private TextView searchTextView;
	// private ImageView statinbydImageView, statingjImageView,
	// statintslImageView, statinqtImageView;
	// private ListView searchListView;
	// private TextView clearsearchTextView;
	// private LinearLayout searchLayout;
	// private LinearLayout shaixuanLayout, paixuLinearLayout;
	// private LinearLayout stationlistLayout;
	// private FrameLayout topinforLayout;
	// private LinearLayout mapviewLayout;
	//
	// public MapView mapView;
	// // 定位相关
	// private LocationClient mLocClient;
	// public MyLocationListenner myListener = new MyLocationListenner();
	// private LocationMode mCurrentMode;
	// private BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_geo);;// 定位显示的自己点图片
	// private BaiduMap mBaiduMap;
	// private boolean isFirstLoc = true;// 是否首次定位
	// private boolean isLoadLocMap = true;// 是否要加载定位地图
	//
	// // 地图标记用到
	// private double chargelat, chargelon;
	// private double mylat, mylon;// 定位后自己的经纬度
	// private String phone = "-1";
	// private ClusterManager<MyItem> mClusterManager;
	// private MapStatus ms;
	// private BitmapDescriptor mybd = null;
	//
	// private Bundle cs_nowInforBundle = null;// 用来保存电站信息
	// private float starzoom = 13;// 地图的初始缩放级别
	// private float nowzoom = 13;// 缩放地图 时候用到
	// private int nowdistance = 30;
	//
	// private String connecttype_map = "";// 地图模式获取的电桩接口类型
	// private String charge_pattern_map = "";// 地图模式获取的电桩充电类型
	// private String pole_status_map = "";// 地图模式获取的电桩状态
	//
	// private String connecttype_list = "";// 获取的电桩接口类型
	// private String charge_pattern_list = "";// 获取的电桩充电类型
	// private String pole_status_list = "";// 获取的电桩状态
	// private ProgressDialog dialog;
	// private int count = 0;// 是否顺序显示
	// private String[] searchkeys = null;
	// private String nowSearchkey = null;
	// private MKOfflineMap mOffline;
	// private int allPage = 1, nowPage = 1;// 列表时用到
	// private List<Map<String, String>> stationsDataList = new
	// ArrayList<Map<String, String>>();
	// private boolean hasGetList = false;// 是否已经获取过列表的数据
	// private boolean isDownFlush = false;// 是否是下拉刷新
	//
	// // private ArrayList<MKOLUpdateElement> localMapList = null;
	//
	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onCreate(savedInstanceState);
//	 setContentView(R.layout.tab_baidumap_view);
	// ((MyApplication) getApplication()).addActivity(this);
	// PushAgent.getInstance(this).onAppStart();
	// init();
	//
	// }
	//
	// /**
	// * 初始化
	// */
	// @SuppressLint("NewApi")
	// private void init() {
	// setTextView = (TextView) findViewById(R.id.map_settextview);
	// // setListView = (ListView) findViewById(R.id.map_setlistview);
	// backTextView = (TextView) findViewById(R.id.map_baiduback);
	// stationView = (TextView) findViewById(R.id.map_statinlistmeau);
	// mapLayout = (FrameLayout) findViewById(R.id.map_maplayout);
	// pullToRefreshListView = (PullToRefreshListView)
	// findViewById(R.id.map_statinlist);
	// searchEditText = (EditText) findViewById(R.id.map_searchinput);
	// searchTextView = (TextView) findViewById(R.id.map_searchok);
	// searchListView = (ListView) findViewById(R.id.map_searchlistview);
	// stationlistLayout = (LinearLayout)
	// findViewById(R.id.map_statinlist_layout);
	// routplanLayout = (FrameLayout)
	// findViewById(R.id.map_chargestatin_infor_routplanlayout);
	// topinforLayout = (FrameLayout)
	// findViewById(R.id.map_chargestatin_infor_topinforlayout);
	// shaixuanLayout = (LinearLayout)
	// findViewById(R.id.map_statinlist_shaixuanlayout);
	// paixuLinearLayout = (LinearLayout)
	// findViewById(R.id.map_statinlist_paixulayout);
	// clearsearchTextView = (TextView) findViewById(R.id.map_searchclear);
	// searchLayout = (LinearLayout) findViewById(R.id.map_searchlayout);
	// mapviewLayout = (LinearLayout) findViewById(R.id.map_mapviewlayout);
	//
	// searchTextView.setOnClickListener(this);
	// clearsearchTextView.setOnClickListener(this);
	// setTextView.setOnClickListener(this);
	// stationView.setOnClickListener(this);
	// backTextView.setOnClickListener(this);
	// // stationlistshaixuan.setOnClickListener(this);
	// // stationlistpaixu.setOnClickListener(this);
	// shaixuanLayout.setOnClickListener(this);
	// paixuLinearLayout.setOnClickListener(this);
	//
	// chargestationinforLayout = (FrameLayout)
	// findViewById(R.id.map_chargestatin_inforlayout);
	// infor_distanceTextView = (TextView)
	// findViewById(R.id.map_chargestatin_infor_distance);
	// infor_siteTextView = (TextView)
	// findViewById(R.id.map_chargestatin_infor_install_site);
	// infor_nameTextView = (TextView)
	// findViewById(R.id.map_chargestatin_infor_name);
	// chargestatusTextView = (TextView)
	// findViewById(R.id.map_chargestatin_infor_chargestatus);
	// chargestatusImageView = (ImageView)
	// findViewById(R.id.map_chargestatin_infor_chargestatus_image);
	//
	// statinbydImageView = (ImageView)
	// findViewById(R.id.map_chargestatin_infor_typeimage_byd);
	// statingjImageView = (ImageView)
	// findViewById(R.id.map_chargestatin_infor_typeimage_gj);
	// statintslImageView = (ImageView)
	// findViewById(R.id.map_chargestatin_infor_typeimage_tsl);
	// statinqtImageView = (ImageView)
	// findViewById(R.id.map_chargestatin_infor_typeimage_qt);
	// routplanLayout.setOnClickListener(this);
	// topinforLayout.setOnClickListener(this);
	// searchLayout.setVisibility(View.GONE);
	//
	// initStatinList();// 初始化列表模式
	//
	// searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
	//
	// @Override
	// public void onFocusChange(View arg0, boolean arg1) {
	// // TODO Auto-generated method stub
	//
	// if (arg1) {
	// String mapkeystrString = PublicUtil.getStorage_string(
	// BaiduMapActivity.this, "mapsearchkey", "-1");
	//
	// if (!"-1".equals(mapkeystrString)) {
	// if (mapkeystrString.contains("DST&DST")) {
	// searchkeys = mapkeystrString.split("DST&DST");
	// } else {
	// searchkeys = new String[] { mapkeystrString };
	// }
	//
	// }
	// String input = searchEditText.getText().toString();
	// if (input == null || TextUtils.isEmpty(input)) {
	// if (searchkeys != null && searchkeys.length > 0) {
	// showList(null, 0);
	// }
	// handler.sendEmptyMessageAtTime(getinforbykeyword,
	// SystemClock.uptimeMillis() + 300);
	// } else {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("act", Constent.ACT_GETMAPSEARCHKEYWORD);
	// map.put("keyword", input);
	// map.put("ver", Constent.VER);
	//
	// AnsynHttpRequest.httpRequest(BaiduMapActivity.this,
	// AnsynHttpRequest.POST, callBack,
	// Constent.ID_GETMAPSEARCHKEYWORD, map, false,
	// false, true);
	// }
	// }
	//
	// }
	// });
	// boolean hasofflinemap = false;
	// LatLng p = null;
	// String city = PublicUtil.getStorage_string(BaiduMapActivity.this,
	// "mycity", "");
	//
	// if (city != null || !TextUtils.isEmpty(city)) {
	// mOffline = new MKOfflineMap();
	// mOffline.init(mkOfflineMapListener);
	// ArrayList<MKOLSearchRecord> records = mOffline.searchCity(city);
	//
	// if (records != null && records.size() == 1) {
	// PublicUtil.logDbug(TAG, records.size() + "records", 0);
	// ArrayList<MKOLUpdateElement> localMapList = mOffline
	// .getAllUpdateInfo();
	//
	// if (localMapList != null && localMapList.size() > 0) {
	//
	// for (int i = 0; i < localMapList.size(); i++) {
	// PublicUtil.logDbug(TAG, TAG + localMapList.get(i).ratio
	// + "ratio", 0);
	// if (city.equals(localMapList.get(i).cityName)
	// && localMapList.get(i).ratio == 100) {
	// p = localMapList.get(i).geoPt;
	//
	// hasofflinemap = true;
	//
	// break;
	// }
	//
	// }
	//
	// }
	// }
	//
	// mOffline.destroy();
	//
	// }
	//
	// String latString = PublicUtil.getStorage_string(BaiduMapActivity.this,
	// "mylat", "4.9E-324");
	// String lonString = PublicUtil.getStorage_string(BaiduMapActivity.this,
	// "mylon", "4.9E-324");
	//
	// PublicUtil.logDbug(TAG, hasofflinemap + "hasofflinemap", 0);
	// if (hasofflinemap) {
	// isLoadLocMap = false;
	//
	// mylat = Double.parseDouble(latString);
	// mylon = Double.parseDouble(lonString);
	//
	// mapView = new MapView(BaiduMapActivity.this,
	// new BaiduMapOptions().mapStatus(new MapStatus.Builder()
	// .target(p).zoom(starzoom).build()));
	// mapView.setClickable(true);
	// mapviewLayout.addView(mapView);
	// mBaiduMap = mapView.getMap();
	//
	// // 地图界面重新定位
	// mLocClient = new LocationClient(BaiduMapActivity.this);
	// mLocClient.registerLocationListener(myListener);
	// LocationClientOption option = new LocationClientOption();
	// option.setOpenGps(true);// 打开gps
	// option.setIsNeedAddress(true);
	// option.setCoorType("bd09ll");// 设置坐标类型
	// option.setScanSpan(99);// ＜1000只定位一次
	// mLocClient.setLocOption(option);
	// mLocClient.start();
	//
	// } else {
	// isLoadLocMap = true;
	// mapView = (MapView) findViewById(R.id.map_baidumap);
	// mapView.setVisibility(View.VISIBLE);
	//
	// mBaiduMap = mapView.getMap();
	//
	// // if ("4.9E-324".equals(latString) || "4.9E-324".equals(lonString))
	// // {
	//
	// // 打开定位地图
	// mCurrentMode = LocationMode.NORMAL;
	// mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
	// mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
	// mCurrentMode, true, mCurrentMarker));
	//
	// mLocClient = new LocationClient(BaiduMapActivity.this);
	// mLocClient.registerLocationListener(myListener);
	// LocationClientOption option = new LocationClientOption();
	// option.setOpenGps(true);// 打开gps
	// option.setIsNeedAddress(true);
	// option.setCoorType("bd09ll");// 设置坐标类型
	// option.setScanSpan(99);// ＜1000只定位一次
	// mLocClient.setLocOption(option);
	// mLocClient.start();
	//
	// // } else {
	// // mylat = Double.parseDouble(latString);
	// // mylon = Double.parseDouble(lonString);
	// // hasLoc = true;
	// //
	// // }
	//
	// }
	//
	// // mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
	// // mBaiduMap.setOnMapLoadedCallback(this);
	// // // 定义点聚合管理类ClusterManager
	// // mClusterManager = new ClusterManager<MyItem>(this,
	// // mBaiduMap);
	//
	// mBaiduMap.setOnMapLoadedCallback(BaiduMapActivity.this);
	// mBaiduMap.setOnMapTouchListener(BaiduMapActivity.this);
	//
	// mClusterManager = new ClusterManager<MyItem>(BaiduMapActivity.this,
	// mBaiduMap);
	//
	// DisplayMetrics dm = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(dm);
	// mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
	// mScreenHeight = dm.heightPixels;
	//
	// // 下载离线地图或者检查离线地图是否有更新
	// Intent intent = new Intent(BaiduMapActivity.this,
	// MapOfflineService.class);
	// startService(intent);
	//
	// }
	//
	// private int mScreenWidth, mScreenHeight;
	//
	// @Override
	// public void onResume() {
	// // TODO Auto-generated method stub
	//
	// super.onResume();
	// MobclickAgent.onResume(this);
	// mapView.onResume();
	//
	// phone = PublicUtil.getStorage_string(BaiduMapActivity.this, "phone",
	// "0");
	//
	// if ("0".equals(phone)) {
	// PublicUtil.setStorage_string(BaiduMapActivity.this, "islogin", "0");
	// }
	// mapLayout.requestFocus();
	// mapLayout.setFocusable(true);
	//
	// }
	//
	// @Override
	// public void onPause() {
	// mapView.onPause();
	// super.onPause();
	// MobclickAgent.onPause(this);
	// if (dialog != null) {
	// dialog.dismiss();
	// }
	// }
	//
	// @Override
	// public void onDestroy() {
	//
	// if (handler != null && handler.hasMessages(getmarkpoints)) {
	// handler.removeMessages(getmarkpoints);
	// }
	// // 退出时销毁定位
	// if (mLocClient != null) {
	// mLocClient.stop();
	// }
	//
	// // 关闭定位图层
	// mBaiduMap.setMyLocationEnabled(false);
	// mapView.onDestroy();
	// mapView = null;
	// super.onDestroy();
	// if (mybd != null) {
	// mybd.recycle();
	// }
	// }
	//
	// /**
	// * 定位SDK监听函数
	// */
	// public class MyLocationListenner implements BDLocationListener {
	//
	// @SuppressLint("NewApi")
	// @Override
	// public void onReceiveLocation(BDLocation location) {
	// // map view 销毁后不在处理新接收的位置
	// if (location == null || mapView == null) {
	// return;
	// }
	//
	// PublicUtil.setStorage_string(BaiduMapActivity.this, "mycity",
	// location.getCity());
	// MyLocationData locData = new MyLocationData.Builder()
	// .accuracy(location.getRadius())
	// // 此处设置开发者获取到的方向信息，顺时针0-360
	// .direction(100).latitude(location.getLatitude())
	// .longitude(location.getLongitude()).build();
	//
	// String latString = String.valueOf(locData.latitude);
	// String lonString = String.valueOf(locData.longitude);
	//
	// if (!"4.9E-324".equals(latString) && !"4.9E-324".equals(lonString)) {
	//
	// mylat = locData.latitude;
	// mylon = locData.longitude;
	//
	// PublicUtil.setStorage_string(BaiduMapActivity.this, "mylat",
	// latString);
	// PublicUtil.setStorage_string(BaiduMapActivity.this, "mylon",
	// lonString);
	//
	// PublicUtil.logDbug("LocationService", location.getCity(), 0);
	// PublicUtil.setStorage_string(BaiduMapActivity.this, "mycity",
	// location.getCity());
	//
	// }
	//
	// if (isLoadLocMap) {
	// mBaiduMap.setMyLocationData(locData);
	//
	// if (isFirstLoc) {
	// isFirstLoc = false;
	// ms = new MapStatus.Builder()
	// .target(new LatLng(mylat, mylon)).zoom(starzoom)
	// .build();
	// mBaiduMap.animateMapStatus(MapStatusUpdateFactory
	// .newMapStatus(ms));
	//
	// }
	//
	// if ("4.9E-324".equals(String.valueOf(mylat))
	// || "4.9E-324".equals(String.valueOf(mylon))) {
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "定位异常，经纬度获取错误，请退出重试", false);
	// } else {
	//
	// handler.sendEmptyMessageAtTime(getmarkpoints,
	// SystemClock.uptimeMillis() + 4000);
	// }
	// }
	//
	// }
	//
	// public void onReceivePoi(BDLocation poiLocation) {
	// }
	// }
	//
	// /**
	// * 获取附近充电桩
	// *
	// * @param latitude
	// * 当前纬度
	// * @param longitude
	// * 当前经度
	// */
	// @SuppressLint("NewApi")
	// public void getMarkPoints(boolean isShowDialog) {
	//
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("act", Constent.ACT_FUJIN);
	// map.put("curlat", mylat + "");
	// map.put("curlng", mylon + "");
	// map.put("radius", nowdistance + "");
	// map.put("contype", connecttype_map);
	// map.put("charge_pattern", charge_pattern_map);
	// map.put("pole_status", pole_status_map);
	//
	// map.put("ver", Constent.VER);
	//
	// if (!"0".equals(phone) && !TextUtils.isEmpty(phone)) {
	// map.put("mobile", phone);
	// }
	//
	// AnsynHttpRequest.httpRequest(BaiduMapActivity.this,
	// AnsynHttpRequest.POST, callBack, Constent.ID_FUJIN, map, false,
	// isShowDialog, true);
	// }
	//
	// private JSONObject jsonObject = null;
	// /**
	// * http请求回调
	// */
	// private HttpRequestCallBack callBack = new HttpRequestCallBack() {
	//
	// @SuppressLint("NewApi")
	// @Override
	// public void back(int backId, boolean isRequestSuccess,
	// boolean isString, String data, JSONArray jsonArray) {
	// // TODO Auto-generated method stub
	// switch (backId) {
	//
	// case Constent.ID_FUJIN:
	// if (isRequestSuccess) {
	// if (!isString) {
	// try {
	// String backstr = jsonArray.getString(1);
	// jsonObject = new JSONObject(backstr);
	// handler.sendEmptyMessage(httprequestsuccess);
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// message.obj = "服务器端返回数据解析错误，请退出后重试";
	// handler.sendMessage(message);
	// }
	// }
	// }
	//
	// } else {
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// message.obj = data;
	// handler.sendMessage(message);
	// }
	// }
	// break;
	// case Constent.ID_GETMAPSEARCHKEYWORD:
	// if (isRequestSuccess) {
	// if (!isString) {
	// try {
	// String backstr = jsonArray.getString(1);
	// jsonObject = new JSONObject(backstr);
	// handler.sendEmptyMessage(httprequestsuccess_getsearchkeyword);
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// handler.sendMessage(message);
	// }
	// }
	// }
	//
	// } else {
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// message.obj = data;
	// handler.sendMessage(message);
	// }
	// }
	// break;
	//
	// case Constent.ID_GETSTATIONLIST:
	// if (isRequestSuccess) {
	// if (!isString) {
	// try {
	// String backstr = jsonArray.getString(1);
	// jsonObject = new JSONObject(backstr);
	// handler.sendEmptyMessage(httprequestsuccess_getstationlist);
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// message.obj = "服务器端返回数据解析错误，请退出后重试";
	// handler.sendMessage(message);
	// }
	// }
	// }
	//
	// } else {
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// message.obj = data;
	// handler.sendMessage(message);
	// }
	// }
	// break;
	// case Constent.ID_GETMAPSTATIONWITHKEYWORD:
	// if (isRequestSuccess) {
	// if (!isString) {
	// try {
	// String backstr = jsonArray.getString(1);
	// jsonObject = new JSONObject(backstr);
	// handler.sendEmptyMessage(httprequestsuccess_getstationwithkeyword);
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// message.obj = "服务器端返回数据解析错误，请退出后重试";
	// handler.sendMessage(message);
	// }
	// }
	// }
	//
	// } else {
	// if (handler != null) {
	// Message message = new Message();
	// message.what = httprequesterror;
	// message.obj = data;
	// handler.sendMessage(message);
	// }
	// }
	// break;
	//
	// default:
	// break;
	// }
	//
	// }
	// };
	//
	// private int httprequesterror = 0x3000;
	// private int httprequestsuccess = 0x3001;
	// private int getmarkpoints = 0x3002;
	// private int httprequestsuccess_getsearchkeyword = 0x3004;
	// private int getinforbykeyword = 0x3005;
	// private int httprequestsuccess_getstationwithkeyword = 0x3006;
	// private int httprequestsuccess_getstationlist = 0x3007;
	// @SuppressLint("HandlerLeak")
	// private Handler handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	//
	// if (msg != null && msg.what == getinforbykeyword) {
	// String input = searchEditText.getText().toString();
	// handler.sendEmptyMessageAtTime(getinforbykeyword,
	// SystemClock.uptimeMillis() + 300);
	//
	// if (input != null && !TextUtils.isEmpty(input)
	// && !input.equals(nowSearchkey)) {
	// nowSearchkey = input;
	//
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("act", Constent.ACT_GETMAPSEARCHKEYWORD);
	// map.put("keyword", input);
	// map.put("ver", Constent.VER);
	//
	// AnsynHttpRequest.httpRequest(BaiduMapActivity.this,
	// AnsynHttpRequest.POST, callBack,
	// Constent.ID_GETMAPSEARCHKEYWORD, map, false, false,
	// true);
	//
	// }
	// }
	//
	// if (msg != null && msg.what == getmarkpoints) {
	// getMarkPoints(true);
	// } else {
	//
	// if (msg != null && msg.what == httprequesterror) {
	// pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
	// if (msg.obj != null) {
	// PublicUtil.showToast(BaiduMapActivity.this,
	// msg.obj.toString(), false);
	//
	// }
	// } else if (msg != null && msg.what == httprequestsuccess) {
	// if (jsonObject != null) {
	//
	// try {
	// // JSONObject jsonObject =
	// // backArray.getJSONObject(0);
	// if ("0".equals(jsonObject.get("error").toString())) {
	// PublicUtil.logDbug(TAG, jsonObject.get("data")
	// + "", 0);
	//
	// JSONObject datajsonObject = (JSONObject) jsonObject
	// .get("data");
	// JSONArray array = datajsonObject
	// .getJSONArray("list");
	//
	// if (array != null && array.length() > 0) {
	// PublicUtil.logDbug(TAG,
	// datajsonObject.getString("radius"),
	// 0);
	// int distence = (int) Float
	// .parseFloat(datajsonObject
	// .getString("radius"));
	//
	// connecttype_map = datajsonObject
	// .getString("contype");
	// charge_pattern_map = datajsonObject
	// .getString("charge_pattern");
	// pole_status_map = datajsonObject
	// .getString("pole_status");
	//
	// if (distence > nowdistance) {
	// nowzoom = getZoom(distence);
	// nowdistance = distence;
	// PublicUtil
	// .logDbug(TAG, nowzoom + "", 0);
	// ms = new MapStatus.Builder().zoom(
	// nowzoom).build();
	// mBaiduMap
	// .animateMapStatus(MapStatusUpdateFactory
	// .newMapStatus(ms));
	// }
	//
	// mark(array);
	// } else {
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "附近暂无充电站信息", false);
	// }
	//
	// } else {
	// PublicUtil
	// .showToast(BaiduMapActivity.this,
	// jsonObject.get("msg")
	// .toString(), false);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "配置解析数据错误，请退出重试", false);
	// }
	//
	// }
	//
	// } else if (msg != null
	// && msg.what == httprequestsuccess_getsearchkeyword) {
	// if (jsonObject != null) {
	//
	// try {
	// // JSONObject jsonObject =
	// // backArray.getJSONObject(0);
	// if ("0".equals(jsonObject.get("error").toString())) {
	// PublicUtil.logDbug(TAG, jsonObject.get("data")
	// + "", 0);
	//
	// JSONArray jsonArray = jsonObject
	// .getJSONArray("data");
	// if (jsonArray != null) {
	// showList(jsonArray, 1);
	// }
	//
	// } else {
	// PublicUtil
	// .showToast(BaiduMapActivity.this,
	// jsonObject.get("msg")
	// .toString(), false);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "配置解析数据错误，请退出重试", false);
	// }
	//
	// }
	//
	// } else if (msg != null
	// && msg.what == httprequestsuccess_getstationwithkeyword) {
	// if (jsonObject != null) {
	//
	// try {
	// // JSONObject jsonObject =
	// // backArray.getJSONObject(0);
	// if ("0".equals(jsonObject.get("error").toString())) {
	// PublicUtil.logDbug(TAG, jsonObject.get("data")
	// + "", 0);
	//
	// JSONObject datajsonObject = (JSONObject) jsonObject
	// .get("data");
	// JSONArray array = datajsonObject
	// .getJSONArray("list");
	//
	// if (array != null && array.length() > 0) {
	// PublicUtil.logDbug(TAG,
	// datajsonObject.getString("radius"),
	// 0);
	// connecttype_map = datajsonObject
	// .getString("contype");
	// charge_pattern_map = datajsonObject
	// .getString("charge_pattern");
	// pole_status_map = datajsonObject
	// .getString("pole_status");
	// if (!TextUtils.isEmpty(datajsonObject
	// .getString("radius"))) {
	//
	// int distence = (int) Float
	// .parseFloat(datajsonObject
	// .getString("radius"));
	//
	// if (distence > nowdistance) {
	// nowzoom = getZoom(distence);
	// nowdistance = distence;
	//
	// }
	//
	// }
	//
	// ms = new MapStatus.Builder().zoom(nowzoom)
	// .build();
	// ms = new MapStatus.Builder()
	// .target(new LatLng(array
	// .getJSONObject(0)
	// .getDouble("cs_lat"), array
	// .getJSONObject(0)
	// .getDouble("cs_lng")))
	// .zoom(nowzoom).build();
	// mBaiduMap
	// .animateMapStatus(MapStatusUpdateFactory
	// .newMapStatus(ms));
	//
	// mark(array);
	// } else {
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "暂无该电站信息", false);
	// }
	//
	// } else {
	// PublicUtil
	// .showToast(BaiduMapActivity.this,
	// jsonObject.get("msg")
	// .toString(), false);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "配置解析数据错误，请退出重试", false);
	// }
	//
	// }
	//
	// } else if (msg != null
	// && msg.what == httprequestsuccess_getstationlist) {
	// pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
	// if (jsonObject != null) {
	//
	// try {
	// // JSONObject jsonObject =
	// // backArray.getJSONObject(0);
	// if ("0".equals(jsonObject.get("error").toString())) {
	// PublicUtil.logDbug(TAG, jsonObject.get("data")
	// + "", 0);
	//
	// JSONObject datajsonObject = (JSONObject) jsonObject
	// .get("data");
	//
	// if (datajsonObject.get("total").toString() != null) {
	// allPage = PublicUtil
	// .getAllPage(datajsonObject.get(
	// "total").toString());
	// }
	// JSONArray array = datajsonObject
	// .getJSONArray("list");
	//
	// if (array != null && array.length() > 0) {
	// connecttype_list = datajsonObject
	// .getString("contype");
	// charge_pattern_list = datajsonObject
	// .getString("charge_pattern");
	// pole_status_list = datajsonObject
	// .getString("pole_status");
	//
	// setDate(array);
	// } else {
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "附近暂无充电站信息", false);
	// }
	//
	// } else {
	// PublicUtil
	// .showToast(BaiduMapActivity.this,
	// jsonObject.get("msg")
	// .toString(), false);
	// }
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "配置解析数据错误，请退出重试", false);
	// }
	//
	// }
	//
	// }
	//
	// }
	//
	// };
	// };
	//
	// private Marker mMark = null;
	//
	// /**
	// * 标记自己位置
	// */
	// public void markself() {
	//
	// LatLng myll = new LatLng(mylat, mylon);
	// mybd = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
	// MarkerOptions options = new MarkerOptions().position(myll).icon(mybd)
	// .zIndex(9).flat(true);
	// mMark = (Marker) mBaiduMap.addOverlay(options);
	//
	// ms = new MapStatus.Builder().target(new LatLng(mylat, mylon))
	// .zoom(starzoom).build();
	// mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
	//
	// }
	//
	// /**
	// * 初始化画标签的对象
	// *
	// * @param jsonObject
	// */
	// public void mark(JSONArray array) {
	// mClusterManager.clearItems();
	// List<MyItem> items = new ArrayList<MyItem>();
	// JSONObject object;
	// // JSONArray jsonArray_charges;
	// BitmapDescriptor descriptor = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_gcoding);
	// BitmapDescriptor descriptor2 = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_gcoding_lianying);
	// BitmapDescriptor descriptor3 = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_gcoding_hezuo);
	// BitmapDescriptor descriptor4 = BitmapDescriptorFactory
	// .fromResource(R.drawable.icon_gcoding_kehu);
	// LatLng[] lats = new LatLng[array.length()];
	// // Map<String, JSONArray> charges_map;
	// try {
	//
	// Bundle inforBundle = null;
	// for (int i = 0; i < array.length(); i++) {
	//
	// object = array.getJSONObject(i);
	// lats[i] = new LatLng(object.getDouble("cs_lat"),
	// object.getDouble("cs_lng"));
	//
	// inforBundle = new Bundle();
	// inforBundle.putString("cs_id", object.getString("cs_id"));
	// inforBundle.putString("cs_code", object.getString("cs_code"));
	// inforBundle.putString("cs_name", object.getString("cs_name"));
	// inforBundle.putString("cs_type", object.getString("cs_type"));
	// inforBundle.putString("cs_lng", object.getString("cs_lng"));
	// inforBundle.putString("cs_lat", object.getString("cs_lat"));
	// inforBundle.putString("my_lat", String.valueOf(mylat));
	// inforBundle.putString("my_lng", String.valueOf(mylon));
	// inforBundle.putString("cs_address",
	// object.getString("cs_address"));
	//
	// inforBundle.putString("distance", object.getString("distance"));
	// inforBundle.putString("GB", "0");
	// inforBundle.putString("BYD", "0");
	// inforBundle.putString("TESLA", "0");
	// inforBundle.putString("QT", "0");
	//
	// if ("SELF_OPERATION".equals(object.getString("cs_type"))
	// || "JOINT_OPERATION"
	// .equals(object.getString("cs_type"))) {// 自营联营
	// JSONObject free_pole_numObject = object
	// .getJSONObject("free_pole_num");
	// inforBundle.putString("slow",
	// free_pole_numObject.getString("slow"));
	// inforBundle.putString("fast",
	// free_pole_numObject.getString("fast"));
	//
	// JSONArray connecttypeArray = object
	// .getJSONArray("connection_type");
	//
	// for (int j = 0; j < connecttypeArray.length(); j++) {
	//
	// if ("GB".equals(connecttypeArray.getString(j))) {
	// inforBundle.putString("GB", "1");
	// } else if ("BYD".equals(connecttypeArray.getString(j))) {
	// inforBundle.putString("BYD", "1");
	// } else if ("TESLA"
	// .equals(connecttypeArray.getString(j))) {
	// inforBundle.putString("TESLA", "1");
	// } else {
	// inforBundle.putString("QT", "1");
	// }
	// }
	// } else {//
	//
	// inforBundle.putString("slow",
	// object.getString("spots_slow_num"));
	// inforBundle.putString("fast",
	// object.getString("spots_fast_num"));
	//
	// if ("GB".equals(object.getString("spots_connection_type"))) {
	// inforBundle.putString("GB", "1");
	// } else if ("BYD".equals(object
	// .getString("spots_connection_type"))) {
	// inforBundle.putString("BYD", "1");
	// } else if ("TESLA".equals(object
	// .getString("spots_connection_type"))) {
	// inforBundle.putString("TESLA", "1");
	// } else {
	// inforBundle.putString("QT", "1");
	// }
	// }
	//
	// if ("JOINT_OPERATION".equals(object.getString("cs_type"))) {// 联营
	// items.add(new MyItem(lats[i], inforBundle, descriptor2));
	// } else if ("COOPERATION".equals(object.getString("cs_type"))) {// 合作
	// items.add(new MyItem(lats[i], inforBundle, descriptor3));
	// } else if ("CUSTOMER_SELF_USE".equals(object
	// .getString("cs_type"))) {// 客户自用
	// items.add(new MyItem(lats[i], inforBundle, descriptor4));
	// } else {// 自营
	// items.add(new MyItem(lats[i], inforBundle, descriptor));
	// }
	//
	// }
	//
	// mClusterManager.addItems(items);
	// mClusterManager.cluster();
	// // 设置地图监听，当地图状态发生改变时，进行点聚合运算
	// mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	// // TODO: handle exception
	// }
	//
	// mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
	// public boolean onMarkerClick(final Marker marker) {
	//
	// if (marker != null) {
	//
	// mBaiduMap.hideInfoWindow();
	// if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
	// chargestationinforLayout.setVisibility(View.GONE);
	// }
	// if (mMark != marker) {
	//
	// chargelat = marker.getPosition().latitude;
	// chargelon = marker.getPosition().longitude;
	// float zoom = mBaiduMap.getMapStatus().zoom;
	// ms = new MapStatus.Builder()
	// .target(new LatLng(
	// marker.getPosition().latitude, marker
	// .getPosition().longitude))
	// .zoom(zoom).build();
	// mBaiduMap.animateMapStatus(MapStatusUpdateFactory
	// .newMapStatus(ms));
	//
	// cs_nowInforBundle = marker.getExtraInfo();
	// if (cs_nowInforBundle != null) {
	//
	// // cs_id = cs_nowInforBundle.getString("cs_id");
	// String nameString = cs_nowInforBundle
	// .getString("cs_name");
	// if (nameString.length() > 8) {
	// nameString = nameString.substring(0, 8) + "..";
	// }
	// infor_nameTextView.setText(nameString);
	// infor_distanceTextView.setText(cs_nowInforBundle
	// .getString("distance") + "KM");
	//
	// infor_siteTextView.setText(cs_nowInforBundle
	// .getString("cs_address"));
	// if ("1".equals(cs_nowInforBundle.getString("GB"))) {
	// statingjImageView.setVisibility(View.VISIBLE);
	// } else {
	// statingjImageView.setVisibility(View.GONE);
	// }
	//
	// if ("1".equals(cs_nowInforBundle.getString("BYD"))) {
	// statinbydImageView.setVisibility(View.VISIBLE);
	// } else {
	// statinbydImageView.setVisibility(View.GONE);
	// }
	//
	// if ("1".equals(cs_nowInforBundle.getString("TESLA"))) {
	// statintslImageView.setVisibility(View.VISIBLE);
	// } else {
	// statintslImageView.setVisibility(View.GONE);
	// }
	//
	// if ("1".equals(cs_nowInforBundle.getString("QT"))) {
	// statinqtImageView.setVisibility(View.VISIBLE);
	// } else {
	// statinqtImageView.setVisibility(View.GONE);
	// }
	//
	// if ("SELF_OPERATION".equals(cs_nowInforBundle
	// .getString("cs_type"))
	// || "JOINT_OPERATION"
	// .equals(cs_nowInforBundle
	// .getString("cs_type"))) {// 自营联营
	// chargestatusImageView
	// .setVisibility(View.VISIBLE);
	// } else {
	// chargestatusImageView.setVisibility(View.GONE);
	//
	// }
	//
	// chargestatusTextView.setText("快充（"
	// + cs_nowInforBundle.getString("fast")
	// + "）  慢充（"
	// + cs_nowInforBundle.getString("slow") + "）");
	//
	// // View view = LayoutInflater.from(
	// // BaiduMapActivity.this).inflate(
	// // R.layout.view_baidumap_markview, null);
	// // TextView csCode = (TextView) view
	// // .findViewById(R.id.view_baidumap_markview_text);
	// // csCode.setText("充电站编号："+cs_nowInforBundle
	// // .getString("cs_code"));
	// //
	// // LatLng ll = new LatLng(chargelat, chargelon);
	// //
	// // mInfoWindow = new InfoWindow(view, ll, -50);
	// // mBaiduMap.showInfoWindow(mInfoWindow);
	//
	// chargestationinforLayout
	// .setVisibility(View.VISIBLE);
	//
	// } else {
	// PublicUtil.logDbug(TAG,
	// marker + "@@@@@@@@@@@@@@@@", 0);
	// zoom = (float) (zoom + 0.5);
	// ms = new MapStatus.Builder().zoom(zoom).build();
	// mBaiduMap.animateMapStatus(MapStatusUpdateFactory
	// .newMapStatus(ms));
	// }
	//
	// }
	// }
	//
	// return true;
	// }
	// });
	//
	// }
	//
	// /**
	// * 每个Marker点，包含Marker点坐标以及图标
	// */
	// public class MyItem implements ClusterItem {
	// private final LatLng mPosition;
	// private final Bundle bundle;
	// private final BitmapDescriptor descriptor;
	//
	// public MyItem(LatLng latLng, Bundle inforBundle,
	// BitmapDescriptor icondescriptor) {
	// mPosition = latLng;
	// bundle = inforBundle;
	// descriptor = icondescriptor;
	// }
	//
	// public LatLng getPosition() {
	// return mPosition;
	// }
	//
	// public Bundle getBundle() {
	// return bundle;
	// }
	//
	// public BitmapDescriptor getBitmapDescriptor() {
	// // return BitmapDescriptorFactory
	// // .fromResource(R.drawable.icon_gcoding);
	// return descriptor;
	// }
	// }
	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	//
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	//
	// if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
	// mBaiduMap.hideInfoWindow();
	// chargestationinforLayout.setVisibility(View.GONE);
	// return true;
	// } else if (stationlistLayout.getVisibility() == View.VISIBLE) {
	// stationlistLayout.setVisibility(View.GONE);
	// mapLayout.setVisibility(View.VISIBLE);
	// stationView.setBackgroundResource(R.drawable.list_icon);
	// return true;
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	//
	// } else {
	// return super.onKeyDown(keyCode, event);
	// }
	//
	// }
	//
	// @SuppressLint("NewApi")
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// Intent intent = null;
	// switch (arg0.getId()) {
	//
	// case R.id.map_settextview:
	// intent = new Intent(BaiduMapActivity.this,
	// BaiduMapConnetionSetActivity.class);
	// intent.putExtra("connecttype", connecttype_map);
	// intent.putExtra("charge_pattern", charge_pattern_map);
	// intent.putExtra("pole_status", pole_status_map);
	// intent.putExtra("distance", nowdistance + "");
	// intent.putExtra("type", "map");
	// startActivityForResult(intent, 1);
	//
	// // if (connectiontypeList.size() > 0) {
	// //
	// // if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
	// // chargestationinforLayout.setVisibility(View.GONE);
	// // mBaiduMap.hideInfoWindow();
	// //
	// // }
	// // showset();
	// //
	// // } else {
	// // PublicUtil.showToast(BaiduMapActivity.this, "车辆充电类型设置异常，返回重试",
	// // false);
	// // }
	//
	// break;
	// case R.id.map_baiduback:
	// intent = new Intent(BaiduMapActivity.this, MainActivity.class);
	// startActivity(intent);
	// finish();
	//
	// break;
	// case R.id.map_chargestatin_infor_topinforlayout:
	// intent = new Intent(BaiduMapActivity.this,
	// ChargeStationInforActivity.class);
	// intent.putExtras(cs_nowInforBundle);
	// startActivity(intent);
	//
	// break;
	// case R.id.map_statinlistmeau:
	//
	// if (mapLayout.getVisibility() == View.VISIBLE) {
	// mapLayout.setVisibility(View.GONE);
	// stationlistLayout.setVisibility(View.VISIBLE);
	// stationView.setBackgroundResource(R.drawable.map_icon);
	// if (!hasGetList) {// 首次进入
	// hasGetList = true;
	// stationsDataList.clear();
	// getData(true);
	// }
	//
	// } else {
	// stationlistLayout.setVisibility(View.GONE);
	// mapLayout.setVisibility(View.VISIBLE);
	// stationView.setBackgroundResource(R.drawable.list_icon);
	// }
	//
	// break;
	// case R.id.map_searchok:
	// mapLayout.requestFocus();
	// mapLayout.setFocusable(true);
	// InputMethodManager imm = (InputMethodManager)
	// getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(
	// searchEditText.getApplicationWindowToken(), 0);
	// String input = searchEditText.getText().toString();
	// if (input == null || TextUtils.isEmpty(input)) {
	// PublicUtil.showToast(BaiduMapActivity.this, "输入内容不能为空", false);
	// return;
	// } else {
	// if (handler.hasMessages(getinforbykeyword)) {
	// handler.removeMessages(getinforbykeyword);
	// nowSearchkey = null;
	//
	// }
	//
	// savekey(input);
	//
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("act", Constent.ACT_GETMAPSTATIONWITHKEYWORD);
	// map.put("keyword", input);
	// map.put("curlat", mylat + "");
	// map.put("curlng", mylon + "");
	// map.put("ver", Constent.VER);
	//
	// AnsynHttpRequest.httpRequest(BaiduMapActivity.this,
	// AnsynHttpRequest.POST, callBack,
	// Constent.ID_GETMAPSTATIONWITHKEYWORD, map, false, true,
	// true);
	// }
	//
	// break;
	// case R.id.map_chargestatin_infor_routplanlayout:
	// routPlan();
	// break;
	// case R.id.map_statinlist_shaixuanlayout:
	// intent = new Intent(BaiduMapActivity.this,
	// BaiduMapConnetionSetActivity.class);
	// intent.putExtra("connecttype", connecttype_list);
	// intent.putExtra("charge_pattern", charge_pattern_list);
	// intent.putExtra("pole_status", pole_status_list);
	// intent.putExtra("type", "list");
	// startActivityForResult(intent, 1);
	// break;
	// case R.id.map_statinlist_paixulayout:
	// paixun();
	// break;
	//
	// case R.id.map_searchclear:
	// PublicUtil.removeStorage_string(BaiduMapActivity.this,
	// "mapsearchkey");
	// keywordList.clear();
	// searchLayout.setVisibility(View.GONE);
	//
	// break;
	// default:
	// break;
	// }
	//
	// }
	//
	// @Override
	// public void onMapLoaded() {
	// // TODO Auto-generated method stub
	// // mapView.showZoomControls(false);// 缩放按钮
	//
	// mapView.showZoomControls(false);
	//
	// if (!isLoadLocMap) {
	// if ("4.9E-324".equals(String.valueOf(mylat))
	// || "4.9E-324".equals(String.valueOf(mylon))) {
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "定位异常，经纬度获取错误，请退出重试", false);
	// return;
	// }
	// markself();
	// getMarkPoints(true);
	// }
	//
	// }
	//
	// public void onTouch(MotionEvent arg0) {
	// // TODO Auto-generated method stub
	// keywordList.clear();
	// searchLayout.setVisibility(View.GONE);
	// mapLayout.requestFocus();
	// mapLayout.setFocusable(true);
	// InputMethodManager imm = (InputMethodManager)
	// getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(searchEditText.getApplicationWindowToken(),
	// 0);
	// if (mBaiduMap == null) {
	// return;
	// }
	// if (arg0.getAction() == MotionEvent.ACTION_UP) {
	// if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
	// chargestationinforLayout.setVisibility(View.GONE);
	// }
	//
	// mBaiduMap.hideInfoWindow();
	//
	// float zoom = mBaiduMap.getMapStatus().zoom;
	// PublicUtil.logDbug(TAG, "zoom" + zoom, 0);
	// PublicUtil.logDbug(TAG, "nowzoom" + nowzoom, 0);
	// if (zoom < nowzoom && ((nowzoom - zoom) >= 1)) {
	// nowzoom = zoom;
	// int distance = getDistance(nowzoom);
	// if (distance > nowdistance) {
	// nowdistance = distance;
	// if ("4.9E-324".equals(String.valueOf(mylat))
	// || "4.9E-324".equals(String.valueOf(mylon))) {
	// return;
	//
	// } else {
	// getMarkPoints(false);
	// }
	//
	// }
	//
	// } else {
	// if (mBaiduMap.getProjection() == null) {
	// return;
	// }
	// Point point = new Point(mScreenWidth / 2, mScreenHeight / 2);
	// LatLng latLng = mBaiduMap.getProjection().fromScreenLocation(
	// point);
	//
	// LatLng myLatLng = new LatLng(mylat, mylon);
	//
	// int distance = (int) (DistanceUtil
	// .getDistance(myLatLng, latLng) / 1000);
	// PublicUtil.logDbug(TAG, "distance" + distance, 0);
	// PublicUtil.logDbug(TAG, "nowdistance" + nowdistance, 0);
	// if (zoom >= 8) {
	// if (nowdistance < 3000 && distance > nowdistance) {
	// if (distance <= 30 && distance > 10) {
	// nowdistance = 30;
	// } else if (distance <= 120 && distance > 30) {
	// nowdistance = 120;
	// } else if (distance <= 500 && distance > 120) {
	// nowdistance = 500;
	// } else if (distance <= 3000 && distance > 500) {
	// nowdistance = 3000;
	// }
	//
	// if ("4.9E-324".equals(String.valueOf(mylat))
	// || "4.9E-324".equals(String.valueOf(mylon))) {
	// return;
	//
	// } else {
	// getMarkPoints(false);
	// }
	//
	// }
	//
	// }
	//
	// }
	//
	// }
	//
	// }
	//
	// /**
	// * 根据缩放等级获得比例尺
	// *
	// * @param zoom
	// * @return
	// */
	// public int getDistance(float zoom) {
	//
	// int radius = 10;
	// if (zoom >= (float) 12) {
	// radius = 30;
	// } else if (zoom >= (float) 10 && zoom < (float) 12) {
	// radius = 150;
	// } else if (zoom >= (float) 8 && zoom < (float) 10) {
	// radius = 500;
	// } else if (zoom < (float) 8) {
	// radius = 4000;
	// }
	//
	// return radius;
	// }
	//
	// /**
	// * 获得地图缩放等级
	// *
	// * @param distence
	// * @return
	// */
	// public int getZoom(int distence) {
	//
	// int zoom = 15;
	//
	// if (distence < 30) {
	// zoom = 13;
	// } else if (distence >= 30 && distence < 60) {
	// zoom = 11;
	// } else if (distence >= 60 && distence < 150) {
	// zoom = 10;
	// } else if (distence >= 150 && distence < 600) {
	// zoom = 8;
	// } else if (distence >= 600) {
	// zoom = 6;
	// }
	//
	// return zoom;
	// }
	//
	// private ChargeStationListAdapter stationListAdapter = null;
	//
	// /**
	// * 切换列表模式
	// */
	// @SuppressWarnings({ "rawtypes", "unchecked" })
	// private void initStatinList() {
	// View view = LayoutInflater.from(this).inflate(
	// R.layout.view_listview_emptyshow, null);
	// pullToRefreshListView.setEmptyView(view);
	// pullToRefreshListView.setMode(Mode.BOTH);
	// ILoadingLayout startLabels = pullToRefreshListView
	// .getLoadingLayoutProxy(true, false);
	// startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
	// startLabels.setRefreshingLabel("正在刷新...");// 刷新时
	// startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
	//
	// ILoadingLayout endLabels = pullToRefreshListView.getLoadingLayoutProxy(
	// false, true);
	// endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
	// endLabels.setRefreshingLabel("正在加载...");// 刷新时
	// endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
	// pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {
	//
	// @Override
	// public void onPullDownToRefresh(PullToRefreshBase refreshView) {
	// // TODO Auto-generated method stub
	//
	// // stationsDataList.clear();
	// // stationListAdapter.notifyDataSetChanged();//
	// // 这里下拉重新加载数据时，清楚原来数据的动作放在那？需要考虑
	//
	// pullToRefreshListView.postDelayed(new Runnable() {//
	// 如果获取数据太快，正在加载的提示不消失，所以延时1秒
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// isDownFlush = true;
	// nowPage = 1;
	//
	// pullToRefreshListView.postDelayed(
	// new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method
	// // stub
	// getData(false);
	// }
	// }, 1000);
	// }
	// }, 1000);
	//
	// }
	//
	// @Override
	// public void onPullUpToRefresh(PullToRefreshBase refreshView) {
	// // TODO Auto-generated method stub
	// nowPage++;
	// pullToRefreshListView.postDelayed(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	// if (nowPage <= allPage) {
	// getData(false);
	// } else {
	// PublicUtil.showToast(BaiduMapActivity.this,
	// "暂无更多充电站了", false);
	// pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
	// // 我们已经更新完成
	// }
	// }
	// }, 1000);
	//
	// }
	// });
	//
	// stationListView = pullToRefreshListView.getRefreshableView();
	//
	// stationListAdapter = new ChargeStationListAdapter(
	// BaiduMapActivity.this, xiangqingCallBack, routplanCallBack);
	//
	// stationListView.setAdapter(stationListAdapter);
	//
	// }
	//
	// /**
	// * 电站列表详情区域点击回调事件
	// */
	// private CallBack xiangqingCallBack = new CallBack() {
	//
	// @Override
	// public void click(View v) {
	// // TODO Auto-generated method stub
	//
	// if (v.getTag() != null) {
	//
	// int i = Integer.valueOf((String) v.getTag());
	//
	// if (count != 0) {// 考虑反序后
	// i = (count - i - 1);
	// }
	//
	// Map<String, String> map = stationsDataList.get(i);
	//
	// Intent intent = new Intent(BaiduMapActivity.this,
	// ChargeStationInforActivity.class);
	// intent.putExtra("cs_id", map.get("cs_id"));
	// intent.putExtra("cs_code", map.get("cs_code"));
	// intent.putExtra("cs_name", map.get("cs_name"));
	// intent.putExtra("cs_type", map.get("cs_type"));
	// intent.putExtra("cs_lat", map.get("cs_lat"));
	// intent.putExtra("cs_lng", map.get("cs_lng"));
	// intent.putExtra("my_lat", map.get("my_lat"));
	// intent.putExtra("my_lng", map.get("my_lng"));
	// intent.putExtra("cs_address", map.get("cs_address"));
	// intent.putExtra("distance", map.get("distance"));
	// intent.putExtra("GB", map.get("GB"));
	// intent.putExtra("BYD", map.get("BYD"));
	// intent.putExtra("TESLA", map.get("TESLA"));
	// intent.putExtra("QT", map.get("QT"));
	// intent.putExtra("slow", map.get("slow"));
	// intent.putExtra("fast", map.get("fast"));
	// startActivity(intent);
	// }
	//
	// }
	// };
	//
	// /**
	// * 电站列表详情区域点击回调事件
	// */
	// private CallBack routplanCallBack = new CallBack() {
	//
	// @Override
	// public void click(View v) {
	// // TODO Auto-generated method stub
	//
	// if (v.getTag() != null) {
	//
	// int i = Integer.valueOf((String) v.getTag());
	//
	// if (count != 0) {// 考虑反序后
	// i = (count - i - 1);
	// }
	//
	// Map<String, String> map = stationsDataList.get(i);
	//
	// chargelat = Float.parseFloat(map.get("cs_lat"));
	// chargelon = Float.parseFloat(map.get("cs_lng"));
	// routPlan();
	// }
	//
	// }
	// };
	//
	// /**
	// * 设置列表数据
	// *
	// * @param array
	// */
	// private void setDate(JSONArray array) {
	//
	// try {
	// if (isDownFlush) {
	// count = 0;
	// isDownFlush = false;
	// stationsDataList.clear();
	// }
	//
	// int nowcount = stationsDataList.size();
	// JSONObject object = null;
	// for (int i = 0; i < array.length(); i++) {
	// Map<String, String> map = new HashMap<String, String>();
	// object = array.getJSONObject(i);
	// map.put("i", String.valueOf(i + nowcount));
	// map.put("cs_id", object.getString("cs_id"));
	// map.put("cs_code", object.getString("cs_code"));
	// map.put("cs_name", object.getString("cs_name"));
	// map.put("cs_type", object.getString("cs_type"));
	// map.put("cs_lat", object.getString("cs_lat"));
	// map.put("cs_lng", object.getString("cs_lng"));
	// map.put("my_lat", String.valueOf(mylat));
	// map.put("my_lng", String.valueOf(mylon));
	// map.put("cs_address", object.getString("cs_address"));
	// map.put("distance", object.getString("distance"));
	//
	// map.put("GB", "0");
	// map.put("BYD", "0");
	// map.put("TESLA", "0");
	// map.put("QT", "0");
	//
	// if ("SELF_OPERATION".equals(object.getString("cs_type"))
	// || "JOINT_OPERATION"
	// .equals(object.getString("cs_type"))) {// 自营联营
	// JSONArray connecttypeArray = object
	// .getJSONArray("connection_type");
	//
	// for (int j = 0; j < connecttypeArray.length(); j++) {
	//
	// if ("GB".equals(connecttypeArray.getString(j))) {
	// map.put("GB", "1");
	// } else if ("BYD".equals(connecttypeArray.getString(j))) {
	// map.put("BYD", "1");
	// } else if ("TESLA"
	// .equals(connecttypeArray.getString(j))) {
	// map.put("TESLA", "1");
	// } else {
	// map.put("QT", "1");
	// }
	// }
	//
	// JSONObject free_pole_numObject = object
	// .getJSONObject("free_pole_num");
	// map.put("slow", free_pole_numObject.getString("slow"));
	// map.put("fast", free_pole_numObject.getString("fast"));
	// } else {//
	//
	// map.put("slow", object.getString("spots_slow_num"));
	// map.put("fast", object.getString("spots_fast_num"));
	//
	// if ("GB".equals(object.getString("spots_connection_type"))) {
	// map.put("GB", "1");
	// } else if ("BYD".equals(object
	// .getString("spots_connection_type"))) {
	// map.put("BYD", "1");
	// } else if ("TESLA".equals(object
	// .getString("spots_connection_type"))) {
	// map.put("TESLA", "1");
	// } else {
	// map.put("QT", "1");
	// }
	// }
	//
	// PublicUtil.logDbug(TAG, map.toString(), 0);
	// stationsDataList.add(map);
	// }
	// stationListAdapter.setData(stationsDataList);
	// stationListAdapter.notifyDataSetChanged();
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// /**
	// * 列表排序
	// */
	// private void paixun() {
	//
	// if (stationsDataList.size() > 0) {
	// Collections.reverse(stationsDataList);
	// if (count == 0) {
	// count = stationsDataList.size();
	// } else {
	// count = 0;
	// }
	// stationListAdapter.setData(stationsDataList);
	// stationListAdapter.notifyDataSetChanged();
	// }
	//
	// }
	//
	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// super.onActivityResult(requestCode, resultCode, data);
	//
	// if (resultCode == 1) {
	//
	// if (stationlistLayout.getVisibility() == View.VISIBLE) {
	// charge_pattern_list = data.getStringExtra("charge_pattern");
	// pole_status_list = data.getStringExtra("pole_status");
	// connecttype_list = data.getStringExtra("connecttype");
	// isDownFlush = true;
	// nowPage = 1;
	// // stationsDataList.clear();
	// // stationListAdapter.notifyDataSetChanged();
	// getData(true);
	// } else {
	// nowdistance = Integer.parseInt(data.getStringExtra("distance"));
	// charge_pattern_map = data.getStringExtra("charge_pattern");
	// pole_status_map = data.getStringExtra("pole_status");
	// connecttype_map = data.getStringExtra("connecttype");
	// if (mClusterManager != null) {
	// mClusterManager.clearItems();
	// mClusterManager.cluster();
	// }
	// ms = new MapStatus.Builder().target(new LatLng(mylat, mylon))
	// .zoom(starzoom).build();
	// mBaiduMap.animateMapStatus(MapStatusUpdateFactory
	// .newMapStatus(ms));
	// getMarkPoints(true);
	// }
	//
	// }
	// }
	//
	// private List<Map<String, String>> keywordList = new ArrayList<Map<String,
	// String>>();
	//
	// /**
	// * 用来展示搜索框下面的关键字列表
	// *
	// * @param jsonArray
	// */
	// private void showList(JSONArray jsonArray, int type) {
	// keywordList.clear();
	// try {
	//
	// if (type == 1) {// 网络获取
	// JSONObject jsonObject = null;
	// for (int i = 0; i < jsonArray.length(); i++) {
	// jsonObject = jsonArray.getJSONObject(i);
	//
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("cs_id", jsonObject.getString("cs_id"));
	// map.put("text", jsonObject.getString("text"));
	// keywordList.add(map);
	//
	// }
	// } else {// 本地数据
	//
	// for (int i = 0; i < searchkeys.length; i++) {
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("text", searchkeys[i]);
	// keywordList.add(map);
	// }
	//
	// }
	//
	// if (keywordList.size() > 0) {
	//
	// if (type == 1) {// 网络获取
	// clearsearchTextView.setVisibility(View.GONE);
	// } else {// 本地数据
	// clearsearchTextView.setVisibility(View.VISIBLE);
	// }
	// SimpleAdapter simplead = new SimpleAdapter(this, keywordList,
	// R.layout.listview_item_mapsearchkeyword,
	// new String[] { "text" },
	// new int[] { R.id.listview_item_mapsearchkeyword_text });
	//
	// searchListView.setAdapter(simplead);
	// searchLayout.setVisibility(View.VISIBLE);
	// searchListView
	// .setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0,
	// View arg1, int arg2, long arg3) {
	// // TODO Auto-generated method stub
	// if (handler.hasMessages(getinforbykeyword)) {
	// handler.removeMessages(getinforbykeyword);
	// nowSearchkey = null;
	//
	// }
	// searchLayout.setVisibility(View.GONE);
	// searchEditText.setText(keywordList.get(arg2)
	// .get("text"));
	// // Log.d(TAG,
	// // keywordList.get(arg2).get("cs_id"));
	// keywordList.clear();
	//
	// // getMarkPoints(keywordList.get(arg2).get("cs_id"),
	// // true);
	//
	// }
	// });
	// } else {
	// searchLayout.setVisibility(View.GONE);
	// }
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// }
	//
	// }
	//
	// private String endAddr = "终点";
	//
	// /**
	// * 导航规划
	// */
	// public void routPlan() {
	//
	// if (isInstallPackage("com.baidu.BaiduMap")
	// && isInstallPackage("com.autonavi.minimap")) {
	// showMenu();
	//
	// } else if (isInstallPackage("com.baidu.BaiduMap")
	// && !isInstallPackage("com.autonavi.minimap")) {
	// dialog = PublicUtil.showDialog(BaiduMapActivity.this);
	// PublicUtil.showToast(BaiduMapActivity.this, "正在启动导航软件，请等待", false);
	// openBaiduMap();
	//
	// } else if (!isInstallPackage("com.baidu.BaiduMap")
	// && isInstallPackage("com.autonavi.minimap")) {
	// dialog = PublicUtil.showDialog(BaiduMapActivity.this);
	// PublicUtil.showToast(BaiduMapActivity.this, "正在启动导航软件，请等待", false);
	// openGaoDeMap();
	// } else {
	// PublicUtil.showToast(BaiduMapActivity.this, "未检测到第三方导航软件", false);
	// }
	// }
	//
	// @SuppressLint("SdCardPath")
	// private boolean isInstallPackage(String packageName) {
	//
	// return new File("/data/data/" + packageName).exists();
	// }
	//
	// /**
	// * 打开百度地图导航，用intent方式
	// *
	// */
	// @SuppressWarnings({ "deprecation" })
	// private void openBaiduMap() {
	//
	// try {
	// String intentString = "intent://map/direction?origin=name:起点|latlng:"
	// + mylat
	// + ","
	// + mylon
	// + "&destination=name:"
	// + endAddr
	// + "|latlng:"
	// + chargelat
	// + ","
	// + chargelon
	// +
	// "&mode=driving&src=地上铁公司|地上铁#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
	// PublicUtil.logDbug(TAG, intentString, 0);
	// Intent intent = Intent.getIntent(intentString);
	// startActivity(intent); // 启动调用
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// PublicUtil.showToast(BaiduMapActivity.this, "打开百度导航失败", false);
	// }
	// }
	//
	// @SuppressWarnings("deprecation")
	// private void openGaoDeMap() {
	// try {
	// double[] gd_lat_lon = bdToGaoDe(chargelat, chargelon);
	// String intentString = "androidamap://navi?sourceApplication=地上铁&lat="
	// + gd_lat_lon[1]
	// + "&lon="
	// + gd_lat_lon[0]
	// + "&dev=0&style=2";
	//
	// PublicUtil.logDbug(TAG, intentString, 0);
	// Intent intent = Intent.getIntent(intentString);
	// startActivity(intent); // 启动调用
	// } catch (Exception e) {
	// e.printStackTrace();
	// PublicUtil.showToast(BaiduMapActivity.this, "打开高德导航失败", false);
	// }
	// }
	//
	// /**
	// * 百度坐标转高德
	// *
	// * @param bd_lat
	// * @param bd_lon
	// * @return
	// */
	// private double[] bdToGaoDe(double bd_lat, double bd_lon) {
	// double[] gd_lat_lon = new double[2];
	// double PI = 3.14159265358979324 * 3000.0 / 180.0;
	// double x = bd_lon - 0.0065, y = bd_lat - 0.006;
	// double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
	// double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
	// gd_lat_lon[0] = z * Math.cos(theta);
	// gd_lat_lon[1] = z * Math.sin(theta);
	// return gd_lat_lon;
	// }
	//
	// /**
	// * 显示导航选择分类
	// */
	// public void showMenu() {
	//
	// View view = LayoutInflater.from(this).inflate(
	// R.layout.view_routplan_dialog, null);
	// TextView baiduTextView, gaodeTextView, cancelTextView;
	// baiduTextView = (TextView) view
	// .findViewById(R.id.view_rounplan_dialog_baidu);
	// gaodeTextView = (TextView) view
	// .findViewById(R.id.view_rounplan_dialog_gaode);
	// cancelTextView = (TextView) view
	// .findViewById(R.id.view_rounplan_dialog_cancle);
	//
	// final AlertDialog alertDialog = new AlertDialog.Builder(
	// BaiduMapActivity.this).setView(view).show();
	//
	// alertDialog.setCancelable(true);
	// alertDialog.setCanceledOnTouchOutside(true);
	// baiduTextView.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// if (alertDialog != null) {
	// alertDialog.dismiss();
	// }
	// // TODO Auto-generated method stub
	// dialog = PublicUtil.showDialog(BaiduMapActivity.this);
	// PublicUtil.showToast(BaiduMapActivity.this, "正在启动导航软件，请等待",
	// false);
	// openBaiduMap();
	//
	// }
	// });
	// gaodeTextView.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// if (alertDialog != null) {
	// alertDialog.dismiss();
	// }
	// dialog = PublicUtil.showDialog(BaiduMapActivity.this);
	// PublicUtil.showToast(BaiduMapActivity.this, "正在启动导航软件，请等待",
	// false);
	// openGaoDeMap();
	// }
	// });
	// cancelTextView.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// // TODO Auto-generated method stub
	// if (alertDialog != null) {
	// alertDialog.dismiss();
	// }
	// }
	// });
	//
	// }
	//
	// /**
	// * 保存本地搜索记录
	// *
	// * @param input
	// */
	// private void savekey(String input) {
	//
	// String mapkeystrString = PublicUtil.getStorage_string(
	// BaiduMapActivity.this, "mapsearchkey", "-1");
	//
	// if (!"-1".equals(mapkeystrString)) {
	//
	// if (mapkeystrString.contains("DST&DST")) {
	// String[] strs = mapkeystrString.split("DST&DST");
	// boolean same = false;
	// if (strs.length < 5) {
	//
	// for (int i = 0; i < strs.length; i++) {
	//
	// if (input.equals(strs[i])) {
	// same = true;
	// }
	// }
	//
	// if (!same) {
	// PublicUtil.setStorage_string(BaiduMapActivity.this,
	// "mapsearchkey", input + "DST&DST"
	// + mapkeystrString);
	// }
	//
	// } else {
	// for (int i = 0; i < strs.length; i++) {
	//
	// if (input.equals(strs[i])) {
	// same = true;
	// }
	// }
	// if (!same) {
	// strs[0] = input;
	//
	// PublicUtil.setStorage_string(BaiduMapActivity.this,
	// "mapsearchkey", strs[0] + "DST&DST" + strs[1]
	// + "DST&DST" + strs[2] + "DST&DST"
	// + strs[3] + "DST&DST" + strs[4]);
	// }
	// }
	//
	// } else {
	// if (!input.equals(mapkeystrString)) {
	// PublicUtil
	// .setStorage_string(BaiduMapActivity.this,
	// "mapsearchkey", input + "DST&DST"
	// + mapkeystrString);
	// }
	//
	// }
	//
	// } else {
	// PublicUtil.setStorage_string(BaiduMapActivity.this, "mapsearchkey",
	// input);
	// }
	//
	// }
	//
	// private MKOfflineMapListener mkOfflineMapListener = new
	// MKOfflineMapListener() {
	//
	// @Override
	// public void onGetOfflineMapState(int arg0, int arg1) {
	// }
	// };
	//
	// /**
	// * 获取列表时电站数据
	// */
	// public void getData(boolean isShowDialog) {
	// if ("4.9E-324".equals(String.valueOf(mylat))
	// || "4.9E-324".equals(String.valueOf(mylon))) {
	// PublicUtil.showToast(BaiduMapActivity.this, "定位异常，无法获取数据，请退出重试",
	// false);
	// return;
	//
	// }
	//
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("act", Constent.ACT_GETSTATIONLIST);
	// map.put("ver", Constent.VER);
	// map.put("rows", PublicUtil.PAGESIZE);
	// map.put("page", String.valueOf(nowPage));
	// map.put("curlat", mylat + "");
	// map.put("curlng", mylon + "");
	// map.put("contype", connecttype_list);
	// map.put("charge_pattern", charge_pattern_list);
	// map.put("pole_status", pole_status_list);
	//
	// AnsynHttpRequest.httpRequest(BaiduMapActivity.this,
	// AnsynHttpRequest.POST, callBack, Constent.ID_GETSTATIONLIST,
	// map, false, isShowDialog, true);
	//
	// }
}
