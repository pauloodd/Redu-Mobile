package br.com.redumobile.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import br.com.redumobile.entity.Course;
import br.com.redumobile.entity.User;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.oauth.Aplicacao;
import br.com.redumobile.oauth.AuxiliarOAuth;
import br.com.redumobile.util.Util;


public class CourseRepository implements ICourseRepository{
	
	private static ICourseRepository instance;
	
	public static ICourseRepository getInstance() {
        if (instance == null) {
            instance = new CourseRepository();
        }
        return instance;
    }

	@Override
	public List<Course> getCoursesPorUser() throws RepositoryException {
		List<Course> ambientesRetorno = new ArrayList<Course>();
		try {
			User me = Aplicacao.getUsuario();
			String url = "http://redu.com.br/api/users/"+me.getLogin()+"/enrollments";
			String resposta = AuxiliarOAuth.obterRespostaUrlViaGet(url, null);
			
			JSONArray array = new JSONArray(resposta);
			
			for(int i = 0; i < array.length(); i++){
				JSONObject objJson = array.getJSONObject(i);
				
				JSONArray links = objJson.getJSONArray("links");
				JSONObject jsonCursoHref = links.getJSONObject(1);
				
				String href = jsonCursoHref.getString("href");
				
				String cursoHref = AuxiliarOAuth.obterRespostaUrlViaGet(href, null);
				
				JSONObject jsonCurso = new JSONObject(cursoHref);
				
				String id = jsonCurso.getString("id");
				String created_at = jsonCurso.getString("created_at");
				String path = jsonCurso.getString("path");
				String nome = jsonCurso.getString("name");

				Course curso = new Course();
				curso.setId(id);
				curso.setPath(path);
				curso.setNome(nome);
				Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
				curso.setDataCriacao(dataCriacao);
				
				ambientesRetorno.add(curso);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RepositoryException(e.getMessage());
		} 
		
		return ambientesRetorno;
	}

	@Override
	public Course getCoursePorIdLogin(String IdLogin) throws RepositoryException {
		Course curso = null;
		try{
			String url = "http://redu.com.br/api/courses/" + IdLogin;
			
			String cursoHref = AuxiliarOAuth.obterRespostaUrlViaGet(url, null);
			
			JSONObject jsonCurso = new JSONObject(cursoHref);
			
			String id = jsonCurso.getString("id");
			String created_at = jsonCurso.getString("created_at");
			String path = jsonCurso.getString("path");
			String nome = jsonCurso.getString("name");
	
			curso = new Course();
			curso.setId(id);
			curso.setPath(path);
			curso.setNome(nome);
			Date dataCriacao = Util.convertStringToDateFromStatus(created_at);
			curso.setDataCriacao(dataCriacao);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RepositoryException(e.getMessage());
		} 
	
		return curso;
		
	}

	

	

}
