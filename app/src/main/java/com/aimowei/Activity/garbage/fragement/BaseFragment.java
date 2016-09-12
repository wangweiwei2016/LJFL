package com.aimowei.Activity.garbage.fragement;



import com.aimowei.Activity.garbage.utils.AppManager;
import com.aimowei.garbage.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.graphics.AvoidXfermode.Mode;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class BaseFragment extends Fragment{
	/**
	 * 已经初始化的SharedPreferences变量
	 */
	protected SharedPreferences sp;
	protected ImageLoader imageLoader;
	protected DisplayImageOptions options;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		sp = getActivity().getSharedPreferences("config", 0);
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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
		
		AppManager.getAppManager().addActivity(getActivity());
	}
	

	/*
	 * 多个应用Activity的退出
	 */
	// private List<Activity> activitys=new ArrayList<Activity>();
	protected void exitSystem() {
		/*
		 * for (Activity item : GlobalParams.activitys) { item.finish(); }
		 */
		AppManager.getAppManager().AppExit(getActivity());

	}

	
       

}
