package br.com.redumobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.redumobile.R;
import br.com.redumobile.entity.Answer;
import br.com.redumobile.entity.User;
import br.com.redumobile.gui.LinkEnabledTextView;
import br.com.redumobile.gui.UserWallActivity;
import br.com.redumobile.gui.LinkEnabledTextView.TextLinkClickListener;
import br.com.redumobile.util.BitmapManager;
import br.com.redumobile.util.DateFormatter;

public final class AnswerAdapter extends BaseAdapter {
	private final class AnswerViewHolder {
		private ImageView imgUserThumb;
		private TextView lblCreationDate;
		private LinkEnabledTextView lblText;
		private TextView lblUserFullName;
	}

	private ArrayList<Answer> answers;
	private BitmapManager bitmapManager;
	private Context context;
	private final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
	private DateFormatter dateFormatter;
	private LayoutInflater inflater;

	public AnswerAdapter(Context context, ArrayList<Answer> answers) {
		this.context = context;

		this.answers = answers;

		bitmapManager = new BitmapManager();

		dateFormatter = new DateFormatter(DATE_FORMAT);

		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return answers.size();
	}

	@Override
	public Object getItem(int position) {
		return answers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AnswerViewHolder holder;

		View v;
		if (convertView == null) {
			ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.answer,
					null);

			holder = new AnswerViewHolder();
			holder.imgUserThumb = (ImageView) viewGroup
					.findViewById(R.id.answerImgUserThumb);
			holder.lblCreationDate = (TextView) viewGroup
					.findViewById(R.id.answerLblCreationDate);
			holder.lblText = (LinkEnabledTextView) viewGroup
					.findViewById(R.id.answerLblText);
			holder.lblUserFullName = (TextView) viewGroup
					.findViewById(R.id.answerLblUserFullName);

			viewGroup.setTag(holder);

			v = viewGroup;
		} else {
			holder = (AnswerViewHolder) convertView.getTag();

			v = convertView;
		}

		Answer answer = answers.get(position);
		TextView lblUserFullName = holder.lblUserFullName;
		
		if(answer.getText().equals("Sem Comentários")){
			lblUserFullName.setText("Sem Comentários");
			holder.imgUserThumb.setVisibility(View.GONE);
			holder.lblCreationDate.setVisibility(View.GONE);
			return v;
		}else{
			holder.imgUserThumb.setVisibility(View.VISIBLE);
			holder.lblCreationDate.setVisibility(View.VISIBLE);
		}

		final User answerUser = answer.getUser();

		String answerUserFullName = answerUser.getFirstName() + " "
				+ answerUser.getLastName();
		String thumbUrl = answerUser.getThumbnails().get(0).getHref();

		lblUserFullName.setText(answerUserFullName);
		lblUserFullName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				openUserWall(answerUser);
			}
		});

		holder.lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(answer
				.getCreatedAt()));

		bitmapManager.displayBitmapAsync(thumbUrl, holder.imgUserThumb);

		LinkEnabledTextView lblText = holder.lblText;
		lblText = holder.lblText;
		lblText.gatherLinksForText(answer.getText());
		lblText.setOnTextLinkClickListener(new TextLinkClickListener() {
			@Override
			public void onTextLinkClick(View textView, String clickedString) {
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(clickedString)));
			}
		});
		lblText.setMovementMethod(LinkMovementMethod.getInstance());

		return v;
	}

	private void openUserWall(User user) {
		Intent intent = new Intent(context, UserWallActivity.class);
		intent.putExtra("userId", user.getLogin());
		intent.putExtra("userFullName",
				user.getFirstName() + " " + user.getLastName());

		context.startActivity(intent);
	}
}
