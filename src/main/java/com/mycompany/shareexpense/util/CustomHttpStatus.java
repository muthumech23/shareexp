package com.mycompany.shareexpense.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;


public class CustomHttpStatus extends HttpStatusCodeException {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	public CustomHttpStatus(	HttpStatus statusCode,
								String statusText) {

		super(statusCode, statusText);
	}

}
