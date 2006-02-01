/*
 * Jan 25, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Comparator;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ComparatorBuilder {

	/**
	 * Create a ComparatorBuilder.
	 */
	public ComparatorBuilder() { super(); }

	private Comparator<SystemMessage> systemMessageDefault;

	public Comparator<SystemMessage> createSystemMessageDefault() {
		if(null == systemMessageDefault) {
			systemMessageDefault = new SystemMessageIdComparator(Boolean.TRUE);
		}
		return systemMessageDefault;
	}

	/**
	 * Create a name comparator.
	 * 
	 * @param isAscending
	 *            Flag indicating whether or not the sort is ascending or
	 *            descending.
	 * @return The name comparator.
	 */
	public Comparator<ParityObject> createByName(final Boolean isAscending) {
		return new NameComparator(isAscending);
	}

	/**
	 * Create a version id comparator.
	 * 
	 * @param isAscending
	 *            Flag indicating whether or not the sort is ascending or
	 *            descending.
	 * @return The version id comparator.
	 */
	public Comparator<ParityObjectVersion> createVersionById(
			final Boolean isAscending) {
		return new VersionIdComparator(isAscending);
	}
}
