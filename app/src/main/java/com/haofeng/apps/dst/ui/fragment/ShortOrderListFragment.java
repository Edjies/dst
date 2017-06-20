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
import com.haofeng.apps.dst.UserDataManager;
import com.haofeng.apps.dst.bean.BaseRes;
import com.haofeng.apps.dst.bean.BeanParser;
import com.haofeng.apps.dst.bean.ResAccount;
import com.haofeng.apps.dst.bean.ResOrderList;
import com.haofeng.apps.dst.httptools.Constent;
import com.haofeng.apps.dst.ui.BaseActivity;
import com.haofeng.apps.dst.ui.NewOrderListActivity;
import com.haofeng.apps.dst.ui.adapter.ShortOrderAdapter;
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

public class ShortOrderListFragment extends PageFragment {
    public final static String TAG = ShortOrderListFragment.class.getSimpleName();

    private PullToRefreshListView mCvPtr;
    private ListView mLvContent;
    private ShortOrderAdapter mAdapter;

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
        mAdapter = new ShortOrderAdapter((BaseActivity) getActivity(), this);
        mLvContent.setAdapter(mAdapter);
        mCvPtr.setEnabled(false);
        mCvPtr.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    private void setListeners() {
        mCvPtr.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                execGetAccountInfo();
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
            execGetAccountInfo();
        }
    }

    @Override
    public void onPageSelected() {
        if(isResumed()) {
            execGetAccountInfo();
        }
    }

    private boolean isPageSelected() {
        if(((NewOrderListActivity) mActivity).getCurPosition() == 0) {
            return true;
        }else {
            return false;
        }
    }

    private void execGetAccountInfo() {
        showProgress("加载中...");
        String url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/member/info")
                .append("user_id", PublicUtil.getStorage_string(mActivity, "userid", ""))
                .append("secret", Constent.secret)
                .append("ver", Constent.VER)
                .append("loginkey", PublicUtil.getStorage_string(mActivity, "loginkey2", ""))
                .build();
        ULog.e(TAG, url);
        UVolley.getVolley(mActivity).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ULog.e(TAG, response.toString());
                if(isResumed()) {
                    ResAccount res = BeanParser.parseAccount(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        // 刷新列表
                        UserDataManager.getInstance().setAccount(res.mAccount);
                        execGetOrderList();
                        return;
                    }else {
                        Toast.makeText(mActivity, res.mMessage, Toast.LENGTH_SHORT).show();
                    }
                }
                hideProgress();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
            }
        }));


    }

    private void execGetOrderList() {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/order/get_list")
                    .append("user_id", PublicUtil.getStorage_string(mActivity, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", Constent.VER)
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
                mCvPtr.setEnabled(true);
                ULog.e(TAG, response.toString());
                if(isResumed()) {
                    ResOrderList res = BeanParser.parseOrderList(response);
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

    public void execCancelOrder(String order_no) {
        String url = "";
        try {
            url = new UrlBuilder().setHost(ServerManager.getInstance().getMainServer()).setPath("/app/order/cancel_order")
                    .append("order_no", order_no)
                    .append("user_id", PublicUtil.getStorage_string(mActivity, "userid", ""))
                    .append("secret", Constent.secret)
                    .append("ver", URLEncoder.encode(Constent.VER, "utf-8"))
                    .append("loginkey", PublicUtil.getStorage_string(mActivity, "loginkey2", ""))
                    .build();
        }catch (Exception e) {

        }
        ULog.e(TAG, url);
        showProgress("");
        UVolley.getVolley(mActivity).addToRequestQueue(new JsonObjectRequest(com.android.volley.Request.Method.GET, url, new JSONObject(), new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideProgress();
                ULog.e(TAG, response.toString());
                if(mActivity != null) {
                    BaseRes res = BeanParser.parseBaseRes(response);
                    if(res.mCode == BaseRes.RESULT_OK) {
                        execGetOrderList();
                    }else {
                        Toast.makeText(mActivity, res.mMessage, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mActivity != null) {
                    hideProgress();
                }
            }
        }));
    }

    public static ShortOrderListFragment newInstance() {
        ShortOrderListFragment f = new ShortOrderListFragment();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }


}
