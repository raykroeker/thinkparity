/*
 * Created On:  6-Jun-07 10:14:04 AM
 */
package com.thinkparity.desdemona.web;

import com.thinkparity.desdemona.web.service.Result;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceResponse {

    /** A service error message. */
    private String errorMessage;

    /** A stack trace of a service error. */
    private StackTraceElement[] errorStackTrace;

    /** An error type. */
    private Class<?> errorType;

    /** A return response. */
    private Result result;

    /**
     * Create ServiceResponse.
     *
     */
    public ServiceResponse() {
        super();
    }

    /**
     * Obtain errorMessage.
     *
     * @return A String.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Obtain stackTrace.
     *
     * @return A StackTraceElement[].
     */
    public StackTraceElement[] getErrorStackTrace() {
        return errorStackTrace;
    }

    /**
     * Obtain the error type.
     * 
     * @return An error type <code>Class<?></code>.
     */
    public Class<?> getErrorType() {
        return errorType;
    }

    /**
     * Obtain result.
     *
     * @return A Object.
     */
    public Result getResult() {
        return result;
    }

    /**
     * Determine whether or not the service response is an error response.
     * 
     * @return True if the service response is an error.
     */
    public Boolean isErrorResponse() {
        return null != errorMessage || null != errorStackTrace;
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
     * Determine if the error type is set.
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
     * Set a declared error.
     * 
     * @param errorType
     *            An error type <code>Class<?></code>.
     * @param errorMessage
     *            An error message <code>String</code>.
     * @param errorStackTrace
     *            A <code>StackTraceElement[]</code>.
     */
    public void setDeclaredError(final Class<?> errorType,
            final String errorMessage, final StackTraceElement[] errorStackTrace) {
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorStackTrace = errorStackTrace;
    }

    /**
     * Set result.
     *
     * @param result
     *		A Object.
     */
    public void setResult(final Result result) {
        this.result = result;
    }

    /**
     * Set an undeclared error.
     * 
     * @param errorMessage
     *            An error message <code>String</code>.
     * @param errorStackTrace
     *            A <code>StackTraceElement[]</code>.
     */
    public void setUndeclaredError(final String errorMessage,
            final StackTraceElement[] errorStackTrace) {
        this.errorType = null;
        this.errorMessage = errorMessage;
        this.errorStackTrace = errorStackTrace;
    }
}
