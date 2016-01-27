package com.utils;

import org.springframework.beans.factory.annotation.Autowired;

import redis.clients.jedis.JedisSentinelPool;

import com.base.SpringTestCase;

public class RedisTest extends SpringTestCase {

	@Autowired
	private JedisSentinelPool msgPool;

}
