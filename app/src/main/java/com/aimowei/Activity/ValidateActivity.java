package com.aimowei.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimowei.common.AppContext;
import com.aimowei.common.LoadingDialog;
import com.aimowei.garbage.R;

public class ValidateActivity extends Activity {
	private AppContext mContext = AppContext.getInstance();
	private Context context;
	private LoadingDialog mDialog;
	private ImageView backward = null;
	private TextView mainName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.binding);
		context = ValidateActivity.this;
		mDialog = new LoadingDialog(context);
		backward = (ImageView) findViewById(R.id.left_ImageView);
		mainName = (TextView) findViewById(R.id.mainName);
		mainName.setText("");
		backward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	public void ButtonClicked(View v) {
		Intent intent;
		switch (v.getId()) {
		case (R.id.next):
		/*	intent = new Intent(context, ChangePassword.class);
			startActivity(intent);
			break;*/
		
		}

	}
}