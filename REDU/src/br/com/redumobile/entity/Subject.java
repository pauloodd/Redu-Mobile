package br.com.redumobile.entity;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;

public class Subject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2560733636484348353L;
	public String id;
	public String nome;
	public String descricao;
	public Date dataCriacao;
	public Space disciplina;
	
	public String hrefCurso;
	public String hrefDisciplina;
	
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
	public Space getDisciplina() {
		return disciplina;
	}
	public void setDisciplina(Space disciplina) {
		this.disciplina = disciplina;
	}
	
	public String getDisciplinaLinkID() throws JSONException{
		String urlSplit[] = hrefDisciplina.split("/");
		return urlSplit[urlSplit.length-1];
	}
	
	public String getCursoLinkID() throws JSONException{
		String urlSplit[] = hrefCurso.split("/");
		return urlSplit[urlSplit.length-1];
	}
	public String getHrefCurso() {
		return hrefCurso;
	}
	public void setHrefCurso(String hrefCurso) {
		this.hrefCurso = hrefCurso;
	}
	public String getHrefDisciplina() {
		return hrefDisciplina;
	}
	public void setHrefDisciplina(String hrefDisciplina) {
		this.hrefDisciplina = hrefDisciplina;
	}
}
