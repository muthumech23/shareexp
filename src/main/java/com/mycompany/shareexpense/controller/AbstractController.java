package com.mycompany.shareexpense.controller;

import com.mycompany.shareexpense.util.CustomException;
import com.mycompany.shareexpense.util.ErrorConstants;
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
     * @param customException     the ex
     * @param httpServletResponse the _request
     * @return the error resource
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResource handleServiceException(CustomException customException,
                                                HttpServletResponse httpServletResponse) {

        ErrorResource error = new ErrorResource();

        if (customException instanceof CustomException) {
            error.setCode(customException.getErrorCode());
            error.setMessage(customException.getErrorMsg());
        }


        return error;
    }

    /**
     * Handle All service exception.
     *
     * @param Exception           e
     * @param httpServletResponse the _request
     * @return the error resource
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResource handleAllException(Exception e,
                                            HttpServletResponse httpServletResponse) {

        ErrorResource error = new ErrorResource();
        error.setCode(ErrorConstants.ERR_GENERAL_FAILURE);
        error.setMessage(e.getMessage());

        return error;
    }
}
