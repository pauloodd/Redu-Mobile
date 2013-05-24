package br.com.redumobile.gui.clickablespan;

import br.com.redumobile.ReduMobile;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class BlueClickableSpan extends ClickableSpan {
	@Override
	public void onClick(View widget) {
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);

		ds.setUnderlineText(false);
		ds.setColor(ReduMobile.COLOR_BLUE_2);
	}
}