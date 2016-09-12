package com.aimowei.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.aimowei.garbage.R;

import java.util.ArrayList;

/**
 * Android实现滑动指引效果
 * 
 * @Description: Android实现滑动指引效果
 * 
 * @File: MyGuideViewActivity.java
 * 
 * @Package com.test.guide
 * 
 * @Version V1.0
 */
public class MyGuideViewActivity extends Activity {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ImageView imageView;
	private ImageView[] imageViews;
	// 包裹滑动图片LinearLayout
	private ViewGroup main;
	// 包裹小圆点的LinearLayout
	private ViewGroup group;
	private Button button;
	private Intent intent;
	private Context context;
	private Handler handler;
	private final int SPLASH_DISPLAY_LENGHT = 3000; // 延�???1�??
	private int num = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		handler = new Handler();
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.item01, null));
		pageViews.add(inflater.inflate(R.layout.item02, null));
		pageViews.add(inflater.inflate(R.layout.item03, null));
		imageViews = new ImageView[pageViews.size()];
		main = (ViewGroup) inflater.inflate(R.layout.first, null);

		group = (ViewGroup) main.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main.findViewById(R.id.guidePages);
		context = MyGuideViewActivity.this;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(MyGuideViewActivity.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;

			if (i == 0) {
				// 默认选中第一张图
				imageViews[i].setBackgroundResource(R.drawable.dot_selected);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.dot_unselected);
			}

			group.addView(imageViews[i]);
		}

		setContentView(main);

		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener(this));

		// button =(Button)findViewById(R.id.come_in);
		// ButtonListener buttonListener =new ButtonListener();
		// button.setOnClickListener( buttonListener);

		LayoutInflater inflater2 = getLayoutInflater();
		View bmain = inflater2.inflate(R.layout.item02, null);
		// button=(Button)bmain.findViewById(R.id.comein);
		// ButtonListener buttonListener =new ButtonListener();
		// button.setOnClickListener( buttonListener);

		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// System.out.println("1");
		// intent=new Intent(MyGuideViewActivity.this,MainActivity.class);
		// startActivity(intent);
		// }
		// });
	}
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	// 指引页面数据适配?
	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("sdafdsadfadsf");
			((ViewPager) container).addView(pageViews.get(position));
			// container.addView(viewList.get(position));

			return pageViews.get(position);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	// 指引页面更改事件监听�????
	class GuidePageChangeListener implements OnPageChangeListener {

		private Activity activity;

		public GuidePageChangeListener(Activity activity) {
			// TODO Auto-generated constructor stub
			this.activity = activity;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0].setBackgroundResource(R.drawable.dot_selected);

				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.dot_unselected);
				}
				if (num == 0) {// 定时进入主activity
					if (arg0 == imageViews.length - 1) {
						Runnable r = new Runnable() {
							@Override
							public void run() { // TODO Auto-generated method
												// stub
								// 启动主Activity
								intent = new Intent(MyGuideViewActivity.this,
										Login.class);
								startActivity(intent);
								activity.finish();
							}
						};
						handler.postDelayed(r, SPLASH_DISPLAY_LENGHT);
						num = 1;
					}
				}
			}

		}
	}
}