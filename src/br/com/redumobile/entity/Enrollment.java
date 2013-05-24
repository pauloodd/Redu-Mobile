package br.com.redumobile.entity;

import java.util.Date;


public final class Enrollment extends ReduEntity {
	public enum State {
		Approved, Invited, ReduInvited, Rejected, Waiting
	}

	private Course courseIn;
	private Environment environmentIn;
	private State state;
	private User user;

	public Enrollment(Date createdAt, Date updatedAt, int id,
			Environment environmentIn, Course courseIn, State state, User user) {
		super(createdAt, updatedAt, id);

		this.environmentIn = environmentIn;

		this.courseIn = courseIn;

		this.state = state;

		this.user = user;
	}

	public Course getCourseIn() {
		return courseIn;
	}

	public Environment getEnvironmentIn() {
		return environmentIn;
	}

	public State getState() {
		return state;
	}

	public User getUser() {
		return user;
	}

	public void setCourseIn(Course courseIn) {
		this.courseIn = courseIn;
	}

	public void setEnvironmentIn(Environment environmentIn) {
		this.environmentIn = environmentIn;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
