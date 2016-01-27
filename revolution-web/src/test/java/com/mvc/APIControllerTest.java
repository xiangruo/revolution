package com.mvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration(value = "revolution-web/src/main/webapp")
@ContextHierarchy({ @ContextConfiguration(name = "parent", locations = "classpath:applicationContext.xml"),
		@ContextConfiguration(name = "child", locations = "classpath:spring-mvc.xml") })
@RunWith(SpringJUnit4ClassRunner.class)
public class APIControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void helloTest() throws Exception {

		MvcResult result = mockMvc.perform(get("/hello", "hello"))// 执行请求
				.andReturn(); // 返回MvcResult
		Assert.assertNotNull(result.getResponse().getContentAsString());
	}

}
