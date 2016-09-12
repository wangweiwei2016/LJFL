package com.aimowei.Activity.garbage.mycenter_function;

import java.lang.reflect.Method;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.GlobalParams;
import com.aimowei.Activity.garbage.login_function.LoginActivity;
import com.aimowei.Activity.garbage.setting_function.AboutActivity;
import com.aimowei.Activity.garbage.setting_function.ConnectUsActivity;
import com.aimowei.Activity.garbage.utils.DataCleanManager;
import com.aimowei.Activity.garbage.utils.MyToast;
import com.aimowei.garbage.R;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private PackageManager pm;//包管理
	//private TextView tv_cathe_data;
	private long cache=0; 
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				long text =  (Long) msg.obj;
				//tv_cathe_data.setText(Formatter.formatFileSize(getApplicationContext(), cache));
				break;
			
			}
		};
	};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);// /slidingmenu里面重写了
		findViewById(R.id.tv_about).setOnClickListener(this);
		findViewById(R.id.tv_connect_us).setOnClickListener(this);
		//findViewById(R.id.tv_suggest).setOnClickListener(this);
		//findViewById(R.id.tv_consult).setOnClickListener(this);
		//findViewById(R.id.tv_view_setting).setOnClickListener(this);
		findViewById(R.id.btn_exit).setOnClickListener(this);
		findViewById(R.id.btn_unlogin).setOnClickListener(this);
		settitle("系统管理");
		//获取缓存
				try {
					pm = getPackageManager();
					Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
					method.invoke(pm, getPackageName(),new MyObserver());
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}

	/**
	 * 退出点击事件
	 */
	public void exit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		builder.setMessage("是否要退出程序?").setCancelable(false)
				.setIcon(R.drawable.icon111garbage).setTitle(R.string.app_name)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						exitSystem();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				}).show();
		AlertDialog alert = builder.create();

	}

	/** 咨询
	 * 
	 */
	public void consult() {
		Intent intent = new Intent(SettingActivity.this, ConsultActivity.class);
		startActivity(intent);
		return;
	}

	

	/**
	 * 系统管理的所有点击事件在这写
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	//	case R.id.tv_consult://咨询
		//	consult();
	//		break;
	//	case R.id.tv_suggest://请教
	//		suggest();
	//		break;
	//	case R.id.tv_view_setting://界面设置
	//		viewSetting();
	//		break;
		case R.id.btn_exit://退出
			exit();
			break;
		case R.id.btn_unlogin://注销当前用户
			unlogin();
			break;
		case R.id.tv_connect_us://联系我们
			connectUs();
			break;
		case R.id.tv_about://关于
			about();
			break;
		default:
			break;
		}

	}

	/**关于
	 * 
	 */
	private void about() {
		Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
		startActivity(intent);
	}

	/**联系我们
	 * 
	 */
	private void connectUs() {
		Intent intent = new Intent(SettingActivity.this,
				ConnectUsActivity.class);
		startActivity(intent);
	}

	/**注销帐号
	 * 
	 */
	public void unlogin() {
		GlobalParams.User = "";
		GlobalParams.isLogin = false;

		Editor editor = sp.edit();
		editor.putString("yonghuleibie", "");
		editor.putString("SQYHMC", "");
		editor.putString("YX", "");
		editor.putString("SQDM", "");
		editor.putString("YDDH", "");
		editor.putString("HZXM", "");
		editor.putString("YHZH", "");
		editor.commit();

		Intent intent = new Intent(SettingActivity.this, LoginActivity.class);

		startActivity(intent);

	}

	/**建议点击
	 * 
	 */
	private void suggest() {
		Intent intent = new Intent(SettingActivity.this, SuggestActivity.class);
		startActivity(intent);
		return;

	}
	private class MyObserver extends IPackageStatsObserver.Stub {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			 cache = pStats.cacheSize;
			 System.out.print(cache+"ddddddddddddddddddddd");
			long codeSize = pStats.codeSize;
		
			Message msg = Message.obtain();
			msg.what = 1;
				//System.out.println("当前应用程序："+pStats.packageName+"有缓存："+Formatter.formatFileSize(getApplicationContext(), cache));
			msg.obj =cache;
			handler.sendMessage(msg);
			
		}
	}
	/**推荐给朋友点击事件
	 * 
	 * 
	 */
	public void share(View view){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, "推荐一个应用,名字叫: " + "垃圾分类系统"+"\n"+"下载地址:"+"http://qyljfl.jimei.gov.cn:8888/Content/images/garbage.apk");
		startActivity(intent);
		
	}
	/**检测手动更新点击事件
	 * 
	 * @param view
	 */
	public void update(View view){
		UmengUpdateAgent.setDefault();
        //请在调用update,forceUpdate,silentUpdate函数之前设置推广id
        UmengUpdateAgent.setSlotId("garbageHUQ");
      
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
		    @Override
		    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
		        switch (updateStatus) {
		        case UpdateStatus.Yes: // has update
		            UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateInfo);
		            break;
		        case UpdateStatus.No: // has no update
		           MyToast.ShowMyToast(SettingActivity.this, "没有更新");
		        	//Toast.makeText(ViewSettingActivity.this, "没有更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.NoneWifi: // none wifi
		        	 MyToast.ShowMyToast(SettingActivity.this, "没有wifi连接， 只在wifi下更新");
		           // Toast.makeText(ViewSettingActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
		            break;
		        case UpdateStatus.Timeout: // time out
		        	 MyToast.ShowMyToast(SettingActivity.this, "超时");
		            //Toast.makeText(ViewSettingActivity.this, "超时", Toast.LENGTH_SHORT).show();
		            break;
		        }
		    }
		});
	
		UmengUpdateAgent.forceUpdate(this);
	}

	/**清理缓存
	 * 
	 *
	 */
	public void cleanCache(View view){
	DataCleanManager.cleanInternalCache(getApplicationContext());
	//	DataCleanManager.cleanExternalCache(getApplicationContext());
		//cleanAll();
		MyToast.ShowMyToast(getApplicationContext(), "缓存已清除");
		//获取缓存
		try {
			List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
			Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
			method.invoke(pm, getPackageName(),new MyObserver());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	
		
		
	}
}
