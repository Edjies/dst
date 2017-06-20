package com.haofeng.apps.dst.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.httptools.Network;
import com.haofeng.apps.dst.service.DolwnLoadAppService;

public class UpdateAppManager {

	private Context mContext;
	// app下载地址
	private String spec;
	private String newVerString;
	private String updateinfor;

	public UpdateAppManager(Context context, String newVerString,
			String urlpath, String updateinfor) {
		this.mContext = context;

		if (newVerString.startsWith("android")) {
			newVerString = newVerString.replace("android", "");
		}
		this.newVerString = newVerString;
		this.updateinfor = updateinfor;
		spec = urlpath;

	}

	/**
	 * 检测应用更新信息
	 */
	public void checkUpdateInfo() {
		showNoticeDialog();
	}

	/**
	 * 显示提示更新对话框
	 */
	@SuppressLint("NewApi")
	private void showNoticeDialog() {

		View view = LayoutInflater.from(mContext).inflate(
				R.layout.view_updateinforshow_dialog, null);

		TextView ok = (TextView) view
				.findViewById(R.id.view_updateinforshow_dialog_ok);
		TextView cancel = (TextView) view
				.findViewById(R.id.view_updateinforshow_dialog_cancel);
		TextView infor = (TextView) view
				.findViewById(R.id.view_updateinforshow_dialog_infor);
		TextView infor2 = (TextView) view
				.findViewById(R.id.view_updateinforshow_dialog_infor2);

		String inforString = "软件版本更新" + newVerString;
		if (Network.checkNetWorkType(mContext) != Network.WIFI) {
			inforString = inforString + "\n当前为运营商网络，是否升级？";
		}

		infor.setText(inforString);
		infor2.setText(updateinfor);
		ok.setText(mContext.getResources().getString(R.string.lijigengxin));

		final AlertDialog alertDialog = new AlertDialog.Builder(mContext,
				R.style.dialog_nostroke).show();
		alertDialog.addContentView(view, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		alertDialog.setCancelable(true);
		alertDialog.setCanceledOnTouchOutside(true);
		ok.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
				DolwnLoadAppService.setContent(mContext);
				Intent intent = new Intent(mContext, DolwnLoadAppService.class);
				intent.putExtra("spec", spec);
				intent.putExtra("newVerString", newVerString);
				mContext.startService(intent);
			}
		});

		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (alertDialog != null) {
					alertDialog.dismiss();
				}
			}
		});

	}

}
