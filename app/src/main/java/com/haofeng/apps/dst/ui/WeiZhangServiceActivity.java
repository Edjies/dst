package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.AnsynHttpRequest;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.httptools.HttpRequestCallBack;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 违章服务
 * 
 * @author WIN10
 *
 */
public class WeiZhangServiceActivity extends BaseActivity implements OnClickListener {
	private FrameLayout topLayout;
	private ListView wzDatailView;
	private List<Map<String, String>> wzCarDetailList = new ArrayList<Map<String, String>>();
	private TextView back;
	private TextView carNumberView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addActivity(this);
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.acitivity_weizhang_service);
		topLayout = (FrameLayout) findViewById(R.id.weizhangchaxun_top_layout);
		setTopLayoutPadding(topLayout);
		wzDatailView = (ListView) findViewById(R.id.activity_wz_detail);
		carNumberView = (TextView) findViewById(R.id.activity_wz_detail_carnumber);
		back = (TextView) findViewById(R.id.weizhangservice_back);
		back.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		String carNumber = getIntent().getStringExtra("carNumber");
		carNumberView.setText(carNumber.toUpperCase());
		Map<String, String> map = new HashMap<String, String>();
		map.put("act", Constent.ACT_WEI_ZHANG_SEARCH);
		map.put("car_no", carNumber);
		AnsynHttpRequest.httpRequest(WeiZhangServiceActivity.this, AnsynHttpRequest.GET, callBack,
				Constent.ID_ACT_WEI_ZHANG_SEARCH, map, false, false, true);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.weizhangservice_back:
			finish();
			break;
		case R.id.lsitview_item_detail_zhixun_kefu: // 咨询客服
			break;
		default:
			break;
		}
	}

	private HttpRequestCallBack callBack = new HttpRequestCallBack() {

		@SuppressLint("NewApi")
		@Override
		public void back(int backId, boolean isRequestSuccess, boolean isString, String data, JSONArray jsonArray) {
			// TODO Auto-generated method stub
			switch (backId) {
			case Constent.ID_ACT_WEI_ZHANG_SEARCH:
				if (isRequestSuccess) {
					if (!isString) {
						try {
							String backstr = jsonArray.getString(1);
							jsonObject = new JSONObject(backstr);
							handler.sendEmptyMessage(httprequestsuccess);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				break;
			}
		}
	};

	class MyBaseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return wzCarDetailList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(WeiZhangServiceActivity.this, R.layout.listview_item_wz_detail, null);
				viewHolder.dateView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_date);
				viewHolder.areaView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_area);
				viewHolder.beizhuView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_beizhu);
				viewHolder.fenView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_fen);
				viewHolder.fakuanView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_fakuan);
				viewHolder.daijiaoView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_daijiao);
				viewHolder.daijiaoView.setOnClickListener(new OnClickListener() { // 违章代缴
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(WeiZhangServiceActivity.this, WeiZhangPayActivity.class);
						intent.putExtra("fen", wzCarDetailList.get(position).get("fen"));
						intent.putExtra("money", wzCarDetailList.get(position).get("money"));
						intent.putExtra("date", wzCarDetailList.get(position).get("date"));
						intent.putExtra("plate_number", wzCarDetailList.get(position).get("plate_number"));
						startActivity(intent);
					}
				});
				viewHolder.zhixunkefuView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_zhixun_kefu);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.areaView.setText(wzCarDetailList.get(position).get("area"));
			viewHolder.dateView.setText(wzCarDetailList.get(position).get("date"));
			viewHolder.fakuanView.setText(wzCarDetailList.get(position).get("money"));
			viewHolder.fenView.setText(wzCarDetailList.get(position).get("fen"));
			viewHolder.beizhuView.setText(wzCarDetailList.get(position).get("act"));
			if ((Integer.parseInt(wzCarDetailList.get(position).get("fen")) > 0)) { // 咨询客服
				viewHolder.zhixunkefuView.setVisibility(View.VISIBLE);
				viewHolder.daijiaoView.setVisibility(View.GONE);
			} else { // 违章代缴
				viewHolder.zhixunkefuView.setVisibility(View.GONE);
				viewHolder.daijiaoView.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		class ViewHolder {
			TextView dateView;
			TextView areaView;
			TextView beizhuView;
			TextView fenView;
			TextView fakuanView;
			TextView daijiaoView;
			TextView zhixunkefuView;
		}
	}

	private JSONObject jsonObject = null;
	private int httprequesterror = 0x10001;
	private int httprequestsuccess = 0x10002;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			if (msg != null && msg.what == httprequestsuccess) {
				if (jsonObject != null) {
					try {
						if ("0".equals(jsonObject.get("errcode").toString())) {
							JSONArray dataArray = jsonObject.getJSONArray("data");
							for (int i = 0; i < dataArray.length(); i++) {
								String fen = ((JSONObject) dataArray.get(i)).getString("fen");
								String date = ((JSONObject) dataArray.get(i)).getString("date");
								String area = ((JSONObject) dataArray.get(i)).getString("area");
								String act = ((JSONObject) dataArray.get(i)).getString("act");
								String money = ((JSONObject) dataArray.get(i)).getString("money");
								String plate_number = ((JSONObject) dataArray.get(i)).getString("plate_number");
								Map<String, String> map = new HashMap<String, String>();
								map.put("fen", fen);
								map.put("date", date);
								map.put("area", area);
								map.put("act", act);
								map.put("money", money);
								map.put("plate_number", plate_number);
								wzCarDetailList.add(map);
							}
							wzDatailView.setItemsCanFocus(true);
							wzDatailView.setAdapter(new MyBaseAdapter());
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		};
	};

}
