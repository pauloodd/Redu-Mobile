package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public abstract class ReduEntityWithWall extends ReduEntity {
	private ArrayList<Status> statuses;
	private ArrayList<Status> timeline;

	public ReduEntityWithWall(Date createdAt, Date updatedAt, int id,
			ArrayList<Status> statuses, ArrayList<Status> timeline) {
		super(createdAt, updatedAt, id);

		this.statuses = statuses;
		this.timeline = timeline;
	}

	public ArrayList<Status> getStatuses() {
		return statuses;
	}

	public ArrayList<Status> getTimeline() {
		return timeline;
	}

	public void setStatuses(ArrayList<Status> statuses) {
		this.statuses = statuses;
	}

	public void setTimeline(ArrayList<Status> timeline) {
		this.timeline = timeline;
	}
}
