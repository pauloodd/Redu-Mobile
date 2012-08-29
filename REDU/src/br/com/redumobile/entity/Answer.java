package br.com.redumobile.entity;

public class Answer extends Status {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1012538058440794679L;

	User autor;
	
	Status in_response_to;
	
	public User getAutor() {
		return autor;
	}

	public void setAutor(User autor) {
		this.autor = autor;
	}

	public Status getIn_response_to() {
		return in_response_to;
	}

	public void setIn_response_to(Status in_response_to) {
		this.in_response_to = in_response_to;
	}


	
}
