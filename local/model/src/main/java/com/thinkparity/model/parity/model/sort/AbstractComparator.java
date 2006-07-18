/*
 * Created On: Jul 17, 2006 3:08:43 PM
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public abstract class AbstractComparator<T> implements Comparator<T> {

	/**
	 * List of comparators to use if the compare objects are equal.
	 * 
	 * @see #add(Comparator)
	 * @see #remove(Comparator)
	 */
	protected final List<Comparator<T>> comparators;

	/**
	 * Multiplier to use when returning the compare result. Controls the
	 * ascending descending value of the sort.
	 */
	protected final int resultMultiplier;

	/**
	 * Create a AbstractArtifactComparator.
	 * 
	 */
	public AbstractComparator(final Boolean doCompareAscending) {
		super();
		this.resultMultiplier = doCompareAscending ? 1 : -1;
		this.comparators = new LinkedList<Comparator<T>>();
	}

	/**
	 * Add a comparator to the list of compare operations.
	 * 
	 * @param comparator
	 *            The comparator to add.
	 */
	public boolean add(final Comparator<T> comparator) {
		Assert.assertNotNull("Cannot add a null comparator.", comparator);
		return comparators.add(comparator);
	}

	/**
	 * Remove a comparator to the list of compare operations.
	 * 
	 * @param comparator
	 *            The comparator to remove.
	 */
	public boolean remove(final Comparator<T> comparator) {
		Assert.assertNotNull("Cannot remove a null comparator.", comparator);
		return comparators.remove(comparator);
	}

	/**
	 * Check the list of comparators chained to this one. The first non-equal
	 * result is returned.
	 * 
	 * @param o1
	 *            The first parity object to be compared.
	 * @param o2
	 *            The second parity object to be compared.
	 * @return 0 if none of the comparators finds a difference; -1 if o1 is less
	 *         than o2 and 1 if o2 is less than o1.
	 */
	protected int subCompare(final T o1, final T o2) {
		if(!comparators.isEmpty()) {
			int compareResult;
			for(final Comparator<T> c : comparators) {
				compareResult = c.compare(o1, o2);
				if(0 != compareResult) { return compareResult; }
			}
			return 0;
		}
		else { return 0; }
	}
}
