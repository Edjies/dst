package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class LeaseCarListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	public LeaseCarListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
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
		LeasrCarViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_leasecarlist, null);
			viewHolder = new LeasrCarViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.leasecarlist_listitem_id);
			viewHolder.vehicle_dentification_numberTextView = (TextView) arg1
					.findViewById(R.id.leasecarlist_listitem_infor_vehicle_dentification_number);
			viewHolder.month_rentTextView = (TextView) arg1
					.findViewById(R.id.leasecarlist_listitem_infor_month_rent);
			viewHolder.let_timeTextView = (TextView) arg1
					.findViewById(R.id.leasecarlist_listitem_infor_let_time);
			viewHolder.plate_numberTextView = (TextView) arg1
					.findViewById(R.id.leasecarlist_listitem_infor_plate_number);
			viewHolder.codeTextView = (TextView) arg1
					.findViewById(R.id.leasecarlist_listitem_infor_code);
			viewHolder.typeView = (ImageView) arg1
					.findViewById(R.id.leasecarlist_listitem_infor_image);
			viewHolder.typeFTextView = (TextView) arg1
					.findViewById(R.id.leasecarlist_listitem_infor_type);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (LeasrCarViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.vehicle_dentification_numberTextView.setText(data.get(
					arg0).get("vehicle_dentification_number"));
			viewHolder.month_rentTextView.setText(data.get(arg0).get(
					"month_rent"));
			viewHolder.let_timeTextView.setText(data.get(arg0).get("let_time"));
			viewHolder.plate_numberTextView.setText(data.get(arg0).get(
					"plate_number"));
			viewHolder.codeTextView.setText(data.get(arg0).get("code"));
			viewHolder.typeFTextView.setText(data.get(arg0).get(
					"car_brand_text"));
			if ("RC".equals(data.get(arg0).get("car_brand"))) {
				viewHolder.typeView.setImageResource(R.drawable.list_ruichi2);
			} else if ("BEV".equals(data.get(arg0).get("car_brand"))) {
				viewHolder.typeView.setImageResource(R.drawable.list_beiqi2);
			} else if ("BYD".equals(data.get(arg0).get("car_brand"))) {
				viewHolder.typeView.setImageResource(R.drawable.list_byd2);
			} else if ("DF".equals(data.get(arg0).get("car_brand"))) {
				viewHolder.typeView.setImageResource(R.drawable.list_dongfeng2);
			} else {
				viewHolder.typeView.setImageResource(R.drawable.list_qita2);
			}

		}

		return arg1;
	}

	public static class LeasrCarViewHolder {
		public TextView idTextView, vehicle_dentification_numberTextView;
		public TextView month_rentTextView;
		public TextView let_timeTextView;
		public TextView plate_numberTextView;
		public TextView codeTextView;
		public ImageView typeView;
		public TextView typeFTextView;

	}

}
