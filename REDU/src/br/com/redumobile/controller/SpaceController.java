package br.com.redumobile.controller;

import br.com.redumobile.entity.Space;
import br.com.redumobile.exception.ControllerException;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.repository.ISpaceRepository;
import br.com.redumobile.repository.SpaceRepository;


public class SpaceController implements ISpaceController{
	
	private static SpaceController instance;
	private static ISpaceRepository spaceRepository;
	
	public SpaceController(){
		spaceRepository = SpaceRepository.getInstance();
	}
	
	
	public static ISpaceController getInstance() {
        if (instance == null) {
            instance = new SpaceController();
        }
        return instance;
    }


	@Override
	public Space getSpacePorIdLogin(String IdLogin)
			throws ControllerException {
		try {
            return spaceRepository.getSpacePorIdLogin(IdLogin);
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}

	

}
