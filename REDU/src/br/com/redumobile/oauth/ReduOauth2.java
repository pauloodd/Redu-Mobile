package br.com.redumobile.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

public class ReduOauth2 extends DefaultApi20 {

	private static final String AUTHORIZE_URL = "http://redu.com.br/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=token";

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "http://redu.com.br/oauth/access_token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {

		return String.format(AUTHORIZE_URL, config.getApiKey(),
				OAuthEncoder.encode("redu://redumobile"));
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

	

}
