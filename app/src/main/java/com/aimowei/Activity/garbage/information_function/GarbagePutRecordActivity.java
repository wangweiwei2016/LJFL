
package com.aimowei.Activity.garbage.information_function;

import java.util.Calendar;
import java.util.LinkedList;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.engine.GarbagePutRecordQueryEngine.GarbagePutRecordInfoModel;
import com.aimowei.Activity.garbage.engine.QueryShareEngine.UIMode;
import com.aimowei.Activity.garbage.engine.impl.GarbagePutRecordQueryEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
/**
 * 垃圾投放记录查询Activity
 * @author SWZ
 */
public class GarbagePutRecordActivity extends BaseActivity implements
		android.view.View.OnClickListener {
	/**开始时间按钮*/
	private Button btn_starttime;
	/**结束时间按钮*/
	private Button btn_finishtime;

	/** 重试按钮 */
	private Button button_retry = null;
	/**
	 * 搜索按钮
	 */
	private Button button_search = null;
	/**
	 * 垃圾投放记录信息Model
	 */
	private LinkedList<GarbagePutRecordInfoModel> infos = null;
	/** 加载进度条 */
	private ProgressBar progressBar_loading = null;
	/**
	 * 提示文本
	 */
	private TextView textView_tip = null;
	/**
	 * 主要显示表格布局(垃圾投放记录列表)
	 */
	private TableLayout tableLayout_main = null;
	//private TableLayout tableLayout_address = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_garbage_put_recorder);
		settitle("垃圾投放记录");
		commonInit();
		btn_starttime = (Button) findViewById(R.id.btn_starttime);
		btn_starttime.setOnClickListener(this);

		btn_finishtime = (Button) findViewById(R.id.btn_finishtime);
		btn_finishtime.setOnClickListener(this);
		calendarBegin = Calendar.getInstance();
		calendarEnd = Calendar.getInstance();
		calendarEnd.setTimeInMillis(calendarBegin.getTimeInMillis());
		btn_starttime.setText(DateFormat.format("yyyy-MM-dd", calendarBegin.getTime())
				.toString());
		btn_finishtime.setText(DateFormat.format("yyyy-MM-dd", calendarEnd.getTime())
				.toString());

		button_search = (Button) findViewById(R.id.btn_search);
		button_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updateInfo();
			}
		});
		tableLayout_main = (TableLayout) this
				.findViewById(R.id.tableLayout_main);
		tableLayout_main.setStretchAllColumns(true);
		views.add(tableLayout_main);

		/*tableLayout_address = (TableLayout) this
				.findViewById(R.id.TableLayoutAddress);
		tableLayout_address.setStretchAllColumns(true);
		views.add(tableLayout_address);*/
		// updateInfo();
	}
	/**
	 * 显示受控的View集合
	 */
	private LinkedList<View> views = new LinkedList<View>();

	/**
	 * 公共的初始化
	 */
	private void commonInit() {
		progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
		button_retry = (Button) findViewById(R.id.button_retry);
		textView_tip = (TextView) findViewById(R.id.textView_tip);

		views.add(progressBar_loading);
		views.add(button_retry);
		views.add(textView_tip);
		button_retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateInfo();
			}
		});

		textView_tip.setText("这段时间内没有投放记录哦");
		String tmp = sp.getString("yonghuleibie", null);
		if (tmp == null || tmp.isEmpty()) {
			canQuery =(true);
		} else {
			if (tmp.equals("0")) {//管理员
				canQuery = (false);
			} else if (tmp.equals("1")) {
				canQuery =(true);
			} else if (tmp.equals("2")) {
				canQuery =(true);
			}
		}
	}
	/**
	 * 指示是否可以进行查询(主要由用户类别得出)
	 */
	boolean canQuery = true;
	/**
	 * 设置UI模式
	 * @param mode UI模式
	 * @param updateUIData 指示是否更新数据
	 */
	private void setUIMode(UIMode mode, boolean updateUIData) {
		//Log.e(this.getClass().toString() + ":UIMode=", mode.toString());
		switch (mode) {
		case UI_1:
			if (updateUIData) {
				updatePutRecordList();
			}
			LinkedList<View> visibleViews = new LinkedList<View>();
			visibleViews.add(tableLayout_main);
			//visibleViews.add(tableLayout_address);
			setViewVisible(visibleViews);
			break;
		case UI_Loading:
			setViewVisible(progressBar_loading);
			break;
		case UI_OnlyRetry:
			setViewVisible(button_retry);
			break;
		case UI_OnlyTip:
			setViewVisible(textView_tip);
			break;
		default:
			MyToast.ShowMyToast(getApplicationContext(), "发现未知UI模式!!");
			break;
		}
	}

	//private LinkedList<String> addressList = new LinkedList<String>();

	/*private String controlAddress(String address) {
		if (address == null || address.equals("") || address.equals("null"))
			return address;// 保留
		if (addressList.contains(address))
			return "地址" + (addressList.indexOf(address) + 1);
		addressList.addLast(address);
		return "地址" + addressList.size();
	}*/

	/**
	 * 更新投放记录列表
	 */
	private void updatePutRecordList() {
		tableLayout_main.removeViews(1, tableLayout_main.getChildCount() - 1);
		boolean isDual = false;
		TableRow tableRow = null;
		RelativeLayout view = null;
		TableLayout tableLayout = null;
		TextView textView_putTime = null;
		TextView textView_putQuantity = null;
		TextView textView_getIntergral = null;
		if (infos != null && !infos.isEmpty()) {
			for (GarbagePutRecordInfoModel model : infos) {

				view = (RelativeLayout) this.getLayoutInflater().inflate(
						R.layout.activity_garbage_put_recorder, null);
				tableRow = (TableRow) view.findViewById(R.id.tableRow_main);
				tableLayout = (TableLayout) view
						.findViewById(R.id.tableLayout_main);
				tableLayout.removeAllViews();
				textView_putTime = (TextView) tableRow
						.findViewById(R.id.textView_putTime);
				textView_putQuantity = (TextView) tableRow
						.findViewById(R.id.textView_putQuantity);
				textView_getIntergral = (TextView) tableRow
						.findViewById(R.id.textView_getIntergral);

				textView_putTime.setText(EventArrangementActivity
						.getFitString(model.time));
				textView_putQuantity.setText(EventArrangementActivity
						.getFitString(model.weight));
				textView_getIntergral.setText(EventArrangementActivity
						.getFitString(model.integral));
				if (isDual) {
					tableRow.setBackgroundColor(Color.rgb(0xf5, 0xf5, 0xf5));
				}
				isDual = !isDual;
				tableLayout_main.addView(tableRow);
			}
		} else
			setUIMode(UIMode.UI_OnlyRetry, false);
	}
	/**
	 * 让指定列表里面的View显示
	 * @param visibleViews 要显示的view
	 */
	private void setViewVisible(LinkedList<View> visibleViews) {
		for (View view : views) {
			if (visibleViews.contains(view))
				view.setVisibility(View.VISIBLE);
			else
				view.setVisibility(View.GONE);
		}
	}
	/**
	 * 让指定的View显示
	 * @param visibleView
	 */
	private void setViewVisible(View visibleView) {
		for (View view : views) {
			if (view.equals(visibleView))
				view.setVisibility(View.VISIBLE);
			else
				view.setVisibility(View.GONE);
		}
	}

	/**
	 * 常量,两周的毫秒数
	 */
	private final long WeekMillis = 1000 * 60 * 60 * 24 * 7 * 2;
	/**
	 * 开始日期
	 */
	Calendar calendarBegin =null;
	/**
	 * 结束日期
	 */
	Calendar calendarEnd =null;
	/**
	 * 联网更新信息
	 */
	private void updateInfo() {
		if(!canQuery)
		{
			MyToast.ShowMyToast(GarbagePutRecordActivity.this, "社区用户才能查垃圾投放记录哦");
			return;
		}
		long queryTime = calendarEnd.getTimeInMillis()
				- calendarBegin.getTimeInMillis();//计算毫秒差值
		if (queryTime > WeekMillis || queryTime < 0) {//小于0或大于2周
			MyToast.ShowMyToast(GarbagePutRecordActivity.this, "查询日期间隔须在2周以内");
			return;
		}
		setUIMode(UIMode.UI_Loading, false);

		new Thread() {
			Calendar calendarBegin = null;
			Calendar calendarEnd = null;

			public Thread setDate(Calendar begin, Calendar end) {
				this.calendarBegin = begin;
				this.calendarEnd = end;
				return this;
			}

			@Override
			public void run() {
				if (NetUtil.checkNet(getApplication())) {

					infos = new GarbagePutRecordQueryEngineImpl()
							.queryGarbagePutRecordInfo(
									sp.getString("YHZH", ""), calendarBegin,
									calendarEnd);
					if (infos == null) {
						if (GarbagePutRecordQueryEngineImpl.isSucceed)
							updateUI(UIMode.UI_OnlyTip, false, false, null);
						else {
							updateUI(UIMode.UI_OnlyRetry, false, true,
									GarbagePutRecordQueryEngineImpl.errorMsg);
						}
						return;
					}
					updateUI(UIMode.UI_1, true, false, null);
					return;
				} else {
					updateUI(UIMode.UI_OnlyRetry, false, false, null);
					showNetWorkLost();
				}

			};
		}.setDate(calendarBegin, calendarEnd).start();
	}

	/**
	 * 更新UI界面
	 * @param mode UI模式
	 * @param updateData 指示是否更新数据
	 * @param showTip 指示是否显示提示
	 * @param tipStr 提示文本
	 */
	private void updateUI(UIMode mode, boolean updateData, boolean showTip,
			String tipStr) {
		runOnUiThread(new Runnable() {
			private UIMode mode;
			private boolean showTip;
			private String tipStr;
			private boolean updateData;

			public void run() {
				setUIMode(mode, updateData);
				if (showTip)
					MyToast.ShowMyToast(getApplicationContext(), tipStr);
			}

			public Runnable setData(UIMode mode, boolean updateData,
					boolean showTip, String tipStr) {
				this.mode = mode;
				this.updateData = updateData;
				this.showTip = showTip;
				this.tipStr = tipStr;
				return this;
			}
		}.setData(mode, updateData, showTip, tipStr));
	}
	/**在UI线程里面显示网络错误信息
	 */
	private void showNetWorkLost() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				PromptManager.showNoNetWork(GarbagePutRecordActivity.this);
			}
		});
	}
	/**
	 * 显示开始日期对话框
	 */
	private void showStartDateDialog() {
		DatePickerDialog date_dialog = new DatePickerDialog(
				GarbagePutRecordActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						btn_starttime.setText(year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth);
						calendarBegin.set(Calendar.YEAR, year);
						calendarBegin.set(Calendar.MONTH, monthOfYear);
						calendarBegin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					}
				}, calendarBegin.get(Calendar.YEAR), calendarBegin.get(Calendar.MONTH),
				calendarBegin.get(Calendar.DAY_OF_MONTH));

		date_dialog.show();

	}
	/**
	 * 显示结束日期对话框
	 */
	private void showFinishDateDialog() {
		DatePickerDialog date_dialog = new DatePickerDialog(
				GarbagePutRecordActivity.this, new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						btn_finishtime.setText(year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth);
						calendarEnd.set(Calendar.YEAR, year);
						calendarEnd.set(Calendar.MONTH, monthOfYear);
						calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);
					}
				}, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
				calendarEnd.get(Calendar.DAY_OF_MONTH));

		date_dialog.show();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_starttime:
			showStartDateDialog();
			break;
		case R.id.btn_finishtime:
			showFinishDateDialog();
			break;
		default:
			break;
		}
	}

}

