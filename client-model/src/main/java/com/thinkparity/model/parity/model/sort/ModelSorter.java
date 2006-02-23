/*
 * Feb 22, 2006
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * Utility convenience class for sorting lists.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ModelSorter {

	public static void sortHistoryItems(final List<HistoryItem> list,
			final Comparator<HistoryItem> comparator) {
		Collections.sort(list, comparator);
	}

	/**
	 * Create a ModelSorter [Singleton]
	 * 
	 */
	private ModelSorter() { super(); }
}
