package br.com.redumobile.repository;

import br.com.redumobile.entity.Subject;
import br.com.redumobile.exception.RepositoryException;

public interface ISubjectRepository {

	public Subject getModuloPorId(String id) throws RepositoryException;

}
