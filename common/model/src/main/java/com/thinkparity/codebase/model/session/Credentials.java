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

    /** The resource. */
    private String resource;

    /** The username. */
    private String username;

    /** Create Credentials. */
    public Credentials() { super(); }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof Credentials) {
            return ((Credentials) obj).username.equals(username) &&
                ((Credentials) obj).password.equals(password) &&
                ((Credentials) obj).resource.equals(resource);
        } else {
            return false;
        }
    }

    /**
     * Obtain the password
     *
     * @return The String.
     */
    public String getPassword() { return password; }

    /**
     * Obtain the resource
     *
     * @return The String.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Obtain the username
     *
     * @return The String.
     */
    public String getUsername() { return username; }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((resource == null) ? 0 : resource.hashCode());
        result = PRIME * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    /**
     * Determine whether or not the resource is set.
     * 
     * @return True if the resource is set; false otherwise.
     */
    public Boolean isSetResource() {
        return null != resource;
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
     * Set resource.
     *
     * @param resource The String.
     */
    public void setResource(final String resource) {
        this.resource = resource;
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
            .append(username).append("/")
            .append(resource).toString();
    }
}
