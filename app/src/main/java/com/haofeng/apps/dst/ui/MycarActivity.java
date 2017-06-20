package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.MycarListAdapter;
import com.haofeng.apps.dst.adapter.MycarListAdapter.Callback;
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
 * 管理车辆信息入口
 * 
 * @author Administrator
 * 
 */
public class MycarActivity extends BaseActivity implements OnClickListener {
	private static String TAG = "MycarActivity";

	private TextView editTextView, backTextView;
	private Button addButton;
	private ListView listView;
	private MycarListAdapter mycarListAdapter;
	private String phone = null, deletId = null;
	private PullToRefreshListView pullToRefreshListView;
	private boolean isEdit = false;// 收否允许编辑
	private int allPage = 1, nowPage = 1;
	private boolean isDownFlush = false;// 是否是下拉刷新

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycar);
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
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.mycar_listview);
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
							PublicUtil.showToast(MycarActivity.this,
									"没有更多车辆信息了", false);
							pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
							// 我们已经更新完成
						}
					}
				}, 1000);

			}
		});

		listView = pullToRefreshListView.getRefreshableView();
		backTextView = (TextView) findViewById(R.id.mycar_backtextview);
		editTextView = (TextView) findViewById(R.id.mycar_update);
		addButton = (Button) findViewById(R.id.mycar_addbutton);
		editTextView.setVisibility(View.GONE);
		addButton.setOnClickListener(this);
		backTextView.setOnClickListener(this);
		editTextView.setOnClickListener(this);

		mycarListAdapter = new MycarListAdapter(MycarActivity.this,
				deletCallback, updateCallback);
		listView.setAdapter(mycarListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});

		getData(true);

	}

	/**
	 * 修改点击事件
	 */
	private Callback updateCallback = new Callback() {

		@Override
		public void click(View v) {
			// TODO Auto-generated method stub
			if (v.getTag() != null) {
				int i = Integer.parseInt((String) v.getTag());

				// TODO Auto-generated method stub

				Intent intent = new Intent(MycarActivity.this,
						CarUpdateActivity.class);
				intent.putExtra("type", "update");
				intent.putExtra("id", dataList.get(i).get("id"));
				intent.putExtra("vehicle", dataList.get(i).get("vehicle"));
				intent.putExtra("model", dataList.get(i).get("vhc_model"));
				intent.putExtra("contype", dataList.get(i).get("vhc_con_type"));
				intent.putExtra("contype_text",
						dataList.get(i).get("vhc_con_type_txt"));
				startActivityForResult(intent, 1);

			}

		}
	};

	/**
	 * 删除点击事件
	 */
	private Callback deletCallback = new Callback() {

		@SuppressLint("NewApi")
		@Override
		public void click(View v) {
			// TODO Auto-generated method stub
			if (v.getTag() != null) {
				int i = Integer.parseInt((String) v.getTag());

				// TODO Auto-generated method stub
				deletId = dataList.get(i).get("id");
				if (deletId != null) {

					View view = LayoutInflater.from(MycarActivity.this)
							.inflate(R.layout.view_delet_dialog, null);
					TextView title = (TextView) view
							.findViewById(R.id.view_dialog_title);
					TextView ok = (TextView) view
							.findViewById(R.id.view_dialog_ok);
					TextView cancel = (TextView) view
							.findViewById(R.id.view_dialog_cancel);
					title.setText("是否删除车辆信息？");

					final AlertDialog alertDialog = new AlertDialog.Builder(
							MycarActivity.this, R.style.dialog_nostroke).show();
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
							map.put("act", Constent.ACT_CARDELETE);
							map.put("mobile", phone);
							map.put("vhcid", deletId);
							map.put("ver", Constent.VER);

							AnsynHttpRequest.httpRequest(MycarActivity.this,
									AnsynHttpRequest.POST, callBack,
									Constent.ID_CARDELETE, map, false, true,
									true);
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
			phone = PublicUtil.getStorage_string(MycarActivity.this, "phone",
					"-1");
		}

		if ("-1".equals(phone)) {

			PublicUtil.showToast(MycarActivity.this,
					"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

		} else {
			Map<String, String> map = new HashMap<String, String>();
			map.put("act", Constent.ACT_GETCAR);
			map.put("mobile", phone);
			map.put("ver", Constent.VER);
			map.put("rows", PublicUtil.PAGESIZE);
			map.put("page", String.valueOf(nowPage));

			AnsynHttpRequest.httpRequest(MycarActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_GETCAR, map,
					false, isShowDialog, true);
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
			case Constent.ID_GETCAR:
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
			case Constent.ID_CARDELETE:
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

	private int httprequesterror = 0x2200;
	private int httprequestsuccess = 0x2201;
	private int httprequestsuccess_delete = 0x2202;

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null && msg.what == httprequesterror) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				// 我们已经更新完成
				if (msg.obj != null) {
					PublicUtil.showToast(MycarActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				pullToRefreshListView.onRefreshComplete();// 通知RefreshListView
				// 我们已经更新完成
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
								editTextView.setVisibility(View.VISIBLE);
								flushList(array);

							} else {
								PublicUtil.showToast(MycarActivity.this,
										"解析数据错误", false);
							}

						} else {
							PublicUtil.showToast(MycarActivity.this, jsonObject
									.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
						PublicUtil.showToast(MycarActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			} else if (msg != null && msg.what == httprequestsuccess_delete) {
				if (jsonObject != null) {

					try {
						PublicUtil.showToast(MycarActivity.this, jsonObject
								.get("msg").toString(), false);
						if ("0".equals(jsonObject.get("error").toString())) {

							if (dataList.size() > 1) {
								isDownFlush = true;
								nowPage = 1;
								getData(false);
							} else {
								editTextView.setVisibility(View.GONE);
								dataList.clear();
								mycarListAdapter.notifyDataSetChanged();
							}

							// Map<String, String> map;
							// for (int i = 0; i < dataList.size(); i++) {
							// map = dataList.get(i);
							// if (deletId.equals(map.get("id").split("&")[0]))
							// {
							// dataList.remove(i);
							// mycarListAdapter.setData(dataList, isEdit);
							// mycarListAdapter.notifyDataSetChanged();
							// if (dataList.size() == 0) {
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
						e.printStackTrace();
						PublicUtil.showToast(MycarActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

	// {"id":"1","vip_id":"1","vehicle":"川A12345","vhc_model":"车型AAA","vhc_con_type":"GB","mark":"","is_del":"0","vhc_con_type_txt":"国标"}

	private List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();

	/**
	 * 刷新列表
	 * 
	 * @param array
	 */
	public void flushList(JSONArray array) {

		try {
			if (isDownFlush) {
				isDownFlush = false;
				dataList.clear();
			}
			JSONObject object = null;
			int code = dataList.size();
			for (int i = 0; i < array.length(); i++) {
				Map<String, String> map = new HashMap<String, String>();
				object = array.getJSONObject(i);
				map.put("i", String.valueOf(i + code));
				map.put("id", object.getString("id"));

				if (code > 0) {
					map.put("code", PublicUtil.setcode(code + i + 1));
				} else {
					map.put("code", PublicUtil.setcode(i + 1));
				}

				map.put("vip_id", object.getString("vip_id"));
				map.put("vehicle", object.getString("vehicle"));
				map.put("vhc_con_type", object.getString("vhc_con_type"));
				map.put("vhc_con_type_txt",
						object.getString("vhc_con_type_txt"));
				map.put("vhc_model", object.getString("vhc_model"));

				PublicUtil.logDbug(TAG, map.toString(), 0);
				dataList.add(map);
			}
			mycarListAdapter.setData(dataList, isEdit);
			mycarListAdapter.notifyDataSetChanged();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case R.id.mycar_addbutton:
			intent = new Intent(MycarActivity.this, CarUpdateActivity.class);
			intent.putExtra("type", "add");
			startActivityForResult(intent, 1);

			break;
		case R.id.mycar_update:

			if (dataList.size() > 0) {
				if ("编辑".equals(editTextView.getText().toString())) {
					editTextView.setText("取消");
					isEdit = true;

				} else {
					editTextView.setText("编辑");
					isEdit = false;
				}
				mycarListAdapter.setData(dataList, isEdit);
				mycarListAdapter.notifyDataSetChanged();
			}

			break;
		case R.id.mycar_backtextview:
			finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {

			if ("1".equals(data.getStringExtra("flush"))) {
				isDownFlush = true;
				nowPage = 1;
				getData(true);
			}

		}
	}

}
