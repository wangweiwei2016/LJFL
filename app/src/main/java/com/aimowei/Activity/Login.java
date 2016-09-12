package com.aimowei.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aimowei.common.AppContext;
import com.aimowei.common.LoadingDialog;
import com.aimowei.common.NetOperator;
import com.aimowei.common.WinToast;
import com.aimowei.garbage.R;
import com.aimowei.model.Response;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Login extends Activity implements OnClickListener {
	private EditText username, password;
	private Button login;
	private TextView register, forgetPassword;
	private Context context;
	private String goal;
	private Handler handler;
	private LinearLayout layout;
	private int num = 0;
	private TextView errorView;
	private LoadingDialog mDialog;
	private SQLiteDatabase db;

	public static final String INTENT_EMAIL = "intent_email";
	public static final String INTENT_PASSWORD = "intent_password";

	private LocationClient mLocationClient;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "bd09ll";
	private AppContext mContext = AppContext.getInstance();
	String longiString, latiString;
	int excute = 0;
	String deviceId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		username = (EditText) findViewById(R.id.user_name);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.loginButton);
		errorView = (TextView) findViewById(R.id.error);
		context = Login.this;
	
		mDialog = new LoadingDialog(context);
		handler = new MyHandler();
		register = (TextView) findViewById(R.id.register);
		register.setOnClickListener(this);
		forgetPassword = (TextView) findViewById(R.id.forget_password);
		forgetPassword.setOnClickListener(this);
		login.setEnabled(false);
		login.setOnClickListener(this);
	
		password.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

				if (!arg0.toString().isEmpty()) {
					System.out.println("login");
					login.setEnabled(true);
					login.setTextColor(Color.WHITE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

		});

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {

			if (mDialog != null && mDialog.isShowing())
				mDialog.dismiss();
			Intent intent;
			switch (msg.what) {
			case (10000):
				errorView.setVisibility(View.VISIBLE);
				String notifyString = "";
				if (msg.obj.toString() != "") {
					notifyString = "," + msg.obj.toString();
				}
				WinToast.toastWithCat(context, "登陆失败 " + notifyString, false);

				break;
			case (10001):
				excute = 1;
				intent = new Intent(Login.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			}
		}

	}

	public void startGetLocation() {
		mLocationClient = ((App) getApplication()).mLocationClient;
		MyLocationListener mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		InitLocation();
		mLocationClient.start();
		if (mLocationClient != null && mLocationClient.isStarted())
			mLocationClient.requestLocation();
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

	/**
	 *
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			/*
			 * city_layoutView.setVisibility(View.VISIBLE); TextView
			 * locationView = (TextView) findViewById(R.id.location);
			 */
			mContext.CityName = location.getCity();
			longiString = location.getLongitude() + "";
			latiString = location.getLatitude() + "";
			mLocationClient.stop();
			excute = 1;
			Thread loginMethod = new NetworkThreadWithoutLogin();
			goal = "Login";
			loginMethod.start();

		}
	}

	private void login() {

		String myUsername = username.getText().toString();
		String myPassword = password.getText().toString();
		if (myUsername.isEmpty() || myPassword.isEmpty()) {
			WinToast.toastWithCat(context, "用户名或密码不能为空", false);
			return;
		}
		if (mDialog != null && !mDialog.isShowing())
			mDialog.show();
		startGetLocation();
		Thread loginMethod = new NetworkThread();
		goal = "Login";
		loginMethod.start();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (arg0.getId()) {
		case (R.id.loginButton):
			arg0.setEnabled(false);
			login();
			arg0.setEnabled(true);
			break;
		case (R.id.register):
		/*	intent = new Intent(Login.this, Register.class);
			Login.this.overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
			startActivity(intent);*/
			break;
		case (R.id.forget_password):
		/*	intent = new Intent(Login.this, ChooseChangeWay.class);
			startActivity(intent);*/
			break;
		}
	}

	class NetworkThread extends Thread {
		@Override
		public void run() {
			String result = "0";
			AppContext mContext = AppContext.getInstance();
			if (AppContext.getInstance() != null) {
				Editor editor = AppContext.getInstance().getSharedPreferences().edit();
				editor.putString("USERNAME", username.getText().toString());
				editor.putString("PASSWORD", password.getText().toString());
				editor.commit();
			}
			try {
				NetOperator network = new NetOperator();

				String passwordString = password.getText().toString();
				NameValuePair nameValuePair1 = new BasicNameValuePair("YHZH", username.getText().toString());
				NameValuePair nameValuePair2 = new BasicNameValuePair("YHMM", passwordString);
				List<NameValuePair> namePairValus = new ArrayList<NameValuePair>();
				namePairValus.add(nameValuePair1);
				namePairValus.add(nameValuePair2);
				namePairValus.add(new BasicNameValuePair("role", "0"));
				namePairValus.add(new BasicNameValuePair("lati", latiString));
				namePairValus.add(new BasicNameValuePair("longi", longiString));
				String url = mContext.baseUrlString + "Account/Login";
				result = network.PostRequest(url, namePairValus);

			} catch (Exception e) {
				e.printStackTrace();
			}

			Gson gson = new Gson();
			Response respone = gson.fromJson(result, Response.class);
			Message msg = handler.obtainMessage(10003, 1, 1, result);
			if (!respone.getStatus()) {
				msg.what = 10000;//
				msg.obj = respone.getData();

			} else {
				msg.what = 10001;//
				mContext.TempData = respone.getData();
				mContext.setUsername(username.getText().toString());
				mContext.setUserID(respone.getData());
			}
			handler.sendMessage(msg);

		}
	}

	class NetworkThreadWithoutLogin extends Thread {
		@Override
		public void run() {
			AppContext mContext = AppContext.getInstance();
			if (AppContext.getInstance() != null) {
				Editor editor = AppContext.getInstance().getSharedPreferences().edit();
				editor.putString("USERNAME", username.getText().toString());
				editor.putString("PASSWORD", password.getText().toString());
				editor.commit();
			}
			try {
				NetOperator network = new NetOperator();

				String passwordString = password.getText().toString();
				NameValuePair nameValuePair1 = new BasicNameValuePair("login_email", username.getText().toString());
				NameValuePair nameValuePair2 = new BasicNameValuePair("login_password", passwordString);
				List<NameValuePair> namePairValus = new ArrayList<NameValuePair>();
				namePairValus.add(nameValuePair1);
				namePairValus.add(nameValuePair2);
				namePairValus.add(new BasicNameValuePair("role", "0"));
				namePairValus.add(new BasicNameValuePair("lati", latiString));
				namePairValus.add(new BasicNameValuePair("longi", longiString));
				String url = mContext.baseUrlString + "index.php?app=app&mod=User&act=login";
				String result = network.PostRequest(url, namePairValus);

			} catch (Exception e) {
				e.printStackTrace();
			}
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
