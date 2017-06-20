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

public class ShareListAdapter extends BaseAdapter {
	private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
	private List<Map<String, String>> data = new ArrayList<Map<String, String>>();

	public ShareListAdapter(Context context) {
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
		SharelistViewHolder viewHolder = null;
		if (arg1 == null) {
			arg1 = inflater.inflate(R.layout.listview_item_sharelist, null);
			viewHolder = new SharelistViewHolder();
			viewHolder.idTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_id);
			viewHolder.share_timeTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_share_time);
			viewHolder.code_from_factoryTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_code_from_factory);
			viewHolder.specificationTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_specification);
			viewHolder.wire_lengthTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_wire_length);
			viewHolder.charge_gun_numsTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_charge_gun_nums);
			viewHolder.install_type_txtTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_install_type_txt);
			viewHolder.manufacturer_txtTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_manufacturer_txt);
			viewHolder.model_txtTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_model_txt);
			viewHolder.charge_type_txtTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_charge_type_txt);
			viewHolder.connection_type_txtTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_connection_type_txt);
			viewHolder.approve_statusTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_approve_status);
			viewHolder.approve_timeTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_approve_time);
			viewHolder.approve_markTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_approve_mark);
			viewHolder.install_siteTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_install_site);
			viewHolder.codeTextView = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_share_cound);
			viewHolder.deleteView = (ImageView) arg1
					.findViewById(R.id.sharelist_listitem_delete);
			viewHolder.line2View = (ImageView) arg1
					.findViewById(R.id.sharelist_listitem_infor_imageline2);
			viewHolder.shenhemarkLayout = (LinearLayout) arg1
					.findViewById(R.id.sharelist_listitem_infor_shenhebeizhulayout);
			viewHolder.shenheshijianLayout = (LinearLayout) arg1
					.findViewById(R.id.sharelist_listitem_infor_shenheshijianlayout);
			viewHolder.charge_type_image = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_charge_type_image);
			viewHolder.connection_type_image = (TextView) arg1
					.findViewById(R.id.sharelist_listitem_infor_connection_type_image);

			arg1.setTag(viewHolder);

		} else {
			viewHolder = (SharelistViewHolder) arg1.getTag();
		}

		if (data.size() > 0) {
			// 这里根据实际情况，取值设值
			viewHolder.idTextView.setText(data.get(arg0).get("id"));
			viewHolder.codeTextView.setText("分享" + data.get(arg0).get("code"));
			viewHolder.share_timeTextView.setText(data.get(arg0).get(
					"share_time"));
			viewHolder.code_from_factoryTextView.setText(data.get(arg0).get(
					"code_from_factory"));
			viewHolder.specificationTextView.setText(data.get(arg0).get(
					"specification"));
			viewHolder.wire_lengthTextView.setText(data.get(arg0).get(
					"wire_length"));
			viewHolder.charge_gun_numsTextView.setText(data.get(arg0).get(
					"charge_gun_nums"));
			viewHolder.install_type_txtTextView.setText(data.get(arg0).get(
					"install_type_txt"));
			viewHolder.manufacturer_txtTextView.setText(data.get(arg0).get(
					"manufacturer_txt"));
			viewHolder.model_txtTextView.setText(data.get(arg0)
					.get("model_txt"));
			viewHolder.charge_type_txtTextView.setText(data.get(arg0)
					.get("charge_type_txt").substring(0, 3));
			viewHolder.connection_type_txtTextView.setText(data.get(arg0).get(
					"connection_type_txt"));
			viewHolder.install_siteTextView.setText(data.get(arg0).get(
					"install_site"));

			if ("DC".equals(data.get(arg0).get("charge_type"))) {
				viewHolder.charge_type_image
						.setBackgroundResource(R.drawable.type_dc);
			} else if ("AC".equals(data.get(arg0).get("charge_type"))) {
				viewHolder.charge_type_image
						.setBackgroundResource(R.drawable.type_ac);
			} else if ("AC_DC".equals(data.get(arg0).get("charge_type"))) {
				viewHolder.charge_type_image
						.setBackgroundResource(R.drawable.type_ac_dc);
			} else if ("DC_DC".equals(data.get(arg0).get("charge_type"))) {
				viewHolder.charge_type_image
						.setBackgroundResource(R.drawable.type_dc_dc);
			} else if ("AC_AC".equals(data.get(arg0).get("charge_type"))) {
				// viewHolder.charge_type_image
				// .setBackgroundResource(R.drawable.type_ac_ac);
			}

			if ("BYD".equals(data.get(arg0).get("connection_type"))) {
				viewHolder.connection_type_image
						.setBackgroundResource(R.drawable.fujin_byd);
			} else if ("TESLA".equals(data.get(arg0).get("connection_type"))) {
				viewHolder.connection_type_image
						.setBackgroundResource(R.drawable.fujin_tesila);
			} else if ("GB".equals(data.get(arg0).get("connection_type"))) {
				viewHolder.connection_type_image
						.setBackgroundResource(R.drawable.fujin_guobiao);
			} else {
				viewHolder.connection_type_image
						.setBackgroundResource(R.drawable.fujin_other);
			}

			int approve_status = Integer.parseInt(data.get(arg0).get(
					"approve_status"));
			if (approve_status == 0) {// 审核中
				viewHolder.shenheshijianLayout.setVisibility(View.GONE);
				viewHolder.line2View.setVisibility(View.GONE);
				viewHolder.shenhemarkLayout.setVisibility(View.GONE);
				viewHolder.deleteView.setVisibility(View.VISIBLE);
				viewHolder.approve_statusTextView
						.setBackgroundResource(R.drawable.share_checking);
				viewHolder.approve_statusTextView.setText("审核中");
			} else if (approve_status == 1) {// 审核未通过
				viewHolder.shenheshijianLayout.setVisibility(View.VISIBLE);
				viewHolder.line2View.setVisibility(View.VISIBLE);
				viewHolder.shenhemarkLayout.setVisibility(View.VISIBLE);
				viewHolder.deleteView.setVisibility(View.VISIBLE);
				viewHolder.approve_statusTextView.setText("审核未通过");
				viewHolder.approve_statusTextView
						.setBackgroundResource(R.drawable.share_no_pass);
				viewHolder.approve_markTextView.setText(data.get(arg0).get(
						"approve_mark"));
				viewHolder.approve_timeTextView.setText(data.get(arg0).get(
						"approve_time"));

			} else if (approve_status == 2) {// 审核通过
				viewHolder.shenheshijianLayout.setVisibility(View.VISIBLE);
				viewHolder.line2View.setVisibility(View.VISIBLE);
				viewHolder.shenhemarkLayout.setVisibility(View.GONE);
				viewHolder.deleteView.setVisibility(View.GONE);
				viewHolder.approve_statusTextView.setText("审核通过");
				viewHolder.approve_statusTextView
						.setBackgroundResource(R.drawable.share_pass);
				viewHolder.approve_timeTextView.setText(data.get(arg0).get(
						"approve_time"));
			}

		}

		return arg1;
	}

	public static class SharelistViewHolder {
		public TextView idTextView;
		public TextView share_timeTextView;
		public TextView code_from_factoryTextView;
		public TextView specificationTextView;
		public TextView wire_lengthTextView;
		public TextView charge_gun_numsTextView;
		public TextView install_type_txtTextView;
		public TextView manufacturer_txtTextView;
		public TextView model_txtTextView;
		public TextView charge_type_txtTextView, charge_type_image;
		public TextView connection_type_txtTextView, connection_type_image;
		public TextView install_siteTextView;
		public TextView approve_statusTextView;
		public TextView approve_timeTextView;
		public TextView approve_markTextView;
		public ImageView deleteView;
		public ImageView line2View;
		public TextView codeTextView;
		public LinearLayout shenhemarkLayout, shenheshijianLayout;

	}

}
