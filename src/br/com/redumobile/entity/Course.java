package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public final class Course extends ReduEntity {
	private String description;
	private ArrayList<Enrollment> enrollments;
	private Environment environmentIn;
	private String name;
	private String path;
	private ArrayList<Space> spaces;
	private int workload;

	public Course(Date createdAt, Date updatedAt, int id,
			Environment environmentIn, String path, int workload,
			String description, ArrayList<Space> spaces,
			ArrayList<Enrollment> enrollments, String name) {
		super(createdAt, updatedAt, id);

		this.environmentIn = environmentIn;

		this.path = path;

		this.workload = workload;

		this.description = description;

		this.spaces = spaces;

		this.enrollments = enrollments;

		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<Enrollment> getEnrollments() {
		return enrollments;
	}

	public Environment getEnvironmentIn() {
		return environmentIn;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public ArrayList<Space> getSpaces() {
		return spaces;
	}

	public int getWorkload() {
		return workload;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnrollments(ArrayList<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}

	public void setEnvironmentIn(Environment environmentIn) {
		this.environmentIn = environmentIn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setSpaces(ArrayList<Space> spaces) {
		this.spaces = spaces;
	}

	public void setWorkload(int workload) {
		this.workload = workload;
	}
}
