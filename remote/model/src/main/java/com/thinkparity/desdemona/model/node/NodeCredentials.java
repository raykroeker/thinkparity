/*
 * Created On:  4-Sep-07 1:13:46 PM
 */
package com.thinkparity.desdemona.model.node;

/**
 * <b>Title:</b>thinkParity Desdmona Model Node Credentials<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NodeCredentials {

    /** A password. */
    private String password;

    /** A username. */
    private String username;

    /**
     * Create NodeCredentials.
     *
     */
    public NodeCredentials() {
        super();
    }

    /**
     * Obtain the password.
     *
     * @return A <code>String</code>.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtain the username.
     *
     * @return A <code>String</code>.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the password.
     *
     * @param password
     *		A <code>String</code>.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Set the username.
     *
     * @param username
     *		A <code>String</code>.
     */
    public void setUsername(final String username) {
        this.username = username;
    }
}
