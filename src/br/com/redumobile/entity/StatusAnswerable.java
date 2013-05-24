package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public abstract class StatusAnswerable extends Status {
	private ArrayList<Answer> answers;
	private ReduEntityWithWall statusable;
	private int answersCount;
	
	public StatusAnswerable(Date createdAt, Date updatedAt, int id,
			String text, User user, ArrayList<Answer> answers,
			ReduEntityWithWall statusable, int answersCount) {
		super(createdAt, updatedAt, id, text, user);

		this.answers = answers;
		this.answersCount = answersCount;
		this.statusable = statusable;
	}

	public ArrayList<Answer> getAnswers() {
		return answers;
	}

	public ReduEntityWithWall getStatusable() {
		return statusable;
	}

	public void setAnswers(ArrayList<Answer> answers) {
		this.answers = answers;
	}

	public void setStatusable(ReduEntityWithWall statusable) {
		this.statusable = statusable;
	}

	public int getAnswersCount() {
		return answersCount;
	}

	public void setAnswersCount(int answersCount) {
		this.answersCount = answersCount;
	}
}
