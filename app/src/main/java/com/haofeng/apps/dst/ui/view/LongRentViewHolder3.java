package com.haofeng.apps.dst.ui.view;

import android.view.View;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ui.LongRentActivity;
import com.haofeng.apps.dst.ui.NewOrderListActivity;

/**
 * Created by WIN10 on 2017/6/18.
 */

public class LongRentViewHolder3 implements View.OnClickListener{
    public final static int resId = R.layout.view_long_rent_3;
    private LongRentActivity mContext;
    private View mParent;
    private TextView mTvOrderNo;
    private TextView mTvQuery;
    private TextView mTvReturn;

    public LongRentViewHolder3(LongRentActivity context, View parent) {
        this.mContext = context;
        this.mParent = parent;
        this.mTvOrderNo = (TextView) mParent.findViewById(R.id.tv_order_no);
        this.mTvQuery = (TextView) mParent.findViewById(R.id.tv_query);
        this.mTvReturn = (TextView) mParent.findViewById(R.id.tv_return);
        this.mTvQuery.setOnClickListener(this);
        this.mTvReturn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.tv_query == id) {
            NewOrderListActivity.intentMe(mContext);
        }
        else if(R.id.tv_return == id) {
            mContext.finish();
        }
    }

    public void setOrderNo(String orderNo) {
        mTvOrderNo.setText("需求单号 : " + orderNo);
    }

    public void show() {
        mParent.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mParent.setVisibility(View.GONE);
    }
}
