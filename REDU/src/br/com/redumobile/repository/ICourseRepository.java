package br.com.redumobile.repository;

import java.util.List;

import br.com.redumobile.entity.Course;
import br.com.redumobile.exception.RepositoryException;

public interface ICourseRepository {


	public List<Course> getCoursesPorUser() throws RepositoryException;
	public Course getCoursePorIdLogin(String IdLogin) throws RepositoryException;
	
}
