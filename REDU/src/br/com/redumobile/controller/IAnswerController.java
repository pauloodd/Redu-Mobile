package br.com.redumobile.controller;

import java.util.List;

import br.com.redumobile.entity.Answer;
import br.com.redumobile.exception.ControllerException;


public interface IAnswerController {

	public List<Answer> pesquisaRespostasDaPostagem(String answerHref)
			throws ControllerException;
}
