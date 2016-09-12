package com.aimowei.Activity.garbage;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.aimowei.Activity.garbage.adapter.MyViewPagerAdapter;
import com.aimowei.Activity.garbage.fragement.HomeFragment;
import com.aimowei.Activity.garbage.fragement.InformationFragment;
import com.aimowei.Activity.garbage.fragement.MyCenterFragment;
import com.aimowei.Activity.garbage.fragement.ShoppingFragment;
import com.aimowei.garbage.R;
import com.umeng.message.PushAgent;

public class MainActivity extends BaseActivity implements OnPageChangeListener {

	private long mExitTime;
	private ViewPager pager;
	private PagerAdapter mAdapter;
	PushAgent mPushAgent;
	private ArrayList<Fragment> fragments;
	private ArrayList<RadioButton> title = new ArrayList<RadioButton>();// 三个标题

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);// /slidingmenu里面重写了
		// 开启推送
		mPushAgent = PushAgent.getInstance(this);
		addAlias(sp.getString("YDDH", "").toString().trim(), "userPhone");
		mPushAgent.enable();

		initView();// 初始化控件
		initTitle();
		initViewPager();
	}
//增加Alias
	private void addAlias(String alias1, String aliasType1) {
		String alias = alias1;
		String aliasType = aliasType1;

		new AddAliasTask(alias, aliasType).execute();

	}

	class AddAliasTask extends AsyncTask<Void, Void, Boolean> {

		String alias;
		String aliasType;

		public AddAliasTask(String aliasString, String aliasTypeString) {
			// TODO Auto-generated constructor stub
			this.alias = aliasString;
			this.aliasType = aliasTypeString;
		}

		protected Boolean doInBackground(Void... params) {
			try {
				return mPushAgent.addAlias(alias, aliasType);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (Boolean.TRUE.equals(result)) {

			}

		}
	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		pager = (ViewPager) findViewById(R.id.pager);// 初始化控件
		fragments = new ArrayList<Fragment>();// 初始化数据
		fragments.add(new HomeFragment());
		fragments.add(new ShoppingFragment());
		fragments.add(new InformationFragment());
		fragments.add(new MyCenterFragment());

	}

	/**
	 * 初始化ViewPager
	 */
	private void initViewPager() {
		mAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),
				fragments);
		pager.setAdapter(mAdapter);
		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(0);// 设置成当前第一个
	}

	/**
	 * 初始化几个用来显示title的RadioButton
	 */
	private void initTitle() {
		title.add((RadioButton) findViewById(R.id.title1));// 三个title标签
		title.add((RadioButton) findViewById(R.id.title2));
		title.add((RadioButton) findViewById(R.id.title3));
		title.add((RadioButton) findViewById(R.id.title4));

		title.get(0).setOnClickListener(new MyOnClickListener(0));// 设置响应
		title.get(1).setOnClickListener(new MyOnClickListener(1));
		title.get(2).setOnClickListener(new MyOnClickListener(2));
		title.get(3).setOnClickListener(new MyOnClickListener(3));

	}

	// 空菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	/**
	 * 重写OnClickListener的响应函数，主要目的就是实现点击title时，pager会跟着响应切换
	 * 
	 * @author llb
	 * 
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index;

		public MyOnClickListener(int index) {
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			pager.setCurrentItem(index);// 把viewpager的视图切过去，实现捏造title跟pager的联动
			title.get(index).setChecked(true);// 设置被选中，否则布局里面的背景不会切换
		}

	}

	/**
	 * 下面三个是OnPageChangeListener的接口函数
	 */
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		Log.i("slide", "onPageSelected+agr0=" + arg0);
		title.get(arg0).setChecked(true);// 保持页面跟按钮的联动
	}

	// 按两次退出程序
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent home = new Intent(Intent.ACTION_MAIN);

			home.addCategory(Intent.CATEGORY_HOME);

			startActivity(home);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
