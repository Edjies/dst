package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class TestAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局

	public TestAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// 在此适配器中所代表的数据集中的条目数
		return 0;
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
		ViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.testitem, null);
			viewHolder = new ViewHolder();
			viewHolder.titleView = (TextView) arg1.findViewById(R.id.textView1);
			viewHolder.imageView = (ImageView) arg1
					.findViewById(R.id.imageView1);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) arg1.getTag();
		}
		
		//这里根据实际情况，取值设值
		viewHolder.titleView.setText("");
		return arg1;
	}

	static class ViewHolder {

		TextView titleView;
		ImageView imageView;

	}

}
