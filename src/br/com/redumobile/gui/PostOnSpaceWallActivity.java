package br.com.redumobile.gui;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import br.com.redumobile.R;
import br.com.redumobile.ReduMobile;
import br.com.redumobile.entity.Space;
import br.com.redumobile.oauth.ReduClient;
import br.com.redumobile.util.DaemonThread;
import br.com.redumobile.util.Utils;

public final class PostOnSpaceWallActivity extends GDActivity {
	private ReduClient client;
	private final int RECOGNIZER_RESULT = 1234;
	private int spaceId;
	private volatile String spaceName;
	private EditText txtPostText;

	private void loadBreadcrumb() {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				if (spaceName == null) {
					Space space = client.getSpace(String.valueOf(spaceId));
					if (space == null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(
										PostOnSpaceWallActivity.this,
										"Algumas informações não puderam ser obtidas",
										Toast.LENGTH_LONG).show();
							}
						});
					} else {
						spaceName = space.getName();
					}
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (spaceName != null) {
							ImageView imgSpace = (ImageView) findViewById(R.id.postOnSpaceWallActivityImgSpace);
							imgSpace.setVisibility(View.VISIBLE);

							TextView lblName = (TextView) findViewById(R.id.postOnSpaceWallActivityLblName);
							lblName.setText(spaceName);
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

		setActionBarContentView(R.layout.post_on_space_wall_activity);

		getAB().setType(ActionBar.Type.Empty);

		client = ReduMobile.getInstance().getClient();

		spaceId = getIntent().getIntExtra("spaceId", -1);
		if (spaceId == -1) {
			throw new RuntimeException("A id da disciplina não foi informada");
		}
		spaceName = getIntent().getStringExtra("spaceName");

		loadBreadcrumb();

		final ViewGroup lytSpinner = (ViewGroup) findViewById(R.id.postOnSpaceWallActivityLytSpinner);

		final TextView lblTextInfo = (TextView) findViewById(R.id.postOnSpaceWallActivityLblTextInfo);

		txtPostText = (EditText) findViewById(R.id.postOnSpaceWallActivityTxtPostText);
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

//		final ImageButton btnStartSpeech = (ImageButton) findViewById(R.id.postOnSpaceWallActivityBtnStartSpeech);
//		btnStartSpeech.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(
//						RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//				intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//						RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//				intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Fale agora");
//
//				startActivityForResult(intent, RECOGNIZER_RESULT);
//			}
//		});

		Button btnPost = (Button) findViewById(R.id.postOnSpaceWallActivityBtnPost);
		btnPost.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if (txtPostText.length() > 0
						&& txtPostText.length() <= ReduClient.MAX_CHARS_COUNT_IN_POST) {
//					btnStartSpeech.setVisibility(View.GONE);

					v.setVisibility(View.GONE);

					lytSpinner.setVisibility(View.VISIBLE);

					new DaemonThread(new Runnable() {
						@Override
						public void run() {
							final String posted = client.postSpaceStatus(
									String.valueOf(spaceId), txtPostText
											.getText().toString());
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (posted != null && !posted.equals("") && !posted.contains("not authorized")) {
										Toast.makeText(
												PostOnSpaceWallActivity.this,
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
													PostOnSpaceWallActivity.this,
													"A ação desejada não pôde ser realizada, o administrador não autoriza postagens por não membros.",
													Toast.LENGTH_LONG).show();
										}else{
											Toast.makeText(
													PostOnSpaceWallActivity.this,
													"A ação desejada não pôde ser realizada. Tente novamente",
													Toast.LENGTH_LONG).show();
										}
									}

									lytSpinner.setVisibility(View.GONE);

									v.setVisibility(View.VISIBLE);

//									btnStartSpeech.setVisibility(View.VISIBLE);
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
}