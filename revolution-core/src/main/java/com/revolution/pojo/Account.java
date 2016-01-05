package com.revolution.pojo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Account {

	// JPA 主键标识, 策略为由数据库生成主键
	public Long id;

	public String email;

	public Account() {

	}

	public Account(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
