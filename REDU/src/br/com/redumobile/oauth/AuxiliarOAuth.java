package br.com.redumobile.oauth;

import java.util.ArrayList;
import java.util.HashMap;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

public class AuxiliarOAuth {
	
	public static String obterRespostaUrlViaGet(String url, HashMap<String, String> parametros){
		
		
		if(parametros != null && !parametros.isEmpty()){
			ArrayList<String> keys = new ArrayList<String>(parametros.keySet());
			ArrayList<String> values = new ArrayList<String>(parametros.values());
			
			for(int i = 0;i<keys.size();i++){
				if(i == 0){
					url=url+("?"+keys.get(i)+"="+ values.get(i));
				}else{
					url=url+("&"+keys.get(i)+"="+ values.get(i));
				}
			}
		}
		
		OAuthRequest request =  new OAuthRequest(Verb.GET, url);
		Token token = new Token(Aplicacao.autenticacao.getAccessToken(), Autenticacao.COSUMER_SECRET);
		
	    Aplicacao.autenticacao.getOAuthService().signRequest(token, request);
	    
	    Response response = request.send();
	    String resposta = response.getBody();
	    
	    return resposta;
	}
	
	public static String obterRespostaUrlViaPost(String url, HashMap<String, String> parametros){
		
		OAuthRequest request = new OAuthRequest(Verb.POST, url);
		Token token = new Token(Aplicacao.autenticacao.getAccessToken(), Autenticacao.COSUMER_SECRET);
		
		if(parametros != null && !parametros.isEmpty()){
			ArrayList<String> keys = new ArrayList<String>(parametros.keySet());
			ArrayList<String> values = new ArrayList<String>(parametros.values());
			
			for(int i = 0;i<keys.size();i++){
				request.addBodyParameter(keys.get(i), values.get(i));
			}
		}
	
		Aplicacao.autenticacao.getOAuthService().signRequest(token, request);
	    
	    Response response = request.send();
	    String resposta = response.getBody();
	    
	    return resposta;
	}
	
	public static void obterRespostaUrlViaPostSemResposta(String url, HashMap<String, String> parametros){
		
		OAuthRequest request = new OAuthRequest(Verb.POST, url);
		Token token = new Token(Aplicacao.autenticacao.getAccessToken(), Autenticacao.COSUMER_SECRET);
		
		if(parametros != null && !parametros.isEmpty()){
			ArrayList<String> keys = new ArrayList<String>(parametros.keySet());
			ArrayList<String> values = new ArrayList<String>(parametros.values());
			
			for(int i = 0;i<keys.size();i++){
				request.addBodyParameter(keys.get(i), values.get(i));
			}
		}
	
		Aplicacao.autenticacao.getOAuthService().signRequest(token, request);
	    
	    request.send();
	    
	}

}
