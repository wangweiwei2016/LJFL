package com.aimowei.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.aimowei.garbage.R;

public class MainActivity extends Activity{
	public static MainActivity instance;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		instance=MainActivity.this;
	}


}
