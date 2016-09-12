package com.aimowei.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aimowei.common.AppContext;
import com.aimowei.common.NetOperator;
import com.aimowei.common.WinToast;
import com.aimowei.garbage.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
public class Input extends Activity {
	public static Input instance = null;
	EditText editText;
	Button button;
	int feed_id = -1;
	String app_uid = "";
	String content = "";
	Context context = Input.this;
	private AppContext mContext = AppContext.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input);
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = Input.this.getWindow().getAttributes();
		lp.width = (int) (display.getWidth()); //
		/*
		 * lp.x=0; lp.y=0;
		 */
		Input.this.getWindow().setAttributes(lp);
		Intent intent = getIntent();
		feed_id = intent.getIntExtra("com.aimowei.feed_id", -1);
		app_uid = intent.getStringExtra("com.aimowei.app_uid");
		editText = (EditText) findViewById(R.id.edittext);
		editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				/*判断是否是“go” */
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					/*
					 *  InputMethodManager imm = (InputMethodManager) v
					 * .getContext().getSystemService(
					 * Context.INPUT_METHOD_SERVICE); if (imm.isActive()) {
					 * imm.hideSoftInputFromWindow(
					 * v.getApplicationWindowToken(), 0); }
					 */
					content = editText.getText().toString();
					MyAsyncTask task = new MyAsyncTask();
					task.execute();
					return true;
				}
				return false;
			}
		});
		button = (Button) findViewById(R.id.done);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				content = editText.getText().toString();
				MyAsyncTask task = new MyAsyncTask();
				task.execute();

			}
		});

		if (feed_id == -1) {
			Input.this.finish();
		}
		editText.setFocusable(true);
		editText.setFocusableInTouchMode(true);
		editText.requestFocus();

		Timer timer = new Timer();

		timer.schedule(new TimerTask() {
			public void run() {

				InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(editText, 0);
			}
		}, 98);
	}

	class MyAsyncTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			NetOperator network = new NetOperator();
			List<NameValuePair> namePairValus = new ArrayList<NameValuePair>();
			namePairValus.add(new BasicNameValuePair("uid", mContext.ID));
			namePairValus.add(new BasicNameValuePair("app_name", "public"));
			namePairValus.add(new BasicNameValuePair("table_name", "feed"));
			namePairValus.add(new BasicNameValuePair("app_uid", app_uid));
			namePairValus.add(new BasicNameValuePair("row_id", feed_id + ""));
			namePairValus.add(new BasicNameValuePair("to_comment_id", "0"));
			namePairValus.add(new BasicNameValuePair("to_uid", "0"));
			namePairValus.add(new BasicNameValuePair("app_row_id", "0"));
			namePairValus.add(new BasicNameValuePair("app_row_table", "feed"));
			namePairValus.add(new BasicNameValuePair("content", content));
			namePairValus.add(new BasicNameValuePair("ifShareFeed", "0"));
			namePairValus.add(new BasicNameValuePair("comment_old", "0"));
			namePairValus.add(new BasicNameValuePair("app_detail_url", "http://dianai.1366768.com/index.php?app=public&mod=Index&act=index"));
			String url = mContext.baseUrlString + "index.php?app=public&mod=CommentApi&act=addcomment";
			result = network.PostRequest(url, namePairValus);
			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null && !result.equals("")) {
				String msgsString = "";
				String dataString = "";
				int status = 0;
				try {
					JSONObject jsonObject = new JSONObject(result);
					status = jsonObject.getInt("status");
					msgsString = jsonObject.getString("info").toString();
					dataString = jsonObject.getString("data").toString();
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.toString());
				}
				if (status == 0) {
					WinToast.makeText(context, "����ʧ��").show();
				} else {
					WinToast.makeText(context, "���۳ɹ�").show();
					Input.this.finish();
				}
			}
		}
	}
}