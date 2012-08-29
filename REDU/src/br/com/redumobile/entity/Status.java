package br.com.redumobile.entity;

import java.io.Serializable;
import java.util.Date;

public abstract class Status implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7947571540745472055L;
	String id;
	String text;
	Date dataCriacao;
	String statusable;
	String selfHref;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	
	public String getStatusable() {
		return statusable;
	}

	public void setStatusable(String statusable) {
		this.statusable = statusable;
	}

	public String getSelfHref() {
		return selfHref;
	}

	public void setSelfHref(String selfHref) {
		this.selfHref = selfHref;
	}

}
