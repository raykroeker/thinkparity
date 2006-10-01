/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.io.md;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum MetaDataType {

	BOOLEAN(0), CALENDAR(1), JABBER_ID(2), LONG(3), STRING(4), USER_ID(5);

	/**
	 * Resolve the meta data type by it's id.
	 * 
	 * @param id
	 *            The meta data type id.
	 * @return The meta data.
	 */
	public static MetaDataType fromId(final Integer id) {
		switch(id) {
		case 0: return BOOLEAN;
		case 1: return CALENDAR;
		case 2: return JABBER_ID;
		case 3: return LONG;
		case 4: return STRING;
        case 5: return USER_ID;
		default:
			throw Assert.createUnreachable("UNKNOWN META DATA TYPE");
		}
	}

	/** A meta data type id <code>Integer</code>. */
	private final Integer id;

	/**
     * Create MetaDataType.
     * 
     * @param id
     *            A meta data type id  <code>Integer</code>.
     */
	private MetaDataType(final Integer id) {
		this.id = id;
	}

	/**
	 * Obtain the meta data type id.
	 * 
	 * @return A meta data type id <code>Integer</code>.
	 */
	public Integer getId() { return id; }
}
