package com.haofeng.apps.dst.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;

import com.haofeng.apps.dst.ui.BaseActivity;

/**
 *
 * Created by WIN10 on 2017/6/15.
 */

public class BaseFragment extends Fragment {
    private ProgressDialog mPdLoading;
    protected BaseActivity mActivity;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (BaseActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    public void showProgress(String message) {
        if(mPdLoading == null && mActivity != null) {
            mPdLoading = new ProgressDialog(mActivity);
        }

        if(mPdLoading != null) {
            mPdLoading.setMessage(message);
            mPdLoading.show();
        }
    }

    public void hideProgress() {
        try{
            if(mPdLoading != null && mActivity != null) {
                mPdLoading.dismiss();
            }
        }catch (Exception e) {

        }

    }
}
