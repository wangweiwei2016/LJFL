package com.aimowei.Activity.garbage;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.aimowei.Activity.garbage.login_function.LoginActivity;
import com.aimowei.garbage.R;

public class SplashActivity extends Activity {

	protected static final String TAG = "SplashActivity";
//	protected static final int SHOW_UPDATE_DIALOG = 0;
//	protected static final int ENTER_HOME = 1;
//	protected static final int URL_ERROR = 2;
//	protected static final int NETWORK_ERROR = 3;
//	protected static final int JSON_ERROR = 4;
	//private TextView tv_splash_version;

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		intallShortCut();
		AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
		aa.setDuration(500);

		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
					enterHome();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		}.start();

	}

	/**
	 * 创建快捷图标
	 */
	private void intallShortCut() {

		boolean shortcut = sp.getBoolean("shortcut", false);
		if (shortcut == false) {
			Intent intent = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");// 告诉桌面要创建图标的意图
			// 快捷方式要包含三个重要的信息，1.名称 2.图标 3.干什么事
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					getString(R.string.appname));
			ShortcutIconResource res = Intent.ShortcutIconResource.fromContext(
					getApplicationContext(), R.drawable.icon111garbage);
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, res);
			Intent shortCutIntent = new Intent();// 桌面图标点击对应的意图
			shortCutIntent.setAction("android.intent.action.MAIN");
			shortCutIntent.addCategory("android.intent.catagory.LAUNCHER");
			shortCutIntent.setClassName(getPackageName(),
					"com.aimowei.Activity.garbage.SplashActivity");
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortCutIntent);
			intent.putExtra("duplicate", false);// 不重复创建
			sendBroadcast(intent);
			Editor editor = sp.edit();
			editor.putBoolean("shortcut", true);

			editor.commit();
		}
	}

	/**
	 * 进入主界面
	 */
	protected void enterHome() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		// 关闭当前页面
		finish();

	}

	/**
	 * 得到应用程序的版本名称
	 */

	private String getVersionName() {
		// 用来管理手机的APK
		PackageManager pm = getPackageManager();

		try {
			// 得到知道APK的功能清单文件
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

}
