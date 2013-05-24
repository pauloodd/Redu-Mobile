package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public final class Environment extends ReduEntity {
	private ArrayList<Course> courses;
	private User creator;
	private String description;
	private String initials;
	private String name;
	private String path;

	public Environment(Date createdAt, Date updatedAt, int id, String path,
			User creator, ArrayList<Course> courses, String description,
			String initials, String name) {
		super(createdAt, updatedAt, id);

		this.path = path;

		this.creator = creator;

		this.courses = courses;

		this.description = description;
		this.initials = initials;
		this.name = name;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public User getCreator() {
		return creator;
	}

	public String getDescription() {
		return description;
	}

	public String getInitials() {
		return initials;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
