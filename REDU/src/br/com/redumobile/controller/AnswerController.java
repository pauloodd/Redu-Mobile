package br.com.redumobile.controller;

import java.util.List;

import br.com.redumobile.entity.Answer;
import br.com.redumobile.exception.ControllerException;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.repository.AnswerRepository;
import br.com.redumobile.repository.IAnswerRepository;


public class AnswerController implements IAnswerController{
	
	private static AnswerController instance;
	private static IAnswerRepository answerRepository;
	
	public AnswerController(){
		answerRepository = AnswerRepository.getInstance();
	}
	
	
	public static IAnswerController getInstance() {
        if (instance == null) {
            instance = new AnswerController();
        }
        return instance;
    }


	@Override
	public List<Answer> pesquisaRespostasDaPostagem(String answerHref)
			throws ControllerException {
		try {
            return answerRepository.pesquisaRespostasDaPostagem(answerHref);
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}

}
