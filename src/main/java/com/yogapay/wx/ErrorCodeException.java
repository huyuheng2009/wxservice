package com.yogapay.wx;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ErrorCodeException extends Exception {

	private final int errorCode;

	public ErrorCodeException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public ResponseEntity toHTML() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.set("Content-Type", "text/html;charset=UTF-8");
		return new ResponseEntity<Object>(getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public ResponseEntity toXML() {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.set("Content-Type", "text/xml;charset=UTF-8");
		return new ResponseEntity<Object>(getMessage(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
