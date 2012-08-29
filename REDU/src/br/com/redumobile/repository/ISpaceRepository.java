package br.com.redumobile.repository;

import br.com.redumobile.entity.Space;
import br.com.redumobile.exception.RepositoryException;

public interface ISpaceRepository {


	public Space getSpacePorIdLogin(String IdLogin) throws RepositoryException;
	
}
