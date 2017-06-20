package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class DateListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;

	public DateListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mContext = context;
	}

	public interface Callback {
		public void click(View v);
	};

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
		DateViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.gridview_item_datecontent, null);
			viewHolder = new DateViewHolder();
			viewHolder.dateView = (TextView) arg1
					.findViewById(R.id.gridview_item_deatcontent_date);
			viewHolder.dateView2 = (TextView) arg1
					.findViewById(R.id.gridview_item_deatcontent_date2);

			arg1.setTag(viewHolder);

		} else {
			viewHolder = (DateViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值

			viewHolder.dateView.setText(data.get(arg0).get("date"));
			viewHolder.dateView2.setText(data.get(arg0).get("date2"));
			if ("0".equals(data.get(arg0).get("free"))) {
				viewHolder.dateView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.white));

			} else {
				viewHolder.dateView.setBackgroundColor(mContext.getResources()
						.getColor(R.color.backgroudgray));
			}

		}

		return arg1;
	}

	private class DateViewHolder {

		public TextView dateView, dateView2;

	}

}
