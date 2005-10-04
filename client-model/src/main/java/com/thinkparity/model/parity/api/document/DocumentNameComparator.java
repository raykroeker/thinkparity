/*
 * Apr 2, 2005
 */
package com.thinkparity.model.parity.api.document;

import com.thinkparity.model.parity.model.document.Document;

/**
 * DocumentNameComparator
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentNameComparator implements DocumentComparator {

	/**
	 * Compare to documents based upon their names.
	 * 
	 * @param d1
	 *            The original document.
	 * @param d2
	 *            The second document.
	 * @return The comparison result of comparing the names of the documents.
	 */
	public int compare(Document d1, Document d2) {
		return d1.getName().compareTo(d2.getName());
	}
}