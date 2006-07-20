/*
 * Created On: Feb 24, 2006
 */
package com.thinkparity.model.xmpp;

/**
 * <b>Title:</b>thinkParity Remote JabberId<br>
 * <b>Description:</b> A thinkParity remote jabber id implementation.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 * @see JabberIdBuilder
 */
public class JabberId {

    /** The host component of the id. */
    private String host;

    /** The resource component of the id. */
    private String resource;

    /** The username component of the id. */
    private String username;

    /**
     * Create a JabberId.
     * 
     * @param username
     *            The jabber username.
     * @param host
     *            The jabber host.
     * @param resource
     *            The jabber resource.
     */
    protected JabberId(final String username, final String host,
            final String resource) {
        super();
        this.username = username;
        this.host = host;
        this.resource = resource;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof JabberId) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    public String getQualifiedJabberId() {
        return new StringBuffer(username)
            .append("@")
            .append(host)
            .append("/")
            .append(resource)
            .toString();
    }

    public String getQualifiedUsername() {
        return new StringBuffer(username)
            .append("@")
            .append(host)
            .toString();
    }

    public String getUsername() { return username; }

    /**
     * @see java.lang.Object#hashCode()
     * 
     */
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     * 
     */
    public String toString() {
        return getClass().getName() + "\\" +getQualifiedJabberId();
    }
}
