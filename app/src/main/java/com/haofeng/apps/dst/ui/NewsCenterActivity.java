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
import com.haofeng.apps.dst.adapter.NewsCenterListAdapter;
import com.haofeng.apps.dst.adapter.NewsCenterListAdapter.Callback;
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

/**
 * 消息中心
 * 
 * @author Administrator
 * 
 */
public class NewsCenterActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "NewsCenterActivity";
	private ListView listView;
	private NewsCenterListAdapter newsCenterListAdapter;
	private FrameLayout topLayout;
	private PullToRefreshListView pullToRefreshListView;
	private TextView backTextView;
	private int allPage = 1, nowPage = 1;
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newcenter);
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

	private void init() {
		topLayout = (FrameLayout) findViewById(R.id.newscenter_top_layout);
		setTopLayoutPadding(topLayout);
		backTextView = (TextView) findViewById(R.id.newscenter_back);
		backTextView.setOnClickListener(this);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.newscenter_listview);
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_listview_empty_show_congzhi, null);
		TextView emptyinforView = (TextView) view
				.findViewById(R.id.view_listview_emptyshow_text);
		emptyinforView.setText("暂无消息");
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
							PublicUtil.showToast(NewsCenterActivity.this,
									"没有更多消息了", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}
					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();
		newsCenterListAdapter = new NewsCenterListAdapter(
				NewsCenterActivity.this, deletCallback, inforCallback);
		listView.setAdapter(newsCenterListAdapter);

		//getData(true);

	}

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
				final String deletId = data.get(i).get("id");
				if (deletId != null) {

					View view = LayoutInflater.from(NewsCenterActivity.this)
							.inflate(R.layout.view_delet_dialog, null);
					TextView title = (TextView) view
							.findViewById(R.id.view_dialog_title);
					TextView ok = (TextView) view
							.findViewById(R.id.view_dialog_ok);
					TextView cancel = (TextView) view
							.findViewById(R.id.view_dialog_cancel);
					title.setText("是否删除当前通知消息？");

					final AlertDialog alertDialog = new AlertDialog.Builder(
							NewsCenterActivity.this, R.style.dialog_nostroke)
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
							map.put("act", Constent.ACT_MEMBER_OPER_MESSAGE);
							map.put("type", "1");
							map.put("id", deletId);

							AnsynHttpRequest.httpRequest(
									NewsCenterActivity.this,
									AnsynHttpRequest.GET, callBack,
									Constent.ID_MEMBER_OPER_MESSAGE, map,
									false, true, true);
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
	 * 点击事件
	 */
	private Callback inforCallback = new Callback() {

		@SuppressLint("NewApi")
		@Override
		public void click(View v) {
			// TODO Auto-generated method stub

			// PublicUtil.logDbug(TAG, "@@@@@2222222222222" + v.getTag(), 0);
			// if (v.getTag() != null) {
			//
			// int i = Integer.parseInt((String) v.getTag());
			// String deletId = data.get(i).get("id");
			// if (deletId != null) {
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("act", Constent.ACT_MEMBER_OPER_MESSAGE);
			// map.put("ver", Constent.VER);
			// map.put("type", "1");
			// map.put("id", deletId);
			//
			// AnsynHttpRequest.httpRequest(NewsCenterActivity.this,
			// AnsynHttpRequest.GET, callBack,
			// Constent.ID_MEMBER_OPER_MESSAGE_READ, map, false,
			// true, true);
			// }

			// }
		}
	};

	/**
	 * 获取数据
	 */
	public void getData(boolean isShowDialog) {

		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_MEMBER_MESSAGE_LIST);
		map.put("ver", Constent.VER);
		map.put("rows", PublicUtil.PAGESIZE);
		map.put("page", String.valueOf(nowPage));

		AnsynHttpRequest
				.httpRequest(NewsCenterActivity.this, AnsynHttpRequest.GET,
						callBack, Constent.ID_MEMBER_MESSAGE_LIST, map, false,
						isShowDialog, true);

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
			case Constent.ID_MEMBER_MESSAGE_LIST:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(success_httprequest_getlist);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = error_httprequest_getlist;
						message.obj = data;
						handler.sendMessage(message);
					}
				}
				break;
			case Constent.ID_MEMBER_OPER_MESSAGE:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(success_httprequest_delet);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = error_httprequest_delet;
						message.obj = data;
						handler.sendMessage(message);
					}
				}
				break;

			case Constent.ID_MEMBER_OPER_MESSAGE_READ:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(success_httprequest_read);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				} else {
					if (handler != null) {
						Message message = new Message();
						message.what = error_httprequest_read;
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

	private int error_httprequest_getlist = 0x7000;
	private int success_httprequest_getlist = 0x7001;
	private int error_httprequest_delet = 0x7002;
	private int success_httprequest_delet = 0x7003;
	private int error_httprequest_read = 0x7004;
	private int success_httprequest_read = 0x7005;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == error_httprequest_getlist) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (msg.obj != null) {
					PublicUtil.showToast(NewsCenterActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest_getlist) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (jsonObject != null) {

					try {
						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("errcode").toString())) {

							JSONObject dataJsonObject = jsonObject
									.getJSONObject("data");
							if (dataJsonObject != null) {
								allPage = PublicUtil.getAllPage(dataJsonObject
										.getString("total"));
								JSONArray array = dataJsonObject
										.getJSONArray("list");

								if (array != null && array.length() > 0) {
									flushList(array);
								}

							}

						} else {
							PublicUtil.showToast(NewsCenterActivity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {

								Intent intent = new Intent(
										NewsCenterActivity.this,
										LoginActivity.class);
								startActivity(intent);

							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			} else if (msg != null && msg.what == error_httprequest_delet) {
				if (msg.obj != null) {
					PublicUtil.showToast(NewsCenterActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest_delet) {
				if (jsonObject != null) {

					try {

						if ("0".equals(jsonObject.get("errcode").toString())) {
							PublicUtil.showToast(NewsCenterActivity.this,
									"删除成功", false);
							if (data.size() > 1) {
								isDownFlush = true;
								nowPage = 1;
								getData(false);
							} else {
								data.clear();
								newsCenterListAdapter.notifyDataSetChanged();
							}

						} else {
							PublicUtil.showToast(NewsCenterActivity.this,
									jsonObject.getString("msg"), false);
							if ("1002".equals(jsonObject.getString("errcode"))) {

								Intent intent = new Intent(
										NewsCenterActivity.this,
										LoginActivity.class);
								startActivity(intent);

							}

						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}

			} else if (msg != null && msg.what == error_httprequest_read) {
				if (msg.obj != null) {
					PublicUtil.showToast(NewsCenterActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == success_httprequest_read) {
				if (jsonObject != null) {

					try {
						PublicUtil.showToast(NewsCenterActivity.this,
								jsonObject.get("msg").toString(), false);
						if ("0".equals(jsonObject.get("error").toString())) {

						} else {
							PublicUtil.showToast(NewsCenterActivity.this,
									jsonObject.getString("msg"), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
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
				map.put("i", String.valueOf(nowcount + i));
				map.put("id", object.getString("id"));
				map.put("content", object.getString("content"));
				map.put("time", object.getString("add_time"));
				map.put("status", object.getString("is_read"));
				map.put("status", object.getString("type"));

				PublicUtil.logDbug(TAG, map.toString(), 0);
				data.add(map);
			}
			newsCenterListAdapter.setData(data);
			newsCenterListAdapter.notifyDataSetChanged();
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
			((MyApplication) getApplication()).setSelectiontab(3);
			Intent intent = new Intent(NewsCenterActivity.this,
					MainActivity.class);
			startActivity(intent);
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
		case R.id.newscenter_back:
			((MyApplication) getApplication()).setSelectiontab(3);
			Intent intent = new Intent(NewsCenterActivity.this,
					MainActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

}
