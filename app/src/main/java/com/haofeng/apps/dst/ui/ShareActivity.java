package com.haofeng.apps.dst.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class ShareActivity extends BaseActivity implements OnClickListener {
	// private static final String TAG = "ShareActivity";
	private Button okButton, nextButton;
	private LinearLayout layout, layout2;
	private TextView connectionTextView, chargeTextView, modelTextView,
			manufacTextView, installTextView, baxkTextView;
	private EditText addrEditText, codeEditText, specifiEditText, wireEditText,
			charge_gunEditText, markEditText;
	private String connectiontype = null, charge = null, model = null,
			manufac = null, install = null, addr = null, code = null,
			specifi = null, wire = null, charge_gun = null, mark = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
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

		layout = (LinearLayout) findViewById(R.id.share_layout1);
		layout2 = (LinearLayout) findViewById(R.id.share_layout2);

		okButton = (Button) findViewById(R.id.share_okbutton);
		nextButton = (Button) findViewById(R.id.share_nextbutton);
		connectionTextView = (TextView) findViewById(R.id.share_connectiontype);
		chargeTextView = (TextView) findViewById(R.id.share_chargetype);
		modelTextView = (TextView) findViewById(R.id.share_modeltype);
		manufacTextView = (TextView) findViewById(R.id.share_manufactype);
		installTextView = (TextView) findViewById(R.id.share_installtype);

		addrEditText = (EditText) findViewById(R.id.share_addressedit);
		codeEditText = (EditText) findViewById(R.id.share_code);
		specifiEditText = (EditText) findViewById(R.id.share_specifi);
		wireEditText = (EditText) findViewById(R.id.share_wire);
		charge_gunEditText = (EditText) findViewById(R.id.share_charge_gun);
		markEditText = (EditText) findViewById(R.id.share_mark);
		baxkTextView = (TextView) findViewById(R.id.share_back);
		connectionTextView.setOnClickListener(this);
		chargeTextView.setOnClickListener(this);
		modelTextView.setOnClickListener(this);
		manufacTextView.setOnClickListener(this);
		installTextView.setOnClickListener(this);
		okButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		baxkTextView.setOnClickListener(this);

		layout.setVisibility(View.VISIBLE);
		layout2.setVisibility(View.GONE);

		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_GETCONNECTIONTYPE);
		map.put("configsfor",
				"connection_type,model,charge_type,manufacturer,install_type");
		map.put("ver", Constent.VER);
		AnsynHttpRequest.httpRequest(ShareActivity.this, AnsynHttpRequest.POST,
				callBack, Constent.ID_GETCONNECTIONTYPE, map, false, false,
				true);

	}

	@TargetApi(19)
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.share_back:
			if (layout2.getVisibility() == View.VISIBLE
					&& layout.getVisibility() == View.GONE) {
				layout.setVisibility(View.VISIBLE);
				layout2.setVisibility(View.GONE);

			} else {
				finish();
			}
			break;
		case R.id.share_connectiontype:
			if (connectiontypeList.size() > 0) {
				showCarchargeTypes(connectiontypeList, 1);
			} else {
				PublicUtil.showToast(ShareActivity.this, "数据初始化设置异常，返回重试",
						false);
			}
			break;
		case R.id.share_chargetype:
			if (chargetypeList.size() > 0) {
				showCarchargeTypes(chargetypeList, 2);
			} else {
				PublicUtil.showToast(ShareActivity.this, "数据初始化设置异常，返回重试",
						false);
			}
			break;
		case R.id.share_modeltype:
			if (modeltypeList.size() > 0) {
				showCarchargeTypes(modeltypeList, 3);
			} else {
				PublicUtil.showToast(ShareActivity.this, "数据初始化设置异常，返回重试",
						false);
			}
			break;
		case R.id.share_manufactype:
			if (manufactypeList.size() > 0) {
				showCarchargeTypes(manufactypeList, 4);
			} else {
				PublicUtil.showToast(ShareActivity.this, "数据初始化设置异常，返回重试",
						false);
			}
			break;
		case R.id.share_installtype:
			if (installtypeList.size() > 0) {
				showCarchargeTypes(installtypeList, 5);
			} else {
				PublicUtil.showToast(ShareActivity.this, "数据初始化设置异常，返回重试",
						false);
			}
			break;
		case R.id.share_nextbutton:

			if (connectiontype == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电连接类型", false);
				return;
			}
			if (charge == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电类型", false);
				return;
			}
			if (model == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择型号", false);
				return;
			}
			if (manufac == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电桩生产厂家", false);
				return;
			}
			if (install == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电桩安装方式", false);
				return;
			}
			layout.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);

			break;
		case R.id.share_okbutton:
			addr = addrEditText.getText().toString();
			code = codeEditText.getText().toString();
			specifi = specifiEditText.getText().toString();
			wire = wireEditText.getText().toString();
			charge_gun = charge_gunEditText.getText().toString();
			mark = markEditText.getText().toString();

			if (connectiontype == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电连接类型", false);
				return;
			}
			if (charge == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电类型", false);
				return;
			}
			if (model == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择型号", false);
				return;
			}
			if (manufac == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电桩生产厂家", false);
				return;
			}
			if (install == null) {
				PublicUtil.showToast(ShareActivity.this, "请选择充电桩安装方式", false);
				return;
			}
			if (charge_gun == null) {
				PublicUtil.showToast(ShareActivity.this, "请输入充电桩充电枪个数", false);
				return;
			} else {

				if (!charge_gun.matches("^[1-9]\\d*")) {
					PublicUtil.showToast(ShareActivity.this, "请输入整数数字", false);
					return;
				}

			}
			if (wire == null) {
				PublicUtil.showToast(ShareActivity.this, "请输入充电桩充电枪线长", false);
				return;
			}
			if (specifi == null) {
				PublicUtil.showToast(ShareActivity.this, "请输入充电桩详细规格参数", false);
				return;
			}
			if (code == null) {
				PublicUtil.showToast(ShareActivity.this, "请输入充电桩厂家编号", false);
				return;
			}
			if (addr == null) {
				PublicUtil.showToast(ShareActivity.this, "请输入充电桩安装详细地址", false);
				return;
			}

			String phone = PublicUtil.getStorage_string(ShareActivity.this,
					"phone", "-1");

			if ("-1".equals(phone)) {

				PublicUtil.showToast(ShareActivity.this,
						"用户id读取失败，无法进行下一步数据请求，请退出后重新登陆", false);

			} else {
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("connection_type", connectiontype);
					jsonObject.put("charge_type", charge);
					jsonObject.put("model", model);
					jsonObject.put("manufacturer", manufac);
					jsonObject.put("install_type", install);
					jsonObject.put("charge_gun_nums", charge_gun);
					jsonObject.put("wire_length", wire);
					jsonObject.put("specification", specifi);
					jsonObject.put("code_from_factory", code);
					jsonObject.put("install_site", addr);
					jsonObject.put("mark", mark);

					JSONArray array = new JSONArray();
					array.put(jsonObject);

					Map<String, String> map = new HashMap<String, String>();
					map.put("act", Constent.ACT_SHARE);
					map.put("mobile", phone);
					map.put("ver", Constent.VER);
					map.put("charger", array.toString());

					// map.put("connection_type", connectiontype);
					// map.put("charge_type", charge);
					// map.put("model", model);
					// map.put("manufacturer", manufac);
					// map.put("install_type", install);
					// map.put("charge_gun_nums", charge_gun);
					// map.put("wire_length", wire);
					// map.put("specification", specifi);
					// map.put("code_from_factory", code);
					// map.put("install_site", addr);
					// map.put("mark", mark);

					AnsynHttpRequest.httpRequest(ShareActivity.this,
							AnsynHttpRequest.POST, callBack, Constent.ID_SHARE,
							map, false, true, true);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			break;

		default:
			break;
		}

	}

	List<Map<String, Object>> listems = new ArrayList<Map<String, Object>>();

	/**
	 * 参数类型展示
	 */
	public void showCarchargeTypes(final List<Map<String, String>> datalist,
			final int cont) {

		View view = LayoutInflater.from(this).inflate(
				R.layout.view_list_radiobtn_dialog, null);
		ListView typeListView = (ListView) view
				.findViewById(R.id.view_radiolist_listview);

		// List<String> list = new ArrayList<String>();
		// for (int i = 0; i < datalist.size(); i++) {
		//
		// list.add(datalist.get(i).get("text"));
		// }

		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(
		// ShareActivity.this, R.layout.listview_item_text, list);
		listems.clear();
		for (int i = 0; i < datalist.size(); i++) {
			Map<String, Object> listem = new HashMap<String, Object>();

			listem.put("title", datalist.get(i).get("text"));

			if (cont == 1) {
				if ("BYD".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.fujin_byd);
				} else if ("TESLA".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.fujin_tesila);
				} else if ("GB".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.fujin_guobiao);
				} else {
					listem.put("image", R.drawable.fujin_other);
				}

				if (connectiontype != null
						&& connectiontype.equals(datalist.get(i).get("value"))) {
					listem.put("choose", R.drawable.btn_radio_on_holo_dark);
				} else {
					listem.put("choose", R.drawable.btn_radio_off_holo_dark);
				}
			} else if (cont == 2) {
				if ("DC".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.type_dc);
				} else if ("AC".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.type_ac);
				} else if ("AC_DC".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.type_ac_dc);
				} else if ("DC_DC".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.type_dc_dc);
				} else if ("AC_AC".equals(datalist.get(i).get("value"))) {
				}
				if (charge != null
						&& charge.equals(datalist.get(i).get("value"))) {
					listem.put("choose", R.drawable.btn_radio_on_holo_dark);
				} else {
					listem.put("choose", R.drawable.btn_radio_off_holo_dark);
				}
			} else if (cont == 3) {
				if (model != null && model.equals(datalist.get(i).get("value"))) {
					listem.put("choose", R.drawable.btn_radio_on_holo_dark);
				} else {
					listem.put("choose", R.drawable.btn_radio_off_holo_dark);
				}
			} else if (cont == 4) {
				if ("KE_LU_DIAN_ZI".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.company_clou);
				} else if ("ZHONG_XING_TONG_XUN".equals(datalist.get(i).get(
						"value"))) {
					listem.put("image", R.drawable.company_zte);
				} else if ("BYD".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.company_byd);
				} else if ("BSB".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.company_basiba);
				} else if ("AOTEXUN".equals(datalist.get(i).get("value"))) {
					listem.put("image", R.drawable.company_aotexun);
				} else {
					listem.put("image", R.drawable.company_test);
				}
				if (manufac != null
						&& manufac.equals(datalist.get(i).get("value"))) {
					listem.put("choose", R.drawable.btn_radio_on_holo_dark);
				} else {
					listem.put("choose", R.drawable.btn_radio_off_holo_dark);
				}
			} else if (cont == 5) {
				if (install != null
						&& install.equals(datalist.get(i).get("value"))) {
					listem.put("choose", R.drawable.btn_radio_on_holo_dark);
				} else {
					listem.put("choose", R.drawable.btn_radio_off_holo_dark);
				}
			}

			listems.add(listem);
		}

		SimpleAdapter simplead;
		if (cont == 3 || cont == 5) {
			simplead = new SimpleAdapter(
					this,
					listems,
					R.layout.listview_item_view_list_radiobtn_dialog_noheadimage,
					new String[] { "title", "choose" },
					new int[] {
							R.id.listview_item_view_radiobtn_dialog_noheadimage_typetext,
							R.id.listview_item_view_radiobtn_dialog_noheadimage_typeradio });
		} else {
			simplead = new SimpleAdapter(this, listems,
					R.layout.listview_item_view_list_radiobtn_dialog,
					new String[] { "image", "title", "choose" }, new int[] {
							R.id.listview_item_view_radiobtn_dialog_typeimage,
							R.id.listview_item_view_radiobtn_dialog_typetext,
							R.id.listview_item_view_radiobtn_dialog_typeradio });
		}

		typeListView.setAdapter(simplead);

		final AlertDialog alertDialog = new AlertDialog.Builder(
				ShareActivity.this).setView(view).show();

		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		typeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (cont == 1) {
					connectionTextView.setText(datalist.get(arg2).get("text"));
					connectiontype = datalist.get(arg2).get("value");
				} else if (cont == 2) {
					chargeTextView.setText(datalist.get(arg2).get("text"));
					charge = datalist.get(arg2).get("value");
				} else if (cont == 3) {
					modelTextView.setText(datalist.get(arg2).get("text"));
					model = datalist.get(arg2).get("value");
				} else if (cont == 4) {
					manufacTextView.setText(datalist.get(arg2).get("text"));
					manufac = datalist.get(arg2).get("value");
				} else if (cont == 5) {
					installTextView.setText(datalist.get(arg2).get("text"));
					install = datalist.get(arg2).get("value");
				}

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
			case Constent.ID_SHARE:
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

	private int httprequesterror = 0x1700;
	private int httprequestsuccess = 0x1701;
	private int httprequestsuccess_getcarchargetype = 0x1702;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequesterror) {
				if (msg.obj != null) {
					PublicUtil.showToast(ShareActivity.this,
							msg.obj.toString(), false);

				}
			} else if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {

					try {
						PublicUtil.showToast(ShareActivity.this, jsonObject
								.get("msg").toString(), false);
						if ("0".equals(jsonObject.get("error").toString())) {
							// new ShareListActivity().getData();
							Intent intent = new Intent(ShareActivity.this,
									ShareListActivity.class);
							startActivity(intent);
							finish();
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(ShareActivity.this,
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
								PublicUtil.showToast(ShareActivity.this,
										"无法获取充电类型", false);
							}
						} else {
							PublicUtil.showToast(ShareActivity.this, jsonObject
									.get("msg").toString(), false);
						}
					} catch (Exception e) {
						// TODO: handle exception
						PublicUtil.showToast(ShareActivity.this,
								"配置解析数据错误，请退出重试", false);
					}

				}

			}

		};
	};

	private List<Map<String, String>> connectiontypeList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> chargetypeList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> modeltypeList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> manufactypeList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> installtypeList = new ArrayList<Map<String, String>>();

	/**
	 * 设置充电桩类型
	 * 
	 * @param array
	 */
	public void setConnectiontype(JSONObject object) {

		try {

			JSONArray array = object.getJSONArray("connection_type");
			if (array != null && array.length() > 0) {
				JSONObject object2;
				for (int j = 0; j < array.length(); j++) {
					object2 = array.getJSONObject(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("value", object2.getString("value"));
					map.put("text", object2.getString("text"));
					connectiontypeList.add(map);
				}
			}

			array = object.getJSONArray("install_type");
			if (array != null && array.length() > 0) {
				JSONObject object2;
				for (int j = 0; j < array.length(); j++) {
					object2 = array.getJSONObject(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("value", object2.getString("value"));
					map.put("text", object2.getString("text"));
					installtypeList.add(map);
				}
			}
			array = object.getJSONArray("model");
			if (array != null && array.length() > 0) {
				JSONObject object2;
				for (int j = 0; j < array.length(); j++) {
					object2 = array.getJSONObject(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("value", object2.getString("value"));
					map.put("text", object2.getString("text"));
					modeltypeList.add(map);
				}
			}
			array = object.getJSONArray("charge_type");
			if (array != null && array.length() > 0) {
				JSONObject object2;
				for (int j = 0; j < array.length(); j++) {
					object2 = array.getJSONObject(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("value", object2.getString("value"));
					map.put("text", object2.getString("text"));
					chargetypeList.add(map);
				}
			}
			array = object.getJSONArray("manufacturer");
			if (array != null && array.length() > 0) {
				JSONObject object2;
				for (int j = 0; j < array.length(); j++) {
					object2 = array.getJSONObject(j);
					Map<String, String> map = new HashMap<String, String>();
					map.put("value", object2.getString("value"));
					map.put("text", object2.getString("text"));
					manufactypeList.add(map);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (layout2.getVisibility() == View.VISIBLE
					&& layout.getVisibility() == View.GONE) {
				layout.setVisibility(View.VISIBLE);
				layout2.setVisibility(View.GONE);

			} else {
				finish();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
