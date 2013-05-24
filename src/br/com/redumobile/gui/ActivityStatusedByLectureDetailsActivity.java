package br.com.redumobile.gui;

import greendroid.app.GDActivity;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarItem;
import greendroid.widget.LoaderActionBarItem;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;
import br.com.redumobile.R;
import br.com.redumobile.ReduMobile;
import br.com.redumobile.entity.Activity;
import br.com.redumobile.entity.Answer;
import br.com.redumobile.entity.Lecture;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.Subject;
import br.com.redumobile.entity.User;
import br.com.redumobile.gui.LinkEnabledTextView.TextLinkClickListener;
import br.com.redumobile.gui.clickablespan.BlueClickableSpan;
import br.com.redumobile.gui.clickablespan.BoldBlueClickableSpan;
import br.com.redumobile.oauth.ReduClient;
import br.com.redumobile.util.BitmapManager;
import br.com.redumobile.util.DaemonThread;
import br.com.redumobile.util.DateFormatter;

public final class ActivityStatusedByLectureDetailsActivity extends GDActivity {

	private ReduMobile application;
	public static Activity activity;
	private BitmapManager bitmapManager;
	private ReduClient client;
	public LayoutInflater inflater;
	
	private final int ITEM_GO_HOME = 0;
	private final int ITEM_UPDATE = 1;
	private final int ITEM_COMPOSE = 2;
	private LoaderActionBarItem itemUpdate;
	private LinearLayout listAnswers;
	private final int RECOGNIZER_RESULT = 1234;
	private EditText txtPostText;
	private volatile String lectureName;
	private int lectureId;
	private ArrayList<Answer> answers;
	private boolean firstStart;

	private void loadBreadcrumb() {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				if (lectureName == null) {
					Lecture lecture = client.getLecture(String
							.valueOf(lectureId));
					if (lecture == null) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Toast.makeText(
										ActivityStatusedByLectureDetailsActivity.this,
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
							ImageView imgLecture = (ImageView) findViewById(R.id.activityStatusedByLectureDetailsImgLecture);
							imgLecture.setVisibility(View.VISIBLE);

							TextView lblName = (TextView) findViewById(R.id.activityStatusedByLectureDetailsLblName);
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

		setActionBarContentView(R.layout.activity_statused_by_lecture_details_activity);

		getAB().setType(ActionBar.Type.Empty);

		client = ReduMobile.getInstance().getClient();
		
		application = ReduMobile.getInstance();
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final User activityUser = activity.getUser();

		final Lecture activityStatusable = (Lecture) activity.getStatusable();

		lectureId = activityStatusable.getId();

		lectureName = activityStatusable.getName();

		loadBreadcrumb();

		bitmapManager = new BitmapManager();

		Subject subjectIn = activityStatusable.getSubjectIn();

		final Space spaceIn = activityStatusable.getSpaceIn();

		String activityUserFullName = activityUser.getFirstName() + " "
				+ activityUser.getLastName();
		String spaceInName = spaceIn.getName();
		String mainAndSecondaryText = spaceInName + " > " + subjectIn.getName();
		String infoText = activityUserFullName + " comentou no mural da aula "
				+ activityStatusable.getName();
		String thumbUrl = activityUser.getThumbnails().get(0).getHref();

		ImageView imgUserThumb = (ImageView) findViewById(R.id.activityStatusedByLectureDetailsImgUserThumb);

		bitmapManager.displayBitmapAsync(thumbUrl, imgUserThumb);

		SpannableString mainAndSecondary = new SpannableString(
				mainAndSecondaryText);
		mainAndSecondary.setSpan(new BoldBlueClickableSpan() {
			@Override
			public void onClick(View widget) {
				super.onClick(widget);

				openSpaceWall(spaceIn);
			}
		}, 0, spaceInName.length(), 0);
		mainAndSecondary.setSpan(new BlueClickableSpan(),
				spaceInName.length() + 3, mainAndSecondaryText.length(), 0);
		SpannableString info = new SpannableString(infoText);
		info.setSpan(new BoldBlueClickableSpan() {
			@Override
			public void onClick(View widget) {
				super.onClick(widget);

				openUserWall(activityUser);
			}
		}, 0, activityUserFullName.length(), 0);
		info.setSpan(new BlueClickableSpan() {
			@Override
			public void onClick(View widget) {
				super.onClick(widget);

				openLectureWall(activityStatusable);
			}
		}, activityUserFullName.length() + 27, infoText.length(), 0);

		TextView lblCreationDate = (TextView) findViewById(R.id.activityStatusedByLectureDetailsLblCreationDate);
		lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(activity.getCreatedAt()));
		TextView lblMainAndSecondary = (TextView) findViewById(R.id.activityStatusedByLectureDetailsLblMainAndSecondary);
		lblMainAndSecondary.setText(mainAndSecondary, BufferType.SPANNABLE);
		lblMainAndSecondary.setMovementMethod(LinkMovementMethod.getInstance());
		TextView lblInfo = (TextView) findViewById(R.id.activityStatusedByLectureDetailsLblInfo);
		lblInfo.setText(info, BufferType.SPANNABLE);
		lblInfo.setMovementMethod(LinkMovementMethod.getInstance());

		LinkEnabledTextView lblText = (LinkEnabledTextView) findViewById(R.id.activityStatusedByLectureDetailsLblText);
		lblText.gatherLinksForText(activity.getText());
		lblText.setOnTextLinkClickListener(new TextLinkClickListener() {
			@Override
			public void onTextLinkClick(View textView, String clickedString) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(clickedString)));
			}
		});
		lblText.setMovementMethod(LinkMovementMethod.getInstance());


		if( activity.getAnswersCount() == 0 && (activity.getAnswers() == null || activity.getAnswers().isEmpty())){
			ArrayList<Answer> listaResp = new ArrayList<Answer>();
			listaResp.add(new Answer(null, null, 0, "Sem Comentários", null, null));
			answers = listaResp;
		}else{
			answers = activity.getAnswers();
		}
		

		listAnswers = (LinearLayout) findViewById(R.id.activityStatusedByLectureDetailsListAnswers);

		auxUpdateAnswers();
		adicionarRespostar(answers);
		
		firstStart = true;

		addActionBarItem(ActionBarItem.Type.GoHome, ITEM_GO_HOME);
		
		itemUpdate = (LoaderActionBarItem) addActionBarItem(
				ActionBarItem.Type.Refresh, ITEM_UPDATE);

		addActionBarItem(ActionBarItem.Type.Compose, ITEM_COMPOSE);
	}

	@Override
	public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
		boolean clickHandled = true;

		if (position == ITEM_GO_HOME) {
			Intent intent = new Intent(this, UserWallActivity.class);
			intent.putExtra("userId", application.getUserLogin());

			startActivity(intent);
		}else if (position == ITEM_UPDATE) {
			updateAnswers();
		} else if (position == ITEM_COMPOSE) {
			 Intent intent = new Intent(this, PostOnAnswer.class);
			 intent.putExtra("statusId", activity.getId());
			 intent.putExtra("statusType", "Lecture");
			 intent.putExtra("lblName", "Responder em: "+lectureName);
			 
			 intent.putExtra("userId", activity.getUser().getId());
			 intent.putExtra("userFullName", activity.getUser().getFirstName() + " " + activity.getUser().getLastName());
			 intent.putExtra("infoText", activity.getText());
			 intent.putExtra("thumbUrl", activity.getUser().getThumbnails().get(0).getHref());
			 intent.putExtra("date", activity.getCreatedAt());
			 

			 startActivity(intent);
		} else {
			clickHandled = super.onHandleActionBarItemClick(item, position);
		}

		return clickHandled;
	}

	private void auxUpdateAnswers() {
		new DaemonThread(new Runnable() {
			@Override
			public void run() {
				if(answers == null){
					answers = new ArrayList<Answer>();
				}
				Answer lastAnswer;
				if(answers != null && !answers.isEmpty()){
					lastAnswer = answers.get(answers.size() - 1);
				}else{
					lastAnswer = null;
				}
				
				if(lastAnswer != null && lastAnswer.getText().equals("Sem Comentários")){
					lastAnswer = null;
					answers.clear();
				}

				ArrayList<Answer> newAnswers = client.getAnswers(""+activity.getId());
				
				if (newAnswers != null) {
					if(!newAnswers.isEmpty()){
						for (int i = newAnswers.size() - 1; i >= 0; i--) {
							Answer answer = newAnswers.get(i);
							
							if(lastAnswer != null){
								if (answer.getUpdatedAt().before(
										lastAnswer.getUpdatedAt())) {
									break;
								} else if (answer.getUpdatedAt().after(
										lastAnswer.getUpdatedAt())) {
									answers.add(answer);
								}
							}else{
								answers.add(answer);
							}
						}
					}else{
						answers.add(new Answer(null, null, 0, "Sem Comentários", null, null));
					}
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(
									ActivityStatusedByLectureDetailsActivity.this,
									"Algumas informações não puderam ser obtidas",
									Toast.LENGTH_LONG).show();
						}
					});
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Collections.sort(answers, Status.InvertedComparator.getInstance());
						adicionarRespostar(answers);

						itemUpdate.setLoading(false);
					}
				});
			}
		}).start();
	}

	private void openSpaceWall(Space space) {
		Intent intent = new Intent(this, SpaceWallActivity.class);
		intent.putExtra("spaceId", space.getId());
		intent.putExtra("spaceName", space.getName());

		startActivity(intent);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (firstStart) {
			firstStart = false;
		} else {
			updateAnswers();
		}
	}

	private void updateAnswers() {
		itemUpdate.setLoading(true);

		auxUpdateAnswers();
	}

	private void openUserWall(User user) {
		Intent intent = new Intent(this, UserWallActivity.class);
		intent.putExtra("userId", user.getLogin());
		intent.putExtra("userFullName",
				user.getFirstName() + " " + user.getLastName());

		startActivity(intent);
	}

	private void openLectureWall(Lecture lecture) {
		Intent intent = new Intent(this, LectureWallActivity.class);
		intent.putExtra("lectureId", lecture.getId());
		intent.putExtra("lectureName", lecture.getName());

		startActivity(intent);
	}
	
	private void adicionarRespostar(ArrayList<Answer> answers){
		
		if(answers != null && !answers.isEmpty()){
			
			listAnswers.removeAllViewsInLayout();

			int count = 0;
			for (Answer answer : answers) {
				
				
				final View view = (View) inflater.inflate(R.layout.answer,null);
				view.setId(count);
				ImageView imgUserThumb = (ImageView) view.findViewById(R.id.answerImgUserThumb);
				TextView lblCreationDate = (TextView) view.findViewById(R.id.answerLblCreationDate);
				LinkEnabledTextView lblText = (LinkEnabledTextView) view.findViewById(R.id.answerLblText);
				TextView lblUserFullName = (TextView) view.findViewById(R.id.answerLblUserFullName);
				
				if(answer.getText().equals("Sem Comentários")){
					lblUserFullName.setText("Sem Comentários");
					imgUserThumb.setVisibility(View.GONE);
					lblCreationDate.setVisibility(View.GONE);
					listAnswers.addView(view,count);
					listAnswers.refreshDrawableState();
					count++;
					return;
				}else{
					imgUserThumb.setVisibility(View.VISIBLE);
					lblCreationDate.setVisibility(View.VISIBLE);
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

				lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(answer
						.getCreatedAt()));

				bitmapManager.displayBitmapAsync(thumbUrl, imgUserThumb);
				
				lblText.gatherLinksForText(answer.getText());
				TextLinkClickListener textLinkClickListener = new TextLinkClickListener() {
					@Override
					public void onTextLinkClick(View textView, String clickedString) {
						startActivity(new Intent(
								Intent.ACTION_VIEW, Uri.parse(clickedString)));
					}
				};
				lblText.setOnTextLinkClickListener(textLinkClickListener);
				
				lblText.setMovementMethod(LinkMovementMethod.getInstance());
				
				listAnswers.post(new Runnable() {
					@Override
					public void run() {
						listAnswers.addView(view);
					}
				});
				
				count++;
			}
			listAnswers.refreshDrawableState();
		}
	}
}
