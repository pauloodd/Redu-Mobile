package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public final class Help extends StatusAnswerable {
	public enum State {
		Answered, Forgotten, Stopped
	}

	private State state;

	public Help(Date createdAt, Date updatedAt, int id, String text, User user,
			ArrayList<Answer> answers, ReduEntityWithWall statusable,
			State state, int answersCount) {
		super(createdAt, updatedAt, id, text, user, answers, statusable, answersCount);

		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
