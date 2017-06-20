package com.haofeng.apps.dst.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ui.adapter.OrderListFragmentAdapter;

/**
 *
 * Created by WIN10 on 2017/6/13.
 */

public class NewOrderListActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTvStatusBar;
    private ImageView mIvBack;
    private TabLayout mTlTabs;
    private ViewPager mVpContainer;
    private OrderListFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        setViews();
        setListeners();
        initData();
//        Fragment f = ShortOrderListFragment.newInstance();
//        getFragmentManager().beginTransaction().add(R.id.ll_fragment_container, f, ShortOrderListFragment.TAG).show(f).commit();

    }

    private void setViews() {
        mTvStatusBar = (TextView) findViewById(R.id.tv_status_bar);
        if(mTranslucent) {
            mTvStatusBar.getLayoutParams().height = getStatusBarHeight();
        }else {
            mTvStatusBar.setVisibility(View.GONE);
        }
        mIvBack = (ImageView) findViewById(R.id.iv_back);

        mTlTabs = (TabLayout) findViewById(R.id.tl_tabs);
        mVpContainer = (ViewPager) findViewById(R.id.vp_container);
        mAdapter = new OrderListFragmentAdapter(getSupportFragmentManager());
        mVpContainer.setAdapter(mAdapter);
        mTlTabs.setupWithViewPager(mVpContainer);
        mVpContainer.setCurrentItem(0);

    }

    private void setListeners() {
        mIvBack.setOnClickListener(this);
        mVpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mAdapter.getItem(position).onPageSelected();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.iv_back == id) {
            finish();
        }
    }

    public int getCurPosition() {
        return mVpContainer.getCurrentItem();
    }

    public final static void intentMe(Activity activity) {
        Intent intent = new Intent(activity, NewOrderListActivity.class);
        Bundle extra = new Bundle();
        intent.putExtras(extra);
        activity.startActivity(intent);
    }



}
