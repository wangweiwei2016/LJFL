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

import com.aimowei.Activity.garbage.mycenter_function.ApplyVolunteerActivity;
import com.aimowei.Activity.garbage.mycenter_function.ConsultActivity;
import com.aimowei.Activity.garbage.mycenter_function.MessageManagementActivity;
import com.aimowei.Activity.garbage.mycenter_function.MyCreditActivity;
import com.aimowei.Activity.garbage.mycenter_function.MyTwoDimensionActivity;
import com.aimowei.Activity.garbage.mycenter_function.SettingActivity;
import com.aimowei.Activity.garbage.mycenter_function.SuggestActivity;
import com.aimowei.Activity.garbage.mycenter_function.UpdateMyInfoActivity;
import com.aimowei.Activity.garbage.mycenter_function.UpdatePasswordActivity;
import com.aimowei.garbage.R;

public class MyCenterFragment extends BaseFragment implements OnClickListener {
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
			view = inflater.inflate(R.layout.activity_my_center, container,
					false);
		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);// 先移除
		}
		invisible(View.INVISIBLE);
		view.findViewById(R.id.tv_update_password).setOnClickListener(this);
		view.findViewById(R.id.tv_mycreadit).setOnClickListener(this);
		view.findViewById(R.id.tv_update_preson_info).setOnClickListener(this);
		view.findViewById(R.id.tv_message_management).setOnClickListener(this);
		view.findViewById(R.id.tv_my_two).setOnClickListener(this);
		view.findViewById(R.id.tv_apply_volunteer).setOnClickListener(this);
		view.findViewById(R.id.tv_system_management).setOnClickListener(this);
		view.findViewById(R.id.tv_consult).setOnClickListener(this);
		view.findViewById(R.id.tv_suggest).setOnClickListener(this);
		settitle("个人中心");

		return view;
	}

	

	// 我的积分
	public void center_mycredit() {
		Intent intent = new Intent(getActivity(), MyCreditActivity.class);
		startActivity(intent);
	}

	// 修改密码
	public void alterPassword() {
		Intent intent = new Intent(getActivity(), UpdatePasswordActivity.class);
		startActivity(intent);
		return;
	}
	//该界面的所有点击事件在这写 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_mycreadit:
			center_mycredit();
			break;
		case R.id.tv_update_password:
			alterPassword();
			break;
		

		case R.id.tv_my_two:
			myTwoDiment();
			break;
		case R.id.tv_apply_volunteer:
			applyVolunteer();
			break;
		case R.id.tv_message_management:
			messageManagement();
			break;
		case R.id.tv_update_preson_info:
			alteruserInfo();
			break;
		case R.id.tv_system_management:
			systemManagement();
			break;
		case R.id.tv_consult:
			consult();
			break;
		case R.id.tv_suggest:
			suggest();
			break;
		default:
			break;
		}

	}
	// 修改个人信息
			public void alteruserInfo() {
				Intent intent = new Intent(getActivity(), UpdateMyInfoActivity.class);
				startActivity(intent);
				return;
			}

	// 消息管理
	private void messageManagement() {
		Intent intent = new Intent(getActivity(), MessageManagementActivity.class);
		startActivity(intent);
		return;
	}

	// 申请志愿者
	private void applyVolunteer() {
		Intent intent = new Intent(getActivity(), ApplyVolunteerActivity.class);
		startActivity(intent);
		return;

	}

	// 我的二维码
	private void myTwoDiment() {
		Intent intent = new Intent(getActivity(), MyTwoDimensionActivity.class);
		startActivity(intent);
		return;

	}
	/** 咨询
	 * 
	 */
	public void consult() {
		Intent intent = new Intent(getActivity(), ConsultActivity.class);
		startActivity(intent);
		return;
	}
	/**建议点击
	 * 
	 */
	private void suggest() {
		Intent intent = new Intent(getActivity(), SuggestActivity.class);
		startActivity(intent);
		return;

	}

	//系统管理点击
private void systemManagement() {
		
		Intent intent=new Intent(getActivity(),SettingActivity.class);
		startActivity(intent);
	}

}
