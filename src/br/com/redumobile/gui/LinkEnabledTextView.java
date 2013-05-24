package br.com.redumobile.gui;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public final class LinkEnabledTextView extends TextView {
	private final class Hyperlink {
		private int end;
		private InternalUrlSpan span;
		private int start;
		private CharSequence textSpan;
	}

	private final class InternalUrlSpan extends ClickableSpan {
		private String clickedSpan;

		public InternalUrlSpan(String clickedString) {
			clickedSpan = clickedString;
		}

		@Override
		public void onClick(View textView) {
			listener.onTextLinkClick(textView, clickedSpan);
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);

			ds.setUnderlineText(false);
		}
	}

	public interface TextLinkClickListener {
		public void onTextLinkClick(View textView, String clickedString);
	}

	private final Pattern HYPERLINK_PATTERN = Pattern
			.compile("([Hh][tT][tT][pP][sS]?:\\/\\/[^ ,'\">\\]\\)]*[^\\. ,'\">\\]\\)])");
	private SpannableString linkableText;
	private ArrayList<Hyperlink> links;
	private TextLinkClickListener listener;

	public LinkEnabledTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		links = new ArrayList<Hyperlink>();
	}

	private final void gatherLinks(ArrayList<Hyperlink> links,
			Spannable spannable, Pattern pattern) {
		links.clear();

		Matcher matcher = pattern.matcher(spannable);
		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();

			Hyperlink spec = new Hyperlink();
			spec.textSpan = spannable.subSequence(start, end);
			spec.span = new InternalUrlSpan(spec.textSpan.toString());
			spec.start = start;
			spec.end = end;

			links.add(spec);
		}
	}

	public void gatherLinksForText(String text) {
		linkableText = new SpannableString(text);

		gatherLinks(links, linkableText, HYPERLINK_PATTERN);

		for (int i = 0; i < links.size(); i++) {
			Hyperlink linkSpec = links.get(i);

			linkableText.removeSpan(linkSpec);
			linkableText
					.setSpan(linkSpec.span, linkSpec.start, linkSpec.end, 0);
		}

		setText(linkableText);
	}

	public void setOnTextLinkClickListener(TextLinkClickListener listener) {
		this.listener = listener;
	}
}