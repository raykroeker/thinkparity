/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import org.apache.lucene.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class QueryHit {

	private Long dataId;

	private Document document;
	
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
	 * @return Returns the document.
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Set the data id.
	 * 
	 * @param dataId
	 *            The data id.
	 */
	public void setDataId(final Long dataId) { this.dataId = dataId; }

	/**
	 * @param document The document to set.
	 */
	public void setDocument(Document document) {
		this.document = document;
	}
}
