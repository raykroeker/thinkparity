/*
 * Nov 9, 2005
 */
package com.thinkparity.browser.platform.util.model;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.parity.model.document.Document;

/**
 * The parity object helper provides common utility functionality for the user
 * interface based upon parity objects.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class ArtifactUtil {

	/** A singleton instance. */
	private static final ArtifactUtil SINGLETON;

	static { SINGLETON = new ArtifactUtil(); }

	/**
     * Obtain the document's filename extension.
     * 
     * @param document
     *            A thinkParity document.
     * @return The document's filename extension.
     */
	public static String getNameExtension(final Document document) {
	    return SINGLETON.doGetNameExtension(document);
	}

	/** Create ArtifactUtil. */
	private ArtifactUtil() { super(); }

	/**
     * Obtain the document's filename extension.
     * 
     * @param document
     *            A thinkParity document.
     * @return The document's filename extension.
     */
	private String doGetNameExtension(final Document document) {
		final String name = document.getName();
		return FileUtil.getExtension(name);
	}
}
