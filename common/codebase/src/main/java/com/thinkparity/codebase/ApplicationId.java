/*
 * Mar 14, 2006
 */
package com.thinkparity.codebase;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ApplicationId {

	/**
	 * Obtain the next application id.
	 * 
	 * @return The next application id.
	 */
	public static ApplicationId next() { return new ApplicationId(); }

	/**
	 * A unique id for an application.
	 * 
	 */
	private final JVMUniqueId uniqueId;

	/**
	 * Create a ApplicationId.
	 * 
	 */
	private ApplicationId() {
		super();
		this.uniqueId = JVMUniqueId.nextId();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof ApplicationId) {
			return ((ApplicationId) obj).uniqueId.equals(uniqueId);
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return uniqueId.hashCode(); }

	/**
	 * @see java.lang.Object#toString()
	 * 
	 */
	public String toString() { return uniqueId.toString(); }

	

	
}
