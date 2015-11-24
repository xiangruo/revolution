package com.revolution.web.api;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class APiController {

	private static final Logger logger = LoggerFactory.getLogger(APiController.class);

	@RequestMapping(value = "/json/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity handleJSONRequest(@RequestBody String body, HttpServletRequest request) {
		logger.info("APiController handleJSONRequest:{}", body);
		return new ResponseEntity<>(body, null, HttpStatus.OK);

	}

}
