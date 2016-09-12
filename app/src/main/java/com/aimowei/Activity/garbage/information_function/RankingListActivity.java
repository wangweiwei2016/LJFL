package com.aimowei.Activity.garbage.information_function;

import java.util.LinkedList;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.engine.QueryShareEngine.UIMode;
import com.aimowei.Activity.garbage.engine.RankingListEngine.RankingModel;
import com.aimowei.Activity.garbage.engine.RankingListEngine.SelfRankingModel;
import com.aimowei.Activity.garbage.engine.impl.EventArrangementEngineImpl;
import com.aimowei.Activity.garbage.engine.impl.RankingListEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
/**
 * 排行榜查询Activity
 * @author SWZ
 */
public class RankingListActivity extends BaseActivity {
	/** 重试按钮 */
	private Button button_retry = null;
	/**
	 * 排行榜数据
	 */
	private LinkedList<RankingModel> infos = null;
	/**
	 * 我的积分标签
	 */
	TextView label_myintegral = null;
	/**
	 * 我的投放量标签
	 */
	TextView label_myputquantity = null;
	/**
	 * 我的排名标签
	 */
	TextView label_myranking = null;
	/**
	 * 我的排行Model
	 */
	private SelfRankingModel myRanking = null;
	/** 加载进度条 */
	private ProgressBar progressBar_loading = null;

	/**
	 * 选择的社区代码
	 */
	private int selectedSpinnerPosition = -1;
	/** 社区标签 */
	private Spinner spinner_community = null;
	/**
	 * 排行榜列表用到的表格布局
	 */
	private TableLayout tableLayout_main = null;
	/**
	 * 我的积分
	 */
	private TextView textView_myintegral = null;
	/**
	 * 我的投放量
	 */
	private TextView textView_myputquantity = null;
	/**
	 * 我的排名
	 */
	private TextView textView_myranking = null;
	/**
	 * 提示文本
	 */
	private TextView textView_tip = null;
	/**
	 * 显示受控的View的集合
	 */
	private LinkedList<View> views = new LinkedList<View>();

	/**
	 * 公共的初始化
	 */
	private void commonInit() {
		progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
		button_retry = (Button) findViewById(R.id.button_retry);
		spinner_community = (Spinner) findViewById(R.id.spinner_community);
		textView_tip = (TextView) findViewById(R.id.textView_tip);

		views.add(progressBar_loading);
		views.add(button_retry);
		views.add(spinner_community);
		views.add(textView_tip);

		spinner_community
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					private boolean first = true;

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (first) {
							first = false;
						} else {
							selectedSpinnerPosition = arg2;
							updateInfo();
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		button_retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateInfo();
			}
		});

		textView_tip.setText("今天没有活动哦");
		updateWithYHLB();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking_list);
		settitle("排行榜查询");

		commonInit();

		textView_myranking = (TextView) this
				.findViewById(R.id.textView_myranking);
		textView_myintegral = (TextView) this
				.findViewById(R.id.textView_myintegral);
		textView_myputquantity = (TextView) this
				.findViewById(R.id.textView_myputquantity);
		label_myranking = (TextView) this.findViewById(R.id.label_myranking);
		label_myintegral = (TextView) this.findViewById(R.id.label_myintegral);
		label_myputquantity = (TextView) this
				.findViewById(R.id.label_myputquantity);
		
		tableLayout_main = (TableLayout) this
				.findViewById(R.id.tableLayout_main);
		tableLayout_main.setStretchAllColumns(true);
		views.add(tableLayout_main);
		updateInfo();
	}
	/**
	 * 根据用户类别修改控件显示方式
	 */
	private void updateWithYHLB() {
		String tmp = sp.getString("yonghuleibie", null);
		ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView_tablayout);
		if (tmp == null || tmp.isEmpty()) {
			spinner_community.setEnabled(true);
			scrollView.getLayoutParams().height =  ScrollView.LayoutParams.MATCH_PARENT;
		} else {
			if (tmp.equals("0")) {//管理员
				spinner_community.setEnabled(true);
				scrollView.getLayoutParams().height =  ScrollView.LayoutParams.MATCH_PARENT;
			} else if (tmp.equals("1")|| tmp.equals("3")) {//社区用户/商户
				spinner_community.setEnabled(false);
			} else if (tmp.equals("2")) {
				spinner_community.setEnabled(false);
			}
		}		
	}
	/**
	 * 设置我的排名部分的显示状态
	 * @param visibility
	 */
	private void setTextViewVisibility(int visibility) {
		textView_myranking.setVisibility(visibility);
		textView_myintegral.setVisibility(visibility);
		textView_myputquantity.setVisibility(visibility);

		label_myranking.setVisibility(visibility);
		label_myintegral.setVisibility(visibility);
		label_myputquantity.setVisibility(visibility);

	}
	/**
	 * 设定UI模式,同时可指定是否更新数据
	 * @param mode
	 * @param updateUIData
	 */
	private void setUIMode(UIMode mode, boolean updateUIData) {
		LinkedList<View> tmp = null;
		Log.e(this.getClass().toString() + ":UIMode=", mode.toString());
		switch (mode) {
		case UI_1://显示社区标签
			if (updateUIData) {
				updateCommunitySpinner();
				updateRankingList();
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(tableLayout_main);
			setViewVisible(tmp);
			setTextViewVisibility(View.GONE);
			break;
		case UI_2:
			if (updateUIData) {
				updateCommunitySpinner();
				updateRankingList();
				updateMyRanking();
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(tableLayout_main);
			setViewVisible(tmp);
			setTextViewVisibility(View.VISIBLE);
			break;
		case UI_3:
			if (updateUIData) {
				updateCommunitySpinner();
				updateMyRanking();
			}
			setViewVisible(spinner_community);
			setTextViewVisibility(View.VISIBLE);
			break;
		case UI_4:
			if (updateUIData) {
				updateCommunitySpinner();
				updateRankingList();
				textView_tip.setText("奇怪!找不到你的个人排名信息");
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(tableLayout_main);
			tmp.add(textView_tip);

			setViewVisible(tmp);
			setTextViewVisibility(View.GONE);
			break;
		case UI_RetryCommunity:
			if (updateUIData) {
				updateCommunitySpinner();
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(button_retry);
			setViewVisible(tmp);
			setTextViewVisibility(View.GONE);
			break;
		case UI_Loading:
			setViewVisible(progressBar_loading);
			setTextViewVisibility(View.GONE);
			break;
		case UI_OnlyCommunity:
			if (updateUIData) {
				updateCommunitySpinner();
			}
			setViewVisible(spinner_community);
			setTextViewVisibility(View.GONE);
			break;
		case UI_OnlyRetry:
			setViewVisible(button_retry);
			setTextViewVisibility(View.GONE);
			break;
		case UI_OnlyTip:
			setViewVisible(textView_tip);
			setTextViewVisibility(View.GONE);
			break;
		case UI_TipCommunity:
			if (updateUIData) {
				updateCommunitySpinner();
				textView_tip.setText(spinner_community.getSelectedItem()
						.toString() + "没有人投放垃圾");
			}
			tmp = new LinkedList<View>();
			tmp.add(textView_tip);
			tmp.add(spinner_community);
			setViewVisible(tmp);
			setTextViewVisibility(View.GONE);
			break;
		default:
			MyToast.ShowMyToast(getApplicationContext(), "发现未知UI模式!!");
			break;
		}
	}

	/**
	 * 让指定列表里面的View显示
	 * @param visibleViews
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
	 * 更新社区标签
	 */
	private void updateCommunitySpinner() {
		if (!EventArrangementEngineImpl.updateSpinnerAdapter(this,
				spinner_community)) {
			setUIMode(UIMode.UI_OnlyRetry, false);
			return;
		}
		EventArrangementEngineImpl.setSelectedSpinnerItem(
				selectedSpinnerPosition, spinner_community);
	}

	private void updateInfo() {
		setUIMode(UIMode.UI_Loading, false);

		new Thread() {

			@Override
			public void run() {
				if (NetUtil.checkNet(getApplication())) {
					if (!EventArrangementEngineImpl.updateCommutityInfo(sp
							.getString("YHZH", ""))) {//获取社区信息失败
						updateUI(UIMode.UI_OnlyRetry, false, true,
								EventArrangementEngineImpl.errorMsg);//只显示错误信息和重试按钮
						return;
					}
					if (selectedSpinnerPosition < 0) {//
						selectedSpinnerPosition = EventArrangementEngineImpl
								.getSelectedSpinnerItemPosition(sp.getString(
										"SQDM", null));
					}

					infos = new RankingListEngineImpl().queryRankingList(sp
							.getString("YHZH", ""),  EventArrangementEngineImpl
							.getSQDM(selectedSpinnerPosition), "1");
					boolean displayList = true;
					if (infos == null) {
						if (RankingListEngineImpl.isSucceed)
							displayList = false;
						else {
							updateUI(UIMode.UI_RetryCommunity, true, true,
									RankingListEngineImpl.errorMsg);
							return;
						}
					}
					String tmp = sp.getString("yonghuleibie", null);
					if (tmp == null || tmp.equals("0") || tmp.equals("2")) {
						if (displayList) {
							updateUI(UIMode.UI_1, true, false, null);
						} else
							updateUI(UIMode.UI_TipCommunity, true, false, null);
						return;
					}
					myRanking = new RankingListEngineImpl().queryMyRanking(
							sp.getString("SQYHMC", ""), "1");
					if (myRanking == null) {
						if (RankingListEngineImpl.isSucceed) {
							updateUI(UIMode.UI_4, true, false, null);
						} else {
							updateUI(UIMode.UI_RetryCommunity, true, true,
									RankingListEngineImpl.errorMsg);
						}
						return;
					}
					updateUI(UIMode.UI_2, true, false, null);
					return;
				} else {
					updateUI(UIMode.UI_OnlyRetry, false,false,null);
					showNetWorkLost() ;
				}

			};
		}.start();
	}
	/**
	 * 在UI线程显示网络错误对话框
	 */
	private void showNetWorkLost() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				PromptManager.showNoNetWork(RankingListActivity.this);
			}
		});
	}	
	/**
	 * 将数据填充到UI控件中
	 */
	private void updateMyRanking() {
		if (myRanking != null) {
			textView_myranking.setText(EventArrangementActivity
					.getFitString(myRanking.ranking));
			String tmp = EventArrangementActivity.getFitString(
					myRanking.integral, null);
			textView_myintegral.setText(tmp == null ? "暂无信息" : tmp + " 分");
			tmp = EventArrangementActivity.getFitString(myRanking.putquantity,
					null);
			textView_myputquantity.setText(tmp == null ? "暂无信息" : tmp + " Kg");
		} else
			setUIMode(UIMode.UI_2, false);
	}
	
	/**
	 * 更新社区标签
	 */
	private void updateRankingList() {
		tableLayout_main.removeViews(1, tableLayout_main.getChildCount() - 1);
		boolean isDual = false;
		if (infos != null && !infos.isEmpty()) {
			for (RankingModel model : infos) {

				RelativeLayout view = (RelativeLayout) this.getLayoutInflater()
						.inflate(R.layout.activity_ranking_list, null);
				TableRow tableRow = (TableRow) view
						.findViewById(R.id.tableRow_main);
				TableLayout tableLayout = (TableLayout) view
						.findViewById(R.id.tableLayout_main);
				tableLayout.removeAllViews();
				TextView textView_ranking = (TextView) tableRow
						.findViewById(R.id.textView_sqranking);
				TextView textView_integral = (TextView) tableRow
						.findViewById(R.id.textView_sqIntegral);
				TextView textView_name = (TextView) tableRow
						.findViewById(R.id.textView_sqyhname);
				
				textView_ranking.setText(EventArrangementActivity
						.getFitString(model.ranking));
				textView_integral.setText(EventArrangementActivity
						.getFitString(model.integral));
				textView_name.setText(EventArrangementActivity
						.getFitString(model.name));
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
	 * 更新UI界面
	 * @param mode ui模式
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
}
