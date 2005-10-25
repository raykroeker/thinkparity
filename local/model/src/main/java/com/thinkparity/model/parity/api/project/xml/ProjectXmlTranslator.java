/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.api.project.xml;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityXmlTranslator;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.xml.XmlTranslator;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * ProjectXmlTranslator
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class ProjectXmlTranslator extends ParityXmlTranslator implements XmlTranslator {

	/**
	 * Document model api.
	 */
	private final DocumentModel documentModel;

	/**
	 * Project model api.
	 */
	private final ProjectModel projectModel;

	/**
	 * Create a ProjectXmlTranslator
	 */
	public ProjectXmlTranslator() {
		super("project", Project.class);
		this.documentModel = DocumentModel.getModel();
		this.projectModel = ProjectModel.getModel();
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) { return type.equals(Project.class); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		final Project project = (Project) source;
		writeName(project.getName(), writer);
		writeCreatedBy(project.getCreatedBy(), writer);
		writeId(project.getId(), writer);
		writeCreatedOn(project.getCreatedOn(), writer);
		writeDescription(project.getDescription(), writer);
		writeProjectDirectory(project.getDirectory(), writer);
		writeChildProjects(project.getProjects(), writer);
		writeChildDocuments(project.getDocuments().iterator(), writer);
		writeCustomProperties(project, writer);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Project project = null;
		try {
			final String name = readName(reader);
			final String createdBy = readCreatedBy(reader);
			final String keyHolder = readKeyHolder(reader);
			final UUID id = readId(reader);
			final Calendar createdOn = readCreatedOn(reader);
			final String description = readDescription(reader);
			final File directory = readDirectory(reader);
			project = new Project(name, createdOn, createdBy, keyHolder,
					description, directory, id);
		}
		catch(Exception x) { fatal(project, "An unknown error occured parsing project xml.", x); }

		try { project.setProjects(readChildProjects(reader)); }
		catch(Exception x) { fatal(project, "An unknown error occured parsing child projects' xml.", x); }

		try { project.setDocuments(readChildDocuments(reader, project)); }
		catch(Exception x) { fatal(project, "An unknown error occured parsing child documents' xml.", x); }

		try { readCustomProperties(project, reader); }
		catch(Exception x) { fatal(project, "An unknown error occured parsing project custom xml.", x); }

		return project;
	}

	private Collection<Document> readChildDocuments(
			final HierarchicalStreamReader reader, final Project project)
			throws ParityException {
		final Collection<Document> childDocuments = new Vector<Document>(10);
		reader.moveDown();
		Document childDocument;
		while(reader.hasMoreChildren()) {
			reader.moveDown();
			childDocument =
				documentModel.getDocument(new File(reader.getAttribute("meta-data")));
			childDocument.setProject(project);
			childDocuments.add(childDocument);
			reader.moveUp();
		}
		reader.moveUp();
		return childDocuments;
	}

	private Collection<Project> readChildProjects(
			final HierarchicalStreamReader reader) throws ParityException {
		final Collection<Project> childProjects = new Vector<Project>(10);
		reader.moveDown();
		while(reader.hasMoreChildren()) {
			reader.moveDown();
			childProjects.add(projectModel.getProject(new File(reader
					.getAttribute("meta-data"))));
			reader.moveUp();
		}
		reader.moveUp();
		return childProjects;
	}

	private void writeChildDocuments(final Iterator<Document> iChildDocuments,
			final HierarchicalStreamWriter writer) {
		writer.startNode("documents");
		Document childDocument;
		while(iChildDocuments.hasNext()) {
			childDocument = iChildDocuments.next();
			writer.startNode("document");
			writer.addAttribute("meta-data", childDocument.getMetaDataFile()
					.getAbsolutePath());
			writer.endNode();
		}
		writer.endNode();
	}

	private void writeChildProjects(final Collection<Project> subProjects,
			final HierarchicalStreamWriter writer) {
		writer.startNode("projects");
		for(Project subProject : subProjects) {
			writer.startNode("project");
			writer.addAttribute("meta-data",
					subProject.getMetaDataFile().getAbsolutePath());
			writer.endNode();
		}
		writer.endNode();
	}

	private void writeProjectDirectory(final File projectDirectory,
			final HierarchicalStreamWriter writer) {
		writer.startNode("directory");
		writer.setValue(projectDirectory.getAbsolutePath());
		writer.endNode();
	}
}
