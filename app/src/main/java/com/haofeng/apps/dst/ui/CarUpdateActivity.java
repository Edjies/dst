package com.haofeng.apps.dst.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 管理车辆信息入口
 * 
 * @author Administrator
 * 
 */
public class CarUpdateActivity extends BaseActivity implements OnClickListener {

	private EditText vehicleEditText, modelEditText;
	private TextView contypeTextView, bacTextView;
	private Button okButton;
	private String type = "add";
	private String connectiontype = null;
	private TextView titleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_carupdate);
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

	public void init() {

		vehicleEditText = (EditText) findViewById(R.id.carupdate_vehicleeditext);
		modelEditText = (EditText) findViewById(R.id.carupdate_modeleditext);
		contypeTextView = (TextView) findViewById(R.id.carupdate_contypetext);
		okButton = (Button) findViewById(R.id.carupdate_okbutton);
		bacTextView = (TextView) findViewById(R.id.carupdate_backtextview);
		titleView = (TextView) findViewById(R.id.carupdate_titleview);
		okButton.setOnClickListener(this);
		bacTextView.setOnClickListener(this);
		contypeTextView.setOnClickListener(this);

		type = getIntent().getStringExtra("type");

		if ("update".equals(type)) {

			vehicleEditText.setText(getIntent().getStringExtra("vehicle"));
			contypeTextView.setText(getIntent().getStringExtra("contype_text"));
			modelEditText.setText(getIntent().getStringExtra("model"));
			connectiontype = getIntent().getStringExtra("contype");
			titleView.setText("修改");

		} else {
			titleView.setText("新增");
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_GETCONNECTIONTYPE);
		map.put("configsfor", "connection_type");
		map.put("ver", Constent.VER);
		AnsynHttpRequest.httpRequest(CarUpdateActivity.this,
				AnsynHttpRequest.POST, callBack, Constent.ID_GETCONNECTIONTYPE,
				map, false, false, true);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.carupdate_okbutton:
			String vehicle = vehicleEditText.getText().toString();// 车牌号
			String model = modelEditText.getText().toString();// 车型号

			if (vehicle == null || TextUtils.isEmpty(vehicle)) {

				PublicUtil.showToast(CarUpdateActivity.this, "请输入车牌号", false);
				return;

			}
			if (connectiontype == null || TextUtils.isEmpty(connectiontype)) {

				PublicUtil.showToast(CarUpdateActivity.this, "请选择充电类型", false);
				return;

			}
			String phone = PublicUtil.getStorage_string(CarUpdateActivity.this,
					"phone", "-1");
			if ("-1".equals(phone)) {

				PublicUtil.showToast(CarUpdateActivity.this,
						"无法获取用户手机号码，请退出重新登陆", false);
				return;

			}
			Map<String, String> map = new HashMap<String, String>();

			map.put("act", Constent.ACT_CARUPDATE);

			if ("update".equals(type)) {
				map.put("vhcid", getIntent().getStringExtra("id"));
			}

			map.put("mobile", phone);
			map.put("vehicle", vehicle);
			map.put("model", model);
			map.put("contype", connectiontype);
			map.put("ver", Constent.VER);
			AnsynHttpRequest.httpRequest(CarUpdateActivity.this,
					AnsynHttpRequest.POST, callBack, Constent.ID_CARUPDATE,
					map, false, true, true);

			break;
		case R.id.carupdate_contypetext:
			if (connectiontypeList.size() > 0) {
				showCarchargeTypes();
			} else {
				PublicUtil.showToast(CarUpdateActivity.this, "车辆充电类型数据异常，返回重试",
						false);
			}
			break;
		case R.id.carupdate_backtextview:
			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 设置车辆充电类型展示
	 */
	public void showCarchargeTypes() {

		View view = LayoutInflater.from(this).inflate(
				R.layout.view_list_radiobtn_dialog, null);
		ListView typeListView = (ListView) view
				.findViewById(R.id.view_radiolist_listview);

		// List<String> list = new ArrayList<String>();
		// int[] images = new int[];
		// for (int i = 0; i < connectiontypeList.size(); i++) {
		//
		// list.add(connectiontypeList.get(i).get("text"));
		// if ("BYD".equals(connectiontypeList.get(i).get("value"))) {
		// images[i] = R.drawable.car_byd;
		// } else if ("TESLA".equals(connectiontypeList.get(i).get("value"))) {
		// images[i] = R.drawable.car_tesila;
		// } else {
		// images[i] = R.drawable.car_guoji;
		// }
		// }

		List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < connectiontypeList.size(); i++) {
			Map<String, Object> listem = new HashMap<String, Object>();

			listem.put("title", connectiontypeList.get(i).get("text"));
			if ("BYD".equals(connectiontypeList.get(i).get("value"))) {
				listem.put("image", R.drawable.fujin_byd);
			} else if ("TESLA".equals(connectiontypeList.get(i).get("value"))) {
				listem.put("image", R.drawable.fujin_tesila);
			} else if ("GB".equals(connectiontypeList.get(i).get("value"))) {
				listem.put("image", R.drawable.fujin_guobiao);
			} else {
				listem.put("image", R.drawable.fujin_other);
			}

			if (connectiontype != null
					&& connectiontype.equals(connectiontypeList.get(i).get(
							"value"))) {
				listem.put("choose", R.drawable.btn_radio_on_holo_dark);
			} else {
				listem.put("choose", R.drawable.btn_radio_off_holo_dark);
			}
			listems.add(listem);
		}

		SimpleAdapter simplead = new SimpleAdapter(this, listems,
				R.layout.listview_item_view_list_radiobtn_dialog, new String[] {
						"image", "title", "choose" }, new int[] {
						R.id.listview_item_view_radiobtn_dialog_typeimage,
						R.id.listview_item_view_radiobtn_dialog_typetext,
						R.id.listview_item_view_radiobtn_dialog_typeradio });

		typeListView.setAdapter(simplead);

		final AlertDialog alertDialog = new AlertDialog.Builder(
				CarUpdateActivity.this).setView(view).show();

		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		typeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

				contypeTextView.setText(connectiontypeList.get(arg2)
						.get("text"));
				connectiontype = connectiontypeList.get(arg2).get("value");
				if (alertDialog != null) {
					alertDialog.dismiss();
				}

			}
		});

	}

	private JSONObject jsonObject = null;

	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@Override
		public void back(int backId, boolean isRequestSuccess,
				boolean isString, String data, JSONArray jsonArray) {
			// TODO Auto-generated method stub
			switch (backId) {
			case Constent.ID_CARUPDATE:
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

			case Constent.ID_GETCONNECTIONTYPE:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							if (handler != null) {
								handler.sendEmptyMessage(httprequestsuccess_getcarchargetype);
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

	private int httprequesterror = 0x1300;
	private int httprequestsuccess = 0x1301;
	private int httprequestsuccess_getcarchargetype = 0x1302;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(CarUpdateActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {

					try {

						if ("0".equals(jsonObject.get("error").toString())) {
							Intent intent = new Intent();
							intent.putExtra("flush", "1");
							setResult(1, intent);
							finish();
						} else {
							PublicUtil.showToast(CarUpdateActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(CarUpdateActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			} else if (msg != null
					&& msg.what == httprequestsuccess_getcarchargetype) {
				if (jsonObject != null) {

					try {

						if ("0".equals(jsonObject.get("error").toString())) {
							JSONObject data = (JSONObject) jsonObject
									.get("data");

							if (jsonObject != null) {
								setConnectiontype(data);
							} else {
								PublicUtil.showToast(CarUpdateActivity.this,
										"无法获取充电类型", false);
							}
						} else {
							PublicUtil.showToast(CarUpdateActivity.this,
									jsonObject.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(CarUpdateActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

	private List<Map<String, String>> connectiontypeList = new ArrayList<Map<String, String>>();

	/**
	 * 设置充电桩类型
	 * 
	 * @param array
	 */
	public void setConnectiontype(JSONObject object) {

		try {

			JSONArray array2 = object.getJSONArray("connection_type");
			if (array2 != null && array2.length() > 0) {
				JSONObject object2;
				for (int j = 0; j < array2.length(); j++) {
					object2 = array2.getJSONObject(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("value", object2.getString("value"));
					map.put("text", object2.getString("text"));
					connectiontypeList.add(map);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}
