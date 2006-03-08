/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IndexHit {

	/**
	 * The index id.
	 * 
	 */
	private final Long id;

	/**
	 * Create a IndexHit.
	 */
	public IndexHit(final Long id) {
		super();
		this.id = id;
	}

	/**
	 * Obtain the id.
	 * 
	 * @return The id.
	 */
	public Long getId() { return id; }
}
