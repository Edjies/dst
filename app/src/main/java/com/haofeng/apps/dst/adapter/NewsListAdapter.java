package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.widget.ListView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.adapter.NewsList_ListviewAdapter.ItemCallback;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;
	private Callback topinforCallback;
	private ItemCallback itemCallback;

	public NewsListAdapter(Context context, Callback topinforCallback,
			ItemCallback itemCallback) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.topinforCallback = topinforCallback;
		this.itemCallback = itemCallback;

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
		NewsListViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_news, null);
			viewHolder = new NewsListViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.listview_item_news_id);
			viewHolder.dateTextView = (TextView) arg1
					.findViewById(R.id.listview_item_news_infor_date);
			viewHolder.topinforTextView = (TextView) arg1
					.findViewById(R.id.listview_item_news_infor_topinfor);
			viewHolder.topLayout = (FrameLayout) arg1
					.findViewById(R.id.listview_item_news_infor_toplayout);
			viewHolder.topinforImageView = (ImageView) arg1
					.findViewById(R.id.listview_item_news_infor_topimage);
			viewHolder.topinforImageView2 = (ImageView) arg1
					.findViewById(R.id.listview_item_news_infor_topimage2);
			viewHolder.inforListView = (ListView) arg1
					.findViewById(R.id.listview_item_news_infor_listview);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (NewsListViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.dateTextView.setText(data.get(arg0).get("date"));
			viewHolder.topinforTextView.setText(data.get(arg0).get("topinfor"));
			if (TextUtils.isEmpty(data.get(arg0).get("topimage"))) {
				viewHolder.topinforImageView.setVisibility(View.VISIBLE);
			} else {
				viewHolder.topinforImageView.setVisibility(View.GONE);
				viewHolder.topinforImageView2.setVisibility(View.VISIBLE);
				ImageLoader.getInstance().displayImage(
						data.get(arg0).get("topimage"),
						viewHolder.topinforImageView2);
			}

			viewHolder.topLayout.setOnClickListener(topinforClickListener);
			viewHolder.topLayout.setTag(data.get(arg0).get("i"));

			if (data.get(arg0).get("listcount") != null) {

				int listcount = Integer.parseInt(data.get(arg0)
						.get("listcount"));

				if (listcount > 0) {
					viewHolder.inforListView.setVisibility(View.VISIBLE);
					List<Map<String, String>> listviewList = new ArrayList<Map<String, String>>();
					NewsList_ListviewAdapter list_ListviewAdapter = new NewsList_ListviewAdapter(
							mContext, itemCallback);
					viewHolder.inforListView.setAdapter(list_ListviewAdapter);

					for (int j = 0; j < listcount; j++) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("count", data.get(arg0).get("count" + j));// 这里仅仅是个标志位，用来标记全部列表中的数据
						map.put("id", data.get(arg0).get("list_id" + j));
						map.put("title", data.get(arg0).get("list_title" + j));
						map.put("image", data.get(arg0).get("list_image" + j));
						listviewList.add(map);

					}

					list_ListviewAdapter.setData(listviewList);
					list_ListviewAdapter.notifyDataSetChanged();
				}

			} else {

				viewHolder.inforListView.setVisibility(View.GONE);
			}

		}

		return arg1;
	}

	private class NewsListViewHolder {

		public TextView dateTextView, topinforTextView, idTextView;
		private FrameLayout topLayout;
		private ImageView topinforImageView, topinforImageView2;
		private ListView inforListView;

	}

	private OnClickListener topinforClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			topinforCallback.click(arg0);
		}
	};

}
