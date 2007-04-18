/*
 * Created On:  30-Mar-07 1:49:11 PM
 */
package com.thinkparity.codebase.model.profile;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.util.Token;

/**
 * <b>Title:</b>thinkParity CommonModel Profile Reservation<br>
 * <b>Description:</b>Used to reserve a profile account for creation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Reservation {

    /** The reservation's creation date. */
    private Calendar createdOn;

    /** An <code>EMail</code> address. */
    private EMail email;

    /** The reservation's expiry date. */
    private Calendar expiresOn;

    /** The reservation token <code>String</code>. */
    private Token token;

    /** A username <code>String</code>. */
    private String username;

    /**
     * Create UsernameReservation.
     *
     */
    public Reservation() {
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
     * Obtain email.
     *
     * @return A EMail.
     */
    public EMail getEMail() {
        return email;
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
     * @return A String.
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
     * Set email.
     *
     * @param email
     *		A EMail.
     */
    public void setEMail(final EMail email) {
        this.email = email;
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
     *		A String.
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
