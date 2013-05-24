package br.com.redumobile;

import greendroid.app.GDApplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import br.com.redumobile.entity.User;
import br.com.redumobile.gui.LoginActivity;
import br.com.redumobile.oauth.ReduClient;

public class ReduMobile extends GDApplication {
	public static final int COLOR_BLUE_2 = Color.parseColor("#73C3E6");
	public static final int COLOR_ORANGE_4 = Color.parseColor("#FF7C35");
	private static ReduMobile singleton;
	private String accessToken;
	private final String CALLBACK_URL = "redu://redudroid";
	private ReduClient client;
	

    public final String CONSUMER_KEY = "solicite as suas credenciais enviando email para contato@redu.com.br";
    public final String CONSUMER_SECRET = "solicite as suas credenciais enviando email para contato@redu.com.br";
	
	
	public static final String NOME_APP_MD5_SHA1 = "b48d62a8c521747b88ce77f95931537093933249";
	public static SharedPreferences preferences;
	private boolean keepConnected;
	private String userLogin;
	
	public static void inicializar(Context contexto) {
		preferences = getSharedPreferences(contexto);
	}

	public static ReduMobile getInstance() {
		if(singleton == null){
			singleton = new ReduMobile();
		}
		return singleton;
	}

	public boolean captureUserLogin(Uri uri) {
		boolean captured = true;

		if (uri.toString().startsWith(CALLBACK_URL)) {
			String[] array = uri.getEncodedFragment().replace("&state=", "")
					.split("access_token=");

			accessToken = array[1];

			getClient().initClient(accessToken);

			User me = getClient().getMe();
			if (me != null) {
				userLogin = me.getLogin();

				if (keepConnected) {

					SharedPreferences.Editor preferencesEditor = preferences
							.edit();
					preferencesEditor.putString("userLogin", userLogin);
					preferencesEditor.putString("accessToken", accessToken);
					preferencesEditor.commit();
				}
			} else {
				captured = false;
			}
		} else {
			captured = false;
		}

		return captured;
	}

	public void deleteUserDate() {
		userLogin = null;
		accessToken = null;

		client = null;

		SharedPreferences.Editor preferencesEditor = preferences.edit();
		preferencesEditor.remove("userLogin");
		preferencesEditor.remove("accessToken");
		preferencesEditor.commit();
	}

	public ReduClient getClient() {
		if (client == null) {
			client = new ReduClient(CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL);
		}

		return client;
	}

	public static SharedPreferences getSharedPreferences(Context contexto) {
		return contexto.getSharedPreferences(NOME_APP_MD5_SHA1, MODE_PRIVATE);
	}

	public String getUserLogin() {
		return userLogin;
	}

	public boolean isKeepConnected() {
		return keepConnected;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		singleton = this;

		client = new ReduClient(CONSUMER_KEY, CONSUMER_SECRET, CALLBACK_URL);
	}

	public boolean retrieveUserDate() {
		boolean retrieved = true;

		userLogin = preferences.getString("userLogin", "");
		accessToken = preferences.getString("accessToken", "");
		if (userLogin.equals("") || accessToken.equals("")) {
			retrieved = false;
		} else {
			if (!getClient().isInitialized()) {
				getClient().initClient(accessToken);
			}
		}

		return retrieved;
	}

	public void setKeepConnected(boolean keepConnected) {
		this.keepConnected = keepConnected;
	}
	
	 @Override
	    public Class<?> getHomeActivityClass() {
	        return LoginActivity.class;
	    }

	    @Override
	    public Intent getMainApplicationIntent() {
	        return new Intent(Intent.ACTION_VIEW, Uri.parse(CALLBACK_URL));
	    }
}
