package com.haofeng.apps.dst.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

public class WalletList_allAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
	private Context mContext;

	public WalletList_allAdapter(Context context) {
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
		WalletViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater
					.inflate(R.layout.listview_item_walletlist_all, null);
			viewHolder = new WalletViewHolder();
			viewHolder.change_moneyTextView = (TextView) arg1
					.findViewById(R.id.walletlist_listitem_all_infor_money);
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.walletlist_listitem_all_infor_id);
			viewHolder.systimeTextView = (TextView) arg1
					.findViewById(R.id.walletlist_listitem_all_infor_time);
			viewHolder.noteTextView = (TextView) arg1
					.findViewById(R.id.walletlist_listitem_all_infor_note);
			viewHolder.yueTextView = (TextView) arg1
					.findViewById(R.id.walletlist_listitem_all_infor_yue);

			arg1.setTag(viewHolder);

		} else {
			viewHolder = (WalletViewHolder) arg1.getTag();
		}
		// alipay wechat

		if (data.size() > 0) {

			viewHolder.systimeTextView.setText(data.get(arg0).get("time"));

			if (!TextUtils.isEmpty(data.get(arg0).get("change_money"))
					&& !"null".equals(data.get(arg0).get("change_money"))) {
				viewHolder.change_moneyTextView.setText("￥"
						+ data.get(arg0).get("change_money"));
				if ((data.get(arg0).get("change_money")).startsWith("+")) {
					viewHolder.change_moneyTextView.setTextColor(mContext
							.getResources().getColor(R.color.textgreen));

				} else {
					viewHolder.change_moneyTextView.setTextColor(mContext
							.getResources().getColor(R.color.gray));
				}
			}

			viewHolder.noteTextView.setText(data.get(arg0).get("type"));

		}

		return arg1;
	}

	private class WalletViewHolder {
		public TextView change_moneyTextView, idTextView, systimeTextView,
				noteTextView, yueTextView;

	}

}
