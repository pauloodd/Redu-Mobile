package br.com.redumobile.entity;

import java.util.Date;

public abstract class ReduEntity implements Cloneable {
	private Date createdAt;
	private int id;
	private Date updatedAt;

	public ReduEntity(Date createdAt, Date updatedAt, int id) {
		this.createdAt = createdAt == null ? null : (Date) createdAt.clone();
		this.updatedAt = updatedAt == null ? null : (Date) updatedAt.clone();

		this.id = id;
	}

	@Override
	public Object clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}

		return clone;
	}

	public Date getCreatedAt() {
		return createdAt == null ? null : (Date) createdAt.clone();
	}

	public int getId() {
		return id;
	}

	public Date getUpdatedAt() {
		return updatedAt == null ? null : (Date) updatedAt.clone();
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt == null ? null : (Date) createdAt.clone();
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt == null ? null : (Date) updatedAt.clone();
	}

}
