package com.aimowei.Activity.garbage.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimowei.Activity.garbage.home_function.TwoDimensionSearchActivity;
import com.aimowei.Activity.garbage.information_function.EventArrangementActivity;
import com.aimowei.Activity.garbage.information_function.GarbagePutQueryActivity;
import com.aimowei.Activity.garbage.information_function.GarbageSortingQueryActivity;
import com.aimowei.Activity.garbage.information_function.RankingListActivity;
import com.aimowei.Activity.garbage.mycenter_function.MessageManagementActivity;
import com.aimowei.Activity.garbage.mycenter_function.MyCreditActivity;
import com.aimowei.Activity.garbage.mycenter_function.MyTwoDimensionActivity;
import com.aimowei.Activity.garbage.mycenter_function.SuggestActivity;
import com.aimowei.Activity.garbage.mycenter_function.UpdateMyInfoActivity;
import com.aimowei.garbage.R;

public class HomeFragment extends BaseFragment implements OnClickListener {
	private TextView title;
	View view;

	// private TextView tv_twoweima;

	// 设置标题
	public void settitle(String title1) {
		title = (TextView) view.findViewById(R.id.title);
		title.setText(title1);
	}

	ImageView imb;
	ImageView ib_twoweima;

	/** 隐藏返回键
	 * 
	 * @param visible
	 */
	public void invisible(int visible) {
		imb = (ImageButton) view.findViewById(R.id.ib_return);
		imb.setVisibility(visible);
	}

	private GridView gv_home;
	/**
	 * GridView 显示的各个Item名
	 */
	private static String[] gvNames = { "活动查询", "排行榜", "投放查询", "我的积分", "消息管理",
			"建议", "垃圾分类", "二维码", "个人信息" };

	/**
	 * GridView 显示的各个Item对应图片
	 */
	private static int[] gvIds = { R.drawable.event_query, R.drawable.ranking,
			R.drawable.lajitoufang, R.drawable.my_credit,
			R.drawable.message_management, R.drawable.suggest,
			R.drawable.laji_sorting, R.drawable.twoweima8,
			R.drawable.sign_info_icon};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("slide", "FriendFragment-onCreateView");
		if (view == null) {
			view = inflater.inflate(R.layout.activity_home, container, false);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);// 先移除
		}
		// tv_twoweima=(TextView) view.findViewById(R.id.tv_two);
		// tv_twoweima.setOnClickListener(this);
		// view.findViewById(R.id.tv_two).setVisibility(View.GONE);
		ib_twoweima = (ImageView) view.findViewById(R.id.ib_twoweima);
		ib_twoweima.setOnClickListener(this);
		ib_twoweima.setVisibility(View.VISIBLE);
		gv_home = (GridView) view.findViewById(R.id.gv_home);
		gv_home.setAdapter(new HomeAdapter());
		// gradView 点击事件
		gv_home.setOnItemClickListener(new OnItemClickListener() {

			private Intent intent;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:// 进入活动查询
					intent = new Intent(getActivity(),
							EventArrangementActivity.class);
					startActivity(intent);
					break;
				case 1:// 进入排行榜

					intent = new Intent(getActivity(),
							RankingListActivity.class);
					startActivity(intent);
					break;
				case 2:// 进入垃圾投放查询
					intent = new Intent(getActivity(),
							GarbagePutQueryActivity.class);
					startActivity(intent);

					break;
				case 3:// 进入我的积分
					intent = new Intent(getActivity(), MyCreditActivity.class);
					startActivity(intent);
					break;
				case 4:// 进入消息管理
					intent = new Intent(getActivity(),
							MessageManagementActivity.class);
					startActivity(intent);
					break;
				case 5:// 进入建议
					intent = new Intent(getActivity(), SuggestActivity.class);
					startActivity(intent);
					break;
				case 6:// 进入垃圾分类
					intent = new Intent(getActivity(),
							GarbageSortingQueryActivity.class);
					startActivity(intent);
					break;
				case 7:// 进入二维码
					intent = new Intent(getActivity(),
							MyTwoDimensionActivity.class);
					startActivity(intent);
					break;
				case 8:// 进入修改个人信息
					intent = new Intent(getActivity(),
							UpdateMyInfoActivity.class);
					startActivity(intent);
					break;

				}

			}
		});
		settitle("首页");
		invisible(View.INVISIBLE);
		// 如果是1普通用户3商户那么就隐藏扫描二维码
		if (sp.getString("yonghuleibie", "").equals("1")||sp.getString("yonghuleibie", "").equals("3")) {

			ib_twoweima.setVisibility(View.INVISIBLE);
		}
		return view;
	}

	/**
	 * GridView 自定义Adapter
	 * 
	 * 
	 */
	private class HomeAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return gvNames.length;
		}

		// 返回每个位置对应的view对象。
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View
					.inflate(getActivity(), R.layout.list_gv_item, null);
			TextView tv_name = (TextView) view.findViewById(R.id.tv_gv_item);
			ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_gv_item);
			tv_name.setText(gvNames[position]);
			iv_icon.setImageResource(gvIds[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("slide", "FriendFragment--onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("slide", "FriendFragment--onStop");
	}

	// 该界面的所有点击事件在这写
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.tv_two:
		// twodimensioncode();
		// break;
		case R.id.ib_twoweima:
			twodimensioncode();
			break;
		default:
			break;
		}

	}

	// 二维码的点击事件
	public void twodimensioncode() {
		Intent intent = new Intent(getActivity(),
				TwoDimensionSearchActivity.class);
		startActivity(intent);
		return;
	}

}
