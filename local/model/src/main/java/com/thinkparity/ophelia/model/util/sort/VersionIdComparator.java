/*
 * Jan 22, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;



/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class VersionIdComparator implements Comparator<ArtifactVersion> {

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
	 * 
	 */
	public int compare(final ArtifactVersion o1, final ArtifactVersion o2) {
		return o1.getVersionId().compareTo(o2.getVersionId()) * resultMultiplier;
	}
}
