package com.aimowei.Activity.garbage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.aimowei.Activity.garbage.utils.AppManager;
import com.aimowei.garbage.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends FragmentActivity {
	/**
	 * SharedPreferences已经初始化的变量
	 */
	protected SharedPreferences sp;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	/**
	 * 标题TextView
	 */
	private TextView title;
	// 底部导航
	protected LinearLayout ll_my_home;
	protected LinearLayout ll_menu_myshopping;
	protected LinearLayout ll_menu_my_information;
	protected LinearLayout ll_menu_my_acount;
	protected LinearLayout ll_menu_my_setting;

	// 设置标题
	public void settitle(String title1) {
		title = (TextView) findViewById(R.id.title);
		title.setText(title1);
	}

	/*
	 * 多个应用Activity的退出
	 */
	// private List<Activity> activitys=new ArrayList<Activity>();
	protected void exitSystem() {
		/*
		 * for (Activity item : GlobalParams.activitys) { item.finish(); }
		 */
		GlobalParams.User = "";
		GlobalParams.isLogin = false;
		Editor editor = sp.edit();
		editor.putString("yonghuleibie", "");
		editor.putString("SQYHMC", "");
		editor.putString("YX", "");
		editor.putString("SQDM", "");
		editor.putString("YDDH", "");
		editor.putString("HZXM", "");
		editor.putString("YHZH", "");
		editor.commit();
		AppManager.getAppManager().AppExit(this);

	}

	@Override
	protected void onDestroy() {
		// GlobalParams.activitys.remove(this);
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();

	}

	/**
	 * 底部菜单栏管理(ImageView)
	 **/

	public Intent intent = new Intent();

	/**
	 * 底部菜单 首页点击事件
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		// 使用DisplayImageOptions.Builder()创建DisplayImageOptions
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_stub) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build(); // 构建完成
		// GlobalParams.activitys.add(this);

		AppManager.getAppManager().addActivity(this);

	}

	/**
	 * 菜单栏设置
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // TODO
	 * Auto-generated method stub switch (item.getItemId()) { case 0: Intent
	 * intent = new Intent(this, HomeActivity.class); startActivity(intent);
	 * break; case 1: Intent intent1 = new Intent(this, TypeActivity.class);
	 * startActivity(intent1); break; case 2: Intent intent2 = new Intent(this,
	 * ShoppingCarActivity.class); startActivity(intent2); break; case 3: Intent
	 * intent3 = new Intent(this, MyAcountActivity.class);
	 * startActivity(intent3); break; case 4: Intent intent4 = new Intent(this,
	 * MoreActivity.class); startActivity(intent4); break;
	 * 
	 * case 5: exitSystem(); break; } return true; }
	 */
	private ImageButton imb;

	/**
	 * 隐藏返回键
	 * 
	 * @param visible
	 */
	public void invisible(int visible) {
		imb = (ImageButton) findViewById(R.id.ib_return);
		imb.setVisibility(visible);
	}

	/**
	 * 返回键
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		// 只能竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onResume();

	}

	/**
	 * 返回
	 * 
	 * @param view
	 */
	public void back(View view) {
		onBackPressed();
	}

}
