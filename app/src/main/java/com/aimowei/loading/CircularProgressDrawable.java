package com.aimowei.loading;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

public class CircularProgressDrawable extends Drawable implements Animatable {
    
    /**
     * 缁樺埗鍦嗗姬璧峰浣嶇疆瑙掑害鐨勫姩鐢伙紝杩欐牱璇ュ渾寮ф槸鎵撳湀杞殑鍔ㄧ敾
     */
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    /**
     * 缁樺埗鍦嗗姬鑷傞暱鐨勫姩鐢伙紝璇ュ姩鐢诲彈 mModeAppearing 鎺у埗锛�
     * 褰� mModeAppearing 涓� false 鐨勬椂鍊欙紝鍦嗗姬鐨勮捣濮嬬偣鍦ㄥ鍔狅紝鍦嗗姬鐨勭粓姝㈢偣涓嶅彉锛屽姬闀垮湪閫愭笎鍑忓皯锛�
     * 褰� mModeAppearing 涓� true 鐨勬椂鍊欙紝 鍦嗗姬鐨勮捣濮嬬偣涓嶅彉锛屽渾寮х殑缁堟鐐瑰彉澶э紝寮ч暱鍦ㄩ�愭笎澧炲姞
     */
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    /**
     * 鍦嗗姬璧峰浣嶇疆鍔ㄧ敾鐨勯棿闅旓紝涔熷氨鏄灏戞绉掑渾寮ц浆涓�鍦堬紝鍙互鎶婅鍊兼墿澶�10鍊嶆潵鏌ョ湅鍔ㄧ敾鐨勬參鍔ㄤ綔
     */
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    /**
     * 鍦嗗姬鑷傞暱鐨勫姩鐢婚棿闅旓紝涔熷氨鏄噦闀夸粠鏈�灏忓埌鏈�澶у�肩殑鍙樺寲鏃堕棿锛屼篃鍙互鎶婅鍊兼墿澶�10鍊嶆潵鏌ョ湅鍔ㄧ敾鐨勬參鍔ㄤ綔
     */
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    /**
     * 鍦嗗姬鐨勬渶涓嬭噦闀挎槸澶氬皯搴�
     */
    private static final int MIN_SWEEP_ANGLE = 30;
    private final RectF fBounds = new RectF();

    /**
     * 璧峰浣嶇疆鐨勫姩鐢诲璞�
     */
    private ObjectAnimator mObjectAnimatorSweep;
    /**
     * 鑷傞暱鐨勫姩鐢诲璞�
     */
    private ObjectAnimator mObjectAnimatorAngle;
    /**
     * 鎺у埗鑷傞暱鏄�愭笎澧炲姞杩樻槸閫愭笎鍑忓皯
     */
    private boolean mModeAppearing;
    private Paint mPaint;
    /**
     * 姣忔鑷傞暱澧炲姞 銆佸噺灏� 杞崲鐨勬椂鍊欙紝 鍦嗗姬璧峰浣嶇疆鐨勫亸绉婚噺浼氬鍔� 2 鍊嶇殑鏈�灏忚噦闀�
     */
    private float mCurrentGlobalAngleOffset;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle;
    private float mBorderWidth;
    private boolean mRunning;

    public CircularProgressDrawable(int color, float borderWidth) {
        mBorderWidth = borderWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(color);

        setupAnimations();
    }

    @Override
    public void draw(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (mModeAppearing) {
            sweepAngle += MIN_SWEEP_ANGLE;
        } else {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    // ////////////////////////////////////////////////////////////////////////////
    // ////////////// Animation

    private Property<CircularProgressDrawable, Float> mAngleProperty = new Property<CircularProgressDrawable, Float>(Float.class, "angle") {
        @Override
        public Float get(CircularProgressDrawable object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(CircularProgressDrawable object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };

    private Property<CircularProgressDrawable, Float> mSweepProperty = new Property<CircularProgressDrawable, Float>(Float.class, "arc") {
        @Override
        public Float get(CircularProgressDrawable object) {
            return object.getCurrentSweepAngle();
        }

        @Override
        public void set(CircularProgressDrawable object, Float value) {
            object.setCurrentSweepAngle(value);
        }
    };

    private void setupAnimations() {
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);

        mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE * 2);
        mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        // 涓轰簡鏂逛究娴嬭瘯锛屽彲浠ユ敞閲婃帀涓嬮潰涓や釜鍔ㄧ敾涓殑涓�涓紝鏉�
        //鍒嗗埆鏌ョ湅姣忎釜鐙珛鐨勫姩鐢绘槸濡備綍杩愬姩鐨�
        mObjectAnimatorAngle.start();
        mObjectAnimatorSweep.start();
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        mObjectAnimatorSweep.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return mCurrentSweepAngle;
    }

}
