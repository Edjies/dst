package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.CollectionListAdapter;
import com.haofeng.apps.dst.adapter.CollectionListAdapter.Callback;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 我的收藏界面，显示收藏的充电桩
 * 
 * @author Administrator
 * 
 */
public class CollectionActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "CollectionActivity";
	private ListView listView;
	private CollectionListAdapter collectionListAdapter;
	private String phone, deletId;
	private PullToRefreshListView pullToRefreshListView;
	private TextView backTextView;
	private FrameLayout topLayout;
	private int allPage = 1, nowPage = 1;
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void init() {
		topLayout = (FrameLayout) findViewById(R.id.collection_top_layout);
		setTopLayoutPadding(topLayout);

		backTextView = (TextView) findViewById(R.id.collection_back);
		backTextView.setOnClickListener(this);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.collection_listview);
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_listview_empty_show_congzhi, null);
		TextView emptyinforView = (TextView) view
				.findViewById(R.id.view_listview_emptyshow_text);
		emptyinforView.setText("暂无收藏");
		pullToRefreshListView.setEmptyView(view);
		pullToRefreshListView.setMode(Mode.BOTH);
		ILoadingLayout startLabels = pullToRefreshListView
				.getLoadingLayoutProxy(true, false);
		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = pullToRefreshListView.getLoadingLayoutProxy(
				false, true);
		endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
		endLabels.setRefreshingLabel("正在加载...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				// TODO Auto-generated method stub

				isDownFlush = true;
				nowPage = 1;

				pullToRefreshListView.postDelayed(new Runnable() {// 如果获取数据太快，正在加载的提示不消失，所以延时1秒

							@Override
							public void run() {
								// TODO Auto-generated method stub
								getData(false);
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
							getData(false);
						} else {
							PublicUtil.showToast(CollectionActivity.this,
									"没有更多收藏了", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}
					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();
		collectionListAdapter = new CollectionListAdapter(
				CollectionActivity.this, inforCallback, deletCallback);
		listView.setAdapter(collectionListAdapter);

		getData(true);

	}

	/**
	 * 查看电站详情
	 */
	private Callback inforCallback = new Callback() {

		@Override
		public void click(View v) {
			// TODO Auto-generated method stub

			if (v.getTag() != null) {

				int i = Integer.parseInt((String) v.getTag());
				Intent intent = new Intent(CollectionActivity.this,
						ChargeStationInforActivity.class);
				intent.putExtra("cs_id", data.get(i).get("cs_id"));
				intent.putExtra("cs_name", data.get(i).get("cs_name"));
				intent.putExtra("cs_address",
						data.get(i).get("cs_address_street"));
				intent.putExtra("cs_lat", data.get(i).get("cs_lat"));
				intent.putExtra("cs_type", data.get(i).get("cs_type"));
				intent.putExtra("cs_lng", data.get(i).get("cs_lng"));
				startActivity(intent);

			}

		}
	};

	/**
	 * 删除按钮事件
	 */
	private Callback deletCallback = new Callback() {

		@SuppressLint("NewApi")
		@Override
		public void click(View v) {
			// TODO Auto-generated method stub

			PublicUtil.logDbug(TAG, "@@@@@" + v.getTag(), 0);
			if (v.getTag() != null) {

				int i = Integer.parseInt((String) v.getTag());
				deletId = data.get(i).get("id");
				if (deletId != null) {

					View view = LayoutInflater.from(CollectionActivity.this)
							.inflate(R.layout.view_delet_dialog, null);
					TextView title = (TextView) view
							.findViewById(R.id.view_dialog_title);
					TextView ok = (TextView) view
							.findViewById(R.id.view_dialog_ok);
					TextView cancel = (TextView) view
							.findViewById(R.id.view_dialog_cancel);
					title.setText("是否删除当前收藏记录？");

					final AlertDialog alertDialog = new AlertDialog.Builder(
							CollectionActivity.this, R.style.dialog_nostroke)
							.show();
					alertDialog.addContentView(view, new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					alertDialog.setCancelable(true);
					alertDialog.setCanceledOnTouchOutside(true);

					ok.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (alertDialog != null) {
								alertDialog.dismiss();
							}
							Map<String, String> map = new HashMap<String, String>();
							map.put("act", Constent.ACT_REMOVECOLLECTION);
							map.put("fid", deletId);
							map.put("ver", Constent.VER);
							map.put("mobile", phone);

							AnsynHttpRequest.httpRequest(
									CollectionActivity.this,
									AnsynHttpRequest.POST, callBack,
									Constent.ID_REMOVECOLLECTION, map, false,
									true, true);
						}
					});

					cancel.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							if (alertDialog != null) {
								alertDialog.dismiss();
							}
						}
					});

				}

			}
		}
	};

	/**
	 * 获取数据
	 */
	public void getData(boolean isShowDialog) {
		if (phone == null) {
			phone = PublicUtil.getStorage_string(CollectionActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(CollectionActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_GETCOLLECTION);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);
			map.put("rows", PublicUtil.PAGESIZE);
			map.put("page", String.valueOf(nowPage));

			AnsynHttpRequest.httpRequest(CollectionActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_GETCOLLECTION,
					map, false, isShowDialog, true);
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
			case Constent.ID_GETCOLLECTION:
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
								message.obj = "网络异常";
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
			case Constent.ID_REMOVECOLLECTION:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess_delete);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (handler != null) {
								Message message = new Message();
								message.what = httprequesterror;
								message.obj = "网络异常";
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

	private int httprequesterror = 0x1500;
	private int httprequestsuccess = 0x1501;
	private int httprequestsuccess_delete = 0x1502;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == httprequesterror) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (msg.obj != null) {
					PublicUtil.showToast(CollectionActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (jsonObject != null) {

					try {
						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {
							PublicUtil.logDbug(TAG,
									jsonObject.get("data") + "", 0);
							if (jsonObject.get("total").toString() != null) {
								allPage = PublicUtil.getAllPage(jsonObject.get(
										"total").toString());

							}
							JSONArray array = (JSONArray) jsonObject
									.get("data");

							if (array != null) {
								flushList(array);
							}

						} else {
							PublicUtil.showToast(CollectionActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			} else if (msg != null && msg.what == httprequestsuccess_delete) {
				if (jsonObject != null) {

					try {
						PublicUtil.showToast(CollectionActivity.this,
								jsonObject.get("msg").toString(), false);
						if ("0".equals(jsonObject.get("error").toString())) {

							if (data.size() > 1) {
								isDownFlush = true;
								nowPage = 1;
								getData(false);
							} else {
								data.clear();
								collectionListAdapter.notifyDataSetChanged();
							}

							// Map<String, String> map;
							// for (int i = 0; i < data.size(); i++) {
							// map = data.get(i);
							// if (deletId.equals(map.get("id").split("&")[0]))
							// {
							// data.remove(i);
							// collectionListAdapter.setData(data, isEdit);
							// collectionListAdapter
							// .notifyDataSetChanged();
							// if (data.size() == 0) {
							// editTextView.setVisibility(View.GONE);
							// }
							// break;
							//
							// }
							//
							// }

						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(CollectionActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	/**
	 * 刷新列表
	 * 
	 * @param array
	 */
	public void flushList(JSONArray array) {
		JSONObject object;

		try {
			if (isDownFlush) {
				isDownFlush = false;
				data.clear();
			}
			int nowcount = data.size();
			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				object = array.getJSONObject(i);
				map.put("i", String.valueOf(i + nowcount));
				map.put("id", object.getString("id"));

				map.put("systime",
						PublicUtil.getTime(object.getString("systime")));
				map.put("cs_code", object.getString("cs_code"));
				map.put("cs_name", object.getString("cs_name"));
				map.put("cs_type", object.getString("cs_type"));
				map.put("cs_id", object.getString("cs_id"));
				map.put("cs_lat", object.getString("cs_lat"));
				map.put("cs_lng", object.getString("cs_lng"));
				map.put("cs_address_street", object.getString("cs_address"));

				PublicUtil.logDbug(TAG, map.toString(), 0);
				data.add(map);
			}
			collectionListAdapter.setData(data);
			collectionListAdapter.notifyDataSetChanged();
			pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
			// 我们已经更新完成

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.collection_back:
			finish();
			break;

		default:
			break;
		}

	}

}
