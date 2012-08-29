package br.com.redumobile.entity;

public class Help extends Status {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8569020461996839114L;

	User autor;
	
	String answer;

	public User getAutor() {
		return autor;
	}

	public void setAutor(User autor) {
		this.autor = autor;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}


	
}
