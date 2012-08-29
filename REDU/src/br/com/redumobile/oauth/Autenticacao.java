package br.com.redumobile.oauth;

import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;



public class Autenticacao {
	public static final String CONSUMER_KEY = "cE5yRp6ImS8z8hiBlajht7kcqsCdhzJRQqtbCqGZ";
	public static final String COSUMER_SECRET = "rIfHx8s8JAMcTOst2EHzZRHg9PsOEkm40Qmq9prX";
	
	public static final String ACCESS_TOKEN_URL = "http://redu.com.br/oauth/access_token";
	public static final String AUTHORIZE_URL = "http://redu.com.br/oauth/authorize";
	public static final String CALLBACK_URL = "redu://redumobile";
	
	public static final String REQUEST_TOKEN_URL = "http://redu.com.br/oauth/request_token";
	
	private String access_token;
	private OAuthService service;
	
	public Autenticacao() {
		
		service= new ServiceBuilder().provider(ReduOauth2.class)
			.apiKey(Autenticacao.CONSUMER_KEY).apiSecret(Autenticacao.COSUMER_SECRET).callback(Autenticacao.CALLBACK_URL).build();

		access_token = "";
	}

	public String getAccessToken (){
		return this.access_token;
	}
	public OAuthService getOAuthService(){
		return this.service;
	}

	public void guardarTokens(Uri uriResultado){
		
		if (uriResultado.toString().startsWith(CALLBACK_URL) == true) {

			try{
				
				String ecoded = uriResultado.getEncodedFragment();
				String array[] = ecoded.split("access_token=");
	            this.access_token = array[array.length-1];
				
				if (Aplicacao.manterConectado == true) {
					Editor editorPreferencias = Aplicacao.preferencias.edit();
					editorPreferencias.putString("access_token", access_token);
					
					editorPreferencias.commit();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean obterTokensGuardados(SharedPreferences preferencias) {
		boolean resultado = true;

		access_token = preferencias.getString("access_token", "");
		if (access_token.equals("") == true || access_token.equals("") == true) {
			resultado = false;
		}

		return resultado;
	}

	public boolean precisaLogar(SharedPreferences preferencias) {
		return !obterTokensGuardados(preferencias);
	}
}