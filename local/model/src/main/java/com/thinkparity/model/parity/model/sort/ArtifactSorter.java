/*
 * Jan 22, 2006
 */
package com.thinkparity.model.parity.model.sort;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactVersion;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSorter {

	/**
	 * Sort the collection of artifacts.
	 * 
	 * @param documents
	 *            A collection of documents.
	 * @param comparator
	 *            The artifact comparator.
	 * 
	 * @see ComparatorBuilder
	 */
	public static void sortDocuments(final Collection<Document> documents,
			final Comparator<Artifact> comparator) {
		final List<Document> l = new LinkedList<Document>();
		l.addAll(documents);
		Collections.sort(l, comparator);
		documents.clear();
		documents.addAll(l);
	}

	/**
	 * Sort the collection of document versions.
	 * 
	 * @param documents
	 *            A collection of documents.
	 * @param comparator
	 *            The artifact version comparator.
	 * 
	 * @see ComparatorBuilder
	 */
	public static void sortVersions(final Collection<DocumentVersion> versions,
			final Comparator<ArtifactVersion> comparator) {
		final List<DocumentVersion> l = new LinkedList<DocumentVersion>();
		l.addAll(versions);
		Collections.sort(l, comparator);
		versions.clear();
		versions.addAll(l);
	}

	/**
	 * Create a ArtifactSorter [Singleton]
	 * 
	 */
	private ArtifactSorter() { super(); }

}
