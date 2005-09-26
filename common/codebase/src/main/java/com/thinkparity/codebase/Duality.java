/*
 * Oct 4, 2003
 */
package com.thinkparity.codebase;

/**
 * <b>Title:</b>  Duality
 * <br><b>Description:</b>  A duality is simply a set of objects which belong
 * together.
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class Duality {

	private Object first;
	private Object second;

	/**
	 * Create a new Duality
	 * @param first <code>java.lang.Object</code>
	 * @param second <code>java.lang.Object</code>
	 */
	protected Duality(Object first, Object second) {
		super();
		this.first = first;
		this.second = second;
	}

	/**
	 * Obtain the first object in the duality
	 * @return <code>java.lang.Object</code>
	 */
	protected Object getFirst() {
		return first;
	}

	/**
	 * Obtain the second object in the duality
	 * @return <code>java.lang.Object</code>
	 */
	protected Object getSecond() {
		return second;
	}

	/**
	 * Set the first object in the duality
	 * @param object <code>java.lang.Object</code>
	 */
	protected void setFirst(Object object) {
		first = object;
	}

	/**
	 * Set the second object in the duality
	 * @param object <code>java.lang.Object</code>
	 */
	protected void setSecond(Object object) {
		second = object;
	}
	
	/**
	 * Obtain the label for the first
	 * @return <code>java.lang.String</code>
	 */
	protected abstract String getFirstLabel();
	
	/**
	 * Obtain the label for the second
	 * @return <code>java.lang.String</code>
	 */
	protected abstract String getSecondLabel();

}
