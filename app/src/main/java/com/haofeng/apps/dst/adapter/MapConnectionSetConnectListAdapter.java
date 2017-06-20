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

public class MapConnectionSetConnectListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private Context mContext;

	public MapConnectionSetConnectListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mContext = context;

	}

	public void setData(List<Map<String, Object>> data) {
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
		MapConnectionSetConnectViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_maptype_connect,
					null);
			viewHolder = new MapConnectionSetConnectViewHolder();

			viewHolder.typeImageView = (ImageView) arg1
					.findViewById(R.id.list_item_maptype_connect_image);
			viewHolder.typeTextView = (TextView) arg1
					.findViewById(R.id.list_item_maptype_connect_text);

			arg1.setTag(viewHolder);

		} else {
			viewHolder = (MapConnectionSetConnectViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值

			viewHolder.typeImageView.setBackgroundResource((Integer) data.get(
					arg0).get("image"));
			viewHolder.typeTextView.setText((CharSequence) data.get(arg0).get(
					"title"));
			viewHolder.typeTextView.setTextColor(mContext.getResources()
					.getColor((Integer) data.get(arg0).get("color")));

		}

		return arg1;
	}

	private class MapConnectionSetConnectViewHolder {

		public ImageView typeImageView;
		public TextView typeTextView;

	}

}
