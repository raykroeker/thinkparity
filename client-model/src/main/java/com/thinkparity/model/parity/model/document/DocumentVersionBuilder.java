/*
 * Jun 18, 2005
 */
package com.thinkparity.model.parity.model.document;



/**
 * DocumentVersionBuilder
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionBuilder {

	/**
	 * Singleton instance of the builder.
	 * @see DocumentVersionBuilder#singletonLock
	 */
	private static final DocumentVersionBuilder singleton;

	/**
	 * Synchronization lock.
	 * @see DocumentVersionBuilder#singleton
	 */
	private static final Object singletonLock;

	static {
		singleton = new DocumentVersionBuilder();
		singletonLock = new Object();
	}

	/**
	 * Create a new version for an existing document.
	 * 
	 * @param document
	 *            The document to create the version for.
	 * @return The new version for the document.
	 */
	public static DocumentVersion create(final Document document) {
		synchronized(singletonLock) {
			return singleton.createImpl(document);
		}
	}

	/**
	 * Obtain an named document version for a given document.
	 * 
	 * @param document
	 *            The document to obtain the version for.
	 * @param version
	 *            The named version of the document.
	 * @return The version of the document.
	 */
	public static DocumentVersion getVersion(final Document document,
			final String version) {
		synchronized(singletonLock) {
			return singleton.getVersionImpl(document, version);
		}
	}

	/**
	 * Create a DocumentVersionBuilder [Builder, Singleton]
	 */
	private DocumentVersionBuilder() { super(); }

	/**
	 * Obtain the next document version for a given document.
	 * 
	 * @param document
	 *            The document to obtain the version for.
	 * @return The next document version.
	 */
	private DocumentVersion createImpl(final Document document) {
		final String newVersion = createNextVersion(document);
		return new DocumentVersion(document, newVersion);
	}

	/**
	 * Obtain the next version in the sequence for a document.
	 * 
	 * @param document
	 *            The document to obtain the version for.
	 * @return The next version in the sequence.
	 */
	private String createNextVersion(final Document document) {
		final Integer numberOfVersions = document.getVersions().size();
		return new StringBuffer("v").append(numberOfVersions + 1).toString();
	}

	/**
	 * Obtain an named document version for a given document.
	 * 
	 * @param document
	 *            The document to obtain the version for.
	 * @param version
	 *            The named version of the document.
	 * @return The version of the document.
	 */
	private DocumentVersion getVersionImpl(final Document document,
			final String version) {
		return new DocumentVersion(document, version);
	}
}
