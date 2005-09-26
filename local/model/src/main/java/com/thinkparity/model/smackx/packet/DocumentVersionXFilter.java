/*
 * May 31, 2005
 */
package com.thinkparity.model.smackx.packet;

import com.thinkparity.model.smackx.XFilter;

/**
 * DocumentVersionXFilter
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionXFilter extends XFilter {

	/**
	 * Create a new DocumentVersionXFilter.
	 */
	public DocumentVersionXFilter() {
		super(DocumentVersionX.getXElementName(), DocumentVersionX
				.getXNamespace());
	}
}
