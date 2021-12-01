/*
 * Jan 22, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.artifact.Artifact;


/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
class NameComparator extends AbstractArtifactComparator {

    /** A general purpose string comparator. */
    private final StringComparator comparator;

	/**
	 * Create a NameComparator.
	 * 
	 */
	NameComparator(final Boolean doCompareAscending) {
		super(doCompareAscending);
        this.comparator = new StringComparator(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(final Artifact o1, final Artifact o2) {
		final int compareResult = comparator.compare(o1.getName(), o2.getName());
		if (0 == compareResult) {
            return subCompare(o1, o2);
		} else {
            return compareResult;
		}
	}
}
