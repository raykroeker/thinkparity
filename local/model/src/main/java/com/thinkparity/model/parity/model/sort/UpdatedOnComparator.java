/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.sort;

import com.thinkparity.model.artifact.Artifact;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UpdatedOnComparator extends AbstractArtifactComparator {

	/**
	 * Create a UpdatedOnComparator.
	 * 
	 * @param doCompareAscending
	 *            Flag indicating whether or not to sort in ascending order.
	 */
	public UpdatedOnComparator(final Boolean doCompareAscending) {
		super(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Artifact o1, Artifact o2) {
		final int compareResult = o1.getUpdatedOn().compareTo(o2.getUpdatedOn());
		if(0 == compareResult) { return subCompare(o1, o2); }
		else { return compareResult * resultMultiplier; }
	}
}
