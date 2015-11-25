package com.revolution.web.api.action;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.revolution.web.api.bean.JsonMessageIn;

/**
 * @author xiangruo 想做一个解析json和xml的接口
 */
@Controller
public class APiController {

	private static final Logger logger = LoggerFactory.getLogger(APiController.class);

	/**
	 * json--->bean consumes : 指定处理请求的提交内容类型 produces :指定返回的内容类型
	 * @param body
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/json/api", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity handleJSONRequest(@RequestBody JsonMessageIn body, HttpServletRequest request) {
		logger.info("APiController handleJSONRequest:{}", body.toString());
		return new ResponseEntity<>(body, null, HttpStatus.OK);

	}

	@RequestMapping(value = "/xml/api", method = RequestMethod.POST, consumes = MediaType.APPLICATION_ATOM_XML_VALUE, produces = MediaType.APPLICATION_ATOM_XML_VALUE)
	public @ResponseBody ResponseEntity handleXMLRequest(@RequestBody JsonMessageIn body, HttpServletRequest request) {
		logger.info("APiController handleJSONRequest:{}", body.toString());
		return new ResponseEntity<>(body, null, HttpStatus.OK);

	}

}
