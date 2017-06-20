package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class MycarListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private boolean isedit = false;
	private Callback deletCallback, updateCallback;

	public MycarListAdapter(Context context, Callback deletCallback,
			Callback updateCallback) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.deletCallback = deletCallback;
		this.updateCallback = updateCallback;
	}

	public interface Callback {
		public void click(View v);
	};

	public void setData(List<Map<String, String>> data, boolean isedit) {
		this.data = data;
		this.isedit = isedit;
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
		MycarViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_mycar, null);
			viewHolder = new MycarViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.mycar_listitem_id);
			viewHolder.vehicleTextView = (TextView) arg1
					.findViewById(R.id.mycar_listitem_infor_vehicle);
			viewHolder.vhc_con_type_txtTextView = (TextView) arg1
					.findViewById(R.id.mycar_listitem_infor_vhc_con_type_txt);
			viewHolder.vhc_modelTextView = (TextView) arg1
					.findViewById(R.id.mycar_listitem_infor_vhc_model);
			viewHolder.typeView = (ImageView) arg1
					.findViewById(R.id.mycar_listitem_infor_image);
			viewHolder.deletLayout = (LinearLayout) arg1
					.findViewById(R.id.mycar_listitem_delete);
			viewHolder.updateLayout = (LinearLayout) arg1
					.findViewById(R.id.mycar_listitem_update);
			viewHolder.bottomLayout = (LinearLayout) arg1
					.findViewById(R.id.mycar_listitem_bottomlayout);
			viewHolder.inforLayout = (LinearLayout) arg1
					.findViewById(R.id.mycar_listitem_infor_layout);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (MycarViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.vehicleTextView.setText(data.get(arg0).get("vehicle"));
			viewHolder.vhc_con_type_txtTextView.setText(data.get(arg0).get(
					"vhc_con_type_txt"));
			if ("GB".equals(data.get(arg0).get("vhc_con_type"))) {
				viewHolder.typeView.setImageResource(R.drawable.fujin_guobiao);
			} else if ("BYD".equals(data.get(arg0).get("vhc_con_type"))) {
				viewHolder.typeView.setImageResource(R.drawable.fujin_byd);
			} else if ("TESLA".equals(data.get(arg0).get("vhc_con_type"))) {
				viewHolder.typeView.setImageResource(R.drawable.fujin_tesila);
			} else {
				viewHolder.typeView.setImageResource(R.drawable.fujin_other);
			}

			if (isedit) {
				viewHolder.bottomLayout.setVisibility(View.VISIBLE);
			} else {
				viewHolder.bottomLayout.setVisibility(View.GONE);
			}

			if (data.get(arg0).get("vhc_model") == null
					|| TextUtils.isEmpty(data.get(arg0).get("vhc_model"))) {
				viewHolder.inforLayout.setVisibility(View.GONE);
			} else {
				viewHolder.inforLayout.setVisibility(View.VISIBLE);
				viewHolder.vhc_modelTextView.setText(data.get(arg0).get(
						"vhc_model"));
			}

			viewHolder.deletLayout.setOnClickListener(deletClickListener);
			viewHolder.deletLayout.setTag(data.get(arg0).get("i"));
			viewHolder.updateLayout.setOnClickListener(updateClickListener);
			viewHolder.updateLayout.setTag(data.get(arg0).get("i"));
		}

		return arg1;
	}

	private class MycarViewHolder {

		public TextView idTextView, vehicleTextView, vhc_con_type_txtTextView,
				vhc_modelTextView;
		public ImageView typeView;
		public LinearLayout deletLayout, updateLayout, bottomLayout,
				inforLayout;

	}

	private OnClickListener deletClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			deletCallback.click(arg0);
		}
	};

	private OnClickListener updateClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			updateCallback.click(arg0);

		}
	};

}
