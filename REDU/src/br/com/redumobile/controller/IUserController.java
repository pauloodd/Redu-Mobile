package br.com.redumobile.controller;

import br.com.redumobile.entity.User;
import br.com.redumobile.exception.ControllerException;

public interface IUserController {

	public User getUserME() throws ControllerException;
	
	public User getUserPorLogin(String login) throws ControllerException; 
}
