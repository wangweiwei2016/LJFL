package com.aimowei.photo.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aimowei.common.AppContext;
import com.aimowei.common.NetOperator;
import com.aimowei.photoview.PhotoView;
import com.shang.photo.util.Bimp;
import com.shang.photo.util.ImageItem;
import com.shang.photo.util.PublicWay;
import com.shang.photo.util.Res;
import com.shang.photo.zoom.ViewPagerFixed;

/**
 * 杩欎釜鏄敤浜庤繘琛屽浘鐗囨祻瑙堟椂鐨勭晫锟�?
 * 
 * @author king
 * @QQ:595163260
 * @version 2014锟�?10锟�?18锟�? 涓嬪崍11:47:53
 */
public class GalleryActivity extends Activity {
	private Intent intent;
	// 杩斿洖鎸夐挳
	private Button back_bt;
	// 鍙戯拷?锟芥寜锟�?
	private Button send_bt;
	// 鍒犻櫎鎸夐挳
	private Button del_bt;
	// 椤堕儴鏄剧ず棰勮鍥剧墖浣嶇疆鐨則extview
	private TextView positionTextView;
	// 鑾峰彇鍓嶄竴涓猘ctivity浼犺繃鏉ョ殑position
	private int position;
	// 褰撳墠鐨勪綅锟�?
	private int location = 0;

	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	private AppContext appContext = AppContext.getInstance();
	private Context mContext;

	RelativeLayout photo_relativeLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_gallery"));// 鍒囧睆鍒颁富鐣岄潰
		PublicWay.activityList.add(this);
		mContext = this;
		back_bt = (Button) findViewById(Res.getWidgetID("gallery_back"));
		send_bt = (Button) findViewById(Res.getWidgetID("send_button"));
		del_bt = (Button) findViewById(Res.getWidgetID("gallery_del"));
		back_bt.setOnClickListener(new BackListener());
		send_bt.setOnClickListener(new GallerySendListener());
		del_bt.setOnClickListener(new DelListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = Integer.parseInt(intent.getStringExtra("position"));
		isShowOkBt();
		// 涓哄彂閫佹寜閽缃枃锟�?
		pager = (ViewPagerFixed) findViewById(Res.getWidgetID("gallery01"));
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			initListViews(Bimp.tempSelectBitmap.get(i).getBitmap());
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int) getResources().getDimensionPixelOffset(Res.getDimenID("ui_10_dip")));
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));	
		listViews.add(img);
	}

	// 杩斿洖鎸夐挳娣诲姞鐨勭洃鍚櫒
	private class BackListener implements OnClickListener {

		public void onClick(View v) {
			intent.setClass(GalleryActivity.this, ImageFile.class);
			startActivity(intent);
		}
	}

	// 鍒犻櫎鎸夐挳娣诲姞鐨勭洃鍚櫒
	private class DelListener implements OnClickListener {

		public void onClick(View v) {
			ImageItem ii = Bimp.tempSelectBitmap.get(location);
			if (!ii.getImageId().isEmpty()) {
				MyAsyncTaskRefresh task = new MyAsyncTaskRefresh();
				task.execute(ii.getImageId());
			}
			if (listViews.size() == 1) {
				Bimp.tempSelectBitmap.clear();
				Bimp.max = 0;
				send_bt.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				Intent intent = new Intent("data.broadcast.action");
				sendBroadcast(intent);
				finish();
			} else {
				Bimp.tempSelectBitmap.remove(location);
				Bimp.max--;
				pager.removeAllViews();
				listViews.remove(location);
				adapter.setListViews(listViews);
				send_bt.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
				adapter.notifyDataSetChanged();
			}
		}
	}

	class MyAsyncTaskRefresh extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {

			String result = "";
			NetOperator network = new NetOperator();
			List<NameValuePair> namePairValus = new ArrayList<NameValuePair>();

			namePairValus.add(new BasicNameValuePair("id", params[0]));
			String url = appContext.baseUrlString + "customShow/remove";
			result = network.PostRequest(url, namePairValus);

			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(String result) {

		}
	}

	// 瀹屾垚鎸夐挳鐨勭洃锟�?
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
			intent = getIntent();
			setResult(200, intent);
			finish();
		}

	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			send_bt.setText(Res.getString("finish") + "(" + Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			send_bt.setPressed(true);
			send_bt.setClickable(true);
			send_bt.setTextColor(Color.WHITE);
		} else {
			send_bt.setPressed(false);
			send_bt.setClickable(false);
			send_bt.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	/**
	 * 鐩戝惉杩斿洖鎸夐挳
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (position == 1) {
				this.finish();
				intent.setClass(GalleryActivity.this, AlbumActivity.class);
				startActivity(intent);
			} else if (position == 2) {
				this.finish();
				intent.setClass(GalleryActivity.this, ShowAllPhoto.class);
				startActivity(intent);
			}
		}
		return true;
	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
