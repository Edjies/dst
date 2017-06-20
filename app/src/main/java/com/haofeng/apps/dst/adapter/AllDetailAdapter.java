package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haofeng.apps.dst.R;

import java.util.List;
import java.util.Map;

/**
 * 明细中所有的记录
 */

public class AllDetailAdapter extends BaseAdapter {
    public List<Map<String, String>> allCarList;
    public Context context;

    public AllDetailAdapter(List<Map<String, String>> RentCarOrderList, Context context) {
        this.allCarList = RentCarOrderList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return allCarList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.view_all_rent_car_order, null);
            viewHolder = new ViewHolder();
            viewHolder.order_no = (TextView) convertView.findViewById(R.id.view_all_rent_car_order_number);  //订单号
            viewHolder.date = (TextView) convertView.findViewById(R.id.view_all_rentcar_order_date);//充值日期
            viewHolder.money = (TextView) convertView.findViewById(R.id.view_all_rentcar_order_money);//支付金额
            viewHolder.payWay = (TextView) convertView.findViewById(R.id.view_all_rentcar_order_pay_way);//支付方式
            viewHolder.explanation = (TextView) convertView.findViewById(R.id.view_all_rentcar_order_explanation);
            viewHolder.trading_type = (TextView) convertView.findViewById(R.id.view_all_rent_car_order_trading_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.order_no.setText(allCarList.get(position).get("order_no"));
        viewHolder.date.setText(allCarList.get(position).get("create_time"));
        viewHolder.money.setText("￥" + allCarList.get(position).get("money"));
        viewHolder.explanation.setText(allCarList.get(position).get("explanation"));
        if ("alipay".equals(allCarList.get(position).get("pay_way"))) {
            viewHolder.payWay.setText("支付宝");
        } else if ("wechat".equals(allCarList.get(position).get("pay_way"))) {
            viewHolder.payWay.setText("微信");
        } else if ("foregift".equals(allCarList.get(position).get("pay_way"))) {
            viewHolder.payWay.setText("押金");
        }
        String trading_type = allCarList.get(position).get("trading_type");
        switch (trading_type) {
            //1,电卡充值 2,租车下单 3,押金充值 4,还车扣款 5,违章扣款 6,取消租车 7,押金退款
            case "1":
                viewHolder.trading_type.setText("电卡充值");
                break;
            case "2":
                viewHolder.trading_type.setText("租车下单");
                break;
            case "3":
                viewHolder.trading_type.setText("押金充值");
                break;
            case "4":
                viewHolder.trading_type.setText("还车扣款");
                break;
            case "5":
                viewHolder.trading_type.setText("违章扣款");
                break;
            case "6":
                viewHolder.trading_type.setText("取消租车");
                break;
            case "7":
                viewHolder.trading_type.setText("押金退款");
                break;

        }
        return convertView;
    }

    class ViewHolder {
        TextView order_no;
        TextView date;
        TextView money;
        TextView payWay;
        TextView explanation;
        TextView trading_type;
    }
}
