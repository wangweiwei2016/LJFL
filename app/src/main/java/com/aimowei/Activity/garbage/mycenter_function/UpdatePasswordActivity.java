package com.aimowei.Activity.garbage.mycenter_function;

import android.content.Intent;
import android.content.SharedPreferences.Editor;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.GlobalParams;
import com.aimowei.Activity.garbage.MainActivity;
import com.aimowei.Activity.garbage.engine.impl.MyPersonCenterEngineimpl;
import com.aimowei.Activity.garbage.engine.impl.UserLoginEngineImpl;
import com.aimowei.Activity.garbage.login_function.LoginActivity;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;

public class UpdatePasswordActivity extends BaseActivity {
	/**
	 * 新密码输入框
	 */
	private EditText password_edit;
	/**
	 * 确认新密码输入框
	 */
	private EditText confirm_edit;
	/**
	 * 原密码输入框
	 */
	private EditText old_edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_password);
		settitle("修改密码");
		password_edit = (EditText) findViewById(R.id.password_edit);
		confirm_edit = (EditText) findViewById(R.id.password_confirm);
		old_edit = (EditText) findViewById(R.id.old_edit);
	
	}

	/**
	 * 取消点击事件
	 * 
	 */
	public void cancel(View view) {
		onBackPressed();
	}

	/**
	 * 修改密码点击事件
	 */
	public void update(View view) {
		final String oldpassword = old_edit.getText().toString().trim();
		final String confim = confirm_edit.getText().toString().trim();
		final String password = password_edit.getText().toString().trim();
		//原密码不能为空
		if (TextUtils.isEmpty(oldpassword)) {
			//Toast.makeText(getApplicationContext(), "原密码不能为空", 0).show();
			MyToast.ShowMyToast(getApplicationContext(),"原密码不能为空");
			return;

		}
		//新密码不能为空
		if (TextUtils.isEmpty(password)) {
			//Toast.makeText(getApplicationContext(), "新密码不能为空", 0).show();
			MyToast.ShowMyToast(getApplicationContext(),"新密码不能为空");
			return;

		}
		//新密码和确认密码不一致
		if (!password.equals(confim)) {
			//Toast.makeText(getApplicationContext(), "新密码和确认密码不一致", 0).show();
			MyToast.ShowMyToast(getApplicationContext(),"新密码和确认密码不一致");
			return;

		}
		new Thread(){
			public void run() {
			//判断网络
			if (NetUtil.checkNet(getApplication())) {
				
				MyPersonCenterEngineimpl im = new MyPersonCenterEngineimpl();

				String result = im.updatePassword(sp.getString("YHZH",""),oldpassword,password);
				//原密码错误
				if ("密码错误".equals(result)) {

					
					runOnUiThread(new Runnable() {
						public void run() {
							//Toast.makeText(getApplicationContext(), "原密码错误", 0).show();
							MyToast.ShowMyToast(getApplicationContext(),"原密码错误");
						}
					});
				
				} else if ("修改成功".equals(result)) {
					//修改成功
					runOnUiThread(new Runnable() {
						public void run() {
							//Toast.makeText(getApplicationContext(), "修改成功", 0).show();
							MyToast.ShowMyToast(getApplicationContext(),"修改成功");
							returnLoading();
						}
						//返回登录框
						private void returnLoading() {
							
							GlobalParams.User = "";
							GlobalParams.isLogin=false;
							
							Editor editor = sp.edit();
							editor.putString("yonghuleibie",""); 
							editor.putString("SQYHMC", "");
							editor.putString("YX",  "");
							editor.putString("SQDM",  "");
							editor.putString("YDDH",  "");
							editor.putString("HZXM",  "");
							editor.putString("YHZH",  "");
							editor.commit();
							Intent intent = new Intent(UpdatePasswordActivity.this, LoginActivity.class);
							startActivity(intent);
							
						}
					});
				
				
				}else{
					runOnUiThread(new Runnable() {
						public void run() {
							//Toast.makeText(getApplicationContext(), "修改失败", 0).show();
							MyToast.ShowMyToast(getApplicationContext(),"修改失败");
							//returnHomeActivity();
						}
					});
				}
				
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						//Toast.makeText(getApplicationContext(), "网络不稳定", 0).show();
						PromptManager.showNoNetWork(UpdatePasswordActivity.this);
					}
				});
			
			
				
			}
			
		}

//			private void returnHomeActivity() {
//				Intent intent = new Intent(UpdatePasswordActivity.this, MainActivity.class);
//				startActivity(intent);
//				
//			};
			}.start();
	
		
		
		
	}
}
