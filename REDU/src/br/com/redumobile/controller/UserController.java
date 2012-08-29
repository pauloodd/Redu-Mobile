package br.com.redumobile.controller;

import br.com.redumobile.entity.User;
import br.com.redumobile.exception.ControllerException;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.repository.IUserRepository;
import br.com.redumobile.repository.UserRepository;


public class UserController implements IUserController{
	
	private static UserController instance;
	private static IUserRepository userRepository;
	
	public UserController(){
		userRepository = UserRepository.getInstance();
	}
	
	
	public static IUserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }


	@Override
	public User getUserME() throws ControllerException {
		try {
            return userRepository.getUserME();
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}


	@Override
	public User getUserPorLogin(String login) throws ControllerException {
		try {
            return userRepository.getUserPorLogin(login);
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}

	

}
