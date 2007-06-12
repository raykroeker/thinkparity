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

    /** The error stack. */
    private StackTraceElement[] errorStackTrace;

    /** A response result. */
    private Result result;

    /**
     * Create ServiceResponse.
     *
     */
    public ServiceResponse() {
        super();
    }

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
        return null != errorMessage || null != errorStackTrace;
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
     * Set the stack trace.
     *
     * @param stackTrace
     *      A <code>StackTraceElement[]</code>.
     */
    public void setError(final String errorMessage,
            final StackTraceElement[] errorStackTrace) {
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
        this.result = result;
    }
}
