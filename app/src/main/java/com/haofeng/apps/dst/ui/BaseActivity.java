package com.haofeng.apps.dst.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.utils.ULog;

public class BaseActivity extends AppCompatActivity {

    protected boolean mTranslucent = false;
    public boolean isPageResumed = false;
    private ProgressDialog mPdLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题栏
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            //透明状态栏
//            getWindow()
//                    .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }

        // 隐藏标题栏 和 ActionBar
        mTranslucent = translucentSystemBar(true, false, this);
        hideActionBar();
        ULog.i(tag(), "onCreate");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ULog.i(tag(), "onNewIntent");
    }

    @Override
    protected void onStart() {
        super.onStart();
        ULog.i(tag(), "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ULog.i(tag(), "onResume");
        isPageResumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ULog.i(tag(), "onPause");
        isPageResumed = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        ULog.i(tag(), "onStop");
    }

    private String tag() {
        return this.getClass().getSimpleName();
    }

    /**
     * 获取状态栏的高度
     */

    public int getStatusBarHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
        } else {
            return 0;
        }
    }
    /**
     * 设置Toplayout的Padding
     *
     * @param viewGroup
     */
    protected void setTopLayoutPadding(ViewGroup viewGroup) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            viewGroup.setPadding(0, 10, 0, 20);
        } else {
            viewGroup.setPadding(0, getStatusBarHeight(getApplicationContext())+10, 0, 20);
        }
    }

    protected  void hideActionBar() {
        try{
            ActionBar actionBar = getSupportActionBar();//高版本可以换成 ActionBar actionBar = getActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.hide();
        }catch (Exception e) {

        }

    }

    /** 获取状态栏的高度*/
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /** 让状态栏透明*/
    public static boolean translucentSystemBar(boolean translucent_status_bar, boolean translucent_navigation_bar, Activity mActivity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            Window window=null;
            if (mActivity != null) {
                window = mActivity.getWindow();			// Translucent status bar
                if (translucent_status_bar){
                    if (window != null)
                        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                }
                return true;
            }
        }
        return false;
    }

    public void showProgress(String message) {
        if(mPdLoading == null) {
            mPdLoading = new ProgressDialog(this);
        }
        mPdLoading.setMessage(message);
        mPdLoading.show();
    }

    public void hideProgress() {
        if(mPdLoading != null) {
            mPdLoading.dismiss();
        }
    }



    /**
     * 添加Activity
     */
    protected void addActivity(Activity activity) {
        ((MyApplication) getApplication()).addActivity(activity);
    }
}
