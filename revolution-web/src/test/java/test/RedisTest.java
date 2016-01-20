package test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import redis.clients.jedis.JedisSentinelPool;

import com.base.SpringTestCase;

public class RedisTest extends SpringTestCase {

	@Autowired
	ApplicationContext context;

	@Test
	public void test1() {
		System.out.println(context.getBean(JedisSentinelPool.class));
	}

}
