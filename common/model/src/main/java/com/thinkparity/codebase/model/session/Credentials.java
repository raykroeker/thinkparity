/*
 * Created On: Jun 11, 2006 3:16:55 PM
 */
package com.thinkparity.codebase.model.session;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.0
 */
public class Credentials {

    /** The password. */
    private transient String password;

    /** The username. */
    private String username;

    /** Create Credentials. */
    public Credentials() { super(); }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof Credentials) {
            return ((Credentials) obj).username.equals(username) &&
                ((Credentials) obj).password.equals(password);
        }
        else { return false; }
    }

    /**
     * Obtain the password
     *
     * @return The String.
     */
    public String getPassword() { return password; }

    /**
     * Obtain the username
     *
     * @return The String.
     */
    public String getUsername() { return username; }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return username.hashCode() & password.hashCode(); }

    /**
     * Set password.
     *
     * @param password The password.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Set username.
     *
     * @param username The username.
     */
    public void setUsername(final String username) { this.username = username; }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(username)
                .toString();
    }
}
