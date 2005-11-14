/*
 * Nov 8, 2005
 */
package com.thinkparity.model.parity.model.io.xml.index;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.io.xml.XmlIO;
import com.thinkparity.model.parity.model.io.xml.document.DocumentXmlIO;
import com.thinkparity.model.parity.model.io.xml.project.ProjectXmlIO;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IndexXmlIO extends XmlIO {

	/**
	 * Document xml interface.
	 */
	private final DocumentXmlIO documentXmlIO;

	/**
	 * Project xml interface.
	 */
	private final ProjectXmlIO projectXmlIO;

	/**
	 * Create a IndexXmlIO.
	 * 
	 * @param workspace
	 *            The workspace to work within.
	 */
	public IndexXmlIO(Workspace workspace) {
		super(workspace);
		this.documentXmlIO = new DocumentXmlIO(workspace);
		this.projectXmlIO = new ProjectXmlIO(workspace);
	}

	/**
	 * Obtain the xml index.
	 * 
	 * @return The xml index.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Index get() throws FileNotFoundException, IOException {
		logger.info("get()");
		final File xmlFile = getIndexXmlFile();
		if(xmlFile.exists()) { return readIndex(xmlFile); }
		else {
			final Index index = lazyCreate();
			write(index, xmlFile);
			return index;
		}
	}

	/**
	 * Create the index. This will obtain the root project(s) and add them to
	 * the index. It will also recursively add the entire project tree to the
	 * index.
	 * 
	 * @return The new index.
	 */
	private Index lazyCreate() throws FileNotFoundException, IOException {
		final Index index = new Index();
		for(Project project : projectXmlIO.list()) {
			lazyCreate(index, project);
		}
		return index;
	}

	/**
	 * Create an index entry for a document.
	 * 
	 * @param index
	 *            The index to add the entry to.
	 * @param document
	 *            The document to add to the index.
	 */
	private void lazyCreate(final Index index, final Document document)
			throws FileNotFoundException, IOException {
		index.addXmlFileLookup(document.getId(), getXmlFile(document));
	}

	/**
	 * Create an index entry for a project. This will recursively add all
	 * projects\documents to the index.
	 * 
	 * @param index
	 *            The index to add the entry to.
	 * @param project
	 *            The project to add to the index.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void lazyCreate(final Index index, final Project project)
			throws FileNotFoundException, IOException {
		for(Project childProject : projectXmlIO.list(project)) {
			lazyCreate(index, childProject);
		}
		for(Document childDocument : documentXmlIO.list(project)) {
			lazyCreate(index, childDocument);
		}
		index.addXmlFileLookup(project.getId(), getXmlFile(project));
	}
}
