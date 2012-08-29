package br.com.redumobile.facade;

import java.util.List;

import br.com.redumobile.controller.CourseController;
import br.com.redumobile.controller.ICourseController;
import br.com.redumobile.controller.ISpaceController;
import br.com.redumobile.controller.IStatusController;
import br.com.redumobile.controller.ISubjectController;
import br.com.redumobile.controller.IUserController;
import br.com.redumobile.controller.SpaceController;
import br.com.redumobile.controller.StatusController;
import br.com.redumobile.controller.SubjectController;
import br.com.redumobile.controller.UserController;
import br.com.redumobile.entity.Course;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.Subject;
import br.com.redumobile.entity.User;
import br.com.redumobile.exception.ControllerException;
import br.com.redumobile.exception.FacadeException;


public class Facade {
	
	 private static Facade instance;
	 private IStatusController controllerStatus;
	 private IUserController controllerUser;
	 private ICourseController controllerCourse;
	 private ISubjectController controllerSubject;
	 private ISpaceController controllerSpace;
	 
	 public Facade(){
		 controllerStatus = StatusController.getInstance();
		 controllerUser = UserController.getInstance();
		 controllerCourse = CourseController.getInstance();
		 controllerSubject = SubjectController.getInstance();
		 controllerSpace = SpaceController.getInstance();
	 }
	 
	 public static Facade getInstance() {
	        if (instance == null) {
	            instance = new Facade();
	        }
	        return instance;
	    }

	 public User getUserME() throws FacadeException{
		 try {
	            return controllerUser.getUserME();
	        } catch (ControllerException e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
	 public User getUserPorLogin(String login) throws FacadeException{
		 try {
	            return controllerUser.getUserPorLogin(login);
	        } catch (ControllerException e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }

	 public List<Status> getPostagens(int page) throws FacadeException{
		 try {
	            return controllerStatus.getPostagens(page);
	        } catch (ControllerException e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
	 public List<Course> getCoursesPorUser() throws FacadeException{
		 try {
	            return controllerCourse.getCoursesPorUser();
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
	 public Subject getModuloPorId(String id) throws FacadeException{
		 try {
	            return controllerSubject.getModuloPorId(id);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
	 public Course getCursoPorIdLogin(String idLogin) throws FacadeException{
		 try {
	            return controllerCourse.getCoursePorIdLogin(idLogin);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
	 public Space getSpacePorIdLogin(String idLogin) throws FacadeException{
		 try {
	            return controllerSpace.getSpacePorIdLogin(idLogin);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
	 public List<Status> pesquisaRepostasStatus(String answerHref) throws FacadeException{
		 try {
	            return controllerStatus.pesquisaRepostasStatus(answerHref);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
	 public String enviarResposta(String statuses, String texto, String tipo) throws FacadeException{
		 try {
	            return controllerStatus.enviarResposta(statuses, texto, tipo);
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new FacadeException(e.getMessage());
	        }
	 }
	 
}


