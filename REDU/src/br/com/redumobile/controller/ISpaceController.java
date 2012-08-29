package br.com.redumobile.controller;

import br.com.redumobile.entity.Space;
import br.com.redumobile.exception.ControllerException;


public interface ISpaceController {

	public Space getSpacePorIdLogin(String IdLogin) throws ControllerException;
}
