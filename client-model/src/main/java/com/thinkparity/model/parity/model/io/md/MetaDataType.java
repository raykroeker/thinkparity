/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.io.md;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum MetaDataType {

	STRING(0);

	/**
	 * Resolve the meta data type by it's id.
	 * 
	 * @param id
	 *            The meta data type id.
	 * @return The meta data.
	 */
	public static MetaDataType fromId(final Integer id) {
		switch(id) {
		case 0: return STRING;
		default:
			throw Assert.createUnreachable(
					"Could not determine meta data type:  " + id);
		}
	}

	/**
	 * Meta data type id.
	 * 
	 */
	private final Integer id;

	/**
	 * Create a MetaDataType.
	 * 
	 * @param id
	 *            The type id.
	 */
	private MetaDataType(final Integer id) {
		this.id = id;
	}

	/**
	 * Obtain the type id.
	 * 
	 * @return The type id.
	 */
	public Integer getId() { return id; }
}
