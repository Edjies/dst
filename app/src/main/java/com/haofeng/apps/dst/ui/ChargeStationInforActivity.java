package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.ChargeGunListAdapter;
import com.haofeng.apps.dst.adapter.ChargeGunListAdapter.Callback;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChargeStationInforActivity extends BaseActivity implements
		OnClickListener {
	private static String TAG = "ChargeStationInforActivity";
	private FrameLayout topLayout;
	private String phone, islogin;
	private String cs_id;
	private String isAddFavorite = "0";
	private Button collectionButton, routplanButton;
	private TextView infor_collectionTextView;
	private LinearLayout collectionLayout, routplanLayout, chooseviewLayout;
	private TextView backView, titleView;
	private TextView errorView;
	private TextView chooseView, chooseView2;
	private TextView chooseImageView, chooseImageView2;

	private List<Map<String, String>> favoritechangeList = new ArrayList<Map<String, String>>();// 收藏电桩时，初始获取到的是否收藏标识动态修改比较麻烦，因此用list了暂时记录
	private String fid, tel;

	private AlertDialog dialog;
	private double mylat, mylon;// 定位后自己的经纬度
	private double chargelat, chargelon;// 选择的充电桩的经纬度
	private List<Bitmap> imagebtmList = new ArrayList<Bitmap>();
	private ViewPager viewPager;
	private List<View> listViews = new ArrayList<View>(); // Tab页面列表
	// private LinearLayout view1, view2;
	private ListView chargesListView;
	private ImageView chargeImageView, telImageView;
	private TextView workdate, weekdate, telTextView, workdatetitle,
			weekdatatitle, imagebottomView, app_tipsView, building_userView;
	private TextView dizhiTextView, tingchefeiinforTextView,
			tingchefeiinforTextView2;
	private ListView dianfeiListView, tingchefeiListView, fuwufeiListView,
			tingchefeiListView2;
	private LinearLayout tingchefeiinfor2Layout;
	private String cs_type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chargestationinfor);
	     addActivity(this);
		init();

	}

	/**
	 * 初始化
	 */
	private void init() {

		topLayout = (FrameLayout) findViewById(R.id.chargestationinfor_top_layout);
		setTopLayoutPadding(topLayout);
		infor_collectionTextView = (TextView) findViewById(R.id.chargestationinfor_collectiontext);
		collectionButton = (Button) findViewById(R.id.chargestationinfor_collectionbutton);
		collectionLayout = (LinearLayout) findViewById(R.id.chargestationinfor_collectionlayout);
		routplanLayout = (LinearLayout) findViewById(R.id.chargestationinfor_routeplanlayout);
		routplanButton = (Button) findViewById(R.id.chargestationinfor_routeplanbutton);
		backView = (TextView) findViewById(R.id.chargestationinfor_back);
		titleView = (TextView) findViewById(R.id.chargestationinfor_title);
		errorView = (TextView) findViewById(R.id.chargestationinfor_error);

		chooseviewLayout = (LinearLayout) findViewById(R.id.chargestationinfor_chooseview_layout);
		chooseView = (TextView) findViewById(R.id.chargestationinfor_chooseview1);
		chooseView2 = (TextView) findViewById(R.id.chargestationinfor_chooseview2);
		chooseImageView = (TextView) findViewById(R.id.chargestationinfor_chooseview1bottom);
		chooseImageView2 = (TextView) findViewById(R.id.chargestationinfor_chooseview2bottom);

		viewPager = (ViewPager) findViewById(R.id.chargestationinfor_vPager);

		// 这里开始属于tab滑动

		View view1 = this.getLayoutInflater().inflate(
				R.layout.chargestationinfor_view1, null);
		View view2 = this.getLayoutInflater().inflate(
				R.layout.chargestationinfor_view2, null);
		chargesListView = (ListView) view1
				.findViewById(R.id.chargestationinfor_chargelist);
		chargeImageView = (ImageView) view2
				.findViewById(R.id.chargestationinfor_view2_chargeimage);
		telImageView = (ImageView) view2
				.findViewById(R.id.chargestationinfor_view2_phoneimage);
		telTextView = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_phonetext);
		workdate = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_workdate);
		weekdate = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_weekdate);
		dizhiTextView = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_dizhi);
		tingchefeiinforTextView = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_tingchefeiinfor);
		tingchefeiinforTextView2 = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_tingchefeiinfor2);
		dianfeiListView = (ListView) view2
				.findViewById(R.id.chargestationinfor_view2_dianfeilist);
		tingchefeiListView = (ListView) view2
				.findViewById(R.id.chargestationinfor_view2_tingchefeilist);
		tingchefeiListView2 = (ListView) view2
				.findViewById(R.id.chargestationinfor_view2_tingchefeilist2);
		fuwufeiListView = (ListView) view2
				.findViewById(R.id.chargestationinfor_view2_fuwufeilist);
		workdatetitle = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_workdatetitle);
		weekdatatitle = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_weekdatetitle);
		imagebottomView = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_chargeimagebottom);
		tingchefeiinfor2Layout = (LinearLayout) view2
				.findViewById(R.id.chargestationinfor_view2_tingchefeiinfor2layout);
		app_tipsView = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_app_tips);
		building_userView = (TextView) view2
				.findViewById(R.id.chargestationinfor_view2_app_cs_building_user);
		cs_type = getIntent().getStringExtra("cs_type");
		if ("SELF_OPERATION".equals(cs_type)
				|| "JOINT_OPERATION".equals(cs_type)) {// 自营
			chooseviewLayout.setVisibility(View.VISIBLE);
			listViews.add(view1);
			listViews.add(view2);
			viewPager.setAdapter(new MyPagerAdapter(listViews));
			viewPager.setCurrentItem(0);
			viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
			chooseView.setOnClickListener(new topTabClick(0));
			chooseView2.setOnClickListener(new topTabClick(1));
		} else {
			chooseviewLayout.setVisibility(View.GONE);
			listViews.add(view2);
			viewPager.setAdapter(new MyPagerAdapter(listViews));
			viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		}

		infor_collectionTextView.setText("收藏");
		backView.setOnClickListener(this);
		collectionLayout.setOnClickListener(this);
		collectionButton.setOnClickListener(this);
		routplanButton.setOnClickListener(this);
		routplanLayout.setOnClickListener(this);
		errorView.setOnClickListener(this);

		telImageView.setOnClickListener(this);
		chargeImageView.setOnClickListener(this);
		phone = PublicUtil.getStorage_string(ChargeStationInforActivity.this,
				"phone", "0");
		islogin = PublicUtil.getStorage_string(ChargeStationInforActivity.this,
				"islogin", "0");

		if ("0".equals(phone)) {
			PublicUtil.setStorage_string(ChargeStationInforActivity.this,
					"islogin", "0");
			islogin = "0";
		}

		String latString = PublicUtil.getStorage_string(
				ChargeStationInforActivity.this, "mylat", "4.9E-324");
		String lonString = PublicUtil.getStorage_string(
				ChargeStationInforActivity.this, "mylon", "4.9E-324");
		mylat = Double.valueOf(latString);
		mylon = Double.valueOf(lonString);
		titleView.setText(getIntent().getStringExtra("cs_name"));
		titleView.requestFocus();
		titleView.setFocusable(true);
		dizhiTextView.setText(getIntent().getStringExtra("cs_address"));

		chargelat = Double.valueOf(getIntent().getStringExtra("cs_lat"));
		chargelon = Double.valueOf(getIntent().getStringExtra("cs_lng"));

		phone = PublicUtil.getStorage_string(ChargeStationInforActivity.this,
				"phone", "0");
		islogin = PublicUtil.getStorage_string(ChargeStationInforActivity.this,
				"islogin", "0");

		if ("0".equals(phone)) {
			PublicUtil.setStorage_string(ChargeStationInforActivity.this,
					"islogin", "0");
			islogin = "0";
		}

		cs_id = getIntent().getStringExtra("cs_id");
		chooseView.setTextColor(getResources().getColor(R.color.menugreen));
		chooseImageView.setVisibility(View.VISIBLE);
		chooseImageView2.setVisibility(View.GONE);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_GETMAPCHARGEINFOR);
		map.put("cs_id", cs_id);
		map.put("curlat", mylat + "");
		map.put("curlng", mylon + "");
		map.put("mobile", phone);
		map.put("ver", Constent.VER);

		AnsynHttpRequest.httpRequest(ChargeStationInforActivity.this,
				AnsynHttpRequest.POST, callBack, Constent.ID_GETMAPCHARGEINFOR,
				map, false, true, true);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		MobclickAgent.onResume(this);

	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		if (dialog != null) {
			dialog.dismiss();
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

			case Constent.ID_COLLECTION:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_collecton);

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
			case Constent.ID_REMOVECOLLECTION:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_deletecollection);

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
			case Constent.ID_GETMAPCHARGEINFOR:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_getchargeinfor);

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

			default:
				break;
			}

		}
	};

	private int httprequesterror = 0x4500;
	private int httprequestsuccess_getchargeinfor = 0x4501;
	private int httprequestsuccess_collecton = 0x4502;
	private int httprequestsuccess_deletecollection = 0x4503;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(ChargeStationInforActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess_collecton) {
				if (jsonObject != null) {

					try {
						// JSONObject jsonObject =
						// backArray.getJSONObject(0);

						if ("0".equals(jsonObject.get("error").toString())) {
							isAddFavorite = "1";
							collectionButton
									.setBackgroundResource(R.drawable.cdzxiangqing4_1_18);
							for (int i = 0; i < favoritechangeList.size(); i++) {
								Map<String, String> map = favoritechangeList
										.get(i);
								if (cs_id.equals(map.get("cs_id"))) {
									favoritechangeList.remove(i);
									break;
								}

							}
							JSONObject dataObject = new JSONObject(
									jsonObject.getString("data"));
							if (dataObject != null) {
								fid = dataObject.getString("id");
							}

							Map<String, String> map = new HashMap<String, String>();
							map.put("cs_id", cs_id);
							map.put("fid", fid);
							map.put("isAddFavorite", isAddFavorite);
							favoritechangeList.add(map);
							infor_collectionTextView.setText("已收藏");
							PublicUtil.showToast(
									ChargeStationInforActivity.this, jsonObject
											.get("msg").toString()
											+ "请在收藏列表中查看", false);
						} else {
							PublicUtil.showToast(
									ChargeStationInforActivity.this, jsonObject
											.get("msg").toString(), false);

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();

					}

				}

			} else if (msg != null
					&& msg.what == httprequestsuccess_deletecollection) {
				if (jsonObject != null) {

					try {
						PublicUtil.showToast(ChargeStationInforActivity.this,
								jsonObject.get("msg").toString(), false);
						if ("0".equals(jsonObject.get("error").toString())) {

							isAddFavorite = "0";
							fid = "0";
							collectionButton
									.setBackgroundResource(R.drawable.cdzxiangqing4_1_17);
							for (int i = 0; i < favoritechangeList.size(); i++) {
								Map<String, String> map = favoritechangeList
										.get(i);
								if (cs_id.equals(map.get("cs_id"))) {
									favoritechangeList.remove(i);
									break;
								}

							}
							Map<String, String> map = new HashMap<String, String>();
							map.put("cs_id", cs_id);
							map.put("fid", fid);
							map.put("isAddFavorite", isAddFavorite);
							favoritechangeList.add(map);
							infor_collectionTextView.setText("收藏");
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();

					}

				}

			}

			else if (msg != null
					&& msg.what == httprequestsuccess_getchargeinfor) {
				if (jsonObject != null) {

					try {
						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {

							JSONObject dataJsonObject = jsonObject
									.getJSONObject("data");

							isAddFavorite = dataJsonObject
									.getString("is_favorite");
							if ("1".equals(isAddFavorite)) {
								fid = dataJsonObject.getString("favorite_id");
								collectionButton
										.setBackgroundResource(R.drawable.cdzxiangqing4_1_18);
								infor_collectionTextView.setText("已收藏");
							} else {
								collectionButton
										.setBackgroundResource(R.drawable.cdzxiangqing4_1_17);
								infor_collectionTextView.setText("收藏");
							}

							tel = dataJsonObject
									.getString("cs_service_telephone");

							if (tel == null || TextUtils.isEmpty(tel)) {
								tel = getResources().getString(R.string.kefu);
							}

							telTextView.setText(tel);

							app_tipsView.setText(dataJsonObject
									.getString("app_tips"));
							building_userView.setText(dataJsonObject
									.getString("cs_building_user"));

							JSONArray open_timeArray = dataJsonObject
									.getJSONArray("open_time");
							if (open_timeArray != null
									&& open_timeArray.length() > 0) {

								JSONObject workObject = open_timeArray
										.getJSONObject(0);
								workdatetitle.setText(workObject
										.getString("text") + ":");
								workdate.setText(workObject
										.getString("content"));
								JSONObject weekObject = open_timeArray
										.getJSONObject(1);
								weekdatatitle.setText(weekObject
										.getString("text") + ":");
								weekdate.setText(weekObject
										.getString("content"));
							}

							if ("SELF_OPERATION".equals(cs_type)
									|| "JOINT_OPERATION".equals(cs_type)) {// 自营

								JSONArray connecttypeArray = dataJsonObject
										.getJSONArray("connection_type");

								JSONArray gunArray = dataJsonObject
										.getJSONArray("pole_list");

								if (gunArray != null && gunArray.length() > 0) {

									showList(gunArray);

								}
							}

							JSONObject rateObject = dataJsonObject
									.getJSONObject("rate");
							if (rateObject != null) {
								rateShow(rateObject);
							}

							JSONArray imagesArray = dataJsonObject
									.getJSONArray("cs_pic_path");
							if (imagesArray != null && imagesArray.length() > 0) {

								setImage(imagesArray);

							} else {
								imagebottomView.setVisibility(View.GONE);
							}

						} else {
							PublicUtil.showToast(
									ChargeStationInforActivity.this, jsonObject
											.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();

					}

				}

			}

		};
	};

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

	@SuppressLint("NewApi")
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (arg0.getId()) {
		case R.id.chargestationinfor_routeplanbutton:
		case R.id.chargestationinfor_routeplanlayout:
			routPlan();

			break;

		// case R.id.map_orderbutton://预约
		//
		// if ("1".equals(islogin)) {
		// intent = new Intent(ChargeStationInforActivity.this,
		// OrderActivity.class);
		// intent.putExtra("type", "add");
		// // intent.putExtras(cs_nowInforBundle);// 预约要修改成预约到某个电枪上
		// startActivity(intent);
		// } else {
		// PublicUtil.showToast(ChargeStationInforActivity.this,
		// "用户还未登录，请先登录", false);
		// intent = new Intent(ChargeStationInforActivity.this,
		// LoginActivity.class);
		// intent.putExtra("clicktype", "main");
		// startActivity(intent);
		// }
		//
		// break;

		case R.id.chargestationinfor_collectionbutton:// 收藏
		case R.id.chargestationinfor_collectionlayout:
			if ("1".equals(islogin)) {

				if ("1".equals(isAddFavorite)) {
					PublicUtil.showToast(this, "您已收藏这个充电站", false);
					// if (fid != null && !"0".equals(fid)) {
					// View view = LayoutInflater.from(
					// ChargeStationInforActivity.this).inflate(
					// R.layout.view_dialog, null);
					// TextView title = (TextView) view
					// .findViewById(R.id.view_dialog_title);
					// TextView infor = (TextView) view
					// .findViewById(R.id.view_dialog_infor);
					// infor.setVisibility(View.GONE);
					// TextView ok = (TextView) view
					// .findViewById(R.id.view_dialog_ok);
					// TextView cancel = (TextView) view
					// .findViewById(R.id.view_dialog_cancel);
					// title.setText("是否删除当前收藏记录？");
					//
					// final AlertDialog alertDialog = new AlertDialog.Builder(
					// ChargeStationInforActivity.this,
					// R.style.dialog_stroke).show();
					// alertDialog.addContentView(view, new LayoutParams(
					// LayoutParams.MATCH_PARENT,
					// LayoutParams.WRAP_CONTENT));
					// alertDialog.setCancelable(true);
					// alertDialog.setCanceledOnTouchOutside(true);
					//
					// ok.setOnClickListener(new OnClickListener() {
					//
					// @Override
					// public void onClick(View arg0) {
					// // TODO Auto-generated method stub
					// if (alertDialog != null) {
					// alertDialog.dismiss();
					// }
					// Map<String, String> map = new HashMap<String, String>();
					// map.put("act", Constent.ACT_REMOVECOLLECTION);
					// map.put("fid", fid);
					// map.put("mobile", phone);
					// map.put("ver", Constent.VER);
					//
					// AnsynHttpRequest.httpRequest(
					// ChargeStationInforActivity.this,
					// AnsynHttpRequest.POST, callBack,
					// Constent.ID_REMOVECOLLECTION, map,
					// false, false);
					// }
					// });
					//
					// cancel.setOnClickListener(new OnClickListener() {
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
					// } else {
					// PublicUtil.showToast(this, "该电桩收藏数据读取错误，请在收藏列表中操作",
					// false);
					// }

				} else {
					Map<String, String> map = new HashMap<String, String>();
					map.put("act", Constent.ACT_COLLECTION);
					map.put("chargerid", cs_id);// 电桩id
					map.put("mobile", phone);
					map.put("ver", Constent.VER);

					AnsynHttpRequest.httpRequest(
							ChargeStationInforActivity.this,
							AnsynHttpRequest.POST, callBack,
							Constent.ID_COLLECTION, map, false, false, true);
				}

			} else {
				PublicUtil.showToast(ChargeStationInforActivity.this, "请登录",
						false);
				intent = new Intent(ChargeStationInforActivity.this,
						LoginActivity.class);
				intent.putExtra("clicktype", "main");
				startActivity(intent);
			}

			break;

		case R.id.chargestationinfor_back:
			finish();

			break;
		case R.id.chargestationinfor_error:
			if ("1".equals(islogin)) {

				intent = new Intent(ChargeStationInforActivity.this,
						SuggestActivity.class);
				intent.putExtra("type", "jiucuo");
				startActivity(intent);
			} else {
				PublicUtil.showToast(ChargeStationInforActivity.this, "请登录",
						false);
				intent = new Intent(ChargeStationInforActivity.this,
						LoginActivity.class);
				intent.putExtra("clicktype", "main");
				startActivity(intent);
			}
			break;
		case R.id.chargestationinfor_view2_phoneimage:
			if (TextUtils.isEmpty(tel)) {
				tel = getResources().getString(R.string.kefu);
			}

			intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);

			break;
		case R.id.chargestationinfor_view2_chargeimage:
			if (imagebtmList.size() > 0) {
				intent = new Intent(ChargeStationInforActivity.this,
						ChargeStationImagesDialogActivity.class);
				intent.putExtra("name", titleView.getText().toString());
				startActivity(intent);

				// showImage();
			}

			break;
		default:
			break;
		}

	}

	private String endAddr = "终点";

	/**
	 * 导航规划
	 */
	public void routPlan() {

		if (isInstallPackage("com.baidu.BaiduMap")
				&& isInstallPackage("com.autonavi.minimap")) {
			showMenu();

		} else if (isInstallPackage("com.baidu.BaiduMap")
				&& !isInstallPackage("com.autonavi.minimap")) {
			dialog = PublicUtil.showDialog(ChargeStationInforActivity.this);
			PublicUtil.showToast(ChargeStationInforActivity.this,
					"正在启动导航软件，请等待", false);
			openBaiduMap();

		} else if (!isInstallPackage("com.baidu.BaiduMap")
				&& isInstallPackage("com.autonavi.minimap")) {
			dialog = PublicUtil.showDialog(ChargeStationInforActivity.this);
			PublicUtil.showToast(ChargeStationInforActivity.this,
					"正在启动导航软件，请等待", false);
			openGaoDeMap();
		} else {
			PublicUtil.showToast(ChargeStationInforActivity.this,
					"未检测到第三方导航软件", false);
		}
	}

	@SuppressLint("SdCardPath")
	private boolean isInstallPackage(String packageName) {

		return new File("/data/data/" + packageName).exists();
	}

	/**
	 * 打开百度地图导航，用intent方式
	 * 
	 */
	@SuppressWarnings({ "deprecation" })
	private void openBaiduMap() {

		try {
			String intentString = "intent://map/direction?origin=name:起点|latlng:"
					+ mylat
					+ ","
					+ mylon
					+ "&destination=name:"
					+ endAddr
					+ "|latlng:"
					+ chargelat
					+ ","
					+ chargelon
					+ "&mode=driving&src=地上铁公司|地上铁#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
			Intent intent = Intent.getIntent(intentString);
			startActivity(intent); // 启动调用

		} catch (Exception e) {
			e.printStackTrace();
			PublicUtil.showToast(ChargeStationInforActivity.this, "打开百度导航失败",
					false);
		}
	}

	@SuppressWarnings("deprecation")
	private void openGaoDeMap() {
		try {
			double[] gd_lat_lon = bdToGaoDe(chargelat, chargelon);
			String intentString = "androidamap://navi?sourceApplication=地上铁&lat="
					+ gd_lat_lon[1]
					+ "&lon="
					+ gd_lat_lon[0]
					+ "&dev=0&style=2";

			Intent intent = Intent.getIntent(intentString);
			startActivity(intent); // 启动调用
		} catch (Exception e) {
			e.printStackTrace();
			PublicUtil.showToast(ChargeStationInforActivity.this, "打开高德导航失败",
					false);
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

		View view = LayoutInflater.from(this).inflate(
				R.layout.view_routplan_dialog, null);
		TextView baiduTextView, gaodeTextView, cancelTextView;
		baiduTextView = (TextView) view
				.findViewById(R.id.view_rounplan_dialog_baidu);
		gaodeTextView = (TextView) view
				.findViewById(R.id.view_rounplan_dialog_gaode);
		cancelTextView = (TextView) view
				.findViewById(R.id.view_rounplan_dialog_cancle);

		final AlertDialog alertDialog = new AlertDialog.Builder(
				ChargeStationInforActivity.this).setView(view).show();

		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		baiduTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				// TODO Auto-generated method stub
				dialog = PublicUtil.showDialog(ChargeStationInforActivity.this);
				PublicUtil.showToast(ChargeStationInforActivity.this,
						"正在启动导航软件，请等待", false);
				openBaiduMap();

			}
		});
		gaodeTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				dialog = PublicUtil.showDialog(ChargeStationInforActivity.this);
				PublicUtil.showToast(ChargeStationInforActivity.this,
						"正在启动导航软件，请等待", false);
				openGaoDeMap();
			}
		});
		cancelTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
			}
		});

	}

	private List<Map<String, String>> data_adapter = new ArrayList<Map<String, String>>();

	/**
	 * 用来展示电桩列表
	 * 
	 * @param jsonArray
	 */
	public void showList(JSONArray jsonArray) {
		data_adapter.clear();

		try {
			Map<String, String> listem;
			JSONObject itemObject;
			JSONArray gunArray;
			for (int i = 0; i < jsonArray.length(); i++) {
				itemObject = jsonArray.getJSONObject(i);
				listem = new HashMap<String, String>();
				listem.put("i", String.valueOf(i));
				listem.put("id", itemObject.getString("id"));
				listem.put("code_from_compony",
						itemObject.getString("code_from_compony"));
				listem.put("DEV_NAME", itemObject.getString("DEV_NAME"));
				listem.put("charge_type", itemObject.getString("charge_type"));
				listem.put("charge_type_text",
						itemObject.getString("charge_type_text"));
				listem.put("charge_pattern",
						itemObject.getString("charge_pattern"));
				listem.put("charge_pattern_text",
						itemObject.getString("charge_pattern_text"));

				gunArray = itemObject.getJSONArray("gun_list");
				listem.put("guns", String.valueOf(gunArray.length()));
				listem.put("guns1_name",
						gunArray.getJSONObject(0).getString("gun_name"));
				listem.put("guns1_status",
						gunArray.getJSONObject(0).getString("status_code"));
				listem.put("mpn", gunArray.getJSONObject(0).getString("mpn"));
				listem.put("guns1_status_txt", gunArray.getJSONObject(0)
						.getString("status_text"));
				if (gunArray.length() > 1) {
					listem.put("guns2_name", gunArray.getJSONObject(1)
							.getString("gun_name"));
					listem.put("guns2_status", gunArray.getJSONObject(1)
							.getString("status_code"));
					listem.put("mpn2",
							gunArray.getJSONObject(1).getString("mpn"));
					listem.put("guns2_status_txt", gunArray.getJSONObject(1)
							.getString("status_text"));
				}

				data_adapter.add(listem);
			}

			ChargeGunListAdapter adapter = new ChargeGunListAdapter(
					ChargeStationInforActivity.this, chargeCallback,
					charge2Callback);
			adapter.setData(data_adapter);

			chargesListView.setAdapter(adapter);

			// chargesListView.setOnItemClickListener(new OnItemClickListener()
			// {
			//
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int arg2, long arg3) {
			// // TODO Auto-generated method stub
			//
			// final ChangeGunViewHolder viewhold = (ChangeGunViewHolder) arg1
			// .getTag();
			// final String id = viewhold.idTextView.getText().toString();
			//
			// viewhold.chargeTextView
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// String status = viewhold.statusTextView
			// .getText().toString().split("&")[0];
			// String mpn = viewhold.statusTextView
			// .getText().toString().split("&")[1];
			//
			// if ("1".equals(status) && id != null) {
			// Intent intent = new Intent(
			// ChargeStationInforActivity.this,
			// ChargeGunInforActivity.class);
			// intent.putExtra("id", id);
			// intent.putExtra("mpn", mpn);
			// startActivity(intent);
			//
			// } else {
			// PublicUtil
			// .showToast(
			// ChargeStationInforActivity.this,
			// "当前电桩无法进行充电", false);
			// }
			// }
			// });
			//
			// viewhold.charge2TextView
			// .setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// String status = viewhold.status2TextView
			// .getText().toString().split("&")[0];
			// String mpn = viewhold.status2TextView
			// .getText().toString().split("&")[1];
			// String number = viewhold.numberTextView
			// .getText().toString();
			// String name = viewhold.name2TextView
			// .getText().toString();
			// if ("1".equals(status) && id != null) {
			// Intent intent = new Intent(
			// ChargeStationInforActivity.this,
			// ChargeGunInforActivity.class);
			// intent.putExtra("id", id);
			// intent.putExtra("mpn", mpn);
			// intent.putExtra("name", number + "  "
			// + name);
			// startActivity(intent);
			// } else {
			// PublicUtil
			// .showToast(
			// ChargeStationInforActivity.this,
			// "当前电桩无法进行充电", false);
			// }
			// }
			// });
			//
			// }
			// });

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

	}

	/**
	 * 充电一点击事件
	 */
	Callback chargeCallback = new Callback() {

		@Override
		public void click(View v) {
			// TODO Auto-generated method stub

			if ("1".equals(islogin)) {
				if (v.getTag() != null) {

					Map<String, String> map = data_adapter.get(Integer
							.valueOf((String) v.getTag()));
					String status = map.get("guns1_status");
					String mpn = map.get("mpn");
					String id = map.get("id");

					if ("1".equals(status) && id != null) {
						Intent intent = new Intent(
								ChargeStationInforActivity.this,
								ChargeGunInforActivity.class);
						intent.putExtra("id", id);
						intent.putExtra("mpn", mpn);
						startActivity(intent);

					} else {
						PublicUtil.showToast(ChargeStationInforActivity.this,
								"当前电桩无法进行充电", false);
					}
				}

			} else {
				PublicUtil.showToast(ChargeStationInforActivity.this, "请登录",
						false);
				Intent intent = new Intent(ChargeStationInforActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}

		}
	};

	/**
	 * 充电二点击事件
	 */
	Callback charge2Callback = new Callback() {

		@Override
		public void click(View v) {
			// TODO Auto-generated method stub
			if ("1".equals(islogin)) {
				Map<String, String> map = data_adapter.get(Integer
						.valueOf((String) v.getTag()));
				String status = map.get("guns2_status");
				String mpn = map.get("mpn2");
				String id = map.get("id");

				if ("1".equals(status) && id != null) {
					Intent intent = new Intent(ChargeStationInforActivity.this,
							ChargeGunInforActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("mpn", mpn);
					startActivity(intent);

				} else {
					PublicUtil.showToast(ChargeStationInforActivity.this,
							"当前电桩无法进行充电", false);
				}

			} else {
				PublicUtil.showToast(ChargeStationInforActivity.this, "请登录",
						false);
				Intent intent = new Intent(ChargeStationInforActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}
		}
	};

	/**
	 * 显示电桩信息
	 * 
	 * @param itemMap
	 * @param gun
	 */

	public void showchargeinfor(Map<String, String> itemMap, int gun) {

		isAddFavorite = null;

		// 这个list是因为，地图电桩标记之后，每个电桩里面的参数实事修改比较麻烦，
		for (int i = 0; i < favoritechangeList.size(); i++) {
			Map<String, String> map = favoritechangeList.get(i);
			if (cs_id.equals(map.get("cs_id"))) {
				isAddFavorite = map.get("isAddFavorite");
				fid = map.get("fid");
				break;
			}

		}

		if (isAddFavorite == null) {
			isAddFavorite = itemMap.get("isAddFavorite");
		}
		if (fid == null) {
			fid = itemMap.get("fid");
		}

		if ("1".equals(isAddFavorite)) {
			collectionButton
					.setBackgroundResource(R.drawable.xiangqing_collected);
			infor_collectionTextView.setText("已收藏");

		} else {
			collectionButton
					.setBackgroundResource(R.drawable.xiangqing_collect);
			infor_collectionTextView.setText("收藏");
		}

	}

	/**
	 * 用来显示收费时间以及费率
	 * 
	 * @param rateObject
	 */
	private void rateShow(JSONObject rateObject) {

		List<Map<String, String>> charge_rateList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> server_rateList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> port_rateList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> port_rateList2 = new ArrayList<Map<String, String>>();
		try {
			JSONArray charge_rateArray = rateObject.getJSONArray("charge_rate");
			JSONObject chargerateObject;
			for (int i = 0; i < charge_rateArray.length(); i++) {
				chargerateObject = charge_rateArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("time", chargerateObject.getString("time"));
				map.put("rate", chargerateObject.getString("rate"));
				charge_rateList.add(map);
			}

			Map<String, String> servermap = new HashMap<String, String>();
			servermap.put("time", "");
			servermap.put("rate", rateObject.getString("server_rate"));
			server_rateList.add(servermap);

			JSONArray port_rateArray = rateObject.getJSONArray("port_rate");

			if (port_rateArray.length() > 0) {
				String freehourString = port_rateArray.getJSONObject(0)
						.getString("free_hour");
				String typeString = port_rateArray.getJSONObject(0).getString(
						"text");
				if (freehourString != null
						&& !TextUtils.isEmpty(freehourString)) {
					typeString = typeString + ":前" + freehourString
							+ "个小时内免费，超出" + freehourString + "个小时按如下费率收费";
				}

				tingchefeiinforTextView.setText(typeString);

				JSONArray port_ratelistArray = port_rateArray.getJSONObject(0)
						.getJSONArray("list");
				JSONObject portObject;
				for (int i = 0; i < port_ratelistArray.length(); i++) {
					portObject = port_ratelistArray.getJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("time", portObject.getString("time"));
					map.put("rate", portObject.getString("rate"));
					port_rateList.add(map);
				}

				if (port_rateArray.length() > 1) {

					String freehourString2 = port_rateArray.getJSONObject(1)
							.getString("free_hour");
					String typeString2 = port_rateArray.getJSONObject(1)
							.getString("text");
					if (freehourString2 != null
							&& !TextUtils.isEmpty(freehourString2)) {
						typeString2 = typeString2 + ":前" + freehourString2
								+ "个小时内免费，超出" + freehourString2 + "个小时按如下费率收费";
					}

					tingchefeiinforTextView2.setText(typeString2);

					JSONArray port_ratelistArray2 = port_rateArray
							.getJSONObject(1).getJSONArray("list");
					JSONObject portObject2;
					for (int i = 0; i < port_ratelistArray2.length(); i++) {
						portObject2 = port_ratelistArray2.getJSONObject(i);
						Map<String, String> map = new HashMap<String, String>();
						map.put("time", portObject2.getString("time"));
						map.put("rate", portObject2.getString("rate"));
						port_rateList2.add(map);
					}
				}

			} else {
				tingchefeiinfor2Layout.setVisibility(View.GONE);
			}

			SimpleAdapter adapter1 = new SimpleAdapter(
					ChargeStationInforActivity.this, charge_rateList,
					R.layout.listview_item_time2money, new String[] { "time",
							"rate" }, new int[] {
							R.id.listview_item_time2money_time,
							R.id.listview_item_time2money_money });

			dianfeiListView.setAdapter(adapter1);
			SimpleAdapter adapter2 = new SimpleAdapter(
					ChargeStationInforActivity.this, server_rateList,
					R.layout.listview_item_time2money, new String[] { "time",
							"rate" }, new int[] {
							R.id.listview_item_time2money_time,
							R.id.listview_item_time2money_money });
			fuwufeiListView.setAdapter(adapter2);

			SimpleAdapter adapter3 = new SimpleAdapter(
					ChargeStationInforActivity.this, port_rateList,
					R.layout.listview_item_time2money, new String[] { "time",
							"rate" }, new int[] {
							R.id.listview_item_time2money_time,
							R.id.listview_item_time2money_money });
			tingchefeiListView.setAdapter(adapter3);

			if (port_rateList2.size() > 0) {
				tingchefeiinfor2Layout.setVisibility(View.VISIBLE);
				SimpleAdapter adapter4 = new SimpleAdapter(
						ChargeStationInforActivity.this, port_rateList2,
						R.layout.listview_item_time2money, new String[] {
								"time", "rate" }, new int[] {
								R.id.listview_item_time2money_time,
								R.id.listview_item_time2money_money });
				tingchefeiListView2.setAdapter(adapter4);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 设置充电站图片
	 * */
	private void setImage(JSONArray array) {

		try {
			ImageLoader.getInstance().displayImage(array.getString(0),
					chargeImageView);

			imagebtmList.clear();

			for (int i = 0; i < array.length(); i++) {
				ImageLoader.getInstance().loadImage(array.getString(i),
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingComplete(String arg0,
									View arg1, Bitmap arg2) {
								// TODO Auto-generated method stub
								if (arg2 != null) {
									imagebtmList.add(arg2);

									((MyApplication) getApplication())
											.setBtmList(imagebtmList);
									imagebottomView.setText(imagebtmList.size()
											+ "");
									imagebottomView.setVisibility(View.VISIBLE);
								}

							}

							@Override
							public void onLoadingCancelled(String arg0,
									View arg1) {
								// TODO Auto-generated method stub

							}
						});

				// imagebottomView.setText(imagebtmList.size() + "");
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	/**
	 * 顶部选择按键
	 * 
	 * @author qtds
	 * 
	 */
	private class topTabClick implements OnClickListener {
		private int index = 0;

		public topTabClick(int i) {
			index = i;
		}

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			viewPager.setCurrentItem(index);
		}
	}

	/**
	 * ViewPager适配器
	 */
	private class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			// Animation animation = null;
			switch (arg0) {
			case 0:
				chooseView.setTextColor(getResources().getColor(
						R.color.menugreen));
				chooseImageView.setVisibility(View.VISIBLE);
				chooseImageView2.setVisibility(View.GONE);
				chooseView2.setTextColor(getResources().getColor(R.color.gray));
				break;
			case 1:
				chooseView2.setTextColor(getResources().getColor(
						R.color.menugreen));
				chooseImageView.setVisibility(View.GONE);
				chooseImageView2.setVisibility(View.VISIBLE);
				chooseView.setTextColor(getResources().getColor(R.color.gray));
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}
