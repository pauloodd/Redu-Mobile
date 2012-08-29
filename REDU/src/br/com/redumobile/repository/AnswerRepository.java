package br.com.redumobile.repository;

import java.util.List;

import br.com.redumobile.entity.Answer;
import br.com.redumobile.exception.RepositoryException;



public class AnswerRepository implements IAnswerRepository{
	
	private static IAnswerRepository instance;
	
	public static IAnswerRepository getInstance() {
        if (instance == null) {
            instance = new AnswerRepository();
        }
        
        return instance;
	}
        
	@Override
	public List<Answer> pesquisaRespostasDaPostagem(String answerHref)
			throws RepositoryException {
		
		return null;
	}
			
			

}
