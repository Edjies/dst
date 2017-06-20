package com.haofeng.apps.dst.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.application.MyApplication;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 推送消息过来之后，软件前台时，显示dialog
 * 
 * @author qtds
 * 
 */
public class NotificationShowDialogActivity extends BaseActivity implements
		OnClickListener {

	private final String TAG = "NotificationShowDialogActivity";
	private EditText inpuText;
	private TextView okTextView, cancleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_notificationshowdialog);
		((MyApplication) getApplication()).addActivity(this);
		PublicUtil.logDbug(TAG, getIntent().getStringExtra("pushmag"), 0);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);// 友盟统计开始
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);// 友盟统计结束
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			finish();
			return true;

		} else {
			return super.onKeyDown(keyCode, event);
		}

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

		switch (arg0.getId()) {
		case R.id.view_capture_dialog_ok:
			finish();
			break;

		default:
			break;
		}

	}

}
