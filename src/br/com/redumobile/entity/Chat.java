package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;


public final class Chat extends ReduEntity {
	private ArrayList<ChatMessage> chatMessages;
	private User contact;
	private User user;

	public Chat(Date createdAt, Date updatedAt, int id, User user,
			User contact, ArrayList<ChatMessage> chatMessages) {
		super(createdAt, updatedAt, id);

		this.user = user;
		this.contact = contact;

		this.chatMessages = chatMessages;
	}

	public ArrayList<ChatMessage> getChatMessages() {
		return chatMessages;
	}

	public User getContact() {
		return contact;
	}

	public User getUser() {
		return user;
	}

	public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

	public void setContact(User contact) {
		this.contact = contact;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
