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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsList_ListviewAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private ItemCallback itemCallback;

	public NewsList_ListviewAdapter(Context context, ItemCallback itemCallback) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.itemCallback = itemCallback;
	}

	public interface ItemCallback {
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
		NewsList_ListviewViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(
					R.layout.listview_item_news_inforlistview_item, null);
			viewHolder = new NewsList_ListviewViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.listview_item_news_infor_listview_item_id);
			viewHolder.titleTextView = (TextView) arg1
					.findViewById(R.id.listview_item_news_infor_listview_item_title);
			viewHolder.layout = (FrameLayout) arg1
					.findViewById(R.id.listview_item_news_infor_listview_item_layout);
			viewHolder.imageView = (ImageView) arg1
					.findViewById(R.id.listview_item_news_infor_listview_item_image);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (NewsList_ListviewViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.titleTextView.setText(data.get(arg0).get("title"));

			if (TextUtils.isEmpty(data.get(arg0).get("image"))) {
				viewHolder.imageView.setBackgroundResource(R.drawable.no_pic2);
			} else {
				ImageLoader.getInstance().displayImage(
						data.get(arg0).get("image"), viewHolder.imageView);

			}

			viewHolder.layout.setOnClickListener(itemClickListener);
			viewHolder.layout.setTag(data.get(arg0).get("count"));

		}

		return arg1;
	}

	private class NewsList_ListviewViewHolder {

		public TextView titleTextView, idTextView;
		private FrameLayout layout;
		private ImageView imageView;

	}

	private OnClickListener itemClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			itemCallback.click(arg0);
		}
	};

}
