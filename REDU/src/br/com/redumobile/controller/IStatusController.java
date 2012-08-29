package br.com.redumobile.controller;

import java.util.List;

import br.com.redumobile.entity.Status;
import br.com.redumobile.exception.ControllerException;

public interface IStatusController {

	public List<Status> getPostagens(int page) throws ControllerException;
	
	public List<Status> pesquisaRepostasStatus(String statusable) throws ControllerException;
	
	public String enviarResposta(String statuses, String texto, String tipo) throws ControllerException;
	
}
