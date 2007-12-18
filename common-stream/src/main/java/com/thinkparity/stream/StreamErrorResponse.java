/*
 * Created On:  8-Oct-07 5:04:00 PM
 */
package com.thinkparity.stream;

/**
 * <b>Title:</b>thinkParity Common Model Stream Error Response<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class StreamErrorResponse {

    /** An empty stream error response. */
    static final StreamErrorResponse EMPTY_RESPONSE;

    /** A list of recoverable error codes. */
    private static final String[] RECOVERABLE_ERROR_CODES;

    static {
        EMPTY_RESPONSE = new StreamErrorResponse();
        RECOVERABLE_ERROR_CODES = new String[] { "RequestTimeout" };
    }

    /** The error code. */
    private String code;

    /** The host id. */
    private String hostId;

    /** The error message. */
    private String message;

    /** The request id. */
    private String requestId;

    /**
     * Create StreamErrorResponse.
     *
     */
    StreamErrorResponse() {
        super();
    }

    /**
     * Obtain the code.
     *
     * @return A <code>Code</code>.
     */
    public String getCode() {
        return code;
    }

    /**
     * Obtain the hostId.
     *
     * @return A <code>String</code>.
     */
    public String getHostId() {
        return hostId;
    }

    /**
     * Obtain the message.
     *
     * @return A <code>String</code>.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtain the requestId.
     *
     * @return A <code>String</code>.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Determine if the error response is recoverable.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isRecoverable() {
        if (null == code) {
            return Boolean.FALSE;
        } else {
            for (final String rec : RECOVERABLE_ERROR_CODES) {
                if (rec.equals(code)) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }
    }

    /**
     * Set the code.
     *
     * @param code
     *		A <code>Code</code>.
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * Set the hostId.
     *
     * @param hostId
     *		A <code>String</code>.
     */
    public void setHostId(final String hostId) {
        this.hostId = hostId;
    }

    /**
     * Set the message.
     *
     * @param message
     *		A <code>String</code>.
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Set the requestId.
     *
     * @param requestId
     *		A <code>String</code>.
     */
    public void setRequestId(final String requestId) {
        this.requestId = requestId;
    }
}
