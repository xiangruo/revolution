package com.revolution.web.api.bean;

import java.io.Serializable;

public class JsonMessageIn implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String post_id;

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	@Override
	public String toString() {
		return "JsonMessageIn [post_id=" + post_id + "]";
	}

}
