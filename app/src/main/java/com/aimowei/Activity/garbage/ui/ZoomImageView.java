package com.aimowei.Activity.garbage.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

public class ZoomImageView extends ImageView {

	public ZoomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private enum TouchMode {
		Drag, Zoom, Wait
	};

	private TouchMode touchMode = TouchMode.Wait;
	private float startDis = 0;// 距离
	private Matrix baseMatrix = new Matrix();// 原始的坐标变换矩阵
	private Matrix currentMatrix = new Matrix();// 当前的坐标变换矩阵
	// private Matrix newMatrix = new Matrix();// 新的坐标变换矩阵
	private PointF prePoint = new PointF();// 上次单击位置
	private float baseImageWidth = -1;
	private float baseImageHeight = -1;
	private RectF imageViewRect = new RectF();
	private RectF imageRect = new RectF();// 当前的image的Rect
	// private boolean firstTimeZoom = true;
	/** 最大缩放级别 */
	private float maxScale = 20;
	/** 最小缩放级别 */
	private float minScale = 0.05f;
	float[] values = new float[9];

	float dx = -1;
	float dy = -1;

	public void reset() {
		this.setScaleType(ImageView.ScaleType.CENTER_CROP);
	}

	//@SuppressLint("NewApi")
	private void init() {
		// 还原模式
		touchMode = (TouchMode.Wait);
		// 获取现在的矩阵(原始的)
		baseMatrix.set(this.getImageMatrix());
		// 获取原始的图片边框
		Rect baseImageRect = this.getDrawable().getBounds();
		// 计算原始的图片大小
		baseImageWidth = baseImageRect.right - baseImageRect.left;
		baseImageHeight = baseImageRect.bottom - baseImageRect.top;
		Log.e("ZoomImageView--基础矩阵baseMatrix", baseMatrix.toShortString());
		Log.e("ZoomImageView--基础图片尺寸", baseImageWidth + ":" + baseImageHeight);
		Rect tmpRect = new Rect();
		// 获取ImageView边框
		this.getDrawingRect(tmpRect);
		imageViewRect.set(tmpRect.left, tmpRect.top, tmpRect.right, tmpRect.bottom);
		Log.e("ZoomImageView--ImageView边框", imageViewRect.left+":"+imageViewRect.top+":"+imageViewRect.right+":"+imageViewRect.bottom);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// 调用父类函数
		super.setImageBitmap(bm);
		init();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		super.setImageDrawable(drawable);
		init();
	}

	@Override
	public void setImageResource(int resId) {
		// TODO Auto-generated method stub
		super.setImageResource(resId);
		init();
	}

	private static final int BufferMove = 5;
	private PointF scalePoint = new PointF();

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			touchMode = (TouchMode.Drag);
			this.setScaleType(ImageView.ScaleType.MATRIX);// 设置ImageView的缩放模式为矩阵缩放
			prePoint.set(event.getX(), event.getY());// 开始点
			return true;
		case MotionEvent.ACTION_MOVE:// 移动事件
			currentMatrix.set(this.getImageMatrix());
			if (touchMode == TouchMode.Drag) {// 图片拖动事件
				dx = event.getX() - prePoint.x;// x轴移动距离
				dy = event.getY() - prePoint.y;
				prePoint.set(event.getX(), event.getY());
				currentMatrix.getValues(values);
				imageRect.left = values[Matrix.MTRANS_X];
				imageRect.top = values[Matrix.MTRANS_Y];
				imageRect.right = imageRect.left + baseImageWidth
						* values[Matrix.MSCALE_X];
				imageRect.bottom = imageRect.top + baseImageHeight
						* values[Matrix.MSCALE_X];
				// Log.e("SWZ-imageRect=", imageRect.toShortString());
				if(!imageViewRect.contains(imageRect))
				{
				if (dx > 0)// 往右扯
				{
					if (imageRect.left - imageViewRect.left >= BufferMove)
						dx = 0;
				} else// 往左扯
				{
					if (imageViewRect.right - imageRect.right >= BufferMove)
						dx = 0;
				}
				if (dy > 0)// 往下扯
				{

					if (imageRect.top - imageViewRect.top >= BufferMove)
						dy = 0;
				} else// 往上扯
				{
					if (imageViewRect.bottom - imageRect.bottom >= BufferMove)
						dy = 0;
				}
				}
				currentMatrix.postTranslate(dx, dy);

			} else if (touchMode == TouchMode.Zoom) {// 图片放大事件
				if (event.getPointerCount() < 2)
					return true;
				float endDis = distanceOf2Point(event);// 结束距离
				if (endDis > 10f) {
					
					float scale = endDis / startDis;// 放大倍数
					startDis = endDis;
					currentMatrix.getValues(values);
					if ((scale > 1 && values[Matrix.MSCALE_X] >= maxScale )||( scale < 1 &&values[Matrix.MSCALE_X] <= minScale)) {
						Log.e("SWZ", "拉伸不合法");
						return true;
					}
					midPoint(scalePoint, event);
					currentMatrix.postScale(scale, scale, scalePoint.x,
							scalePoint.y);
				} else
					return true;
			}
			this.setImageMatrix(currentMatrix);
			return true;

		case MotionEvent.ACTION_UP:
			// 有手指离开屏幕，但屏幕还有触点(手指)
		case MotionEvent.ACTION_POINTER_UP:
			touchMode = (TouchMode.Wait);
			return true;
			// 当屏幕上已经有触点（手指）,再有一个手指压下屏幕
		case MotionEvent.ACTION_POINTER_DOWN:
			startDis = distanceOf2Point(event);

			touchMode = (TouchMode.Zoom);
			return true;

		}
		return super.onTouchEvent(event);
	}

	/**
	 * 重置Matrix
	 */
	/*
	 * private void reSetMatrix() { newMatrix.set(baseMatrix);
	 * setImageMatrix(newMatrix); }
	 */

	// 求两点距离
	private float distanceOf2Point(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	// 求两点间中点
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}
}
