package br.com.redumobile.oauth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.scribe.utils.OAuthEncoder;

import br.com.redumobile.entity.Activity;
import br.com.redumobile.entity.Answer;
import br.com.redumobile.entity.Course;
import br.com.redumobile.entity.Help;
import br.com.redumobile.entity.Lecture;
import br.com.redumobile.entity.ReduEntityWithWall;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.StatusAnswerable;
import br.com.redumobile.entity.Subject;
import br.com.redumobile.entity.Thumbnail;
import br.com.redumobile.entity.User;
import br.com.redumobile.util.DateFormatter;

public final class ReduClient {
	public static final int MAX_CHARS_COUNT_IN_POST = 800;
	private final String ACCESS_TOKEN_URL = "http://redu.com.br/oauth/access_token";
	private final String AUTHORIZE_URL = "http://redu.com.br/oauth/authorize";
	
	private String callbackUrl;
	private HttpClient client;
	private String consumerKey;
	private String consumerSecret;
	private final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZZZ";
	private DateFormatter dateFormatter;
	private boolean initialized;
	private ThreadLocal<OAuthService> service;
	private Token token;

	public ReduClient(String consumerKey, String consumerSecret,
			String callbackUrl) {
		dateFormatter = new DateFormatter(DATE_FORMAT);

		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.callbackUrl = callbackUrl;

		service = new ThreadLocal<OAuthService>() {
			@Override
			protected OAuthService initialValue() {
				return new ServiceBuilder()
						.provider(new DefaultApi20() {
							@Override
							public String getAccessTokenEndpoint() {
								return ACCESS_TOKEN_URL;
							}

							@Override
							public AccessTokenExtractor getAccessTokenExtractor() {
								return new JsonTokenExtractor();
							}

							@Override
							public Verb getAccessTokenVerb() {
								return Verb.POST;
							}

							@Override
							public String getAuthorizationUrl(OAuthConfig config) {
								return String
										.format(AUTHORIZE_URL
												+ "?client_id=%s&redirect_uri=%s&response_type=token",
												config.getApiKey(),
												OAuthEncoder
														.encode(ReduClient.this.callbackUrl));
							}
						}).apiKey(ReduClient.this.consumerKey)
						.apiSecret(ReduClient.this.consumerSecret)
						.callback(ReduClient.this.callbackUrl).build();
			}
		};

		client = new HttpClient() {
			@Override
			public String makeGetRequest(String url,
					HashMap<String, String> params) {
				OAuthRequest request = new OAuthRequest(Verb.GET, url);

				if (params != null) {
					Set<Map.Entry<String, String>> entrySet = params.entrySet();
					for (Map.Entry<String, String> entry : entrySet) {
						request.addQuerystringParameter(entry.getKey(),
								entry.getValue());
					}
				}

				service.get().signRequest(token, request);

				Response response = request.send();

				String body = response.getBody();

				return body;
			}

			@Override
			public String makePostRequest(String url,
					HashMap<String, String> params) {
				OAuthRequest request = new OAuthRequest(Verb.POST, url);

				if (params != null) {
					Set<Map.Entry<String, String>> entrySet = params.entrySet();
					for (Map.Entry<String, String> entry : entrySet) {
						request.addBodyParameter(entry.getKey(),
								entry.getValue());
					}
				}

				service.get().signRequest(token, request);

				Response response = request.send();

				String body = response.getBody();

				return body;
			}
		};
	}

	private String cutText(String s) {
		String cutText = s.length() <= MAX_CHARS_COUNT_IN_POST ? s : s
				.substring(0, MAX_CHARS_COUNT_IN_POST);

		return cutText;
	}

	private ArrayList<Answer> getAnswers(final StatusAnswerable inResponseTo) {
		ArrayList<Answer> answers = null;

		String url = "http://www.redu.com.br/api/statuses/:status_id/answers"
				.replace(":status_id", String.valueOf(inResponseTo.getId()));
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			answers = new ArrayList<Answer>();

			ExecutorService threadPool = Executors
					.newCachedThreadPool(new ThreadFactory() {
						@Override
						public Thread newThread(Runnable r) {
							Thread thread = Executors.defaultThreadFactory()
									.newThread(r);
							thread.setDaemon(true);

							return thread;
						}
					});

			ExecutorCompletionService<Answer> pool = new ExecutorCompletionService<Answer>(
					threadPool);

			try {
				JSONArray json = new JSONArray(response);
				for (int i = 0; i < json.length(); i++) {
					final JSONObject answerJson = json.getJSONObject(i);

					pool.submit(new Callable<Answer>() {
						@Override
						public Answer call() throws Exception {
							return parseAnswer(answerJson, inResponseTo);
						}
					});
				}
				for (int i = 0; i < json.length(); i++) {
					Answer answer = pool.take().get();

					answers.add(answer);
				}
			} catch (JSONException e) {
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}

			threadPool.shutdownNow();

			Collections.sort(answers, Status.InvertedComparator.getInstance());
		}

		return answers;
	}

	public ArrayList<Answer> getAnswers(String statusId) {
		StatusAnswerable inResponseTo = (StatusAnswerable) getStatus(statusId,
				false);

		ArrayList<Answer> answers = getAnswers(inResponseTo);

		return answers;
	}

	public String getAuthorizationUrl() {
		return service.get().getAuthorizationUrl(null);
	}

	public Course getCourse(String courseId) {
		Course course = null;

		String url = "http://www.redu.com.br/api/courses/:id".replace(":id",
				courseId);
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);

				course = parseCourse(json);
			} catch (JSONException e) {
			}
		}

		return course;
	}

	public Lecture getLecture(String lectureId) {
		Lecture lecture = null;

		String url = "http://www.redu.com.br/api/lectures/:id".replace(":id",
				lectureId);
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);

				lecture = parseLecture(json);
			} catch (JSONException e) {
			}
		}

		return lecture;
	}

	public ArrayList<Status> getLectureStatuses(String lectureId, int page,
			boolean withAnswers) {
		return getStatuses(
				"http://www.redu.com.br/api/lectures/:lecture_id/statuses".replace(
						":lecture_id", lectureId), page, withAnswers);
	}

	public User getMe() {
		User me = null;

		String url = "http://www.redu.com.br/api/me";
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);

				me = parseUser(json);
			} catch (JSONException e) {
			}
		}

		return me;
	}

	public Space getSpace(String spaceId) {
		Space space = null;

		String url = "http://www.redu.com.br/api/spaces/:id".replace(":id",
				spaceId);
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);

				space = parseSpace(json);
			} catch (JSONException e) {
			}
		}

		return space;
	}
	

	public ArrayList<Status> getSpaceTimeline(String spaceId, int page,
			boolean withAnswers) {
		return getStatuses(
				"http://www.redu.com.br/api/spaces/:space_id/statuses/timeline".replace(
						":space_id", spaceId), page, withAnswers);
	}

	public Status getStatus(String statusId, boolean withAnswers) {
		Status status = null;

		String url = "http://www.redu.com.br/api/statuses/:status_id".replace(
				":status_id", statusId);
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);

				status = parseStatus(json, withAnswers);
			} catch (JSONException e) {
			}
		}

		return status;
	}

	private ReduEntityWithWall getStatusable(User statusUser, String linkHref) {
		ReduEntityWithWall statusable = null;
		
		if(linkHref.contains("tonix35")){
			System.out.println("aa");
		}

		if (linkHref.contains("http://www.redu.com.br/api/users/")) {
			String userId = linkHref.replace(
					"http://www.redu.com.br/api/users/", "");
			if (userId.equals(statusUser.getLogin())) {
				statusable = (ReduEntityWithWall) statusUser.clone();
			} else {
				statusable = getUser(userId);
			}
		} else if (linkHref.contains("http://www.redu.com.br/api/spaces/")) {
			String spaceId = linkHref.replace(
					"http://www.redu.com.br/api/spaces/", "");

			statusable = getSpace(spaceId);
		} else if (linkHref.contains("http://www.redu.com.br/api/lectures/")) {
			String lectureId = linkHref.replace(
					"http://www.redu.com.br/api/lectures/", "");
			lectureId = lectureId.substring(0, lectureId.indexOf("-"));

			statusable = getLecture(lectureId);
		}

		return statusable;
	}

	private ArrayList<Status> getStatuses(String url, int page,
			final boolean withAnswers) {
		ArrayList<Status> statuses = null;

		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("page", String.valueOf(page));
		
		/*if(url != null && url.contains("timeline")){
			params.put("type", "Activity");
			
		}*/

		String response = null;
		try {
			response = client.makeGetRequest(url, params);
		} catch (OAuthException e) {
			System.out.println(e.getMessage());
		}
		if (response != null) {
			statuses = new ArrayList<Status>();

			ExecutorService threadPool = Executors
					.newCachedThreadPool(new ThreadFactory() {
						@Override
						public Thread newThread(Runnable r) {
							Thread thread = Executors.defaultThreadFactory()
									.newThread(r);
							thread.setDaemon(true);

							return thread;
						}
					});

			ExecutorCompletionService<Status> pool = new ExecutorCompletionService<Status>(
					threadPool);

			try {
				JSONArray json = new JSONArray(response);
				for (int i = 0; i < json.length(); i++) {
					final JSONObject statusJson = json.getJSONObject(i);

					pool.submit(new Callable<Status>() {
						@Override
						public Status call() throws Exception {
							return parseStatus(statusJson, withAnswers);
						}
					});
				}
				for (int i = 0; i < json.length(); i++) {
					Status status = pool.take().get();
					if (status != null) {
						if (status instanceof Activity) {
							Activity activity = (Activity) status;
							if(activity.getStatusable() == null && activity.getUser() != null){
								String userId = url.replace("http://www.redu.com.br/api/users/", "").replace("/statuses/timeline", "");
								activity.setStatusable(this.getUser(userId));
							}
						}
						statuses.add(status);
					}
				}
			} catch (JSONException e) {
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}

			threadPool.shutdownNow();

			Collections.sort(statuses, Status.DefaultComparator.getInstance());
		}

		return statuses;
	}

	public Subject getSubject(String subjectId) {
		Subject subject = null;

		String url = "http://www.redu.com.br/api/subjects/:id".replace(":id",
				subjectId);
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);

				subject = parseSubject(json);
			} catch (JSONException e) {
			}
		}

		return subject;
	}

	public User getUser(String userId) {
		User user = null;

		String url = "http://www.redu.com.br/api/users/:id".replace(":id",
				userId);
		String response = null;
		try {
			response = client.makeGetRequest(url, null);
		} catch (OAuthException e) {
		}
		if (response != null) {
			try {
				JSONObject json = new JSONObject(response);

				user = parseUser(json);
			} catch (JSONException e) {
			}
		}

		return user;
	}

	public ArrayList<Status> getUserTimeline(String userId, int page,
			boolean withAnswers) {
		return getStatuses(
				"http://www.redu.com.br/api/users/:user_id/statuses/timeline".replace(
						":user_id", userId), page, withAnswers);
	}

	public void initClient(String accessToken) {
		synchronized (this) {
			if (!initialized) {
				token = new Token(accessToken, consumerSecret);

				initialized = true;
			} else {
				throw new RuntimeException("O cliente não foi inicializado");
			}
		}
	}

	public boolean isInitialized() {
		synchronized (this) {
			return initialized;
		}
	}

	private Activity parseActivity(JSONObject activityJson, boolean withAnswers)
			throws JSONException {
		int id = Integer.parseInt(activityJson.getString("id"));
		

		Date updatedAt = dateFormatter.parse(activityJson
				.getString("updated_at"));
		Date createdAt = dateFormatter.parse(activityJson
				.getString("created_at"));
		
		String text = activityJson.getString("text");

		User user = parseUser(activityJson.getJSONObject("user"));

		ReduEntityWithWall statusable = null;

		JSONArray linksJson = activityJson.getJSONArray("links");
		for (int j = 0; j < linksJson.length(); j++) {
			JSONObject linkJson = linksJson.getJSONObject(j);

			String linkRel = linkJson.getString("rel");
			if (linkRel.equals("statusable")) {
				ReduEntityWithWall statusableRetorno = getStatusable(user, linkJson.getString("href"));

				if(statusableRetorno != null){
					statusable = statusableRetorno;
				}
				
			}else if(linkRel.equals("wall")){
				String href = linkJson.getString("href");
				
				if(href.contains("http://www.redu.com.br/api/users/")){
					String name = linkJson.getString("name");
					User userStatusable = new User();
					userStatusable.setFirstName(name);
					userStatusable.setLastName("");
					String login = href.replace("http://www.redu.com.br/api/users/", "");
					userStatusable.setLogin(login);
					ArrayList<Thumbnail> thumbnails = new ArrayList<Thumbnail>();
					thumbnails.add(new Thumbnail("http://redu-assets.s3.amazonaws.com/images/new/missing_users_thumb_110.png", "110"));
					userStatusable.setThumbnails(thumbnails);
					
					statusable = userStatusable;
				}
			}
		}
		
		String answersCount = activityJson.getString("answers_count");
		int answersQtd = 0;
		if(answersCount != null && !answersCount.equals("")){
			answersQtd = Integer.parseInt(answersCount);
		}

		Activity activity = new Activity(createdAt, updatedAt, id, text, user,
				null, statusable,answersQtd);

		if (withAnswers) {
			activity.setAnswers(getAnswers(activity));
		}

		return activity;
	}

	private Answer parseAnswer(JSONObject answerJson,
			StatusAnswerable inResponseTo) throws JSONException {
		int id = Integer.parseInt(answerJson.getString("id"));

		Date updatedAt = dateFormatter
				.parse(answerJson.getString("updated_at"));
		Date createdAt = dateFormatter
				.parse(answerJson.getString("created_at"));

		String text = answerJson.getString("text");

		if (inResponseTo == null) {
			JSONArray linksJson = answerJson.getJSONArray("links");
			for (int j = 0; j < linksJson.length(); j++) {
				JSONObject linkJson = linksJson.getJSONObject(j);

				String linkRel = linkJson.getString("rel");
				if (linkRel.equals("in_response_to")) {
					String linkHref = linkJson.getString("href");
					String statusId = linkHref.replace(
							"http://www.redu.com.br/api/statuses/", "");

					inResponseTo = (StatusAnswerable) getStatus(statusId, false);

					break;
				}
			}
		}

		User user = parseUser(answerJson.getJSONObject("user"));

		Answer answer = new Answer(createdAt, updatedAt, id, text, user,
				inResponseTo);

		return answer;
	}

	private Course parseCourse(JSONObject courseJson) throws JSONException {
		int id = courseJson.getInt("id");

		String name = courseJson.getString("name");

		Course course = new Course(null, null, id, null, null, 0, null, null,
				null, name);

		return course;
	}

	private Help parseHelp(JSONObject helpJson, boolean withAnswers)
			throws JSONException {
		int id = Integer.parseInt(helpJson.getString("id"));

		Date createdAt = dateFormatter.parse(helpJson.getString("created_at"));
		Date updatedAt = dateFormatter.parse(helpJson.getString("updated_at"));

		String text = helpJson.getString("text");

		User user = parseUser(helpJson.getJSONObject("user"));

		ReduEntityWithWall statusable = null;

		JSONArray linksJson = helpJson.getJSONArray("links");
		for (int j = 0; j < linksJson.length(); j++) {
			JSONObject linkJson = linksJson.getJSONObject(j);

			String linkRel = linkJson.getString("rel");
			if (linkRel.equals("statusable")) {
				statusable = getStatusable(user, linkJson.getString("href"));

				break;
			}
		}


		String answersCount = helpJson.getString("answers_count");
		int answersQtd = 0;
		if(answersCount != null && !answersCount.equals("")){
			answersQtd = Integer.parseInt(answersCount);
		}

		Help help = new Help(createdAt, updatedAt, id, text, user, null,
				statusable, null,answersQtd);

		if (withAnswers) {
			Help.State state;

			ArrayList<Answer> answers = getAnswers(help);
			if (answers.size() > 0) {
				state = Help.State.Answered;
			} else {
				Date now = new Date();
				Date sevenDaysAfterCreation = (Date) createdAt.clone();
				sevenDaysAfterCreation
						.setDate(sevenDaysAfterCreation.getDate() + 7);
				if (now.after(sevenDaysAfterCreation)) {
					state = Help.State.Forgotten;
				} else {
					state = Help.State.Stopped;
				}
			}

			help.setAnswers(answers);
			help.setState(state);
		}else{
			
			Help.State state;
			if (answersQtd > 0) {
				state = Help.State.Answered;
			}else {
				Date now = new Date();
				Date sevenDaysAfterCreation = (Date) createdAt.clone();
				sevenDaysAfterCreation
						.setDate(sevenDaysAfterCreation.getDate() + 7);
				if (now.after(sevenDaysAfterCreation)) {
					state = Help.State.Forgotten;
				} else {
					state = Help.State.Stopped;
				}
			}
			
			help.setState(state);
		}

		return help;
	}

	private Lecture parseLecture(JSONObject lectureJson) throws JSONException {
		int id = lectureJson.getInt("id");

		String name = lectureJson.getString("name");

		Subject subjectIn = null;

		JSONArray linksJson = lectureJson.getJSONArray("links");
		for (int i = 0; i < linksJson.length(); i++) {
			JSONObject linkJson = linksJson.getJSONObject(i);

			String linkRel = linkJson.getString("rel");
			if (linkRel.equals("subject")) {
				String linkHref = linkJson.getString("href");
				String subjectId = linkHref.replace(
						"http://www.redu.com.br/api/subjects/", "");

				subjectIn = getSubject(subjectId);

				break;
			}
		}

		Lecture lecture = new Lecture(null, null, id, null, null,
				subjectIn.getCourseIn(), subjectIn.getEnvironmentIn(), null,
				name, 0, 0, subjectIn.getSpaceIn(), subjectIn, 0);

		return lecture;
	}

	private Space parseSpace(JSONObject spaceJson) throws JSONException {
		int id = spaceJson.getInt("id");

		Course courseIn = null;

		JSONArray linksJson = spaceJson.getJSONArray("links");
		for (int i = 0; i < linksJson.length(); i++) {
			JSONObject linkJson = linksJson.getJSONObject(i);

			String linkRel = linkJson.getString("rel");
			if (linkRel.equals("course")) {
				String linkHref = linkJson.getString("href");
				String courseId = linkHref.replace(
						"http://www.redu.com.br/api/courses/", "");

				courseIn = getCourse(courseId);

				break;
			}
		}

		String name = spaceJson.getString("name");

		Space space = new Space(null, null, id, null, null,
				courseIn.getEnvironmentIn(), courseIn, null, null, name, null);

		return space;
	}

	private Status parseStatus(JSONObject statusJson, boolean withAnswers)
			throws JSONException {
		Status status = null;

		String type = statusJson.getString("type");
		if (type.equals("Activity")) {
			status = parseActivity(statusJson, withAnswers);
		} else if (type.equals("Help")) {
			status = parseHelp(statusJson, withAnswers);
		} else if (type.equals("Answer")) {
			status = parseAnswer(statusJson, null);
		}

		return status;
	}

	private Subject parseSubject(JSONObject subjectJson) throws JSONException {
		int id = subjectJson.getInt("id");

		Space spaceIn = null;

		JSONArray linksJson = subjectJson.getJSONArray("links");
		for (int i = 0; i < linksJson.length(); i++) {
			JSONObject linkJson = linksJson.getJSONObject(i);

			String linkRel = linkJson.getString("rel");
			if (linkRel.equals("space")) {
				String linkHref = linkJson.getString("href");
				String spaceId = linkHref.substring(
						linkHref.lastIndexOf("/") + 1, linkHref.length());

				spaceIn = getSpace(spaceId);

				break;
			}
		}

		String name = subjectJson.getString("name");

		Subject subject = new Subject(null, null, id, null,
				spaceIn.getEnvironmentIn(), spaceIn.getCourseIn(), null,
				spaceIn, name);

		return subject;
	}

	private User parseUser(JSONObject userJson) throws JSONException {
		ArrayList<Thumbnail> thumbnails = new ArrayList<Thumbnail>();

		JSONArray thumbsJson = userJson.getJSONArray("thumbnails");
		for (int i = 0; i < thumbsJson.length(); i++) {
			JSONObject thumbJson = thumbsJson.getJSONObject(i);

			String thumbSize = thumbJson.getString("size");
			String thumbHref = thumbJson.getString("href");

			Thumbnail thumbnail = new Thumbnail(thumbHref, thumbSize);

			thumbnails.add(thumbnail);
		}

		String firstName = userJson.getString("first_name");
		String lastName = userJson.getString("last_name");
		String login = userJson.getString("login");

		User user = new User(null, null, 0, null, null, null, null, null, null,
				null, null, login, null, thumbnails, firstName, 0, null, null,
				lastName);

		return user;
	}

	public String postLectureActivity(String lectureId, String text) {
		return postLectureStatus(lectureId, text, "Activity");
	}

	public String postLectureHelp(String lectureId, String text) {
		return postLectureStatus(lectureId, text, "Help");
	}

	public String postLectureStatus(String lectureId, String text, String type) {
		String url = "http://www.redu.com.br/api/lectures/:lecture_id/statuses"
				.replace(":lecture_id", lectureId);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("status[text]", cutText(text));
		params.put("status[type]", type);

		return postStatus(url, params);
	}

	public String postSpaceStatus(String spaceId, String text) {
		String url = "http://www.redu.com.br/api/spaces/:space_id/statuses"
				.replace(":space_id", spaceId);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("status[text]", cutText(text));

		return postStatus(url, params);
	}

	public String postStatusAnswer(String statusId, String text) {
		String url = "http://www.redu.com.br/api/statuses/:status_id/answers"
				.replace(":status_id", statusId);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("status[text]", cutText(text));

		return postStatus(url, params);
	}

	private String postStatus(String url, HashMap<String, String> params) {
		String retorno = null;
		try {
			retorno = client.makePostRequest(url, params);
		} catch (OAuthException e) {
			retorno = null;
		}

		return retorno;
	}

	public String postUserStatus(String userId, String text) {
		String url = "http://www.redu.com.br/api/users/:user_id/statuses"
				.replace(":user_id", userId);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("status[text]", cutText(text));

		return postStatus(url, params);
	}
}