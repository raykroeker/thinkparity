/*
 * Jan 22, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Comparator;

import com.thinkparity.model.parity.api.ParityObjectVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class VersionIdComparator implements Comparator<ParityObjectVersion> {

	/**
	 * Used to control the sort order.
	 * 
	 */
	private final int resultMultiplier;

	/**
	 * Create a VersionIdComparator.
	 * 
	 * @param doCompareAscending
	 *            Indicate whether to sort ascending or descending.
	 */
	public VersionIdComparator(final Boolean doCompareAscending) {
		super();
		this.resultMultiplier = doCompareAscending ? 1 : -1;
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(ParityObjectVersion o1, ParityObjectVersion o2) {
		return o1.getVersionId().compareTo(o2.getVersionId()) * resultMultiplier;
	}
}
