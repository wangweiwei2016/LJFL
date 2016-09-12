package com.aimowei.Activity.garbage.mycenter_function;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.adapter.DateAdapter;
import com.aimowei.Activity.garbage.bean.DateText;
import com.aimowei.Activity.garbage.bean.LevelBean;
import com.aimowei.Activity.garbage.engine.impl.MyPersonCenterEngineimpl;
import com.aimowei.Activity.garbage.net.NetUtil;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.Activity.garbage.utils.PromptManager;
import com.aimowei.garbage.R;
import com.alibaba.fastjson.JSON;

public class MyCreditActivity extends BaseActivity {
	// 时间轴列表
	private ListView lvList;
	// 数据list
	private List<DateText> list;
	// 列表适配器
	private DateAdapter adapter;

	private ProgressBar pro_loading;
	private LinearLayout ll;
	private TextView tv1;
	private Button btn;
	private TextView tv2;
	// private VerticalProgressBar pro1;
	private ImageView iv_sugan_no;// 树头没变色的时候
	private ImageView iv_sugan_deep;// 树头变色的时候
	private TextView tv3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycreadit);
		settitle("我的积分");

		// pro1 = (VerticalProgressBar) findViewById(R.id.pb1);
		iv_sugan_deep = (ImageView) findViewById(R.id.sugan_deep);
		iv_sugan_no = (ImageView) findViewById(R.id.sugan_no);
		tv3 = (TextView) findViewById(R.id.tv_mycredit_info3);
		pro_loading = (ProgressBar) findViewById(R.id.pbar_loading);
		ll = (LinearLayout) findViewById(R.id.ll_mycreadit);
		tv1 = (TextView) findViewById(R.id.tv_mycredit_info);
		tv2 = (TextView) findViewById(R.id.tv_mycredit_info2);
		btn = (Button) findViewById(R.id.button_retry_next);
		lvList = (ListView) findViewById(R.id.lv_list);
		list = new ArrayList<DateText>();
		if (sp.getString("yonghuleibie", "").equals("1")||sp.getString("yonghuleibie", "").equals("3")) {
			// 社区用户才有积分 “1” 为社区用户
			GetCreadit();
		} else {
			// pro_loading.setVisibility(View.INVISIBLE);

			// pro1.setMax(2500);
			// 31图片误差
			// pro1.setProgress(0);
			GetCreadit1();

			// listview绑定适配器
			// adapter = new DateAdapter(this, list);
			// lvList.setAdapter(adapter);
			// pro_loading.setVisibility(View.INVISIBLE);
			// ll.setVisibility(View.VISIBLE);
			// ll.setVisibility(View.VISIBLE);
		}

	}

	// 非社区用户查询
	private void GetCreadit1() {
		new Thread() {
			public void run() {
				// 判断网络
				if (NetUtil.checkNet(getApplication())) {
					MyPersonCenterEngineimpl im = new MyPersonCenterEngineimpl();

					runOnUiThread(new Runnable() {
						public void run() {
							// Toast.makeText(getApplicationContext(), "请稍候", 0)
							// .show();
						}
					});

					result = im.getCredit(sp.getString("yonghuleibie", ""),sp.getString("YDDH", ""));

					System.out.println(result);
					try {
						object = new JSONObject(result);

						levels = object.getString("level1");
						List = JSON.parseArray(levels, LevelBean.class);
						// 添加数据
						addData1();
						runOnUiThread(new Runnable() {
							public void run() {
								tv1.setText(ToDBC("我的积分：暂无积分"));
								tv2.setText(ToDBC("非社区用户没有积分"));
								tv3.setText("");
								// listview绑定适配器
								adapter = new DateAdapter(
										MyCreditActivity.this, list);
								lvList.setAdapter(adapter);
								pro_loading.setVisibility(View.INVISIBLE);

								ll.setVisibility(View.VISIBLE);
							}
						});
						System.out.println(List.toString());
						// return "";
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							btn.setVisibility(View.VISIBLE);
							pro_loading.setVisibility(View.INVISIBLE);

							// Toast.makeText(getApplicationContext(), "网络不稳定",
							// 0)
							// .show();
							// 显示无网络
							PromptManager.showNoNetWork(MyCreditActivity.this);
						}
					});

				}

			};
		}.start();

	}

	// 将textview中的字符全角化。即将所有的数字、字母及标点
	// 全部转为全角字符，使它们与汉字同占两个字节，这样就可以避
	// 免由于占位导致的排版混乱问题了。
	// 半角转为全角的代码如下，只需调用即可。
	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 往积分树 添加数据 非社区用户初始化的积分树的函数
	 */
	private void addData1() {
		for (int i = List.size() - 1; i >= 0; i--) {

			DateText date = new DateText(List.get(i).getName(), "", ""
					+ (int) List.get(i).getLevel_score(), false, false, false);
			list.add(date);
		}
		// DateText date1 = new DateText("环保超人", "", "2000", false, false,
		// false);

		// DateText date2 = new DateText("环保先锋", "", "1000", false, false,
		// false);

		// DateText date3 = new DateText("环保达人", "", "500", false, false,
		// false);

		// DateText date4 = new DateText("环保卫士", "", "200", false, false,
		// false);
		// DateText date5 = new DateText("环保新手", "", "0", false, false, false);

		// list.add(date2);
		// list.add(date3);
		// list.add(date4);
		// list.add(date5);

	}

	/**
	 * 社区用户初始化积分树
	 */
	private void addData(Double jifen) {

		// for(int i=List.size()-1;i>=0;i--){

		// DateText date = new DateText(List.get(i).getName(), "",
		// ""+(int)List.get(i).getLevel_score(), false, false, false);
		// list.add(date);
		// }
		int a = 0;
		//树顶>2500
		if (jifen == List.get(List.size() - 1).getLevel_score() + 500) {
			// 如果积分在2500

			// 环保新手环保卫士环保达人环保先锋环保超人
			tv2.setText(Html.fromHtml(ToDBC("总积分:" + "<font color=\"#ff0000\">"
					+ (jifen))));
			// tv3.setText(ToDBC("已经到达顶峰了，都来膜拜吧"));
			tv3.setText(Html
					.fromHtml(ToDBC("已经到达<font color=\"#ff0000\">顶峰</font>了，都来膜拜吧")));
			for (int i = List.size() - 1; i >= 0; i--) {
				if (i == List.size() - 1) {
					DateText date = new DateText(List.get(i).getName(), "", ""
							+ (int) List.get(i).getLevel_score(), false, true,
							true);
					list.add(date);
				} else {
					DateText date = new DateText(List.get(i).getName(), "", ""
							+ (int) List.get(i).getLevel_score(), false, true,
							false);
					list.add(date);
				}

			}
			 
		}else if(jifen>=List.get(List.size()-1).getLevel_score()&&jifen< List.get(List.size() - 1).getLevel_score() + 500)
		{//大于等于2000小于2500
			for (int i = List.size() - 1; i >= 0; i--) {
				tv2.setText(Html.fromHtml(ToDBC("总积分:" + "<font color=\"#ff0000\">" +
						 (jifen)))); 
						 tv3.setText(Html .fromHtml(ToDBC(
						 "正处于<font color=\"#ff0000\">"+List.get(List.size()-1).getName()+"</font>，距离<font color=\"#ff0000\">顶峰</font>还有"
						 + "<font color=\"#ff0000\">" + (int) ( List.get(List.size()-1).getLevel_score()+500- jifen) + "</font>" +
						  "分,加油哦")));
				if (i == List.size() - 1) {
					DateText date = new DateText(List.get(i).getName(), "", ""
							+ (int) List.get(i).getLevel_score(), false, true,
							true);
					list.add(date);
				} else {
					DateText date = new DateText(List.get(i).getName(), "", ""
							+ (int) List.get(i).getLevel_score(), false, true,
							false);
					list.add(date);
				}

			}
	
		}else{
			//>0小于2000
			int i=List.size()-1;
			for (i = List.size() - 1; i >= 0; i--) {
				if(i-1>=0&&jifen>=List.get(i-1).getLevel_score()&&jifen<List.get(i).getLevel_score()){
					tv2.setText(Html.fromHtml(ToDBC("总积分:" + "<font color=\"#ff0000\">" +
							 (jifen)))); 
							 tv3.setText(Html .fromHtml(ToDBC(
							 "正处于<font color=\"#ff0000\">"+List.get(i-1).getName()+"</font>，距离<font color=\"#ff0000\">"+List.get(i).getName()+"</font>还有"
							 + "<font color=\"#ff0000\">" + (int) ( List.get(i).getLevel_score()- jifen) + "</font>" +
							  "分,加油哦")));
								DateText date1 = new DateText(List.get(i).getName(), "", ""
										+ (int) List.get(i).getLevel_score(), false, false,
										false);
								list.add(date1);
							 DateText date = new DateText(List.get(i-1).getName(), "", ""
										+ (int) List.get(i-1).getLevel_score(), false, true,
										true);
								list.add(date); 
							 break;
				}else{
					DateText date = new DateText(List.get(i).getName(), "", ""
							+ (int) List.get(i).getLevel_score(), false, false,
							false);
					list.add(date);
				}
				
				
			
			}
			
			for(int j=i-1-1;j>=0;j--){
				if(j>=0){
					DateText date = new DateText(List.get(j).getName(), "", ""
							+ (int) List.get(j).getLevel_score(), false, true,
							false);
					list.add(date);
				}
				
			}
			
		}
		/*
		 * 
		 * DateText date1 = null; DateText date2 = null; DateText date3 = null;
		 * DateText date4 = null; DateText date5 = null; // 如果积分在0到200之间 if (0
		 * <= jifen && jifen < 200) {
		 * 
		 * date1 = new DateText("环保超人", "", "2000", false, false, false);
		 * 
		 * date2 = new DateText("环保先锋", "", "1000", false, false, false);
		 * 
		 * date3 = new DateText("环保达人", "", "500", false, false, false);
		 * 
		 * date4 = new DateText("环保卫士", "", "200", false, false, false); date5 =
		 * new DateText("环保新手", "", "0", false, true, true);
		 * tv2.setText(Html.fromHtml(ToDBC("总积分:" + "<font color=\"#ff0000\">" +
		 * (jifen))));
		 * 
		 * tv3.setText(Html .fromHtml(ToDBC(
		 * "正处于<font color=\"#ff0000\">环保新手</font>，距离<font color=\"#ff0000\">环保卫士</font>还有"
		 * + "<font color=\"#ff0000\">" + (int) (200 - jifen) + "</font>" +
		 * "分,加油哦"))); } else if (200 < jifen && jifen < 500 || jifen == 200) {
		 * // 如果积分在200到500之间 tv2.setText(Html.fromHtml(ToDBC("总积分:" +
		 * "<font color=\"#ff0000\">" + (jifen)))); //
		 * tv3.setText(ToDBC("正处于环保卫士，距离环保达人还有" + (int) (500 - jifen) + //
		 * "分，加油哦")); tv3.setText(Html .fromHtml(ToDBC(
		 * "正处于<font color=\"#ff0000\">环保卫士</font>，距离<font color=\"#ff0000\">环保达人</font>还有"
		 * + "<font color=\"#ff0000\">" + (int) (500 - jifen) + "</font>" +
		 * "分,加油哦"))); date1 = new DateText("环保超人", "", "2000", false, false,
		 * false);
		 * 
		 * date2 = new DateText("环保先锋", "", "1000", false, false, false);
		 * 
		 * date3 = new DateText("环保达人", "", "500", false, false, false);
		 * 
		 * date4 = new DateText("环保卫士", "", "200", false, true, true); date5 =
		 * new DateText("环保新手", "", "0", false, true, false); } else if (500 <
		 * jifen && jifen < 1000 || jifen == 500) { // 如果积分在500到1000之间
		 * 
		 * // tv3.setText(ToDBC("正处于环保达人，距离环保先锋还有" + (int) (1000 - jifen) + //
		 * "分，加油哦")); tv3.setText(Html .fromHtml(ToDBC(
		 * "正处于<font color=\"#ff0000\">环保达人</font>，距离<font color=\"#ff0000\">环保先锋</font>还有"
		 * + "<font color=\"#ff0000\">" + (int) (1000 - jifen) + "</font>" +
		 * "分,加油哦"))); tv2.setText(Html.fromHtml(ToDBC("总积分:" +
		 * "<font color=\"#ff0000\">" + (jifen)))); date1 = new DateText("环保超人",
		 * "", "2000", false, false, false);
		 * 
		 * date2 = new DateText("环保先锋", "", "1000", false, false, false);
		 * 
		 * date3 = new DateText("环保达人", "", "500", false, true, true);
		 * 
		 * date4 = new DateText("环保卫士", "", "200", false, true, false); date5 =
		 * new DateText("环保新手", "", "0", false, true, false); } else if (1000 <
		 * jifen && jifen < 2000 || jifen == 1000) { // 如果积分在1000到2000之间
		 * 
		 * // 环保新手环保卫士环保达人环保先锋环保超人 tv2.setText(Html.fromHtml(ToDBC("总积分:" +
		 * "<font color=\"#ff0000\">" + (jifen)))); //
		 * tv3.setText(ToDBC("正处于环保先锋，距离环保超人还有" + (int) (2000 - jifen) + //
		 * "分，加油哦")); tv3.setText(Html .fromHtml(ToDBC(
		 * "正处于<font color=\"#ff0000\">环保先锋</font>，距离<font color=\"#ff0000\">环保超人</font>还有"
		 * + "<font color=\"#ff0000\">" + (int) (2000 - jifen) + "</font>" +
		 * "分,加油哦"))); date1 = new DateText("环保超人", "", "2000", false, false,
		 * false);
		 * 
		 * date2 = new DateText("环保先锋", "", "1000", false, true, true);
		 * 
		 * date3 = new DateText("环保达人", "", "500", false, true, false);
		 * 
		 * date4 = new DateText("环保卫士", "", "200", false, true, false); date5 =
		 * new DateText("环保新手", "", "0", false, true, false); } else if (2000 <
		 * jifen && jifen < 2500 || jifen == 2000) { // 如果积分在2000到2500之间
		 * 
		 * tv2.setText(Html.fromHtml(ToDBC("总积分:" + "<font color=\"#ff0000\">" +
		 * (jifen)))); // tv3.setText(ToDBC("正处于环保超人，距离顶峰还有" + (int) (2500 -
		 * jifen) + // "分，加油哦")); tv3.setText(Html .fromHtml(ToDBC(
		 * "正处于<font color=\"#ff0000\">环保超人</font>，距离<font color=\"#ff0000\">顶峰</font>还有"
		 * + "<font color=\"#ff0000\">" + (int) (2500 - jifen) + "</font>" +
		 * "分,加油哦"))); date1 = new DateText("环保超人", "", "2000", false, true,
		 * true);
		 * 
		 * date2 = new DateText("环保先锋", "", "1000", false, true, false);
		 * 
		 * date3 = new DateText("环保达人", "", "500", false, true, false);
		 * 
		 * date4 = new DateText("环保卫士", "", "200", false, true, false); date5 =
		 * new DateText("环保新手", "", "0", false, true, false); } else if (jifen
		 * == 2500) { // 如果积分在2500
		 * 
		 * // 环保新手环保卫士环保达人环保先锋环保超人 tv2.setText(Html.fromHtml(ToDBC("总积分:" +
		 * "<font color=\"#ff0000\">" + (jifen)))); //
		 * tv3.setText(ToDBC("已经到达顶峰了，都来膜拜吧")); tv3.setText(Html
		 * .fromHtml(ToDBC("已经到达<font color=\"#ff0000\">顶峰</font>了，都来膜拜吧")));
		 * date1 = new DateText("环保超人", "", "2000", false, true, true);
		 * 
		 * date2 = new DateText("环保先锋", "", "1000", false, true, false);
		 * 
		 * date3 = new DateText("环保达人", "", "500", false, true, false);
		 * 
		 * date4 = new DateText("环保卫士", "", "200", false, true, false); date5 =
		 * new DateText("环保新手", "", "0", false, true, false); }
		 * 
		 * list.add(date1); list.add(date2); list.add(date3); list.add(date4);
		 * list.add(date5);
		 */
	}

	private String result;
	private JSONObject object;
	private int status;
	private static List<LevelBean> List = new ArrayList<LevelBean>();
	private String levels;
	/**
	 * 当前积分
	 */
	private String mycredit;
	/**
	 * 全部积分
	 */
	private String allmycredit;

	/**
	 * 查询积分
	 */
	private void GetCreadit() {
		new Thread() {
			public void run() {
				// 判断网络
				if (NetUtil.checkNet(getApplication())) {
					MyPersonCenterEngineimpl im = new MyPersonCenterEngineimpl();

					runOnUiThread(new Runnable() {
						public void run() {
							// Toast.makeText(getApplicationContext(), "请稍候", 0)
							// .show();
						}
					});

					result = im.getCredit(sp.getString("yonghuleibie", ""),sp.getString("YDDH", ""));

					System.out.println(result);
					try {
						object = new JSONObject(result);

						status = object.getInt("status");
						object = new JSONObject(result);

						levels = object.getString("level1");
						List = JSON.parseArray(levels, LevelBean.class);
						// return "";
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// 查询成功 当status为0的时候
					if (status == 0) {
						try {

							object = new JSONObject(result);
							mycredit = object.getString("DQJF");
							allmycredit = object.getString("QBJF");

							runOnUiThread(new Runnable() {
								public void run() {
									pro_loading.setVisibility(View.INVISIBLE);

									tv1.setText(Html
											.fromHtml(ToDBC("当前积分：<font color=\"#ff0000\"  >"
													+ mycredit)));
									// 添加测试数据

									if (Double.parseDouble(allmycredit) < (int) List
											.get(List.size() - 1)
											.getLevel_score() + 500) {

										// pro1.setMax(2500);
										// 31图片误差
										// pro1.setProgress((int) (Double
										// .parseDouble(allmycredit)));

										addData(Double.parseDouble(allmycredit));

										// listview绑定适配器
										adapter = new DateAdapter(
												MyCreditActivity.this, list);
										lvList.setAdapter(adapter);
									} else {
										addData(List.get(List.size() - 1)
												.getLevel_score() + 500);

										tv2.setText(Html.fromHtml(ToDBC("总积分:"
												+ "<font color=\"#ff0000\">"
												+ (Double
														.parseDouble(allmycredit)))));

										tv3.setText(Html
												.fromHtml(ToDBC("已经到达<font color=\"#ff0000\">顶峰</font>了，都来膜拜吧")));
										iv_sugan_no.setVisibility(View.GONE);
										iv_sugan_deep
												.setVisibility(View.VISIBLE);

										// listview绑定适配器
										adapter = new DateAdapter(
												MyCreditActivity.this, list);
										lvList.setAdapter(adapter);
									}
									ll.setVisibility(View.VISIBLE);

									// Toast.makeText(getApplicationContext(),
									// "查询成功", 0).show();
								}
							});

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								pro_loading.setVisibility(View.INVISIBLE);
								btn.setVisibility(View.VISIBLE);
								// Toast.makeText(getApplicationContext(),
								// "查询失败",
								// 0).show();
								// 自定义Toast
								// MyToast.ShowMyToast(getApplicationContext(),
								// "查询成功");
							}
						});

					}

				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							btn.setVisibility(View.VISIBLE);
							pro_loading.setVisibility(View.INVISIBLE);

							// Toast.makeText(getApplicationContext(), "网络不稳定",
							// 0)
							// .show();
							// 显示无网络
							PromptManager.showNoNetWork(MyCreditActivity.this);
						}
					});

				}

			};
		}.start();
	}

	/**
	 * 网络不稳定 再次重试 重试button的点击事件
	 */
	public void try_next(View view) {
		btn.setVisibility(View.INVISIBLE);
		pro_loading.setVisibility(View.VISIBLE);
		if (sp.getString("yonghuleibie", "").equals("1")) {
			// 社区用户才有积分 “1” 为社区用户
			GetCreadit();
		} else {
			GetCreadit1();
		}
	}

}
