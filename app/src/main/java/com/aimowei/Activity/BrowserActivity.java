package com.aimowei.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aimowei.garbage.R;
import com.aimowei.push.VideoEnabledWebChromeClient;
import com.aimowei.push.VideoEnabledWebView;
public class BrowserActivity extends Activity {
	private VideoEnabledWebView webView;
	private VideoEnabledWebChromeClient webChromeClient;
	private String homeUrl = "www.baidu.com";
	private ImageView backward = null;
	private TextView mainName;

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.webview);
		backward = (ImageView) findViewById(R.id.left_ImageView);
		mainName = (TextView) findViewById(R.id.mainName);
		mainName.setText("完善資料");
		backward.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Intent intent = getIntent();
		homeUrl = intent.getStringExtra("com.aimowei.MyXYW.url");
		// Save the web view
		webView = (VideoEnabledWebView) findViewById(R.id.webView);
		WebSettings settings = webView.getSettings();
		settings.setPluginState(PluginState.ON);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
		settings.setDefaultTextEncodingName("UTF-8");

		settings.setJavaScriptEnabled(true);
		// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setUseWideViewPort(true);
		settings.setLoadWithOverviewMode(true);
		// webView.setInitialScale(25);
		// Initialize the VideoEnabledWebChromeClient and set event handlers
		View nonVideoLayout = findViewById(R.id.nonVideoLayout);
		ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout);
		// noinspection all
		View loadingView = getLayoutInflater().inflate(R.layout.view_loading_video, null); // Your
																							// own
																							// view,
																							// read
																							// class
																							// comments
		webChromeClient = new VideoEnabledWebChromeClient(nonVideoLayout, videoLayout, loadingView, webView) // See
																												// all
																												// available
																												// constructors...
		{
			// Subscribe to standard events, such as onProgressChanged()...
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Your code...
			}

		};

		webChromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {

			@Override
			public void toggledFullscreen(boolean fullscreen) {
				// Your code to handle the full-screen change, for
				// example showing and hiding the title bar. Example:
				if (fullscreen) {
					WindowManager.LayoutParams attrs = getWindow().getAttributes();
					attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
					attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
					getWindow().setAttributes(attrs);
					if (android.os.Build.VERSION.SDK_INT >= 9) {
						// noinspection all
						getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
					}
				} else {
					WindowManager.LayoutParams attrs = getWindow().getAttributes();
					attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
					attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
					getWindow().setAttributes(attrs);
					if (android.os.Build.VERSION.SDK_INT >= 9) {
						// noinspection all
						getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
					}
				}

			}
		});

		webView.setWebChromeClient(webChromeClient);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				webView.loadUrl(url);

				return true;

			}
		});

		webView.loadUrl(homeUrl);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
		};
	};

	@Override
	public void onBackPressed() {
		// Notify the VideoEnabledWebChromeClient, and handle it ourselves if it
		// doesn't handle it
		if (!webChromeClient.onBackPressed()) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				// Standard back button implementation (for example this could
				// close the app)
				super.onBackPressed();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 閼惧嘲褰�??
			finish(); // back闁匡�??
		}
		return true;
	}

	@Override
	public void finish() {// 缁ф壙鑷狝ctivity
		super.finish();
		// webView.onFinishTemporaryDetach();
		// onPause();
		// // webView.onPause();
		// //鍋滄瑙嗛瑙ｅ喅鏂规
		// webView.pauseTimers();
		// webView.stopLoading();
		// webView.loadData("<a></a>", "text/html", "utf-8");
	}

	private void callHiddenWebViewMethod(String name) {
		if (webView != null) {
			try {
				Method method = WebView.class.getMethod(name);
				method.invoke(webView);
			} catch (NoSuchMethodException e) {
				Log.i("No such method: " + name, e.toString());
			} catch (IllegalAccessException e) {
				Log.i("Illegal Access: " + name, e.toString());
			} catch (InvocationTargetException e) {
				Log.d("Invocation Target Exception: " + name, e.toString());
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onPause() {
		super.onPause();
		/*
		 * try { Message msg = handler.obtainMessage();
		 * handler.sendMessage(msg); Runnable r = new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub
		 * System.out.println("js"); String js =
		 * "javascript: var v=document.getElementsByTagName('video')[0]; " +
		 * "if(v){v.pause()}"; webView.loadUrl(js); System.out.println("1");
		 * webView.onPause(); // webView.pauseTimers(); webView.stopLoading();
		 * webView.loadData("<a></a>", "text/html", "utf-8");
		 * callHiddenWebViewMethod("onPause"); System.out.println("1.5"); } };
		 * handler.post(r);
		 * 
		 * } catch (Exception e) {
		 * 
		 * }
		 */
	}

	@Override
	@SuppressLint("NewApi")
	public void onResume() {// 缁ф壙鑷狝ctivity
		super.onResume();
		webView.onResume();
		webView.resumeTimers();
		// callHiddenWebViewMethod("onResume");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
			Runnable r = new Runnable() {
				@Override
				public void run() { // TODO Auto-generated method stub

					RelativeLayout _layout = (RelativeLayout) findViewById(R.id.nonVideoLayout);
					_layout.removeView(webView);
					webView.removeAllViews();
					webView.destroy();

					System.out.println("2");
					webView.destroy();
				}
			};
			handler.post(r);

		} catch (Exception e) {

		}
	}
}
