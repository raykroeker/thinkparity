/*
 * Created On:  6-Sep-07 9:49:49 AM
 */
package com.thinkparity.desdemona.model.profile.payment;

/**
 * <b>Title:</b>thinkParity Profile Payment Currency<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Currency {

    /** A currency id. */
    private transient Integer id;

    /** A name. */
    private String name;

    /**
     * Create Currency.
     *
     */
    public Currency() {
        super();
    }

    /**
     * Obtain the id.
     *
     * @return A <code>Integer</code>.
     */
    public Integer getId() {
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
     *		A <code>Integer</code>.
     */
    public void setId(final Integer id) {
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
