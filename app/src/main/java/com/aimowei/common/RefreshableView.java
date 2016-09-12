package com.aimowei.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aimowei.garbage.R;

/**
 * 閸欘垵绻樼悰灞肩瑓閹峰鍩涢弬鎵畱閼奉亜鐣炬稊澶嬪付娴犺翰锟斤拷
 * 
 * @author 
 * 
 */
public class RefreshableView extends LinearLayout implements OnTouchListener {

	/**
	 * 娑撳濯洪悩鑸碉拷锟�
	 */
	public static final int STATUS_PULL_TO_REFRESH = 0;

	/**
	 * 闁插﹥鏂佺粩瀣祮閸掗攱鏌婇悩鑸碉拷锟�
	 */
	public static final int STATUS_RELEASE_TO_REFRESH = 1;

	/**
	 * 濮濓絽婀崚閿嬫煀閻樿埖锟斤拷
	 */
	public static final int STATUS_REFRESHING = 2;

	/**
	 * 閸掗攱鏌婄�瑰本鍨氶幋鏍ㄦ弓閸掗攱鏌婇悩鑸碉拷锟�
	 */
	public static final int STATUS_REFRESH_FINISHED = 3;

	/**
	 * 娑撳濯烘径鎾劥閸ョ偞绮撮惃鍕拷鐔峰
	 */
	public static final int SCROLL_SPEED = -20;

	/**
	 * 娑擄拷閸掑棝鎸撻惃鍕嚑缁夋帒锟界》绱濋悽銊ょ艾閸掋倖鏌囨稉濠冾偧閻ㄥ嫭娲块弬鐗堟闂傦拷
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * 娑擄拷鐏忓繑妞傞惃鍕嚑缁夋帒锟界》绱濋悽銊ょ艾閸掋倖鏌囨稉濠冾偧閻ㄥ嫭娲块弬鐗堟闂傦拷
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * 娑擄拷婢垛晝娈戝В顐ゎ潡閸婄》绱濋悽銊ょ艾閸掋倖鏌囨稉濠冾偧閻ㄥ嫭娲块弬鐗堟闂傦拷
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * 娑擄拷閺堝牏娈戝В顐ゎ潡閸婄》绱濋悽銊ょ艾閸掋倖鏌囨稉濠冾偧閻ㄥ嫭娲块弬鐗堟闂傦拷
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * 娑擄拷楠炲娈戝В顐ゎ潡閸婄》绱濋悽銊ょ艾閸掋倖鏌囨稉濠冾偧閻ㄥ嫭娲块弬鐗堟闂傦拷
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/**
	 * 娑撳﹥顐奸弴瀛樻煀閺冨爼妫块惃鍕摟缁楋缚瑕嗙敮鎼佸櫤閿涘瞼鏁ゆ禍搴濈稊娑撶癄haredPreferences閻ㄥ嫰鏁崐锟�
	 */
	private static final String UPDATED_AT = "updated_at";

	/**
	 * 娑撳濯洪崚閿嬫煀閻ㄥ嫬娲栫拫鍐╁复閸欙拷
	 */
	private PullToRefreshListener mListener;

	/**
	 * 閻€劋绨�涙ê鍋嶆稉濠冾偧閺囧瓨鏌婇弮鍫曟？
	 */
	private SharedPreferences preferences;

	/**
	 * 娑撳濯烘径瀵告畱View
	 */
	private View header;

	/**
	 * 闂囷拷鐟曚礁骞撴稉瀣閸掗攱鏌婇惃鍑﹊stView
	 */
	private ListView listView;

	/**
	 * 閸掗攱鏌婇弮鑸垫▔缁�铏规畱鏉╂稑瀹抽弶锟�
	 */
	private ProgressBar progressBar;

	/**
	 * 閹稿洨銇氭稉瀣閸滃矂鍣撮弨鍓ф畱缁狀厼銇�
	 */
	private ImageView arrow;

	/**
	 * 閹稿洨銇氭稉瀣閸滃矂鍣撮弨鍓ф畱閺傚洤鐡ч幓蹇氬牚
	 */
	private TextView description;

	/**
	 * 娑撳﹥顐奸弴瀛樻煀閺冨爼妫块惃鍕瀮鐎涙寮挎潻锟�
	 */
	private TextView updateAt;

	/**
	 * 娑撳濯烘径瀵告畱鐢啫鐪崣鍌涙殶
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * 娑撳﹥顐奸弴瀛樻煀閺冨爼妫块惃鍕嚑缁夋帒锟斤拷
	 */
	private long lastUpdateTime;

	/**
	 * 娑撹桨绨￠梼鍙夘剾娑撳秴鎮撻悾宀勬桨閻ㄥ嫪绗呴幏澶婂煕閺傛澘婀稉濠冾偧閺囧瓨鏌婇弮鍫曟？娑撳﹣绨伴惄鍛婃箒閸愯尙鐛婇敍灞煎▏閻⑩暐d閺夈儱浠涢崠鍝勫瀻
	 */
	private int mId = -1;

	/**
	 * 娑撳濯烘径瀵告畱妤傛ê瀹�
	 */
	private int hideHeaderHeight;

	/**
	 * 瑜版挸澧犳径鍕倞娴狅拷娑斿牏濮搁幀渚婄礉閸欘垶锟藉锟藉吋婀丼TATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING 閸滐拷 STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = STATUS_REFRESH_FINISHED;;

	/**
	 * 鐠佹澘缍嶆稉濠佺濞嗭紕娈戦悩鑸碉拷浣规Ц娴狅拷娑斿牞绱濋柆鍨帳鏉╂稖顢戦柌宥咁槻閹垮秳缍�
	 */
	private int lastStatus = currentStatus;

	/**
	 * 閹靛瀵氶幐澶夌瑓閺冨墎娈戠仦蹇撶缁鹃潧娼楅弽锟�
	 */
	private float yDown;

	/**
	 * 閸︺劏顫﹂崚銈呯暰娑撶儤绮撮崝銊ょ閸撳秶鏁ら幋閿嬪閹稿洤褰叉禒銉╅崝銊ф畱閺堬拷婢堆冿拷绗猴拷锟�
	 */
	private int touchSlop;

	/**
	 * 閺勵垰鎯佸鎻掑鏉炲�熺箖娑擄拷濞嗩摬ayout閿涘矁绻栭柌瀹眓Layout娑擃厾娈戦崚婵嗩潗閸栨牕褰ч棁锟介崝鐘烘祰娑擄拷濞嗭拷
	 */
	private boolean loadOnce;

	/**
	 * 瑜版挸澧犻弰顖氭儊閸欘垯浜掓稉瀣閿涘苯褰ч張濉媔stView濠婃艾濮╅崚鏉裤仈閻ㄥ嫭妞傞崐娆愬閸忎浇顔忔稉瀣
	 */
	private boolean ableToPull;

	/**
	 * 娑撳濯洪崚閿嬫煀閹貉傛閻ㄥ嫭鐎柅鐘插毐閺佸府绱濇导姘躬鏉╂劘顢戦弮璺哄З閹焦鍧婇崝鐘辩娑擃亙绗呴幏澶娿仈閻ㄥ嫬绔风仦锟介妴锟�
	 * 
	 * @param context
	 * @param attrs
	 */
	@SuppressLint("Instantiatable")
	public RefreshableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		header = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh, null, true);
		progressBar = (ProgressBar) header.findViewById(R.id.progress_bar);
		arrow = (ImageView) header.findViewById(R.id.arrow);
		description = (TextView) header.findViewById(R.id.description);
		updateAt = (TextView) header.findViewById(R.id.updated_at);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		refreshUpdatedAtValue();
		setOrientation(VERTICAL);
		addView(header, 0);
	}

	/**
	 * 鏉╂稖顢戞稉锟芥禍娑樺彠闁款喗锟窖呮畱閸掓繂顫愰崠鏍ㄦ惙娴ｆ粣绱濆В鏂款洤閿涙艾鐨㈡稉瀣婢舵潙鎮滄稉濠備焊缁夋槒绻樼悰宀勬閽樺骏绱濈紒姗猧stView濞夈劌鍞絫ouch娴滃娆㈤妴锟�
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			hideHeaderHeight = -header.getHeight();
			headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
			headerLayoutParams.topMargin = hideHeaderHeight;
			listView = (ListView) getChildAt(1);
			listView.setOnTouchListener(this);
			loadOnce = true;
			new HideHeaderTaskInstant().execute();

		}
		
	}

	/**
	 * 瑜版彆istView鐞氼偉袝閹藉憡妞傜拫鍐暏閿涘苯鍙炬稉顓烆槱閻炲棔绨￠崥鍕潚娑撳濯洪崚閿嬫煀閻ㄥ嫬鍙挎担鎾伙拷鏄忕帆閵嗭拷
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		setIsAbleToPull(event);
		if (ableToPull) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float yMove = event.getRawY();
				int distance = (int) (yMove - yDown);
				// 婵″倹鐏夐幍瀣瘹閺勵垯绗呭鎴犲Ц閹緤绱濋獮鏈电瑬娑撳濯烘径瀛樻Ц鐎瑰苯鍙忛梾鎰閻ㄥ嫸绱濈亸鍗炵潌閽勬垝绗呴幏澶夌皑娴狅拷
				if (distance <= 0 && headerLayoutParams.topMargin <= hideHeaderHeight) {
					return false;
				}
				if (distance < touchSlop) {
					return false;
				}
				if (currentStatus != STATUS_REFRESHING) {
					if (headerLayoutParams.topMargin > 0) {
						currentStatus = STATUS_RELEASE_TO_REFRESH;
					} else {
						currentStatus = STATUS_PULL_TO_REFRESH;
					}
					// 闁俺绻冮崑蹇曅╂稉瀣婢跺娈憈opMargin閸婄》绱濋弶銉ョ杽閻滈绗呴幏澶嬫櫏閺嬶拷
					headerLayoutParams.topMargin = (distance / 2) + hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				break;
			case MotionEvent.ACTION_UP:
			default:
				if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
					// 閺夌偓澧滈弮璺侯洤閺嬫粍妲搁柌濠冩杹缁斿宓嗛崚閿嬫煀閻樿埖锟戒緤绱濈亸鍗炲箵鐠嬪啰鏁ゅ锝呮躬閸掗攱鏌婇惃鍕崲閸旓拷
					new RefreshingTask().execute();
				} else if (currentStatus == STATUS_PULL_TO_REFRESH) {
					// 閺夌偓澧滈弮璺侯洤閺嬫粍妲告稉瀣閻樿埖锟戒緤绱濈亸鍗炲箵鐠嬪啰鏁ら梾鎰娑撳濯烘径瀵告畱娴犺濮�
					new HideHeaderTask().execute();
				}
				break;
			}
			// 閺冭泛鍩㈢拋鏉跨繁閺囧瓨鏌婃稉瀣婢剁繝鑵戦惃鍕繆閹拷
			if (currentStatus == STATUS_PULL_TO_REFRESH
					|| currentStatus == STATUS_RELEASE_TO_REFRESH) {
				updateHeaderView();
				// 瑜版挸澧犲锝咁槱娴滃簼绗呴幏澶嬪灗闁插﹥鏂侀悩鑸碉拷渚婄礉鐟曚浇顔�ListView婢跺崬骞撻悞锔惧仯閿涘苯鎯侀崚娆掝潶閻愮懓鍤惃鍕亝娑擄拷妞ら�涚窗娑擄拷閻╂潙顦╂禍搴拷澶夎厬閻樿埖锟斤拷
				listView.setPressed(false);
				listView.setFocusable(false);
				listView.setFocusableInTouchMode(false);
				lastStatus = currentStatus;
				// 瑜版挸澧犲锝咁槱娴滃簼绗呴幏澶嬪灗闁插﹥鏂侀悩鑸碉拷渚婄礉闁俺绻冩潻鏂挎礀true鐏炲繗鏂�閹哄istView閻ㄥ嫭绮撮崝銊ょ皑娴狅拷
				return true;
			}
		}
		return false;
	}

	/**
	 * 缂佹瑤绗呴幏澶婂煕閺傜増甯舵禒鑸垫暈閸愬奔绔存稉顏嗘磧閸氼剙娅掗妴锟�
	 * 
	 * @param listener
	 *            閻╂垵鎯夐崳銊ф畱鐎圭偟骞囬妴锟�
	 * @param id
	 *            娑撹桨绨￠梼鍙夘剾娑撳秴鎮撻悾宀勬桨閻ㄥ嫪绗呴幏澶婂煕閺傛澘婀稉濠冾偧閺囧瓨鏌婇弮鍫曟？娑撳﹣绨伴惄鍛婃箒閸愯尙鐛婇敍锟� 鐠囪渹绗夐崥宀�鏅棃銏犳躬濞夈劌鍞芥稉瀣閸掗攱鏌婇惄鎴濇儔閸ｃ劍妞傛稉锟界�规俺顩︽导鐘插弳娑撳秴鎮撻惃鍒琩閵嗭拷
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * 瑜版挻澧嶉張澶屾畱閸掗攱鏌婇柅鏄忕帆鐎瑰本鍨氶崥搴礉鐠佹澘缍嶇拫鍐暏娑擄拷娑撳绱濋崥锕�鍨担鐘垫畱ListView鐏忓棔绔撮惄鏉戭槱娴滃孩顒滈崷銊ュ煕閺傛壆濮搁幀浣碉拷锟�
	 */
	public void finishRefreshing() {
		currentStatus = STATUS_REFRESH_FINISHED;
		preferences.edit().putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
		new HideHeaderTask().execute();
	}

	/**
	 * 閺嶈宓佽ぐ鎾冲ListView閻ㄥ嫭绮撮崝銊уЦ閹焦娼电拋鎯х暰 {@link #ableToPull}
	 * 閻ㄥ嫬锟界》绱濆В蹇旑偧闁粙娓剁憰浣告躬onTouch娑擃厾顑囨稉锟芥稉顏呭⒔鐞涘矉绱濇潻娆愮壉閸欘垯浜掗崚銈嗘焽閸戝搫缍嬮崜宥呯安鐠囥儲妲稿姘ЗListView閿涘矁绻曢弰顖氱安鐠囥儴绻樼悰灞肩瑓閹峰锟斤拷
	 * 
	 * @param event
	 */
	private void setIsAbleToPull(MotionEvent event) {
		View firstChild = listView.getChildAt(0);
		if (firstChild != null) {
			int firstVisiblePos = listView.getFirstVisiblePosition();
			if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
				if (!ableToPull) {
					yDown = event.getRawY();
				}
				// 婵″倹鐏夋＃鏍﹂嚋閸忓啰绀岄惃鍕瑐鏉堝湱绱敍宀冪獩缁傝崵鍩楃敮鍐ㄧ湰閸婇棿璐�0閿涘苯姘ㄧ拠瀛樻ListView濠婃艾濮╅崚棰佺啊閺堬拷妞ゅ爼鍎撮敍灞绢劃閺冭泛绨茬拠銉ュ帒鐠侀晲绗呴幏澶婂煕閺傦拷
				ableToPull = true;
			} else {
				if (headerLayoutParams.topMargin != hideHeaderHeight) {
					headerLayoutParams.topMargin = hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
				}
				ableToPull = false;
			}
		} else {
			// 婵″倹鐏塋istView娑擃厽鐥呴張澶婂帗缁辩媴绱濇稊鐔风安鐠囥儱鍘戠拋闀愮瑓閹峰鍩涢弬锟�
			ableToPull = true;
		}
	}

	/**
	 * 閺囧瓨鏌婃稉瀣婢剁繝鑵戦惃鍕繆閹垬锟斤拷
	 */
	private void updateHeaderView() {
		if (lastStatus != currentStatus) {
			if (currentStatus == STATUS_PULL_TO_REFRESH) {
				description.setText(getResources().getString(R.string.pull_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
				description.setText(getResources().getString(R.string.release_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == STATUS_REFRESHING) {
				description.setText(getResources().getString(R.string.refreshing));
				progressBar.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
			}
			refreshUpdatedAtValue();
		}
	}

	/**
	 * 閺嶈宓佽ぐ鎾冲閻ㄥ嫮濮搁幀浣规降閺冨娴嗙粻顓炪仈閵嗭拷
	 */
	private void rotateArrow() {
		float pivotX = arrow.getWidth() / 2f;
		float pivotY = arrow.getHeight() / 2f;
		float fromDegrees = 0f;
		float toDegrees = 0f;
		if (currentStatus == STATUS_PULL_TO_REFRESH) {
			fromDegrees = 180f;
			toDegrees = 360f;
		} else if (currentStatus == STATUS_RELEASE_TO_REFRESH) {
			fromDegrees = 0f;
			toDegrees = 180f;
		}
		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees, pivotX, pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		arrow.startAnimation(animation);
	}

	/**
	 * 閸掗攱鏌婃稉瀣婢剁繝鑵戞稉濠冾偧閺囧瓨鏌婇弮鍫曟？閻ㄥ嫭鏋冪�涙寮挎潻鑸拷锟�
	 */
	private void refreshUpdatedAtValue() {
		lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = getResources().getString(R.string.not_updated_yet);
		} else if (timePassed < 0) {
			updateAtValue = getResources().getString(R.string.time_error);
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = getResources().getString(R.string.updated_just_now);
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "閸掑棝鎸�";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "鐏忓繑妞�";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "婢讹拷";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "娑擃亝婀�";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "楠烇拷";
			updateAtValue = String.format(getResources().getString(R.string.updated_at), value);
		}
		updateAt.setText(updateAtValue);
	}

	/**
	 * 濮濓絽婀崚閿嬫煀閻ㄥ嫪鎹㈤崝鈽呯礉閸︺劍顒濇禒璇插娑擃厺绱伴崢璇叉礀鐠嬪啯鏁為崘宀冪箻閺夈儳娈戞稉瀣閸掗攱鏌婇惄鎴濇儔閸ｃ劊锟斤拷
	 * 
	 * @author guolin
	 */
	class RefreshingTask extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= 0) {
					topMargin = 0;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			currentStatus = STATUS_REFRESHING;
			publishProgress(0);
			if (mListener != null) {
				mListener.onRefresh();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			updateHeaderView();
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

	}

	/**
	 * 闂呮劘妫屾稉瀣婢跺娈戞禒璇插閿涘苯缍嬮張顏囩箻鐞涘奔绗呴幏澶婂煕閺傜増鍨ㄦ稉瀣閸掗攱鏌婄�瑰本鍨氶崥搴礉濮濄倓鎹㈤崝鈥崇殺娴兼矮濞囨稉瀣婢舵挳鍣搁弬浼存閽樺繈锟斤拷
	 * 
	 * @author guolin
	 */
	class HideHeaderTask extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= hideHeaderHeight) {
					topMargin = hideHeaderHeight;
					break;
				}
				publishProgress(topMargin);
				sleep(10);
			}
			return topMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer topMargin) {
			headerLayoutParams.topMargin = topMargin;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = STATUS_REFRESH_FINISHED;
		}
	}
	class HideHeaderTaskInstant extends AsyncTask<Void, Integer, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			int topMargin = headerLayoutParams.topMargin;
			while (true) {
				topMargin = topMargin + SCROLL_SPEED;
				if (topMargin <= hideHeaderHeight) {
					topMargin = hideHeaderHeight;
					break;
				}
				publishProgress(topMargin);
			
			}
			return topMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... topMargin) {
			headerLayoutParams.topMargin = topMargin[0];
			header.setLayoutParams(headerLayoutParams);
		}

		@Override
		protected void onPostExecute(Integer topMargin) {
			headerLayoutParams.topMargin = topMargin;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = STATUS_REFRESH_FINISHED;
		}
	}
	/**
	 * 娴ｅ灝缍嬮崜宥囧殠缁嬪娼惇鐘冲瘹鐎规氨娈戝В顐ゎ潡閺佽埇锟斤拷
	 * 
	 * @param time
	 *            閹稿洤鐣捐ぐ鎾冲缁捐法鈻奸惈锛勬耿婢舵矮绠欓敍灞间簰濮ｎ偆顫楁稉鍝勫礋娴ｏ拷
	 */
	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 娑撳濯洪崚閿嬫煀閻ㄥ嫮娲冮崥顒�娅掗敍灞煎▏閻€劋绗呴幏澶婂煕閺傛壆娈戦崷鐗堟煙鎼存棁顕氬▔銊ュ斀濮濄倗娲冮崥顒�娅掗弶銉ㄥ箯閸欐牕鍩涢弬鏉挎礀鐠嬪啨锟斤拷
	 * 
	 * @author guolin
	 */
	public interface PullToRefreshListener {

		/**
		 * 閸掗攱鏌婇弮鏈电窗閸樿娲栫拫鍐╊劃閺傝纭堕敍灞芥躬閺傝纭堕崘鍛椽閸愭瑥鍙挎担鎾舵畱閸掗攱鏌婇柅鏄忕帆閵嗗倹鏁為幇蹇旑劃閺傝纭堕弰顖氭躬鐎涙劗鍤庣粙瀣╄厬鐠嬪啰鏁ら惃鍕剁礉 娴ｇ姴褰叉禒銉ょ瑝韫囧懎褰熷锟界痪璺ㄢ柤閺夈儴绻樼悰宀冿拷妤佹閹垮秳缍旈妴锟�
		 */
		void onRefresh();

	}

}
