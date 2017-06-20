package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.utils.PublicUtil;

public class NewsCenterListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Callback deletCallback, inforCallback;
	private Context mContext;

	public NewsCenterListAdapter(Context context, Callback delet, Callback infor) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		deletCallback = delet;
		mContext = context;
		inforCallback = infor;

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
			arg1 = inflater.inflate(R.layout.listview_item_newscenter, null);
			viewHolder = new CollectionViewHolder();
			viewHolder.shanchuView = (TextView) arg1
					.findViewById(R.id.listview_item_newscenter_shanchu);
			viewHolder.contentView = (TextView) arg1
					.findViewById(R.id.listview_item_newscenter_content);
			viewHolder.timeView = (TextView) arg1
					.findViewById(R.id.listview_item_newscenter_time);
			viewHolder.statusView = (TextView) arg1
					.findViewById(R.id.listview_item_newscenter_status);
			viewHolder.layout = (LinearLayout) arg1
					.findViewById(R.id.listview_item_newscenter_toplayout);
			viewHolder.horizontalScrollView = (HorizontalScrollView) arg1
					.findViewById(R.id.listview_item_newscenter_ScrollView);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (CollectionViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.horizontalScrollView.scrollTo(0, 0);
			viewHolder.contentView.setText(data.get(arg0).get("content"));
			viewHolder.timeView.setText(data.get(arg0).get("time"));

			if ("1".equals(data.get(arg0).get("type"))) {
				viewHolder.statusView.setText("待支付");
			}

			viewHolder.shanchuView.setOnClickListener(shanchuClickListener);
			viewHolder.shanchuView.setTag(data.get(arg0).get("i"));
			// viewHolder.layout.setOnClickListener(inforClickListener);
			// viewHolder.layout.setTag(data.get(arg0).get("i"));

		}

		return arg1;
	}

	private class CollectionViewHolder {

		public TextView shanchuView, contentView, timeView, statusView;
		private LinearLayout layout;
		private HorizontalScrollView horizontalScrollView;

	}

	private OnClickListener shanchuClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			deletCallback.click(arg0);
		}
	};
	private OnClickListener inforClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			inforCallback.click(arg0);
		}
	};

}
