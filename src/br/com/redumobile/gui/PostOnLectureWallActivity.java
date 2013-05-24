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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import br.com.redumobile.R;
import br.com.redumobile.ReduMobile;
import br.com.redumobile.entity.Lecture;
import br.com.redumobile.oauth.ReduClient;
import br.com.redumobile.util.DaemonThread;
import br.com.redumobile.util.Utils;

public final class PostOnLectureWallActivity extends GDActivity {
	private ReduClient client;
	private final int RECOGNIZER_RESULT = 1234;
	private int lectureId;
	private volatile String lectureName;
	private EditText txtPostText;

	private void loadBreadcrumb() {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				if (lectureName == null) {
					Lecture lecture = client.getLecture(String.valueOf(lectureId));
					if (lecture == null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(
										PostOnLectureWallActivity.this,
										"Algumas informações não puderam ser obtidas",
										Toast.LENGTH_LONG).show();
							}
						});
					} else {
						lectureName = lecture.getName();
					}
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (lectureName != null) {
							ImageView imgLecture = (ImageView) findViewById(R.id.postOnLectureWallActivityImgLecture);
							imgLecture.setVisibility(View.VISIBLE);

							TextView lblName = (TextView) findViewById(R.id.postOnLectureWallActivityLblName);
							lblName.setText(lectureName);
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

		setActionBarContentView(R.layout.post_on_lecture_wall_activity);

		getAB().setType(ActionBar.Type.Empty);

		client = ReduMobile.getInstance().getClient();

		lectureId = getIntent().getIntExtra("lectureId", -1);
		if (lectureId == -1) {
			throw new RuntimeException("O id da disciplina não foi informada");
		}
		lectureName = getIntent().getStringExtra("lectureName");

		loadBreadcrumb();

		final ViewGroup lytSpinner = (ViewGroup) findViewById(R.id.postOnLectureWallActivityLytSpinner);

		final TextView lblTextInfo = (TextView) findViewById(R.id.postOnLectureWallActivityLblTextInfo);

		txtPostText = (EditText) findViewById(R.id.postOnLectureWallActivityTxtPostText);
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
		
		
		Button btnPost = (Button) findViewById(R.id.postOnLectureWallActivityBtnPost);
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
							
							String tipoPost = "Activity";
							RadioButton radioButtonAjuda = (RadioButton) findViewById(R.id.radioButtonAjuda);
							if(radioButtonAjuda.isChecked()){
								tipoPost = "Help";
							}
							
							final String posted = client.postLectureStatus(String.valueOf(lectureId), txtPostText.getText().toString(), tipoPost);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									if (posted != null && !posted.equals("") && !posted.contains("not authorized")) {
										Toast.makeText(
												PostOnLectureWallActivity.this,
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
													PostOnLectureWallActivity.this,
													"A ação desejada não pôde ser realizada, o usuário não autoriza postagens por desconhecidos.",
													Toast.LENGTH_LONG).show();
										}else{
											Toast.makeText(
													PostOnLectureWallActivity.this,
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
	
	private void checkRadio(int checkedId) {
		RadioButton radioButtonComentar = (RadioButton) findViewById(R.id.radioButtonComentar);
		RadioButton radioButtonAjuda = (RadioButton) findViewById(R.id.radioButtonAjuda);
		 if(checkedId == R.id.radioButtonAjuda){
			radioButtonAjuda.setChecked(true);
         	radioButtonComentar.setChecked(false);
         }else if(checkedId == R.id.radioButtonComentar){
         	radioButtonAjuda.setChecked(false);
         	radioButtonComentar.setChecked(true);
         }
	}
	
	public void onAjudaClick(View view) {
	    checkRadio(R.id.radioButtonAjuda);
	}

	public void onComentClick(View view) {
	    checkRadio(R.id.radioButtonComentar);
	}
}