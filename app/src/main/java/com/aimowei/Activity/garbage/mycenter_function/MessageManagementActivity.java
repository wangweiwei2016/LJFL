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
import com.aimowei.Activity.garbage.engine.impl.MessageManagementEngineImpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.ui.RefreshListView;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;

public class MessageManagementActivity extends BaseActivity implements
		OnRefreshListener {

	private RefreshListView listview;
	private MessageAdapter messageAdapter;
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
		setContentView(R.layout.activity_message_management);
		settitle("消息管理");
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
				if (sp.getString("yonghuleibie", "").equals("1")) {
					// 创建线程进行网络请求获取相应的商品信息
					Thread thread = new Thread(new GetMessage(sp.getString("SQYHMC", ""), "0", "10"));
					thread.start();
				} else {
					Thread thread = new Thread(new GetMessage(sp.getString("YHZH", ""), "0", "10"));
					thread.start();
				}
			}
		});

		setUIStatus(UIStatus.UI_Loading);
		// 用户为社区用户则发送社区用户名称，否则发送用户帐号
		if (sp.getString("yonghuleibie", "").equals("1")) {
			Thread thread = new Thread(new GetMessage(sp.getString("SQYHMC", ""), "0", "10"));
			thread.start();
		} else {
			Thread thread = new Thread(new GetMessage(sp.getString("YHZH", ""), "0", "10"));
			thread.start();
		}
	}

	/**
	 * 获取消息的线程类
	 */
	public class GetMessage implements Runnable {

		String jsr;
		String skipNum;
		String takeNum;

		/**
		 * @param jsr
		 *            --对应数据库消息表中的接收人字段，社区用户传社区用户代码，其他传用户帐号
		 * @param skipNum
		 *            --跳过消息的条数
		 * @param takeNum
		 *            --获取消息的条数
		 */
		public GetMessage(String jsr, String skipNum, String takeNum) {
			this.jsr = jsr;
			this.skipNum = skipNum;
			this.takeNum = takeNum;
		}

		@Override
		public void run() {
			String data = null;
			if (jsr.equals("")) {// 获取不到接收人的信息
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
				MessageManagementEngineImpl impl = new MessageManagementEngineImpl();
				data = impl.queryMessage(jsr, skipNum, takeNum);
				Log.e("11111", " " + data);
				if (data == null) {
					Log.e("11111", "11111");
					runOnUiThread(new Runnable() {
						public void run() {
							listview.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							PromptManager.showNoNetWork(MessageManagementActivity.this);
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
						PromptManager.showNoNetWork(MessageManagementActivity.this);
					}
				});
			}
		}
	}

	/**
	 * 删除消息的线程类
	 * 
	 * @param 流水号serial
	 */
	public class DeleteMessage implements Runnable {

		String serial;

		// 参数对应数据库消息表中的消息流水号字段
		public DeleteMessage(String serial) {
			this.serial = serial;
		}

		@Override
		public void run() {
			String data = null;
			if (serial.equals("")) {// 获取不到要删除的消息所对应的流水号
				runOnUiThread(new Runnable() {
					public void run() {
						listview.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						MyToast.ShowMyToast(getApplicationContext(), "当前消息列表失效，请重新获取消息");
					}
				});
				return;
			}
			if (NetUtil.checkNet(getApplication())) {// 检查网络
				// 发送删除消息的请求，获取返回信息data
				MessageManagementEngineImpl impl = new MessageManagementEngineImpl();
				data = impl.deleteMessage(serial);
				Log.e("11111", " " + data);
				if (data == null) {
					Log.e("11111", "11111");
					runOnUiThread(new Runnable() {
						public void run() {
							listview.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							PromptManager.showNoNetWork(MessageManagementActivity.this);
						}
					});
					return;
				}
				try {
					// 取出josn中的Status字段，为0则输出Info字段的信息，否则提示“删除成功”
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
								listview.setAdapter(null);// 清空listview
								setUIStatus(UIStatus.UI_LoadError);
								MyToast.ShowMyToast(getApplicationContext(), str);
							}
						}.setString(object.getString("Info")));
					} else {// 提示删除成功，并重新获取消息列表
						runOnUiThread(new Runnable() {
							public void run() {
								setUIStatus(UIStatus.UI_Loaded);
								MyToast.ShowMyToast(getApplicationContext(), "删除成功");
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
					runOnUiThread(new Runnable() {
						public void run() {
							Log.e("11111", "33333");
							listview.setAdapter(null);// 清空listview
							setUIStatus(UIStatus.UI_LoadError);
							MyToast.ShowMyToast(getApplicationContext(), "删除失败，请检查网络");
						}
					});
				}
			} else {
				runOnUiThread(new Runnable() {
					public void run() {
						Log.e("11111", "444444");
						listview.setAdapter(null);// 清空listview
						setUIStatus(UIStatus.UI_LoadError);
						PromptManager.showNoNetWork(MessageManagementActivity.this);
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
			PromptManager.showNoNetWork(MessageManagementActivity.this);
			return;
		}
		// 创建适配器
		messageAdapter = new MessageAdapter(MessageManagementActivity.this,
				listItems);
		listview.setAdapter(messageAdapter);
		listview.setOnRefreshListener(this);
	}

	public List<Map<String, Object>> getData_hash(String str)
			throws JSONException {
		JSONObject object = new JSONObject(str);
		// 将数据转化为JSON数组
		object = object.getJSONObject("Data");
		JSONArray data1 = object.getJSONArray("Time");
		JSONArray data2 = object.getJSONArray("Type");
		JSONArray data3 = object.getJSONArray("Content");
		JSONArray data4 = object.getJSONArray("Serial");
		int length = data1.length();
		if (length <= 0) {
			return null;
		}
		// 将JSON数组转化为HashMap
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < length; i++) {
			map.put("Time", data1.get(i));
			map.put("Type", data2.get(i));
			Log.e("suweizhao", "+++" + data2.get(i));
			map.put("Content", data3.get(i));
			map.put("Serial", data4.get(i));
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

	public class MessageAdapter extends BaseAdapter {

		private Context context; // 运行上下文
		private List<Map<String, Object>> listItems; // 商品信息集合
		private LayoutInflater listContainer; // 视图容器

		public final class ListItemView { // 自定义控件集合
			public TextView listitem_textView_time;
			public TextView listitem_textView_type;
			public TextView listitem_textView_content;
			public Button listitem_button_delete;
		}

		public MessageAdapter(Context context,
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
		public View getView(int position, View convertView, ViewGroup parent) {
			final int selectID = position;
			// 自定义视图
			ListItemView listItemView = null;
			if (convertView == null) {
				listItemView = new ListItemView();
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(R.layout.message_management_list, null);
				// 获取控件对象
				listItemView.listitem_textView_time = (TextView) convertView.findViewById(R.id.listitem_textView_time);
				listItemView.listitem_textView_type = (TextView) convertView.findViewById(R.id.listitem_textView_type);
				listItemView.listitem_textView_content = (TextView) convertView.findViewById(R.id.listitem_textView_content);
				listItemView.listitem_button_delete = (Button) convertView.findViewById(R.id.listitem_button_delete);
				listItemView.listitem_button_delete.setFocusable(false);
				// 设置控件集到convertView
				convertView.setTag(listItemView);
				Log.e("1111111111", "1111111111111");
			} else {
				listItemView = (ListItemView) convertView.getTag();
				Log.e("2222222222", "222222222222");
			}

			// 设置文字
			listItemView.listitem_textView_time.setText((String) listItems.get(position).get("Time"));
			listItemView.listitem_textView_type.setText((String) listItems.get(position).get("Type"));
			listItemView.listitem_textView_content.setText((String) listItems.get(position).get("Content"));
			listItemView.listitem_button_delete.setTag((String) listItems.get(position).get("Serial"));

			// 设置按钮的监听器
			listItemView.listitem_button_delete.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new AlertDialog.Builder(MessageManagementActivity.this);
							builder.setMessage("确定删除？").setCancelable(false)
							.setIcon(R.drawable.icon111garbage)
							.setTitle(R.string.app_name)
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								String Serial = null;
								public DialogInterface.OnClickListener GetSerial(String str) {
									this.Serial = str;
									return this;
									}
								public void onClick(DialogInterface dialog, int id) {
									setUIStatus(UIStatus.UI_Loading);
									// 创建子线程进行删除消息操作
									Thread thread = new Thread(new DeleteMessage(Serial));
									thread.start();
									// 通知适配器更新
									listItems.remove(selectID);
									messageAdapter.notifyDataSetChanged();
									}
								}.GetSerial((String) v.getTag()))
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
					if (sp.getString("yonghuleibie", "").equals("1")) {
						// 创建线程进行网络请求获取相应的消息记录
						Thread thread = new Thread(new GetMessage(sp.getString("SQYHMC", ""), "0", "10"));
						thread.start();
					} else {
						Thread thread = new Thread(new GetMessage(sp.getString("YHZH", ""), "0", "10"));
						thread.start();
					}
					return true;
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							PromptManager.showNoNetWork(MessageManagementActivity.this);
							listview.hideHeaderView();
						}
					});
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					messageAdapter.notifyDataSetChanged();
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
					if (sp.getString("yonghuleibie", "").equals("1")) {// 用户为社区用户则发送社区用户名称，否则发送用户帐号
						MessageManagementEngineImpl impl = new MessageManagementEngineImpl();
						data = impl.queryMessage(sp.getString("SQYHMC", ""), String.valueOf(skipnumber), String.valueOf(takenumber));
					} else {
						MessageManagementEngineImpl impl = new MessageManagementEngineImpl();
						data = impl.queryMessage(sp.getString("YHZH", ""), String.valueOf(skipnumber), String.valueOf(takenumber));
					}
					return true;
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							PromptManager.showNoNetWork(MessageManagementActivity.this);
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
									MyToast.ShowMyToast(getApplicationContext(), "消息已全部加载");
								}
							});
						} else {
							List<Map<String, Object>> templist = null;
							templist = getData_hash(data);
							// 将新加载到的消息添加到messageAdapter的listItems中
							for (int i = 0; i < templist.size(); i++)
								messageAdapter.listItems.add(templist.get(i));
							// 通知messageAdapter更新listview
							messageAdapter.notifyDataSetChanged();
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
