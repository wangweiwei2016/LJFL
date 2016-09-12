package com.aimowei.Activity.garbage.home_function;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.MainActivity;
import com.aimowei.Activity.garbage.engine.impl.MyPersonCenterEngineimpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.Activity.garbage.utils.TextUtils1;
import com.aimowei.Activity.garbage.zxing.activity.CaptureActivity;
import com.aimowei.garbage.R;

public class TwoDimensionSearchActivity extends BaseActivity {
	/**
	 * 手动输入的输入框
	 * 
	 */
	private EditText resultTextView;
	/**
	 * 扫描二维码得到的结果Text
	 * 
	 */
	private TextView tv_result;
	/**
	 * 厨余垃圾下拉框
	 * 
	 */
	private Spinner spinner;
	/**
	 * 重量输入框
	 * 
	 */
	private EditText et_zl;
	/**
	 * 厨余垃圾类别list
	 * 
	 */
	private Button btn_add;
	private List<String> list;

	// private Spinner spinnerTwo;
	// private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_dimension_search);
		settitle("二维码扫描");
		resultTextView = (EditText) this.findViewById(R.id.et_scan_result);
		spinner = (Spinner) findViewById(R.id.spinner);
		et_zl = (EditText) findViewById(R.id.et_zl_string);
		tv_result = (TextView) findViewById(R.id.tv_scan_result);
		btn_add=(Button) findViewById(R.id.btn_add_laji);
		setPricePoint(et_zl);
		// 1、设置数据源
		list = new ArrayList<String>();
		
		Set<String> set=sp.getStringSet("lajiNameList", null);
		list.addAll(set);
//		list.add("厨余垃圾");

		/*
		 * //2、新建ArrayAdapter（数组适配器） adapter = new
		 * ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
		 * list); //3、adapter设置一个下拉列表样式
		 * adapter.setDropDownViewResource(android.R
		 * .layout.simple_spinner_dropdown_item); //4、spinner加载适配器
		 * spinner.setAdapter(adapter);
		 */
		// 根据原生态样式改变而来的自定义样式
		// Spinner中文框显示样式
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.my_simple_spinner_self_item, list);
		// Spinner下拉菜单显示样式
		adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		Button scanBarCodeButton1 = (Button) findViewById(R.id.btn_scan_barcode);
		// 二维码扫描点击事件
		scanBarCodeButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultTextView.setText("");
				resultTextView.setVisibility(View.INVISIBLE);
				tv_result.setVisibility(View.VISIBLE);

				// 打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(
						TwoDimensionSearchActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
		LinearLayout scanBarCodeButton = (LinearLayout) findViewById(R.id.ll_scan_barcode);
		scanBarCodeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 打开扫描界面扫描条形码或二维码
				Intent openCameraIntent = new Intent(
						TwoDimensionSearchActivity.this, CaptureActivity.class);
				startActivityForResult(openCameraIntent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");

			tv_result.setText(TextUtils1.stringFilter(TextUtils1
					.ToDBC(scanResult)));

			/*
			 * String UTF_Str = ""; String GB_Str = ""; boolean is_cN = false;
			 * try {
			 * 
			 * UTF_Str = new String(scanResult.getBytes("ISO-8859-1"), "UTF-8");
			 * System.out.println("这是转了UTF-8的" + UTF_Str); is_cN =
			 * IsChineseOrNot.isChineseCharacter(UTF_Str); //
			 * 防止有人特意使用乱码来生成二维码来判断的情况 boolean b =
			 * IsChineseOrNot.isSpecialCharacter(scanResult); if (b) { is_cN =
			 * true; } System.out.println("是为:" + is_cN); if (!is_cN) { GB_Str =
			 * new String(scanResult.getBytes("ISO-8859-1"), "GB2312");
			 * System.out.println("这是转了GB2312的" + GB_Str); } } catch
			 * (UnsupportedEncodingException e) { // TODO Auto-generated catch
			 * block e.printStackTrace(); } Bitmap bm =
			 * intent.getParcelableExtra("IMG_return"); if (is_cN) {
			 * 
			 * resultTextView.setText(UTF_Str); } else {
			 * resultTextView.setText(GB_Str); }
			 */

		} else {
			tv_result.setText("扫描结果为空，请重新扫描或者手动输入");

		}
	}

	/**
	 * 用户全名
	 * 
	 */
	String Yonghuming ="";
	String yonghumingS="";
	String yonghuming="";
//当a=0 线程不关闭 为1线程关闭
	int a=0;
	/**
	 * 添加重量点击事件
	 * 
	 * @param view
	 */
	public void add(View view) {
		// 获得选中项名称
		final String lajiming = spinner.getSelectedItem().toString().trim();

		final String zhongliang = et_zl.getText().toString().trim();
		yonghuming = resultTextView.getText().toString().trim();
		 yonghumingS = tv_result.getText().toString().trim();
		Yonghuming = "";
//		// 用户名为空
//		if (TextUtils.isEmpty(yonghumingS) && TextUtils.isEmpty(yonghuming)) {
//			// Toast.makeText(getApplicationContext(), "用户名不能为空", 0).show();
//			MyToast.ShowMyToast(getApplicationContext(), "用户名不能为空");
//			return;
//
//		}
		if(resultTextView.getVisibility()==View.VISIBLE){
			Yonghuming = yonghuming;
			if (TextUtils.isEmpty(yonghuming) ) {
				// Toast.makeText(getApplicationContext(), "用户名不能为空", 0).show();
				MyToast.ShowMyToast(getApplicationContext(), "用户名不能为空");
				return;

			}
		}
//		if (TextUtils.isEmpty(yonghuming)) {
//			Yonghuming = yonghumingS;
//
//		}
		if(tv_result.getVisibility()==View.VISIBLE){
			Yonghuming = yonghumingS;
			if (TextUtils.isEmpty(yonghumingS) ) {
				// Toast.makeText(getApplicationContext(), "用户名不能为空", 0).show();
				MyToast.ShowMyToast(getApplicationContext(), "用户名不能为空");
				return;

			}
		}
//		if (TextUtils.isEmpty(yonghumingS)) {
//
//			Yonghuming = yonghuming;
//		}
		// 重量不能为空
		if (TextUtils.isEmpty(zhongliang)) {
			// Toast.makeText(getApplicationContext(), "重量不能为空", 0).show();
			MyToast.ShowMyToast(getApplicationContext(), "重量不能为空");
			return;

		}
		// 重量的格式
		if (zhongliang.substring(0, 1).equals(".")) {
			// Toast.makeText(getApplicationContext(), "重量格式不对", 0).show();
			MyToast.ShowMyToast(getApplicationContext(), "重量格式不对");
			return;

		}
		
		//添加progressDialog 使用户不多点
		final ProgressDialog dialog=new ProgressDialog(this);
	
		//touch 不消失
		dialog.setCanceledOnTouchOutside(false);
		dialog.setTitle("正在添加中。。。");
		dialog.setMessage("请稍候。。。");
		  dialog.setOnCancelListener(new OnCancelListener(){
			     @Override
			     public void onCancel(DialogInterface arg0) {
			     a=1;
			    	 //用于终止我的线程
			      
			     }});
	dialog.show();
	
	a=0;
	new Thread() {
			public void run() {
			
				  //判断线程是否被终止
				  if(a==1){
						dialog.dismiss();
			
					    }
				// 判断网络
				if (NetUtil.checkNet(getApplication())) {

					MyPersonCenterEngineimpl im = new MyPersonCenterEngineimpl();

					String result = im.addLajiToufang(Yonghuming, lajiming,
							zhongliang);
					//if(result.equals("网络连接不稳定")||result==null)
					if(result.equals("网络连接不稳定")){
						runOnUiThread(new Runnable() {
							public void run() {

								dialog.dismiss();
								// Toast.makeText(getApplicationContext(),
								// "社区用户名称错误", 0).show();
								
								MyToast.ShowMyToast(getApplicationContext(),
										"网络连接不稳定");
								
							}
						});
						return;
					}
					//社区用户名称错误
					else if ("社区用户名称错误".equals(result)) {

						runOnUiThread(new Runnable() {
							public void run() {

								dialog.dismiss();
								// Toast.makeText(getApplicationContext(),
								// "社区用户名称错误", 0).show();
								
								MyToast.ShowMyToast(getApplicationContext(),
										"社区用户名称错误");
								
							}
						});

					} else if ("垃圾名称错误".equals(result)) {
						// 垃圾名称错误
						runOnUiThread(new Runnable() {
							public void run() {
							dialog.dismiss();
								// Toast.makeText(getApplicationContext(),
								// "垃圾名称错误", 0).show();
								MyToast.ShowMyToast(getApplicationContext(),
										"垃圾名称错误");
								
							}
						});

					} else if ("用户未激活".equals(result)) {
						// 垃圾名称错误
						runOnUiThread(new Runnable() {
							public void run() {
							dialog.dismiss();
								// Toast.makeText(getApplicationContext(),
								// "垃圾名称错误", 0).show();
								MyToast.ShowMyToast(getApplicationContext(),
										"用户未激活");
								
							}
						});

					} else if ("已投放过，请不要再投放".equals(result)) {
						// 垃圾名称错误
						runOnUiThread(new Runnable() {
							public void run() {
							dialog.dismiss();
								// Toast.makeText(getApplicationContext(),
								// "垃圾名称错误", 0).show();
							et_zl.setText("");
							resultTextView.setText("");
								MyToast.ShowMyToast(getApplicationContext(),
										"已投放过，请不要再投放");
								
							}
						});

					}
					
					else if ("添加成功".equals(result)) {
						// 添加成功
						runOnUiThread(new Runnable() {
							public void run() {
								// Toast.makeText(getApplicationContext(),
								// "添加成功", 0).show();
								dialog.dismiss();
								MyToast.ShowMyToast(getApplicationContext(),
										"添加成功");
								et_zl.setText("");
								resultTextView.setText("");
								tv_result.setText("添加成功请重新扫描");
							}
						});
						return;

					} else if("添加失败".equals(result)){
						runOnUiThread(new Runnable() {
							public void run() {
								// Toast.makeText(getApplicationContext(),
								// "添加失败", 0).show();
								dialog.dismiss();
								MyToast.ShowMyToast(getApplicationContext(),
										"添加失败");
							}
						});
						return;
					}else if("网络问题".equals(result)){
						runOnUiThread(new Runnable() {
							public void run() {
								dialog.dismiss();
								// Toast.makeText(getApplicationContext(),
								// "添加失败", 0).show();
								MyToast.ShowMyToast(getApplicationContext(),
										"网络异常请检查网络");
							}
						});
						return;
					}else{
						runOnUiThread(new Runnable() {
							public void run() {

								dialog.dismiss();
								// Toast.makeText(getApplicationContext(),
								// "社区用户名称错误", 0).show();
								
								MyToast.ShowMyToast(getApplicationContext(),
										"网络连接不稳定");
								
							}
						});
						return;
					}

				} else { // 无网络的时候
					runOnUiThread(new Runnable() {
						public void run() {
							// Toast.makeText(getApplicationContext(), "网络不稳定",
							// 0).show();
							dialog.dismiss();
							PromptManager
									.showNoNetWork(TwoDimensionSearchActivity.this);
						}
					});

				}

			}

		}.start();

	}

	/**取消点击事件
	 * 
	 * @param view
	 */
	public void cancel(View view) {
		Intent intent = new Intent(TwoDimensionSearchActivity.this,
				MainActivity.class);
		startActivity(intent);
	}
	public  void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
 
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
 
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
 
            }
 
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                 
            }

	
        });
 
    }
	/**手动输入
	 * 
	 * @param view
	 */
	public void input_manal(View view) {
		tv_result.setText("");
		tv_result.setVisibility(View.INVISIBLE);
		resultTextView.setVisibility(View.VISIBLE);

		resultTextView.setFocusable(true);
	}
}
