package br.com.redumobile.repository;

import java.util.Date;

import org.json.JSONObject;

import br.com.redumobile.entity.Space;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.oauth.AuxiliarOAuth;
import br.com.redumobile.util.Util;


public class SpaceRepository implements ISpaceRepository{
	
	private static ISpaceRepository instance;
	
	public static ISpaceRepository getInstance() {
        if (instance == null) {
            instance = new SpaceRepository();
        }
        return instance;
    }

	@Override
	public Space getSpacePorIdLogin(String IdLogin) throws RepositoryException {
		Space disciplina = null;
		try{
			String url = "http://redu.com.br/api/spaces/" + IdLogin;
			
			String disciplinaHref = AuxiliarOAuth.obterRespostaUrlViaGet(url, null);
			
			JSONObject jsonSpace = new JSONObject(disciplinaHref);
			
			String id = jsonSpace.getString("id");
			String created_at = jsonSpace.getString("created_at");
			String nome = jsonSpace.getString("name");
	
			disciplina = new Space();
			disciplina.setId(id);
			disciplina.setNome(nome);
			Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
			disciplina.setDataCriacao(dataCriacao);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RepositoryException(e.getMessage());
		} 
	
		return disciplina;
		
	}

	

	

}
