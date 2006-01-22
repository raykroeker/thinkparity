/*
 * Jan 22, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSorter {

	/**
	 * Synchronization lock for the sort method.
	 * 
	 * @see ArtifactSorter#sort(Collection, Comparator)
	 */
	private static final Object LOCK;

	static { LOCK = new Object(); }

	/**
	 * Sort the list of documents by name.
	 * 
	 * @param documents
	 *            The list of documents.
	 */
	public static void sortByName(final Collection<Document> documents) {
		sortDocuments(documents, new NameComparator(Boolean.TRUE));
	}

	/**
	 * Sort the list of artifact versions by the version id.
	 * 
	 * @param versions
	 *            The list of document versions.
	 */
	public static void sortByVersionId(
			final Collection<DocumentVersion> versions) {
		sortVersions(versions, new VersionIdComparator(Boolean.TRUE));
	}

	/**
	 * Sort the collection of artifacts.
	 * 
	 * @param documents
	 *            A collection of documents.
	 * @param comparator
	 *            The artifact comparator.
	 */
	private static void sortDocuments(final Collection<Document> documents,
			final Comparator<ParityObject> comparator) {
		synchronized(LOCK) {
			final List<Document> l = new LinkedList<Document>();
			l.addAll(documents);
			Collections.sort(l, comparator);
			documents.clear();
			documents.addAll(l);
		}
	}

	/**
	 * Sort the collection of document versions.
	 * 
	 * @param documents
	 *            A collection of documents.
	 * @param comparator
	 *            The artifact comparator.
	 */
	private static void sortVersions(final Collection<DocumentVersion> versions,
			final Comparator<ParityObjectVersion> comparator) {
		synchronized(LOCK) {
			final List<DocumentVersion> l = new LinkedList<DocumentVersion>();
			l.addAll(versions);
			Collections.sort(l, comparator);
			versions.clear();
			versions.addAll(l);
		}
	}

	/**
	 * Create a ArtifactSorter [Singleton]
	 * 
	 */
	private ArtifactSorter() { super(); }

}
