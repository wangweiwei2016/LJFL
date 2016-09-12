package com.aimowei.UpLoad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.aimowei.common.Config;
import com.aimowei.garbage.R;
import com.aimowei.photo.activity.AlbumActivity;
import com.shang.photo.util.Bimp;
import com.shang.photo.util.FileUtils;
import com.shang.photo.util.ImageItem;

public class SelectImage<T> {
	private Context context;
	private Activity activity;
	public static Bitmap bimap ;
	public SelectImage(Context context, T activity)// 泛型参数
	{
		this.context = context;
		bimap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
		this.activity = (Activity) activity;
	}

	public void takePhoto() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		activity.startActivityForResult(openCameraIntent,  Config.TAKE_PICTURE);
	}

	public void choosePhoto() {
		Intent intent = new Intent(context, AlbumActivity.class);
		activity.startActivityForResult(intent, Config.CHOOSE_PICTURE_REQUESTCODE);
	}

	public String onActivityForTakePhotoReuslt(int requestCode, int resultCode, Intent data) {
		String picPath = "";
		if (Bimp.tempSelectBitmap.size() < 9 && resultCode == -1) {
			String fileName = String.valueOf(System.currentTimeMillis());
			Bitmap bm = (Bitmap) data.getExtras().get("data");
			FileUtils.saveBitmap(bm, fileName);
			picPath = FileUtils.SDPATH + fileName + ".JPEG";
		} else {
			return "";
		}
		return picPath;
	}

	public String onActivityForChoosePhotoReuslt(int requestCode, int resultCode, Intent data) {
		String picPath = "";
		if (requestCode == Config.CHOOSE_PICTURE_REQUESTCODE && resultCode == Config.CHOOSE_PICTURE_RESULTCODE) {
			int x = Bimp.tempSelectBitmap.size();
			if (x == 1) {
				picPath = ((ImageItem) (Bimp.tempSelectBitmap.get(0))).getImagePath();
				Bimp.tempSelectBitmap.clear();
				return picPath;
			}
		} else {
			return picPath;
		}
		return picPath;
	}

}
