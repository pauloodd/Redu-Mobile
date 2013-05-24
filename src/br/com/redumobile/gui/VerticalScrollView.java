package br.com.redumobile.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public final class VerticalScrollView extends ScrollView {
	public VerticalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VerticalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VerticalScrollView(Context context) {
		super(context);
	}

	/*@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN | MotionEvent.ACTION_CANCEL:
			super.onTouchEvent(ev);

			break;
		case MotionEvent.ACTION_MOVE:
			return false;
		case MotionEvent.ACTION_UP:
			return false;
		default:
			break;
		}

		return false;
	}*/

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		super.onTouchEvent(ev);

		return true;
	}
}
