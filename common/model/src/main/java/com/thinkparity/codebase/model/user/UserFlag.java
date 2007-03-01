/*
 * Nov 14, 2005
 */
package com.thinkparity.codebase.model.user;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Model User Flag<br>
 * <b>Description:</b>A list of potential flags that can be applied to a user.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public enum UserFlag {

	CONTAINER_PUBLISH_RESTRICTED(0);

    /**
     * Extract the flag enumerated type from the id.
     * 
     * @param id
     *            A user flag id <code>Integer</code>.
     * @return A <code>UserFlag</code>.
     */
	public static UserFlag fromId(final Integer id) {
		switch(id) {
		case 0:
            return CONTAINER_PUBLISH_RESTRICTED;
		default:
			throw Assert.createUnreachable("Unknown user flag id:  " + id);
		}
	}

	/** The user flag id <code>Integer</code>. */
	private Integer id;

	/**
     * Create UserFlag.
     * 
     * @param id
     *            The user flag id <code>Integer</code>.
     */
	private UserFlag(final Integer id) { this.id = id; }

	/**
     * Obtain the user flag id.
     * 
     * @return The user flag id <code>Integer</code>.
     */
	public Integer getId() {
        return id;
	}
}
