package cn.yunluosoft.tonglou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

/**
 * @author Mu
 * @date 2015-5-21 下午12:42:52
 * @Description 
 */
public class MyGallery extends Gallery {

	public MyGallery(Context context) {
		super(context);
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int position = getSelectedItemPosition() % getChildCount();
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			// Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			// Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}

		return onKeyDown(kEvent, null);
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	protected boolean getChildStaticTransformation(View child, Transformation t) {
		return super.getChildStaticTransformation(child, t);
	}

	public boolean onTouchEvent(MotionEvent event) {
		boolean result;
		if (event.getAction() == MotionEvent.ACTION_UP) {
			requestLayout();
			invalidate();
		}
		try {
			result = super.onTouchEvent(event);
		} catch (NullPointerException e) {
			result = false;
		} catch (OutOfMemoryError ex) {
			ex.printStackTrace();
			result = false;
			System.gc();
			System.gc();
		}
		return result;
	}
}
