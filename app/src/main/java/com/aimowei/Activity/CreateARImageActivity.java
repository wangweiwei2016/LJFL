package com.aimowei.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.aimowei.common.CreateQRImage;
import com.aimowei.garbage.R;
import com.shang.Zxing.Demo.camera.CameraManager;

public class CreateARImageActivity  extends Activity  {
	private ImageView test_iv;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		//  CameraManager
		CameraManager.init(getApplication());
		test_iv = (ImageView) findViewById(R.id.test_iv);
		CreateQRImage create =new CreateQRImage();
		create.sweepIV=test_iv;
		create.createQRImage("http://www.baidu.com");
	}
}
