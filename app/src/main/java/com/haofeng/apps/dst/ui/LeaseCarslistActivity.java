package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.haofeng.apps.dst.adapter.LeaseCarListAdapter;
import com.haofeng.apps.dst.adapter.LeaseCarListAdapter.LeasrCarViewHolder;
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
 * 租赁车辆管理列表
 * 
 * @author Administrator
 * 
 */
public class LeaseCarslistActivity extends BaseActivity implements
		OnClickListener {
	private static String TAG = "LeaseCarslistActivity";
	private ListView listView;
	private PullToRefreshListView pullToRefreshListView;
	private LeaseCarListAdapter leaseCarListAdapter;
	private int allPage = 1, nowPage = 1;
	private TextView backTextView;
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carleaselist);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void init() {
		backTextView = (TextView) findViewById(R.id.leasecarlist_back);
		backTextView.setOnClickListener(this);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.leasecarlist_list);
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

				pullToRefreshListView.postDelayed(new Runnable() {//

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
							PublicUtil.showToast(LeaseCarslistActivity.this,
									"暂无更多数据", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}

					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();

		leaseCarListAdapter = new LeaseCarListAdapter(
				LeaseCarslistActivity.this);
		listView.setAdapter(leaseCarListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				LeasrCarViewHolder viewHolder = (LeasrCarViewHolder) arg1
						.getTag();
				if (viewHolder.vehicle_dentification_numberTextView.getText()
						.toString() != null) {

					Intent intent = new Intent(LeaseCarslistActivity.this,
							BaiduLeaseCarActivity.class);
					intent.putExtra("vin",
							viewHolder.vehicle_dentification_numberTextView
									.getText().toString());
					intent.putExtra("chepai", viewHolder.plate_numberTextView
							.getText().toString());
					intent.putExtra("zujin", viewHolder.month_rentTextView
							.getText().toString());
					intent.putExtra("image", viewHolder.idTextView.getText()
							.toString().split("&")[1]);
					startActivity(intent);
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
			phone = PublicUtil.getStorage_string(LeaseCarslistActivity.this,
					"phone", "-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(LeaseCarslistActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_GETLEASECARLIST);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);
			map.put("rows", PublicUtil.PAGESIZE);
			map.put("page", String.valueOf(nowPage));

			AnsynHttpRequest
					.httpRequest(LeaseCarslistActivity.this,
							AnsynHttpRequest.POST, callBack,
							Constent.ID_GETLEASECARLIST, map, false,
							isShowDialog, true);
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
			case Constent.ID_GETLEASECARLIST:
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

	private int httprequesterror = 0x3800;
	private int httprequestsuccess = 0x3801;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == httprequesterror) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (msg.obj != null) {
					PublicUtil.showToast(LeaseCarslistActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (jsonObject != null) {

					try {
						// JSONObject jsonObject =
						// backArray.getJSONObject(0);
						if ("0".equals(jsonObject.get("error").toString())) {

							if (jsonObject.get("total").toString() != null) {
								allPage = PublicUtil.getAllPage(jsonObject.get(
										"total").toString());
							}

							PublicUtil.logDbug(TAG,
									jsonObject.get("data") + "", 0);
							JSONArray array = (JSONArray) jsonObject
									.get("data");

							if (array != null && array.length() > 0) {
								flushList(array);
							} else {
								PublicUtil.showToast(
										LeaseCarslistActivity.this, "解析数据错误",
										false);
							}

						} else {
							PublicUtil.showToast(LeaseCarslistActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(LeaseCarslistActivity.this,
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

		// [{"systime":"1448440525","id":"2","install_site":null,"is_del":"0","chargerid":"0","mark":"","code_from_compony":null,"connection_type":null,"mobile":"18782253660"}]

		try {
			if (isDownFlush) {
				isDownFlush = false;
				data.clear();
			}
			int code = data.size();
			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				object = array.getJSONObject(i);
				map.put("id",
						object.getString("id") + "&"
								+ object.getString("car_brand"));
				if (code > 0) {
					map.put("code", String.valueOf(code + i + 1));
				} else {
					map.put("code", String.valueOf(i + 1));
				}
				map.put("let_time",
						PublicUtil.getTime2(object.getString("let_time")));
				map.put("month_rent", object.getString("month_rent"));
				map.put("plate_number", object.getString("plate_number"));
				map.put("vehicle_dentification_number",
						object.getString("vehicle_dentification_number"));
				map.put("car_brand", object.getString("car_brand"));
				map.put("car_brand_text", object.getString("car_brand_text"));
				PublicUtil.logDbug(TAG, map.toString(), 0);
				data.add(map);
			}
			leaseCarListAdapter.setData(data);
			leaseCarListAdapter.notifyDataSetChanged();
			pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
			// 我们已经更新完成

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	String vin;

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.leasecarlist_back:
			finish();
			break;

		default:
			break;
		}

	}

}
