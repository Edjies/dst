package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.AdderListAdapter;
import com.haofeng.apps.dst.adapter.ChargeStationListAdapter;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.clusterutil.clustering.ClusterItem;
import com.haofeng.apps.dst.utils.clusterutil.clustering.ClusterManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.haofeng.apps.dst.R.id.tab_charge_statinlistmeau;


/**
 * 寻找电桩
 */
public class SearchPowerActivity extends BaseActivity {
    private final int REQUESTCODE = 0x3020;
    private FrameLayout topLayout;
    private View view;
    private TextView routplan;
    private LinearLayout chargestationinforLayout;
    private TextView infor_siteTextView, infor_nameTextView;
    private TextView chargestatusTextView;
    private TextView setTextView, stationView;
    private FrameLayout mapLayout;
    private PullToRefreshListView pullToRefreshListView;
    private ListView stationListView;
    private EditText searchEditText;
    private TextView searchTextView;
    private ImageView statinbydImageView, statingjImageView, statintslImageView, statinqtImageView;
    private ListView searchListView;
    private TextView clearsearchTextView;
    private LinearLayout searchLayout;
    private LinearLayout shaixuanLayout;
    private FrameLayout dizhishaixuanLayout;
    private TextView dizhishaixuanView;
    private LinearLayout stationlistLayout;
    private LinearLayout topinforLayout;
    private LinearLayout mapviewLayout;
    private final double EARTH_RADIUS = 6378137.0;

    public MapView mapView;
    // 定位相关
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
    // 定位显示的自己点图片
    private BaiduMap mBaiduMap;
    private boolean isFirstLoc = true;// 是否首次定位

    // 地图标记用到
    private double chargelat, chargelon;
    private double mylat, mylon;// 定位后自己的经纬度
    private String mycity;      //定位后我的城市
    private double cityToLat, cityToLon; //地址编码获取的经纬度
    private String phone = "-1";
    private ClusterManager<MyItem> mClusterManager;
    private MapStatus ms;
    private BitmapDescriptor mybd = null;

    private Bundle cs_nowInforBundle = null;// 用来保存电站信息
    private float starzoom = 13;// 地图的初始缩放级别
    private float nowzoom = 13;// 缩放地图 时候用到
    private int nowdistance = 30;

    private String connecttype_map = "";// 地图模式获取的电桩接口类型
    private String charge_pattern_map = "";// 地图模式获取的电桩充电类型
    private String pole_status_map = "";// 地图模式获取的电桩状态

    private String connecttype_list = "";// 获取的电桩接口类型
    private String charge_pattern_list = "";// 获取的电桩充电类型
    private String pole_status_list = "";// 获取的电桩状态
    private ProgressDialog dialog;
    private int count = 0;// 是否顺序显示
    private String[] searchkeys = null;
    private String nowSearchkey = null;
    private MKOfflineMap mOffline;
    private int allPage = 1, nowPage = 1;// 列表时用到
    private List<Map<String, String>> stationsDataList = new ArrayList<Map<String, String>>();
    private boolean hasGetList = false;// 是否已经获取过列表的数据
    private boolean isDownFlush = false;// 是否是下拉刷新
    private ListView quyuListView;// 区域列表
    private TextView quyuView;// 区域地名显示
    private String city_name = "深圳", city_id = "77", aera_id = "";// 默认是深圳
    private TextView distanceTextView;
    private GeoCoder mSearch;
    private boolean isGetLocatioFromCity = false;
    private String selectCity;
    private boolean isHasChangedCity = false;
    private static final String TAG = "SearchPowerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        init();

    }

    private void init() {
        view = View.inflate(this, R.layout.tab_baidumap_view, null);
        setContentView(view);
        topLayout = (FrameLayout) view.findViewById(R.id.tab_charge_top_layout);
        setTopLayoutPadding(topLayout);
        setTextView = (TextView) view.findViewById(R.id.tab_charge_settextview);
        stationView = (TextView) view.findViewById(R.id.tab_charge_statinlistmeau);
        mapLayout = (FrameLayout) view.findViewById(R.id.tab_charge_maplayout);
        pullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.tab_charge_statinlist);
        searchEditText = (EditText) view.findViewById(R.id.tab_charge_searchinput);
        searchTextView = (TextView) view.findViewById(R.id.tab_charge_searchok);
        searchListView = (ListView) view.findViewById(R.id.tab_charge_searchlistview);
        stationlistLayout = (LinearLayout) view.findViewById(R.id.tab_charge_statinlist_layout);
        routplan = (TextView) view.findViewById(R.id.tab_charge_chargestatin_infor_routplan);
        topinforLayout = (LinearLayout) view.findViewById(R.id.tab_charge_chargestatin_infor_topinforlayout);
        shaixuanLayout = (LinearLayout) view.findViewById(R.id.tab_charge_statinlist_shaixuanlayout);
        dizhishaixuanLayout = (FrameLayout) view.findViewById(R.id.tab_charge_statinlist_dizhilayout);
        dizhishaixuanView = (TextView) view.findViewById(R.id.tab_charge_statinlist_dizhi);

        clearsearchTextView = (TextView) view.findViewById(R.id.tab_charge_searchclear);
        searchLayout = (LinearLayout) view.findViewById(R.id.tab_charge_searchlayout);
        mapviewLayout = (LinearLayout) view.findViewById(R.id.tab_charge_mapviewlayout);
        quyuListView = (ListView) view.findViewById(R.id.tab_charge_quyulistview);
        quyuView = (TextView) view.findViewById(R.id.tab_charge_quyu);

        searchTextView.setOnClickListener(onClickListener);
        clearsearchTextView.setOnClickListener(onClickListener);
        setTextView.setOnClickListener(onClickListener);
        stationView.setOnClickListener(onClickListener);
        shaixuanLayout.setOnClickListener(onClickListener);
        dizhishaixuanLayout.setOnClickListener(onClickListener);
        chargestationinforLayout = (LinearLayout) view.findViewById(R.id.tab_charge_chargestatin_inforlayout);
        infor_siteTextView = (TextView) view.findViewById(R.id.tab_charge_chargestatin_infor_install_site);
        infor_nameTextView = (TextView) view.findViewById(R.id.tab_charge_chargestatin_infor_name);
        chargestatusTextView = (TextView) view.findViewById(R.id.tab_charge_chargestatin_infor_chargestatus);

        statinbydImageView = (ImageView) view.findViewById(R.id.tab_charge_chargestatin_infor_typeimage_byd);
        statingjImageView = (ImageView) view.findViewById(R.id.tab_charge_chargestatin_infor_typeimage_gj);
        statintslImageView = (ImageView) view.findViewById(R.id.tab_charge_chargestatin_infor_typeimage_tsl);
        statinqtImageView = (ImageView) view.findViewById(R.id.tab_charge_chargestatin_infor_typeimage_qt);
        distanceTextView = (TextView) view.findViewById(R.id.view_map_item_info_distance);
        routplan.setOnClickListener(onClickListener);
        topinforLayout.setOnClickListener(onClickListener);
        searchLayout.setVisibility(View.GONE);

        initStatinList();//初始化电站详情列表
        //地理编码
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());

        city_id = PublicUtil.getStorage_string(this, "city_id", "77");
        city_name = PublicUtil.getStorage_string(this, "city_name", "深圳");
        dizhishaixuanView.setText(city_name);
        adapter = new AdderListAdapter(this);
        quyuListView.setAdapter(adapter);

        String latString = PublicUtil.getStorage_string(this, "mylat", "4.9E-324");
        String lonString = PublicUtil.getStorage_string(this, "mylon", "4.9E-324");
        mylat = Double.parseDouble(latString);
        mylon = Double.parseDouble(lonString);

        mapView = (MapView) view.findViewById(R.id.tab_charge_baidumap);
        mBaiduMap = mapView.getMap();
        mapView.showZoomControls(true);
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(false);
        // 打开定位地图
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap.setMyLocationEnabled(true);// 开启定位图层
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");// 设置坐标类型
        option.setScanSpan(1000);// ＜1000只定位一次
        mLocClient.setLocOption(option);
        mLocClient.start();
        quyuListView.setOnItemClickListener(new MyCityAreaOnItemClickListener());
        searchEditText.setOnFocusChangeListener(new MyOnFocusChangeListener());
        mBaiduMap.setOnMapLoadedCallback(mapLoadedCallback);
        mBaiduMap.setOnMapTouchListener(onMapTouchListener);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode, false, mCurrentMarker));
        mClusterManager = new ClusterManager<MyItem>(this, mBaiduMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        mapView.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && handler.hasMessages(getmarkpoints)) {
            handler.removeMessages(getmarkpoints);
        }
        // 退出时销毁定位
        if (mLocClient != null) {
            mLocClient.stop();
        }
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mSearch.destroy();
        mapView = null;
        if (mybd != null) {
            mybd.recycle();
        }
    }

    /**
     * 搜索充电桩监听
     */
    class MyOnFocusChangeListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View arg0, boolean arg1) {
            // TODO Auto-generated method stub
            if (arg1) {
                String mapkeystrString = PublicUtil.getStorage_string(SearchPowerActivity.this, "mapsearchkey", "-1");
                if (!"-1".equals(mapkeystrString)) {
                    if (mapkeystrString.contains("DST&DST")) {
                        searchkeys = mapkeystrString.split("DST&DST");
                    } else {
                        searchkeys = new String[]{mapkeystrString};
                    }
                }
                String input = searchEditText.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    if (searchkeys != null && searchkeys.length > 0) {
                        showList(null, 0);
                    }
                    handler.sendEmptyMessageAtTime(getinforbykeyword, SystemClock.uptimeMillis() + 300);
                }
            }

        }
    }

    /**
     * 城市区域选择
     */
    class MyCityAreaOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            AdderListAdapter.AddrViewHolder viewHolder = (AdderListAdapter.AddrViewHolder) arg1.getTag();
            String id = viewHolder.idView.getText().toString();
            String name = viewHolder.addrView.getText().toString();
            if (!TextUtils.isEmpty(id)) {
                quyuView.setText(name);
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    if (aera_id.equals(map.get("id"))) {
                        map.remove("ischoose");
                        map.put("ischoose", "0");
                        list.remove(i);
                        list.add(i, map);
                        break;
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    Map<String, String> map = list.get(i);
                    if (id.equals(map.get("id"))) {
                        map.remove("ischoose");
                        map.put("ischoose", "1");
                        list.remove(i);
                        list.add(i, map);
                        break;
                    }

                }
                adapter.setData(list);
                adapter.notifyDataSetChanged();
                aera_id = id;

                if ("1".equals(list.get(arg2).get("hascont"))) {
                    stationsDataList.clear();
                    getData();
                    isDownFlush = true;
                } else {
                    PublicUtil.showToast(SearchPowerActivity.this, "该区域暂无充电站", false);
                }

            }
        }
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @SuppressLint("NewApi")
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            if ("4.9E-324".equals(String.valueOf(mylat)) || "4.9E-324".equals(String.valueOf(mylon))) {
                PublicUtil.showToast(SearchPowerActivity.this, "定位异常，经纬度获取错误，请退出重试", false);
                return;
            }
            MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            String latString = String.valueOf(locData.latitude);
            String lonString = String.valueOf(locData.longitude);

            if (!"4.9E-324".equals(latString) && !"4.9E-324".equals(lonString)) {
                mylat = locData.latitude;
                mylon = locData.longitude;
                mycity = location.getCity();
                PublicUtil.setStorage_string(SearchPowerActivity.this, "mylat", latString);
                PublicUtil.setStorage_string(SearchPowerActivity.this, "mylon", lonString);
                PublicUtil.setStorage_string(SearchPowerActivity.this, "mycity", location.getCity());
            }
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                ms = new MapStatus.Builder().target(new LatLng(mylat, mylon)).zoom(starzoom).overlook(0).build();
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            }

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }

    /**
     * 获取附近充电桩
     */
    public void getMarkPoints(boolean isShowDialog) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_FUJIN);
        if (!isGetLocatioFromCity) {
            map.put("curlat", mylat + "");
            map.put("curlng", mylon + "");
        } else {//根据城市定位到点，如果城市是市中心
            isGetLocatioFromCity = false;
            Log.e(TAG, "mycity: " + mycity + "selectCity: " + selectCity);
            if (mycity.equals(selectCity + "市")) {
                map.put("curlat", mylat + "");
                map.put("curlng", mylon + "");
            } else {
                map.put("curlat", cityToLat + "");
                map.put("curlng", cityToLon + "");
            }
        }


        map.put("radius", nowdistance + "");
        map.put("contype", connecttype_map);
        map.put("charge_pattern", charge_pattern_map);
        map.put("pole_status", pole_status_map);
        map.put("ver", Constent.VER);

        if (!"0".equals(phone) && !TextUtils.isEmpty(phone)) {
            map.put("mobile", phone);
        }
        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.POST, callBack, Constent.ID_FUJIN, map, false,
                isShowDialog, true);
    }

    private JSONObject jsonObject = null;
    /**
     * http请求回调
     */
    private HttpRequestCallBack callBack = new HttpRequestCallBack() {

        @SuppressLint("NewApi")
        @Override
        public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
            // TODO Auto-generated method stub
            switch (backId) {
                case Constent.ID_FUJIN:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                Log.e(TAG, "back: "+backstr);
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
                case Constent.ID_GETMAPSEARCHKEYWORD:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(httprequestsuccess_getsearchkeyword);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                if (handler != null) {
                                    Message message = new Message();
                                    message.what = httprequesterror;
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

                case Constent.ID_GETSTATIONLIST:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(httprequestsuccess_getstationlist);
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

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
                case Constent.ID_GETMAPSTATIONWITHKEYWORD:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(httprequestsuccess_getstationwithkeyword);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

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
                case Constent.ID_REGION_INDEX:
                    if (isRequestSuccess) {
                        if (!isString) {
                            try {
                                String backstr = jsonArray.getString(1);
                                jsonObject = new JSONObject(backstr);
                                handler.sendEmptyMessage(success_http_getquyu);

                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();

                            }
                        }

                    } else {
                        if (handler != null) {
                            Message message = new Message();
                            message.what = error_http_getquyu;
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
    private int httprequesterror = 0x3000;
    private int httprequestsuccess = 0x3001;
    private int getmarkpoints = 0x3002;
    private int httprequestsuccess_getsearchkeyword = 0x3004;
    private int getinforbykeyword = 0x3005;
    private int httprequestsuccess_getstationwithkeyword = 0x3006;
    private int httprequestsuccess_getstationlist = 0x3007;
    private int success_http_getquyu = 0x3008;
    private int error_http_getquyu = 0x3009;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg != null && msg.what == getinforbykeyword) {
                String input = searchEditText.getText().toString();
                handler.sendEmptyMessageAtTime(getinforbykeyword, SystemClock.uptimeMillis() + 300);
                if (!TextUtils.isEmpty(input) && !input.equals(nowSearchkey)) {
                    nowSearchkey = input;
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("act", Constent.ACT_GETMAPSEARCHKEYWORD);
                    map.put("keyword", input);
                    map.put("ver", Constent.VER);
                    AnsynHttpRequest.httpRequest(SearchPowerActivity.this, AnsynHttpRequest.POST, callBack,
                            Constent.ID_GETMAPSEARCHKEYWORD, map, false, false, true);
                }
            } else if (msg != null && msg.what == getmarkpoints) {
                getMarkPoints(true);
            } else if (msg != null && msg.what == httprequesterror) {
                pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                if (msg.obj != null) {
                    PublicUtil.showToast(SearchPowerActivity.this, msg.obj.toString(), false);
                }
            } else if (msg != null && msg.what == httprequestsuccess) {   //获取附近电桩
                if (jsonObject != null) {
                    try {
                        if ("0".equals(jsonObject.get("error").toString())) {
                            PublicUtil.logDbug(TAG, jsonObject.get("data") + "", 0);

                            JSONObject datajsonObject = (JSONObject) jsonObject.get("data");
                            JSONArray array = datajsonObject.getJSONArray("list");

                            if (array != null && array.length() > 0) {
                                PublicUtil.logDbug(TAG, datajsonObject.getString("radius"), 0);
                                int distence = (int) Float.parseFloat(datajsonObject.getString("radius"));
                                Log.e(TAG, "distence: " + distence);
                                Log.e(TAG, "array: " + array.length());
                                connecttype_map = datajsonObject.getString("contype");
                                charge_pattern_map = datajsonObject.getString("charge_pattern");
                                pole_status_map = datajsonObject.getString("pole_status");
                                if (distence > nowdistance) {  //距离变大，减小缩放值
                                    nowzoom = getZoom(distence);
                                    nowdistance = distence;
                                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(nowzoom));
                                }
                                mark(array);
                            } else {
                                PublicUtil.showToast(SearchPowerActivity.this, "附近暂无充电站信息", false);
                            }

                        } else {
                            String msg1 = jsonObject.get("msg").toString();
                            PublicUtil.showToast(SearchPowerActivity.this, jsonObject.get("msg").toString(), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        PublicUtil.showToast(SearchPowerActivity.this, "配置解析数据错误，请退出重试", false);
                    }

                }

            } else if (msg != null && msg.what == httprequestsuccess_getsearchkeyword) {
                if (jsonObject != null) {

                    try {
                        if ("0".equals(jsonObject.get("error").toString())) {
                            PublicUtil.logDbug(TAG, jsonObject.get("data") + "", 0);

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray != null) {
                                showList(jsonArray, 1);
                            }

                        } else {
                            PublicUtil.showToast(SearchPowerActivity.this, jsonObject.get("msg").toString(), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        PublicUtil.showToast(SearchPowerActivity.this, "配置解析数据错误，请退出重试", false);
                    }

                }

            } else if (msg != null && msg.what == httprequestsuccess_getstationwithkeyword) {
                if (jsonObject != null) {

                    try {
                        if ("0".equals(jsonObject.get("error").toString())) {
                            PublicUtil.logDbug(TAG, jsonObject.get("data") + "", 0);

                            JSONObject datajsonObject = (JSONObject) jsonObject.get("data");
                            JSONArray array = datajsonObject.getJSONArray("list");

                            if (array != null && array.length() > 0) {
                                PublicUtil.logDbug(TAG, datajsonObject.getString("radius"), 0);
                                connecttype_map = datajsonObject.getString("contype");
                                charge_pattern_map = datajsonObject.getString("charge_pattern");
                                pole_status_map = datajsonObject.getString("pole_status");
                                if (!TextUtils.isEmpty(datajsonObject.getString("radius"))) {

                                    int distence = (int) Float.parseFloat(datajsonObject.getString("radius"));

                                    if (distence > nowdistance) {   //电桩距离
                                        nowzoom = getZoom(distence);
                                        nowdistance = distence;
                                    }
                                }
                                ms = new MapStatus.Builder()
                                        .target(new LatLng(array.getJSONObject(0).getDouble("cs_lat"),
                                                array.getJSONObject(0).getDouble("cs_lng")))
                                        .zoom(nowzoom).build();
                                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                                mark(array);
                            } else {
                                PublicUtil.showToast(SearchPowerActivity.this, "暂无该电站信息", false);
                            }

                        } else {
                            PublicUtil.showToast(SearchPowerActivity.this, jsonObject.get("msg").toString(), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }
                }

            } else if (msg != null && msg.what == httprequestsuccess_getstationlist) {
                pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                if (jsonObject != null) {
                    try {
                        if ("0".equals(jsonObject.get("error").toString())) {
                            JSONObject datajsonObject = (JSONObject) jsonObject.get("data");

                            if (datajsonObject.get("total").toString() != null) {
                                allPage = PublicUtil.getAllPage(datajsonObject.get("total").toString());
                            }
                            JSONArray array = datajsonObject.getJSONArray("list");

                            if (array != null && array.length() > 0) {
                                connecttype_list = datajsonObject.getString("contype");
                                charge_pattern_list = datajsonObject.getString("charge_pattern");
                                pole_status_list = datajsonObject.getString("pole_status");
                                setDate(array);
                            } else {

                                PublicUtil.showToast(SearchPowerActivity.this, "该区域暂无充电站", false);

                                for (int i = 0; i < list.size(); i++) {
                                    Map<String, String> map = list.get(i);
                                    if (aera_id.equals(map.get("id"))) {
                                        map.remove("hascont");
                                        map.put("hascont", "0");
                                        list.remove(i);
                                        list.add(i, map);
                                        break;

                                    }

                                }
                            }

                        } else if ("1".equals(jsonObject.get("error").toString())) {
                            PublicUtil.showToast(SearchPowerActivity.this, "该区域暂无充电站", false);
                            for (int i = 0; i < list.size(); i++) {
                                Map<String, String> map = list.get(i);
                                if (aera_id.equals(map.get("id"))) {
                                    map.remove("hascont");
                                    map.put("hascont", "0");
                                    list.remove(i);
                                    list.add(i, map);
                                    break;

                                }

                            }
                        } else {
                            PublicUtil.showToast(SearchPowerActivity.this, jsonObject.get("msg").toString(), false);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }

                }

            } else if (msg != null && msg.what == error_http_getquyu) {
                if (msg.obj != null) {
                    PublicUtil.showToast(SearchPowerActivity.this, msg.obj.toString(), false);

                }

            } else if (msg != null && msg.what == success_http_getquyu) {
                if (jsonObject != null) {

                    try {
                        // JSONObject jsonObject =
                        // backArray.getJSONObject(0);
                        if ("0".equals(jsonObject.getString("errcode"))) {

                            JSONArray dataArray = jsonObject.getJSONArray("data");

                            if (dataArray != null && dataArray.length() > 0) {
                                flushlist(dataArray);
                            }

                        } else {
                            PublicUtil.showToast(SearchPowerActivity.this, jsonObject.getString("msg"), false);
                            if ("1002".equals(jsonObject.getString("errcode"))) {

                                Intent intent = new Intent(SearchPowerActivity.this, LoginActivity.class);
                                startActivity(intent);

                            }
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();

                    }

                }

            }


        }
    };
    private Marker mMark = null;

    /**
     * 标记自己位置
     */
    public void markself() {

    }

    /**
     * 初始化画标签的对象
     */
    public void mark(JSONArray array) {
        mClusterManager.clearItems();
        List<MyItem> items = new ArrayList<>();
        JSONObject object;
        // JSONArray jsonArray_charges;
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.dianzhanditu_1_14);
        BitmapDescriptor descriptor2 = BitmapDescriptorFactory.fromResource(R.drawable.dianzhanditu_1_16);
        BitmapDescriptor descriptor3 = BitmapDescriptorFactory.fromResource(R.drawable.dianzhanditu_1_18);
        BitmapDescriptor descriptor4 = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding_kehu);
        LatLng[] lats = new LatLng[array.length()];
        // Map<String, JSONArray> charges_map;
        try {

            Bundle inforBundle = null;
            for (int i = 0; i < array.length(); i++) {

                object = array.getJSONObject(i);
                lats[i] = new LatLng(object.getDouble("cs_lat"), object.getDouble("cs_lng"));

                inforBundle = new Bundle();
                inforBundle.putString("cs_id", object.getString("cs_id"));
                inforBundle.putString("cs_code", object.getString("cs_code"));
                inforBundle.putString("cs_name", object.getString("cs_name"));
                inforBundle.putString("cs_type", object.getString("cs_type"));
                inforBundle.putString("cs_lng", object.getString("cs_lng"));
                inforBundle.putString("cs_lat", object.getString("cs_lat"));
                inforBundle.putString("my_lat", String.valueOf(mylat));
                inforBundle.putString("my_lng", String.valueOf(mylon));
                inforBundle.putString("cs_address", object.getString("cs_address"));

                inforBundle.putString("distance", object.getString("distance"));
                inforBundle.putString("GB", "0");
                inforBundle.putString("BYD", "0");
                inforBundle.putString("TESLA", "0");
                inforBundle.putString("QT", "0");

                if ("SELF_OPERATION".equals(object.getString("cs_type"))
                        || "JOINT_OPERATION".equals(object.getString("cs_type"))) {// 自营联营
                    JSONObject free_pole_numObject = object.getJSONObject("free_pole_num");  //空闲电桩数
                    inforBundle.putString("slow", free_pole_numObject.getString("slow"));
                    inforBundle.putString("fast", free_pole_numObject.getString("fast"));

                    JSONArray connecttypeArray = object.getJSONArray("connection_type");   //充电接口类型

                    for (int j = 0; j < connecttypeArray.length(); j++) {

                        if ("GB".equals(connecttypeArray.getString(j))) {
                            inforBundle.putString("GB", "1");
                        } else if ("BYD".equals(connecttypeArray.getString(j))) {
                            inforBundle.putString("BYD", "1");
                        } else if ("TESLA".equals(connecttypeArray.getString(j))) {
                            inforBundle.putString("TESLA", "1");
                        } else {
                            inforBundle.putString("QT", "1");
                        }
                    }
                } else {
                    inforBundle.putString("slow", object.getString("spots_slow_num"));
                    inforBundle.putString("fast", object.getString("spots_fast_num"));

                    if ("GB".equals(object.getString("spots_connection_type"))) {
                        inforBundle.putString("GB", "1");
                    } else if ("BYD".equals(object.getString("spots_connection_type"))) {
                        inforBundle.putString("BYD", "1");
                    } else if ("TESLA".equals(object.getString("spots_connection_type"))) {
                        inforBundle.putString("TESLA", "1");
                    } else {
                        inforBundle.putString("QT", "1");
                    }
                }


                if ("JOINT_OPERATION".equals(object.getString("cs_type"))) {// 联营
                    items.add(new MyItem(lats[i], inforBundle, descriptor2));
                } else if ("COOPERATION".equals(object.getString("cs_type"))) {// 合作
                    items.add(new MyItem(lats[i], inforBundle, descriptor3));
                } else if ("CUSTOMER_SELF_USE".equals(object.getString("cs_type"))) {// 客户自用
                    items.add(new MyItem(lats[i], inforBundle, descriptor4));
                } else {// 自营
                    items.add(new MyItem(lats[i], inforBundle, descriptor));
                }
            }
            mClusterManager.addItems(items);
            mClusterManager.cluster();
            // 设置地图监听，当地图状态发生改变时，进行点聚合运算
            mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            public boolean onMarkerClick(final Marker marker) {

                if (marker != null) {
                    mBaiduMap.hideInfoWindow();
                    if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
                        chargestationinforLayout.setVisibility(View.GONE);
                    }
                    if (mMark != marker) {
                        chargelat = marker.getPosition().latitude;
                        chargelon = marker.getPosition().longitude;
                        //计算距离
                        LatLng chargeLatLng = new LatLng(chargelat, chargelon);
                        LatLng myLatLng = new LatLng(mylat, mylon);

                        distanceTextView.setText(new DecimalFormat("#.0")
                                .format(DistanceUtil.getDistance(chargeLatLng, myLatLng) / 1000) + "KM");


                        float zoom = mBaiduMap.getMapStatus().zoom;
                        ms = new MapStatus.Builder()
                                .target(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
                                .zoom(zoom).build();
                        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                        cs_nowInforBundle = marker.getExtraInfo();
                        if (cs_nowInforBundle != null) {
                            String nameString = cs_nowInforBundle.getString("cs_name");
                            if (nameString.length() >= 10) {
                                nameString = nameString.substring(0, 10) + "...";
                            }
                            infor_nameTextView.setText(nameString);
                            infor_siteTextView.setText(cs_nowInforBundle.getString("cs_address"));
                            if ("1".equals(cs_nowInforBundle.getString("GB"))) {
                                statingjImageView.setVisibility(View.VISIBLE);
                            } else {
                                statingjImageView.setVisibility(View.GONE);
                            }

                            if ("1".equals(cs_nowInforBundle.getString("BYD"))) {
                                statinbydImageView.setVisibility(View.VISIBLE);
                            } else {
                                statinbydImageView.setVisibility(View.GONE);
                            }

                            if ("1".equals(cs_nowInforBundle.getString("TESLA"))) {
                                statintslImageView.setVisibility(View.VISIBLE);
                            } else {
                                statintslImageView.setVisibility(View.GONE);
                            }

                            if ("1".equals(cs_nowInforBundle.getString("QT"))) {
                                statinqtImageView.setVisibility(View.VISIBLE);
                            } else {
                                statinqtImageView.setVisibility(View.GONE);
                            }
                            chargestatusTextView.setText("快充（" + cs_nowInforBundle.getString("fast") + "）  慢充（"
                                    + cs_nowInforBundle.getString("slow") + "）");
                            chargestationinforLayout.setVisibility(View.VISIBLE);

                        } else {
                            zoom = (float) (zoom + 0.5);
                            ms = new MapStatus.Builder().zoom(zoom).build();
                            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                        }

                    }
                }
                return true;
            }
        });

    }


    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        private final Bundle bundle;
        private final BitmapDescriptor descriptor;

        public MyItem(LatLng latLng, Bundle inforBundle, BitmapDescriptor icondescriptor) {
            mPosition = latLng;
            bundle = inforBundle;
            descriptor = icondescriptor;
        }

        public LatLng getPosition() {
            return mPosition;
        }

        public Bundle getBundle() {
            return bundle;
        }

        public BitmapDescriptor getBitmapDescriptor() {
            return descriptor;
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            Intent intent = null;
            switch (arg0.getId()) {

                case R.id.tab_charge_settextview:
                    intent = new Intent(SearchPowerActivity.this, BaiduMapConnetionSetActivity.class);
                    intent.putExtra("connecttype", connecttype_map);
                    intent.putExtra("charge_pattern", charge_pattern_map);
                    intent.putExtra("pole_status", pole_status_map);
                    intent.putExtra("distance", nowdistance + "");
                    intent.putExtra("type", "map");
                    startActivityForResult(intent, 1);

                    break;
                case R.id.tab_charge_chargestatin_infor_topinforlayout:
                    intent = new Intent(SearchPowerActivity.this, ChargeStationInforActivity.class);
                    intent.putExtras(cs_nowInforBundle);
                    startActivity(intent);

                    break;
                case tab_charge_statinlistmeau:
                    if (mapLayout.getVisibility() == View.VISIBLE) {
                        mapLayout.setVisibility(View.GONE);
                        stationlistLayout.setVisibility(View.VISIBLE);
                        stationView.setBackgroundResource(R.drawable.dianzhanliebiao);
                        if (!hasGetList) {// 首次进入
                            hasGetList = true;
                            stationsDataList.clear();
                            getCity_quyu();
                        }
                    } else {
                        stationlistLayout.setVisibility(View.GONE);
                        mapLayout.setVisibility(View.VISIBLE);
                        selectCity = dizhishaixuanView.getText().toString();
                        if (!selectCity.equals("")) {
                            //地理编码
                            mSearch.geocode(new GeoCodeOption().city(
                                    selectCity).address(selectCity + "站"));
                        }
                        stationView.setBackgroundResource(R.drawable.dianzhanditu_1_03);
                    }
                    break;
                case R.id.tab_charge_searchok:
                    mapView.requestFocus();
                    mapView.setFocusable(true);
                    if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
                        chargestationinforLayout.setVisibility(View.GONE);
                    }

                    InputMethodManager imm = (InputMethodManager) SearchPowerActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getApplicationWindowToken(), 0);
                    String input = searchEditText.getText().toString();
                    if (TextUtils.isEmpty(input)) {
                        PublicUtil.showToast(SearchPowerActivity.this, "输入内容不能为空", false);
                        return;
                    } else {
                        if (handler.hasMessages(getinforbykeyword)) {
                            handler.removeMessages(getinforbykeyword);
                            nowSearchkey = null;
                        }

                        savekey(input);

                        Map<String, String> map = new HashMap<String, String>();
                        map.put("act", Constent.ACT_GETMAPSTATIONWITHKEYWORD);
                        map.put("keyword", input);
                        map.put("curlat", mylat + "");
                        map.put("curlng", mylon + "");
                        map.put("ver", Constent.VER);

                        AnsynHttpRequest.httpRequest(SearchPowerActivity.this, AnsynHttpRequest.POST, callBack,
                                Constent.ID_GETMAPSTATIONWITHKEYWORD, map, false, true, true);
                    }

                    break;
                case R.id.tab_charge_chargestatin_infor_routplan:
                    routPlan();
                    break;
                case R.id.tab_charge_statinlist_shaixuanlayout:
                    intent = new Intent(SearchPowerActivity.this, BaiduMapConnetionSetActivity.class);
                    intent.putExtra("connecttype", connecttype_list);
                    intent.putExtra("charge_pattern", charge_pattern_list);
                    intent.putExtra("pole_status", pole_status_list);
                    intent.putExtra("type", "list");
                    startActivityForResult(intent, 1);
                    break;
                case R.id.tab_charge_statinlist_dizhilayout:
                    intent = new Intent(SearchPowerActivity.this, AddrListActivity.class);
                    intent.putExtra("type", "map");
                    startActivityForResult(intent, REQUESTCODE);
                    break;

                case R.id.tab_charge_searchclear:
                    PublicUtil.removeStorage_string(SearchPowerActivity.this, "mapsearchkey");
                    keywordList.clear();
                    searchLayout.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 地图加载完成的回调事件
     */
    private BaiduMap.OnMapLoadedCallback mapLoadedCallback = new BaiduMap.OnMapLoadedCallback() {

        @Override
        public void onMapLoaded() {
            // TODO Auto-generated method stub

            if ("4.9E-324".equals(String.valueOf(mylat)) || "4.9E-324".equals(String.valueOf(mylon))) {
                PublicUtil.showToast(SearchPowerActivity.this, "定位异常，经纬度获取错误，请退出重试", false);
                return;
            }
            ms = new MapStatus.Builder().target(new LatLng(mylat, mylon)).zoom(starzoom).build();
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            getMarkPoints(true);
        }
    };

    private BaiduMap.OnMapTouchListener onMapTouchListener = new BaiduMap.OnMapTouchListener() {

        @Override
        public void onTouch(MotionEvent arg0) {
            // TODO Auto-generated method stub
            mapLayout.requestFocus();
            mapLayout.setFocusable(true);
            keywordList.clear();
            searchLayout.setVisibility(View.GONE);
            //隐藏输入法
            InputMethodManager imm = (InputMethodManager) SearchPowerActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getApplicationWindowToken(), 0);
            if (mBaiduMap == null) {
                return;
            }
            if (arg0.getAction() == MotionEvent.ACTION_UP) {
                if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
                    chargestationinforLayout.setVisibility(View.GONE);
                }
                float zoom = mBaiduMap.getMapStatus().zoom;
                Log.e(TAG, "zoom: " + zoom + "nowzoom" + nowzoom);
                //变化后的zoom和变化前的zoom
                if (zoom < nowzoom) {  //缩小
                    nowzoom = zoom;
                    int distance = getDistance(nowzoom);
                    if (distance > nowdistance) {
                        nowdistance = distance;
                        Log.e(TAG, "onTouch: " +mylat+": "+mylon);
                        if ("4.9E-324".equals(String.valueOf(mylat)) || "4.9E-324".equals(String.valueOf(mylon))) {
                            return;
                        } else {
                            if(isHasChangedCity){
                                isGetLocatioFromCity = true;
                            }
                            getMarkPoints(false);
                        }

                    }

                }

                //                else {
                //                    if (mBaiduMap.getProjection() == null) {
                //                        return;
                //                    }
                //                    Point point = new Point(mScreenWidth / 2, mScreenHeight / 2);
                //                    LatLng latLng = mBaiduMap.getProjection().fromScreenLocation(point);  //将屏幕坐标转换地理坐标
                //
                //                    LatLng myLatLng = new LatLng(mylat, mylon);
                //
                //                    int distance = (int) (DistanceUtil.getDistance(myLatLng, latLng) / 1000);
                //                    PublicUtil.logDbug(TAG, "distance" + distance, 0);
                //                    PublicUtil.logDbug(TAG, "nowdistance" + nowdistance, 0);
                //                    if (zoom >= 8) {
                //                        if (nowdistance < 3000 && distance > nowdistance) {
                //                            if (distance <= 30 && distance > 10) {
                //                                nowdistance = 30;
                //                            } else if (distance <= 120 && distance > 30) {
                //                                nowdistance = 120;
                //                            } else if (distance <= 500 && distance > 120) {
                //                                nowdistance = 500;
                //                            } else if (distance <= 3000 && distance > 500) {
                //                                nowdistance = 3000;
                //                            }
                //
                //                            if ("4.9E-324".equals(String.valueOf(mylat)) || "4.9E-324".equals(String.valueOf(mylon))) {
                //                                return;
                //
                //                            } else {
                //                                getMarkPoints(false);
                //                            }
                //
                //                        }
                //
                //                    }
                //
                //                }

            }

        }
    };

    /**
     * 根据缩放等级获得比例尺
     *
     * @param zoom
     * @return
     */
    public int getDistance(float zoom) {

        int radius = 10;
        if (zoom >= (float) 12) {
            radius = 30;
        } else if (zoom >= (float) 10 && zoom < (float) 12) {
            radius = 150;
        } else if (zoom >= (float) 8 && zoom < (float) 10) {
            radius = 500;
        } else if (zoom < (float) 8) {
            radius = 4000;
        }

        return radius;
    }

    /**
     * 根据电站半径获取缩放等级
     *
     * @param distence
     * @return
     */
    public int getZoom(int distence) {

        int zoom = 15;

        if (distence < 30) {
            zoom = 13;
        } else if (distence >= 30 && distence < 60) {
            zoom = 11;
        } else if (distence >= 60 && distence < 150) {
            zoom = 10;
        } else if (distence >= 150 && distence < 600) {
            zoom = 8;
        } else if (distence >= 600) {
            zoom = 6;
        }

        return zoom;
    }

    private ChargeStationListAdapter stationListAdapter = null;

    /**
     * 切换列表模式
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void initStatinList() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_listview_empty_show_congzhi, null);
        TextView emptyinforView = (TextView) view.findViewById(R.id.view_listview_emptyshow_text);
        emptyinforView.setText("暂无充电站");
        pullToRefreshListView.setEmptyView(view);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        ILoadingLayout startLabels = pullToRefreshListView.getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = pullToRefreshListView.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                pullToRefreshListView.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        isDownFlush = true;
                        nowPage = 1;
                        getData();
                    }
                }, 1000);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // TODO Auto-generated method stub
                nowPage++;
                pullToRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (nowPage <= allPage) {
                            getData();
                        } else {
                            PublicUtil.showToast(SearchPowerActivity.this, "没有更多充电站了", false);
                            pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
                            // 我们已经更新完成
                        }
                    }
                }, 1000);

            }
        });

        stationListView = pullToRefreshListView.getRefreshableView();

        stationListAdapter = new ChargeStationListAdapter(this, xiangqingCallBack);

        stationListView.setAdapter(stationListAdapter);

    }

    /**
     * 电站列表详情区域点击回调事件
     */
    private ChargeStationListAdapter.CallBack xiangqingCallBack = new ChargeStationListAdapter.CallBack() {

        @Override
        public void click(View v) {
            // TODO Auto-generated method stub

            if (v.getTag() != null) {

                int i = Integer.valueOf((String) v.getTag());

                if (count != 0) {// 考虑反序后
                    i = (count - i - 1);
                }

                Map<String, String> map = stationsDataList.get(i);

                Intent intent = new Intent(SearchPowerActivity.this, ChargeStationInforActivity.class);
                intent.putExtra("cs_id", map.get("cs_id"));
                intent.putExtra("cs_code", map.get("cs_code"));
                intent.putExtra("cs_name", map.get("cs_name"));
                intent.putExtra("cs_type", map.get("cs_type"));
                intent.putExtra("cs_lat", map.get("cs_lat"));
                intent.putExtra("cs_lng", map.get("cs_lng"));
                intent.putExtra("my_lat", map.get("my_lat"));
                intent.putExtra("my_lng", map.get("my_lng"));
                intent.putExtra("cs_address", map.get("cs_address"));
                intent.putExtra("distance", map.get("distance"));
                intent.putExtra("GB", map.get("GB"));
                intent.putExtra("BYD", map.get("BYD"));
                intent.putExtra("TESLA", map.get("TESLA"));
                intent.putExtra("QT", map.get("QT"));
                intent.putExtra("slow", map.get("slow"));
                intent.putExtra("fast", map.get("fast"));
                startActivity(intent);
            }

        }
    };

    /**
     * 设置列表数据
     *
     * @param array
     */
    private void setDate(JSONArray array) {

        try {
            if (isDownFlush) {
                count = 0;
                isDownFlush = false;
                stationsDataList.clear();
            }

            int nowcount = stationsDataList.size();
            JSONObject object = null;
            for (int i = 0; i < array.length(); i++) {
                Map<String, String> map = new HashMap<String, String>();
                object = array.getJSONObject(i);
                map.put("i", String.valueOf(i + nowcount));
                map.put("cs_id", object.getString("cs_id"));
                map.put("cs_code", object.getString("cs_code"));
                map.put("cs_name", object.getString("cs_name"));
                map.put("cs_type", object.getString("cs_type"));
                map.put("cs_lat", object.getString("cs_lat"));
                map.put("cs_lng", object.getString("cs_lng"));
                map.put("my_lat", String.valueOf(mylat));
                map.put("my_lng", String.valueOf(mylon));
                map.put("cs_address", object.getString("cs_address"));
                map.put("distance", object.getString("distance"));

                map.put("GB", "0");
                map.put("BYD", "0");
                map.put("TESLA", "0");
                map.put("QT", "0");

                if ("SELF_OPERATION".equals(object.getString("cs_type"))
                        || "JOINT_OPERATION".equals(object.getString("cs_type"))) {// 自营联营
                    JSONArray connecttypeArray = object.getJSONArray("connection_type");

                    for (int j = 0; j < connecttypeArray.length(); j++) {

                        if ("GB".equals(connecttypeArray.getString(j))) {
                            map.put("GB", "1");
                        } else if ("BYD".equals(connecttypeArray.getString(j))) {
                            map.put("BYD", "1");
                        } else if ("TESLA".equals(connecttypeArray.getString(j))) {
                            map.put("TESLA", "1");
                        } else {
                            map.put("QT", "1");
                        }
                    }

                    JSONObject free_pole_numObject = object.getJSONObject("free_pole_num");
                    map.put("slow", free_pole_numObject.getString("slow"));
                    map.put("fast", free_pole_numObject.getString("fast"));
                } else {//

                    map.put("slow", object.getString("spots_slow_num"));
                    map.put("fast", object.getString("spots_fast_num"));

                    if ("GB".equals(object.getString("spots_connection_type"))) {
                        map.put("GB", "1");
                    } else if ("BYD".equals(object.getString("spots_connection_type"))) {
                        map.put("BYD", "1");
                    } else if ("TESLA".equals(object.getString("spots_connection_type"))) {
                        map.put("TESLA", "1");
                    } else {
                        map.put("QT", "1");
                    }
                }

                PublicUtil.logDbug(TAG, map.toString(), 0);
                stationsDataList.add(map);
            }
            stationListAdapter.setData(stationsDataList);
            stationListAdapter.notifyDataSetChanged();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1 && data != null) {// 从筛选类型界面回来

            if (stationlistLayout.getVisibility() == View.VISIBLE) {
                charge_pattern_list = data.getStringExtra("charge_pattern");
                pole_status_list = data.getStringExtra("pole_status");
                connecttype_list = data.getStringExtra("connecttype");
                isDownFlush = true;
                nowPage = 1;
                getData();
            } else {
                //筛选距离
                nowdistance = Integer.parseInt(data.getStringExtra("distance"));
                charge_pattern_map = data.getStringExtra("charge_pattern");
                pole_status_map = data.getStringExtra("pole_status");
                connecttype_map = data.getStringExtra("connecttype");
                ms = new MapStatus.Builder().target(new LatLng(mylat, mylon)).zoom(starzoom).build();
                nowzoom = 13;
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
                getMarkPoints(false);
            }

        } else if (resultCode == 11 && data != null) {// 从地址选择界面返回
            city_id = data.getStringExtra("id");
            city_name = data.getStringExtra("name");
            dizhishaixuanView.setText(city_name);
            stationsDataList.clear();
            getCity_quyu();
        }

    }

    class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(SearchPowerActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                        .show();
                return;
            }
            if (chargestationinforLayout.getVisibility() == View.VISIBLE) {
                chargestationinforLayout.setVisibility(View.GONE);
            }
            cityToLat = result.getLocation().latitude;
            cityToLon = result.getLocation().longitude;
            if (mycity.equals(selectCity + "市")) {
                ms = new MapStatus.Builder().target(new LatLng(mylat, mylon)).zoom(starzoom).build();
            } else {
                ms = new MapStatus.Builder().target(new LatLng(cityToLat, cityToLon)).zoom(starzoom).build();
            }

            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
            nowzoom = starzoom;
            nowdistance = 30;
            isHasChangedCity = true;
            isGetLocatioFromCity = true;
            getMarkPoints(false);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

        }


    }

    private List<Map<String, Object>> keywordList = new ArrayList<Map<String, Object>>();

    /**
     * 用来展示搜索框下面的关键字列表
     *
     * @param jsonArray
     */
    private void showList(JSONArray jsonArray, int type) {
        keywordList.clear();
        try {

            if (type == 1) {// 网络获取
                JSONObject jsonObject = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("cs_id", jsonObject.getString("cs_id"));
                    map.put("text", jsonObject.getString("text"));
                    if ("SELF_OPERATION".equals(jsonObject.getString("cs_type"))) {
                        map.put("cs_type", R.drawable.dianzhanditu_2_03);
                    } else if ("JOINT_OPERATION".equals(jsonObject.getString("cs_type"))) {
                        map.put("cs_type", R.drawable.dianzhanditu_2_09);
                    } else {
                        map.put("cs_type", R.drawable.dianzhanditu_2_06);
                    }

                    keywordList.add(map);

                }
            } else {// 本地数据

                for (int i = 0; i < searchkeys.length; i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("text", searchkeys[i]);
                    map.put("cs_type", "0");
                    keywordList.add(map);
                }

            }

            if (keywordList.size() > 0) {

                if (type == 1) {// 网络获取
                    clearsearchTextView.setVisibility(View.GONE);
                } else {// 本地数据
                    clearsearchTextView.setVisibility(View.VISIBLE);
                }
                SimpleAdapter simplead = new SimpleAdapter(this, keywordList,
                        R.layout.listview_item_mapsearchkeyword, new String[]{"text", "cs_type"}, new int[]{
                        R.id.listview_item_mapsearchkeyword_text, R.id.listview_item_mapsearchkeyword_image});

                searchListView.setAdapter(simplead);
                searchLayout.setVisibility(View.VISIBLE);
                searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                        // TODO Auto-generated method stub
                        if (handler.hasMessages(getinforbykeyword)) {
                            handler.removeMessages(getinforbykeyword);
                            nowSearchkey = null;
                        }
                        searchLayout.setVisibility(View.GONE);
                        searchEditText.setText((CharSequence) keywordList.get(arg2).get("text"));
                        keywordList.clear();
                    }
                });
            } else {
                searchLayout.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private String endAddr = "终点";

    /**
     * 导航规划
     */
    public void routPlan() {

        if (isInstallPackage("com.baidu.BaiduMap") && isInstallPackage("com.autonavi.minimap")) {
            showMenu();

        } else if (isInstallPackage("com.baidu.BaiduMap") && !isInstallPackage("com.autonavi.minimap")) {
            dialog = PublicUtil.showDialog(this);
            PublicUtil.showToast(this, "正在启动导航软件，请等待", false);
            openBaiduMap();

        } else if (!isInstallPackage("com.baidu.BaiduMap") && isInstallPackage("com.autonavi.minimap")) {
            dialog = PublicUtil.showDialog(this);
            PublicUtil.showToast(this, "正在启动导航软件，请等待", false);
            openGaoDeMap();
        } else {
            PublicUtil.showToast(this, "未检测到第三方导航软件", false);
        }
    }

    @SuppressLint("SdCardPath")
    private boolean isInstallPackage(String packageName) {

        return new File("/data/data/" + packageName).exists();
    }

    /**
     * 打开百度地图导航，用intent方式
     */
    @SuppressWarnings({"deprecation"})
    private void openBaiduMap() {

        try {
            String intentString = "intent://map/direction?origin=name:起点|latlng:" + mylat + "," + mylon
                    + "&destination=name:" + endAddr + "|latlng:" + chargelat + "," + chargelon
                    + "&mode=driving&src=地上铁公司|地上铁#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
            PublicUtil.logDbug(TAG, intentString, 0);
            Intent intent = Intent.getIntent(intentString);
            startActivity(intent); // 启动调用

        } catch (Exception e) {
            e.printStackTrace();
            PublicUtil.showToast(this, "打开百度导航失败", false);
        }
    }

    @SuppressWarnings("deprecation")
    private void openGaoDeMap() {
        try {
            double[] gd_lat_lon = bdToGaoDe(chargelat, chargelon);
            String intentString = "androidamap://navi?sourceApplication=地上铁&lat=" + gd_lat_lon[1] + "&lon="
                    + gd_lat_lon[0] + "&dev=0&style=2";

            PublicUtil.logDbug(TAG, intentString, 0);
            Intent intent = Intent.getIntent(intentString);
            startActivity(intent); // 启动调用
        } catch (Exception e) {
            e.printStackTrace();
            PublicUtil.showToast(this, "打开高德导航失败", false);
        }
    }

    /**
     * 百度坐标转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
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

    /**
     * 显示导航选择分类
     */
    public void showMenu() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_routplan_dialog, null);
        TextView baiduTextView, gaodeTextView, cancelTextView;
        baiduTextView = (TextView) view.findViewById(R.id.view_rounplan_dialog_baidu);
        gaodeTextView = (TextView) view.findViewById(R.id.view_rounplan_dialog_gaode);
        cancelTextView = (TextView) view.findViewById(R.id.view_rounplan_dialog_cancle);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).show();

        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        baiduTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                // TODO Auto-generated method stub
                dialog = PublicUtil.showDialog(SearchPowerActivity.this);
                PublicUtil.showToast(SearchPowerActivity.this, "正在启动导航软件，请等待", false);
                openBaiduMap();

            }
        });
        gaodeTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                dialog = PublicUtil.showDialog(SearchPowerActivity.this);
                PublicUtil.showToast(SearchPowerActivity.this, "正在启动导航软件，请等待", false);
                openGaoDeMap();
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            }
        });

    }

    /**
     * 保存本地搜索记录
     *
     * @param input
     */
    private void savekey(String input) {

        String mapkeystrString = PublicUtil.getStorage_string(this, "mapsearchkey", "-1");

        if (!"-1".equals(mapkeystrString)) {

            if (mapkeystrString.contains("DST&DST")) {
                String[] strs = mapkeystrString.split("DST&DST");
                boolean same = false;
                if (strs.length < 5) {

                    for (int i = 0; i < strs.length; i++) {

                        if (input.equals(strs[i])) {
                            same = true;
                        }
                    }

                    if (!same) {
                        PublicUtil.setStorage_string(this, "mapsearchkey",
                                input + "DST&DST" + mapkeystrString);
                    }

                } else {
                    for (int i = 0; i < strs.length; i++) {

                        if (input.equals(strs[i])) {
                            same = true;
                        }
                    }
                    if (!same) {
                        strs[0] = input;

                        PublicUtil.setStorage_string(this, "mapsearchkey", strs[0] + "DST&DST" + strs[1]
                                + "DST&DST" + strs[2] + "DST&DST" + strs[3] + "DST&DST" + strs[4]);
                    }
                }

            } else {
                if (!input.equals(mapkeystrString)) {
                    PublicUtil.setStorage_string(this, "mapsearchkey", input + "DST&DST" + mapkeystrString);
                }

            }

        } else {
            PublicUtil.setStorage_string(this, "mapsearchkey", input);
        }

    }


    /**
     * 获取列表时电站数据
     */
    public void getData() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_GETSTATIONLIST);
        map.put("ver", Constent.VER);
        map.put("rows", PublicUtil.PAGESIZE);
        map.put("page", String.valueOf(nowPage));
        map.put("area_id", aera_id);
        map.put("curlat", mylat + "");
        map.put("curlng", mylon + "");
        map.put("contype", connecttype_list);
        map.put("charge_pattern", charge_pattern_list);
        map.put("pole_status", pole_status_list);

        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.POST, callBack, Constent.ID_GETSTATIONLIST, map,
                false, true, true);
    }

    /**
     * 获取城市里面的区域
     */
    private void getCity_quyu() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("act", Constent.ACT_REGION_INDEX);
        map.put("id", city_id);

        AnsynHttpRequest.httpRequest(this, AnsynHttpRequest.GET, callBack, Constent.ID_REGION_INDEX, map,
                false, true, true);
    }

    private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    private AdderListAdapter adapter;

    /**
     * 设置区域列表
     *
     * @param dataArray
     */
    private void flushlist(JSONArray dataArray) {
        list.clear();
        try {
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject object = dataArray.getJSONObject(i);
                Map<String, String> map = new HashMap<String, String>();
                if (i == 0) {
                    map.put("ischoose", "1");
                    aera_id = object.getString("id");
                } else {
                    map.put("ischoose", "0");
                }

                map.put("id", object.getString("id"));
                map.put("name", object.getString("name"));
                map.put("type", object.getString("type"));
                map.put("type2", "map");
                map.put("hascont", "1");// 默认所有区域内都有电站
                list.add(map);
            }

            adapter.setData(list);
            adapter.notifyDataSetChanged();
            quyuView.setText(list.get(0).get("name"));
            quyuView.setVisibility(View.VISIBLE);
            pullToRefreshListView.setVisibility(View.VISIBLE);
            getData();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

}
