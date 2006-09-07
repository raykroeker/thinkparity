/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.sort;

import com.thinkparity.model.artifact.Artifact;
import com.thinkparity.model.artifact.ArtifactFlag;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyComparator extends AbstractArtifactComparator {

	/**
	 * Flag to compare.
	 * 
	 */
	private final ArtifactFlag flagToCompare = ArtifactFlag.KEY;


	/**
	 * Create a KeyComparator.
	 * 
	 * @param doCompareAscending
	 *            Compare asending\descending.
	 */
	public KeyComparator(final Boolean doCompareAscending) {
		super(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(final Artifact o1, final Artifact o2) {
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
