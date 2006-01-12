/*
 * Nov 9, 2005
 */
package com.thinkparity.browser.model.util;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.browser.log4j.BrowserLoggerFactory;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * The parity object helper provides common utility functionality for the user
 * interface based upon parity objects.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityObjectUtil {

	/**
	 * Singleton instance of the parity object util.
	 */
	private static final ParityObjectUtil singleton;

	static {
		singleton = new ParityObjectUtil();
	}

	/**
	 * Obtain the document's file name extension.
	 * 
	 * @param document
	 *            The document.
	 * @return The document's file name extension (doc)
	 */
	public static String getNameExtension(final Document document) {
		return singleton.doGetNameExtension(document);
	}

	/**
	 * Obtain the path for the parity object.
	 * 
	 * @param parityObject
	 *            The parity object.
	 * @return A path for display in the user interface.
	 */
	public static String getPath(final ParityObject parityObject) {
		return singleton.doGetPath(parityObject);
	}

	/**
	 * Determine whether or not the parity object has been seen.
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 * @param artifactType
	 *            The artifact type
	 * @return True if it has been seen; false otherwise.
	 * @throws ParityException
	 */
	public static Boolean hasBeenSeen(final UUID artifactId,
			final ParityObjectType artifactType) throws ParityException {
		return singleton.hasBeenSeenImpl(artifactId, artifactType);
	}

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		BrowserLoggerFactory.getLogger(getClass());

	/**
	 * Parity document api.
	 */
	private final DocumentModel documentModel;

	/**
	 * Parity project api.
	 */
	private final ProjectModel projectModel;

	/**
	 * Create a ParityObjectUtil.
	 */
	private ParityObjectUtil() {
		super();
		this.documentModel = DocumentModel.getModel();
		this.projectModel = ProjectModel.getModel();
	}

	/**
	 * Obtain the document's file name extension.
	 * 
	 * @param document
	 *            The document.
	 * @return The document's file name extension:  .doc
	 */
	private String doGetNameExtension(final Document document) {
		final String name = document.getName();
		return FileUtil.getExtension(name);
	}

	/**
	 * Obtain the path for the parity object.
	 * 
	 * @param parityObject
	 *            The parity object.
	 * @return A path for display in the user interface.
	 */
	private String doGetPath(final ParityObject p) {
		if(p.isSetParentId()) {
			final StringBuffer path = new StringBuffer();
			try { path.append(doGetPath(getProject(p.getParentId()))); }
			catch(ParityException px) {
				logger.warn("getPathImpl(ParityObject)", px);
			}
			return path.append("/")
				.append(p.getCustomName()).toString();
		}
		else { return p.getCustomName(); }
	}

	/**
	 * Obtain a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The document.
	 * @throws ParityException
	 */
	private Document getDocument(final UUID documentId) throws ParityException {
		return documentModel.get(documentId);
	}

	/**
	 * Obtain a project for a given id.
	 * 
	 * @param projectId
	 *            The project id.
	 * @return The project.
	 * @throws ParityException
	 */
	private Project getProject(final UUID projectId) throws ParityException {
		return projectModel.get(projectId);
	}

	/**
	 * Determine whether or not the parity object has been seen.
	 * 
	 * @param parityObject
	 *            The parity object.
	 * @return True if it has been seen by this user; false otherwise.
	 */
	private Boolean hasBeenSeenImpl(final UUID artifactId,
			final ParityObjectType artifactType) throws ParityException {
		final ParityObject artifact;
		switch(artifactType) {
		case DOCUMENT:
			artifact = getDocument(artifactId);
			break;
		case PROJECT:
			artifact = getProject(artifactId);
			break;
		default:
			throw Assert.createUnreachable(
					"hasBeenSeenImpl(UUID,ParityObjectType)");
		}
		return artifact.contains(ParityObjectFlag.SEEN);
	}
}
