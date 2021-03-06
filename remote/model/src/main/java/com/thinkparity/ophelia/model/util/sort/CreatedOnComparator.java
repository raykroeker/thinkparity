/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import com.thinkparity.codebase.model.artifact.Artifact;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreatedOnComparator extends AbstractArtifactComparator {

	/**
	 * Create a CreatedOnComparator.
	 * 
	 * @param doCompareAscending
	 *            Flag indicating whether or not to sort in ascending order.
	 */
	public CreatedOnComparator(Boolean doCompareAscending) {
		super(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Artifact o1, Artifact o2) {
		final int compareResult = o1.getCreatedOn().compareTo(o2.getCreatedOn());
		if(0 == compareResult) { return subCompare(o1, o2); }
		else { return compareResult * resultMultiplier; }
	}
}
