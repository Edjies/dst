package com.haofeng.apps.dst.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ui.CarRestListDetail2Activity;
import com.haofeng.apps.dst.utils.PublicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarRestListAdapter extends BaseAdapter {
    private static final String TAG = "CarRestListAdapter";
    private LayoutInflater inflater;// 得到一个LayoutInfalter对象用来导入布局
    private List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    private Context mContext;
    private Callback inforCallback, tarendqCallback, lijifkCallback, xuzuCallback, shenqinghcCallback,
            quxiaodingdanCallback, yihuancheCallback;
    private String isConfirmHuanCar;

    public CarRestListAdapter(Context context, Callback inforCallback, Callback tarendqCallback,
                              Callback lijifkCallback, Callback xuzuCallback, Callback shenqinghcCallback,
                              Callback quxiaodingdanCallback) {
        // TODO Auto-generated constructor stub
        this.inflater = LayoutInflater.from(context);
        mContext = context;
        this.inforCallback = inforCallback;
        this.tarendqCallback = tarendqCallback;
        this.lijifkCallback = lijifkCallback;
        this.xuzuCallback = xuzuCallback;
        this.shenqinghcCallback = shenqinghcCallback;
        this.quxiaodingdanCallback = quxiaodingdanCallback;

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
        WalletViewHolder viewHolder = null;
        if (arg1 == null) {
            arg1 = inflater.inflate(R.layout.listview_item_carrestlist, null);
            viewHolder = new WalletViewHolder();
            viewHolder.idView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_id);
            viewHolder.zhuangtaiView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_zhuangtai);
            viewHolder.zhuangtai_timeView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_zhuangtai_time);
            viewHolder.zuche_dateView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_zuche_date);
            viewHolder.zuche_timeView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_zuche_time);
            viewHolder.zuche_zujinView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_zuche_zujin);
            viewHolder.zhuangtaiImageView = (ImageView) arg1
                    .findViewById(R.id.listview_item_carrestlist_zhuangtai_image);
            viewHolder.querenhuancheView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_querenhc);
            viewHolder.yajinView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_yajin);
            viewHolder.baoxian_zhuangtaiView = (TextView) arg1
                    .findViewById(R.id.listview_item_carrestlist_baoxian_zhuangtai);
            viewHolder.baoxianView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_baoxian);
            viewHolder.fukuan_jiesuanLayout = (FrameLayout) arg1
                    .findViewById(R.id.listview_item_carrestlist_zuijin_jiesuan_layout);
            viewHolder.zuijinfkLayout = (LinearLayout) arg1
                    .findViewById(R.id.listview_item_carrestlist_zuijinfukuan_layout);
            viewHolder.zujinjsLayout = (LinearLayout) arg1.findViewById(R.id.listview_item_carrestlist_jiesuan_layout);
            viewHolder.zuijinfkView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_zuijinfukuan_time);
            viewHolder.zujinjsView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_jiesuan_money);
            viewHolder.hejiLayout = (LinearLayout) arg1.findViewById(R.id.listview_item_carrestlist_heji_layout);
            viewHolder.hejiView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_heji_money);
            viewHolder.hejiinforView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_heji_moneyinfor);
            viewHolder.lichengfyView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_heji_lichengfeiyong);
            viewHolder.xiangqingView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_dingdanxiangqing);
            viewHolder.xuzuView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_xuzu);
            viewHolder.quxiaodingdanView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_quxiao);
            viewHolder.tarendqView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_tarendq);
            viewHolder.lijifkView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_lijifk);
            viewHolder.shenqinghcView = (TextView) arg1.findViewById(R.id.listview_item_carrestlist_shenqinghc);
            arg1.setTag(viewHolder);
        } else {
            viewHolder = (WalletViewHolder) arg1.getTag();
        }

        if (data.size() > 0) {

            viewHolder.idView.setText(data.get(arg0).get("i"));
            int type = Integer.parseInt(data.get(arg0).get("type"));

            viewHolder.zuche_zujinView.setText("￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("zc_amount")), 2));
            viewHolder.zhuangtaiImageView.setVisibility(View.GONE);
            viewHolder.zuche_dateView.setText("时长:" + data.get(arg0).get("timepading"));

            viewHolder.zuche_timeView.setText(data.get(arg0).get("qc_time") + "——" + data.get(arg0).get("hc_time"));
            float yajin = PublicUtil.toTwo(Float.parseFloat(data.get(arg0).get("actual_foregift_amount")));
            viewHolder.yajinView.setText("￥" + PublicUtil.roundByScale(yajin, 2));
            float baoxian = PublicUtil.toTwo(Float.parseFloat(data.get(arg0).get("safe_amount"))
                    + Float.parseFloat(data.get(arg0).get("iop_amount")));
            viewHolder.baoxianView.setText("￥" + PublicUtil.roundByScale(baoxian, 2));
            viewHolder.zujinjsLayout.setVisibility(View.GONE);
            viewHolder.hejiLayout.setVisibility(View.GONE);
            viewHolder.lichengfyView.setVisibility(View.GONE);
            viewHolder.xuzuView.setVisibility(View.GONE);
            viewHolder.tarendqView.setVisibility(View.GONE);
            viewHolder.shenqinghcView.setVisibility(View.GONE);
            viewHolder.quxiaodingdanView.setVisibility(View.GONE);
            viewHolder.querenhuancheView.setVisibility(View.GONE);
            viewHolder.xiangqingView.setVisibility(View.VISIBLE);

            // 0 预付款，1已支付，2已提车，3已还车，4租金结算，5已取消 6，违章确认
            if ("0".equals(data.get(arg0).get("order_state"))) {
                viewHolder.zhuangtaiView.setText("待付款");
                viewHolder.hejiView.setText("￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("heji")), 2));
                viewHolder.hejiinforView.setText("(含租车押金￥" + PublicUtil.roundByScale(yajin, 2) + ")");
                viewHolder.hejiLayout.setVisibility(View.VISIBLE);
                viewHolder.lichengfyView.setText(
                        "里程费用:起步￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("lowest_price")), 2) + "("
                                + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("lowest_mileage")), 2)
                                + "公里内)+￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("mileage_price")), 2) + "/公里");
                viewHolder.lichengfyView.setVisibility(View.VISIBLE);
                viewHolder.lijifkView.setVisibility(View.VISIBLE);
                viewHolder.quxiaodingdanView.setVisibility(View.VISIBLE);
            } else if ("1".equals(data.get(arg0).get("order_state"))) {
                viewHolder.zhuangtaiView.setText("待提车");
                viewHolder.hejiView.setText("￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("heji")), 2));
                viewHolder.hejiinforView.setText("(含租车押金￥" + PublicUtil.roundByScale(yajin, 2) + ")");
                viewHolder.hejiLayout.setVisibility(View.VISIBLE);
                viewHolder.lichengfyView.setText(
                        "里程费用:起步￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("lowest_price")), 2) + "("
                                + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("lowest_mileage")), 2)
                                + "公里内)+￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("mileage_price")), 2) + "/公里");
                viewHolder.lichengfyView.setVisibility(View.VISIBLE);
                viewHolder.tarendqView.setVisibility(View.VISIBLE);
                viewHolder.lijifkView.setVisibility(View.GONE);
                viewHolder.quxiaodingdanView.setVisibility(View.VISIBLE);
            } else if ("2".equals(data.get(arg0).get("order_state"))) {
                viewHolder.zhuangtaiView.setText("已提车");
                viewHolder.hejiView.setText("￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("heji")), 2));
                viewHolder.hejiinforView.setText("(含租车押金￥" + PublicUtil.roundByScale(yajin, 2) + ")");
                viewHolder.hejiLayout.setVisibility(View.VISIBLE);
                viewHolder.lichengfyView.setText(
                        "里程费用:起步￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("lowest_price")), 2) + "("
                                + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("lowest_mileage")), 2)
                                + "公里内)+￥" + PublicUtil.roundByScale(Double.parseDouble(data.get(arg0).get("mileage_price")), 2) + "/公里");
                viewHolder.lichengfyView.setVisibility(View.VISIBLE);
                viewHolder.lijifkView.setVisibility(View.GONE);
                String isConfirmHuanCar = PublicUtil.getStorage_string(mContext, "isConfirmHuanCar", "0");
                if (isConfirmHuanCar.equals("1")) {
                    viewHolder.xuzuView.setVisibility(View.GONE);
                    viewHolder.shenqinghcView.setVisibility(View.GONE);
                    if (data.get(arg0).get("amount_confirm").equals("1")) {
                        viewHolder.querenhuancheView.setVisibility(View.GONE);
                    } else {
                        viewHolder.querenhuancheView.setVisibility(View.VISIBLE);
                    }
                    viewHolder.querenhuancheView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, CarRestListDetail2Activity.class);
                            intent.putExtra("confirmMoney", "1");
                            intent.putExtra("order_no", data.get(0).get("order_no"));
                            mContext.startActivity(intent);
                        }
                    });
                    Log.e(TAG, "isConfirmHuanCar: " + isConfirmHuanCar);
                } else {
                    viewHolder.xuzuView.setVisibility(View.VISIBLE);
                    viewHolder.shenqinghcView.setVisibility(View.VISIBLE);
                    viewHolder.querenhuancheView.setVisibility(View.GONE);
                    Log.e(TAG, "isConfirmHuanCar: " + isConfirmHuanCar);
                }

            } else if ("3".equals(data.get(arg0).get("order_state")) || "6".equals(data.get(arg0).get("order_state"))) {
                viewHolder.zhuangtaiView.setText("已还车");
                viewHolder.lijifkView.setVisibility(View.GONE);
                viewHolder.zujinjsLayout.setVisibility(View.VISIBLE);
                viewHolder.zujinjsView.setText("￥" + data.get(arg0).get("total_amount"));
                viewHolder.xuzuView.setVisibility(View.GONE);
            } else if ("4".equals(data.get(arg0).get("order_state"))) {
                viewHolder.zhuangtaiView.setText("已完结");
                viewHolder.lijifkView.setVisibility(View.GONE);
                viewHolder.zujinjsLayout.setVisibility(View.VISIBLE);
                viewHolder.zujinjsView.setText("￥" + data.get(arg0).get("total_amount"));

            } else if ("5".equals(data.get(arg0).get("order_state"))) {
                viewHolder.zhuangtaiView.setText("已取消");
                viewHolder.lijifkView.setVisibility(View.GONE);
            }
            viewHolder.xiangqingView.setOnClickListener(inforClickListener);
            viewHolder.xiangqingView.setTag(data.get(arg0).get("i"));
            viewHolder.xuzuView.setOnClickListener(xuzuClickListener);
            viewHolder.xuzuView.setTag(data.get(arg0).get("i"));
            viewHolder.tarendqView.setOnClickListener(tarendqClickListener);
            viewHolder.tarendqView.setTag(data.get(arg0).get("i"));
            viewHolder.lijifkView.setOnClickListener(lijifkClickListener);
            viewHolder.lijifkView.setTag(data.get(arg0).get("i"));
            viewHolder.shenqinghcView.setOnClickListener(shenqinghcClickListener);
            viewHolder.shenqinghcView.setTag(data.get(arg0).get("i"));
            viewHolder.quxiaodingdanView.setOnClickListener(quxiaodingdanClickListener);
            viewHolder.quxiaodingdanView.setTag(data.get(arg0).get("i"));
        }

        return arg1;
    }

    private class WalletViewHolder {
        public TextView idView, zhuangtaiView, zhuangtai_timeView, zuche_dateView, zuche_timeView, zuche_zujinView,
                yajin_zhuangtaiView, yajinView, baoxian_zhuangtaiView, baoxianView, zuijinfkView, zujinjsView, hejiView,
                hejiinforView, lichengfyView;
        public TextView xiangqingView, xuzuView, tarendqView, lijifkView, shenqinghcView, quxiaodingdanView,
                querenhuancheView;
        public ImageView zhuangtaiImageView;
        public FrameLayout fukuan_jiesuanLayout;
        public LinearLayout zuijinfkLayout, zujinjsLayout, hejiLayout;

    }

    private OnClickListener inforClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            inforCallback.click(arg0);

        }
    };

    private OnClickListener tarendqClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            tarendqCallback.click(arg0);

        }
    };
    private OnClickListener lijifkClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            lijifkCallback.click(arg0);
        }
    };

    private OnClickListener xuzuClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            xuzuCallback.click(arg0);

        }
    };
    private OnClickListener shenqinghcClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            shenqinghcCallback.click(arg0);

        }
    };

    private OnClickListener quxiaodingdanClickListener = new OnClickListener() {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            quxiaodingdanCallback.click(arg0);

        }
    };

}
