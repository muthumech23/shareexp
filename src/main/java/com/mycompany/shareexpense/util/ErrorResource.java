
package com.mycompany.shareexpense.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResource {

	private String	resource;
	private String	code;
	private String	message;

	public String getCode() {

		return code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getMessage() {

		return message;
	}

	public void setMessage(String message) {

		this.message = message;
	}

	public String getResource() {

		return resource;
	}

	public void setResource(String resource) {

		this.resource = resource;
	}

}
