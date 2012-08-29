package br.com.redumobile.controller;

import br.com.redumobile.entity.Subject;
import br.com.redumobile.exception.ControllerException;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.repository.ISubjectRepository;
import br.com.redumobile.repository.SubjectRepository;


public class SubjectController implements ISubjectController{
	
	private static SubjectController instance;
	private static ISubjectRepository subjectRepository;
	
	public SubjectController(){
		subjectRepository = SubjectRepository.getInstance();
	}
	
	
	public static ISubjectController getInstance() {
        if (instance == null) {
            instance = new SubjectController();
        }
        return instance;
    }


	@Override
	public Subject getModuloPorId(String id) throws ControllerException {
		try {
            return subjectRepository.getModuloPorId(id);
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}



}
