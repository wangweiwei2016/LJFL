package com.aimowei.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.Window;

import com.aimowei.common.AppContext;
import com.aimowei.common.NetOperator;
import com.aimowei.garbage.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class SplashActivity extends Activity {

	private SharedPreferences preferences, LoginSharedPreferences;
	private Editor editor;
	private Context context;
	private final int SPLASH_DISPLAY_LENGHT = 3; //
	private String account;
	private String password;
	private Handler handler;
	private AppContext mContext = AppContext.getInstance();

	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "bd09ll";
	String longiString, latiString;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		context = SplashActivity.this;
		handler = new Handler();
		preferences = getSharedPreferences("firststart", Context.MODE_PRIVATE);
		// 判断是不是首次登录，
		System.out.println(preferences.getBoolean("firststart", true));
		if (preferences.getBoolean("firststart", true)) {
			System.out.println("1");
			editor = preferences.edit();
			//  将登录标志位设置为false，下次登录时不在显示首次登录界面
			editor.putBoolean("firststart", false);
			editor.commit();
			Intent mainIntent = new Intent(SplashActivity.this,
					MyGuideViewActivity.class);
			startActivity(mainIntent);
			SplashActivity.this.finish();
			return;
		} else {
			logiValidate();
			startGetLocation();

		}
	}

	private void logiValidate() {

		Thread tt = new Thread(new Thread() {
			@Override
			public void run() {
				String result = "0";

				try {
					String mDeviceID = Secure.getString(
							context.getContentResolver(), Secure.ANDROID_ID);
					SharedPreferences sp = AppContext.getInstance().getSharedPreferences();
					account = sp.getString("USERNAME", "0");
					password = sp.getString("PASSWORD", "0");
					System.out.println(account + "|" + password);
					if (account.equals("0") || password.equals("0")
							|| account.equals("") || password.equals("")) {
						handler.postDelayed(new Runnable() {
							@Override
							public void run() {
								Intent mainIntent = new Intent(
										SplashActivity.this, Login.class);
								SplashActivity.this.startActivity(mainIntent);
								SplashActivity.this.finish();
							}
						}, SPLASH_DISPLAY_LENGHT);
					} else {
						NetOperator network = new NetOperator();
						String passwordString = password;
						NameValuePair nameValuePair1 = new BasicNameValuePair(
								"YHZH", account);
						NameValuePair nameValuePair2 = new BasicNameValuePair(
								"YHMM", passwordString);
						List<NameValuePair> namePairValus = new ArrayList<NameValuePair>();
						namePairValus.add(nameValuePair1);
						namePairValus.add(nameValuePair2);
						
						String url = mContext.baseUrlString +  "Account/Login";
						result = network.PostRequest(url, namePairValus);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String msgsString = "";
				String dataString = "";
				int status = 0;
				try {
					JSONObject jsonObject = new JSONObject(result);
					status = jsonObject.getInt("status");
					msgsString = jsonObject.getString("msg");
					dataString = jsonObject.getString("data")+"";
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (status == 0) {
					Editor editor = AppContext.getInstance().getSharedPreferences().edit();
					editor.putString("password", "");
					editor.commit();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							Intent mainIntent = new Intent(SplashActivity.this,
									Login.class);
							SplashActivity.this.startActivity(mainIntent);
							SplashActivity.this.finish();
						}
					}, SPLASH_DISPLAY_LENGHT);
				} else if (status == 1) {
					mContext.setUsername(account) ;
					mContext.setUserID(dataString);
					Runnable r = new Runnable() {
						@Override
						public void run() {
							// Activity
							mLocationClient.stop();
							Intent mainIntent = new Intent(SplashActivity.this,
									MainActivity.class);
							SplashActivity.this.startActivity(mainIntent);
							SplashActivity.this.finish();
						}
					};
					handler.postDelayed(r, SPLASH_DISPLAY_LENGHT);
				}

			}
		});
		tt.start();

	}
	private void logiValidateWithLocaion() {

		Thread tt = new Thread(new Thread() {
			@Override
			public void run() {
				String result = "0";
				try {
					String mDeviceID = Secure.getString(
							context.getContentResolver(), Secure.ANDROID_ID);
					SharedPreferences sp = AppContext.getInstance().getSharedPreferences();
					account = sp.getString("USERNAME", "0");
					password = sp.getString("PASSWORD", "0");
					System.out.println(account + "|" + password);
					
						NetOperator network = new NetOperator();
						String passwordString = password;
						NameValuePair nameValuePair1 = new BasicNameValuePair(
								"login_email", account);
						NameValuePair nameValuePair2 = new BasicNameValuePair(
								"login_password", passwordString);
						List<NameValuePair> namePairValus = new ArrayList<NameValuePair>();
						namePairValus.add(nameValuePair1);
						namePairValus.add(nameValuePair2);
						namePairValus.add(new BasicNameValuePair("role", "0"));
						namePairValus.add(new BasicNameValuePair("lati",
								latiString));
						namePairValus.add(new BasicNameValuePair("longi",
								longiString));
						String url = mContext.baseUrlString + "index.php?app=app&mod=User&act=login";
						result = network.PostRequest(url, namePairValus);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		tt.start();

	}
	public void startGetLocation() {
		mLocationClient = ((App) getApplication()).mLocationClient;
		MyLocationListener mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
		/*else
			logiValidate();*/
	}

	private void InitLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);
		option.setCoorType(tempcoor);
		int span = 999;
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			mContext.CityName = location.getCity();
			longiString = location.getLongitude() + "";
			latiString = location.getLatitude() + "";
			logiValidateWithLocaion();

		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}
