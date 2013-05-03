/*
 * Created On:  21-Dec-07 11:41:38 AM
 */
package com.thinkparity.ophelia.model.workspace.configuration;

/**
 * <b>Title:</b>thinkParity Common Proxy Credentials<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ProxyCredentials {

    /** A password. */
    private String password;

    /** A username. */
    private String username;

    /**
     * Create ProxyCredentials.
     *
     */
    public ProxyCredentials() {
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
