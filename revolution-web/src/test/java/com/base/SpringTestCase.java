package com.base;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * @author liuwei
 */
@ContextConfiguration(locations = { "/spring/applicationContext.xml" })
public abstract class SpringTestCase extends AbstractJUnit4SpringContextTests {

}
