/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;



/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueryHit {

	private Long dataId;
	
	/**
	 * Create a QueryHit.
	 * 
	 */
	QueryHit() { super(); }

	/**
	 * Obtain the data id.
	 * 
	 * @return The data id.
	 */
	public Long getDataId() { return dataId; }

	/**
	 * Set the data id.
	 * 
	 * @param dataId
	 *            The data id.
	 */
	public void setDataId(final Long dataId) { this.dataId = dataId; }
}
