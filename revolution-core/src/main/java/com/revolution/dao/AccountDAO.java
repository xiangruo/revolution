package com.revolution.dao;

import org.springframework.stereotype.Repository;

import com.revolution.pojo.Account;

@Repository
public interface AccountDAO {

	public int insert(Account account);

}
