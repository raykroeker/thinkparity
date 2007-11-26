/*
 * Created On:  23-Nov-07 8:57:14 AM
 */
package com.thinkparity.desdemona.model.admin.message;

/**
 * <b>Title:</b>thinkParity Desdemona Model Admin Message Channel<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Channel {

    /** An id. */
    private Long id;

    /** A name. */
    private String name;

    /**
     * Create Channel.
     *
     */
    public Channel() {
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
     * Obtain the name.
     *
     * @return A <code>String</code>.
     */
    public String getName() {
        return name;
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
     * Set the name.
     *
     * @param name
     *		A <code>String</code>.
     */
    public void setName(final String name) {
        this.name = name;
    }
}
