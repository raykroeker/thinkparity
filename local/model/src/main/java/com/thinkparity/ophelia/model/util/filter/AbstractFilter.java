/*
 * Mar 22, 2006
 */
package com.thinkparity.ophelia.model.util.filter;

/**
 * TODO Remove from model and reference codebase.
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public abstract class AbstractFilter<T> implements Filter<T> {

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(final Object obj) {
		return null != obj && getClass().equals(obj.getClass());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return getClass().hashCode(); }


}
