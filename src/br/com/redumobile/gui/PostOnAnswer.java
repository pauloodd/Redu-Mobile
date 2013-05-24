package br.com.redumobile.gui;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import br.com.redumobile.R;
import br.com.redumobile.ReduMobile;
import br.com.redumobile.entity.Status;
import br.com.redumobile.gui.LinkEnabledTextView.TextLinkClickListener;
import br.com.redumobile.gui.clickablespan.BoldBlueClickableSpan;
import br.com.redumobile.oauth.ReduClient;
import br.com.redumobile.util.BitmapManager;
import br.com.redumobile.util.DaemonThread;
import br.com.redumobile.util.Utils;

public final class PostOnAnswer extends GDActivity {
	private ReduClient client;
	private final int RECOGNIZER_RESULT = 1234;
	private EditText txtPostText;
	private volatile String lblName;
	private volatile String statusType;
	private volatile String userFullName;
	private volatile String infoText;
	private volatile String thumbUrl;
	private volatile String userId;
	private volatile String date;
	private String statusId;
	private BitmapManager bitmapManager;

	
	private void loadBreadcrumb() {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				if (lblName == null) {
					Status status = client.getStatus(statusId, false);
					if (status == null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(
										PostOnAnswer.this,
										"Algumas informações não puderam ser obtidas",
										Toast.LENGTH_LONG).show();
							}
						});
					} else {
						lblName = status.getText();
					}
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (lblName != null) {
							ImageView img = (ImageView) findViewById(R.id.postOnAnswerImg);
							
							if(statusType != null && statusType.equals("Lecture")){
								img.setBackgroundResource(R.drawable.lecture_icon);
							}else if(statusType != null &&  statusType.equals("Space")){
								img.setBackgroundResource(R.drawable.space_icon);
							}else if(statusType != null &&  statusType.equals("Help")){
								img.setBackgroundResource(R.drawable.user_icon);
							}else if(statusType != null &&  statusType.equals("User")){
								img.setBackgroundResource(R.drawable.user_icon);
							}
							img.setVisibility(View.VISIBLE);

							TextView labelName = (TextView) findViewById(R.id.postOnAnswerLblName);
							labelName.setText(lblName);
						}
					}
				});
			}
		}).start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == RECOGNIZER_RESULT) {
				ArrayList<String> results = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				String oldText = txtPostText.getText().toString();

				txtPostText.setText(oldText
						+ (oldText.length() == 0 || oldText.endsWith(" ") ? ""
								: " ") + results.get(0) + " ");
				txtPostText.setSelection(txtPostText.length());
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setActionBarContentView(R.layout.post_on_answer);

		getAB().setType(ActionBar.Type.Empty);

		client = ReduMobile.getInstance().getClient();

		statusId = getIntent().getStringExtra("statusId");
		statusType = getIntent().getStringExtra("statusType");
		
		if (statusId == null) {
			int id = getIntent().getIntExtra("statusId", -1);
			if (id == -1) {
				throw new RuntimeException("A id do status não foi informada");
			} else {
				statusId = String.valueOf(id);
			}
		}
		
		bitmapManager = new BitmapManager();
		
		lblName = getIntent().getStringExtra("lblName");
		
		TextView lblCreationDate = (TextView) findViewById(R.id.postOnDetailsLblCreationDate);
		LinkEnabledTextView lblText = (LinkEnabledTextView) findViewById(R.id.postOnDetailsLblText);
		TextView lblInfo = (TextView) findViewById(R.id.postOnDetailsLblInfo);
		
		bitmapManager = new BitmapManager();
		
		userFullName = getIntent().getStringExtra("userFullName");
		infoText = getIntent().getStringExtra("infoText");
		userId = getIntent().getStringExtra("userId");
		thumbUrl = getIntent().getStringExtra("thumbUrl");
		date = getIntent().getStringExtra("date");
		
		
		ImageView imgUserThumb = (ImageView) findViewById(R.id.postOnDetailsImgUserThumb);
		bitmapManager.displayBitmapAsync(thumbUrl, imgUserThumb);
		
		SpannableString info = new SpannableString(userFullName);
		
		info.setSpan(new BoldBlueClickableSpan() {
			@Override
			public void onClick(View widget) {
				super.onClick(widget);
				
				openUserWall(userId,userFullName);
			}
		}, 0, userFullName.length(), 0);
		
		
		lblCreationDate.setText(date);
		lblInfo.setText(info, BufferType.SPANNABLE);
		lblInfo.setMovementMethod(LinkMovementMethod.getInstance());
		lblText.gatherLinksForText(reduzirTexto(infoText));
		lblText.setOnTextLinkClickListener(new TextLinkClickListener() {
			@Override
			public void onTextLinkClick(View textView, String clickedString) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(clickedString)));
			}
		});
		lblText.setMovementMethod(LinkMovementMethod.getInstance());
		
		

		loadBreadcrumb();

		final ViewGroup lytSpinner = (ViewGroup) findViewById(R.id.postOnAnswerLytSpinner);

		final TextView lblTextInfo = (TextView) findViewById(R.id.postOnAnswerLblTextInfo);

		txtPostText = (EditText) findViewById(R.id.postOnAnswerTxtPostText);
		txtPostText.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				int currentCharsCount = s.length();
				int remainingCharsCount = (int) Utils.clamp(
						ReduClient.MAX_CHARS_COUNT_IN_POST - currentCharsCount,
						0, ReduClient.MAX_CHARS_COUNT_IN_POST);

				lblTextInfo
						.setText(remainingCharsCount == 1 ? "1 caractere restante."
								: remainingCharsCount
										+ " caracteres restantes.");
			}
		});

		Button btnPost = (Button) findViewById(R.id.postOnAnswerBtnPost);
		btnPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (txtPostText.length() > 0
						&& txtPostText.length() <= ReduClient.MAX_CHARS_COUNT_IN_POST) {

					v.setVisibility(View.GONE);

					lytSpinner.setVisibility(View.VISIBLE);

					new DaemonThread(new Runnable() {
						@Override
						public void run() {
							final String posted = client.postStatusAnswer(statusId, txtPostText.getText().toString());
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (posted != null && !posted.equals("") && !posted.contains("not authorized")) {
										Toast.makeText(
												PostOnAnswer.this,
												"Ação realizada com sucesso",
												Toast.LENGTH_LONG).show();

										new Handler().postDelayed(
												new Runnable() {
													@Override
													public void run() {
														finish();
													}
												}, 3800);
									} else {
										if(posted.contains("not authorized")){
											Toast.makeText(
													PostOnAnswer.this,
													"A ação desejada não pôde ser realizada, o usuário não autoriza postagens feitas por desconhecidos.",
													Toast.LENGTH_LONG).show();
										}else{
											Toast.makeText(
													PostOnAnswer.this,
													"A ação desejada não pôde ser realizada. Tente novamente",
													Toast.LENGTH_LONG).show();
										}
									}

									lytSpinner.setVisibility(View.GONE);

									v.setVisibility(View.VISIBLE);

								}
							});
						}
					}).start();
				} else if (txtPostText.length() > ReduClient.MAX_CHARS_COUNT_IN_POST) {
					lblTextInfo.setText("Texto muito longo.");
				} else {
					lblTextInfo.setText("Texto deixado em branco.");
				}
			}
		});
	}
	
	private void openUserWall(String userId, String userFullName) {
		Intent intent = new Intent(this, UserWallActivity.class);
		intent.putExtra("userId", userId);
		intent.putExtra("userFullName",userFullName);
		startActivity(intent);
	}

	private String reduzirTexto(String info){
		if(info.length() > 199){
			return info.substring(0, 199)+"...";
		}else{
			return info;
		}
	}
}