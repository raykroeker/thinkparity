/*
 * Created On:  7-Jun-07 9:37:50 AM
 */
package com.thinkparity.service.client;

/**
 * <b>Title:</b>thinkParity Service Client Response<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceResponse {

    /** The error message. */
    private String errorMessage;

    /** A boolean indicating an error response. */
    private Boolean errorResponse;

    /** The error stack. */
    private StackTraceElement[] errorStackTrace;

    /** The error type. */
    private Class<?> errorType;

    /** A response result. */
    private Result result;

    /**
     * Create ServiceResponse.
     *
     */
    public ServiceResponse() {
        super();
    }

    /**
     * Obtain the error message.
     * 
     * @return An error message <code>String</code>.
     */
    public String getErrorMessage(){ 
        return errorMessage;
    }

    /**
     * Obtain the stack trace.
     * 
     * @return A <code>StackTraceElement[]</code>.
     */
    public StackTraceElement[] getErrorStackTrace() {
        return errorStackTrace;
    }

    /**
     * Obtain the response error type.
     * 
     * @return A <code>Class<? extends Throwable></code>.
     */
    public Class<?> getErrorType() {
        return errorType;
    }

    /**
     * Obtain result.
     *
     * @return A Result.
     */
    public Result getResult() {
        return result;
    }

    /**
     * Determine if the service response is an error response.
     * 
     * @return True if the response is an error response.
     */
    public Boolean isErrorResponse() {
        return errorResponse;
    }

    /**
     * Determine if the error message is set.
     * 
     * @return True if the error message is set.
     */
    public Boolean isSetErrorMessage() {
        return Boolean.valueOf(null != errorMessage);
    }

    /**
     * Determine whether or not the error type is set.
     * 
     * @return True if the error type is set.
     */
    public Boolean isSetErrorType() {
        return Boolean.valueOf(null != errorType);
    }

    /**
     * Determine if the result is set.
     * 
     * @return True if the result is set.
     */
    public Boolean isSetResult() {
        return null != result;
    }

    /**
     * Set an declared error.
     * 
     * @param errorType
     *            An error type <code>Class<?></code>.
     * @param errorMessage
     *            An error message <code>String</code>.
     * @param errorStackTrace
     *            An error <code>StackTraceElement[]</code>.
     */
    public void setDeclaredError(final Class<?> errorType,
            final String errorMessage, final StackTraceElement[] errorStackTrace) {
        this.errorResponse = Boolean.TRUE;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorStackTrace = errorStackTrace;
    }

    /**
     * Set result.
     * 
     * @param result
     *            A <code>Result</code>.
     */
    public void setResult(final Result result) {
        this.errorResponse = Boolean.FALSE;
        this.result = result;
    }

    /**
     * Set an undeclared error.
     * 
     * @param errorMessage
     *            An error message <code>String</code>.
     * @param errorStackTrace
     *            An error <code>StackTraceElement[]</code>.
     */
    public void setUndeclaredError(final String errorMessage,
            final StackTraceElement[] errorStackTrace) {
        this.errorResponse = Boolean.TRUE;
        this.errorType = null;
        this.errorMessage = errorMessage;
        this.errorStackTrace = errorStackTrace;
    }
}
