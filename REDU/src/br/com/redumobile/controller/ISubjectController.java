package br.com.redumobile.controller;

import br.com.redumobile.entity.Subject;
import br.com.redumobile.exception.ControllerException;


public interface ISubjectController {

	public Subject getModuloPorId(String id) throws ControllerException;
	
}
