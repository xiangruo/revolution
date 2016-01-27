package com.dao;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.base.SpringTestCase;
import com.revolution.dao.AccountDAO;

public class AccountDAOTest extends SpringTestCase {
	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private AccountDAO accountDAO;

	@Test
	public void test1() {

		System.out.println(accountDAO);
	}

}
