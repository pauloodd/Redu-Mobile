package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public final class Subject extends ReduEntity {
	private Course courseIn;
	private String description;
	private Environment environmentIn;
	private ArrayList<Lecture> lectures;
	private String name;
	private Space spaceIn;

	public Subject(Date createdAt, Date updatedAt, int id,
			ArrayList<Lecture> lectures, Environment environmentIn,
			Course courseIn, String description, Space spaceIn, String name) {
		super(createdAt, updatedAt, id);

		this.lectures = lectures;

		this.environmentIn = environmentIn;

		this.courseIn = courseIn;

		this.description = description;

		this.spaceIn = spaceIn;

		this.name = name;
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

	public ArrayList<Lecture> getLectures() {
		return lectures;
	}

	public String getName() {
		return name;
	}

	public Space getSpaceIn() {
		return spaceIn;
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

	public void setLectures(ArrayList<Lecture> lectures) {
		this.lectures = lectures;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSpaceIn(Space spaceIn) {
		this.spaceIn = spaceIn;
	}
}
