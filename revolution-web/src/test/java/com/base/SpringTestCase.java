package com.base;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-redis.xml" })
public abstract class SpringTestCase extends AbstractJUnit4SpringContextTests {

}
