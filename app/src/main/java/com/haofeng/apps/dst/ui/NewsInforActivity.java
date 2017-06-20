package com.haofeng.apps.dst.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.haofeng.apps.dst.R;
import com.haofeng.apps.dst.utils.PublicUtil;
import com.umeng.analytics.MobclickAgent;

public class NewsInforActivity extends BaseActivity {
	private final String TAG = "NewsInforActivity";
	private FrameLayout topLayout;

	private TextView backView;
	private WebView webView;
	private String urlString;
	private boolean isNotifiCreate = false;// 是否是直接从通知点击进来加载的

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newsinfor);
		addActivity(this);
		init();
	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void init() {
		topLayout = (FrameLayout) findViewById(R.id.newsinfor_top_layout);
		setTopLayoutPadding(topLayout);
		urlString = getIntent().getStringExtra("urlString");

		webView = (WebView) findViewById(R.id.newsinfor_webview);

		backView = (TextView) findViewById(R.id.newsinfor_back);

		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (webView != null && webView.canGoBack()) {
					webView.goBack();
				} else {
					if (isNotifiCreate) {

						Intent intent = new Intent(NewsInforActivity.this,
								NewsActivity.class);
						startActivity(intent);
					}

					finish();
				}

			}
		});

		WebSettings webSettings = webView.getSettings();

		// 开启javascript设置
		webSettings.setJavaScriptEnabled(true);

		// 让缩放显示的最小值为起始
		// webView.setInitialScale(5);
		// 自适应屏幕

		webSettings.setUseWideViewPort(true);// 将图片调整到适合webview的大小
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// 设置支持缩放
		webSettings.setSupportZoom(true);
		// 设置缩放工具的显示
		webSettings.setBuiltInZoomControls(false);

		// 设置可以访问文件
		webSettings.setAllowFileAccess(true);
		// 设置支持缩放
		webSettings.setDisplayZoomControls(false);

		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		// webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				PublicUtil.logDbug(TAG, "onLoadResource url=" + url, 0);

				super.onLoadResource(view, url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				PublicUtil.logDbug(TAG, "intercept url=" + url, 0);
				view.loadUrl(url);
				return true;
			}

			// 页面开始时调用
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
			}

			// 页面加载完成调用
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);

			}

			@Override
			public void onFormResubmission(WebView view, Message dontResend,
					Message resend) {
				// TODO Auto-generated method stub
				super.onFormResubmission(view, dontResend, resend);
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				PublicUtil.logDbug(TAG, "onJsAlert " + message, 0);

				PublicUtil.showToast(NewsInforActivity.this, message, false);

				result.confirm();
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {
				PublicUtil.logDbug(TAG, "onJsConfirm " + message, 0);
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				PublicUtil.logDbug(TAG, "onJsPrompt " + url, 0);
				return super.onJsPrompt(view, url, message, defaultValue,
						result);
			}

		});
		if (urlString != null) {
			webView.loadUrl(urlString);
		}

	}

	// @Override
	// protected void onNewIntent(Intent intent) {
	// // TODO Auto-generated method stub
	// super.onNewIntent(intent);
	//
	// if ("news".equals(intent.getStringExtra("notifi_type"))) {
	// urlString = intent.getStringExtra("urlString");
	// webView.clearCache(true);
	// webView.loadUrl(urlString);
	// isNotifiCreate = false;
	// } else {
	// isNotifiCreate = true;
	// }
	//
	// }

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
		// 检查是否为返回事件，如果有网页历史记录
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (webView.canGoBack()) {
				webView.goBack();

			} else if (isNotifiCreate) {
				Intent intent = new Intent(NewsInforActivity.this,
						NewsActivity.class);
				startActivity(intent);
				finish();
			} else {
				finish();
			}

			return true;
		} else {

			return super.onKeyDown(keyCode, event);
		}

	}

}
