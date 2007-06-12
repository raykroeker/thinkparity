/*
 * Created On: Jun 11, 2006 3:16:55 PM
 */
package com.thinkparity.codebase.model.session;

/**
 * <b>Title:</b>thinkParity CommonModel Session Credentials<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
public final class Credentials {

    /** The password. */
    private String password;

    /** The username. */
    private String username;

    /**
     * Create Credentials.
     *
     */
    public Credentials() {
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
        return ((Credentials) obj).username.equals(username)
                && ((Credentials) obj).password.equals(password);
    }

    /**
     * Obtain the password
     *
     * @return The String.
     */
    public String getPassword() {
        return password;
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
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

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
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @see java.lang.Object#toString()
     * 
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(username).toString();
    }
}
