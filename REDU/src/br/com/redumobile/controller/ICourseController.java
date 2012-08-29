package br.com.redumobile.controller;

import java.util.List;

import br.com.redumobile.entity.Course;
import br.com.redumobile.exception.ControllerException;


public interface ICourseController {

	public List<Course> getCoursesPorUser() throws ControllerException;
	
	public Course getCoursePorIdLogin(String IdLogin) throws ControllerException;
}
