/*
 * Nov 9, 2005
 */
package com.thinkparity.browser.model.util;

import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

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
	 * Singleton instance.
	 * 
	 */
	private static final ParityObjectUtil singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new ParityObjectUtil();
		singletonLock = new Object();
	}

	/**
	 * Determine if the artifact can be closed.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return True if the artifact can be closed; false otherwise.
	 */
	public static Boolean canClose(final UUID artifactId,
			final ParityObjectType artifactType) {
		synchronized(singletonLock) {
			return singleton.determineCanClose(artifactId, artifactType);
		}
	}

	/**
	 * Determine whether or not the user can delete this artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return True if the user can be deleted; false otherwise.
	 */
	public static Boolean canDelete(final UUID artifactId,
			final ParityObjectType artifactType) {
		synchronized(singletonLock) {
			return singleton.determineCanDelete(artifactId, artifactType);
		}
	}

	/**
	 * Obtain a parity artifact for a given id and type.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return The parity artifact.
	 */
	public static ParityObject getArtifact(final UUID artifactId,
			final ParityObjectType artifactType) {
		synchronized(singletonLock) {
			return singleton.doGetArtifact(artifactId, artifactType);
		}
	}

	/**
	 * Obtain the document's file NAME extension.
	 * 
	 * @param document
	 *            The document.
	 * @return The document's file NAME extension (doc)
	 */
	public static String getNameExtension(final Document document) {
		synchronized(singletonLock) {
			return singleton.doGetNameExtension(document);
		}
	}

	/**
	 * Obtain the path for the parity object.
	 * 
	 * @param parityObject
	 *            The parity object.
	 * @return A path for display in the user interface.
	 */
	public static String getPath(final ParityObject parityObject) {
		synchronized(singletonLock) {
			return singleton.doGetPath(parityObject);
		}
	}

	/**
	 * Determine whether or not the parity object has been seen.
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 * @param artifactType
	 *            The artifact type
	 * @return True if it has been seen; false otherwise.
	 */
	public static Boolean hasBeenSeen(final UUID artifactId,
			final ParityObjectType artifactType) {
		synchronized(singletonLock) {
			return singleton.hasBeenSeenImpl(artifactId, artifactType);
		}
	}

	/**
	 * Determine whether or not the parity artifact has been closed.
	 * 
	 * @param artifactId
	 *            the artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return True if the artifact has been closed; false otherwise.
	 */
	public static Boolean isClosed(final UUID artifactId,
			final ParityObjectType artifactType) {
		synchronized(singletonLock) {
			return singleton.determineIsClosed(artifactId, artifactType);
		}
	}

	/**
	 * Determine whether or not the current user is the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return True if the current user is the key holder; false otherwise.
	 */
	public static Boolean isKeyHolder(final UUID artifactId,
			final ParityObjectType artifactType) {
		synchronized(singletonLock) {
			return singleton.determineIsKeyHolder(artifactId, artifactType);
		}
	}

	/**
	 * Handle to an apache logger.
	 */
	protected final Logger logger =
		LoggerFactory.getLogger(getClass());

	/**
	 * Parity document api.
	 */
	private final DocumentModel documentModel;

	/**
	 * Handle to the model factory.
	 * 
	 */
	private final ModelFactory modelFactory = ModelFactory.getInstance();

	/**
	 * Parity project api.
	 */
	private final ProjectModel projectModel;

	/**
	 * Create a ParityObjectUtil.
	 * 
	 */
	private ParityObjectUtil() {
		super();
		this.documentModel = modelFactory.getDocumentModel(getClass());
		this.projectModel = modelFactory.getProjectModel(getClass());
	}

	/**
	 * Determine whether or not the artifact can be closed.
	 * 
	 * @param artifactId
	 *            The parity artifact id.
	 * @param artifactType
	 *            The parity artifact type.
	 * @return True if the artifact can be closed by this user; false otherwise.
	 */
	private Boolean determineCanClose(final UUID artifactId,
			final ParityObjectType artifactType) {
		return doesArtifactContainFlag(
				doGetArtifact(artifactId, artifactType), ParityObjectFlag.KEY);
	}

	/**
	 * Determine whether or not the user can delete this artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return True if the user can be deleted; false otherwise.
	 */
	private Boolean determineCanDelete(final UUID artifactId,
			final ParityObjectType artifactType) {
		return doesArtifactContainFlag(
				doGetArtifact(artifactId, artifactType), ParityObjectFlag.CLOSED);
	}

	/**
	 * Determine whether or not the parity artifact has been closed.
	 * 
	 * @param artifactId
	 *            the artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return True if the artifact has been closed; false otherwise.
	 */
	private Boolean determineIsClosed(final UUID artifactId,
			final ParityObjectType artifactType) {
		return doesArtifactContainFlag(
				doGetArtifact(artifactId, artifactType), ParityObjectFlag.CLOSED);
	}

	/**
	 * Determine whether or not the current user is the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return True if the current user is the key holder; false otherwise.
	 */
	private Boolean determineIsKeyHolder(final UUID artifactId,
			final ParityObjectType artifactType) {
		return doesArtifactContainFlag(
				doGetArtifact(artifactId, artifactType), ParityObjectFlag.KEY);
	}

	/**
	 * Determine whether or not the artifact contains the flag.
	 * 
	 * @param artifact
	 *            The artifact.
	 * @param flag
	 *            The flag.
	 * @return True; if the artifact contains the flag; false otherwise.
	 */
	private Boolean doesArtifactContainFlag(final ParityObject artifact,
			final ParityObjectFlag flag) {
		return artifact.contains(flag);
	}

	/**
	 * Obtain the parity artifact for a given type\id.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param artifactType
	 *            The artifact type.
	 * @return The artifact.
	 */
	private ParityObject doGetArtifact(final UUID artifactId,
			final ParityObjectType artifactType) {
		switch(artifactType) {
		case DOCUMENT:
			return getDocument(artifactId);
		case PROJECT:
			return getProject(artifactId);
		default:
			throw Assert.createUnreachable("getArtifact(UUID,ParityObjectType)");
		}	
	}

	/**
	 * Obtain the document's file NAME extension.
	 * 
	 * @param document
	 *            The document.
	 * @return The document's file NAME extension:  .doc
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
			return path.append(doGetPath(getProject(p.getParentId())))
				.append("/")
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
	 */
	private Document getDocument(final UUID documentId) {
		try { return documentModel.get(documentId); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
	 * Obtain a project for a given id.
	 * 
	 * @param projectId
	 *            The project id.
	 * @return The project.
	 */
	private Project getProject(final UUID projectId) {
		try { return projectModel.get(projectId); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
	 * Determine whether or not the artifact been seen.
	 * 
	 * @param parityObject
	 *            The parity artifact.
	 * @return True if it has been seen by this user; false otherwise.
	 */
	private Boolean hasBeenSeenImpl(final UUID artifactId,
			final ParityObjectType artifactType) {
		return doesArtifactContainFlag(
				doGetArtifact(artifactId, artifactType), ParityObjectFlag.SEEN);
	}
}
