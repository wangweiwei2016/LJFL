package com.aimowei.Activity.garbage.mycenter_function;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.engine.impl.ConsultEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;

public class ConsultActivity extends BaseActivity {
	/**
	 * 
	 */
	private TextView consult_textView;
	/**
	 * 文本数字
	 */
	private TextView tv_num;
	/**
	 * 咨询内容文本框
	 */
	private EditText consult_editText;
	/**
	 * 最大字数
	 */
	private static final int MAX_COUNT = 100;
	/**
	 * 咨询类别标签
	 */
	private Spinner consult_spinner;
	/**
	 * 咨询类别字符串适配器
	 */
	private ArrayAdapter<String> adapter;
	/**
	 * 是否可以提交
	 */
	private boolean canSubmit = false ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consult);
		settitle("咨询");
		
		consult_textView = (TextView) findViewById(R.id.consult_textView);
		tv_num = (TextView) findViewById(R.id.tv_num);
		consult_spinner = (Spinner) findViewById(R.id.consult_spinner);
		consult_editText = (EditText) findViewById(R.id.consult_editText);
		consult_editText.addTextChangedListener(mTextWatcher);
		consult_editText.setSelection(consult_editText.length()); // 将光标移动最后一个字符后面
		setLeftCount();

		// 1、设置数据源
		// String[] list = { "垃圾投放", "积分奖品", "系统使用", "其他" };
		new Thread() {

			@Override
			public void run() {
				String[] tmpList = null;
				if (NetUtil.checkNet(getApplication())) {
					tmpList = new ConsultEngineImpl().getConsultType();
					if (tmpList == null) {
						if (ConsultEngineImpl.isSucceed)
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									MyToast.ShowMyToast(
											getApplicationContext(), "服务器上数据为空");
									ConsultActivity.this.finish();
								}
							});
						else {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									MyToast.ShowMyToast(
											getApplicationContext(),
											ConsultEngineImpl.errorMsg);
									ConsultActivity.this.finish();
								}
							});
						}
					} else
						runOnUiThread(new Runnable() {
							String[] list = null;

							public Runnable setList(String[] tmpList) {
								list = tmpList;
								return this;
							}

							@Override
							public void run() {
								initSpinner(list);
							}
						}.setList(tmpList));
				} else {
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							MyToast.ShowMyToast(getApplicationContext(),
									"网络不稳定,请稍后重试o(︶︿︶)o ");
							ConsultActivity.this.finish();
						}
					});
				}
			}
		}.start();

	}

	private void initSpinner(String[] list) {
		// 2、新建ArrayAdapter（数组适配器）
		// adapter = new
		// ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
		// list);
		adapter = new ArrayAdapter<String>(this, R.layout.setting_spinner_item,
				list);

		// 3、adapter设置一个下拉列表样式
		adapter.setDropDownViewResource(R.layout.drop_down_item);
		// 4、spinner加载适配器
		consult_spinner.setAdapter(adapter);
		canSubmit = true;
	}

	private TextWatcher mTextWatcher = new TextWatcher() {

		private int editStart;

		private int editEnd;

		public void afterTextChanged(Editable s) {
			editStart = consult_editText.getSelectionStart();
			editEnd = consult_editText.getSelectionEnd();

			// 先去掉监听器，否则会出现栈溢出
			consult_editText.removeTextChangedListener(mTextWatcher);

			// 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
			// 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
			while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
			}
			consult_editText.setText(s);
			consult_editText.setSelection(editStart);

			// 恢复监听器
			consult_editText.addTextChangedListener(mTextWatcher);

			setLeftCount();
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	};

	/**
	 * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
	 * 
	 * @param c
	 * @return
	 */
	private long calculateLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int tmp = (int) c.charAt(i);
			if (tmp > 0 && tmp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	/**
	 * 刷新剩余输入字数,最大值新浪微博是140个字，人人网是200个字
	 */
	private void setLeftCount() {
		tv_num.setText(String.valueOf((MAX_COUNT - getInputCount())));
	}

	/**
	 * 获取用户输入的分享内容字数
	 * 
	 * @return
	 */
	private long getInputCount() {
		return calculateLength(consult_editText.getText().toString());
	}

	private JSONObject object;
	private int status;
	private String result;

	public void consult(View view) {
		if(!canSubmit)
		{
			MyToast.ShowMyToast(getApplicationContext(),
					"正在加载咨询类型,请稍等..");
			return ;
		}
		final String type = consult_spinner.getSelectedItem().toString().trim();
		final String faqineirong = consult_editText.getText().toString().trim();
		if (TextUtils.isEmpty(faqineirong)) {
			// Toast.makeText(getApplicationContext(), "请填写咨询内容", 0).show();
			MyToast.ShowMyToast(getApplicationContext(), "请填写咨询内容");
			return;
		}
		new Thread() {
			public void run() {

				if (NetUtil.checkNet(getApplication())) {

					ConsultEngineImpl im = new ConsultEngineImpl();
					// 如果是1（社区用户）发起人是社区用户名称，如果是0（管理员）或2（志愿者）就是用户账号
					if ("1".equals(sp.getString("yonghuleibie", ""))) {
						result = im.getConsult("1", type, faqineirong,
								sp.getString("SQYHMC", ""));
					} else
						result = im.getConsult("1", type, faqineirong,
								sp.getString("YHZH", ""));

					if ("建议咨询类别代码错误".equals(result)) {
						runOnUiThread(new Runnable() {
							public void run() {
								// Toast.makeText(getApplicationContext(),
								// "建议咨询类别代码错误", 0).show();
								MyToast.ShowMyToast(getApplicationContext(),
										"建议咨询类别代码错误");
							}
						});
						return;

					} else if ("添加失败".equals(result)) {
						runOnUiThread(new Runnable() {
							public void run() {
								// Toast.makeText(getApplicationContext(),
								// "提交失败", 0).show();
								MyToast.ShowMyToast(getApplicationContext(),
										"提交失败");
							}
						});
					} else if ("添加成功".equals(result)) {
						runOnUiThread(new Runnable() {
							public void run() {
								// Toast.makeText(getApplicationContext(),
								// "提交成功", 0).show();
								MyToast.ShowMyToast(getApplicationContext(),
										"提交成功");
								consult_editText.setText("");
							}
						});
					}

				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							// Toast.makeText(getApplicationContext(), "网络不稳定",
							// 0).show();
							PromptManager.showNoNetWork(ConsultActivity.this);
						}
					});

				}

			}

		}.start();

	}

}
