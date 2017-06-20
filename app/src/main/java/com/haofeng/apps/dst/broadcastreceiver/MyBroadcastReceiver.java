package com.haofeng.apps.dst.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.haofeng.apps.dst.R;

public class MyBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		if ("com.myreceiver.showtoast".equals(arg1.getAction())) {
			Toast.makeText(
					arg0,
					arg0.getResources().getString(
							arg1.getIntExtra("msgid",
									R.string.network_show_error)),
					Toast.LENGTH_SHORT).show();

		}

	}

}
