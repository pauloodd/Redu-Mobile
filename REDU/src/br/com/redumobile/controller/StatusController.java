package br.com.redumobile.controller;

import java.util.List;

import br.com.redumobile.entity.Status;
import br.com.redumobile.exception.ControllerException;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.repository.IStatusRepository;
import br.com.redumobile.repository.StatusRepository;


public class StatusController implements IStatusController{
	
	private static StatusController instance;
	private static IStatusRepository statusRepository;
	
	public StatusController(){
		statusRepository = StatusRepository.getInstance();
	}
	
	
	public static IStatusController getInstance() {
        if (instance == null) {
            instance = new StatusController();
        }
        return instance;
    }

	@Override
	public List<Status> getPostagens(int page)
			throws ControllerException {
		 try {
	            return statusRepository.getPostagens(page);
	        } catch (RepositoryException e) {
	            e.printStackTrace();
	            throw new ControllerException(e.getMessage());
	        }
	}


	@Override
	public List<Status> pesquisaRepostasStatus(String statusable)
			throws ControllerException {
		 try {
            return statusRepository.pesquisaRepostasStatus(statusable);
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}


	@Override
	public String enviarResposta(String statuses, String texto, String tipo)
			throws ControllerException {
		 try {
            return statusRepository.enviarResposta(statuses, texto, tipo);
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}
	
	
	


}
