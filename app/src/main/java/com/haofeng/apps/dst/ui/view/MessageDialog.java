package com.haofeng.apps.dst.ui.view;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by WIN10 on 2017/6/15.
 */

public class MessageDialog {
    private Context mContext;
    private AlertDialog.Builder builder;
    public MessageDialog(Context context) {
        this.mContext = context;
        builder = new AlertDialog.Builder(mContext);
    }

    public MessageDialog setTitle(String title) {
        builder.setTitle(title);
        return this;
    }


    public void show() {

    }
}
