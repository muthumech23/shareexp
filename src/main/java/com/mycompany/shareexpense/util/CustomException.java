package com.mycompany.shareexpense.util;


/**
 * The Class CustomException.
 */
@SuppressWarnings("serial")
public class CustomException extends RuntimeException {
    /** The error code. */
    private String errorCode = null;


    /** The error msg. */
    private String errorMsg = null;


/**
     * Instantiates a new custom exception.
     *
     * @param errorCode the error code
     * @param errorMsg the error msg
     */
    public CustomException(String errorCode,
                           String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    /**
     * Gets the error code.
     *
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the error code.
     *
     * @param errorCode the new error code
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the error msg.
     *
     * @return the error msg
     */
    public String getErrorMsg() {
        return errorMsg;
    }


    /**
     * Sets the error msg.
     *
     * @param errorMsg the new error msg
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
