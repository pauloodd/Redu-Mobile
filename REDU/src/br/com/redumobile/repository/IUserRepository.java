package br.com.redumobile.repository;

import br.com.redumobile.entity.User;
import br.com.redumobile.exception.RepositoryException;

public interface IUserRepository {

	public User getUserME() throws RepositoryException;

	User getUserPorLogin(String login) throws RepositoryException;
}
