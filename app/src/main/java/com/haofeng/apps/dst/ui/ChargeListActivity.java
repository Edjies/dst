package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.ChargeListAdapter;
import com.haofeng.apps.dst.adapter.ChargeListAdapter.ChargeViewHolder;
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

public class ChargeListActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "ChargeListActivity";
	private FrameLayout topLayout;
	private ListView listView;
	private PullToRefreshListView pullToRefreshListView;
	private ChargeListAdapter chargeListAdapter;
	private int allPage = 1, nowPage = 1;
	private TextView backTextView;
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chargelist);
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
		topLayout = (FrameLayout) findViewById(R.id.chargelist_top_layout);
		setTopLayoutPadding(topLayout);
		backTextView = (TextView) findViewById(R.id.chargelist_back);
		backTextView.setOnClickListener(this);

		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.chargelist_listview);
		View view = LayoutInflater.from(this).inflate(
				R.layout.view_listview_empty_show_congzhi, null);
		TextView emptyinforView = (TextView) view
				.findViewById(R.id.view_listview_emptyshow_text);
		emptyinforView.setText("暂无充电记录");
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
				// chargeListAdapter.notifyDataSetChanged();//
				// 这里下拉重新加载数据时，清楚原来数据的动作放在那？需要考虑
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
							PublicUtil.showToast(ChargeListActivity.this,
									"没有更多充电记录了", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}
					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();
		chargeListAdapter = new ChargeListAdapter(ChargeListActivity.this);
		listView.setAdapter(chargeListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				ChargeViewHolder viewHolder = (ChargeViewHolder) arg1.getTag();
				String fm_id = viewHolder.idTextView.getText().toString()
						.split("&")[0];
				String fm_end_id = viewHolder.idTextView.getText().toString()
						.split("&")[1];

				if (fm_id != null && fm_end_id != null) {
					// if ("success".equals(status)) {
					Intent intent = new Intent(ChargeListActivity.this,
							ChargeEndActivity.class);
					intent.putExtra("type", "list");
					intent.putExtra("fm_id", fm_id);
					intent.putExtra("fm_end_id", fm_end_id);
					intent.putExtra("phone", phone);
					startActivity(intent);
					// } else {
					// PublicUtil.showToast(ChargeListActivity.this,
					// "当前状态无法查看详情",
					// false);
					// }

				} else {
					PublicUtil.showToast(ChargeListActivity.this,
							"信息读取失败，无法查看", false);
				}

			}
		});

		getData(true);
	}

	private String phone;

	/**
	 * 获取数据
	 */
	public void getData(boolean isShowDialog) {
		if (phone == null) {
			phone = PublicUtil.getStorage_string(ChargeListActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(ChargeListActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_CHARGELIST);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);
			map.put("rows", PublicUtil.PAGESIZE);
			map.put("page", String.valueOf(nowPage));

			AnsynHttpRequest.httpRequest(ChargeListActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_CHARGELIST,
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
			case Constent.ID_CHARGELIST:
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

			default:
				break;
			}

		}
	};

	private int httprequesterror = 0x2600;
	private int httprequestsuccess = 0x2601;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == httprequesterror) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (msg.obj != null) {
					PublicUtil.showToast(ChargeListActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (jsonObject != null) {

					try {

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
							PublicUtil.showToast(ChargeListActivity.this,
									jsonObject.get("msg").toString(), false);
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

			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				object = array.getJSONObject(i);
				String money = object.getString("c_amount");
				if (money.startsWith("-")) {
					money = money.replace("-", "");

				} else if (money.startsWith("+")) {
					money = money.replace("+", "");

				}
				map.put("id",
						object.getString("fm_id") + "&"
								+ object.getString("fm_end_id"));

				map.put("time", object.getString("DEAL_START_DATE"));
				map.put("endtime", object.getString("DEAL_END_DATE"));
				map.put("addr", object.getString("cs_name"));
				map.put("number", object.getString("DEAL_NO"));
				map.put("money", money);
				map.put("status", object.getString("status"));
				map.put("status_text", object.getString("status_text"));

				PublicUtil.logDbug(TAG, map.toString(), 0);
				data.add(map);
			}
			chargeListAdapter.setData(data);
			chargeListAdapter.notifyDataSetChanged();
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
		case R.id.chargelist_back:

			finish();
			break;

		default:
			break;
		}

	}

}
