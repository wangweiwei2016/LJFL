package com.aimowei.Activity.garbage.mycenter_function;

import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.engine.impl.MyPersonCenterEngineimpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
import com.umeng.message.PushAgent;

public class UpdateMyInfoActivity extends BaseActivity {
	/**
	 * 用户全称TextView
	 */
	private TextView tv_user_name;
	/**
	 * 户主TextView
	 */
	private TextView tv_huzhu_name;
	/**
	 * 手机输入框
	 */
	private EditText et_phone;
	/**
	 * 电子邮件输入框
	 */
	private EditText et_email;
	//友盟消息推送
	PushAgent mPushAgent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_info);
		settitle("修改个人信息");
		mPushAgent = PushAgent.getInstance(this);
		tv_user_name = (TextView) findViewById(R.id.tv_user_name);
		tv_huzhu_name = (TextView) findViewById(R.id.tv_huzhu_name);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_email = (EditText) findViewById(R.id.et_email);

		if (sp.getString("yonghuleibie", "").equals("1")) {
			// 普通用户显示用户和户主
			tv_user_name.setText("用户:" + sp.getString("SQYHMC", ""));
			tv_huzhu_name.setText("户主：" + sp.getString("HZXM", ""));
			et_phone.setText(sp.getString("YDDH", ""));
			et_email.setText(sp.getString("YX", ""));

		} else {
			// 非普通用户
			ImageView im = (ImageView) findViewById(R.id.iv_huzhu);
			im.setVisibility(View.GONE);
			tv_user_name.setText("用户:" + sp.getString("YHZH", ""));
			tv_huzhu_name.setVisibility(View.GONE);
			et_phone.setText(sp.getString("YDDH", ""));
			et_email.setText(sp.getString("YX", ""));

		}
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
	 * 取消按钮
	 * 
	 */
	public void cancel(View view) {
		onBackPressed();
	}

	/**
	 * 修改按钮点击事件
	 * 
	 * 
	 */
	public void update(View view) {

		final String newphone = et_phone.getText().toString().trim();
		final String YX = et_email.getText().toString().trim();
		// 手机号不能为空
		if (TextUtils.isEmpty(newphone)) {
			// Toast.makeText(getApplicationContext(), "手机号码不能为空", 0).show();
			MyToast.ShowMyToast(getApplicationContext(), "手机号码不能为空");
		}
		// 手机格式要为11位
		if (newphone.length() != 11) {
			// Toast.makeText(getApplicationContext(), "手机号格式错误", 0).show();
			MyToast.ShowMyToast(getApplicationContext(), "手机号格式错误");
			return;

		}
		new Thread() {
			public void run() {
				// 判断网络
				if (NetUtil.checkNet(getApplication())) {

					MyPersonCenterEngineimpl im = new MyPersonCenterEngineimpl();

					String result = im.UpdateUserInfo(
							sp.getString("yonghuleibie", ""),
							sp.getString("YDDH", ""), newphone, YX);
					// 如果手机号已经存在
					if ("手机号码已经存在".equals(result)) {

						runOnUiThread(new Runnable() {
							public void run() {
								// Toast.makeText(getApplicationContext(),
								// "手机号码已经存在", 0).show();
								MyToast.ShowMyToast(getApplicationContext(),
										"手机号码已经存在");
								return;
							}
						});

					} else if ("修改成功".equals(result)) {
						// 修改成功
						runOnUiThread(new Runnable() {
							public void run() {
								// Toast.makeText(getApplicationContext(),
								// "修改成功",
								// 0).show();
								// 自定义Toast
								MyToast.ShowMyToast(getApplicationContext(),
										"修改成功");
								Editor editor = sp.edit();
								editor.putString("YDDH", newphone);
								editor.putString("YX", YX);
								editor.commit();
							
								addAlias(sp.getString("YDDH", "").toString().trim(), "userPhone");
							}

						});
						

					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(getApplicationContext(), "修改失败",
										0).show();
								return;
							}
						});
					}

				} else {
					//网络不稳定
					runOnUiThread(new Runnable() {
						public void run() {
							// Toast.makeText(getApplicationContext(), "网络不稳定",
							// 0)
							// .show();
							PromptManager
									.showNoNetWork(UpdateMyInfoActivity.this);
						}
					});

				}

			}

		}.start();

	}
}
