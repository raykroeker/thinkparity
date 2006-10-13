/*
 * Jan 25, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;

import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.message.SystemMessage;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ComparatorBuilder {

	private Comparator<SystemMessage> systemMessageDefault;

	/**
	 * Create a ComparatorBuilder.
	 */
	public ComparatorBuilder() { super(); }

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

	public Comparator<SystemMessage> createSystemMessageDefault() {
		if(null == systemMessageDefault) {
			systemMessageDefault = new SystemMessageIdComparator(Boolean.TRUE);
		}
		return systemMessageDefault;
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
}
