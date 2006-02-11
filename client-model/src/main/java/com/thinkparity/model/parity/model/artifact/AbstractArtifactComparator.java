/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractArtifactComparator implements Comparator<Artifact> {

	/**
	 * List of comparators to use if the compare objects are equal.
	 * 
	 * @see #add(Comparator)
	 * @see #remove(Comparator)
	 */
	protected final List<Comparator<Artifact>> comparators;

	/**
	 * Multiplier to use when returning the compare result. Controls the
	 * ascending descending value of the sort.
	 */
	protected final int resultMultiplier;

	/**
	 * Create a AbstractArtifactComparator.
	 * 
	 */
	public AbstractArtifactComparator(final Boolean doCompareAscending) {
		super();
		this.resultMultiplier = doCompareAscending ? 1 : -1;
		this.comparators = new LinkedList<Comparator<Artifact>>();
	}

	/**
	 * Add a comparator to the list of compare operations.
	 * 
	 * @param comparator
	 *            The comparator to add.
	 */
	public boolean add(final Comparator<Artifact> comparator) {
		Assert.assertNotNull("Cannot add a null comparator.", comparator);
		return comparators.add(comparator);
	}

	/**
	 * Remove a comparator to the list of compare operations.
	 * 
	 * @param comparator
	 *            The comparator to remove.
	 */
	public boolean remove(final Comparator<Artifact> comparator) {
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
	protected int subCompare(final Artifact o1, final Artifact o2) {
		if(!comparators.isEmpty()) {
			int compareResult;
			for(Comparator<Artifact> c : comparators) {
				compareResult = c.compare(o1, o2);
				if(0 != compareResult) { return compareResult; }
			}
			return 0;
		}
		else { return 0; }
	}
}
