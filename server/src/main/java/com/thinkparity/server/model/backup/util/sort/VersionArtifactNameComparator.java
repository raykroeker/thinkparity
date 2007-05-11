/*
 * Jan 22, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class VersionArtifactNameComparator implements Comparator<ArtifactVersion> {

    /** A general purpose string comparator. */
    private final StringComparator comparator;

	/**
	 * Create a NameComparator.
	 * 
	 */
	VersionArtifactNameComparator(final Boolean doCompareAscending) {
		super();
        this.comparator = new StringComparator(doCompareAscending);
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(final ArtifactVersion o1, final ArtifactVersion o2) {
		return comparator.compare(o1.getArtifactName(), o2.getArtifactName());
	}
}
