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

public class WeiZhangListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;

	public WeiZhangListAdapter(Context context) {
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
		WeiZhangViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_weizhanglist, null);
			viewHolder = new WeiZhangViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_id);
			viewHolder.danhaoView = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_danhao);
			viewHolder.chehaoView = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_chehao);
			viewHolder.inforView = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_infor);
			viewHolder.inforView2 = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_infor2);
			viewHolder.moneyView = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_money);
			viewHolder.timeView = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_time);
			viewHolder.statusView = (TextView) arg1
					.findViewById(R.id.weizhanglist_listitem_status);
			arg1.setTag(viewHolder);

		} else {
			viewHolder = (WeiZhangViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get(
					"wz_foregift_state"));
			viewHolder.chehaoView.setText(data.get(arg0).get("plate_number"));
			viewHolder.inforView.setText(data.get(arg0).get("area"));
			viewHolder.inforView2.setText(data.get(arg0).get("act"));
			viewHolder.timeView.setText(data.get(arg0).get("date"));

			viewHolder.danhaoView.setText("订单:"
					+ data.get(arg0).get("order_no"));
			viewHolder.moneyView.setText("￥ " + data.get(arg0).get("money"));

			if ("2".equals(data.get(arg0).get("wz_foregift_state"))) {// 已支付
				viewHolder.statusView.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
				viewHolder.statusView.setText("已支付");
			} else if ("1".equals(data.get(arg0).get("wz_foregift_state"))) {// 已结算
				viewHolder.statusView.setTextColor(mContext.getResources()
						.getColor(R.color.gray));
				viewHolder.statusView.setText("已结算");
			} else {// 待结算

				viewHolder.statusView.setTextColor(mContext.getResources()
						.getColor(R.color.yellow2));
				viewHolder.statusView.setText("待结算");
			}

		}

		return arg1;
	}

	public static class WeiZhangViewHolder {
		public TextView idTextView, danhaoView, chehaoView, inforView,
				inforView2, moneyView, timeView, statusView;

	}

}
