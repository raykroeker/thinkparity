/*
 * Jan 22, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Comparator;

import com.thinkparity.model.parity.api.ParityObject;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class NameComparator implements Comparator<ParityObject> {

	/**
	 * If we are doing an ascending sort, this value is one; otherwise it is
	 * negative one.
	 * 
	 */
	private final int resultMultiplier;

	/**
	 * Create a NameComparator.
	 * 
	 */
	NameComparator(final Boolean doCompareAscending) {
		super();
		this.resultMultiplier = doCompareAscending ? 1 : -1;
	}

	/**
	 * @see java.util.Comparator#compare(T, T)
	 * 
	 */
	public int compare(ParityObject o1, ParityObject o2) {
		return o1.getName().compareTo(o2.getName()) * resultMultiplier;
	}
}
