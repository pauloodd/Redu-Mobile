package br.com.redumobile.entity;

import java.io.Serializable;
import java.util.Date;

public class Course implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3393883944431974540L;
	public String id;
	public String nome;
	public String path;
	public Date dataCriacao;
	
	public Course(){}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
