/*
 * Jan 22, 2006
 */
package com.thinkparity.model.parity.model.sort;

import com.thinkparity.model.parity.model.artifact.Artifact;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class NameComparator extends AbstractArtifactComparator {

	/**
	 * Create a NameComparator.
	 * 
	 */
	NameComparator(final Boolean doCompareAscending) {
		super(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(Artifact o1, Artifact o2) {
		final int compareResult = o1.getName().compareTo(o2.getName());
		if(0 == compareResult) { return subCompare(o1, o2); }
		else { return compareResult * resultMultiplier; }
	}
}
