package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public final class Space extends ReduEntityWithWall {
	private Course courseIn;
	private String description;
	private Environment environmentIn;
	private String name;
	private ArrayList<Subject> subjects;
	private ArrayList<User> users;

	public Space(Date createdAt, Date updatedAt, int id,
			ArrayList<Status> statuses, ArrayList<Status> timeline,
			Environment environmentIn, Course courseIn, String description,
			ArrayList<Subject> subjects, String name, ArrayList<User> users) {
		super(createdAt, updatedAt, id, statuses, timeline);

		this.environmentIn = environmentIn;

		this.courseIn = courseIn;

		this.description = description;

		this.subjects = subjects;

		this.name = name;

		this.users = users;
	}

	public Course getCourseIn() {
		return courseIn;
	}

	public String getDescription() {
		return description;
	}

	public Environment getEnvironmentIn() {
		return environmentIn;
	}

	public String getName() {
		return name;
	}

	public ArrayList<Subject> getSubjects() {
		return subjects;
	}

	public ArrayList<User> getUsers() {
		return users;
	}

	public void setCourseIn(Course courseIn) {
		this.courseIn = courseIn;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnvironmentIn(Environment environmentIn) {
		this.environmentIn = environmentIn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSubjects(ArrayList<Subject> subjects) {
		this.subjects = subjects;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}
}
