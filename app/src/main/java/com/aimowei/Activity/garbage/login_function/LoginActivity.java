package com.aimowei.Activity.garbage.login_function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.GlobalParams;
import com.aimowei.Activity.garbage.MainActivity;
import com.aimowei.Activity.garbage.bean.LajiNameBean;
import com.aimowei.Activity.garbage.bean.LevelBean;
import com.aimowei.Activity.garbage.engine.impl.UserLoginEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
import com.alibaba.fastjson.JSON;

public class LoginActivity extends BaseActivity {
	/**记录第一次按返回键的时间
	 * 
	 */
	private long mExitTime;
	/**用户名输入框
	 * 
	 */
	private EditText username_edit;
	/**密码输入框
	 * 
	 */
	private EditText password_edit;
	/**记住密码checkbox
	 * 
	 */
	private CheckBox cb_remember_password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		username_edit = (EditText) findViewById(R.id.username_edit);
		password_edit = (EditText) findViewById(R.id.password_edit);
		settitle("登录");
		//隐藏返回键
		invisible(View.INVISIBLE);
		cb_remember_password = (CheckBox) findViewById(R.id.cb_remerber);
		//记住密码功能
		boolean isRember = sp.getBoolean("isRember", false); // 记住密码
		cb_remember_password.setChecked(isRember);
		if (isRember) {
			username_edit.setText(sp.getString("phone", ""));
			password_edit.setText(sp.getString("password", ""));

		}

		cb_remember_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							Editor editor = sp.edit();
							editor.putBoolean("isRember", true);
							editor.commit();
						} else {
							Editor editor = sp.edit();
							editor.putBoolean("isRember", false);
							editor.commit();
						}

					}
				});
	}
	private String laji1;
	private JSONObject object;
	private int status;//状态变量
	private String result;
	private static List<LajiNameBean> List = new ArrayList<LajiNameBean>();
/**登录点击事件
 * 
 * @param view
 */
	public void login(View view) {
		final String username = username_edit.getText().toString().trim();
		final String password = password_edit.getText().toString().trim();
		//用户名不能为空
		if (TextUtils.isEmpty(username)) {
			//Toast.makeText(getApplicationContext(), "用户名不能为空", 0).show();
			MyToast.ShowMyToast(getApplicationContext(), "用户名不能为空");
			return;

		}
		//密码不能为空
		if (TextUtils.isEmpty(password)) {
			//Toast.makeText(getApplicationContext(), "密码不能为空", 0).show();
			MyToast.ShowMyToast(getApplicationContext(), "密码不能为空");
			return;

		}
		new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				//判断网络
				if (NetUtil.checkNet(getApplication())) {

					UserLoginEngineImpl im = new UserLoginEngineImpl();

					result = im.getUserLoginInfoByList(username, password);
					System.out.println(result);
					try {
						object = new JSONObject(result);
						laji1 = object.getString("laji1");
						List = JSON.parseArray(laji1, LajiNameBean.class);
						Editor editor2= sp.edit();
						List<String> ls=new ArrayList<String>();
						 
						for(LajiNameBean str:List){
							ls.add(str.getName());
						}
						 Set<String> set=new HashSet<String>();   
						 set.addAll(ls);
						editor2.putStringSet("lajiNameList",set);
						System.out.println(ls.get(0).toString()+"sdasjnds");
						editor2.commit();
						status = object.getInt("status");
						//status为1表示登录成功
						if (status == 1) {
							try {
								object = new JSONObject(result);
								Editor editor = sp.edit();
								editor.putString("yonghuleibie",
										object.getString("yonghuleibie"));// 用户类别;

								editor.commit();

								if (sp.getString("yonghuleibie", "")
										.equals("1")) {
									//"1"为 普通用户
									object = new JSONObject(result);
									Editor editor1 = sp.edit();
									editor1.putString("SQYHMC",
											object.getString("SQYHMC"));// 用户全名称
									editor1.putString("YX",
											object.getString("YX"));// 邮箱
									editor1.putString("SQDM",
											object.getString("SQDM"));// 社区代码
									editor1.putString("YDDH",
											object.getString("YDDH"));// 移动电话
									editor1.putString("HZXM",
											object.getString("HZXM"));// 户主名称
									editor1.putString("YHZH",
											object.getString("YHZH"));// 用户帐号
									editor1.putString("phone", username);
									editor1.putString("password", password);
									editor1.commit();

								} else if (sp.getString("yonghuleibie", "")
										.equals("0")) {
									// “0” 为管理员
									object = new JSONObject(result);
									Editor editor1 = sp.edit();

									editor1.putString("YX",
											object.getString("YX"));
									editor1.putString("SQDM",
											object.getString("SQDM"));
									editor1.putString("YDDH",
											object.getString("YDDH"));

									editor1.putString("YHZH",
											object.getString("YHZH"));
									editor1.putString("phone", username);
									editor1.putString("password", password);
									editor1.commit();

								} else if(sp.getString("yonghuleibie", "")
										.equals("2")) {
									object = new JSONObject(result);

									// 外来志愿者
									Editor editor1 = sp.edit();

									editor1.putString("YX",
											object.getString("YX"));// 邮箱
									editor1.putString("SQDM",
											object.getString("SQDM"));// 社区代码
									editor1.putString("YDDH",
											object.getString("YDDH"));// 移动电话

									editor1.putString("YHZH",
											object.getString("YHZH"));// 用户帐号

									editor1.putString("phone", username);//手机号码
									editor1.putString("password", password);//密码

									editor1.commit();

								}else{
									//"3"为 商户
									object = new JSONObject(result);
									Editor editor1 = sp.edit();
									editor1.putString("SQYHMC",
											object.getString("SQYHMC"));// 商户全称
									editor1.putString("YX",
											object.getString("YX"));// 邮箱
									editor1.putString("SQDM",
											object.getString("SQDM"));// 社区代码
									editor1.putString("YDDH",
											object.getString("YDDH"));// 移动电话
									editor1.putString("HZXM",
											object.getString("HZXM"));// 负责人
									editor1.putString("YHZH",
											object.getString("YHZH"));// 用户帐号
									editor1.putString("phone", username);
									editor1.putString("password", password);
									editor1.commit();
									
								}

								GlobalParams.User = username;
								GlobalParams.isLogin = true;
								runOnUiThread(new Runnable() {
									public void run() {
										//Toast.makeText(getApplicationContext(),
										//		"登录成功", 0).show();
									//自定义Toast
										MyToast.ShowMyToast(getApplicationContext(), "登录成功");
									}
								});

								returnHomeActivity();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if (status == 0) {
							//status为0表示登录失败
							GlobalParams.isLogin = false;
							runOnUiThread(new Runnable() {
								public void run() {
									//Toast.makeText(getApplicationContext(),
									//		"用户名或者密码错误", 0).show();
									//自定义Toast
									MyToast.ShowMyToast(getApplicationContext(), "用户名或者密码错误");
								}
							});

							return;

						}
						// return "";
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					//无网络时的操作
					runOnUiThread(new Runnable() {
						public void run() {
							//Toast.makeText(getApplicationContext(), "网络不稳定", 0)
							//		.show();
							//显示无网络
							PromptManager.showNoNetWork(LoginActivity.this);
						}
					});

				}

			}
			//进入主界面
			private void returnHomeActivity() {
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);

			};
		}.start();

		// Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		// startActivity(intent);
	}

	/** 按两次退出程序
	 * 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Object mHelperUtils;
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
				
			} else {
				exitSystem();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
