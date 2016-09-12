package com.aimowei.common;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aimowei.garbage.R;



public class SlidingSwitcherView extends RelativeLayout implements OnTouchListener {

	public SlidingSwitcherView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 鐠佲晞褰嶉崡鏇熺泊閸旑煉绱濋幍瀣瘹濠婃垵濮╅棁锟界憰浣芥彧閸掓壆娈戦柅鐔峰閵嗭拷
	 */
	public static final int SNAP_VELOCITY = 200;

	/**
	 * SlidingSwitcherView閻ㄥ嫬顔旀惔锔猴拷锟�
	 */
	private int switcherViewWidth;

	/**
	 * 瑜版挸澧犻弰鍓с仛閻ㄥ嫬鍘撶槐鐘垫畱娑撳鐖ｉ妴锟�
	 */
	private int currentItemIndex;

	/**
	 * 閼挎粌宕熸稉顓炲瘶閸氼偆娈戦崗鍐閹粯鏆熼妴锟�
	 */
	private int itemsCount;

	/**
	 * 閸氬嫪閲滈崗鍐閻ㄥ嫬浜哥粔鏄忕珶閻ｅ苯锟界锟斤拷
	 */
	private int[] borders;

	/**
	 * 閺堬拷婢舵艾褰叉禒銉︾拨閸斻劌鍩岄惃鍕箯鏉堝湱绱妴鍌氾拷鑲╂暠閼挎粌宕熸稉顓炲瘶閸氼偆娈戦崗鍐閹粯鏆熼弶銉ョ暰閿涘arginLeft閸掓媽鎻銈咃拷闂寸閸氬函绱濇稉宥堝厴閸愬秴鍣虹亸鎴欙拷锟�
	 * 
	 */
	private int leftEdge = 0;

	/**
	 * 閺堬拷婢舵艾褰叉禒銉︾拨閸斻劌鍩岄惃鍕礁鏉堝湱绱妴鍌氾拷鍏间航娑擄拷0閿涘arginLeft閸掓媽鎻銈咃拷闂寸閸氬函绱濇稉宥堝厴閸愬秴顤冮崝鐘拷锟�
	 */
	private int rightEdge = 0;

	/**
	 * 鐠佹澘缍嶉幍瀣瘹閹稿绗呴弮鍓佹畱濡亜娼楅弽鍥ワ拷锟�
	 */
	private float xDown;

	/**
	 * 鐠佹澘缍嶉幍瀣瘹缁夎濮╅弮鍓佹畱濡亜娼楅弽鍥ワ拷锟�
	 */
	private float xMove;

	/**
	 * 鐠佹澘缍嶉幍瀣簚閹额剝鎹ｉ弮鍓佹畱濡亜娼楅弽鍥ワ拷锟�
	 */
	private float xUp;

	/**
	 * 閼挎粌宕熺敮鍐ㄧ湰閵嗭拷
	 */
	private LinearLayout itemsLayout;

	/**
	 * 閺嶅洨顒风敮鍐ㄧ湰閵嗭拷
	 */
	private LinearLayout dotsLayout;

	/**
	 * 閼挎粌宕熸稉顓犳畱缁楊兛绔存稉顏勫帗缁辩姰锟斤拷
	 */
	private View firstItem;

	/**
	 * 閼挎粌宕熸稉顓狀儑娑擄拷娑擃亜鍘撶槐鐘垫畱鐢啫鐪敍宀�鏁ゆ禍搴㈡暭閸欐ΓeftMargin閻ㄥ嫬锟界》绱濋弶銉ュ枀鐎规艾缍嬮崜宥嗘▔缁�铏规畱閸濐亙绔存稉顏勫帗缁辩姰锟斤拷
	 */
	private MarginLayoutParams firstItemParams;

	/**
	 * 閻€劋绨拋锛勭暬閹靛瀵氬鎴濆З閻ㄥ嫰锟界喎瀹抽妴锟�
	 */
	private VelocityTracker mVelocityTracker;

	/**
	 * 闁插秴鍟揝lidingSwitcherView閻ㄥ嫭鐎柅鐘插毐閺佸府绱濋悽銊ょ艾閸忎浇顔忛崷鈺擬L娑擃厼绱╅悽銊ョ秼閸撳秶娈戦懛顏勭暰娑斿绔风仦锟介妴锟�
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlidingSwitcherView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingSwitcherView);
		boolean isAutoPlay = a.getBoolean(R.styleable.SlidingSwitcherView_auto_play, false);
		if (isAutoPlay) {
			startAutoPlay();
		}
		a.recycle();
	}

	/**
	 * 濠婃艾濮╅崚棰佺瑓娑擄拷娑擃亜鍘撶槐鐘拷锟�
	 */
	public void scrollToNext() {
		new ScrollTask().execute(-20);
	}

	/**
	 * 濠婃艾濮╅崚棰佺瑐娑擄拷娑擃亜鍘撶槐鐘拷锟�
	 */
	public void scrollToPrevious() {
		new ScrollTask().execute(20);
	}

	/**
	 * 濠婃艾濮╅崚鎵儑娑擄拷娑擃亜鍘撶槐鐘拷锟�
	 */
	public void scrollToFirstItem() {
		new ScrollToFirstItemTask().execute(20 * itemsCount);
	}

	/**
	 * 閻€劋绨崷銊ョ暰閺冭泛娅掕ぐ鎾茶厬閹垮秳缍擴I閻ｅ矂娼伴妴锟�
	 */
	private Handler handler = new Handler();

	/**
	 * 瀵拷閸氼垰娴橀悧鍥殰閸斻劍鎸遍弨鎯у閼虫枻绱濊ぐ鎾寸泊閸斻劌鍩岄張锟介崥搴濈瀵姴娴橀悧鍥╂畱閺冭泛锟芥瑱绱濇导姘冲殰閸斻劌娲栧姘煂缁楊兛绔村鐘叉禈閻楀洢锟斤拷
	 */
	public void startAutoPlay() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (currentItemIndex == itemsCount - 1) {
					currentItemIndex = 0;
					handler.post(new Runnable() {
						@Override
						public void run() {
							scrollToFirstItem();
							refreshDotsLayout();
						}
					});
				} else {
					currentItemIndex++;
					handler.post(new Runnable() {
						@Override
						public void run() {
							scrollToNext();
							refreshDotsLayout();
						}
					});
				}
			}
		}, 7000, 7000);
	}

	/**
	 * 閸︹暙nLayout娑擃參鍣搁弬鎷岊啎鐎规俺褰嶉崡鏇炲帗缁辩姴鎷伴弽鍥╊劮閸忓啰绀岄惃鍕棘閺佽埇锟斤拷
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			initializeItems();
			initializeDots();
		}
	}

	/**
	 * 閸掓繂顫愰崠鏍綅閸楁洖鍘撶槐鐙呯礉娑撶儤鐦℃稉锟芥稉顏勭摍閸忓啰绀屾晶鐐插閻╂垵鎯夋禍瀣╂閿涘苯鑻熸稉鏃�鏁奸崣妯诲閺堝鐡欓崗鍐閻ㄥ嫬顔旀惔锔肩礉鐠佲晛鐣犳禒顒傜搼娴滃海鍩楅崗鍐閻ㄥ嫬顔旀惔锔猴拷锟�
	 */
	private void initializeItems() {
		switcherViewWidth = getWidth();
		itemsLayout = (LinearLayout) getChildAt(0);
		itemsCount = itemsLayout.getChildCount();
		borders = new int[itemsCount];
		for (int i = 0; i < itemsCount; i++) {
			borders[i] = -i * switcherViewWidth;
			View item = itemsLayout.getChildAt(i);
			MarginLayoutParams params = (MarginLayoutParams) item.getLayoutParams();
			params.width = switcherViewWidth;
			item.setLayoutParams(params);
			item.setOnTouchListener(this);
		}
		leftEdge = borders[itemsCount - 1];
		firstItem = itemsLayout.getChildAt(0);
		firstItemParams = (MarginLayoutParams) firstItem.getLayoutParams();
	}

	/**
	 * 閸掓繂顫愰崠鏍ㄧ垼缁涙儳鍘撶槐鐘拷锟�
	 */
	private void initializeDots() {
		dotsLayout = (LinearLayout) getChildAt(1);
		refreshDotsLayout();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		createVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 閹靛瀵氶幐澶夌瑓閺冭绱濈拋鏉跨秿閹稿绗呴弮鍓佹畱濡亜娼楅弽锟�
			xDown = event.getRawX();
			break;
		case MotionEvent.ACTION_MOVE:
			// 閹靛瀵氱粔璇插З閺冭绱濈�佃鐦幐澶夌瑓閺冨墎娈戝Ο顏勬綏閺嶅浄绱濈拋锛勭暬閸戣櫣些閸斻劎娈戠捄婵堫瀲閿涘本娼电拫鍐╂殻瀹革缚鏅剁敮鍐ㄧ湰閻ㄥ埐eftMargin閸婄》绱濇禒搴わ拷灞炬▔缁�鍝勬嫲闂呮劘妫屽锔挎櫠鐢啫鐪�
			xMove = event.getRawX();
			int distanceX = (int) (xMove - xDown) - (currentItemIndex * switcherViewWidth);
			firstItemParams.leftMargin = distanceX;
			if (beAbleToScroll()) {
				firstItem.setLayoutParams(firstItemParams);
			}
			break;
		case MotionEvent.ACTION_UP:
			// 閹靛瀵氶幎顒冩崳閺冭绱濇潻娑滎攽閸掋倖鏌囪ぐ鎾冲閹靛濞嶉惃鍕壈閸ユ拝绱濇禒搴わ拷灞藉枀鐎规碍妲稿姘З閸掓澘涔忔笟褍绔风仦锟介敍宀冪箷閺勵垱绮撮崝銊ュ煂閸欏厖鏅剁敮鍐ㄧ湰
			xUp = event.getRawX();
			if (beAbleToScroll()) {
				if (wantScrollToPrevious()) {
					if (shouldScrollToPrevious()) {
						currentItemIndex--;
						scrollToPrevious();
						refreshDotsLayout();
					} else {
						scrollToNext();
					}
				} else if (wantScrollToNext()) {
					if (shouldScrollToNext()) {
						currentItemIndex++;
						scrollToNext();
						refreshDotsLayout();
					} else {
						scrollToPrevious();
					}
				}
			}
			recycleVelocityTracker();
			break;
		}
		return false;
	}

	/**
	 * 瑜版挸澧犻弰顖氭儊閼宠棄顧勫姘З閿涘本绮撮崝銊ュ煂缁楊兛绔存稉顏呭灗閺堬拷閸氬簼绔存稉顏勫帗缁辩姵妞傜亸鍡曠瑝閼宠棄鍟�濠婃艾濮╅妴锟�
	 * 
	 * @return 瑜版挸澧爈eftMargin閻ㄥ嫬锟界厧婀猯eftEdge閸滃ightEdge娑斿妫挎潻鏂挎礀true,閸氾箑鍨潻鏂挎礀false閵嗭拷
	 */
	private boolean beAbleToScroll() {
		return firstItemParams.leftMargin < rightEdge && firstItemParams.leftMargin > leftEdge;
	}

	/**
	 * 閸掋倖鏌囪ぐ鎾冲閹靛濞嶉惃鍕壈閸ョ偓妲告稉宥嗘Ц閹櫕绮撮崝銊ュ煂娑撳﹣绔存稉顏囧綅閸楁洖鍘撶槐鐘拷鍌氼洤閺嬫粍澧滈幐鍥┬╅崝銊ф畱鐠烘繄顬囬弰顖涱劀閺佸府绱濋崚娆掝吇娑撳搫缍嬮崜宥嗗閸旀寧妲搁幆瀹狀洣濠婃艾濮╅崚棰佺瑐娑擄拷娑擃亣褰嶉崡鏇炲帗缁辩姰锟斤拷
	 * 
	 * @return 瑜版挸澧犻幍瀣◢閹櫕绮撮崝銊ュ煂娑撳﹣绔存稉顏囧綅閸楁洖鍘撶槐鐘虹箲閸ョ�焤ue閿涘苯鎯侀崚娆掔箲閸ョ�巃lse閵嗭拷
	 */
	private boolean wantScrollToPrevious() {
		return xUp - xDown > 0;
	}

	/**
	 * 閸掋倖鏌囪ぐ鎾冲閹靛濞嶉惃鍕壈閸ョ偓妲告稉宥嗘Ц閹櫕绮撮崝銊ュ煂娑撳绔存稉顏囧綅閸楁洖鍘撶槐鐘拷鍌氼洤閺嬫粍澧滈幐鍥┬╅崝銊ф畱鐠烘繄顬囬弰顖濈閺佸府绱濋崚娆掝吇娑撳搫缍嬮崜宥嗗閸旀寧妲搁幆瀹狀洣濠婃艾濮╅崚棰佺瑓娑擄拷娑擃亣褰嶉崡鏇炲帗缁辩姰锟斤拷
	 * 
	 * @return 瑜版挸澧犻幍瀣◢閹櫕绮撮崝銊ュ煂娑撳绔存稉顏囧綅閸楁洖鍘撶槐鐘虹箲閸ョ�焤ue閿涘苯鎯侀崚娆掔箲閸ョ�巃lse閵嗭拷
	 */
	private boolean wantScrollToNext() {
		return xUp - xDown < 0;
	}

	/**
	 * 閸掋倖鏌囬弰顖氭儊鎼存棁顕氬姘З閸掗绗呮稉锟芥稉顏囧綅閸楁洖鍘撶槐鐘拷鍌氼洤閺嬫粍澧滈幐鍥┬╅崝銊ㄧ獩缁傝銇囨禍搴＄潌楠炴洜娈�1/2閿涘本鍨ㄩ懓鍛閹稿洨些閸斻劑锟界喎瀹虫径褌绨琒NAP_VELOCITY閿涳拷
	 * 鐏忚精顓绘稉鍝勭安鐠囥儲绮撮崝銊ュ煂娑撳绔存稉顏囧綅閸楁洖鍘撶槐鐘拷锟�
	 * 
	 * @return 婵″倹鐏夋惔鏃囶嚉濠婃艾濮╅崚棰佺瑓娑擄拷娑擃亣褰嶉崡鏇炲帗缁辩姾绻戦崶鐎焤ue閿涘苯鎯侀崚娆掔箲閸ョ�巃lse閵嗭拷
	 */
	private boolean shouldScrollToNext() {
		return xDown - xUp > switcherViewWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 閸掋倖鏌囬弰顖氭儊鎼存棁顕氬姘З閸掗绗傛稉锟芥稉顏囧綅閸楁洖鍘撶槐鐘拷鍌氼洤閺嬫粍澧滈幐鍥┬╅崝銊ㄧ獩缁傝銇囨禍搴＄潌楠炴洜娈�1/2閿涘本鍨ㄩ懓鍛閹稿洨些閸斻劑锟界喎瀹虫径褌绨琒NAP_VELOCITY閿涳拷
	 * 鐏忚精顓绘稉鍝勭安鐠囥儲绮撮崝銊ュ煂娑撳﹣绔存稉顏囧綅閸楁洖鍘撶槐鐘拷锟�
	 * 
	 * @return 婵″倹鐏夋惔鏃囶嚉濠婃艾濮╅崚棰佺瑐娑擄拷娑擃亣褰嶉崡鏇炲帗缁辩姾绻戦崶鐎焤ue閿涘苯鎯侀崚娆掔箲閸ョ�巃lse閵嗭拷
	 */
	private boolean shouldScrollToPrevious() {
		return xUp - xDown > switcherViewWidth / 2 || getScrollVelocity() > SNAP_VELOCITY;
	}

	/**
	 * 閸掗攱鏌婇弽鍥╊劮閸忓啰绀岀敮鍐ㄧ湰閿涘本鐦″▎顡﹗rrentItemIndex閸婂吋鏁奸崣妯兼畱閺冭泛锟芥瑩鍏樻惔鏃囶嚉鏉╂稖顢戦崚閿嬫煀閵嗭拷
	 */
	private void refreshDotsLayout() {
		if(dotsLayout==null)
			return;
		dotsLayout.removeAllViews();
		for (int i = 0; i < itemsCount; i++) {
			LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(0,
					android.view.ViewGroup.LayoutParams.FILL_PARENT);
			linearParams.weight = 1;
			RelativeLayout relativeLayout = new RelativeLayout(getContext());
			ImageView image = new ImageView(getContext());
			if (i == currentItemIndex) {
				image.setBackgroundResource(R.drawable.dot_selected);
			} else {
				image.setBackgroundResource(R.drawable.dot_unselected);
			}
			RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			relativeLayout.addView(image, relativeParams);
			dotsLayout.addView(relativeLayout, linearParams);
		}
	}

	/**
	 * 閸掓稑缂揤elocityTracker鐎电钖勯敍灞借嫙鐏忓棜袝閹介晲绨ㄦ禒璺哄閸忋儱鍩孷elocityTracker瑜版挷鑵戦妴锟�
	 * 
	 * @param event
	 *            閸欏厖鏅剁敮鍐ㄧ湰閻╂垵鎯夐幒褌娆㈤惃鍕拨閸斻劋绨ㄦ禒锟�
	 */
	private void createVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 閼惧嘲褰囬幍瀣瘹閸︺劌褰告笟褍绔风仦锟介惃鍕磧閸氱悤iew娑撳﹦娈戝鎴濆З闁喎瀹抽妴锟�
	 * 
	 * @return 濠婃垵濮╅柅鐔峰閿涘奔浜掑В蹇曨潡闁界喓些閸斻劋绨℃径姘毌閸嶅繒绀岄崐闂磋礋閸楁洑缍呴妴锟�
	 */
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return Math.abs(velocity);
	}

	/**
	 * 閸ョ偞鏁筕elocityTracker鐎电钖勯妴锟�
	 */
	private void recycleVelocityTracker() {
		mVelocityTracker.recycle();
		mVelocityTracker = null;
	}

	/**
	 * 濡拷濞村褰嶉崡鏇熺泊閸斻劍妞傞敍灞炬Ц閸氾附婀佺粚鑳Шborder閿涘異order閻ㄥ嫬锟藉ジ鍏樼�涙ê鍋嶉崷鈻凘link #borders}娑擃厹锟斤拷
	 * 
	 * @param leftMargin
	 *            缁楊兛绔存稉顏勫帗缁辩姷娈戝锕�浜哥粔璇诧拷锟�
	 * @param speed
	 *            濠婃艾濮╅惃鍕拷鐔峰閿涘本顒滈弫鎷岊嚛閺勫骸鎮滈崣铏泊閸旑煉绱濈拹鐔告殶鐠囧瓨妲戦崥鎴濅箯濠婃艾濮╅妴锟�
	 * @return 缁岃儻绉烘禒璁崇秿娑擄拷娑撶寵order娴滃棜绻戦崶鐎焤ue閿涘苯鎯侀崚娆掔箲閸ョ�巃lse閵嗭拷
	 */
	private boolean isCrossBorder(int leftMargin, int speed) {
		for (int border : borders) {
			if (speed > 0) {
				if (leftMargin >= border && leftMargin - speed < border) {
					return true;
				}
			} else {
				if (leftMargin <= border && leftMargin - speed > border) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 閹垫儳鍩岀粋璇茬秼閸撳秶娈憀eftMargin閺堬拷鏉╂垹娈戞稉锟芥稉鐚檕rder閸婄锟斤拷
	 * 
	 * @param leftMargin
	 *            缁楊兛绔存稉顏勫帗缁辩姷娈戝锕�浜哥粔璇诧拷锟�
	 * @return 缁傝缍嬮崜宥囨畱leftMargin閺堬拷鏉╂垹娈戞稉锟芥稉鐚檕rder閸婄锟斤拷
	 */
	private int findClosestBorder(int leftMargin) {
		int absLeftMargin = Math.abs(leftMargin);
		int closestBorder = borders[0];
		int closestMargin = Math.abs(Math.abs(closestBorder) - absLeftMargin);
		for (int border : borders) {
			int margin = Math.abs(Math.abs(border) - absLeftMargin);
			if (margin < closestMargin) {
				closestBorder = border;
				closestMargin = margin;
			}
		}
		return closestBorder;
	}

	class ScrollToFirstItemTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			int leftMargin = firstItemParams.leftMargin;
			while (true) {
				leftMargin = leftMargin + speed[0];
				// 瑜版悋eftMargin婢堆傜艾0閺冭绱濈拠瀛樻瀹歌尙绮″姘З閸掗绨＄粭顑跨娑擃亜鍘撶槐鐙呯礉鐠哄啿鍤顏嗗箚
				if (leftMargin > 0) {
					leftMargin = 0;
					break;
				}
				publishProgress(leftMargin);
				sleep(20);
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			firstItemParams.leftMargin = leftMargin[0];
			firstItem.setLayoutParams(firstItemParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			firstItemParams.leftMargin = leftMargin;
			firstItem.setLayoutParams(firstItemParams);
		}

	}

	class ScrollTask extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... speed) {
			if(firstItemParams==null)
				return 0;
			int leftMargin = firstItemParams.leftMargin;
			// 閺嶈宓佹导鐘插弳閻ㄥ嫰锟界喎瀹抽弶銉︾泊閸斻劎鏅棃顫礉瑜版挻绮撮崝銊р敍鐡掑ゲorder閺冭绱濈捄鍐插毉瀵邦亞骞嗛妴锟�
			while (true) {
				leftMargin = leftMargin + speed[0];
				if (isCrossBorder(leftMargin, speed[0])) {
					leftMargin = findClosestBorder(leftMargin);
					break;
				}
				publishProgress(leftMargin);
				// 娑撹桨绨＄憰浣规箒濠婃艾濮╅弫鍫熺亯娴溠呮晸閿涘本鐦″▎鈥虫儕閻滎垯濞囩痪璺ㄢ柤閻紕婀�10濮ｎ偆顫楅敍宀冪箹閺嶇柉鍊濋惇鍏煎閼宠棄顧勯惇瀣煂濠婃艾濮╅崝銊ф暰閵嗭拷
				sleep(10);
			}
			return leftMargin;
		}

		@Override
		protected void onProgressUpdate(Integer... leftMargin) {
			if(firstItemParams==null)
				return;
			firstItemParams.leftMargin = leftMargin[0];
			firstItem.setLayoutParams(firstItemParams);
		}

		@Override
		protected void onPostExecute(Integer leftMargin) {
			if(firstItemParams==null)
				return;
			firstItemParams.leftMargin = leftMargin;
			firstItem.setLayoutParams(firstItemParams);
		}
	}

	/**
	 * 娴ｅ灝缍嬮崜宥囧殠缁嬪娼惇鐘冲瘹鐎规氨娈戝В顐ゎ潡閺佽埇锟斤拷
	 * 
	 * @param millis
	 *            閹稿洤鐣捐ぐ鎾冲缁捐法鈻奸惈锛勬耿婢舵矮绠欓敍灞间簰濮ｎ偆顫楁稉鍝勫礋娴ｏ拷
	 */
	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
