package com.aimowei.Activity.garbage.mycenter_function;

import java.io.UnsupportedEncodingException;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimowei.Activity.garbage.BaseActivity;
import com.aimowei.Activity.garbage.GlobalParams;
import com.aimowei.Activity.garbage.utils.DensityUtil;
import com.aimowei.Activity.garbage.zxing.encoding.EncodingHandler;
import com.aimowei.garbage.R;

import com.google.zxing.WriterException;

public class MyTwoDimensionActivity extends BaseActivity {
	/**
	 * 二维码图片
	 */
	private ImageView qrImgImageView;
	/**
	 * 用户全称
	 */
	private TextView tv_yonghu;

	@TargetApi(19)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two_dimension);
		settitle("我的二维码");
		tv_yonghu = (TextView) findViewById(R.id.tv_yonghu);
		qrImgImageView = (ImageView) this.findViewById(R.id.iv_qr_image);
		String contentString = "";
		final Resources r = getResources();
		if (sp.getString("yonghuleibie", "").equals("1")
				&& sp.getString("SQYHMC", "") != "") {
			// 用户登录
			contentString = sp.getString("SQYHMC", "");
			tv_yonghu.setText("用户名：" + sp.getString("SQYHMC", ""));
		} else if (sp.getString("yonghuleibie", "").equals("3")
				&& sp.getString("SQYHMC", "") != "") {
			//商户登录
			contentString = sp.getString("SQYHMC", "");
			tv_yonghu.setText("商户名：" + sp.getString("SQYHMC", ""));
		} else {
			contentString = "非社区用户";
			tv_yonghu.setText("非社区用户");
		}
		// 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
		Bitmap qrCodeBitmap = null;
		try {
			float px1 = DensityUtil.sp2px(getApplicationContext(), 350);
			qrCodeBitmap = EncodingHandler.createQRCode(contentString,
					(int) px1);
			// ------------------添加logo部分------------------//
			Bitmap logoBmp = BitmapFactory
					.decodeResource(r, R.drawable.erweima);

			// 二维码和logo合并
			Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(),
					qrCodeBitmap.getHeight(), qrCodeBitmap.getConfig());
			Canvas canvas = new Canvas(bitmap);
			// 二维码
			canvas.drawBitmap(qrCodeBitmap, 0, 0, null);
			// logo绘制在二维码中央
			canvas.drawBitmap(logoBmp,
					qrCodeBitmap.getWidth() / 2 - logoBmp.getWidth() / 2,
					qrCodeBitmap.getHeight() / 2 - logoBmp.getHeight() / 2,
					null);
			// ------------------添加logo部分------------------//

			qrImgImageView.setImageBitmap(bitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// qrImgImageView.setImageBitmap(qrCodeBitmap);

	}
}
