package com.aimowei.Activity.garbage.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimowei.Activity.garbage.information_function.EventArrangementActivity;
import com.aimowei.Activity.garbage.information_function.GarbagePutQueryActivity;
import com.aimowei.Activity.garbage.information_function.GarbagePutRecordActivity;
import com.aimowei.Activity.garbage.information_function.GarbageSortingQueryActivity;
import com.aimowei.Activity.garbage.information_function.RankingListActivity;
import com.aimowei.Activity.garbage.mycenter_function.SettingActivity;
import com.aimowei.Activity.garbage.mycenter_function.UpdateMyInfoActivity;
import com.aimowei.garbage.R;

public class InformationFragment extends BaseFragment implements
		OnClickListener {
	private TextView title;
	View view;

	// 设置标题
	public void settitle(String title1) {
		title = (TextView) view.findViewById(R.id.title);
		title.setText(title1);
	}
	ImageView imb;
	/** 隐藏返回键
	 * 
	 * @param visible
	 */
		public void invisible(int visible) {
			imb = (ImageButton) view.findViewById(R.id.ib_return);
			imb.setVisibility(visible);
		}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("slide", "FriendFragment-onCreateView");
		if (view == null) {
			view = inflater.inflate(R.layout.activity_information_search,
					container, false);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);// 先移除
		}
		// 设置点击监听
		view.findViewById(R.id.tv_select_lajifenlei).setOnClickListener(this);
		view.findViewById(R.id.tv_select_huodong).setOnClickListener(this);
		view.findViewById(R.id.tv_select_lajitoufang_put).setOnClickListener(
				this);
		view.findViewById(R.id.tv_select_lajitoufang_recorder).setOnClickListener(this);
		view.findViewById(R.id.tv_select_paihangbang).setOnClickListener(this);
		
		

		settitle("信息查询");
		invisible(View.INVISIBLE);
		return view;
	}

	// ------------------------------------点击事件
	// 垃圾分类点击
	public void lajitypeSearch() {
		Intent intent=new Intent(getActivity(),GarbageSortingQueryActivity.class);
		startActivity(intent);
		return;
	}


	// 活动查询点击
	public void huodongSearch() {
		Intent intent=new Intent(getActivity(),EventArrangementActivity.class);
		startActivity(intent);
		return;
	}

	// 垃圾投放点击
	public void lajiPutSearch() {
		Intent intent=new Intent(getActivity(),GarbagePutQueryActivity.class);
		startActivity(intent);
		return;
	}

	// 垃圾投放记录点击
	public void lajiPutRecord() {
		Intent intent=new Intent(getActivity(),GarbagePutRecordActivity.class);
		startActivity(intent);
		return;
	}

	


	// 排行榜查询点击
	public void paihangSearch() {
		Intent intent=new Intent(getActivity(),RankingListActivity.class);
		startActivity(intent);
		return;
	}

	//该界面的所有点击事件在这写 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_select_lajifenlei:
			lajitypeSearch();
			break;
		case R.id.tv_select_huodong:
			huodongSearch();
			break;
		
		case R.id.tv_select_lajitoufang_recorder:
			lajiPutRecord();
			break;
	
		case R.id.tv_select_lajitoufang_put:
			lajiPutSearch();
			break;
		
		case R.id.tv_select_paihangbang:
			paihangSearch();
			break;
		
		
		default:
			break;
		}

	}
	

}
