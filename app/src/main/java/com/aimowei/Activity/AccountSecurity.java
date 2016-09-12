package com.aimowei.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimowei.garbage.R;

public class AccountSecurity extends Activity {
	private ImageView backward = null;
	private TextView mainName, right_text;
	private Context context=AccountSecurity.this;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_security);
		backward = (ImageView) (findViewById(R.id.left_ImageView));
		mainName = (TextView) (findViewById(R.id.mainName));
		right_text = (TextView) (findViewById(R.id.right_text));
		mainName.setText("个人主页");
		backward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// backward.setImageResource(R.drawable.publish);
		right_text.setText("");
	}

	public void ButtonClicked(View v) {
		Intent intent;
		switch (v.getId()) {
		case (R.id.phone):
			intent = new Intent(context, ValidateActivity.class);
			startActivity(intent);
			break;
		case (R.id.mail):
			intent = new Intent(context, ValidateActivity.class);
			startActivity(intent);
			break;
		}

	}
}
