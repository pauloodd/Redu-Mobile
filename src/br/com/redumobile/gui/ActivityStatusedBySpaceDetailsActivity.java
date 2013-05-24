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
import br.com.redumobile.entity.Course;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.User;
import br.com.redumobile.gui.LinkEnabledTextView.TextLinkClickListener;
import br.com.redumobile.gui.clickablespan.BlueClickableSpan;
import br.com.redumobile.gui.clickablespan.BoldBlueClickableSpan;
import br.com.redumobile.oauth.ReduClient;
import br.com.redumobile.util.BitmapManager;
import br.com.redumobile.util.DaemonThread;
import br.com.redumobile.util.DateFormatter;

public final class ActivityStatusedBySpaceDetailsActivity extends GDActivity {
	
	private ReduMobile application;
	public static Activity activity;
	public LayoutInflater inflater;
	private BitmapManager bitmapManager;
	private ReduClient client;
	
	private final int ITEM_GO_HOME = 0;
	private final int ITEM_UPDATE = 1;
	private final int ITEM_COMPOSE = 2;
	private LoaderActionBarItem itemUpdate;
	private LinearLayout listAnswers;
	private final int RECOGNIZER_RESULT = 1234;
	private EditText txtPostText;
	private volatile String spaceName;
	private int spaceId;
	private ArrayList<Answer> answers;
	private boolean firstStart;

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
										ActivityStatusedBySpaceDetailsActivity.this,
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
							ImageView imgSpace = (ImageView) findViewById(R.id.activityStatusedBySpaceDetailsImgSpace);
							imgSpace.setVisibility(View.VISIBLE);

							TextView lblName = (TextView) findViewById(R.id.activityStatusedBySpaceDetailsLblName);
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

		setActionBarContentView(R.layout.activity_statused_by_space_details_activity);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		getAB().setType(ActionBar.Type.Empty);

		client = ReduMobile.getInstance().getClient();
		
		application = ReduMobile.getInstance();

		final User activityUser = activity.getUser();

		final Space activityStatusable = (Space) activity.getStatusable();

		spaceId = activityStatusable.getId();

		spaceName = activityStatusable.getName();

		loadBreadcrumb();

		bitmapManager = new BitmapManager();

		Course courseIn = activityStatusable.getCourseIn();

		String activityUserFullName = activityUser.getFirstName() + " "
				+ activityUser.getLastName();
		String activityStatusableName = activityStatusable.getName();
		String courseInName = courseIn.getName();
		String mainAndSecondaryText = courseInName + " > "
				+ activityStatusableName;
		String infoText = activityUserFullName
				+ " comentou no mural da disciplina " + activityStatusableName;
		String thumbUrl = activityUser.getThumbnails().get(0).getHref();

		ImageView imgUserThumb = (ImageView) findViewById(R.id.activityStatusedBySpaceDetailsImgUserThumb);

		bitmapManager.displayBitmapAsync(thumbUrl, imgUserThumb);

		SpannableString mainAndSecondary = new SpannableString(
				mainAndSecondaryText);
		mainAndSecondary.setSpan(new BoldBlueClickableSpan(), 0,
				courseInName.length(), 0);
		mainAndSecondary.setSpan(new BlueClickableSpan() {
			@Override
			public void onClick(View widget) {
				super.onClick(widget);

				openSpaceWall(activityStatusable);
			}
		}, courseInName.length() + 3, mainAndSecondaryText.length(), 0);
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

				openSpaceWall(activityStatusable);
			}
		}, activityUserFullName.length() + 33, infoText.length(), 0);

		TextView lblCreationDate = (TextView) findViewById(R.id.activityStatusedBySpaceDetailsLblCreationDate);
		lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(activity.getCreatedAt()));
		TextView lblMainAndSecondary = (TextView) findViewById(R.id.activityStatusedBySpaceDetailsLblMainAndSecondary);
		lblMainAndSecondary.setText(mainAndSecondary, BufferType.SPANNABLE);
		lblMainAndSecondary.setMovementMethod(LinkMovementMethod.getInstance());
		TextView lblInfo = (TextView) findViewById(R.id.activityStatusedBySpaceDetailsLblInfo);
		lblInfo.setText(info, BufferType.SPANNABLE);
		lblInfo.setMovementMethod(LinkMovementMethod.getInstance());

		LinkEnabledTextView lblText = (LinkEnabledTextView) findViewById(R.id.activityStatusedBySpaceDetailsLblText);
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
			answers  = listaResp;
		}else{
			answers = activity.getAnswers();
		}
		

		listAnswers = (LinearLayout) findViewById(R.id.activityStatusedBySpaceDetailsListAnswers);

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
			 intent.putExtra("statusType", "Space");
			 intent.putExtra("lblName", "Responder em: "+spaceName);
			 
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
									ActivityStatusedBySpaceDetailsActivity.this,
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
	
	private void adicionarRespostar(ArrayList<Answer> answers){
		
		int count = 0;
		if(answers != null && !answers.isEmpty()){
			
			listAnswers.removeAllViewsInLayout();
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
