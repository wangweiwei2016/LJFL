package com.aimowei.Activity.garbage.setting_function;

import android.os.Bundle;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.garbage.R;

public class AboutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		settitle("关于");
	}
}
