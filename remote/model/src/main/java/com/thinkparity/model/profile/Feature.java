/*
 * Created On: Sep 4, 2006 2:54:30 PM
 */
package com.thinkparity.model.profile;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Feature {

    /** The feature id. */
    private Long id;

    /** The feature name. */
    private String name;

    /** Create Feature. */
    public Feature() {
        super();
    }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getId() {
        return id;
    }

    /**
     * Obtain the name
     *
     * @return The String.
     */
    public String getName() {
        return name;
    }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Set name.
     *
     * @param name The String.
     */
    public void setName(final String name) {
        this.name = name;
    }
}
