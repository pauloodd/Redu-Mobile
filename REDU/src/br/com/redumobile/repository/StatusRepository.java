package br.com.redumobile.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.redumobile.entity.Activity;
import br.com.redumobile.entity.Answer;
import br.com.redumobile.entity.Help;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.User;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.oauth.Aplicacao;
import br.com.redumobile.oauth.AuxiliarOAuth;
import br.com.redumobile.util.Util;

public class StatusRepository implements IStatusRepository{
	
	private static IStatusRepository instance;
	
	public static IStatusRepository getInstance() {
        if (instance == null) {
            instance = new StatusRepository();
        }
        return instance;
    }

	@Override
	public List<Status> getPostagens(int page)
			throws RepositoryException {
		
		List<Status> postagens = new ArrayList<Status>();
		
		String urlTimeline = "http://redu.com.br/api/users/:user_id/statuses/timeline".replace(":user_id", Aplicacao.getUsuario().getLogin());
		String ret;
		
		try {
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("page", ""+page);
			
			ret = AuxiliarOAuth.obterRespostaUrlViaGet(urlTimeline, parametros);
			
			JSONArray array = new JSONArray(ret);
			
			for(int i = 0; i < array.length(); i++){
				JSONObject objJson = array.getJSONObject(i);
				
				String id = objJson.getString("id");
				String created_at = objJson.getString("created_at");
				String text = objJson.getString("text");
				String type = objJson.getString("type");
				JSONArray links = objJson.getJSONArray("links");
				JSONObject objJsonUSer = objJson.getJSONObject("user");
				
				if(type != null  && type.equals("Activity")){
					
					User usuario = contruirUsuarioAutor(objJsonUSer, id);
					
					JSONObject linkSelf = links.getJSONObject(0);
					String self = linkSelf.getString("href");

					JSONObject linkStatusable = links.getJSONObject(1);
					String statusable = linkStatusable.getString("href");
					
					JSONObject linkAnswer = links.getJSONObject(3);
					String answer = linkAnswer.getString("href");
					
					Activity activity = new Activity();
					activity.setAutor(usuario);
					activity.setId(id);
					activity.setStatusable(statusable);
					activity.setText(text);
					activity.setAnswer(answer);
					activity.setSelfHref(self);
					Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
					activity.setDataCriacao(dataCriacao);
					
					postagens.add(activity);
				}else if(type != null  && type.equals("Help")){
					
					User usuario = contruirUsuarioAutor(objJsonUSer, id);
					
					JSONObject linkSelf = links.getJSONObject(0);
					String self = linkSelf.getString("href");
					
					JSONObject linkStatusable = links.getJSONObject(1);
					String statusable = linkStatusable.getString("href");
					
					JSONObject linkAnswer = links.getJSONObject(3);
					String answer = linkAnswer.getString("href");
					
					Help help = new Help();
					help.setAutor(usuario);
					help.setId(id);
					help.setStatusable(statusable);
					help.setText(text);
					help.setSelfHref(self);
					help.setAnswer(answer);
					Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
					help.setDataCriacao(dataCriacao);
					
					postagens.add(help);
				}
				
			}
		} catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		} 
		
		return postagens;
	}

	private User contruirUsuarioAutor(JSONObject objJson, String id)
			throws JSONException {
		String primeiroNome = objJson.getString("first_name");
		String segundoNome = objJson.getString("last_name");
		String friends_count = objJson.getString("friends_count");
//		String localization = objJson.getString("localization");
		String email = objJson.getString("email");
		String birthday = objJson.getString("birthday");
		String idUser = objJson.getString("id");
		JSONArray thumbs = objJson.getJSONArray("thumbnails");
		JSONObject imagemPerfil = thumbs.getJSONObject(1);
		String urlImagem = imagemPerfil.getString("href");
		
		User usuario = new User();
		usuario.setFirstName(primeiroNome);
		usuario.setLastName(segundoNome);
		usuario.setLogin(idUser);
		usuario.setId(id);
//		usuario.setLocalization(localization);
		usuario.setEmail(email);
		usuario.setFriends_count(Integer.parseInt(friends_count));
		usuario.setBirthday(Util.convertStringToDate(birthday));
		usuario.setUrlFotoPerfil(urlImagem);
		return usuario;
	}

	@Override
	public List<Status> pesquisaRepostasStatus(String statusable)
			throws RepositoryException {
		
		List<Status> respostas = new ArrayList<Status>();
		String ret;
		
		try {
			ret = AuxiliarOAuth.obterRespostaUrlViaGet(statusable, null);
			
			JSONArray array = new JSONArray(ret);
			
			for(int i = 0; i < array.length(); i++){
				JSONObject objJson = array.getJSONObject(i);
				
				String id = objJson.getString("id");
				String created_at = objJson.getString("created_at");
				String text = objJson.getString("text");
				String type = objJson.getString("type");
				JSONArray links = objJson.getJSONArray("links");
				JSONObject objJsonUSer = objJson.getJSONObject("user");
				
				if(type != null  && type.equals("Activity")){
					
					User usuario = contruirUsuarioAutor(objJsonUSer, id);
					
					JSONObject linkStatusable = links.getJSONObject(1);
					String statusablePost = linkStatusable.getString("href");
					
					JSONObject linkAnswer = links.getJSONObject(3);
					String answer = linkAnswer.getString("href");
					
					Activity activity = new Activity();
					activity.setAutor(usuario);
					activity.setId(id);
					activity.setStatusable(statusablePost);
					activity.setText(text);
					activity.setAnswer(answer);
					Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
					activity.setDataCriacao(dataCriacao);
					
					respostas.add(activity);
				}else if(type != null  && type.equals("Help")){
					
					User usuario = contruirUsuarioAutor(objJsonUSer, id);
					
					JSONObject linkStatusable = links.getJSONObject(1);
					String statusablePost = linkStatusable.getString("href");
					
					JSONObject linkAnswer = links.getJSONObject(3);
					String answer = linkAnswer.getString("href");
					
					Help help = new Help();
					help.setAutor(usuario);
					help.setId(id);
					help.setStatusable(statusablePost);
					help.setText(text);
					help.setAnswer(answer);
					Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
					help.setDataCriacao(dataCriacao);
					
					respostas.add(help);
				}else if(type != null && type.equals("Answer")){
					
					
					User usuario = contruirUsuarioAutor(objJsonUSer, id);
					
					JSONObject linkStatusable = links.getJSONObject(1);
					String statusablePost = linkStatusable.getString("href");
					
					Answer answerNova = new Answer();
					answerNova.setAutor(usuario);
					answerNova.setId(id);
					answerNova.setStatusable(statusablePost);
					answerNova.setText(text);
					Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
					answerNova.setDataCriacao(dataCriacao);
					
					respostas.add(answerNova);
					
				}
				
			}
		} catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		} 
		
		return respostas;
	}
	
	public String enviarResposta(String statuses, String texto, String tipo) throws RepositoryException {
		String retorno = null;
		try{
			
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("status[text]", texto);
			if(tipo.equals("Help")){
				parametros.put("status[type]", tipo);
			}
			
			AuxiliarOAuth.obterRespostaUrlViaPostSemResposta(statuses, parametros);
			
			retorno = "enviado";
		}catch (Exception e) {
			throw new RepositoryException(e.getMessage());
		}
		return retorno;
	}

	
}
