package com.haofeng.apps.dst.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

/**
 * 自动输入电桩号充电
 * @author qtds
 *
 */
public class CaptureInputDialogActivity extends BaseActivity implements
		OnClickListener {
	private EditText inpuText;
	private TextView okTextView, cancleTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_captureinputdialog);
		addActivity(this);
		inpuText = (EditText) findViewById(R.id.view_capture_dialog_input);
		okTextView = (TextView) findViewById(R.id.view_capture_dialog_ok);
		cancleTextView = (TextView) findViewById(R.id.view_capture_dialog_cancle);
		okTextView.setOnClickListener(this);
		cancleTextView.setOnClickListener(this);
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
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.view_capture_dialog_ok:
			String inputString = inpuText.getText().toString();
			if (inputString != null && !TextUtils.isEmpty(inputString)) {
				Intent intent = new Intent();
				intent.putExtra("input", inputString);
				intent.putExtra("type", "ok");
				CaptureInputDialogActivity.this.setResult(2, intent);
				finish();
			} else {
				PublicUtil.showToast(CaptureInputDialogActivity.this,
						"请输入电枪编号", false);
			}
			break;

		case R.id.view_capture_dialog_cancle:
			Intent intent = new Intent();
			intent.putExtra("type", "cancle");

			CaptureInputDialogActivity.this.setResult(2, intent);
			finish();
			break;

		default:
			break;
		}

	}

}
