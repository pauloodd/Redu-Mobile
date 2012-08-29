package br.com.redumobile.repository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import br.com.redumobile.entity.User;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.oauth.Aplicacao;
import br.com.redumobile.oauth.Autenticacao;
import br.com.redumobile.oauth.AuxiliarOAuth;
import br.com.redumobile.util.Util;

public class UserRepository implements IUserRepository{
	
	private static IUserRepository instance;
	
	public static IUserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

	@Override
	public User getUserME() throws RepositoryException {
		User usuarioLogado = null;
		try {
	
			OAuthRequest request = new OAuthRequest(Verb.GET, "http://redu.com.br/api/me");
			Token token = new Token(Aplicacao.autenticacao.getAccessToken(), Autenticacao.COSUMER_SECRET);
		    Aplicacao.autenticacao.getOAuthService().signRequest(token, request);
		    Response response = request.send();
		    String resposta = response.getBody();
			
			System.out.println(resposta);
			
			JSONObject objJson = new JSONObject(resposta);
			
			String primeiroNome = objJson.getString("first_name");
			String segundoNome = objJson.getString("last_name");
			String login = objJson.getString("login");
			String friends_count = objJson.getString("friends_count");
			String localization = objJson.getString("localization");
			String email = objJson.getString("email");
			String birthday = objJson.getString("birthday");
			String id = objJson.getString("id");
			JSONArray thumbs = objJson.getJSONArray("thumbnails");
			JSONObject imagemPerfil = thumbs.getJSONObject(1);
			String urlImagem = imagemPerfil.getString("href");
			
			usuarioLogado = new User();
			usuarioLogado.setFirstName(primeiroNome);
			usuarioLogado.setLastName(segundoNome);
			usuarioLogado.setLogin(login);
			usuarioLogado.setId(id);
			usuarioLogado.setLocalization(localization);
			usuarioLogado.setEmail(email);
			usuarioLogado.setFriends_count(Integer.parseInt(friends_count));
			usuarioLogado.setBirthday(Util.convertStringToDate(birthday));
			usuarioLogado.setUrlFotoPerfil(urlImagem);
		
		} catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		} 
		 
		return usuarioLogado;
	}
	
	
	@Override
	public User getUserPorLogin(String login) throws RepositoryException {
		User usuario = null;
		try {

			String url = "http://redu.com.br/api/users/"+login;
			String resposta = AuxiliarOAuth.obterRespostaUrlViaGet(url, null);
			
			System.out.println(resposta);
			
			JSONObject objJson = new JSONObject(resposta);
			
			String primeiroNome = objJson.getString("first_name");
			String segundoNome = objJson.getString("last_name");
			String friends_count = objJson.getString("friends_count");
			String localization = objJson.getString("localization");
			String email = objJson.getString("email");
			String birthday = objJson.getString("birthday");
			String id = objJson.getString("id");
			JSONArray thumbs = objJson.getJSONArray("thumbnails");
			JSONObject imagemPerfil = thumbs.getJSONObject(1);
			String urlImagem = imagemPerfil.getString("href");
			
			usuario = new User();
			usuario.setFirstName(primeiroNome);
			usuario.setLastName(segundoNome);
			usuario.setLogin(login);
			usuario.setId(id);
			usuario.setLocalization(localization);
			usuario.setEmail(email);
			usuario.setFriends_count(Integer.parseInt(friends_count));
			usuario.setBirthday(Util.convertStringToDate(birthday));
			usuario.setUrlFotoPerfil(urlImagem);
		
		} catch (Exception e) {
			if(e.getMessage().contains("There was a problem while creating a connection to the remote service.")){
				return null;
			}else{
				throw new RepositoryException(e.getMessage());
			}
		} 
		 
		return usuario;
	}

	

}
