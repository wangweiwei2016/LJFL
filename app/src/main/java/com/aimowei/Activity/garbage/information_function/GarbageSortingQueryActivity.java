package com.aimowei.Activity.garbage.information_function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.engine.GarbageSortingEngine;
import com.aimowei.Activity.garbage.engine.QueryShareEngine.UIMode;
import com.aimowei.Activity.garbage.engine.impl.GarbageSortingEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
 
/**
 * 垃圾分类查询Activity
 * @author SWZ
 */
public class GarbageSortingQueryActivity extends BaseActivity {
	
	/**
	 * 主要的Adapter
	 * @author Administrator
	 *
	 */
	 class ExAdapter extends BaseExpandableListAdapter {
		Activity parentActivity;
		List<TreeNode> treeNodes = null;
		/**
		 * 引用容器
		 * @author Administrator
		 *
		 */
		public final class GroupViewHolder {
			/**
			 * 图片标签
			 */
			public ImageView logo;
			/**
			 * 图片标志(是否展开)
			 */
			public ImageView flag;
			/**
			 * 文本
			 */
			public TextView text;
		}

		public final class ChildViewHolder {
			/**
			 * 整体表格布局
			 */
			public TableLayout table;
			/**
			 * 名称
			 */
			public TextView label_name;
			/**
			 * 积分
			 */
			public TextView label_integral;
			/**
			 * 第一行
			 */
			public TableRow firstRow;
		}

		public ExAdapter(Activity parentActivity) {
			this.parentActivity = parentActivity;
		}

		public Object getChild(int groupPosition, int childPosition) {
			if (treeNodes == null)
				return null;
			return treeNodes.get(groupPosition).list.get(childPosition)
					.toString();
		}

		public long getChildId(int groupPosition, int childPosition) {
			return groupPosition * 10000 + childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			if (treeNodes == null)
				return 0;
			return treeNodes.get(groupPosition).list.isEmpty() ? 0 : 1;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ChildViewHolder viewHolder = null;
			View returnView = null;
			if (convertView == null) {

				viewHolder = new ChildViewHolder();
				returnView = parentActivity.getLayoutInflater().inflate(
						R.layout.item_garbagesorting_item, null);
				viewHolder.table = (TableLayout) returnView
						.findViewById(R.id.tableLayout_main);
				viewHolder.firstRow = (TableRow) returnView
						.findViewById(R.id.tableRow_main);
				viewHolder.label_name = (TextView) returnView
						.findViewById(R.id.textView_name);
				viewHolder.label_integral = (TextView) returnView
						.findViewById(R.id.textView_integral);
				returnView.setTag(viewHolder);
			} else {
				returnView = convertView;
				viewHolder = (ChildViewHolder) convertView.getTag();
			}
			viewHolder.table.removeAllViews();
			viewHolder.table.addView(viewHolder.firstRow);

			TableLayout tableLayout = null;
			TableRow tableRow = null;
			TextView textView_name = null;
			TextView textView_integral = null;
			boolean isDual = false;
			for (Map<String, String> data : treeNodes.get(groupPosition).list) {
				View tmp = parentActivity.getLayoutInflater().inflate(
						R.layout.item_garbagesorting_item, null);
				tableLayout = (TableLayout) tmp
						.findViewById(R.id.tableLayout_main);
				tableRow = (TableRow) tableLayout
						.findViewById(R.id.tableRow_main);
				tableLayout.removeAllViews();
				textView_name = (TextView) tableRow
						.findViewById(R.id.textView_name);
				textView_integral = (TextView) tableRow
						.findViewById(R.id.textView_integral);

				textView_name.setText(EventArrangementActivity
						.getFitString(data.get("name")));
				textView_integral.setText(EventArrangementActivity
						.getFitString(data.get("integral")));
				if (isDual) {
					tableRow.setBackgroundColor(Color.rgb(0xf5, 0xf5, 0xf5));
				}
				isDual = !isDual;
				viewHolder.table.addView(tableRow);

			}
			return returnView;
		}

		public Object getGroup(int groupPosition) {
			if (treeNodes == null)
				return null;
			return groupPosition;
		}

		public int getGroupCount() {
			if (treeNodes == null)
				return 0;
			return treeNodes.size();
		}

		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
        /**图标资源id*/
		private int[] resids = new int[] { R.drawable.garbagetype0,
				R.drawable.garbagetype1, R.drawable.garbagetype2,
				R.drawable.garbagetype3 };

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) { // 不会选中丢失
			// Log.e("SWZ-getGroupView", "groupPosition=" + groupPosition);
			GroupViewHolder viewHolder = null;
			View returnView = null;
			if (convertView == null) {
				viewHolder = new GroupViewHolder();
				returnView = this.parentActivity.getLayoutInflater().inflate(
						R.layout.item_garbagesorting_group, null);
				viewHolder.logo = (ImageView) returnView
						.findViewById(R.id.imageView_label);

				viewHolder.text = (TextView) returnView
						.findViewById(R.id.textView_label);
				viewHolder.flag = (ImageView) returnView
						.findViewById(R.id.imageView_flag);
				returnView.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				returnView.setTag(viewHolder);
			} else {
				returnView = convertView;
				viewHolder = (GroupViewHolder) returnView.getTag();
			}
			viewHolder.text.setText(EventArrangementActivity
					.getFitString(treeNodes.get(groupPosition).parent));

			if (viewHolder.text.getText().toString().contains("可回收"))
				viewHolder.logo.setImageResource(resids[0]);
			else if (viewHolder.text.getText().toString().contains("厨余"))
				viewHolder.logo.setImageResource(resids[1]);
			else if (viewHolder.text.getText().toString().contains("有害"))
				viewHolder.logo.setImageResource(resids[2]);
			else//其他都使用其他图标
				viewHolder.logo.setImageResource(resids[3]);

			if (isExpanded) {
				viewHolder.flag.setImageResource(R.drawable.pic_garbageclose);
			} else
				viewHolder.flag.setImageResource(R.drawable.pic_garbageopen);

			return returnView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		public void setTreeNode(List<TreeNode> nodes) {
			this.treeNodes = nodes;
		}
	}

	class TreeNode {
		public List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		public String parent;
	}

	/** 重试按钮 */
	private Button button_retry = null;
	/**
	 * 垃圾分类数据Model链表
	 */
	private LinkedList<GarbageSortingEngine.GarbageSortingModel> garbageSortings = null;
	/**
	 * 主要显示的ExpandableListView
	 */
	private ExpandableListView listView_showmain = null;
	/** 加载进度条 */
	private ProgressBar progressBar_loading = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_garbage_sorting);
		settitle("垃圾分类查询");
		commonInit();

		listView_showmain = (ExpandableListView) findViewById(R.id.listView_showmain);
		listView_showmain.setGroupIndicator(null);
		views.add(listView_showmain);
		updateInfo();
	}

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
		textView_tip.setText("奇怪,查不到垃圾分类信息?");
	}

	/**
	 * 更新UI界面
	 * @param UIMode UI模式
	 * @param updateData 指示是否更新数据
	 * @param showTip 指示是否显示提示
	 * @param tipStr 提示文本
	 */
	public void updateUI(UIMode UIMode, boolean updateData, boolean showTip,
			String tipStr) {
		runOnUiThread(new Runnable() {
			private UIMode UIMode;
			private boolean updateData;
			private boolean showTip;
			private String tipStr;

			public Runnable setData(UIMode UIMode, boolean updateData,
					boolean showTip, String tipStr) {
				this.UIMode = UIMode;
				this.updateData = updateData;
				this.showTip = showTip;
				this.tipStr = tipStr;
				return this;
			}

			public void run() {
				setUIMode(UIMode, updateData);
				if (showTip)
					MyToast.ShowMyToast(getApplicationContext(), tipStr);
			}
		}.setData(UIMode, updateData, showTip, tipStr));
	}
	
	/**
	 * 显示受控的View的集合
	 */
	private LinkedList<View> views = new LinkedList<View>();
	/**
	 * 设置只有指定的View显示
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
	 * 文本提示
	 */
	private TextView textView_tip = null;
	/**
	 * 设定UI模式,同时可指定是否更新数据
	 * @param mode
	 * @param updateUIData
	 */
	private void setUIMode(UIMode mode, boolean updateUIData) {
		Log.e(this.getClass().toString() + ":UIMode=", mode.toString());
		switch (mode) {
		case UI_1:
			if (updateUIData) {
				updateGarbageList();
			}
			setViewVisible(listView_showmain);
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

	/**
	 * 联网更新信息
	 */
	private void updateInfo() {
		setUIMode(UIMode.UI_Loading, false);

		new Thread() {

			@Override
			public void run() {
				if (NetUtil.checkNet(getApplication())) {
					garbageSortings = new GarbageSortingEngineImpl()
							.queryGarbageCategoryInfo();
					if (garbageSortings == null) {
						if (GarbageSortingEngineImpl.isSucceed)
							updateUI(UIMode.UI_OnlyTip, false, false, null);
						else
							updateUI(UIMode.UI_OnlyRetry, false, true,
									GarbageSortingEngineImpl.errorMsg);

						return;
					}
					for (GarbageSortingEngine.GarbageSortingModel model : garbageSortings) {
						model.garbages = new GarbageSortingEngineImpl()
								.queryGarbageInfo(model.id);
						if (model.garbages == null) {
							if (GarbageSortingEngineImpl.isSucceed)
								continue;
							else
								updateUI(UIMode.UI_OnlyRetry, false, true,
										GarbageSortingEngineImpl.errorMsg);
							return;
						}
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
	 * 在UI线程显示网络不稳定对话框
	 */
	private void showNetWorkLost() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				PromptManager.showNoNetWork(GarbageSortingQueryActivity.this);
			}
		});
	}
	/**
	 * 更新垃圾列表
	 */
	private void updateGarbageList() {
		if (garbageSortings != null && !garbageSortings.isEmpty()) {
			Map<String, String> map = null;
			LinkedList<TreeNode> treeNodes = new LinkedList<TreeNode>();
			for (GarbageSortingEngine.GarbageSortingModel model : garbageSortings) {
				TreeNode node = new TreeNode();
				node.parent = model.name;
				Log.e("SWZ-parent", node.parent);
				if (model.garbages != null && !model.garbages.isEmpty())
					for (GarbageSortingEngine.GarbageModel subModel : model.garbages) {
						map = new HashMap<String, String>();
						map.put("name", subModel.name);
						map.put("integral", subModel.integral);
						Log.e("SWZ-name/integral", subModel.name + "/"
								+ subModel.integral);
						node.list.add(map);
					}
				treeNodes.add(node);
			}
			ExAdapter tmpAdapter = new ExAdapter(this);
			tmpAdapter.setTreeNode(treeNodes);
			listView_showmain.setAdapter(tmpAdapter);
		} else {
			setUIMode(UIMode.UI_OnlyRetry, false);
			MyToast.ShowMyToast(getApplicationContext(), "垃圾分类数据赋值给UI控件失败");
		}
	}

}
