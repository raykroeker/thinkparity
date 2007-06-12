/*
 * Created On:  30-Mar-07 1:49:11 PM
 */
package com.thinkparity.codebase.model.profile;

import java.util.Calendar;

import com.thinkparity.codebase.model.util.Token;

/**
 * <b>Title:</b>thinkParity CommonModel Profile Reservation<br>
 * <b>Description:</b>Used to reserve a profile account for creation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UsernameReservation {

    /** The reservation's creation date. */
    private Calendar createdOn;

    /** The reservation's expiry date. */
    private Calendar expiresOn;

    /** A <code>Token</code>. */
    private Token token;

    /** A username <code>String</code>. */
    private String username;

    /**
     * Create UsernameReservation.
     *
     */
    public UsernameReservation() {
        super();
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
     * Obtain usernameToken.
     *
     * @return A Token.
     */
    public Token getToken() {
        return token;
    }

    /**
     * Obtain username.
     *
     * @return A String.
     */
    public String getUsername() {
        return username;
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
     * Set usernameToken.
     *
     * @param usernameToken
     *		A Token.
     */
    public void setToken(final Token token) {
        this.token = token;
    }

    /**
     * Set username.
     *
     * @param username
     *		A String.
     */
    public void setUsername(final String username) {
        this.username = username;
    }
}
