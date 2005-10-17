/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity.model.project;

import java.io.File;
import java.util.*;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.api.document.DocumentComparator;
import com.thinkparity.model.parity.model.document.Document;

/**
 * Project
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public class Project extends ParityObject {

	/**
	 * Project directory.
	 */
	private File directory;

	/**
	 * List of documents in this project.
	 */
	private Collection<Document> documents;

	/**
	 * List of child projects in this project.
	 */
	private Collection<Project> projects;

	/**
	 * Create a Project
	 */
	public Project(final Project parent, final String name, final Calendar createdOn,
			final String createdBy, final String keyHolder,
			final String description, final File directory, final UUID id) {
		super(parent, name, description, createdOn, createdBy, keyHolder, id);
		this.directory = directory;
		// sub-projects documents and contacts are set in the project
		// via the addXXX\setXXX apis.
		this.projects = new Vector<Project>(0);
		this.documents = new Vector<Document>(0);
	}

	/**
	 * Create a Project
	 */
	public Project(final String name, final Calendar createdOn,
			final String createdBy, final String keyHolder,
			final String description, final File directory, final UUID id) {
		super(null, name, description, createdOn, createdBy, keyHolder, id);
		this.directory = directory;
		// sub-projects documents and contacts are set in the project
		// via the addXXX\setXXX apis.
		this.projects = new Vector<Project>(0);
		this.documents = new Vector<Document>(0);
	}

	/**
	 * Add a document to the project.
	 * 
	 * @param document
	 *            <code>com.thinkparity.model.parity.api.document.Document</code>
	 */
	public void addDocument(final Document document) {
		documents.add(document);
	}

	/**
	 * Add a sub-project to the project.
	 * 
	 * @param project
	 *            <code>com.thinkparity.model.parity.api.project.Project</code>
	 */
	public void addProject(final Project project) {
		projects.add(project);
	}

	/**
	 * @see com.thinkparity.model.parity.api.ParityObject#getDirectory()
	 */
	public File getDirectory() { return directory; }

	/**
	 * Obtain a list of documents attached to this project. Note that the
	 * iterator returned is disconnected from the underlying list of documents,
	 * and removing elements from it will not affect the list.
	 * 
	 * @return <code>java.util.Iterator&lt;com.thinkparity.model.parity.api.document.Document&gt;</code>
	 */
	public Collection<Document> getDocuments() {
		final Collection<Document> newDocuments = new Vector<Document>(
				documents.size());
		newDocuments.addAll(documents);
		return newDocuments;
	}

	public Collection<Document> getDocuments(
			final DocumentComparator documentComparator) {
		final List<Document> sortedDocuments = new Vector<Document>(documents
				.size());
		sortedDocuments.addAll(documents);
		Collections.sort(sortedDocuments, documentComparator);
		return sortedDocuments;
	}

	/**
	 * Obtain a list of sub-projects attached to this project. Note that this
	 * iterator is disconnected from the underlying list of projects, and
	 * removing elements from it will have not affect on the list.
	 * 
	 * @return <code>java.util.Iterator&lt;com.thinkparity.model.parity.api.project.Project&gt;</code>
	 */
	public Iterator<Project> getProjects() {
		final Collection<Project> newProjects = new Vector<Project>(projects
				.size());
		newProjects.addAll(projects);
		return newProjects.iterator();
	}

	public Collection<ParityObject> getChildren() {
		final Vector<ParityObject> children =
			new Vector<ParityObject>(getChildCount());
		children.addAll(projects);
		children.addAll(documents);
		return children;
	}

	private Integer getChildCount() {
		return projects.size() + documents.size();
	}

	/**
	 * Remove a document from the project.
	 * 
	 * @param document
	 *            <code>com.thinkparity.model.parity.api.document.Document</code>
	 */
	public void removeDocument(final Document document) {
		documents.remove(document);
	}

	/**
	 * Set the documents for this project.
	 * 
	 * @param documents
	 *            <code>java.util.Collection&lt;com.thinkparity.model.parity.api.document.Document&gt;</code>
	 */
	public void setDocuments(final Collection<Document> documents) {
		this.documents.clear();
		this.documents.addAll(documents);
	}

	/**
	 * Set the sub-projets for this project.
	 * 
	 * @param projects
	 *            <code>java.util.Collection&lg;com.thinkparity.model.parity.api.project.Project&gt;</code>
	 */
	public void setProjects(final Collection<Project> projects) {
		this.projects.clear();
		this.projects.addAll(projects);
	}

	/**
	 * @see com.thinkparity.model.parity.api.ParityObject#getType()
	 */
	public ParityObjectType getType() { return ParityObjectType.PROJECT; }

	public Boolean hasChildren() {
		return (0 < projects.size() || 0 < documents.size());
	}

	/**
	 * @see com.thinkparity.model.parity.api.ParityObject#getPath()
	 */
	@Override
	public StringBuffer getPath() {
		if(isSetParent()) {
			return new StringBuffer(getParent().getPath())
				.append("/")
				.append(getName());
		}
		else { return new StringBuffer(getName()); }
	}
}
