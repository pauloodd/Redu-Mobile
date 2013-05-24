package br.com.redumobile.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.redumobile.util.SocialNetwork;

public final class User extends ReduEntityWithWall {
	private Date birthday;
	private String birthLocalization;
	private ArrayList<Chat> chats;
	private ArrayList<User> contacts;
	private String email;
	private ArrayList<Enrollment> enrollments;
	private String firstName;
	private int friendsCount;
	private String lastName;
	private String localization;
	private String login;
	private String mobile;
	private HashMap<SocialNetwork, String> socialNetworks;
	private ArrayList<Thumbnail> thumbnails;
	
	public User(){
		super(null, null, 0,null,null);
	}
	
	public User(Date createdAt, Date updatedAt, int id,
			ArrayList<Status> statuses, ArrayList<Status> timeline,
			Date birthday, ArrayList<Chat> chats, ArrayList<User> contacts,
			String email, String localization, String birthLocalization,
			String login, ArrayList<Enrollment> enrollments,
			ArrayList<Thumbnail> thumbnails, String firstName,
			int friendsCount, HashMap<SocialNetwork, String> socialNetworks,
			String mobile, String lastName) {
		super(createdAt, updatedAt, id, statuses, timeline);

		this.birthday = birthday == null ? null : (Date) birthday.clone();

		this.chats = chats;

		this.contacts = contacts;

		this.email = email;
		this.localization = localization;
		this.birthLocalization = birthLocalization;
		this.login = login;

		this.enrollments = enrollments;

		this.thumbnails = thumbnails;

		this.firstName = firstName;

		this.friendsCount = friendsCount;

		this.socialNetworks = socialNetworks;

		this.mobile = mobile;
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return birthday == null ? null : (Date) birthday.clone();
	}

	public String getBirthLocalization() {
		return birthLocalization;
	}

	public ArrayList<Chat> getChats() {
		return chats;
	}

	public ArrayList<User> getContacts() {
		return contacts;
	}

	public String getEmail() {
		return email;
	}

	public ArrayList<Enrollment> getEnrollments() {
		return enrollments;
	}

	public String getFirstName() {
		return firstName;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public String getLastName() {
		return lastName;
	}

	public String getLocalization() {
		return localization;
	}

	public String getLogin() {
		return login;
	}

	public String getMobile() {
		return mobile;
	}

	public HashMap<SocialNetwork, String> getSocialNetworks() {
		return socialNetworks;
	}

	public ArrayList<Thumbnail> getThumbnails() {
		return thumbnails;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday == null ? null : (Date) birthday.clone();
	}

	public void setBirthLocalization(String birthLocalization) {
		this.birthLocalization = birthLocalization;
	}

	public void setChats(ArrayList<Chat> chats) {
		this.chats = chats;
	}

	public void setContacts(ArrayList<User> contacts) {
		this.contacts = contacts;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEnrollments(ArrayList<Enrollment> enrollments) {
		this.enrollments = enrollments;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setSocialNetworks(HashMap<SocialNetwork, String> socialNetworks) {
		this.socialNetworks = socialNetworks;
	}

	public void setThumbnails(ArrayList<Thumbnail> thumbnails) {
		this.thumbnails = thumbnails;
	}
}
