package br.com.redumobile.entity;

import java.io.Serializable;
import java.util.Date;

public class Space implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2872155290034429063L;
	public String id;
	public String descricao;
	public Date dataCriacao;
	public String nome;
	
	public Course curso;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Course getCurso() {
		return curso;
	}
	public void setCurso(Course curso) {
		this.curso = curso;
	}

}
