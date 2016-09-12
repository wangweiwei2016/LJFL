package com.aimowei.Activity.garbage.information_function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.engine.GarbagePutQueryEngine.GarbagePutInfoModel;
import com.aimowei.Activity.garbage.engine.OnRefreshListener;
import com.aimowei.Activity.garbage.engine.QueryShareEngine.UIMode;
import com.aimowei.Activity.garbage.engine.impl.EventArrangementEngineImpl;
import com.aimowei.Activity.garbage.engine.impl.GarbagePutQueryEngineImpl;
import com.aimowei.Activity.garbage.mycenter_function.SelectDateActivity;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.ui.RefreshListView;
import com.aimowei.Activity.garbage.ui.ZoomImageView;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
/**
 * 垃圾投放查询Activity
 * @author SWZ
 */
public class GarbagePutQueryActivity extends BaseActivity {
	/**
	 * 主要内容ListView的Adapter
	 */
	public class ExAdapter extends BaseAdapter {
		public final class ViewHolder {
			/**
			 * 地址
			 */
			public TextView address;
			/**
			 * 图示按钮
			 */
			public Button btn_detailpic;
			/**
			 * 管理员
			 */
			public TextView manager;
			/**
			 * 时间
			 */
			public TextView time;
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
						R.layout.item_garbage_put_listview_main, null);
				holder.time = (TextView) returnView
						.findViewById(R.id.textView_putTime);
				holder.address = (TextView) returnView
						.findViewById(R.id.textView_putAddress);
				holder.manager = (TextView) returnView
						.findViewById(R.id.textView_putManager);
				holder.btn_detailpic = (Button) returnView
						.findViewById(R.id.button_detailPic);
				holder.btn_detailpic.setFocusable(false);
				returnView.setTag(holder);
			} else {
				returnView = convertView;
				holder = (ViewHolder) returnView.getTag();
			}
			//设置值
			holder.time
					.setText(EventArrangementActivity
							.getFitString((String) this.mData.get(position)
									.get("time")));
			holder.address.setText(EventArrangementActivity
					.getFitString((String) this.mData.get(position).get(
							"address")));
			holder.manager.setText(EventArrangementActivity
					.getFitString((String) this.mData.get(position).get(
							"manager")));
			holder.btn_detailpic.setTag(this.mData.get(position).get("pic"));
			holder.btn_detailpic.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String url = (String) v.getTag();
					showDetailPic(url);
				}
			});
			
			return returnView;
		}
	}



	/** 显示当前期的按钮 */
	private Button button_date = null;

	/** 上一期 */
	private Button button_lastDay = null;
	/** 下一期 */
	private Button button_nextDay = null;

	/** 重试按钮 */
	private Button button_retry = null;
	/**选择的日期*/
	Calendar currentChooseDate = null;
	/**支持图片缩放的ImageView*/
	private ZoomImageView imageView_showDetail = null;
	/**
	 *投机投放信息Model链表
	 */
	LinkedList<GarbagePutInfoModel> infos = new LinkedList<GarbagePutInfoModel>();
	/**
	 * 指示是否已经加载了社区信息的标志
	 */
	boolean isLoadedCommutity = false;
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

	/**
	 * 选择的社区代码
	 */
	private int selectedSpinnerPosition = -1;

	/** 社区标签 */
	private Spinner spinner_community = null;
	/**
	 * 提示文本
	 */
	private TextView textView_tip = null;
	/**
	 * 显示受控制的view集合
	 */
	private LinkedList<View> views = new LinkedList<View>();

	/**
	 * 公共的初始化
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 得到SelectDateActivity关闭后返回的期数据
		// resultCode为回传的标记，在BSelectDateActivity中回传的是RESULT_OK
		switch (resultCode) {
		case RESULT_OK:
			String result = data.getExtras().getString("result");
			if (!result.equals("")) {
				String[] numbers = result.split("-", 3);
				int year = Integer.valueOf(numbers[0]);
				int month = Integer.valueOf(numbers[1]);
				int day = Integer.valueOf(numbers[2]);
				currentChooseDate.set(Calendar.YEAR, year);
				currentChooseDate.set(Calendar.MONTH, month - 1);
				currentChooseDate.set(Calendar.DAY_OF_MONTH, day);
				button_date.setText(DateFormat.format("yyyy-MM-dd",
						currentChooseDate.getTime()).toString());
				updateInfo();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		if (view_detailpicback.getVisibility() == View.GONE)
			super.onBackPressed();
		else {
			view_detailpicback.setVisibility(View.GONE);
		}

	}
	/**
	 * 图示界面返回按钮
	 */
	private View view_detailpicback = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_garbage_put);
		settitle("垃圾投放查询");
		commonInit();
		listView_showmain = (RefreshListView) findViewById(R.id.listView_showmain);
		listView_showmain.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onDownPullRefresh() {
				new Thread() {
					@Override
					public void run() {
						updatePutInfosData(false);
						GarbagePutQueryActivity.this
								.runOnUiThread(new Runnable() {
									public void run() {
										// 控制脚布局隐藏
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
						updatePutInfosData(true);
						GarbagePutQueryActivity.this
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
		listViewAdapter = new ExAdapter(GarbagePutQueryActivity.this);
		listView_showmain.setAdapter(listViewAdapter);
		button_lastDay = (Button) findViewById(R.id.button_lastday);
		button_nextDay = (Button) findViewById(R.id.button_nextday);
		button_date = (Button) findViewById(R.id.button_date);
		button_lastDay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				currentChooseDate.add(Calendar.DATE, -1);
				button_date.setText(DateFormat.format("yyyy-MM-dd",
						currentChooseDate.getTime()).toString());
				updateInfo();
			}
		});
		button_nextDay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				currentChooseDate.add(Calendar.DATE, 1);
				button_date.setText(DateFormat.format("yyyy-MM-dd",
						currentChooseDate.getTime()).toString());
				updateInfo();
			}
		});
		button_date.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDateChooseDialog();
			}
		});
		imageView_showDetail = (ZoomImageView) findViewById(R.id.imageView_showdetail);
		view_detailpicback = findViewById(R.id.view_detailpicback);
		view_detailpicback.setVisibility(View.GONE);
		view_detailpicback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				arg0.setVisibility(View.GONE);
			}
		});
		//image_url =(TextView)findViewById(R.id.textView_url);
		/*Button button_backToNormalImage = (Button)findViewById(R.id.backToNormalImage);
		button_backToNormalImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				imageView_showDetail.backToNormal();
			}
		});*/
		Button button_back = (Button)findViewById(R.id.button_back);
		button_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				GarbagePutQueryActivity.this.onBackPressed();
			}
		});
		currentChooseDate = Calendar.getInstance();// 获取当前历对象
		button_date.setText(DateFormat.format("yyyy-MM-dd",
				currentChooseDate.getTime()).toString());
		views.add(listView_showmain);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_loading) // 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.icon_error) // 设置图片加载或解码过程中发生错误显示的图片
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
		.imageScaleType(ImageScaleType.NONE)
		.build(); // 构建完成
		
		updateInfo();
	}
	/**在UI线程里面显示网络错误信息
	 */
	private void showNetWorkLost() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				PromptManager.showNoNetWork(GarbagePutQueryActivity.this);
			}
		});
	}
	/**
	 * 在UI线程显示提示
	 * @param tip
	 */
	private void showToastOnUIThread(String tip) {
		runOnUiThread(new Runnable() {
			public String tip = "";

			@Override
			public void run() {
				MyToast.ShowMyToast(GarbagePutQueryActivity.this, this.tip);
			}

			public Runnable setTip(String tip) {
				this.tip = tip;
				return this;
			}
		}.setTip(tip));
	}
	/**
	 * 更新投放信息
	 * @param append 是否叠加
	 */
	private void updatePutInfosData(boolean append) {
		if (!append)
			nextIndex = 0;
		if (NetUtil.checkNet(getApplication())) {
			infos = new GarbagePutQueryEngineImpl()
					.queryGarbagePutInfo(EventArrangementEngineImpl
							.getSQDM(selectedSpinnerPosition),
							currentChooseDate, nextIndex, pageLength);
			if (infos == null || infos.isEmpty()) {
				if (GarbagePutQueryEngineImpl.isSucceed)
					showToastOnUIThread("已加载全部");
				else {
					updateUI(UIMode.UI_1, false, false, null);
					showToastOnUIThread(GarbagePutQueryEngineImpl.errorMsg);
				}
			} else {
				runOnUiThread(new Runnable() {
					boolean append;

					public Runnable setValue(boolean append) {
						this.append = append;
						return this;
					}

					@Override
					public void run() {
						updateGarbagePutInfo(this.append);
					}
				}.setValue(append));

			}
		} else {
			updateUI(UIMode.UI_RetryCommunity, false, false, null);
			showNetWorkLost();
		}
	}

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
	 * 设定UI模式,同时可指定是否更新数据
	 * @param mode
	 * @param updateUIData
	 */
	private void setUIMode(UIMode mode, boolean updateUIData) {
		LinkedList<View> tmp = null;
		Log.e(this.getClass().toString() + ":UIMode=", mode.toString());
		switch (mode) {
		case UI_1:
			if (updateUIData) {
				updateCommunitySpinner();
				updateGarbagePutInfo(false);
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(listView_showmain);
			setViewVisible(tmp);
			break;
		case UI_2:
			if (updateUIData) {
				updateCommunitySpinner();
			}
			tmp = new LinkedList<View>();
			tmp.add(spinner_community);
			tmp.add(progressBar_loading);
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
				textView_tip.setText("当天"
						+ spinner_community.getSelectedItem().toString()
						+ "没有投放点哦");
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

	private void showDateChooseDialog() {
		startActivityForResult(new Intent(GarbagePutQueryActivity.this,
				SelectDateActivity.class), 1);
	}
	//private TextView image_url = null;
	public void showDetailPic(String url) {
		//Log.i("SWZ1", url);
		url = ConstantValue.LOTTERY_URI1+"/BussinessManage/" + url;
		Log.i("SWZ", url);
		//image_url.setText(url);
		
		imageView_showDetail.reset();
		imageLoader.displayImage(url, imageView_showDetail,options);
		
		view_detailpicback.setVisibility(View.VISIBLE);
	}
	/**
	 * 更新社区标签
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
	 * 更新垃圾投放信息
	 * @param append 指示是否叠加
	 */
	private void updateGarbagePutInfo(boolean append) {
		if (infos == null || infos.isEmpty()) {
			setUIMode(UIMode.UI_RetryCommunity, false);
			Log.e("SWZ-严重Error:", "再没更新成功下调用了刷新UI控件数据函数!!");
		} else {
			listViewAdapter.notifyDataSetInvalidated();
			List<Map<String, Object>> mData = listViewAdapter.getData();
			if (!append)
			{
				mData.clear();
				nextIndex = 0;
			}
			Map<String, Object> map = null;
			for (GarbagePutInfoModel model : infos) {
				map = new HashMap<String, Object>();
				map.put("time", model.time);
				map.put("address", model.address);
				map.put("manager", model.manager);
				map.put("pic", model.pic);
				mData.add(map);
			}
			nextIndex += mData.size();
			listViewAdapter.notifyDataSetChanged();
		}
	}
	/**
	 * 联网更新信息
	 */
	private void updateInfo() {
		if (isLoadedCommutity)
			setUIMode(UIMode.UI_2, false);
		else
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
					isLoadedCommutity = true;
					if (selectedSpinnerPosition < 0) {
						selectedSpinnerPosition = EventArrangementEngineImpl
								.getSelectedSpinnerItemPosition(sp.getString(
										"SQDM", null));
					}
					nextIndex = 0;
					infos = new GarbagePutQueryEngineImpl()
							.queryGarbagePutInfo(EventArrangementEngineImpl
									.getSQDM(selectedSpinnerPosition),
									currentChooseDate, nextIndex, pageLength);
					if (infos == null) {
						if (GarbagePutQueryEngineImpl.isSucceed) {
							updateUI(UIMode.UI_TipCommunity, true, false, null);
						} else {
							updateUI(UIMode.UI_RetryCommunity, true, true,
									GarbagePutQueryEngineImpl.errorMsg);

						}
						return;
					}
					updateUI(UIMode.UI_1, true, false, null);
				} else {
					updateUI(UIMode.UI_OnlyRetry, false, false, null);
					showNetWorkLost();
				}
			};
		}.start();
	}

	/**
	 * 更新UI界面
	 * @param mode UI模式
	 * @param updateData  指示是否更新控件中数据
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
