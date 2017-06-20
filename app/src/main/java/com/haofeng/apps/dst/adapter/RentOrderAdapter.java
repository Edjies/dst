package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.utils.PublicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 订单列表Adapter
 */

public class RentOrderAdapter extends BaseAdapter {
    private ArrayList<Map<String, String>> list;
    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    private Context mContext;
    private Callback lijizhifuCallBack, confrimMoneyCallBack, lijicongzhiCallBack, concelOrderCallBack;

    public RentOrderAdapter(Context context, Callback lijizhifuCallBack
            , Callback confrimMoneyCallBack, Callback lijicongzhiCallBack, Callback concelOrderCallBack) {
        this.lijizhifuCallBack = lijizhifuCallBack;
        this.confrimMoneyCallBack = confrimMoneyCallBack;
        this.lijicongzhiCallBack = lijicongzhiCallBack;
        this.concelOrderCallBack = concelOrderCallBack;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public interface Callback {
        public void click(View v);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.order_listview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.orderStatus = (TextView) convertView.findViewById(R.id.order_list_view_item_order_status);
            viewHolder.orderType = (ImageView) convertView.findViewById(R.id.order_list_view_item_order_type);
            viewHolder.orderNumber = (TextView) convertView.findViewById(R.id.order_list_view_item_order_number);
            viewHolder.orderQcDate = (TextView) convertView.findViewById(R.id.order_list_view_item_order_qc_date);
            viewHolder.orderQcTime = (TextView) convertView.findViewById(R.id.order_list_view_item_order_qc_time);
            viewHolder.orderTotalTime = (TextView) convertView.findViewById(R.id.order_list_view_item_order_rent_total_time);
            viewHolder.orderShowRentType = (TextView) convertView.findViewById(R.id.order_list_view_item_order_show_rent_type);
            viewHolder.orderHcDate = (TextView) convertView.findViewById(R.id.order_list_view_item_order_hc_date);
            viewHolder.orderHcTime = (TextView) convertView.findViewById(R.id.order_list_view_item_order_hc_time);
            viewHolder.orderTotalMoney = (TextView) convertView.findViewById(R.id.order_list_view_item_order_total_money);
            viewHolder.orderStatus1Layout = (LinearLayout) convertView.findViewById(R.id.order_list_view_item_order_status1_layout);
            viewHolder.orderStatus2Layout = (LinearLayout) convertView.findViewById(R.id.order_list_view_item_order_status2_layout);
            viewHolder.orderStatus3Layout = (LinearLayout) convertView.findViewById(R.id.order_list_view_item_order_status3_layout);
            viewHolder.orderStatus4Layout = (LinearLayout) convertView.findViewById(R.id.order_list_view_item_order_status4_layout);
            viewHolder.orderShowRentMoneyLayout = (LinearLayout) convertView.findViewById(R.id.order_list_view_item_order_show_rent_money_layout);

            viewHolder.orderAddYaJinLayout = (LinearLayout) convertView.findViewById(R.id.order_list_view_item_order_congzhi_yajin_layout);
            viewHolder.orderAddYaJinNeed = (TextView) convertView.findViewById(R.id.order_list_view_item_order_congzhi_yajin_need);
            viewHolder.orderDetail1 = (TextView) convertView.findViewById(R.id.order_list_view_item_order_detail_1);
            viewHolder.orderConcelOrder = (Button) convertView.findViewById(R.id.order_list_view_item_order_concel_order);
            viewHolder.orderLiJiCongZhi = (Button) convertView.findViewById(R.id.order_list_view_item_order_liji_congzhi);

            viewHolder.orderHasSubmitMoney = (TextView) convertView.findViewById(R.id.order_list_view_item_order_has_submit_money);
            viewHolder.orderDetail2 = (TextView) convertView.findViewById(R.id.order_list_view_item_order_detail2);
            viewHolder.orderHasSubmitMoneyLayout = (RelativeLayout) convertView.findViewById(R.id.order_list_view_item_order_has_submit_money_layout);

            viewHolder.orderHasMadeMoney = (TextView) convertView.findViewById(R.id.order_list_view_item_order_has_made_rent_money);
            viewHolder.orderwaitToPayMoney = (TextView) convertView.findViewById(R.id.order_list_view_item_order_wait_to_pay_money);
            viewHolder.liJiPayMoney = (Button) convertView.findViewById(R.id.order_list_view_item_order_liji_pay);
            viewHolder.orderDetail3 = (TextView) convertView.findViewById(R.id.order_list_view_item_order_detail3);

            viewHolder.nextJieShuanTime = (TextView) convertView.findViewById(R.id.order_list_view_item_order_to_next_jieshuan_time);
            viewHolder.orderDetail4 = (TextView) convertView.findViewById(R.id.order_list_view_item_order_detail4);
            viewHolder.orderYiTiCheType = (TextView) convertView.findViewById(R.id.order_list_view_item_order_yitiche_type);
            viewHolder.orderConfirmMoney = (Button) convertView.findViewById(R.id.order_list_view_item_order_confirm_money);
            viewHolder.orderHasAddMoneyLayout = (LinearLayout) convertView.findViewById(R.id.order_list_view_item_order_has_add_money_layout);
            viewHolder.orderHasAddMoney = (TextView) convertView.findViewById(R.id.order_list_view_item_order_has_add_money);
            viewHolder.orderLiJiPay2 = (Button) convertView.findViewById(R.id.order_list_view_item_order_liji_pay_2);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String, String> orderData = data.get(position);
        String order_state = orderData.get("order_state"); //0 押金未付, 1 待提车, 2 已提车, 3 已还车, 4. 已付款 5 已完结, 6 已取消
        String orderState = null;
        String orderType = orderData.get("type");
        viewHolder.orderStatus1Layout.setVisibility(View.GONE);
        viewHolder.orderStatus2Layout.setVisibility(View.GONE);
        viewHolder.orderStatus3Layout.setVisibility(View.GONE);
        viewHolder.orderStatus4Layout.setVisibility(View.GONE);
        viewHolder.orderAddYaJinLayout.setVisibility(View.GONE);
        viewHolder.orderConcelOrder.setVisibility(View.GONE);
        viewHolder.orderHasSubmitMoneyLayout.setVisibility(View.INVISIBLE);
        viewHolder.orderHasAddMoneyLayout.setVisibility(View.GONE);
        viewHolder.orderLiJiPay2.setVisibility(View.GONE);
        viewHolder.orderConfirmMoney.setVisibility(View.GONE);
        viewHolder.orderShowRentMoneyLayout.setVisibility(View.GONE);
        switch (order_state) {
            case "0":
                orderState = "押金未付(剩余时间:" + orderData.get("yajin_notpay_time") + ")";
                viewHolder.orderStatus1Layout.setVisibility(View.VISIBLE);
                viewHolder.orderAddYaJinLayout.setVisibility(View.VISIBLE);
                viewHolder.orderLiJiCongZhi.setVisibility(View.VISIBLE);
                viewHolder.orderShowRentMoneyLayout.setVisibility(View.VISIBLE);
                break;
            case "1":
                orderState = "待提车";
                viewHolder.orderStatus1Layout.setVisibility(View.VISIBLE);
                viewHolder.orderConcelOrder.setVisibility(View.VISIBLE);
                viewHolder.orderShowRentMoneyLayout.setVisibility(View.VISIBLE);
                break;
            case "2":
                orderState = "已提车";
                viewHolder.orderShowRentMoneyLayout.setVisibility(View.VISIBLE);
                viewHolder.orderStatus4Layout.setVisibility(View.VISIBLE);
                if (orderType.equals("1")) {//时租
                    viewHolder.orderYiTiCheType.setText("已产生租金");
                    viewHolder.orderHasAddMoneyLayout.setVisibility(View.GONE);
                } else {  //日租
                    viewHolder.orderYiTiCheType.setText("距离下次结算时间");
                    viewHolder.orderHasAddMoneyLayout.setVisibility(View.VISIBLE);
                }
                break;
            case "3":
                orderState = "已还车";
                viewHolder.orderStatus3Layout.setVisibility(View.VISIBLE);
                viewHolder.orderShowRentMoneyLayout.setVisibility(View.GONE);
                break;
            case "4":
                orderState = "已付款";
                viewHolder.orderStatus2Layout.setVisibility(View.VISIBLE);
                viewHolder.orderHasSubmitMoneyLayout.setVisibility(View.VISIBLE);
                viewHolder.orderShowRentMoneyLayout.setVisibility(View.GONE);
                break;
            case "5":
                orderState = "已完结";
                viewHolder.orderStatus2Layout.setVisibility(View.VISIBLE);
                viewHolder.orderHasSubmitMoneyLayout.setVisibility(View.VISIBLE);
                viewHolder.orderShowRentMoneyLayout.setVisibility(View.GONE);
                break;
            case "6":
                orderState = "已取消";
                viewHolder.orderStatus2Layout.setVisibility(View.VISIBLE);
                viewHolder.orderHasSubmitMoneyLayout.setVisibility(View.INVISIBLE);
                viewHolder.orderShowRentMoneyLayout.setVisibility(View.VISIBLE);
                break;
        }
        String orderNo = orderData.get("order_no");
        String orderQctime = orderData.get("qc_time");
        String orderHctime = orderData.get("hc_time");
        String orderRentAmount = orderData.get("rent_amount");
        String orderHasPayedMoney = orderData.get("has_pay_amount");
        String orderRealRentMoney = orderData.get("rent_real_amount");
        String orderJieShuanTimeHas= orderData.get("jieshuan_time_has");
        String qcDate = orderQctime.substring(5, 10);
        String qcTime = orderQctime.substring(11, 17);
        String qcXinQi = PublicUtil.getWeekOfDate(orderQctime);
        String hcDate = orderHctime.substring(5, 10);
        String hcTime = orderHctime.substring(11, 17);
        String hcXinQi = PublicUtil.getWeekOfDate(orderHctime);
        int totalHour = 0;
        int totalDay = 0;
        viewHolder.orderStatus.setText(orderState);
        viewHolder.orderNumber.setText(orderNo);
        viewHolder.orderQcDate.setText(qcDate);
        viewHolder.orderQcTime.setText(qcXinQi + " " + qcTime);
        viewHolder.orderHcDate.setText(hcDate);
        viewHolder.orderHcTime.setText(hcXinQi + " " + hcTime);
        if (orderType.equals("1")) {  //时租
            totalHour = PublicUtil.compareHour(orderQctime, orderHctime);
            viewHolder.orderType.setImageResource(R.drawable.shizu);
            viewHolder.orderTotalTime.setText(totalHour);
            viewHolder.orderShowRentType.setText("小时");
            viewHolder.nextJieShuanTime.setText(orderJieShuanTimeHas);
        } else {  //日租
            totalDay = PublicUtil.compareDay(orderQctime, orderHctime);
            viewHolder.orderType.setImageResource(R.drawable.rizu);
            viewHolder.orderTotalTime.setText(totalDay);
            viewHolder.orderShowRentType.setText("天");
        }
        viewHolder.orderTotalMoney.setText(orderRentAmount);
        viewHolder.orderHasSubmitMoney.setText(orderHasPayedMoney);
        viewHolder.orderHasMadeMoney.setText(orderRealRentMoney);
        viewHolder.orderHasAddMoney.setText(orderRealRentMoney);

        return convertView;
    }

    class ViewHolder {
        TextView orderStatus, orderNumber, orderQcDate, orderQcTime,
                orderTotalTime, orderShowRentType, orderHcDate, orderHcTime, orderTotalMoney, orderAddYaJinNeed, orderDetail1, orderHasSubmitMoney, orderDetail2, orderHasMadeMoney, orderwaitToPayMoney, nextJieShuanTime, orderDetail3, orderDetail4, orderYiTiCheType, orderHasAddMoney;
        ImageView orderType;
        LinearLayout orderAddYaJinLayout, orderStatus1Layout, orderStatus2Layout, orderStatus3Layout, orderStatus4Layout, orderHasAddMoneyLayout, orderShowRentMoneyLayout;
        RelativeLayout orderHasSubmitMoneyLayout;
        Button orderConcelOrder, orderLiJiCongZhi, liJiPayMoney, orderConfirmMoney, orderLiJiPay2;
    }
}
