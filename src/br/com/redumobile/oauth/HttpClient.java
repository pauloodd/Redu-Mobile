package br.com.redumobile.oauth;

import java.util.HashMap;

public abstract class HttpClient {
	public abstract String makeGetRequest(String url,
			HashMap<String, String> params);

	public abstract String makePostRequest(String url,
			HashMap<String, String> params);
}
