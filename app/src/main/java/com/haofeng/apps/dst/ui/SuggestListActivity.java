package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.SuggestListAdapter;
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

public class SuggestListActivity extends BaseActivity implements
		OnClickListener {
	private static String TAG = "SuggestListActivity";
	private FrameLayout topLayout;
	private TextView addTextView, backTextView;
	private ListView listView;
	private SuggestListAdapter suggestListAdapter;
	private PullToRefreshListView pullToRefreshListView;
	private String phone = null;
	private int allPage = 1, nowPage = 1;
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestlist);
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
	public void init() {
		topLayout = (FrameLayout) findViewById(R.id.suggestlist_top_layout);
		setTopLayoutPadding(topLayout);
		addTextView = (TextView) findViewById(R.id.suggestlist_add);
		backTextView = (TextView) findViewById(R.id.suggestlist_back);
		addTextView.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.suggestlist_listview);
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
							PublicUtil.showToast(SuggestListActivity.this,
									"没有更多意见建议了", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}
					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();

		suggestListAdapter = new SuggestListAdapter(SuggestListActivity.this);
		listView.setAdapter(suggestListAdapter);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		data.clear();
		getData(true);
	}

	/**
	 * 获取数据
	 */
	public void getData(boolean isShowDialog) {
		if (phone == null) {
			phone = PublicUtil.getStorage_string(SuggestListActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(SuggestListActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_GETSUGGEST);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);
			map.put("rows", PublicUtil.PAGESIZE);
			map.put("page", String.valueOf(nowPage));
			AnsynHttpRequest.httpRequest(SuggestListActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_GETSUGGEST,
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
			case Constent.ID_GETSUGGEST:
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

	private int httprequesterror = 0x1800;
	private int httprequestsuccess = 0x1801;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == httprequesterror) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (msg.obj != null) {
					PublicUtil.showToast(SuggestListActivity.this,
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
								PublicUtil.showToast(SuggestListActivity.this,
										"解析数据错误", false);
							}

						} else {
							PublicUtil.showToast(SuggestListActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(SuggestListActivity.this,
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

		try {
			if (isDownFlush) {
				isDownFlush = false;
				data.clear();
			}
			JSONObject object = null;
			int code = data.size();
			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				object = array.getJSONObject(i);
				map.put("id", object.getString("vs_id"));
				// Log.d(TAG, PublicUtil.getTime(object.getString("systime")));
				// map.put("systime",
				// PublicUtil.getTime(object.getString("systime")));
				if (code > 0) {
					map.put("code", PublicUtil.setcode(code + i + 1));
				} else {
					map.put("code", PublicUtil.setcode(i + 1));
				}

				map.put("vs_code", object.getString("vs_code"));
				map.put("vs_title", object.getString("vs_title"));
				map.put("vs_time", object.getString("vs_time"));
				map.put("vs_content", object.getString("vs_content"));
				map.put("vs_responder_id", object.getString("vs_responder_id"));
				map.put("vs_responder", object.getString("vs_responder"));
				map.put("vs_respond_time", object.getString("vs_respond_time"));
				map.put("vs_respond_txt", object.getString("vs_respond_txt"));

				PublicUtil.logDbug(TAG, map.toString(), 0);
				data.add(map);
			}

			suggestListAdapter.setData(data);
			suggestListAdapter.notifyDataSetChanged();
			pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
			// 我们已经更新完成
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.suggestlist_add:
			Intent intent = new Intent(SuggestListActivity.this,
					SuggestActivity.class);
			startActivity(intent);

			break;
		case R.id.suggestlist_back:
			intent = new Intent(SuggestListActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

}
