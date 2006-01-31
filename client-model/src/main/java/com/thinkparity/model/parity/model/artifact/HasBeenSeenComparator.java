/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectFlag;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HasBeenSeenComparator extends AbstractArtifactComparator {

	/**
	 * Flag to compare.
	 * 
	 */
	private final ParityObjectFlag flagToCompare = ParityObjectFlag.SEEN;


	/**
	 * Create a HasBeenSeenComparator.
	 * 
	 * @param doCompareAscending
	 *            Compare asending\descending.
	 */
	public HasBeenSeenComparator(final Boolean doCompareAscending) {
		super(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(final ParityObject o1, final ParityObject o2) {
		if(o1.contains(flagToCompare)) {
			if(o2.contains(flagToCompare)) { return subCompare(o1, o2); }
			else { return -1 * resultMultiplier; }
		}
		else {
			if(o2.contains(flagToCompare)) { return 1 * resultMultiplier; }
			else { return subCompare(o1, o2); }
		}
	}
}
