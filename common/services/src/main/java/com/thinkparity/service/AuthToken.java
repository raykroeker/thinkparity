/*
 * Created On:  30-May-07 9:09:17 AM
 */
package com.thinkparity.service;

import java.util.Date;

/**
 * <b>Title:</b>thinkParity Services Authentication Token<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AuthToken {

    /** A service client id <code>String</code>. */
    private String clientId;

    /** An expiry date. */
    private Date expiresOn;

    /** A user sesion id <code>String</code>. */
    private String sessionId;

    /**
     * Create AuthenticationToken.
     *
     */
    public AuthToken() {
        super();
    }

    /**
     * Obtain clientId.
     *
     * @return A String.
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Obtain expires on.
     *
     * @return A <code>Date</code>.
     */
    public Date getExpiresOn() {
        return expiresOn;
    }

    /**
     * Obtain sessionId.
     *
     * @return A String.
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Set clientId.
     *
     * @param clientId
     *		A String.
     */
    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    /**
     * Set expires on.
     *
     * @param expiresOn
     *		A <code>Date</code>.
     */
    public void setExpiresOn(final Date expiresOn) {
        this.expiresOn = expiresOn;
    }

    /**
     * Set sessionId.
     *
     * @param sessionId
     *		A String.
     */
    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }
}
