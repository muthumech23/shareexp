package com.mycompany.shareexpense.util;

import org.springframework.http.HttpStatus;


public class CustomException extends Exception {
	
	private CustomHttpStatus httpStatus = null;
	
	public CustomException(HttpStatus httpStatus, String statusText) {
		
		new CustomHttpStatus(httpStatus, statusText);
		
	}

	
	public CustomHttpStatus getHttpStatus() {
	
		return httpStatus;
	}

	
	public void setHttpStatus(CustomHttpStatus httpStatus) {
	
		this.httpStatus = httpStatus;
	}

}
