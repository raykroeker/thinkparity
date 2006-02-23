/*
 * Jan 25, 2006
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Comparator;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.document.history.HistoryItem;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

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
