/*
 * Nov 9, 2005
 */
package com.thinkparity.browser.model.util;

import org.apache.log4j.Logger;

import com.thinkparity.browser.model.ModelFactory;
import com.thinkparity.browser.platform.util.log4j.LoggerFactory;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.artifact.ArtifactType;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * The parity object helper provides common utility functionality for the user
 * interface based upon parity objects.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactUtil {

	/**
	 * Singleton instance.
	 * 
	 */
	private static final ArtifactUtil singleton;

	/**
	 * Singleton synchronization lock.
	 * 
	 */
	private static final Object singletonLock;

	static {
		singleton = new ArtifactUtil();
		singletonLock = new Object();
	}

	/**
	 * Determine if the artifact can be closed.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the artifact can be closed; false otherwise.
	 */
	public static Boolean canClose(final Long artifactId) {
		synchronized(singletonLock) {
			return singleton.determineCanClose(artifactId);
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
	public static Artifact getArtifact(final Long artifactId,
			final ArtifactType artifactType) {
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
	 * Determine whether or not the parity object has been seen.
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 * @param artifactType
	 *            The artifact type
	 * @return True if it has been seen; false otherwise.
	 */
	public static Boolean hasBeenSeen(final Long artifactId,
			final ArtifactType artifactType) {
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
	public static Boolean isClosed(final Long artifactId,
			final ArtifactType artifactType) {
		synchronized(singletonLock) {
			return singleton.determineIsClosed(artifactId, artifactType);
		}
	}

	/**
	 * Determine whether or not the current user is the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the current user is the key holder; false otherwise.
	 */
	public static Boolean isKeyHolder(final Long artifactId) {
		synchronized(singletonLock) {
			return singleton.determineIsKeyHolder(artifactId);
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
	 * The parity session api.
	 * 
	 */
	private final SessionModel sessionModel;

	/**
	 * Create a ArtifactUtil.
	 * 
	 */
	private ArtifactUtil() {
		super();
		this.documentModel = modelFactory.getDocumentModel(getClass());
		this.sessionModel = modelFactory.getSessionModel(getClass());
	}

	/**
	 * Determine whether or not the artifact can be closed.
	 * 
	 * @param artifactId
	 *            The parity artifact id.
	 * @return True if the artifact can be closed by this user; false otherwise.
	 */
	private Boolean determineCanClose(final Long artifactId) {
		return determineIsKeyHolder(artifactId);
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
	private Boolean determineIsClosed(final Long artifactId,
			final ArtifactType artifactType) {
		return getArtifact(artifactId, artifactType).getState() == ArtifactState.CLOSED;
	}

	/**
	 * Determine whether or not the current user is the artifact key holder.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the current user is the key holder; false otherwise.
	 */
	private Boolean determineIsKeyHolder(final Long artifactId) {
		try { return sessionModel.isLoggedInUserKeyHolder(artifactId); }
		catch(final ParityException px) { throw new RuntimeException(px); }
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
	private Boolean doesArtifactContainFlag(final Artifact artifact,
			final ArtifactFlag flag) {
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
	private Artifact doGetArtifact(final Long artifactId,
			final ArtifactType artifactType) {
		switch(artifactType) {
		case DOCUMENT:
			return getDocument(artifactId);
		default:
			throw Assert.createUnreachable("getArtifact(UUID,ArtifactType)");
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
	 * Obtain a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The document.
	 */
	private Document getDocument(final Long documentId) {
		try { return documentModel.get(documentId); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
	 * Determine whether or not the artifact been seen.
	 * 
	 * @param parityObject
	 *            The parity artifact.
	 * @return True if it has been seen by this user; false otherwise.
	 */
	private Boolean hasBeenSeenImpl(final Long artifactId,
			final ArtifactType artifactType) {
		return doesArtifactContainFlag(
				doGetArtifact(artifactId, artifactType), ArtifactFlag.SEEN);
	}
}
