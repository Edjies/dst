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

public class MyorderListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;
	private boolean isEdit = false;

	public MyorderListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mContext = context;
	}

	public void setData(List<Map<String, String>> data, boolean isEdit) {
		this.data = data;
		this.isEdit = isEdit;
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
		MyorderViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_myorder, null);
			viewHolder = new MyorderViewHolder();
			viewHolder.appointed_dateTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_appointed_date);
			viewHolder.timeTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_time);
			viewHolder.codeTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_code);
			viewHolder.code_from_componyTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_code_from_compony);
			viewHolder.install_siteTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_install_site);
			viewHolder.connection_type_txtTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_connection_type_txt);
			viewHolder.systimeTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_systime);
			viewHolder.deleteTextView = (ImageView) arg1
					.findViewById(R.id.myorder_listitem_delete);
			viewHolder.updateTextView = (ImageView) arg1
					.findViewById(R.id.myorder_listitem_update);
			viewHolder.contentImageView = (ImageView) arg1
					.findViewById(R.id.myorder_listitem_infor_image);
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_id);
			viewHolder.stanusTextView = (TextView) arg1
					.findViewById(R.id.myorder_listitem_infor_stanus);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (MyorderViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.appointed_dateTextView.setText(data.get(arg0).get(
					"appointed_date"));
			viewHolder.timeTextView.setText(data.get(arg0).get("time_start")
					+ "---" + data.get(arg0).get("time_end"));
			viewHolder.codeTextView.setText("预约" + data.get(arg0).get("code"));
			viewHolder.code_from_componyTextView.setText(data.get(arg0).get(
					"code_from_compony"));
			viewHolder.install_siteTextView.setText(data.get(arg0).get(
					"install_site"));
			viewHolder.connection_type_txtTextView.setText(data.get(arg0).get(
					"connection_type_txt"));
			viewHolder.systimeTextView.setText(data.get(arg0).get("systime"));
			viewHolder.idTextView.setText(data.get(arg0).get("id"));

			if ("GB".equals(data.get(arg0).get("connection_type"))) {
				viewHolder.contentImageView
						.setImageResource(R.drawable.fujin_guobiao);
			} else if ("BYD".equals(data.get(arg0).get("connection_type"))) {
				viewHolder.contentImageView
						.setImageResource(R.drawable.fujin_byd);
			} else if ("TESLA".equals(data.get(arg0).get("connection_type"))) {
				viewHolder.contentImageView
						.setImageResource(R.drawable.fujin_tesila);
			} else {
				viewHolder.contentImageView
						.setImageResource(R.drawable.fujin_other);
			}

			if ("0".equals(data.get(arg0).get("isfinished"))) {
				viewHolder.stanusTextView.setTextColor(mContext.getResources()
						.getColor(R.color.menugreen));
				viewHolder.stanusTextView.setText("预约成功");
			} else {
				viewHolder.stanusTextView.setTextColor(mContext.getResources()
						.getColor(R.color.yellow));
				viewHolder.stanusTextView.setText("充电结束");
			}

			if (isEdit) {
				if ("0".equals(data.get(arg0).get("isfinished"))) {
					viewHolder.updateTextView.setVisibility(View.VISIBLE);
					viewHolder.deleteTextView.setVisibility(View.VISIBLE);
				} else {
					viewHolder.updateTextView.setVisibility(View.GONE);
					viewHolder.deleteTextView.setVisibility(View.VISIBLE);
				}
			} else {
				viewHolder.updateTextView.setVisibility(View.GONE);
				viewHolder.deleteTextView.setVisibility(View.GONE);
			}
		}

		return arg1;
	}

	public static class MyorderViewHolder {

		public TextView appointed_dateTextView;
		public TextView timeTextView;
		public TextView codeTextView;
		public TextView code_from_componyTextView;
		public TextView install_siteTextView;
		public TextView connection_type_txtTextView;
		public TextView systimeTextView;
		public ImageView deleteTextView, updateTextView;
		public ImageView contentImageView;
		public TextView idTextView;
		public TextView stanusTextView;

	}

}
