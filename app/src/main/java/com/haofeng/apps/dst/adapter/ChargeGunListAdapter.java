package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class ChargeGunListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;
	private Callback mCallback, mCallback2;

	public ChargeGunListAdapter(Context context, Callback callback,
			Callback callback2) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mContext = context;
		mCallback = callback;
		mCallback2 = callback2;
	}

	public interface Callback {
		public void click(View v);
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// 在此适配器中所代表的数据集中的条目数
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		// 获取数据集中与指定索引对应的数据项
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		// 获取在列表中与指定索引对应的行id
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ChangeGunViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_chargeguns, null);
			viewHolder = new ChangeGunViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_id);
			viewHolder.numberTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_number);
			viewHolder.nameTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_name);

			viewHolder.typeTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_type);
			viewHolder.statusTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_status);
			viewHolder.statustxtTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_statustxt);
			viewHolder.chargeTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_charge);
			viewHolder.name2TextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_name2);
			viewHolder.type2TextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_type2);
			viewHolder.status2TextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_status2);
			viewHolder.status2txtTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_status2txt);
			viewHolder.charge2TextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_charge2);
			viewHolder.lineImageView = (ImageView) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_line);
			viewHolder.frameLayout = (FrameLayout) arg1
					.findViewById(R.id.listview_item_chargegunslist_infor_framelayout);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (ChangeGunViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.numberTextView.setText(data.get(arg0).get("DEV_NAME"));

			viewHolder.typeTextView.setText(data.get(arg0).get(
					"charge_pattern_text")
					+ "充");
			String status = data.get(arg0).get("guns1_status");
			if ("0".equals(status)) {
				viewHolder.statustxtTextView.setTextColor(mContext
						.getResources().getColor(R.color.red));
				viewHolder.chargeTextView.setText("充电中");
				viewHolder.chargeTextView
						.setBackgroundResource(R.drawable.cdzxiangqing4_1_14);
				viewHolder.chargeTextView.setTextColor(mContext.getResources()
						.getColor(R.color.red));
			} else if ("1".equals(status)) {
				viewHolder.statustxtTextView.setTextColor(mContext
						.getResources().getColor(R.color.menugreen));
				viewHolder.chargeTextView.setText("立即充电");
				viewHolder.chargeTextView
						.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
				viewHolder.chargeTextView.setTextColor(mContext.getResources()
						.getColor(R.color.white));
			} else {
				viewHolder.statustxtTextView.setTextColor(mContext
						.getResources().getColor(R.color.gray));
				viewHolder.chargeTextView.setText("离线");
				viewHolder.chargeTextView
						.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
				viewHolder.chargeTextView.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
			}
			viewHolder.statustxtTextView.setText(data.get(arg0).get(
					"guns1_status_txt"));
			viewHolder.statusTextView.setText(data.get(arg0)
					.get("guns1_status"));

			viewHolder.nameTextView.setText(data.get(arg0).get("guns1_name"));

			viewHolder.chargeTextView.setOnClickListener(clickListener);
			viewHolder.chargeTextView.setTag(data.get(arg0).get("i"));
			if (Integer.parseInt(data.get(arg0).get("guns")) > 1) {
				viewHolder.nameTextView.setVisibility(View.VISIBLE);
				viewHolder.name2TextView.setVisibility(View.VISIBLE);
				viewHolder.frameLayout.setVisibility(View.VISIBLE);
				viewHolder.lineImageView.setVisibility(View.VISIBLE);

				viewHolder.name2TextView.setText(data.get(arg0).get(
						"guns2_name"));
				viewHolder.status2txtTextView.setText(data.get(arg0).get(
						"guns2_status_txt"));
				viewHolder.status2TextView.setText(data.get(arg0).get(
						"guns2_status"));
				viewHolder.type2TextView.setText(data.get(arg0).get(
						"charge_pattern_text")
						+ "充");

				status = data.get(arg0).get("guns1_status");
				String status2 = data.get(arg0).get("guns2_status");

				if ("0".equals(status) && "1".equals(status2)) {
					viewHolder.status2txtTextView.setTextColor(mContext
							.getResources().getColor(R.color.menugreen));
					viewHolder.charge2TextView.setText("预约充电");
					viewHolder.charge2TextView
							.setBackgroundResource(R.drawable.cdzxiangqing4_1_07_yellow);
					viewHolder.charge2TextView.setTextColor(mContext
							.getResources().getColor(R.color.white));
				} else if ("1".equals(status) && "0".equals(status2)) {

					viewHolder.status2txtTextView.setTextColor(mContext
							.getResources().getColor(R.color.red));
					viewHolder.charge2TextView.setText("充电中");
					viewHolder.charge2TextView
							.setBackgroundResource(R.drawable.cdzxiangqing4_1_14);
					viewHolder.charge2TextView.setTextColor(mContext
							.getResources().getColor(R.color.red));
					viewHolder.chargeTextView.setText("预约充电");
					viewHolder.chargeTextView
							.setBackgroundResource(R.drawable.cdzxiangqing4_1_07_yellow);

				} else {

					if ("0".equals(status2)) {
						viewHolder.status2txtTextView.setTextColor(mContext
								.getResources().getColor(R.color.red));
						viewHolder.charge2TextView.setText("充电中");
						viewHolder.charge2TextView
								.setBackgroundResource(R.drawable.cdzxiangqing4_1_14);
						viewHolder.charge2TextView.setTextColor(mContext
								.getResources().getColor(R.color.red));
					} else if ("1".equals(status2)) {
						viewHolder.status2txtTextView.setTextColor(mContext
								.getResources().getColor(R.color.menugreen));
						viewHolder.charge2TextView.setText("立即充电");
						viewHolder.charge2TextView
								.setBackgroundResource(R.drawable.cdzxiangqing4_1_07);
						viewHolder.charge2TextView.setTextColor(mContext
								.getResources().getColor(R.color.white));
					} else {
						viewHolder.status2txtTextView.setTextColor(mContext
								.getResources().getColor(R.color.gray));
						viewHolder.charge2TextView.setText("离线");
						viewHolder.charge2TextView
								.setBackgroundResource(R.drawable.cdzxiangqing4_1_12);
						viewHolder.charge2TextView.setTextColor(mContext
								.getResources().getColor(R.color.gray));
					}

				}

				viewHolder.charge2TextView.setOnClickListener(clickListener2);
				viewHolder.charge2TextView.setTag(data.get(arg0).get("i"));
			} else {
				viewHolder.lineImageView.setVisibility(View.GONE);
				viewHolder.frameLayout.setVisibility(View.GONE);
			}

		}

		return arg1;
	}

	private class ChangeGunViewHolder {

		public TextView idTextView, numberTextView, nameTextView, typeTextView,
				statusTextView, statustxtTextView, chargeTextView,
				name2TextView, type2TextView, status2TextView,
				status2txtTextView, charge2TextView;
		public ImageView lineImageView;
		public FrameLayout frameLayout;

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			mCallback.click(arg0);
		}
	};

	private OnClickListener clickListener2 = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			mCallback2.click(arg0);
		}
	};

}
