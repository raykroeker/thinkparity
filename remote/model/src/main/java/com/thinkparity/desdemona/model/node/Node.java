/*
 * Created On:  4-Sep-07 2:58:02 PM
 */
package com.thinkparity.desdemona.model.node;

/**
 * <b>Title:</b>thinkParity Desdmona Model Node<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Node {

    /** A node id. */
    private transient Long id;

    /** A node username. */
    private String username;

    /**
     * Create Node.
     *
     */
    public Node() {
        super();
    }

    /**
     * Obtain the id.
     *
     * @return A <code>Long</code>.
     */
    public Long getId() {
        return id;
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
     * Set the id.
     *
     * @param id
     *		A <code>Long</code>.
     */
    public void setId(final Long id) {
        this.id = id;
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
