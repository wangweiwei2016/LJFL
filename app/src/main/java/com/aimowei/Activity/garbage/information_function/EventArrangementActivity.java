package com.aimowei.Activity.garbage.information_function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.engine.EventArrangementEngine;
import com.aimowei.Activity.garbage.engine.OnRefreshListener;
import com.aimowei.Activity.garbage.engine.QueryShareEngine.UIMode;
import com.aimowei.Activity.garbage.engine.impl.EventArrangementEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.ui.RefreshListView;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
/**
 * 活动查询Activity--信息查询的主要注释将分布于这里,其他请参考这个类
 * @author SWZ 1739530597@qq.com 
 */
public class EventArrangementActivity extends BaseActivity {
	
	/**
	 * 主要内容ListView的Adapter
	 */
	class ExAdapter extends BaseAdapter {

		
		public final class ViewHolder {
			/**
			 * 详情按钮
			 */
			public Button btn_detail;
			/**
			 * 时间
			 */
			public TextView time;
			/**
			 * 主题
			 */
			public TextView title;
			/**
			 * 类型
			 */
			public TextView type;
		}
		/**
		 * 数据
		 */
		private ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();

		private LayoutInflater mInflater;

		public ExAdapter(Context context) {

			this.mInflater = LayoutInflater.from(context);

		}

		@Override
		public int getCount() {
			if (this.mData != null)
				return this.mData.size();
			return 0;
		}

		public ArrayList<Map<String, Object>> getData() {
			return this.mData;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			View returnView = null;
			if (convertView == null) {//重新构建
				holder = new ViewHolder();
				returnView = mInflater.inflate(
						R.layout.item_event_arrangment_listview_main, null);
				holder.time = (TextView) returnView
						.findViewById(R.id.textView_time);
				holder.type = (TextView) returnView
						.findViewById(R.id.textView_type);
				holder.title = (TextView) returnView
						.findViewById(R.id.textView_title);
				holder.btn_detail = (Button) returnView
						.findViewById(R.id.button_detail);
				holder.btn_detail.setFocusable(false);//去除焦点

				returnView.setTag(holder);
			} else {
				returnView = convertView;
				holder = (ViewHolder) returnView.getTag();
			}
			//设置值,我尝试过将下面使代码初始化时就只执行一遍,但是发生了数据错位
			holder.time.setText(getFitString((String) this.mData.get(position)
					.get("time")));
			holder.type.setText(getFitString((String) this.mData.get(position)
					.get("type")));
			holder.title.setText(getFitString((String) this.mData.get(position)
					.get("title")));
			holder.btn_detail.setTag(this.mData.get(position).get("id"));
			holder.btn_detail.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String id = (String) v.getTag();
					showActivityDetail(id);
				}
			});
			return returnView;
		}
	}

	/**
	 * 获取合适的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getFitString(String str) {
		if (str == null || str.equals("") || str.equals("null") || str.equals("NULL"))
			return "暂无信息";
		else
			return str;
	}

	/**
	 * 获取合适的字符串
	 * 
	 * @param str
	 * @return
	 */
	public static String getFitString(String str, String def) {
		if (str == null || str == "" || str.equals("null") || str.equals("NULL"))
			return def;
		else
			return str;
	}
	/**
	 * 活动信息Model链表
	 */
	private LinkedList<EventArrangementEngine.EventArrangementModel> activitys = null;
	/**返回按钮*/
	private Button button_back = null;
	/** 重试按钮 */
	private Button button_retry = null;
	/**
	 * 活动详情字符串
	 */
	private String detailStr = null;
	/**
	 * 指示是否正在查看活动详情的标志
	 */
	private boolean isExploringDetail = false;
	/** 主要的ListView */
	private RefreshListView listView_showmain = null;
	/**
	 * 主要的ListView的Adapter
	 */
	ExAdapter listViewAdapter = null;
	/**
	 * 接下去的索引
	 */
	int nextIndex = 0;
	/**
	 * 每次加载数
	 */
	int pageLength = 5;

	/** 加载进度条 */
	private ProgressBar progressBar_loading = null;

	/** 显示活动详情的ScrollView */
	private ScrollView scrollView_detailtext = null;

	/**
	 * 选择的社区代码
	 */
	private int selectedSpinnerPosition = -1;

	/** 社区标签 */
	private Spinner spinner_community = null;
	/** 显示活动详情的TextView */
	private TextView textView_detailtext = null;
	/**
	 * 提示文本
	 */
	private TextView textView_tip = null;
	/**
	 * 显示受控制的view集合
	 */
	private LinkedList<View> views = new LinkedList<View>();
	/**
	 * 公共的初始化-在信息查询中这个函数可能被复制好几份而仅有少数的改动
	 */
	private void commonInit() {
		//从XML初始化
		progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
		button_retry = (Button) findViewById(R.id.button_retry);
		spinner_community = (Spinner) findViewById(R.id.spinner_community);
		textView_tip = (TextView) findViewById(R.id.textView_tip);
		//加入显示受控的View集合
		views.add(progressBar_loading);
		views.add(button_retry);
		views.add(spinner_community);
		views.add(textView_tip);
		//设置社区标签的功能
		spinner_community
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					private boolean first = true;

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (first) {//因为初始化时会被调用,所以跳过
							first = false;
						} else {
							selectedSpinnerPosition = arg2;//arg2是选中的索引
							updateInfo();
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		//设置重试按钮操作
		button_retry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				updateInfo();
			}
		});
		//初始化提示文本
		textView_tip.setText("当天没有活动哦");
		//设置社区标签状态
		setSpinnerStatus();
	}

	@Override
	public void onBackPressed() {
		if (isExploringDetail) {
			isExploringDetail = false;
			setUIMode(UIMode.UI_1, false);
		} else
			super.onBackPressed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_arrangement);
		settitle("活动查询");
		//公共初始化
		commonInit();
		//主要ListView初始化
		listView_showmain = (RefreshListView) findViewById(R.id.listView_main);
		//设置刷新监听
		listView_showmain.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onDownPullRefresh() {
				new Thread() {
					@Override
					public void run() {
						updateActivityData(false);//联网更新活动数据,指定不叠加(清空原来)
						EventArrangementActivity.this
								.runOnUiThread(new Runnable() {
									public void run() {
										// 控制头布局隐藏
										listView_showmain.hideHeaderView();
									}
								});
					}
				}.start();
			}
			
			@Override
			public void onLoadingMore() {
				new Thread() {
					@Override
					public void run() {
						updateActivityData(true);
						EventArrangementActivity.this
								.runOnUiThread(new Runnable() {
									public void run() {
										// 控制脚布局隐藏
										listView_showmain.hideFooterView();
									}
								});
					}
				}.start();
			}
		});
		//初始化活动详情查看返回按钮
		button_back =(Button)findViewById(R.id.button_back);
		button_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
		listViewAdapter = new ExAdapter(EventArrangementActivity.this);
		listView_showmain.setAdapter(listViewAdapter);
		textView_detailtext = (TextView) findViewById(R.id.textView_detail);
		
		scrollView_detailtext = (ScrollView)findViewById(R.id.scrollView_detail);
		//显示受控
		views.add(button_back);
		views.add(listView_showmain);
		views.add(scrollView_detailtext);
		//初始化更新信息
		updateInfo();
	}
	/**
	 * 设定社区标签状态(根据用户类别)--可用否?只有管理员登录时才能查看不同社区的情况
	 */
	private void setSpinnerStatus() {
		String tmp = sp.getString("yonghuleibie", null);
		if (tmp == null || tmp.isEmpty()) {
			spinner_community.setEnabled(true);
		} else {
			if (tmp.equals("0")) {
				spinner_community.setEnabled(true);
			} else if (tmp.equals("1")|| tmp.equals("3")) {
				spinner_community.setEnabled(false);
			} else if (tmp.equals("2")) {
				spinner_community.setEnabled(false);
			}
		}
	}
	/**
	 * 设定UI模式,同时可指定是否更新数据--这种方式有冗余,但是要修改的代价颇大
	 * @param mode UI模式 目前使用共享的ui模式,建议未来改进可以改用这个activity私有的ui模式
	 * @param updateUIData 是否更新数据(用存储的变量更新ui控件内容)
	 */
	private void setUIMode(UIMode mode, boolean updateUIData) {
		LinkedList<View> tmp = null;
		//Log.e(this.getClass().toString() + ":UIMode=", mode.toString());
		switch (mode) {
		case UI_1:
			if (updateUIData) {
				updateCommunitySpinner();
				updateActivityList(false);
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(listView_showmain);
			setViewVisible(tmp);
			break;
		case UI_RetryCommunity:
			if (updateUIData) {
				updateCommunitySpinner();
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(button_retry);
			setViewVisible(tmp);
			break;
		case UI_2:
			if (updateUIData) {
				updateActivityDetail();
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(scrollView_detailtext);
			tmp.add(button_back);
			setViewVisible(tmp);
			break;
		case UI_3:
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(textView_tip);
			textView_tip.setText("活动详情为空..??");
			setViewVisible(tmp);
			break;
		case UI_Loading:
			setViewVisible(progressBar_loading);
			break;
		case UI_OnlyCommunity:
			if (updateUIData) {
				updateCommunitySpinner();
			}
			setViewVisible(spinner_community);
			break;
		case UI_OnlyRetry:
			setViewVisible(button_retry);
			break;
		case UI_OnlyTip:
			setViewVisible(textView_tip);
			break;
		case UI_TipCommunity:
			if (updateUIData) {
				updateCommunitySpinner();
				textView_tip.setText("今天"
						+ spinner_community.getSelectedItem().toString()
						+ "没有活动哦");
			}
			tmp = new LinkedList<View>();
			tmp.add(textView_tip);
			tmp.add(spinner_community);
			setViewVisible(tmp);
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
	 * 显示活动详情
	 * @param activityID
	 */
	private void showActivityDetail(String activityID) {
		isExploringDetail = true;
		setUIMode(UIMode.UI_Loading, false);

		new Thread() {
			/*缓存的活动id*/
			private String activityID = null;

			@Override
			public void run() {
				if (NetUtil.checkNet(getApplication())) {
					detailStr = new EventArrangementEngineImpl()
							.queryActivityDetail(this.activityID);
					if (detailStr == null) {

						if (EventArrangementEngineImpl.isSucceed)
							updateUI(UIMode.UI_3, false, false, null);
						else {
							updateUI(UIMode.UI_1, false, true,
									EventArrangementEngineImpl.errorMsg);
							isExploringDetail = false;
						}

						return;
					}
					updateUI(UIMode.UI_2, true, false, null);
				} else {
					isExploringDetail = false;
					updateUI(UIMode.UI_1, false, false, null);
					showNetWorkLost();

				}

			}

			public Thread setActivityID(String activityID) {
				this.activityID = activityID;
				return this;
			};
		}.setActivityID(activityID).start();
	}

	/**
	 * 在UI线程里面显示网络错误信息
	 */
	private void showNetWorkLost() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				PromptManager.showNoNetWork(EventArrangementActivity.this);
			}
		});
	}
	/**在UI线程里面显示提示信息
	 */
	private void showToastOnUIThread(String tip) {
		runOnUiThread(new Runnable() {
			public String tip = "";

			@Override
			public void run() {
				MyToast.ShowMyToast(EventArrangementActivity.this, this.tip);
			}

			public Runnable setTip(String tip) {
				this.tip = tip;
				return this;
			}
		}.setTip(tip));
	}

	/**
	 * 联网更新活动列表数据,可以指定是否叠加
	 * @param append 叠加标志
	 */
	private void updateActivityData(boolean append) {
		if(!append)
			nextIndex = 0;
		if (NetUtil.checkNet(getApplication())) {
			activitys = new EventArrangementEngineImpl()
					.queryActivityList(EventArrangementEngineImpl
							.getSQDM(selectedSpinnerPosition), nextIndex,
							pageLength);
			if (activitys == null || activitys.isEmpty()) {
				if (EventArrangementEngineImpl.isSucceed)
					showToastOnUIThread("已加载全部");
				else {
					updateUI(UIMode.UI_1, false, false, null);
					showToastOnUIThread(EventArrangementEngineImpl.errorMsg);
				}
			} else {
				runOnUiThread(new Runnable() {
					boolean append ;
					@Override
					public void run() {
						updateActivityList(this.append);
					}
					public Runnable setValue(boolean append)
					{
						this.append =append;
						return this;
					}
				}.setValue(append));

			}
		} else {
			updateUI(UIMode.UI_RetryCommunity, false, false, null);
			showNetWorkLost();
		}
	}

	/**
	 * 更新活动详情
	 */
	private void updateActivityDetail() {
		if (detailStr != null)
			textView_detailtext.setText(getFitString(detailStr));
		else {
			setUIMode(UIMode.UI_1, false);
			MyToast.ShowMyToast(getApplicationContext(), "活动详情数据赋值给UI控件失败");
		}
	}

	/**
	 * 更新活动列表--将内存中存储的Model链表显示到UI控件上
	 */
	private void updateActivityList(boolean append) {
		if (activitys == null || activitys.isEmpty()) {
			setUIMode(UIMode.UI_RetryCommunity, false);
			//Log.e("SWZ-严重Error:", "再没更新成功下调用了刷新UI控件数据函数!!");
		} else {
			listViewAdapter.notifyDataSetInvalidated();
			ArrayList<Map<String, Object>> mData = listViewAdapter.getData();
			if(!append)
			{
				mData.clear();
				nextIndex = 0;
			}
			Map<String, Object> map = null;
			for (EventArrangementEngine.EventArrangementModel model : activitys) {
				map = new HashMap<String, Object>();
				map.put("time", model.startTime + " - " + model.endTime);
				map.put("type", model.type);
				map.put("title", model.title);
				map.put("id", model.id);
				mData.add(map);
			}
			nextIndex += mData.size();
			listViewAdapter.notifyDataSetChanged();

		}
	}

	/**
	 * 更新社区标签--在管理员登录时可以切换社区查看信息
	 * <br>在活动查询,排行榜,垃圾投放中都有社区标签
	 */
	private void updateCommunitySpinner() {
		if (!EventArrangementEngineImpl.updateSpinnerAdapter(this,
				spinner_community)) {
			setUIMode(UIMode.UI_OnlyRetry, false);
			MyToast.ShowMyToast(getApplicationContext(), "社区数据赋值给UI控件失败");
			return;
		}
		EventArrangementEngineImpl.setSelectedSpinnerItem(
				selectedSpinnerPosition, spinner_community);
	}

	/**
	 * 更新所有信息
	 */
	private void updateInfo() {
		setUIMode(UIMode.UI_Loading, false);

		new Thread() {

			@Override
			public void run() {
				if (NetUtil.checkNet(getApplication())) {
					if (!EventArrangementEngineImpl.updateCommutityInfo(sp
							.getString("YHZH", ""))) {
						updateUI(UIMode.UI_OnlyRetry, false, true,
								EventArrangementEngineImpl.errorMsg);
						return;
					}
					if (selectedSpinnerPosition < 0) {
						selectedSpinnerPosition = EventArrangementEngineImpl
								.getSelectedSpinnerItemPosition(sp.getString(
										"SQDM", null));
					}
					//Log.e("SWZ-updateInfo", "SQDM=" + selectedSpinnerPosition);
					
					nextIndex = 0;
					activitys = new EventArrangementEngineImpl()
							.queryActivityList(EventArrangementEngineImpl
									.getSQDM(selectedSpinnerPosition),
									nextIndex, pageLength);
					if (activitys == null || activitys.isEmpty()) {
						if (EventArrangementEngineImpl.isSucceed)
							updateUI(UIMode.UI_TipCommunity, true, false, null);
						else
							updateUI(UIMode.UI_RetryCommunity, true, true,
									EventArrangementEngineImpl.errorMsg);
						return;
					}
					updateUI(UIMode.UI_1, true, false, null);
				} else {
					updateUI(UIMode.UI_OnlyRetry, false, false, null);
					showNetWorkLost();
				}
			}
		}.start();
	}

	/**
	 *  更新UI界面
	 * @param mode UI模式
	 * @param updateData 是否更新数据
	 * @param showTip 是否显示提示Toast
	 * @param tipStr 提示字符串
	 */
	private void updateUI(UIMode mode, boolean updateData, boolean showTip,
			String tipStr) {
		runOnUiThread(new Runnable() {
			/**
			 * 暂时存储UI模式
			 */
			private UIMode mode;
			/**
			 * 暂时存储是否显示提示
			 */
			private boolean showTip;
			/**
			 * 暂时存储提示字符串
			 */
			private String tipStr;
			/**
			 * 暂时存储是否更新数据
			 */
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
