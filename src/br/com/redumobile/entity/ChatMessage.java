package br.com.redumobile.entity;

import java.util.Date;


public final class ChatMessage extends ReduEntity {
	private User contact;
	private String message;
	private User user;

	public ChatMessage(Date createdAt, Date updatedAt, int id, User user,
			User contact, String message) {
		super(createdAt, updatedAt, id);

		this.user = user;
		this.contact = contact;

		this.message = message;
	}

	public User getContact() {
		return contact;
	}

	public String getMessage() {
		return message;
	}

	public User getUser() {
		return user;
	}

	public void setContact(User contact) {
		this.contact = contact;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
