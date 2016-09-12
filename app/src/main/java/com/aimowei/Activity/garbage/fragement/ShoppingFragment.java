package com.aimowei.Activity.garbage.fragement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.aimowei.Activity.garbage.ConstantValue;
import com.aimowei.Activity.garbage.adapter.CommodityInfoAdapter;
import com.aimowei.Activity.garbage.engine.OnRefreshListener;
import com.aimowei.Activity.garbage.engine.impl.ShoppingEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.ui.RefreshListView;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;

public class ShoppingFragment extends BaseFragment implements OnRefreshListener {
	private TabHost tabHost;
	private TextView title;
	private RefreshListView lv_1;
	private RefreshListView lv_2;
	private CommodityInfoAdapter commodityInfoAdapter;
	private Button button_retry = null;
	private ProgressBar progressBar_loading = null;
	View view;
	/**
	 * 跳过消息的条数,用于上拉加载,默认为10
	 */
	private int skipnumber = 10;
	/**
	 * 获取消息的条数,用于上拉加载,默认为5
	 */
	private int takenumber = 5;

	public Handler MyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) { // 处理消息
			super.handleMessage(msg);
			Bundle b = msg.getData();
			String datas = b.getString("data");
			Log.e("abcde", " " + datas);
			try {
				setUIStatus(UIStatus.UI_Loaded);
				// 根据当前页面选择对应的listview进行更新
				if (b.getInt("page") == 0)
					update_listview(datas, lv_1);
				else
					update_listview(datas, lv_2);
			} catch (Exception e) {
				e.printStackTrace();
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						lv_1.setAdapter(null);// 清空listview
						lv_2.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						MyToast.ShowMyToast(getActivity(), "数据处理失败");
					}
				});
			}
		}
	};

	// 设置标题
	public void settitle(String title1) {
		title = (TextView) view.findViewById(R.id.title);
		title.setText(title1);
	}

	ImageView imb;

	// 隐藏返回键
	public void invisible(int visible) {
		imb = (ImageButton) view.findViewById(R.id.ib_return);
		imb.setVisibility(visible);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i("slide", "FriendFragment-onCreateView");
		if (view == null) {
			view = inflater.inflate(R.layout.activity_shop, container, false);
			tabHost = (TabHost) view.findViewById(R.id.TabHost01);

			tabHost.setup();

			View woTab = (View) LayoutInflater.from(getActivity()).inflate(
					R.layout.tabmini, null);
			TextView text1 = (TextView) woTab.findViewById(R.id.tab_label);
			text1.setBackgroundResource(R.drawable.tabmini);
			text1.setText("销量");
			View woTab1 = (View) LayoutInflater.from(getActivity()).inflate(
					R.layout.tabmini1, null);
			TextView text2 = (TextView) woTab1.findViewById(R.id.tab_label);
			text2.setBackgroundResource(R.drawable.tabmini1);
			text2.setText("积分");

			tabHost.addTab(tabHost.newTabSpec("销量").setIndicator(woTab)
					.setContent(R.id.LinearLayout1));
			tabHost.addTab(tabHost.newTabSpec("积分").setIndicator(woTab1)
					.setContent(R.id.LinearLayout2));

			tabHost.setCurrentTab(0);

		}
		ViewGroup parent = (ViewGroup) view.getParent();
		if (parent != null) {
			parent.removeView(view);// 先移除
		}

		invisible(View.INVISIBLE);
		settitle("积分奖品");

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		lv_1 = (RefreshListView) view.findViewById(R.id.lv_1);
		lv_2 = (RefreshListView) view.findViewById(R.id.lv_2);

		progressBar_loading = (ProgressBar) view.findViewById(R.id.progressBar_loading);
		button_retry = (Button) view.findViewById(R.id.button_retry);
		button_retry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("test", "点击到了重试按钮");
				setUIStatus(UIStatus.UI_Loading);
				skipnumber = 10;// 初始化参数
				takenumber = 5;
				// 创建线程进行网络请求获取相应的商品信息
				String str = String.valueOf(tabHost.getCurrentTab());// 查看当前页，看是进行销量查询还是积分查询
				Thread thread = new Thread(new GetCommodity(str, "0", "0", "10"));
				thread.start();
			}
		});

		// 创建线程进行网络请求获取相应的商品信息
		Thread thread = new Thread(new GetCommodity("0", "0", "0", "10"));
		thread.start();

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				// 当点击tab选项卡的时候，获取对应的页面的信息
				switch (tabHost.getCurrentTab()) {
				case 0:
					setUIStatus(UIStatus.UI_Loading);
					skipnumber = 10;// 初始化参数
					takenumber = 5;
					Thread thread1 = new Thread(new GetCommodity("0", "0", "0", "10"));
					thread1.start();
					break;
				case 1:
					setUIStatus(UIStatus.UI_Loading);
					skipnumber = 10;// 初始化参数
					takenumber = 5;
					Thread thread2 = new Thread(new GetCommodity("1", "0", "0", "10"));
					thread2.start();
					break;
				}
			}
		});

	}

	/**
	 * 获取商品信息的线程类
	 */
	public class GetCommodity implements Runnable {

		String type, sort;
		String skipNum;
		String takeNum;

		/**
		 * 获取商品信息的线程类的构造函数
		 * 
		 * @param type
		 *            --销量或积分排序
		 * @param sort
		 *            --降序或升序
		 * @param skipNum
		 *            --跳过消息的条数
		 * @param takeNum
		 *            --获取消息的条数
		 */
		public GetCommodity(String type, String sort, String skipNum,
				String takeNum) {
			this.sort = sort;
			this.type = type;
			this.skipNum = skipNum;
			this.takeNum = takeNum;
		}

		@Override
		public void run() {
			String data = null;
			if (sp.getString("YDDH", "").equals("")) {// 检查用户的移动电话字段是否为空
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						lv_1.setAdapter(null);// 清空listview
						lv_2.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						MyToast.ShowMyToast(getActivity(), "系统超时，请重新登录");
					}
				});
				return;
			}
			if (NetUtil.checkNet(getActivity())) {// 检查网络
				// 发送获取商品信息的请求，获取返回信息data
				ShoppingEngineImpl impl = new ShoppingEngineImpl();
				data = impl.queryCommodity(sp.getString("YDDH", ""), type, sort, skipNum, takeNum);
				if (data == null) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							lv_1.setAdapter(null);// 清空listview
							lv_2.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							PromptManager.showNoNetWork(getActivity());
						}
					});
					return;
				}
				try {
					// 取出josn中的Status字段，为0则输出Info字段的信息，否则发送Message更新listview
					JSONObject object = new JSONObject(data);
					if (object.getInt("Status") == 0) {
						getActivity().runOnUiThread(new Runnable() {
							String str;

							public Runnable setString(String str) {
								this.str = str;
								return this;
							}

							public void run() {
								lv_1.setAdapter(null);// 清空listview
								lv_2.setAdapter(null);// 清空listview
								setUIStatus(UIStatus.UI_LoadError);
								MyToast.ShowMyToast(getActivity(), str);
							}
						}.setString(object.getString("Info")));

					} else {
						Message message = new Message();
						Bundle b = new Bundle();
						Log.e("qwert", " " + data);
						b.putString("data", data);
						b.putInt("page", tabHost.getCurrentTab());
						message.setData(b);
						MyHandler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							lv_1.setAdapter(null);// 清空listview
							lv_2.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							MyToast.ShowMyToast(getActivity(), "商城数据获取失败，请检查网络");
						}
					});
				}
			} else {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						lv_1.setAdapter(null);// 清空listview
						lv_2.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						PromptManager.showNoNetWork(getActivity());
					}
				});
			}
		}
	}

	public void update_listview(String data, RefreshListView listview1) throws JSONException {
		List<Map<String, Object>> listItems = getData_hash(data);
		if (listItems == null) {
			lv_1.setAdapter(null);// 清空listview
			lv_2.setAdapter(null);// 清空listview
			setUIStatus(UIStatus.UI_LoadError);
			PromptManager.showNoNetWork(getActivity());
			return;
		}
		// 创建适配器
		commodityInfoAdapter = new CommodityInfoAdapter(view.getContext(), listItems); // 创建适配器
		listview1.setAdapter(commodityInfoAdapter);
		listview1.setOnRefreshListener(this);
	}

	public List<Map<String, Object>> getData_hash(String str)
			throws JSONException {
		JSONObject object = new JSONObject(str);
		// 将数据转化为JSON数组
		object = object.getJSONObject("Data");
		JSONArray data1 = object.getJSONArray("CommodityName");
		JSONArray data2 = object.getJSONArray("CommodityPicture");
		JSONArray data3 = object.getJSONArray("OriginalIntegral");
		JSONArray data4 = object.getJSONArray("CurrentIntegral");
		JSONArray data5 = object.getJSONArray("ExchangeNumber");
		JSONArray data6 = object.getJSONArray("StockNumber");
		int length = data1.length();
		if (length <= 0) {
			return null;
		}
		// 将JSON数组转化为HashMap
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < length; i++) {
			map.put("CommodityName", data1.get(i));
			map.put("CommodityPicture", ConstantValue.LOTTERY_URI1.concat("/Exchange/") + data2.get(i).toString());
			// map.put("CommodityPicture",data2.get(i).toString().replace("~",
			// "http://120.25.150.89"));
			map.put("OriginalIntegral", "原积分：" + data3.get(i));
			map.put("CurrentIntegral", "现积分：" + data4.get(i));
			map.put("ExchangeNumber", "销量：" + data5.get(i));
			map.put("StockNumber", "库存：" + data6.get(i));
			list.add(map);
			map = new HashMap<String, Object>();
		}
		return list;
	}

	public enum UIStatus {
		/** 加载中 */
		UI_Loading,
		/** 加载完毕 */
		UI_Loaded,
		/** 加载失败 */
		UI_LoadError
	}

	/**
	 * 设定UI模式
	 * @param UI_Loading -- 加载中，隐藏刷新按钮，显示加载进度条。
	 * @param UI_Loaded -- 加载完成，隐藏刷新按钮与加载进度条。
	 * @param UI_LoadError -- 加载失败，隐藏加载进度条，显示刷新按钮。
	 */
	public void setUIStatus(UIStatus uiStatus) {
		switch (uiStatus) {
		case UI_Loading:
			progressBar_loading.setVisibility(View.VISIBLE);
			button_retry.setVisibility(View.GONE);
			break;
		case UI_Loaded:
			progressBar_loading.setVisibility(View.GONE);
			button_retry.setVisibility(View.GONE);
			break;
		case UI_LoadError:
			progressBar_loading.setVisibility(View.GONE);
			button_retry.setVisibility(View.VISIBLE);
			break;
		default:
			Log.e("LCJ", "有未处理的ActivityStatus");
			break;
		}
	}

	@Override
	public void onDownPullRefresh() {
		skipnumber = 10;// 初始化参数
		takenumber = 5;

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtil.checkNet(getActivity())) {// 检查网络
					// SystemClock.sleep(1000);
					// 创建线程进行网络请求获取相应的商品信息
					String page_str = String.valueOf(tabHost.getCurrentTab());// 查看当前页，看是进行销量查询还是积分查询
					Thread thread = new Thread(new GetCommodity(page_str, "0", "0", "10"));
					thread.start();
					return true;
				} else {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							PromptManager.showNoNetWork(getActivity());
						}
					});
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					commodityInfoAdapter.notifyDataSetChanged();
					MyToast.ShowMyToast(getActivity(), "刷新成功");
				}
				// 控制头布局隐藏
				lv_1.hideHeaderView();
				lv_2.hideHeaderView();
			}
		}.execute(new Void[] {});
	}

	@Override
	public void onLoadingMore() {

		new AsyncTask<Void, Void, Boolean>() {
			String data = null;

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtil.checkNet(getActivity())) {// 检查网络
					// 查看当前页，看是进行销量查询还是积分查询
					String page_str = String.valueOf(tabHost.getCurrentTab());
					// 发送获取商品信息的请求，获取返回信息data
					ShoppingEngineImpl impl = new ShoppingEngineImpl();
					data = impl.queryCommodity(sp.getString("YDDH", ""), page_str, "0", String.valueOf(skipnumber), String.valueOf(takenumber));
					return true;
				} else {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							PromptManager.showNoNetWork(getActivity());
						}
					});
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					if (data == null) {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								MyToast.ShowMyToast(getActivity(), "查询失败，请检查网络");
								// 控制脚布局隐藏
								lv_1.hideFooterView();
								lv_2.hideFooterView();
							}
						});
						return;
					}
					try {
						// 取出josn中的Status字段，为0则输出Info字段的信息，否则发送Message更新listview
						JSONObject object = new JSONObject(data);
						if (object.getInt("Status") == 0) {
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									MyToast.ShowMyToast(getActivity(), "奖品信息已全部加载");
								}
							});
						} else {
							List<Map<String, Object>> templist = null;
							templist = getData_hash(data);
							// 将新加载到的消息添加到commodityInfoAdapter的listItems中
							for (int i = 0; i < templist.size(); i++)
								commodityInfoAdapter.listItems.add(templist.get(i));
							// 通知messageAdapter更新listview
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									commodityInfoAdapter.notifyDataSetChanged();
								}
							});
							// 更新记录当前已加载的消息的数量
							skipnumber = takenumber + skipnumber;
						}
					} catch (Exception e) {
						e.printStackTrace();
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								MyToast.ShowMyToast(getActivity(), "数据获取失败，请检查网络");
							}
						});
					}
				}
				// 控制脚布局隐藏
				lv_1.hideFooterView();
				lv_2.hideFooterView();
			}
		}.execute(new Void[] {});
	}
}
