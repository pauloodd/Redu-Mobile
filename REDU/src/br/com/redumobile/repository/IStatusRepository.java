package br.com.redumobile.repository;

import java.util.List;

import br.com.redumobile.entity.Status;
import br.com.redumobile.exception.RepositoryException;

public interface IStatusRepository {

	public List<Status> getPostagens(int page) throws RepositoryException;
	
	public List<Status> pesquisaRepostasStatus(String statusable) throws RepositoryException;
	
	public String enviarResposta(String statuses, String texto, String tipo) throws RepositoryException;
}
