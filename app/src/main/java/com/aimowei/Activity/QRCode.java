package com.aimowei.Activity;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aimowei.common.AppContext;
import com.aimowei.common.CircleImageView;
import com.aimowei.garbage.R;
import com.aimowei.model.User;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
public class QRCode  extends Activity {
	private ImageView backward = null;
	private TextView mainName, right_text;
	private CircleImageView profile_image;
	private TextView name;
	private ImageView qr_code;
	private AppContext mContext = AppContext.getInstance();
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	@SuppressWarnings("deprecation")
	private DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.empty_photo) // 璁剧疆鍥剧墖涓嬭浇鏈熼棿鏄剧ず鐨勫浘鐗�
			.showImageForEmptyUri(R.drawable.empty_photo) // 璁剧疆鍥剧墖Uri涓虹┖鎴栨槸閿欒鐨勬椂鍊欐樉绀虹殑鍥剧�??
			.showImageOnFail(R.drawable.empty_photo) // 璁剧疆鍥剧墖鍔犺浇鎴栬В鐮佽繃绋嬩腑鍙戠敓閿欒鏄剧ず鐨勫浘鐗�??
			.cacheInMemory(true) // 璁剧疆涓嬭浇鐨勫浘鐗囨槸鍚︾紦�?�樺湪鍐呭瓨涓�
			.cacheOnDisc(true) // 璁剧疆涓嬭浇鐨勫浘鐗囨槸鍚︾紦�?�樺湪SD鍗�?�腑
			.build();
	User model;
	private Context context = QRCode.this;
	private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_qr_code);
		backward = (ImageView) (findViewById(R.id.left_ImageView));
		mainName = (TextView) (findViewById(R.id.mainName));
		right_text = (TextView) (findViewById(R.id.right_text));
		mainName.setText("我的二维�??");
		backward.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		/*Intent intent = getIntent();
		// 获取该Intent�??携带的数�??
		Bundle bundle = intent.getExtras();
		// 从bundle数据包中取出数据
		model = (User) bundle.getSerializable("detail");*/
		model=new User();
		model.setUname("刘洋");
		model.setAvatar_middle("http://img.blog.csdn.net/20130803190142484");
		right_text.setText("");
		profile_image=(CircleImageView)findViewById(R.id.profile_image);
		name=(TextView)findViewById(R.id.name);
		qr_code=(ImageView)findViewById(R.id.qr_code);
		name.setText(model.getUname());
		ImageLoader.getInstance().displayImage(model.getAvatar_middle(), profile_image, options, animateFirstListener);
		ImageLoader.getInstance().displayImage(model.getAvatar_middle(), qr_code, options, animateFirstListener);
	}
}
