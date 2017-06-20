package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ui.WeiZhangPayActivity;

import java.util.List;
import java.util.Map;

/**
 * 违章信息Adapter
 */

public class WeiZhangInfoAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> wzInfoList;
    private String isDeal;

    public WeiZhangInfoAdapter(List<Map<String, String>> wzInfoList, Context context, String isDeal) {
        this.context = context;
        this.wzInfoList = wzInfoList;
        this.isDeal = isDeal;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return wzInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.listview_item_wzinfo, null);

            viewHolder.dateView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_date);
            viewHolder.areaView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_area);
            viewHolder.beizhuView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_beizhu);
            viewHolder.fenView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_fen);
            viewHolder.fakuanView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_fakuan);
            viewHolder.daijiaoView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_daijiao);
            viewHolder.statusView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_status);
            viewHolder.statusView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_status);
            viewHolder.carNumberView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_car_number);
            viewHolder.zhixunkefuView = (TextView) convertView.findViewById(R.id.lsitview_item_detail_zhixun_kefu);
            viewHolder.daijiaoView.setOnClickListener(new View.OnClickListener() { // 违章代缴
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(context, WeiZhangPayActivity.class);
                    intent.putExtra("fen", wzInfoList.get(position).get("fen"));
                    intent.putExtra("money", wzInfoList.get(position).get("money"));
                    intent.putExtra("date", wzInfoList.get(position).get("date"));
                    intent.putExtra("plate_number", wzInfoList.get(position).get("plate_number"));
                    context.startActivity(intent);
                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.areaView.setText(wzInfoList.get(position).get("area"));
        viewHolder.dateView.setText(wzInfoList.get(position).get("date"));
        viewHolder.fakuanView.setText(wzInfoList.get(position).get("money"));
        viewHolder.fenView.setText(wzInfoList.get(position).get("fen"));
        viewHolder.beizhuView.setText(wzInfoList.get(position).get("act"));
        viewHolder.carNumberView.setText(wzInfoList.get(position).get("plate_number"));
        viewHolder.zhixunkefuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "400-860-4558"));
                context.startActivity(intent);
            }
        });
        if ("0".equals(isDeal)) {  //未处理
            viewHolder.statusView.setVisibility(View.GONE);
            if ((Integer.parseInt(wzInfoList.get(position).get("fen")) > 0)) { // 咨询客服
                viewHolder.zhixunkefuView.setVisibility(View.VISIBLE);
                viewHolder.daijiaoView.setVisibility(View.GONE);
            } else { // 违章代缴
                viewHolder.zhixunkefuView.setVisibility(View.GONE);
                viewHolder.daijiaoView.setVisibility(View.VISIBLE);
            }
        } else {//已处理  //处理中
            viewHolder.zhixunkefuView.setVisibility(View.GONE);
            viewHolder.daijiaoView.setVisibility(View.GONE);
            viewHolder.statusView.setVisibility(View.VISIBLE);
            if (wzInfoList.get(position).get("status").equals("0")) {  //已处理
                viewHolder.statusView.setText("已完成");
                viewHolder.statusView.setTextColor(context.getResources().getColor(R.color.textgreen));
            } else {
                viewHolder.statusView.setText("处理中");
                viewHolder.statusView.setTextColor(context.getResources().getColor(R.color.red));
            }
        }

        return convertView;
    }

    class ViewHolder {
        TextView dateView;
        TextView areaView;
        TextView beizhuView;
        TextView fenView;
        TextView fakuanView;
        TextView daijiaoView;
        TextView zhixunkefuView;
        TextView statusView;
        TextView carNumberView;
    }

}
