package com.revolution.web.api.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.revolution.web.api.action.APiController;

//@ControllerAdvice(basePackages = { "com.revolution.web.api.action" })
/**
 * @author xiangruo
 * @ControllerAdvice的应用 异常处理 初始化一些数据 以及加入一些新的原始
 */
@ControllerAdvice(assignableTypes = APiController.class)
public class ApiControllerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleDefaultException(Exception e) {
		return new ResponseEntity<>(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ModelAttribute
	public String addAttr() {
		System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前把返回值放入Model");
		return "Element";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		System.out.println("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器");

		// DateFormat dateFormat1 = new SimpleDateFormat("d-MM-yyyy");
		// CustomDateEditor orderDateEditor = new CustomDateEditor(dateFormat1,
		// true);
		// DateFormat dateFormat2 = new SimpleDateFormat("MMM d, YYYY");
		// CustomDateEditor shipDateEditor = new CustomDateEditor(dateFormat2,
		// true);
		// binder.registerCustomEditor(Date.class, "orderDate",
		// orderDateEditor);
		// binder.registerCustomEditor(Date.class, "shipDate", shipDateEditor);

	}

}
