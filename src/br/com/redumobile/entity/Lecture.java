package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public final class Lecture extends ReduEntityWithWall {
	private Course courseIn;
	private Environment environmentIn;
	private String link;
	private String name;
	private int position;
	private int rating;
	private Space spaceIn;
	private Subject subjectIn;
	private int viewCount;

	public Lecture(Date createdAt, Date updatedAt, int id,
			ArrayList<Status> statuses, ArrayList<Status> timeline,
			Course courseIn, Environment environmentIn, String link,
			String name, int position, int rating, Space spaceIn,
			Subject subjectIn, int viewCount) {
		super(createdAt, updatedAt, id, statuses, timeline);

		this.courseIn = courseIn;

		this.environmentIn = environmentIn;

		this.link = link;
		this.name = name;

		this.position = position;
		this.rating = rating;

		this.spaceIn = spaceIn;

		this.subjectIn = subjectIn;

		this.viewCount = viewCount;
	}

	public Course getCourseIn() {
		return courseIn;
	}

	public Environment getEnvironmentIn() {
		return environmentIn;
	}

	public String getLink() {
		return link;
	}

	public String getName() {
		return name;
	}

	public int getPosition() {
		return position;
	}

	public int getRating() {
		return rating;
	}

	public Space getSpaceIn() {
		return spaceIn;
	}

	public Subject getSubjectIn() {
		return subjectIn;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setCourseIn(Course courseIn) {
		this.courseIn = courseIn;
	}

	public void setEnvironmentIn(Environment environmentIn) {
		this.environmentIn = environmentIn;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public void setSpaceIn(Space spaceIn) {
		this.spaceIn = spaceIn;
	}

	public void setSubjectIn(Subject subjectIn) {
		this.subjectIn = subjectIn;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
}
