package br.com.redumobile.gui.clickablespan;

import android.graphics.Typeface;
import android.text.TextPaint;

public class BoldBlueClickableSpan extends BlueClickableSpan {
	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);

		ds.setTypeface(Typeface.create(ds.getTypeface(), Typeface.BOLD));
	}
}