/*
 * Created On: Thu May 04 2006 11:43 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.util.sort;

import java.util.Comparator;

import com.thinkparity.ophelia.model.audit.HistoryItem;

/**
 * A history item comparator that knows how to compare history item
 * ids.
 *
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class HistoryItemIdComparator implements Comparator<HistoryItem> {

	/** The  multiplier to control ascending\descending order. */
	private final Integer mux;

	/**
     * Create HistoryItemDateComparator.
     *
     * @param doCompareAscending
     *      Whether or not to compare in ascending order.
	 */
	HistoryItemIdComparator(final Boolean doCompareAscending) {
		super();
		this.mux = doCompareAscending ? 1 : -1;
	}

	/** @see java.util.Comparator#compare(HistoryItem, HistoryItem) */
	public int compare(final HistoryItem o1, final HistoryItem o2) {
		final int compareResult = o1.getId().compareTo(o2.getId());
		return compareResult * mux;
	}
}
