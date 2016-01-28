/**
 * Copyright © 2014 Xiong Zhijun, All Rights Reserved.
 * Email : hust.xzj@gmail.com
 */
package com.drools;

import org.junit.Before;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;

/**
 * base
 */
public abstract class DroolsBaseTest {

	protected KieServices kieServices;

	protected KieContainer kieContainer;

	@Before
	public void setUp() {
		kieServices = KieServices.Factory.get();
		kieContainer = kieServices.getKieClasspathContainer();
	}

}
