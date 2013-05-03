/*
 * Created On: Jun 11, 2006 3:16:55 PM
 */
package com.thinkparity.codebase.model.session;

import java.util.Calendar;

import com.thinkparity.codebase.HashCodeUtil;
import com.thinkparity.codebase.StringUtil;

import com.thinkparity.codebase.model.util.Token;

/**
 * <b>Title:</b>thinkParity CommonModel Session Temporary Credentials<br>
 * <b>Description:</b>A set of temporary session credentials.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TemporaryCredentials {

    /** The credential's creation date. */
    private Calendar createdOn;

    /** The credential's expiry date. */
    private Calendar expiresOn;

    /** The <code>Token</code>. */
    private Token token;

    /** The username. */
    private String username;

    /**
     * Create TemporaryCredentials.
     *
     */
    public TemporaryCredentials() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return ((TemporaryCredentials) obj).username.equals(username)
                && ((TemporaryCredentials) obj).token.equals(token);
    }

    /**
     * Obtain createdOn.
     *
     * @return A Calendar.
     */
    public Calendar getCreatedOn() {
        return createdOn;
    }

    /**
     * Obtain expiresOn.
     *
     * @return A Calendar.
     */
    public Calendar getExpiresOn() {
        return expiresOn;
    }

    /**
     * Obtain token.
     *
     * @return A Token.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Obtain the username
     *
     * @return The String.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return HashCodeUtil.hashCode(username, token);
    }

    /**
     * Set createdOn.
     *
     * @param createdOn
     *		A Calendar.
     */
    public void setCreatedOn(final Calendar createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * Set expiresOn.
     *
     * @param expiresOn
     *		A Calendar.
     */
    public void setExpiresOn(final Calendar expiresOn) {
        this.expiresOn = expiresOn;
    }

    /**
     * Set token.
     *
     * @param token
     *		A Token.
     */
    public void setToken(final Token token) {
        this.token = token;
    }

    /**
     * Set username.
     * 
     * @param username
     *            A username <code>String</code>.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @see java.lang.Object#toString()
     * 
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(), "createdOn", createdOn,
                "expiresOn", expiresOn, "username", username, "token", token);
    }
}
