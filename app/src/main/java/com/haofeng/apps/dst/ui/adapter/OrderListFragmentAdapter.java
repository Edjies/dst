package com.haofeng.apps.dst.ui.adapter;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.haofeng.apps.dst.ui.fragment.LongOrderListFragment;
import com.haofeng.apps.dst.ui.fragment.PageFragment;
import com.haofeng.apps.dst.ui.fragment.ShortOrderListFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/10.
 */
public class OrderListFragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<PageFragment> mFragments = new ArrayList<>();

    public OrderListFragmentAdapter(FragmentManager fm) {
        super(fm);
        mFragments.add(ShortOrderListFragment.newInstance());
        mFragments.add(LongOrderListFragment.newInstance());
    }

    @Override
    public PageFragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "分时订单";
            case 1:
                return "长租申请";
            default:
                return "分时订单";
        }
    }
}
