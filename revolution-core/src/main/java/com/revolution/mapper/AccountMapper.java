package com.revolution.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.revolution.pojo.Account;

@Repository
public interface AccountMapper {

	@Insert("insert into account(id,email) values(null,#{email})")
	public void insert(Account account);

	@Select("select id,email from account ")
	public List<Account> selectList();

	@Select("select id,email from account where id=#{id}")
	public Account selectOne(Long id);

	@Delete("delete from account where id=#{id}")
	public void delete(Long id);

	@Delete("update  account  set email=#{email} where id=#{id}")
	public void update(Account account);

}
