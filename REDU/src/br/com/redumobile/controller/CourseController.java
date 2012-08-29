package br.com.redumobile.controller;

import java.util.List;

import br.com.redumobile.entity.Course;
import br.com.redumobile.exception.ControllerException;
import br.com.redumobile.exception.RepositoryException;
import br.com.redumobile.repository.CourseRepository;
import br.com.redumobile.repository.ICourseRepository;


public class CourseController implements ICourseController{
	
	private static CourseController instance;
	private static ICourseRepository courseRepository;
	
	public CourseController(){
		courseRepository = CourseRepository.getInstance();
	}
	
	
	public static ICourseController getInstance() {
        if (instance == null) {
            instance = new CourseController();
        }
        return instance;
    }


	

	@Override
	public List<Course> getCoursesPorUser() throws ControllerException {
		try {
            return courseRepository.getCoursesPorUser();
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}


	@Override
	public Course getCoursePorIdLogin(String IdLogin)
			throws ControllerException {
		try {
            return courseRepository.getCoursePorIdLogin(IdLogin);
        } catch (RepositoryException e) {
            e.printStackTrace();
            throw new ControllerException(e.getMessage());
        }
	}

	

}
