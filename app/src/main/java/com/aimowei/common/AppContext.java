package com.aimowei.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.aimowei.model.User;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption.LocationMode;

public class AppContext {

	private static AppContext self;

	private SharedPreferences mPreferences;

	public Context mContext;

	public User currentUser;
	public final String USERNAME = "USERNAME";
	public final String APPNAME = "Garbage";
	public String CityName = "";
	public String TempData = "";
	public boolean ExceptionRecord = false;
	public LocationClient mLocationClient;
	public LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	public GeofenceClient mGeofenceClient;
	public String baseUrlString = "http://120.25.150.89/Mobile/";
	public String dataString = "";
	public String ID = "5";

	public static AppContext getInstance() {

		if (self == null) {
			self = new AppContext();
		}

		return self;
	}

	public static boolean isMobileNum(String mobiles) {
		Pattern p = Pattern.compile("^[1]+[1-9]+\\d{9}");
		Matcher m = p.matcher(mobiles);
		System.out.println(m.matches() + "---");
		return m.matches();

	}

	/**
	 * 閸掋倖鏌囬弰顖氭儊娑撹桨鑵戦弬鍥х摟缁楋拷
	 * 
	 * @param c
	 * @return
	 */
	public boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 閹跺﹣鑵戦弬鍥祮閹存�硁icode閻拷
	 * 
	 * @param str
	 * @return
	 */
	public String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 濮瑰鐡ч懠鍐ㄦ纯 \u4e00-\u9fa5
													// (娑擃厽鏋�)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	public AppContext() {
	}

	public String getUsername() {
		if (mPreferences == null)
			mPreferences = mContext.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

		return mPreferences.getString(USERNAME, null);
	}

	public void setUsername(String username) {

		if (mPreferences == null)
			mPreferences = mContext.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(USERNAME, username);
		editor.commit();
	}

	public String getUserID() {
		if (mPreferences == null)
			mPreferences = mContext.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

		return mPreferences.getString("ID", null);
	}

	public void setUserID(String id) {

		if (mPreferences == null)
			mPreferences = mContext.getSharedPreferences(APPNAME, Context.MODE_PRIVATE);

		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString("ID", id);
		editor.commit();
	}

	public void init(Context context) {

		mContext = context;
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);

	}

	public void initLocation(Context context) {
		mLocationClient = new LocationClient(context);

	}

	public boolean stopGetLocation() {
		mLocationClient.stop();
		return true;
	}

	public AppContext(Context context) {
		self = this;
	}

	/**
	 * 婵★拷?缁佺銇愰幘鑸电暠缂傚啯鍨圭划鍫曟晬閸︾AN闁碉拷?G/2G闁挎稑顦辨慨鎼佸箑?
	 * 
	 * @param context
	 *            Context
	 * @return true 閻炴稏鍔庨妵姘辩磾閹寸姷鎹曢柛娆樺灣閺侊拷
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 鐟滅増鎸告晶鐘电磾閹寸姷鎹曢柡鍕靛灥缁绘盯骞掗妷褎鐣�
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					// 鐟滅増鎸告晶鐘诲箥?缁绘盯骞掗妷褎鐣辩紓鍐╁灩缁爼宕ｉ婊勬殢
					return true;
				}
			}
		}
		return false;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public SharedPreferences getSharedPreferences() {
		return mPreferences;
	}

	public void setSharedPreferences(SharedPreferences sharedPreferences) {
		this.mPreferences = sharedPreferences;
	}

	public String convert(String time) {
		long mill = Long.parseLong(time) * 1000;
		Date date = new Date(mill);
		String strs = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			strs = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strs;
	}

	public static Bitmap decodeSampledBitmapFromResource(String res, int resId, int reqWidth, int reqHeight) {
		// 缁楊兛绔村▎陇袙閺嬫劕鐨nJustDecodeBounds鐠佸墽鐤嗘稉绨峳ue閿涘本娼甸懢宄板絿閸ュ墽澧栨径褍鐨�
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(res, options);
		// 鐠嬪啰鏁ゆ稉濠囨桨鐎规矮绠熼惃鍕煙濞夋洝顓哥粻姊歯SampleSize閸婏拷
		int init = calculateInSampleSize(options, reqWidth, reqHeight);
		options.inSampleSize = init < 2 ? 2 : init;
		// 娴ｈ法鏁ら懢宄板絿閸掓壆娈慽nSampleSize閸婄厧鍟�濞喡ば掗弸鎰禈閻楋拷
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(res, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 濠ф劕娴橀悧鍥╂畱妤傛ê瀹抽崪灞筋啍鎼达拷
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 鐠侊紕鐣婚崙鍝勭杽闂勫懎顔旀妯烘嫲閻╊喗鐖ｇ�逛粙鐝惃鍕槷閻滐拷
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 闁瀚ㄧ�硅棄鎷版妯硅厬閺堬拷鐏忓繒娈戝В鏃傚芳娴ｆ粈璐焛nSampleSize閻ㄥ嫬锟界》绱濇潻娆愮壉閸欘垯浜掓穱婵婄槈閺堬拷缂佸牆娴橀悧鍥╂畱鐎硅棄鎷版锟�
			// 娑擄拷鐎规岸鍏樻导姘亣娴滃海鐡戞禍搴ｆ窗閺嶅洨娈戠�硅棄鎷版妯革拷锟�
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
}
