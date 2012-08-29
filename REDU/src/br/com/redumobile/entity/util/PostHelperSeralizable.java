package br.com.redumobile.entity.util;

import java.io.Serializable;

import br.com.redumobile.entity.Lecture;
import br.com.redumobile.entity.Space;
import br.com.redumobile.entity.Status;
import br.com.redumobile.entity.User;

public class PostHelperSeralizable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3805397749721990242L;
	
	private Status postagemOriginal;
	private User muralPublicadoUser;
	private Lecture muralPublicadoAula;
	private Space muralPublicadoDisciplina;
	
	public PostHelperSeralizable(PostHelper postHelper){
		this.postagemOriginal = postHelper.getPostagemOriginal();
		this.muralPublicadoAula = postHelper.getMuralPublicadoAula();
		this.muralPublicadoDisciplina = postHelper.getMuralPublicadoDisciplina();
		this.muralPublicadoUser = postHelper.getMuralPublicadoUser();
	}
	
	public PostHelperSeralizable(Status postagemOriginal){
		this.postagemOriginal = postagemOriginal;
	}

	public Status getPostagemOriginal() {
		return postagemOriginal;
	}

	public void setPostagemOriginal(Status postagemOriginal) {
		this.postagemOriginal = postagemOriginal;
	}

	public User getMuralPublicadoUser() {
		return muralPublicadoUser;
	}

	public void setMuralPublicadoUser(User muralPublicadoUser) {
		this.muralPublicadoUser = muralPublicadoUser;
	}

	public Lecture getMuralPublicadoAula() {
		return muralPublicadoAula;
	}

	public void setMuralPublicadoAula(Lecture muralPublicadoAula) {
		this.muralPublicadoAula = muralPublicadoAula;
	}

	public Space getMuralPublicadoDisciplina() {
		return muralPublicadoDisciplina;
	}

	public void setMuralPublicadoDisciplina(Space muralPublicadoDisciplina) {
		this.muralPublicadoDisciplina = muralPublicadoDisciplina;
	}

}
