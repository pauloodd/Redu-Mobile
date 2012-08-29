package br.com.redumobile.repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;

import br.com.redumobile.entity.Subject;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.oauth.Aplicacao;
import br.com.redumobile.oauth.Autenticacao;
import br.com.redumobile.util.Util;


public class SubjectRepository implements ISubjectRepository{
	
	private static ISubjectRepository instance;
	
	public static ISubjectRepository getInstance() {
        if (instance == null) {
            instance = new SubjectRepository();
        }
        return instance;
    }

	@Override
	public Subject getModuloPorId(String id) throws RepositoryException {
		
		Subject modulo = null;
		try {
			String urlModulo = "http://redu.com.br/api/subjects/:suject_id".replace(":suject_id", id);
			
			OAuthRequest request = new OAuthRequest(Verb.GET, urlModulo);
			Token token = new Token(Aplicacao.autenticacao.getAccessToken(), Autenticacao.COSUMER_SECRET);
		    Aplicacao.autenticacao.getOAuthService().signRequest(token, request);
		    Response response = request.send();
		    String retornoModulo = response.getBody();
		    
			JSONObject jsonModulo = new JSONObject(retornoModulo);
			
			modulo = new Subject();
			modulo.setId(jsonModulo.getString("id"));
			
			modulo.setNome(jsonModulo.getString("name"));
			modulo.setDescricao(jsonModulo.getString("description"));
			String dataCriacao = jsonModulo.getString("created_at");
			modulo.setDataCriacao(Util.convertStringToDate(dataCriacao));
			
			JSONArray links = jsonModulo.getJSONArray("links");
			
			JSONObject hrefDisciplina = links.getJSONObject(2);
			if(hrefDisciplina.getString("rel").equals("space")){
				String urlDisciplina = hrefDisciplina.getString("href");
				modulo.setHrefDisciplina(urlDisciplina);
			}

			JSONObject hrefCurso = links.getJSONObject(3);
			if(hrefCurso.getString("rel").equals("course")){
				String urlCurso = hrefCurso.getString("href");
				modulo.setHrefCurso(urlCurso);
			}

			
		} catch (JSONException e) {
			e.printStackTrace();
			throw new RepositoryException(e.getMessage());
		}
		
		return modulo;
	}

}
