package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class ChargeStationListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private CallBack mCallBack;

	public ChargeStationListAdapter(Context context, CallBack callBack) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mCallBack = callBack;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public interface CallBack {

		public void click(View v);

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
		ChangeStationViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_chargestationlist,
					null);
			viewHolder = new ChangeStationViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargestationlist_id);
			viewHolder.nameTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_name);
			viewHolder.distanceTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_distance);
			viewHolder.addrTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_addr);
			viewHolder.inforTextView = (TextView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_chargestatus);

			viewHolder.bydImageView = (ImageView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_typeimage_byd);
			viewHolder.gjImageView = (ImageView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_typeimage_gj);
			viewHolder.tslImageView = (ImageView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_typeimage_tsl);
			viewHolder.qtImageView = (ImageView) arg1
					.findViewById(R.id.listview_item_chargestationlist_infor_typeimage_qt);
			viewHolder.inforLayout = (LinearLayout) arg1
					.findViewById(R.id.listview_item_chargestationlist_layout);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (ChangeStationViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("cs_id"));

			viewHolder.distanceTextView.setText(data.get(arg0).get("distance")
					+ "KM");
			viewHolder.addrTextView.setText(data.get(arg0).get("cs_address"));

			String nameString = data.get(arg0).get("cs_name");
			if (nameString.length() > 8) {
				nameString = nameString.substring(0, 8) + "..";
			}
			viewHolder.nameTextView.setText(nameString);

			if ("1".equals(data.get(arg0).get("GB"))) {
				viewHolder.gjImageView.setVisibility(View.VISIBLE);
			} else {
				viewHolder.gjImageView.setVisibility(View.GONE);
			}

			if ("1".equals(data.get(arg0).get("BYD"))) {
				viewHolder.bydImageView.setVisibility(View.VISIBLE);
			} else {
				viewHolder.bydImageView.setVisibility(View.GONE);
			}

			if ("1".equals(data.get(arg0).get("TESLA"))) {
				viewHolder.tslImageView.setVisibility(View.VISIBLE);
			} else {
				viewHolder.tslImageView.setVisibility(View.GONE);
			}

			if ("1".equals(data.get(arg0).get("QT"))) {
				viewHolder.qtImageView.setVisibility(View.VISIBLE);
			} else {
				viewHolder.qtImageView.setVisibility(View.GONE);
			}

			viewHolder.inforTextView.setText("快充(" + data.get(arg0).get("fast")
					+ ") 慢充(" + data.get(arg0).get("slow") + ")");

			viewHolder.inforLayout.setOnClickListener(clickListener);
			viewHolder.inforLayout.setTag(data.get(arg0).get("i"));

		}

		return arg1;
	}

	private class ChangeStationViewHolder {

		public TextView idTextView, nameTextView, distanceTextView,
				addrTextView, inforTextView;
		public ImageView bydImageView, gjImageView, tslImageView, qtImageView;
		private LinearLayout inforLayout;

	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			mCallBack.click(arg0);
		}
	};

}
