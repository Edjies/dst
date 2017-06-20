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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.ShareListAdapter;
import com.haofeng.apps.dst.adapter.ShareListAdapter.SharelistViewHolder;
import com.haofeng.apps.dst.application.MyApplication;
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

public class ShareListActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "ShareListActivity";
	private TextView addshareTextView, backTextView;
	private ListView listView;
	private PullToRefreshListView pullToRefreshListView;
	private ShareListAdapter shareListAdapter;
	private int allPage = 1, nowPage = 1;
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sharelist);
		((MyApplication) getApplication()).addActivity(this);
//		 PushAgent.getInstance(this).onAppStart();
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init() {

		addshareTextView = (TextView) findViewById(R.id.share_sharelisttextview);
		backTextView = (TextView) findViewById(R.id.sharelist_back);
		addshareTextView.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.sharelist_listview);
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_listview_empty_show_congzhi, null);
		pullToRefreshListView.setEmptyView(view);
		pullToRefreshListView.setMode(Mode.BOTH);
		ILoadingLayout startLabels = pullToRefreshListView
				.getLoadingLayoutProxy(true, false);

		startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
		startLabels.setRefreshingLabel("正在刷新...");// 刷新时
		startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

		ILoadingLayout endLabels = pullToRefreshListView.getLoadingLayoutProxy(
				false, true);

		endLabels.setPullLabel("上拉加载更多...");// 刚上拉时，显示的提示
		endLabels.setRefreshingLabel("正在加载...");// 刷新时
		endLabels.setReleaseLabel("放开加载...");// 上拉达到一定距离时，显示的提示
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
							PublicUtil.showToast(ShareListActivity.this,
									"没有更多分享记录了", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}

					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();
		shareListAdapter = new ShareListAdapter(ShareListActivity.this);
		listView.setAdapter(shareListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				SharelistViewHolder holder = (SharelistViewHolder) arg1
						.getTag();
				deletId = holder.idTextView.getText().toString();

				holder.deleteView.setOnClickListener(new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (deletId != null) {

							View view = LayoutInflater.from(
									ShareListActivity.this).inflate(
									R.layout.view_delet_dialog, null);
							TextView title = (TextView) view
									.findViewById(R.id.view_dialog_title);
							TextView ok = (TextView) view
									.findViewById(R.id.view_dialog_ok);
							TextView cancel = (TextView) view
									.findViewById(R.id.view_dialog_cancel);
							title.setText("是否删除当前分享信息？");

							final AlertDialog alertDialog = new AlertDialog.Builder(
									ShareListActivity.this,
									R.style.dialog_nostroke).show();
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
									map.put("act", Constent.ACT_SHAREREMOVE);
									map.put("sid", deletId);
									map.put("ver", Constent.VER);

									AnsynHttpRequest.httpRequest(
											ShareListActivity.this,
											AnsynHttpRequest.POST, callBack,
											Constent.ID_SHAREREMOVE, map,
											false, false, true);
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
				});

			}
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		data.clear();
		getData(true);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.share_sharelisttextview:

			intent = new Intent(ShareListActivity.this, ShareActivity.class);
			startActivity(intent);

			break;
		case R.id.sharelist_back:

			intent = new Intent(ShareListActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

	private String phone, deletId;

	/**
	 * 获取数据
	 */
	public void getData(boolean isShowDialog) {
		if (phone == null) {
			phone = PublicUtil.getStorage_string(ShareListActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(ShareListActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_SHARELIST);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);
			map.put("rows", PublicUtil.PAGESIZE);
			map.put("page", String.valueOf(nowPage));

			AnsynHttpRequest.httpRequest(ShareListActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_SHARELIST,
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
			case Constent.ID_SHARELIST:
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
			case Constent.ID_SHAREREMOVE:
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

	private int httprequesterror = 0x2500;
	private int httprequestsuccess = 0x2501;
	private int httprequestsuccess_delete = 0x2502;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == httprequesterror) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (msg.obj != null) {
					PublicUtil.showToast(ShareListActivity.this,
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

							if (array != null && array.length() > 0) {
								flushList(array);
							} else {
								PublicUtil.showToast(ShareListActivity.this,
										"解析数据错误", false);
							}

						} else {
							PublicUtil.showToast(ShareListActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(ShareListActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			} else if (msg != null && msg.what == httprequestsuccess_delete) {
				if (jsonObject != null) {

					try {
						PublicUtil.showToast(ShareListActivity.this, jsonObject
								.get("msg").toString(), false);
						if ("0".equals(jsonObject.get("error").toString())) {
							Map<String, String> map;
							for (int i = 0; i < data.size(); i++) {
								map = data.get(i);
								if (deletId.equals(map.get("id"))) {
									data.remove(i);
									shareListAdapter.setData(data);
									shareListAdapter.notifyDataSetChanged();
									break;

								}

							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(ShareListActivity.this,
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
			int code = data.size();
			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				object = array.getJSONObject(i);
				map.put("id", object.getString("id"));
				if (code > 0) {
					map.put("code", PublicUtil.setcode(code + i + 1));
				} else {
					map.put("code", PublicUtil.setcode(i + 1));
				}
				map.put("share_time", object.getString("share_time"));
				map.put("code_from_factory",
						object.getString("code_from_factory"));
				map.put("specification", object.getString("specification"));
				map.put("wire_length", object.getString("wire_length"));
				map.put("charge_gun_nums", object.getString("charge_gun_nums"));
				map.put("install_type_txt",
						object.getString("install_type_txt"));
				map.put("manufacturer_txt",
						object.getString("manufacturer_txt"));
				map.put("model_txt", object.getString("model_txt"));
				map.put("charge_type_txt", object.getString("charge_type_txt"));
				map.put("connection_type_txt",
						object.getString("connection_type_txt"));
				map.put("charge_type", object.getString("charge_type"));
				map.put("connection_type", object.getString("connection_type"));
				map.put("install_site", object.getString("install_site"));
				map.put("approve_status", object.getString("approve_status"));
				map.put("approve_time", object.getString("approve_time"));
				map.put("approve_mark", object.getString("approve_mark"));

				PublicUtil.logDbug(TAG, map.toString(), 0);
				data.add(map);
			}
			shareListAdapter.setData(data);
			shareListAdapter.notifyDataSetChanged();
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
			Intent intent = new Intent(ShareListActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

}
