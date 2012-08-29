package br.com.redumobile.repository;

import java.util.List;

import br.com.redumobile.entity.Answer;
import br.com.redumobile.exception.RepositoryException;

public interface IAnswerRepository {


	public List<Answer> pesquisaRespostasDaPostagem(String answerHref) throws RepositoryException;
	
}
