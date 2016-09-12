package com.aimowei.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

//生成该类的对象并调用Excute,首先执行的是onPreExcute,该方法执行在UI线程
public class MyAsyncTask extends AsyncTask<String, Void, String> {

	// 构�?函数用于接收从UI线程传过来的参数�?
	private String goal = null;
	private String password = null;
	private String userName = null;
	private Context context = null;
	private Spinner spinner = null;
	private ListView list = null;
	private ArrayAdapter<String> adapter;

	public MyAsyncTask(Context context) {

		this.context = context;
	}

	public MyAsyncTask(Context context, Spinner spinner) {

		this.context = context;
		this.spinner = spinner;
	}

	public MyAsyncTask(Context context, ListView list) {

		this.context = context;
		this.list = list;
	}

	// 该方法执行在UI线程之中，在doInBackground执行结束之后，将参数返回给onPostExecute�?
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (goal == "得到全部员工") {
			String[] payPerson = {};
			// json数据解析
			JsonUtils jsonUtils = new JsonUtils();
			payPerson = jsonUtils.getAllEmployee(result);

			// 设置下拉框内�?
			// 将可选内容与ArrayAdapter连接起来
			adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_spinner_item, payPerson);
			// 设置下拉列表的风�?
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// 将adapter 添加到spinner�?
			spinner.setAdapter(adapter);
			// 添加事件Spinner事件监听
			// spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
			// 设置默认�?
			spinner.setVisibility(View.VISIBLE);

			return;
		} else if (goal == "Login") {
			// Toast.makeText(context, result, Toast.LENGTH_LONG).show();

		}
	}

	/**
	 * listview中点击按键弹出对话框
	 */
	public void showInfo() {
		new AlertDialog.Builder(context).setTitle("详情")
				.setMessage("经办人：尚辉�?   状�?：已结束")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}

	// 该方法执行在UI线程之中
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	// 第一个参数定�?doInBackground接收参数类型
	// 第二个参数定义onProgressUpdate 参数类型
	// 第三个参数定义doInBackground的返回�?类型以及onPostExcute的参数类�?
	// 当在UI线程执行以下代码�?MyAsyncTask myAsyncTask=new
	// MyAsyncTask(textView,progressBar)//参数传到MyAsyncTask的构造函数里
	// myAsyncTask.excute(1000)此处参数 �?doInBackground的参数，是泛�?

	// 该方法不能执行用户UI线程的修改�?设置操作�?
	@Override
	protected String doInBackground(String... arg) {
		String result = "";
		this.goal = arg[0];
		if (goal == "Login") {
			NetOperator netOperator = new NetOperator();
			String url = "http://145.28.104.112:8887/index.php/mqtt/bind?deviceid=";
			url += arg[1];
			url += "&nickname=";
			url += arg[2];
			// Toast.makeText(context, url, Toast.LENGTH_LONG).show();
			result = netOperator.GetResquest(url);
		}

		// publishProgress(result);//该方法用于执行UI线程的信息修改，其调用OnPregressUpdate，执行在用户线程。其参数为泛型，传到OnPregressUpdate里面�?
		return result;
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
