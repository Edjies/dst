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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class CollectionListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Callback inforCallback, deletCallback;

	public CollectionListAdapter(Context context, Callback infor, Callback delet) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		inforCallback = infor;
		deletCallback = delet;

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
		CollectionViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_collection, null);
			viewHolder = new CollectionViewHolder();
			viewHolder.systimeTextView = (TextView) arg1
					.findViewById(R.id.collection_listitem_infor_systime);
			viewHolder.addrView = (TextView) arg1
					.findViewById(R.id.collection_listitem_infor_install_site);
			viewHolder.nameTextView = (TextView) arg1
					.findViewById(R.id.collection_listitem_infor_name);
			viewHolder.deletView = (TextView) arg1
					.findViewById(R.id.collection_listitem_delete);
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.collection_listitem_id);
			viewHolder.inforFrameLayout = (LinearLayout) arg1
					.findViewById(R.id.collection_listitem_inforlayout);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (CollectionViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.systimeTextView.setText(data.get(arg0).get("systime")
					.substring(0, 10));
			viewHolder.addrView
					.setText(data.get(arg0).get("cs_address_street"));

			String nameString = data.get(arg0).get("cs_name");

			viewHolder.nameTextView.setText(nameString);

			viewHolder.inforFrameLayout.setOnClickListener(inforClickListener);
			viewHolder.inforFrameLayout.setTag(data.get(arg0).get("i"));
			viewHolder.deletView.setOnClickListener(deletClickListener);
			viewHolder.deletView.setTag(data.get(arg0).get("i"));

		}

		return arg1;
	}

	private class CollectionViewHolder {

		public TextView systimeTextView, addrView, nameTextView, idTextView,
				deletView;
		private LinearLayout inforFrameLayout;

	}

	private OnClickListener inforClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			inforCallback.click(arg0);
		}
	};

	private OnClickListener deletClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			deletCallback.click(arg0);
		}
	};

}
