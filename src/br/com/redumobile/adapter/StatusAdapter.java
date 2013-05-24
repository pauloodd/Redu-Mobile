package br.com.redumobile.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import br.com.redumobile.R;
import br.com.redumobile.ReduMobile;
import br.com.redumobile.entity.Activity;
import br.com.redumobile.entity.Course;
import br.com.redumobile.entity.Help;
import br.com.redumobile.entity.Lecture;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.Subject;
import br.com.redumobile.entity.User;
import br.com.redumobile.gui.ActivityStatusedByLectureDetailsActivity;
import br.com.redumobile.gui.ActivityStatusedBySpaceDetailsActivity;
import br.com.redumobile.gui.ActivityStatusedByUserDetailsActivity;
import br.com.redumobile.gui.HelpStatusedByLectureDetailsActivity;
import br.com.redumobile.gui.LectureWallActivity;
import br.com.redumobile.gui.LinkEnabledTextView;
import br.com.redumobile.gui.LinkEnabledTextView.TextLinkClickListener;
import br.com.redumobile.gui.SpaceWallActivity;
import br.com.redumobile.gui.UserWallActivity;
import br.com.redumobile.gui.clickablespan.BlueClickableSpan;
import br.com.redumobile.gui.clickablespan.BoldBlueClickableSpan;
import br.com.redumobile.util.BitmapManager;
import br.com.redumobile.util.DateFormatter;

public final class StatusAdapter extends BaseAdapter {
	private final class ActivityStatusedByLectureViewHolder {
		private ImageView imgAnswers;
		private ImageView imgArrowRevolve;
		private ImageView imgUserThumb;
		private TextView lblAnswer;
		private TextView lblAnswersCount;
		private TextView lblCreationDate;
		private TextView lblInfo;
		private TextView lblMainAndSecondary;
		private LinkEnabledTextView lblText;
	}

	private final class ActivityStatusedBySpaceViewHolder {
		private ImageView imgAnswers;
		private ImageView imgArrowRevolve;
		private ImageView imgUserThumb;
		private TextView lblAnswer;
		private TextView lblAnswersCount;
		private TextView lblCreationDate;
		private TextView lblInfo;
		private TextView lblMainAndSecondary;
		private LinkEnabledTextView lblText;
	}

	private final class ActivityStatusedByUserViewHolder {
		private ImageView imgAnswers;
		private ImageView imgArrowRevolve;
		private ImageView imgUserThumb;
		private TextView lblAnswer;
		private TextView lblAnswersCount;
		private TextView lblCreationDate;
		private TextView lblInfo;
		private LinkEnabledTextView lblText;
	}

	private final class HelpStatusedByLectureViewHolder {
		private ImageView imgAnswers;
		private ImageView imgArrowRevolve;
		private ImageView imgUserThumb;
		private TextView lblAnswer;
		private TextView lblAnswersCount;
		private TextView lblCreationDate;
		private TextView lblInfo;
		private TextView lblMainAndSecondary;
		private LinkEnabledTextView lblText;
		private ViewGroup lytRoot;
	}

	private BitmapManager bitmapManager;
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<Status> statuses;
	private TextLinkClickListener textLinkClickListener;
	private final int VIEW_TYPE_ACTIVITY_STATUSED_BY_LECTURE = 2;
	private final int VIEW_TYPE_ACTIVITY_STATUSED_BY_SPACE = 1;
	private final int VIEW_TYPE_ACTIVITY_STATUSED_BY_USER = 0;
	private final int VIEW_TYPE_COUNT = 4;
	private final int VIEW_TYPE_HELP_STATUSED_BY_LECTURE = 3;

	public StatusAdapter(Context context, ArrayList<Status> statuses) {
		this.context = context;

		this.statuses = statuses;

		bitmapManager = new BitmapManager();

		inflater = LayoutInflater.from(this.context);

		textLinkClickListener = new TextLinkClickListener() {
			@Override
			public void onTextLinkClick(View textView, String clickedString) {
				StatusAdapter.this.context.startActivity(new Intent(
						Intent.ACTION_VIEW, Uri.parse(clickedString)));
			}
		};
	}

	@Override
	public int getCount() {
		return statuses.size();
	}

	@Override
	public Object getItem(int position) {
		return statuses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		int viewType = 0;

		Status status = statuses.get(position);
		if (status instanceof Activity) {
			Activity activity = (Activity) status;
			if (activity.getStatusable() instanceof User) {
				viewType = VIEW_TYPE_ACTIVITY_STATUSED_BY_USER;
			} else if (activity.getStatusable() instanceof Space) {
				viewType = VIEW_TYPE_ACTIVITY_STATUSED_BY_SPACE;
			} else if (activity.getStatusable() instanceof Lecture){
				viewType = VIEW_TYPE_ACTIVITY_STATUSED_BY_LECTURE;
			}else if(activity.getStatusable() == null && activity.getUser() != null){
				viewType = VIEW_TYPE_ACTIVITY_STATUSED_BY_USER;
			}
		} else if (status instanceof Help) {
			viewType = VIEW_TYPE_HELP_STATUSED_BY_LECTURE;
		}

		return viewType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;

		int viewType = getItemViewType(position);
		if (viewType == VIEW_TYPE_ACTIVITY_STATUSED_BY_USER) {
			ActivityStatusedByUserViewHolder holder;

			View v;
			if (convertView == null) {
				ViewGroup viewGroup = (ViewGroup) inflater.inflate(
						R.layout.activity_statused_by_user, null);

				holder = new ActivityStatusedByUserViewHolder();
				holder.imgUserThumb = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedByUserImgUserThumb);
				holder.imgAnswers = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedByUserImgAnswers);
				holder.imgArrowRevolve = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedByUserImgArrowRevolve);
				holder.lblAnswer = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByUserLblAnswer);
				holder.lblAnswersCount = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByUserLblAnswersCount);
				holder.lblCreationDate = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByUserLblCreationDate);
				holder.lblInfo = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByUserLblInfo);
				holder.lblText = (LinkEnabledTextView) viewGroup
						.findViewById(R.id.activityStatusedByUserLblText);

				viewGroup.setTag(holder);

				v = viewGroup;
			} else {
				holder = (ActivityStatusedByUserViewHolder) convertView
						.getTag();

				v = convertView;
			}

			final Activity activity = (Activity) statuses.get(position);

			View.OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					openActivityStatusedByUserDetails(activity);
				}
			};

			holder.lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(activity
					.getCreatedAt()));
			holder.lblAnswer.setOnClickListener(listener);
			holder.imgArrowRevolve.setOnClickListener(listener);
			holder.imgAnswers.setOnClickListener(listener);

			TextView lblAnswersCount = holder.lblAnswersCount;
			
			/*if(activity.getAnswers() == null || activity.getAnswers().isEmpty()){
				lblAnswersCount.setText("0");
			}else{
				lblAnswersCount.setText(String
						.valueOf(activity.getAnswers().size()));
			}*/
			lblAnswersCount.setText(String.valueOf(activity.getAnswersCount()));
			
			lblAnswersCount.setOnClickListener(listener);

			SpannableString info = null;

			final User activityUser = activity.getUser();
			final User activityStatusableFinal;
			if(activity.getStatusable() == null){
				activityStatusableFinal = activityUser;
			}else{
				activityStatusableFinal = (User) activity.getStatusable();
			}

			String activityUserFullName = activityUser.getFirstName() + " "
					+ activityUser.getLastName();
			String activityStatusableFullName = activityStatusableFinal
					.getFirstName() + " " + activityStatusableFinal.getLastName();
			String thumbUrl = activityUser.getThumbnails().get(activityUser.getThumbnails().size()-1).getHref();

			bitmapManager.displayBitmapAsync(thumbUrl, holder.imgUserThumb);

			if (activityUser.getLogin().equals(activityStatusableFinal.getLogin())) {
				String infoText = activityUserFullName
						+ " comentou no seu próprio mural";

				info = new SpannableString(infoText);
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

						openUserWall(activityStatusableFinal);
					}
				}, activityUserFullName.length() + 13, infoText.length(), 0);
			} else {
				String infoText = activityUserFullName
						+ " comentou no mural de " + activityStatusableFullName;

				info = new SpannableString(infoText);
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

						openUserWall(activityStatusableFinal);
					}
				}, activityUserFullName.length() + 22, infoText.length(), 0);
			}

			TextView lblInfo = holder.lblInfo;
			lblInfo.setText(info, BufferType.SPANNABLE);
			lblInfo.setMovementMethod(LinkMovementMethod.getInstance());

			LinkEnabledTextView lblText = holder.lblText;
			lblText.gatherLinksForText(activity.getText());
			lblText.setOnTextLinkClickListener(textLinkClickListener);
			lblText.setMovementMethod(LinkMovementMethod.getInstance());

			view = v;
		} else if (viewType == VIEW_TYPE_ACTIVITY_STATUSED_BY_SPACE) {
			ActivityStatusedBySpaceViewHolder holder;

			View v;
			if (convertView == null) {
				ViewGroup viewGroup = (ViewGroup) inflater.inflate(
						R.layout.activity_statused_by_space, null);

				holder = new ActivityStatusedBySpaceViewHolder();
				holder.imgUserThumb = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceImgUserThumb);
				holder.imgAnswers = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceImgAnswers);
				holder.imgArrowRevolve = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceImgArrowRevolve);
				holder.lblAnswer = (TextView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceLblAnswer);
				holder.lblAnswersCount = (TextView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceLblAnswersCount);
				holder.lblCreationDate = (TextView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceLblCreationDate);
				holder.lblInfo = (TextView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceLblInfo);
				holder.lblMainAndSecondary = (TextView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceLblMainAndSecondary);
				holder.lblText = (LinkEnabledTextView) viewGroup
						.findViewById(R.id.activityStatusedBySpaceLblText);

				viewGroup.setTag(holder);

				v = viewGroup;
			} else {
				holder = (ActivityStatusedBySpaceViewHolder) convertView
						.getTag();

				v = convertView;
			}

			final Activity activity = (Activity) statuses.get(position);

			View.OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					openActivityStatusedBySpaceDetails(activity);
				}
			};

			holder.lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(activity
					.getCreatedAt()));
			holder.lblAnswer.setOnClickListener(listener);
			holder.imgArrowRevolve.setOnClickListener(listener);
			holder.imgAnswers.setOnClickListener(listener);

			TextView lblAnswersCount = holder.lblAnswersCount;
			
			lblAnswersCount.setText(String.valueOf(activity.getAnswersCount()));
			lblAnswersCount.setOnClickListener(listener);

			final User activityUser = activity.getUser();

			final Space activityStatusable = (Space) activity.getStatusable();

			Course courseIn = activityStatusable.getCourseIn();

			String activityUserFullName = activityUser.getFirstName() + " "
					+ activityUser.getLastName();
			String activityStatusableName = activityStatusable.getName();
			String courseInName = courseIn.getName();
			String mainAndSecondaryText = courseInName + " > "
					+ activityStatusableName;
			String infoText = activityUserFullName
					+ " comentou no mural da disciplina "
					+ activityStatusableName;
			String thumbUrl = activityUser.getThumbnails().get(activityUser.getThumbnails().size()-1).getHref();

			bitmapManager.displayBitmapAsync(thumbUrl, holder.imgUserThumb);

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

			TextView lblMainAndSecondary = holder.lblMainAndSecondary;
			lblMainAndSecondary.setText(mainAndSecondary, BufferType.SPANNABLE);
			lblMainAndSecondary.setMovementMethod(LinkMovementMethod
					.getInstance());
			TextView lblInfo = holder.lblInfo;
			lblInfo.setText(info, BufferType.SPANNABLE);
			lblInfo.setMovementMethod(LinkMovementMethod.getInstance());

			LinkEnabledTextView lblText = holder.lblText;
			lblText.gatherLinksForText(activity.getText());
			lblText.setOnTextLinkClickListener(textLinkClickListener);
			lblText.setMovementMethod(LinkMovementMethod.getInstance());

			view = v;
		} else if (viewType == VIEW_TYPE_ACTIVITY_STATUSED_BY_LECTURE) {
			ActivityStatusedByLectureViewHolder holder;

			View v;
			if (convertView == null) {
				ViewGroup viewGroup = (ViewGroup) inflater.inflate(
						R.layout.activity_statused_by_lecture, null);

				holder = new ActivityStatusedByLectureViewHolder();
				holder.imgUserThumb = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedByLectureImgUserThumb);
				holder.imgAnswers = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedByLectureImgAnswers);
				holder.imgArrowRevolve = (ImageView) viewGroup
						.findViewById(R.id.activityStatusedByLectureImgArrowRevolve);
				holder.lblAnswer = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByLectureLblAnswer);
				holder.lblAnswersCount = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByLectureLblAnswersCount);
				holder.lblCreationDate = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByLectureLblCreationDate);
				holder.lblInfo = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByLectureLblInfo);
				holder.lblMainAndSecondary = (TextView) viewGroup
						.findViewById(R.id.activityStatusedByLectureLblMainAndSecondary);
				holder.lblText = (LinkEnabledTextView) viewGroup
						.findViewById(R.id.activityStatusedByLectureLblText);

				viewGroup.setTag(holder);

				v = viewGroup;
			} else {
				holder = (ActivityStatusedByLectureViewHolder) convertView
						.getTag();

				v = convertView;
			}

			final Activity activity = (Activity) statuses.get(position);

			View.OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					openActivityStatusedByLectureDetails(activity);
				}
			};

			holder.lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(activity
					.getCreatedAt()));
			holder.lblAnswer.setOnClickListener(listener);
			holder.imgArrowRevolve.setOnClickListener(listener);
			holder.imgAnswers.setOnClickListener(listener);

			TextView lblAnswersCount = holder.lblAnswersCount;
			/*if(activity.getAnswers() == null || activity.getAnswers().isEmpty()){
				lblAnswersCount.setText("0");
			}else{
				lblAnswersCount.setText(String
						.valueOf(activity.getAnswers().size()));
			}*/
			lblAnswersCount.setText(String.valueOf(activity.getAnswersCount()));
			
			lblAnswersCount.setOnClickListener(listener);

			final User activityUser = activity.getUser();

			final Lecture activityStatusable = (Lecture) activity
					.getStatusable();

			
			Subject subjectIn = null;
			Space spaceIn = null;
			final Space spaceInFinal; 
			boolean criarViewClicando = true;
			
			if(activityStatusable != null){
				subjectIn = activityStatusable.getSubjectIn();
				spaceIn = activityStatusable.getSpaceIn();
				spaceInFinal = spaceIn;
			}else{
				spaceInFinal = null;
				criarViewClicando = false;
			}

			if(criarViewClicando){
				
				String activityUserFullName = activityUser.getFirstName() + " "
						+ activityUser.getLastName();
				
				String spaceInName ="";
				if(spaceIn != null){
					spaceInName = spaceIn.getName();
				}
				
				String mainAndSecondaryText = "";
				if(subjectIn == null){
					mainAndSecondaryText = spaceInName;
				}else{
					mainAndSecondaryText = spaceInName + " > "
							+ subjectIn.getName();
				}
				
				String infoText = "";
				if(activityStatusable == null){
					infoText = activityUserFullName
							+ " comentou no mural da aula ";
				}else{
					infoText = activityUserFullName
							+ " comentou no mural da aula "
							+ activityStatusable.getName();
				}
				String thumbUrl = activityUser.getThumbnails().get(activityUser.getThumbnails().size()-1).getHref();
				
				bitmapManager.displayBitmapAsync(thumbUrl, holder.imgUserThumb);
				
				SpannableString mainAndSecondary = new SpannableString(
						mainAndSecondaryText);
				mainAndSecondary.setSpan(new BoldBlueClickableSpan() {
					@Override
					public void onClick(View widget) {
						super.onClick(widget);
						
						openSpaceWall(spaceInFinal);
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
				
				TextView lblMainAndSecondary = holder.lblMainAndSecondary;
				lblMainAndSecondary.setText(mainAndSecondary, BufferType.SPANNABLE);
				lblMainAndSecondary.setMovementMethod(LinkMovementMethod
						.getInstance());
				TextView lblInfo = holder.lblInfo;
				lblInfo.setText(info, BufferType.SPANNABLE);
				lblInfo.setMovementMethod(LinkMovementMethod.getInstance());
				
				LinkEnabledTextView lblText = holder.lblText;
				lblText.gatherLinksForText(activity.getText());
				lblText.setOnTextLinkClickListener(textLinkClickListener);
				lblText.setMovementMethod(LinkMovementMethod.getInstance());
			}

			view = v;
		} else if (viewType == VIEW_TYPE_HELP_STATUSED_BY_LECTURE) {
			HelpStatusedByLectureViewHolder holder;

			View v;
			if (convertView == null) {
				ViewGroup viewGroup = (ViewGroup) inflater.inflate(
						R.layout.help_statused_by_lecture, null);

				holder = new HelpStatusedByLectureViewHolder();
				holder.imgUserThumb = (ImageView) viewGroup
						.findViewById(R.id.helpStatusedByLectureImgUserThumb);
				holder.imgAnswers = (ImageView) viewGroup
						.findViewById(R.id.helpStatusedByLectureImgAnswers);
				holder.imgArrowRevolve = (ImageView) viewGroup
						.findViewById(R.id.helpStatusedByLectureImgArrowRevolve);
				holder.lblAnswer = (TextView) viewGroup
						.findViewById(R.id.helpStatusedByLectureLblAnswer);
				holder.lblAnswersCount = (TextView) viewGroup
						.findViewById(R.id.helpStatusedByLectureLblAnswersCount);
				holder.lblCreationDate = (TextView) viewGroup
						.findViewById(R.id.helpStatusedByLectureLblCreationDate);
				holder.lblInfo = (TextView) viewGroup
						.findViewById(R.id.helpStatusedByLectureLblInfo);
				holder.lblMainAndSecondary = (TextView) viewGroup
						.findViewById(R.id.helpStatusedByLectureLblMainAndSecondary);
				holder.lblText = (LinkEnabledTextView) viewGroup
						.findViewById(R.id.helpStatusedByLectureLblText);
				holder.lytRoot = (ViewGroup) viewGroup
						.findViewById(R.id.helpStatusedByLectureLytRoot);

				viewGroup.setTag(holder);

				v = viewGroup;
			} else {
				holder = (HelpStatusedByLectureViewHolder) convertView.getTag();

				v = convertView;
			}

			final Help help = (Help) statuses.get(position);

			View.OnClickListener listener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					openHelpStatusedByLectureDetails(help);
				}
			};

			holder.lblCreationDate.setText(DateFormatter.getDataPostagemEmPalavras(help
					.getCreatedAt()));
			holder.lblAnswer.setOnClickListener(listener);
			holder.imgArrowRevolve.setOnClickListener(listener);
			holder.imgAnswers.setOnClickListener(listener);

			TextView lblAnswersCount = holder.lblAnswersCount;
			lblAnswersCount.setText(String.valueOf(help.getAnswersCount()));
			lblAnswersCount.setOnClickListener(listener);

			ViewGroup lytRoot = holder.lytRoot;

			Help.State state = help.getState();
			if (state.equals(Help.State.Answered)) {
				lytRoot.setBackgroundResource(R.drawable.help_answered);
			} else if (state.equals(Help.State.Stopped)) {
				lytRoot.setBackgroundResource(R.drawable.help_stopped);
			} else {
				lytRoot.setBackgroundResource(R.drawable.help_forgotten);
			}

			final User helpUser = help.getUser();

			final Lecture helpStatusable = (Lecture) help.getStatusable();

			Subject subjectIn = helpStatusable.getSubjectIn();

			final Space spaceIn = helpStatusable.getSpaceIn();

			String helpUserFullName = helpUser.getFirstName() + " "
					+ helpUser.getLastName();
			String spaceInName = spaceIn.getName();
			String mainAndSecondaryText = spaceInName + " > "
					+ subjectIn.getName();
			String infoText = helpUserFullName + " solicitou ajuda em "
					+ helpStatusable.getName();
			String thumbUrl = helpUser.getThumbnails().get(helpUser.getThumbnails().size()-1).getHref();

			bitmapManager.displayBitmapAsync(thumbUrl, holder.imgUserThumb);

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

					openUserWall(helpUser);
				}
			}, 0, helpUserFullName.length(), 0);
			info.setSpan(new ForegroundColorSpan(ReduMobile.COLOR_ORANGE_4),
					helpUserFullName.length() + 11,
					helpUserFullName.length() + 16, 0);
			info.setSpan(new BlueClickableSpan() {
				@Override
				public void onClick(View widget) {
					super.onClick(widget);

					openLectureWall(helpStatusable);
				}
			}, helpUserFullName.length() + 20, infoText.length(), 0);

			TextView lblMainAndSecondary = holder.lblMainAndSecondary;
			lblMainAndSecondary.setText(mainAndSecondary, BufferType.SPANNABLE);
			lblMainAndSecondary.setMovementMethod(LinkMovementMethod
					.getInstance());
			TextView lblInfo = holder.lblInfo;
			lblInfo.setText(info, BufferType.SPANNABLE);
			lblInfo.setMovementMethod(LinkMovementMethod.getInstance());

			LinkEnabledTextView lblText = holder.lblText;
			lblText.gatherLinksForText(help.getText());
			lblText.setOnTextLinkClickListener(textLinkClickListener);
			lblText.setMovementMethod(LinkMovementMethod.getInstance());

			view = v;
		}

		return view;
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	private void openActivityStatusedByLectureDetails(Activity activity) {
		ActivityStatusedByLectureDetailsActivity.activity = activity;
		
		Intent intent = new Intent(context,
				ActivityStatusedByLectureDetailsActivity.class);
		intent.putExtra("activityId", activity.getId());

		context.startActivity(intent);
	}

	private void openActivityStatusedBySpaceDetails(Activity activity) {
		ActivityStatusedBySpaceDetailsActivity.activity = activity;
		
		Intent intent = new Intent(context,
				ActivityStatusedBySpaceDetailsActivity.class);
		intent.putExtra("activityId", activity.getId());

		context.startActivity(intent);
	}

	private void openActivityStatusedByUserDetails(Activity activity) {
		ActivityStatusedByUserDetailsActivity.activity = activity;

		Intent intent = new Intent(context,
				ActivityStatusedByUserDetailsActivity.class);

		context.startActivity(intent);
	}

	private void openHelpStatusedByLectureDetails(Help help) {
		HelpStatusedByLectureDetailsActivity.help = help;
		
		Intent intent = new Intent(context,
				HelpStatusedByLectureDetailsActivity.class);
		intent.putExtra("helpId", help.getId());

		context.startActivity(intent);
	}

	private void openLectureWall(Lecture lecture) {
		Intent intent = new Intent(context, LectureWallActivity.class);
		intent.putExtra("lectureId", lecture.getId());
		intent.putExtra("lectureName", lecture.getName());

		context.startActivity(intent);//OK
	}

	private void openSpaceWall(Space space) {
		Intent intent = new Intent(context, SpaceWallActivity.class);
		intent.putExtra("spaceId", space.getId());
		intent.putExtra("spaceName", space.getName());

		context.startActivity(intent);//OK
	}

	private void openUserWall(User user) {
		Intent intent = new Intent(context, UserWallActivity.class);
		intent.putExtra("userId", user.getLogin());
		intent.putExtra("userFullName",
				user.getFirstName() + " " + user.getLastName());

		context.startActivity(intent);
	}
}