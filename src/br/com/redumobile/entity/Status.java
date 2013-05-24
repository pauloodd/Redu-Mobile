package br.com.redumobile.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;


public abstract class Status extends ReduEntity {
	public static final class DefaultComparator implements Comparator<Status>,
			Serializable {
		private static final long serialVersionUID = 5295988685063933022L;
		private static volatile DefaultComparator singleton;

		public static DefaultComparator getInstance() {
			if (singleton == null) {
				singleton = new DefaultComparator();
			}

			return singleton;
		}

		private DefaultComparator() {
		}

		@Override
		public int compare(Status lhs, Status rhs) {
			int result;

			if (lhs.getUpdatedAt().before(rhs.getUpdatedAt())) {
				result = 1;
			} else if (lhs.getUpdatedAt().after(rhs.getUpdatedAt())) {
				result = -1;
			} else {
				result = 0;
			}

			return result;
		}
	}

	public static final class InvertedComparator implements Comparator<Status>,
			Serializable {
		private static final long serialVersionUID = 6326985777872232459L;
		private static volatile InvertedComparator singleton;

		public static InvertedComparator getInstance() {
			if (singleton == null) {
				singleton = new InvertedComparator();
			}

			return singleton;
		}

		private InvertedComparator() {
		}

		@Override
		public int compare(Status lhs, Status rhs) {
			int result;

			if (lhs.getUpdatedAt().before(rhs.getUpdatedAt())) {
				result = -1;
			} else if (lhs.getUpdatedAt().after(rhs.getUpdatedAt())) {
				result = 1;
			} else {
				result = 0;
			}

			return result;
		}
	}

	private String text;
	private User user;

	public Status(Date createdAt, Date updatedAt, int id, String text, User user) {
		super(createdAt, updatedAt, id);

		this.text = text;

		this.user = user;
	}

	public String getText() {
		return text;
	}

	public User getUser() {
		return user;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
