package br.com.redumobile.entity;

public final class Thumbnail {
	private String href;
	private String size;

	public Thumbnail(String href, String size) {
		this.href = href;
		this.size = size;
	}

	public String getHref() {
		return href;
	}

	public String getSize() {
		return size;
	}
}
