package br.com.redumobile.entity;

import java.io.Serializable;
import java.util.Date;

public class Lecture implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3019061963517796838L;
	public String id;
	public Date dataAtualizacao;
	public String nome;
	public String tipo;
	public Date dataCriacao;
	public String usuerioCriadorId;
	public Subject modulo;
	
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public String getUsuerioCriadorId() {
		return usuerioCriadorId;
	}
	public void setUsuerioCriadorId(String usuerioCriadorId) {
		this.usuerioCriadorId = usuerioCriadorId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Subject getModulo() {
		return modulo;
	}
	public void setModulo(Subject modulo) {
		this.modulo = modulo;
	}
	
	
}
