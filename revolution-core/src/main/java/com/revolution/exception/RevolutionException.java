package com.revolution.exception;

import org.apache.log4j.spi.ErrorCode;

public class RevolutionException extends Exception {

	private static final long serialVersionUID = -8634700792767837033L;

	public ErrorCode errorCode;

	public RevolutionException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

}
