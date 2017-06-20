package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.MyorderListAdapter;
import com.haofeng.apps.dst.adapter.MyorderListAdapter.MyorderViewHolder;
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
 * 我的预约（支持一个预约还是多个预约）
 * 
 * @author Administrator
 * 
 */
public class MyOrderActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "MyOrderActivity";
	private ListView listView;
	private MyorderListAdapter myorderListAdapter;
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private String phone, caid;
	private PullToRefreshListView pullToRefreshListView;
	private TextView backTextView, ediTextView;
	private int allPage = 1, nowPage = 1;
	private boolean isEdit = false;// 是否处于编辑状态
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myorder);
		((MyApplication) getApplication()).addActivity(this);
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
	public void init() {
		backTextView = (TextView) findViewById(R.id.myorder_back);
		ediTextView = (TextView) findViewById(R.id.myorder_edit);
		backTextView.setOnClickListener(this);
		ediTextView.setOnClickListener(this);
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.myorder_listview);
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
							PublicUtil.showToast(MyOrderActivity.this,
									"没有更多预约信息了", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}
					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();

		myorderListAdapter = new MyorderListAdapter(MyOrderActivity.this);
		listView.setAdapter(myorderListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				final MyorderViewHolder viewhold = (MyorderViewHolder) arg1
						.getTag();
				final String idinfor = viewhold.idTextView.getText().toString();
				caid = idinfor.split("&")[0];
				// 删除
				viewhold.deleteTextView
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if (caid != null) {

									new AlertDialog.Builder(
											MyOrderActivity.this)
											.setTitle("是否取消当前预约？")
											.setNegativeButton(
													"是",
													new DialogInterface.OnClickListener() {

														public void onClick(
																DialogInterface arg0,
																int arg1) {
															// TODO
															// Auto-generated
															// method stub
															Map<String, String> map = new HashMap<String, String>();
															map.put("act",
																	Constent.ACT_DELETEORDER);
															map.put("caid",
																	caid);
															map.put("ver",
																	Constent.VER);
															AnsynHttpRequest
																	.httpRequest(
																			MyOrderActivity.this,
																			AnsynHttpRequest.POST,
																			callBack,
																			Constent.ID_DELETEORDER,
																			map,
																			false,
																			true,
																			true);
														}
													})
											.setPositiveButton("否", null)
											.show();

								}

							}
						});
				// 修改
				viewhold.updateTextView
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub

								Intent intent = new Intent(
										MyOrderActivity.this,
										OrderActivity.class);
								intent.putExtra("type", "update");
								intent.putExtra("caid", caid);
								intent.putExtra("id", idinfor.split("&")[1]);
								intent.putExtra("connection_type",
										idinfor.split("&")[2]);
								intent.putExtra("time_start",
										idinfor.split("&")[3]);
								intent.putExtra("time_end",
										idinfor.split("&")[4]);
								intent.putExtra("mark", idinfor.split("&")[5]);
								intent.putExtra("appointed_date",
										idinfor.split("&")[6]);
								startActivity(intent);

							}
						});

			}
		});

		getData(true);

	}

	/**
	 * 获取数据
	 */
	public void getData(boolean isShowDialog) {
		if (phone == null) {
			phone = PublicUtil.getStorage_string(MyOrderActivity.this, "phone",
					"-1");
		}
		if ("-1".equals(phone)) {

			PublicUtil.showToast(MyOrderActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_MYORDER);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);
			map.put("rows", PublicUtil.PAGESIZE);
			map.put("page", String.valueOf(nowPage));

			AnsynHttpRequest.httpRequest(MyOrderActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_MYORDER, map,
					false, isShowDialog, true);
		}

	}

	private JSONObject jsonObject = null;

	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@Override
		public void back(int backId, boolean isRequestSuccess,
				boolean isString, String data, JSONArray jsonArray) {
			// TODO Auto-generated method stub
			switch (backId) {
			case Constent.ID_MYORDER:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							if (handler != null) {
								handler.sendEmptyMessage(httprequestsuccess);
							}

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
			case Constent.ID_DELETEORDER:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							if (handler != null) {
								handler.sendEmptyMessage(httprequestsuccess_deldte);
							}

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

	private int httprequesterror = 0x1100;
	private int httprequestsuccess = 0x1101;
	private int httprequestsuccess_deldte = 0x1102;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (msg.obj != null) {
					PublicUtil.showToast(MyOrderActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				if (jsonObject != null) {

					try {

						if ("0".equals(jsonObject.get("error").toString())) {
							JSONArray array = (JSONArray) jsonObject
									.get("data");
							if (jsonObject.get("total").toString() != null) {
								allPage = PublicUtil.getAllPage(jsonObject.get(
										"total").toString());
							}

							if (array != null && array.length() > 0) {
								flushList(array);
							} else {
								PublicUtil.showToast(MyOrderActivity.this,
										"解析数据错误", false);
							}

						} else {
							PublicUtil.showToast(MyOrderActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(MyOrderActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			} else if (msg != null && msg.what == httprequestsuccess_deldte) {
				if (jsonObject != null) {

					try {

						if ("0".equals(jsonObject.get("error").toString())) {

							Map<String, String> map;
							for (int i = 0; i < data.size(); i++) {
								map = data.get(i);
								if (caid.equals(map.get("id").split("&")[0])) {
									data.remove(i);
									myorderListAdapter.setData(data, isEdit);
									myorderListAdapter.notifyDataSetChanged();
									break;

								}

							}

						} else {
							PublicUtil.showToast(MyOrderActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(MyOrderActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

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
			JSONObject object;
			int code = data.size();
			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				object = array.getJSONObject(i);
				map.put("id",
						object.getString("id") + "&"
								+ object.getString("chargerid") + "&"
								+ object.getString("connection_type") + "&"
								+ object.getString("time_start") + "&"
								+ object.getString("time_end") + "&"
								+ object.getString("mark") + "&"
								+ object.getString("appointed_date"));

				if (code > 0) {
					map.put("code", PublicUtil.setcode(code + i + 1));
				} else {
					map.put("code", PublicUtil.setcode(i + 1));
				}
				map.put("appointed_date", object.getString("appointed_date"));
				map.put("time_start", object.getString("time_start"));
				map.put("time_end", object.getString("time_end"));
				map.put("code_from_compony",
						object.getString("code_from_compony"));
				map.put("install_site", object.getString("install_site"));
				map.put("connection_type_txt",
						object.getString("connection_type_txt"));
				map.put("connection_type", object.getString("connection_type"));
				map.put("connection_type", object.getString("connection_type"));
				map.put("isfinished", object.getString("isfinished"));
				map.put("systime",
						PublicUtil.getTime(object.getString("systime")));

				PublicUtil.logDbug(TAG, map.toString(), 0);
				data.add(map);
			}
			myorderListAdapter.setData(data, isEdit);
			myorderListAdapter.notifyDataSetChanged();
			pullToRefreshListView.onRefreshComplete();//
			// 通知RefreshListView
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
		case R.id.myorder_back:
			finish();
			break;
		case R.id.myorder_edit:
			if (data.size() > 0) {
				if ("编辑".equals(ediTextView.getText().toString())) {
					ediTextView.setText("取消");
					isEdit = true;

				} else {
					ediTextView.setText("编辑");
					isEdit = false;
				}
				myorderListAdapter.setData(data, isEdit);
				myorderListAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}

	}

}
