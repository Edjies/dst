package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class SuggestListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	public SuggestListAdapter(Context context) {
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
		SuggestViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_suggestlist, null);
			viewHolder = new SuggestViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.suggestlist_listitem_id);
			viewHolder.codeTextView = (TextView) arg1
					.findViewById(R.id.suggestlist_listitem_infor_code);
			// viewHolder.titleTextView = (TextView) arg1
			// .findViewById(R.id.suggestlist_listitem_infor_title);
			viewHolder.timeTextView = (TextView) arg1
					.findViewById(R.id.suggestlist_listitem_infor_time);
			viewHolder.contentTextView = (TextView) arg1
					.findViewById(R.id.suggestlist_listitem_infor_content);
			viewHolder.responderTextView = (TextView) arg1
					.findViewById(R.id.suggestlist_listitem_infor_responder);
			viewHolder.respond_timeTextView = (TextView) arg1
					.findViewById(R.id.suggestlist_listitem_infor_respond_time);
			viewHolder.respond_contentTextView = (TextView) arg1
					.findViewById(R.id.suggestlist_listitem_infor_respond_content);
			viewHolder.respondLayout = (LinearLayout) arg1
					.findViewById(R.id.suggestlist_listitem_infor_respond_layout);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (SuggestViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {

			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.codeTextView.setText("建议" + data.get(arg0).get("code"));
			// viewHolder.titleTextView.setText("建议："
			// + data.get(arg0).get("vs_title"));
			viewHolder.timeTextView.setText(data.get(arg0).get("vs_time"));
			viewHolder.contentTextView
					.setText(data.get(arg0).get("vs_content"));

			int vs_responder_id = Integer.parseInt(data.get(arg0).get(
					"vs_responder_id"));
			if (vs_responder_id <= 0) {
				viewHolder.respondLayout.setVisibility(View.GONE);
				viewHolder.responderTextView.setVisibility(View.GONE);
			} else {
				viewHolder.respondLayout.setVisibility(View.VISIBLE);
				viewHolder.responderTextView.setVisibility(View.GONE);
				viewHolder.respond_timeTextView.setText(data.get(arg0).get(
						"vs_respond_time"));
				viewHolder.respond_contentTextView.setText(data.get(arg0).get(
						"vs_responder")
						+ ":" + data.get(arg0).get("vs_respond_txt"));
			}

		}

		return arg1;
	}

	public static class SuggestViewHolder {
		public TextView idTextView;
		public TextView codeTextView;
		// public TextView titleTextView;
		public TextView timeTextView;
		public TextView contentTextView;
		public TextView responderTextView;
		public TextView respond_timeTextView;
		public TextView respond_contentTextView;
		public LinearLayout respondLayout;

	}

}
