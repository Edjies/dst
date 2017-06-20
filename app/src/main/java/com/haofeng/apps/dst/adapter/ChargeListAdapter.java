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

public class ChargeListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;

	public ChargeListAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.inflater = LayoutInflater.from(context);
		mContext = context;
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
		ChargeViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_chargelist, null);
			viewHolder = new ChargeViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.chargelist_listitem_id);
			viewHolder.nameTextView = (TextView) arg1
					.findViewById(R.id.chargelist_listitem_infor_name);
			viewHolder.addrTextView = (TextView) arg1
					.findViewById(R.id.chargelist_listitem_infor_addr);
			viewHolder.statusView = (TextView) arg1
					.findViewById(R.id.chargelist_listitem_infor_status);
			viewHolder.timeTextView = (TextView) arg1
					.findViewById(R.id.chargelist_listitem_infor_time);
			viewHolder.moneyTextView = (TextView) arg1
					.findViewById(R.id.chargelist_listitem_infor_money);
			viewHolder.time2TextView = (TextView) arg1
					.findViewById(R.id.chargelist_listitem_infor_time2);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (ChargeViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.timeTextView.setText(data.get(arg0).get("time"));
			viewHolder.time2TextView.setText(data.get(arg0).get("endtime"));
			viewHolder.addrTextView.setText(data.get(arg0).get("addr"));
			viewHolder.nameTextView.setText("单号:"
					+ data.get(arg0).get("number"));

			viewHolder.moneyTextView.setText("消费:￥"
					+ data.get(arg0).get("money"));
			viewHolder.statusView.setText(data.get(arg0).get("status_text"));

			if ("success".equals(data.get(arg0).get("status"))) {// 已支付
				viewHolder.statusView.setTextColor(mContext.getResources()
						.getColor(R.color.menugreen));
			} else {

				viewHolder.statusView.setTextColor(mContext.getResources()
						.getColor(R.color.red));
			}

		}

		return arg1;
	}

	public static class ChargeViewHolder {
		public TextView idTextView, nameTextView, timeTextView, statusView,
				moneyTextView, time2TextView, addrTextView;

	}

}
