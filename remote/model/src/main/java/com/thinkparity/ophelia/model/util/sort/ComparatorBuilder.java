/*
 * Jan 25, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.ophelia.model.audit.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ComparatorBuilder {

	/**
	 * Create a ComparatorBuilder.
	 */
	public ComparatorBuilder() { super(); }

    public Comparator<ArtifactReceipt> createArtifactReceiptByReceivedOnAscending() {
        return new ArtifactReceiptReceivedOn(Boolean.TRUE);
    }

    /**
     * Create a name comparator in ascending order.
     * 
     * @return The name comparator.
     */
    public Comparator<Artifact> createByName() {
        return new NameComparator(Boolean.TRUE);
    }

    /**
     * Create a name comparator.
     * 
     * @param isAscending
     *            Flag indicating whether or not the sort is ascending or
     *            descending.
     * @return The name comparator.
     */
    public Comparator<Artifact> createByName(final Boolean isAscending) {
        return new NameComparator(isAscending);
    }

    public Comparator<HistoryItem> createDateAscending() {
		return new HistoryItemDateComparator(Boolean.TRUE);
	}

    public Comparator<HistoryItem> createDateDescending() {
		return new HistoryItemDateComparator(Boolean.FALSE);
	}

    public Comparator<HistoryItem> createIdAscending() {
        return new HistoryItemIdComparator(Boolean.TRUE);
    }

    public Comparator<HistoryItem> createIdDescending() {
        return new HistoryItemIdComparator(Boolean.FALSE);
    }

	/**
	 * Create a version id comparator.
	 * 
	 * @param isAscending
	 *            Flag indicating whether or not the sort is ascending or
	 *            descending.
	 * @return The version id comparator.
	 */
	public Comparator<ArtifactVersion> createVersionById(
			final Boolean isAscending) {
		return new VersionIdComparator(isAscending);
	}

	/**
     * Create a version artifact name comparator.
     * 
     * @return An <code>ArtifactVersion</code> <code>Comparator</code>.
     */
    public Comparator<ArtifactVersion> createVersionByName() {
        return new VersionArtifactNameComparator(Boolean.TRUE);
    }
}
