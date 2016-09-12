package com.aimowei.Activity.garbage.mycenter_function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.engine.OnRefreshListener;
import com.aimowei.Activity.garbage.engine.impl.ApplyVolunteerEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.ui.RefreshListView;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;

public class ApplyVolunteerActivity extends BaseActivity implements OnRefreshListener {

	private RefreshListView listview;
	private ApplyVolunteerAdapter applyVolunteerAdapter;
	private Button button_retry = null;
	private ProgressBar progressBar_loading = null;
	private TextView textView;
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
				update_listview(datas);
			} catch (Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					public void run() {
						listview.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						MyToast.ShowMyToast(getApplicationContext(), "数据处理失败");
					}
				});
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_volunteer);
		settitle("申请志愿者");
		Log.e("11111", "0000000");
		listview = (RefreshListView) findViewById(R.id.lv_111);
		progressBar_loading = (ProgressBar) findViewById(R.id.progressBar_loading);
		textView = (TextView) findViewById(R.id.textView);
		button_retry = (Button) findViewById(R.id.button_retry);
		button_retry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("test", "点击到了刷新按钮");
				setUIStatus(UIStatus.UI_Loading);
				// 创建线程进行网络请求获取相应的申请信息
				Thread thread = new Thread(new GetMessage(sp.getString("SQDM", ""), "0", "10"));
				thread.start();
			}
		});

		setUIStatus(UIStatus.UI_Loading);
		Thread thread = new Thread(new GetMessage(sp.getString("SQDM", ""), "0", "10"));
		thread.start();
	}

	/**
	 * 获取申请消息的线程类
	 */
	public class GetMessage implements Runnable {

		String sqdm;
		String skipNum;
		String takeNum;

		/**
		 * @param Phone
		 *            --移动电话
		 * @param skipNum
		 *            --跳过消息的条数
		 * @param takeNum
		 *            --获取消息的条数
		 */
		public GetMessage(String sqdm, String skipNum, String takeNum) {
			this.sqdm = sqdm;
			this.skipNum = skipNum;
			this.takeNum = takeNum;
		}

		@Override
		public void run() {
			String data = null;
			if (sqdm.equals("")) {// 检查用户的移动电话字段是否为空
				runOnUiThread(new Runnable() {
					public void run() {
						listview.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						MyToast.ShowMyToast(getApplicationContext(), "系统超时，请以重新登录");
					}
				});
				return;
			}
			if (NetUtil.checkNet(getApplication())) {// 检查网络
				// 发送获取消息的请求，获取返回信息data
				ApplyVolunteerEngineImpl impl = new ApplyVolunteerEngineImpl();
				data = impl.getmessage(sqdm, skipNum, takeNum);
				Log.e("11111", " " + data);
				if (data == null) {
					Log.e("11111", "11111");
					runOnUiThread(new Runnable() {
						public void run() {
							listview.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							PromptManager.showNoNetWork(ApplyVolunteerActivity.this);
						}
					});
					return;
				}
				try {
					// 取出josn中的Status字段，为0则输出Info字段的信息，否则发送Message更新listview
					JSONObject object = new JSONObject(data);
					if (object.getInt("Status") == 0) {
						Log.e("11111", "22222");
						runOnUiThread(new Runnable() {
							public void run() {
								listview.setAdapter(null);// 清空listview
								setUIStatus(UIStatus.UI_LoadError);
							}
						});
					} else {
						Message message = new Message();
						Bundle b = new Bundle();
						Log.e("qwert", " " + data);
						b.putString("data", data);
						message.setData(b);
						MyHandler.sendMessage(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							Log.e("11111", "33333");
							listview.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							MyToast.ShowMyToast(getApplicationContext(), "数据获取失败，请检查网络");
						}
					});
				}
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						Log.e("11111", "444444");
						listview.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						PromptManager.showNoNetWork(ApplyVolunteerActivity.this);
					}
				});
			}
		}
	}

	/**
	 * 申请志愿者的线程类
	 */
	public class Apply implements Runnable {

		String yddh;
		String starttime;
		String endtime;

		// 参数对应数据库消息表中的消息流水号字段
		public Apply(String yddh,String starttime,String endtime) {
			this.yddh = yddh;
			this.starttime = starttime;
			this.endtime = endtime;
		}

		@Override
		public void run() {
			String data = null;
			if (yddh.equals("")) {// 获取用户的移动电话
				runOnUiThread(new Runnable() {
					public void run() {
						listview.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						MyToast.ShowMyToast(getApplicationContext(), "当前申请列表失效，请重新获取申请时段");
					}
				});
				return;
			}
			if (NetUtil.checkNet(getApplication())) {// 检查网络
				// 发送申请志愿者的请求，获取返回信息data
				ApplyVolunteerEngineImpl impl = new ApplyVolunteerEngineImpl();
				data = impl.apply(yddh, starttime, endtime);
				Log.e("11111", " " + data);
				if (data == null) {
					Log.e("11111", "11111");
					runOnUiThread(new Runnable() {
						public void run() {
							listview.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							PromptManager.showNoNetWork(ApplyVolunteerActivity.this);
						}
					});
					return;
				}
				try {
					// 取出josn中的Status字段，为0申请失败，为1申请成功，同时输出Info字段的信息
					JSONObject object = new JSONObject(data);
					if (object.getInt("Status") == 0) {
						Log.e("11111", "22222");
						runOnUiThread(new Runnable() {
							String str;

							public Runnable setString(String str) {
								this.str = str;
								return this;
							}

							public void run() {
								//listview.setAdapter(null);// 清空listview
								setUIStatus(UIStatus.UI_Loaded);
								MyToast.ShowMyToast(getApplicationContext(), str);
							}
						}.setString(object.getString("Info")));
					} else {// 提示申请成功，并重新获取消息列表
						runOnUiThread(new Runnable() {
							String str;

							public Runnable setString(String str) {
								this.str = str;
								return this;
							}
							
							public void run() {
								setUIStatus(UIStatus.UI_Loaded);
								MyToast.ShowMyToast(getApplicationContext(), str);
							}
						}.setString(object.getString("Info")));
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("异常", e.toString());
					runOnUiThread(new Runnable() {
						public void run() {
							Log.e("11111", "33333");
							listview.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							MyToast.ShowMyToast(getApplicationContext(), "申请失败，请检查网络");
						}
					});
				}
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						Log.e("11111", "444444");
						listview.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						PromptManager.showNoNetWork(ApplyVolunteerActivity.this);
					}
				});
			}
		}
	}

	public void update_listview(String data) {
		List<Map<String, Object>> listItems = null;
		try {
			listItems = getData_hash(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (listItems == null) {
			listview.setAdapter(null);// 清空listview
			setUIStatus(UIStatus.UI_LoadError);
			PromptManager.showNoNetWork(ApplyVolunteerActivity.this);
			return;
		}
		// 创建适配器
		applyVolunteerAdapter = new ApplyVolunteerAdapter(ApplyVolunteerActivity.this,
				listItems);
		listview.setAdapter(applyVolunteerAdapter);
		listview.setOnRefreshListener(this);
	}

	public List<Map<String, Object>> getData_hash(String str)
			throws JSONException {
		JSONObject object = new JSONObject(str);
		// 将数据转化为JSON数组
		object = object.getJSONObject("Data");
		JSONArray data1 = object.getJSONArray("StartTime");
		JSONArray data2 = object.getJSONArray("EndTime");
		JSONArray data3 = object.getJSONArray("Place");
		int length = data1.length();
		if (length <= 0) {
			return null;
		}
		// 将JSON数组转化为HashMap
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < length; i++) {
			map.put("StartTime", data1.get(i));
			map.put("EndTime", data2.get(i));
			map.put("Place", data3.get(i));
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
	 * @param UI_Loading -- 加载中，隐藏提示文本与刷新按钮，显示加载进度条。
	 * @param UI_Loaded -- 加载完成，隐藏提示文本，刷新按钮与加载进度条。
	 * @param UI_LoadError -- 加载失败，隐藏加载进度条，显示提示文本与刷新按钮。
	 */
	public void setUIStatus(UIStatus uiStatus) {
		switch (uiStatus) {
		case UI_Loading:
			progressBar_loading.setVisibility(View.VISIBLE);
			textView.setVisibility(View.GONE);
			button_retry.setVisibility(View.GONE);
			break;
		case UI_Loaded:
			progressBar_loading.setVisibility(View.GONE);
			textView.setVisibility(View.GONE);
			button_retry.setVisibility(View.GONE);
			break;
		case UI_LoadError:
			progressBar_loading.setVisibility(View.GONE);
			textView.setVisibility(View.VISIBLE);
			button_retry.setVisibility(View.VISIBLE);
			break;
		default:
			Log.e("LCJ", "有未处理的ActivityStatus");
			break;
		}
	}

	public class ApplyVolunteerAdapter extends BaseAdapter {

		private Context context; // 运行上下文
		private List<Map<String, Object>> listItems; // 商品信息集合
		private LayoutInflater listContainer; // 视图容器

		public final class ListItemView { // 自定义控件集合
			public TextView listitem_textView_starttime;
			public TextView listitem_textView_endtime;
			public TextView listitem_textView_place;
			public Button listitem_button_apply;
		}

		public ApplyVolunteerAdapter(Context context,
				List<Map<String, Object>> listItems) {
			this.context = context;
			listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		/**
		 * ListView Item设置
		 */
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final int selectID = position;
			// 自定义视图
			ListItemView listItemView = null;
			if (convertView == null) {
				listItemView = new ListItemView();
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(R.layout.apply_volunteer_list, null);
				// 获取控件对象
				listItemView.listitem_textView_starttime = (TextView) convertView.findViewById(R.id.listitem_textView_starttime);
				listItemView.listitem_textView_endtime = (TextView) convertView.findViewById(R.id.listitem_textView_endtime);
				listItemView.listitem_textView_place = (TextView) convertView.findViewById(R.id.listitem_textView_place);
				listItemView.listitem_button_apply = (Button) convertView.findViewById(R.id.listitem_button_apply);
				listItemView.listitem_button_apply.setFocusable(false);
				// 设置控件集到convertView
				convertView.setTag(listItemView);
				Log.e("1111111111", "1111111111111");
			} else {
				listItemView = (ListItemView) convertView.getTag();
				Log.e("2222222222", "222222222222");
			}

			// 设置文字
			listItemView.listitem_textView_starttime.setText((String) listItems.get(position).get("StartTime"));
			listItemView.listitem_textView_endtime.setText((String) listItems.get(position).get("EndTime"));
			listItemView.listitem_textView_place.setText((String) listItems.get(position).get("Place"));

			// 设置按钮的监听器
			listItemView.listitem_button_apply.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new AlertDialog.Builder(ApplyVolunteerActivity.this);
							builder.setMessage("是否申请？").setCancelable(false)
							.setIcon(R.drawable.icon111garbage)
							.setTitle(R.string.app_name)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									setUIStatus(UIStatus.UI_Loading);
									// 创建子线程进行申请志愿者操作
									Thread thread = new Thread(new Apply(sp.getString("YDDH", ""), (String) listItems.get(position).get("StartTime"), (String) listItems.get(position).get("EndTime")));
									thread.start();
									// 通知适配器更新
									listItems.remove(selectID);
									applyVolunteerAdapter.notifyDataSetChanged();
									}
								})
							.setNegativeButton("取消", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
									}
								}).show();
						}
					});
			return convertView;
		}
	}

	@Override
	public void onDownPullRefresh() {
		skipnumber = 10;// 重置参数
		takenumber = 5;

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtil.checkNet(getApplication())) {// 检查网络
					// 创建线程进行网络请求获取相应的消息记录
					Thread thread = new Thread(new GetMessage(sp.getString("SQDM", ""), "0", "10"));
					thread.start();
					return true;
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							PromptManager.showNoNetWork(ApplyVolunteerActivity.this);
							listview.hideHeaderView();
						}
					});
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					applyVolunteerAdapter.notifyDataSetChanged();
					MyToast.ShowMyToast(getApplicationContext(), "刷新成功");
				}
				// 控制头布局隐藏
				listview.hideHeaderView();
			}
		}.execute(new Void[] {});

	}

	@Override
	public void onLoadingMore() {

		new AsyncTask<Void, Void, Boolean>() {
			String data = null;

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtil.checkNet(getApplication())) {// 检查网络
					// 发送获取加载更多消息的请求，获取返回信息data
					ApplyVolunteerEngineImpl impl = new ApplyVolunteerEngineImpl();
					data = impl.getmessage(sp.getString("SQDM", ""), String.valueOf(skipnumber), String.valueOf(takenumber));
					return true;
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							PromptManager.showNoNetWork(ApplyVolunteerActivity.this);
							// 控制脚布局隐藏
							listview.hideFooterView();
						}
					});
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					if (data == null) {
						runOnUiThread(new Runnable() {
							public void run() {
								MyToast.ShowMyToast(getApplicationContext(), "查询失败，请检查网络");
							}
						});
						return;
					}
					try {
						// 取出josn中的Status字段，为0则输出Info字段的信息，否则更新listview
						JSONObject object = new JSONObject(data);
						if (object.getInt("Status") == 0) {
							runOnUiThread(new Runnable() {
								public void run() {
									MyToast.ShowMyToast(getApplicationContext(), "可申请信息已全部加载");
								}
							});
						} else {
							List<Map<String, Object>> templist = null;
							templist = getData_hash(data);
							// 将新加载到的消息添加到applyVolunteerAdapter的listItems中
							for (int i = 0; i < templist.size(); i++)
								applyVolunteerAdapter.listItems.add(templist.get(i));
							// 通知applyVolunteerAdapter更新listview
							applyVolunteerAdapter.notifyDataSetChanged();
							// 更新记录当前已加载的消息的数量
							skipnumber = takenumber + skipnumber;
						}
					} catch (Exception e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							public void run() {
								MyToast.ShowMyToast(getApplicationContext(), "数据获取失败，请检查网络");
							}
						});
					}
				}
				// 控制脚布局隐藏
				listview.hideFooterView();
			}
		}.execute(new Void[] {});

	}
}
