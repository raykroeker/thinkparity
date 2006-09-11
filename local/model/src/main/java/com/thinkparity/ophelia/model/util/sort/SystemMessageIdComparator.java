/*
 * Jan 31, 2006
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.ophelia.model.message.SystemMessage;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageIdComparator implements Comparator<SystemMessage> {

	private final int resultMultiplier;

	/**
	 * Create a SystemMessageIdComparator.
	 * @param doCompareAscending
	 */
	public SystemMessageIdComparator(final Boolean doCompareAscending) {
		super();
		this.resultMultiplier = doCompareAscending ? 1 : -1;
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(SystemMessage o1, SystemMessage o2) {
		return o1.getId().compareTo(o2.getId()) * resultMultiplier;
	}
}
