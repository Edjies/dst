package com.haofeng.apps.dst.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.ServerManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.ResLongOrder;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.NewOrderListActivity;
import com.haofeng.apps.dst.ui.adapter.LongOrderAdapter;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.haofeng.apps.dst.utils.ULog;
import com.haofeng.apps.dst.utils.http.UVolley;
import com.haofeng.apps.dst.utils.http.UrlBuilder;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * 短租订单列表
 * Created by WIN10 on 2017/6/13.
 */

public class LongOrderListFragment extends PageFragment {
    public final static String TAG = LongOrderListFragment.class.getSimpleName();

    private PullToRefreshListView mCvPtr;
    private ListView mLvContent;
    private LongOrderAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_short_order_list, container, false);
        setViews(v);
        setListeners();
        initData();
        return v;
    }



    private void setViews(View rootView) {
        mCvPtr = (PullToRefreshListView) rootView.findViewById(R.id.lv_content);
        ILoadingLayout startLabels = mCvPtr
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在刷新...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabels = mCvPtr.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉加载更多...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载...");// 刷新时
        endLabels.setReleaseLabel("放开加载...");// 下来达到一定距离时，显示的提示
        mLvContent = mCvPtr.getRefreshableView();
        mAdapter = new LongOrderAdapter(mActivity);
        mLvContent.setAdapter(mAdapter);

    }

    private void setListeners() {
        mCvPtr.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                execGetOrderList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    private void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if(isPageSelected()) {
            execGetOrderList();
        }
    }

    @Override
    public void onPageSelected() {
        if(isResumed()) {
            execGetOrderList();
        }
    }

    private boolean isPageSelected() {
        if(((NewOrderListActivity) mActivity).getCurPosition() == 1) {
            return true;
        }else {
            return false;
        }
    }

    private void execGetOrderList() {
        showProgress("加载中...");
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/car/long_order_list")
                    .append("user_id", PublicUtil.getStorage_string(mActivity, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(mActivity, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(TAG, url);
        UVolley.getVolley(mActivity).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                mCvPtr.onRefreshComplete();
                hideProgress();
                ULog.e(TAG, response.toString());
                if(isResumed()) {
                    ResLongOrder res = BeanParser.parseLongOrderList(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        Toast.makeText(mActivity, "获取订单列表成功", Toast.LENGTH_SHORT).show();
                        // 用户
                        mAdapter.updateData(res.mDatas);
                    }else {

                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mCvPtr.onRefreshComplete();
                hideProgress();
            }
        }));
    }


    public static LongOrderListFragment newInstance() {
        LongOrderListFragment f = new LongOrderListFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }


}
