/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.message.system;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageId implements Comparable<SystemMessageId> {

	/**
	 * Create a SystemMessageId.
	 */
	public SystemMessageId() { super(); }

	/**
	 * @see java.lang.Comparable#compareTo(T)
	 * 
	 */
	public int compareTo(final SystemMessageId o) {
		return getId().compareTo(o.getId());
	}

	public String getId() { return ""; }
}
