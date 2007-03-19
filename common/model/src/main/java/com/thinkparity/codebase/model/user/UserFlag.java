/*
 * Nov 14, 2005
 */
package com.thinkparity.codebase.model.user;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Model User Flag<br>
 * <b>Description:</b>A list of potential flags that can be applied to a user.
 * Note that all user flags should ascend by a power of two.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public enum UserFlag {

	CONTAINER_PUBLISH_RESTRICTED(1);

    /**
     * Extract the flag enumerated type from the id.
     * 
     * @param id
     *            A user flag id <code>Integer</code>.
     * @return A <code>UserFlag</code>.
     */
    public static UserFlag fromId(final Integer id) {
        switch(id) {
        case 1:
            return CONTAINER_PUBLISH_RESTRICTED;
        default:
            throw Assert.createUnreachable("Unknown user flag id:  " + id);
        }
    }

    /**
     * Extract the flags from an id sum.
     * 
     * @param idSum
     *            An integer representing the sum of the ids of user flags.
     * @return A <code>List</code> of <code>UserFlag</code>s.
     */
    public static List<UserFlag> fromIdSum(final Integer idSum) {
        final List<UserFlag> values = new ArrayList<UserFlag>(values().length);
        for (final UserFlag value : values())
            if (value.id == (idSum & value.id))
                values.add(value);
        return values;
    }

    /**
     * Extract the flags from an id sum.
     * 
     * @param idSum
     *            An integer representing the sum of the ids of user flags.
     * @return A <code>List</code> of <code>UserFlag</code>s.
     */
    public static Integer toIdSum(final List<UserFlag> flags) {
        int idSum = 0;
        for (final UserFlag flag : flags)
            idSum = idSum | flag.id;
        return Integer.valueOf(idSum);
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
