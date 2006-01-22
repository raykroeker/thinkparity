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
import com.thinkparity.model.parity.model.project.Project;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSorter {

	/**
	 * Sort the list of documents by name.
	 * 
	 * @param documents
	 *            The list of documents.
	 */
	public static void sortDocumentsByName(final Collection<Document> documents) {
		sortDocuments(documents, new NameComparator(Boolean.TRUE));
	}

	/**
	 * Sort the list of artifact versions by the version id.
	 * 
	 * @param versions
	 *            The list of document versions.
	 */
	public static void sortDocumentVersionsByVersionId(
			final Collection<DocumentVersion> versions) {
		sortVersions(versions, new VersionIdComparator(Boolean.TRUE));
	}

	/**
	 * Sort the list of projects by name.
	 * 
	 * @param projects
	 *            The list of projects.
	 */
	public static void sortProjectsByName(final Collection<Project> projects) {
		sortProjects(projects, new NameComparator(Boolean.TRUE));
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
		final List<Document> l = new LinkedList<Document>();
		l.addAll(documents);
		Collections.sort(l, comparator);
		documents.clear();
		documents.addAll(l);
	}

	/**
	 * Sort the collection of projects.
	 * 
	 * @param projects
	 *            A collection of projects.
	 * @param comparator
	 *            The artifact comparator.
	 */
	private static void sortProjects(final Collection<Project> projects,
			final Comparator<ParityObject> comparator) {
		final List<Project> l = new LinkedList<Project>();
		l.addAll(projects);
		Collections.sort(l, comparator);
		projects.clear();
		projects.addAll(l);
	}

	/**
	 * Sort the collection of document versions.
	 * 
	 * @param documents
	 *            A collection of documents.
	 * @param comparator
	 *            The artifact version comparator.
	 */
	private static void sortVersions(final Collection<DocumentVersion> versions,
			final Comparator<ParityObjectVersion> comparator) {
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
