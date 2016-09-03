package com.longuto.myswitch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyswitchView extends View {
	private Bitmap mBgBtm;	// 开关背景图
	private Bitmap mSlideBtm;	// 开关滑动图
	private boolean mIsOpen = true;	// 开关状态
	private int mSlideMax;	// 滑块图最大距离
	private int mSlideCurrent;	// 滑块图当前位置
	private OnMyChangedListen mListen;	// 监听事件
	
	
	public MyswitchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	public MyswitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	public MyswitchView(Context context) {
		super(context);
		initView();
	}

	/**
	 * 自定义状态改变接口类
	 */
	public interface OnMyChangedListen {
		public void myChanged(View v, boolean isOpen);
	}
	
	public void setOnMyChangedListen(OnMyChangedListen listen) {
		mListen = listen;
	}
	
	
	/**初始化视图*/
	private void initView() {
		mBgBtm = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);	// 初始化背景图
		mSlideBtm = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);	// 初始化滑块图
		mSlideMax = mBgBtm.getWidth() - mSlideBtm.getWidth();	// 滑动图片向右滑动的最大距离
		mSlideCurrent = mSlideMax;
		// 为自定义View设置点击事件
		setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("OnClick");
				if(isClick) {
					if(mIsOpen) {
						mIsOpen = false;	
						mSlideCurrent = 0;
						invalidate();	// 刷新View
					}else {
						mIsOpen = true;
						mSlideCurrent = mSlideMax;
						invalidate();	// 刷新View
					}
					if(mListen != null) {
						mListen.myChanged(MyswitchView.this, mIsOpen);
					}					
				}
			}
		});
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mBgBtm.getWidth(), mBgBtm.getHeight());	// 测量自定义控件的宽度和高度
	}
	
	/**画出布局*/
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mBgBtm, 0, 0, null);	// 画背景图
		canvas.drawBitmap(mSlideBtm, mSlideCurrent, 0, null);	// 画滑动图
	}
	
	private int startX;	// 起始点x的位置
	private int moveX;	// down -> up 事件x轴偏移量
	private boolean isClick;	// 是否点击事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("调用该方法");
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:	// 按下
			System.out.println("ACTION_DOWN");
			startX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:	// 移动
			System.out.println("ACTION_MOVE");
			int dx = (int)event.getX() - startX;	// x轴偏移量
			mSlideCurrent += dx;	// 计算当前滑块的位置
			if(mSlideCurrent > mSlideMax) {
				mSlideCurrent = mSlideMax;
			}else if(mSlideCurrent < 0){
				mSlideCurrent = 0;
			}
			invalidate();	// 刷新View
			startX = (int) event.getX();
			moveX += Math.abs(dx);
			break;
		case MotionEvent.ACTION_UP:	// 弹起	
			System.out.println("ACTION_UP");
			if(moveX < 5) {
				isClick = true;
			}else {
				isClick = false;
			}
			System.out.println("移动距离: " + moveX);
			moveX = 0;
			if(!isClick) {				
				if(mSlideCurrent >= mSlideMax/2) {
					mSlideCurrent = mSlideMax;
					mIsOpen = true;
				}else {
					mSlideCurrent = 0;
					mIsOpen = false;
				}
				if(mListen != null) {
					mListen.myChanged(MyswitchView.this, mIsOpen);
				}
				invalidate();
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}
	
}
