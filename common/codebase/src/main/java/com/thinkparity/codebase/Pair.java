/*
 * Mar 29, 2006
 */
package com.thinkparity.codebase;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Pair extends Duality {

	/**
     * Create a Pair.
     * 
     * @param first
     *            The first object.
     * @param second
     *            The second object.
     */
	public Pair(final Object first, final Object second) {
		super(first, second);
	}

	/**
	 * @see com.thinkparity.codebase.Duality#getFirst()
	 * 
	 */
	public Object getFirst() { return super.getFirst(); }

	/**
	 * @see com.thinkparity.codebase.Duality#getSecond()
	 * 
	 */
	public Object getSecond() { return super.getSecond(); }

	/**
	 * @see com.thinkparity.codebase.Duality#getFirstLabel()
	 */
	protected String getFirstLabel() { return null; }

	/**
	 * @see com.thinkparity.codebase.Duality#getSecondLabel()
	 */
	protected String getSecondLabel() { return null; }
}
