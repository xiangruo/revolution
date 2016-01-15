package com.revolution.web.api.velocity;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VelocityController {

	@RequestMapping(value = "/velocity/hello")
	public String hello(HttpServletRequest request, ModelMap modelMap) {
		modelMap.addAttribute("testparam", "velocity");
		return "index.vm";

	}

}
