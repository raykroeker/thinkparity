/*
 * Mar 27, 2006
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Calendar;

import com.thinkparity.model.artifact.Artifact;

/**
 * Order by the artifact's remote updated on date\time.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RemoteUpdatedOnComparator extends AbstractArtifactComparator {

	/**
	 * Create a UpdatedOnComparator.
	 * 
	 * @param doCompareAscending
	 *            Flag indicating whether or not to sort in ascending order.
	 */
	public RemoteUpdatedOnComparator(final Boolean doCompareAscending) {
		super(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(final Artifact o1, final Artifact o2) {
		final Calendar c1 = o1.getRemoteInfo().getUpdatedOn();
		final Calendar c2 = o2.getRemoteInfo().getUpdatedOn();
		final int compareResult;
		if(null == c1) {
			if(null == c2) { compareResult = 0; }
			else {
				// c1 is null; c2 is not
				compareResult = -1;
			}
		}
		else {
			if(null == c2) {
				// c2 is null; c1 is not
				compareResult = 1;
			}
			else { compareResult = c1.compareTo(c2); }
		}
		if(0 == compareResult) { return subCompare(o1, o2); }
		else { return compareResult * resultMultiplier; }
	}
}
