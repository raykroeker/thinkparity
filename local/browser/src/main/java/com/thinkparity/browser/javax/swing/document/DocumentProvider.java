/*
 * Jan 6, 2006
 */
package com.thinkparity.browser.javax.swing.document;

import java.util.List;

import com.thinkparity.model.parity.model.document.Document;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface DocumentProvider {
	public List<Document> getDocuments();
}
