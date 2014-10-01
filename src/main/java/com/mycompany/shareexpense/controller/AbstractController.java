
package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorResource;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author Muthukumaran Swaminathan
 * @version $Revision$
 */
public class AbstractController {

	/**
	 * Handle service exception.
	 *
	 * @param customException the ex
	 * @param httpServletResponse the _request
	 * @return the error resource
	 */
	@ExceptionHandler(CustomException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ErrorResource handleServiceException(CustomException customException,
												HttpServletResponse httpServletResponse) {

		ErrorResource error = new ErrorResource();

		if (customException instanceof CustomException) {
			error.setCode(customException.getErrorCode());
			error.setMessage(customException.getErrorMsg());
		}


		return error;
	}
}
