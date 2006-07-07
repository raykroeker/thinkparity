/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Comparator;

import com.thinkparity.model.parity.model.audit.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryItemDateComparator implements Comparator<HistoryItem> {

	/**
	 * The result multiplier to control ascending\descending order.
	 * 
	 */
	private final Integer resultMultiplier;

	/**
	 * Create a HistoryItemDateComparator.
	 */
	HistoryItemDateComparator(final Boolean doCompareAscending) {
		super();
		this.resultMultiplier = doCompareAscending ? 1 : -1;
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(final HistoryItem o1, final HistoryItem o2) {
		final int compareResult = o1.getDate().compareTo(o2.getDate());
		return compareResult * resultMultiplier;
	}
}
