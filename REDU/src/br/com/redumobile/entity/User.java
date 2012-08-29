package br.com.redumobile.entity;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8825678914602117096L;

	public String id;
	
	public String firstName;
	
	public String lastName;
	
	public String email;
	
	public Date birthday;
	
	public String localization;
	
	public String[] links;
	
	public String login;
	
	public int friends_count;
	
	public String urlFotoPerfil;
	
	public User(){}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String[] getLinks() {
		return links;
	}

	public void setLinks(String[] links) {
		this.links = links;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}



	public String getLocalization() {
		return localization;
	}



	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrlFotoPerfil() {
		return urlFotoPerfil;
	}

	public void setUrlFotoPerfil(String urlFotoPerfil) {
		this.urlFotoPerfil = urlFotoPerfil;
	}

}


