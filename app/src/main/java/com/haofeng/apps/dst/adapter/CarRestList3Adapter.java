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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;


/**
 * 长租订单详情列表
 * @author qtds
 *
 */
public class CarRestList3Adapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;

	public CarRestList3Adapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mContext = context;

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
		CarRestLise3ViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_carrestlist3, null);
			viewHolder = new CarRestLise3ViewHolder();

			viewHolder.nameView = (TextView) arg1
					.findViewById(R.id.listview_item_carrestlist3_name);
			viewHolder.typeView = (TextView) arg1
					.findViewById(R.id.listview_item_carrestlist3_type);
			viewHolder.inforView = (TextView) arg1
					.findViewById(R.id.listview_item_carrestlist3_infor);

			viewHolder.inforView2 = (TextView) arg1
					.findViewById(R.id.listview_item_carrestlist3_infor2);
			viewHolder.inforView3 = (TextView) arg1
					.findViewById(R.id.listview_item_carrestlist3_infor3);
			viewHolder.inforView4 = (TextView) arg1
					.findViewById(R.id.listview_item_carrestlist3_infor4);

			viewHolder.lineImageView = (ImageView) arg1
					.findViewById(R.id.listview_item_carrestlist3_image);
			viewHolder.linearLayout = (LinearLayout) arg1
					.findViewById(R.id.listview_item_carrestlist3_line_layout);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (CarRestLise3ViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值

			if ("first".equals(data.get(arg0).get("count"))) {
				viewHolder.linearLayout.setPadding(0, 20, 0, 0);
			} else if ("last".equals(data.get(arg0).get("count"))) {
				viewHolder.linearLayout.setPadding(0, 0, 0, 40);
				viewHolder.nameView.setText("结束");
				viewHolder.inforView.setVisibility(View.GONE);
				viewHolder.inforView2.setVisibility(View.GONE);
				viewHolder.inforView3.setVisibility(View.GONE);
				viewHolder.inforView4.setVisibility(View.GONE);
				viewHolder.typeView.setVisibility(View.GONE);
			} else {
				viewHolder.linearLayout.setPadding(0, 0, 0, 0);
			}

		}

		return arg1;
	}

	private class CarRestLise3ViewHolder {

		public TextView nameView, typeView, inforView, inforView2, inforView3,
				inforView4;
		public ImageView lineImageView;
		public LinearLayout linearLayout;

	}

}
