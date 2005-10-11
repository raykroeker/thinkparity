/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Loggable;
import com.thinkparity.codebase.log4j.LoggerFormatter;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.api.document.xml.DocumentXml;
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.api.events.VersionCreationEvent;
import com.thinkparity.model.parity.api.project.xml.ProjectXml;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.MD5Util;
import com.thinkparity.model.parity.util.ParityUtil;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.user.User;

/**
 * DocumentModelImpl
 * @author raykroeker@gmail.com
 * @version 1.2
 */
class DocumentModelImpl extends AbstractModelImpl {

	/**
	 * DocumentException
	 * Is used as an error indicator within the document api implementation.
	 * This exception should never escape the impl as a DocumentException, but
	 * rather as a ParityException.
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class DocumentException extends Exception {
		private static final long serialVersionUID = -1;
		/**
		 * Create a DocumentException
		 * @param message <code>java.lang.String</code>
		 */
		private DocumentException(final String message) { super(message); }
		/**
		 * Create a DocumentException
		 * @param cause <code>java.lang.Throwable</code>
		 */
		private DocumentException(final Throwable cause) { super(cause); }
	}

	/**
	 * List of listeners to notify when a document is created or received.
	 * @see DocumentModelImpl#creationListenersLock
	 */
	private static final Collection<CreationListener> creationListeners;

	/**
	 * Synchronization lock for the creation listeners.
	 * @see DocumentModelImpl#creationListeners
	 */
	private static final Object creationListenersLock;

	/**
	 * Handle to a class used to format various classes.
	 */
	private static final LoggerFormatter loggerFormatter = new LoggerFormatter();

	/**
	 * List of listeners to notify when a document has been updated.
	 * @see DocumentModelImpl#updateListenersLock
	 */
	private static final Collection<UpdateListener> updateListeners;

	/**
	 * Synchronization lock for the update listeners.
	 * @see DocumentModelImpl#updateListeners
	 */
	private static final Object updateListenersLock;

	static {
		creationListeners = new Vector<CreationListener>(7);
		creationListenersLock = new Object();

		updateListeners = new Vector<UpdateListener>(7);
		updateListenersLock = new Object();
	}

	/**
	 * Handle to the session model.
	 */
	private final SessionModel sessionModel;

	/**
	 * Create a DocumentModelImpl
	 * 
	 * @param workspace
	 *            The workspace to work within.
	 */
	DocumentModelImpl(final Workspace workspace) {
		super(workspace);
		this.sessionModel = SessionModel.getModel();
	}

	/**
	 * @see DocumentModel#addCreationListener(CreationListener)
	 * @param creationListener
	 */
	void addCreationListener(final CreationListener creationListener) {
		Assert.assertNotNull("Null creation listener.", creationListener);
		synchronized(DocumentModelImpl.creationListenersLock) {
			Assert.assertNotTrue("Creation listener already registered.",
					DocumentModelImpl.creationListeners.contains(creationListener));
			DocumentModelImpl.creationListeners.add(creationListener);
		}
	}

	void addNote(final Document document, final Note note)
			throws ParityException {
		debug("documentapiimpl.addnote:document", document);
		debug("documentapiimpl.addnote:note", note);
		try {
			document.add(note);
			serialize(document);
			notifyUpdate_objectUpdated(document);
		}
		catch(DocumentException dx) { throw new ParityException(dx); }
	}

	/**
	 * @see DocumentModel#addUpdateListener(UpdateListener)
	 * @param updateListener
	 *            The update listener to add.
	 */
	void addUpdateListener(final UpdateListener updateListener) {
		Assert.assertNotNull("Null update listener.", updateListener);
		synchronized(DocumentModelImpl.updateListenersLock) {
			Assert.assertNotTrue("Update listener already registered.",
					DocumentModelImpl.updateListeners.contains(updateListener));
			DocumentModelImpl.updateListeners.add(updateListener);
		}
	}

	Document createDocument(final Project project, final String name,
			final String description, final File documentFile)
			throws ParityException {
		debug("DocumentModelImpl.createDocument():project", project);
		debug("DocumentModelImpl.createDocument():name", name);
		debug("DocumentModelImpl.createDocument():description", description);
		debug("DocumentModelImpl.createDocument():documentFile", documentFile);
		try {
			final UUID nextDocumentId = UUIDGenerator.nextUUID();
			debug("DocumentModelImpl.createDocument():nextDocumentId", nextDocumentId);
			final byte[] documentContent = FileUtil.readFile(documentFile);
			final Document document = new Document(project, name, DateUtil
					.getInstance(), preferences.getUsername(), preferences
					.getUsername(), description, project.getDirectory(),
					nextDocumentId, documentContent);
			createDocument(project, document);
			createDocumentVersion(document);
			return document;
		}
		catch(DocumentException dx) { throw new ParityException(dx); }
		catch(IOException iox) { throw new ParityException(iox); }
	}

	/**
	 * Create a new document version based upon an existing document.  This will
	 * check the cache for updates to the document, write the updates to the document,
	 * then create a new version based upon that document.
	 * @param document <code>Document</code>
	 * @return <code>DocumentVersion</code>
	 * @throws DocumentException
	 * @throws IOException
	 * @throws ParityException
	 */
	DocumentVersion createDocumentVersion(final Document document)
			throws ParityException {
		try {
			final File cacheFile = getCacheFile(document);
			if(cacheFile.exists()) {
				final byte[] cacheFileBytes = getCacheFileBytes(document);
				final String cacheFileChecksum = MD5Util.md5Hex(cacheFileBytes);
				if(!cacheFileChecksum.equals(document.getContentChecksum())) {
					document.setContent(cacheFileBytes);
					updateDocument(document);
				}
			}
			final DocumentVersion newDocumentVersion =
					DocumentVersionBuilder.newDocumentVersion(document);
			createDocumentVersion(newDocumentVersion);
			return newDocumentVersion;
		}
		catch(DocumentException dx) { throw new ParityException(dx); }
		catch(IOException iox) { throw new ParityException(iox); }
	}

	/**
	 * Take the given document, and export it to the specified file. This will
	 * obtain the document's content, and save it to the file.
	 * 
	 * @param file
	 *            <code>java.io.File</code>
	 * @param document
	 *            <code>com.thinkparity.model.parity.api.document.Document</code>
	 * @throws ParityException
	 */
	void exportDocument(final File file, final Document document)
			throws ParityException {
		debug("DocumentModelImpl.exportDocument():file", file);
		debug("DocumentModelImpl.exportDocument():document", document);
		exportDocument_CheckRules(file, document);
		createFile(file);
		writeDocumentContent(file, readDocument(document));
	}

	Document getDocument(final File documentMetaDataFile) throws ParityException {
		debug("documentapiimpl.getdocument:documentMetaDataFile", documentMetaDataFile);
		try { return DocumentXml.readXml(documentMetaDataFile); }
		catch(IOException iox) { throw new ParityException(iox); }
	}

	StringBuffer getRelativePath(final Document document) throws ParityException {
		debug("documentapiimpl.getrelativepath:document", document);
		final Project rootProject = ProjectModel.getRootProject(workspace);
		final URI relativeURI = rootProject.getDirectory().toURI()
			.relativize(document.getDirectory().toURI());
		return new StringBuffer(relativeURI.toString());
	}

	Boolean isDocumentClosable(final Document document) throws ParityException {
		debug("documentapiimpl.isdocumentclosable:document", document);
		return Boolean.FALSE;
	}

	Boolean isDocumentDeletable(final Document document) throws ParityException {
		debug("documentapiimpl.isdocumentdeletable:document", document);
		return Boolean.FALSE;
	}

	/**
	 * Determine if a document exists in the disk cache, and open it. If it does
	 * not exists in the cache, create it, and open it.
	 * 
	 * @param document
	 *            <code>Document</code>
	 * @throws ParityException
	 */
	void openDocument(final Document document) throws ParityException {
		debug("DocumentModelImpl.openDocument():document", document);
		try {
			final File documentCacheFile = getFileFromDiskCache(document);
			ParityUtil.launchFileWin32(documentCacheFile.getAbsolutePath());
		}
		catch(IOException iox) { throw new ParityException(iox); }
	}

	/**
	 * @see DocumentModel#receiveDocumentVersion(DocumentVersion)
	 * @param documentVersion
	 *            The document version to receive.
	 * @throws ParityException
	 */
	void receiveDocumentVersion(final DocumentVersion documentVersion)
			throws ParityException {
		debug("DocumenApiImpl.createDocument():documentVersion:",
				documentVersion);
		try {
			// the imported document will reside within the root project
			final Project project = ProjectModel.getRootProject(workspace);

			// create a new instance of a document based upon the imported
			// document with a few modifications
			final Document originalDocument = documentVersion.getDocument();
			final Document newDocument = new Document(originalDocument);
			newDocument.setDirectory(project);

			// create a temporary file using the documentVersion's content
			// final File documentFile = createTempFile(documentVersion);

			// create the document
			createMetaData(newDocument);
			updateProjectMetaData(project, newDocument);
			notifyCreation_objectReceived(newDocument);
		}
		catch(DocumentException dx) { throw new ParityException(dx); }
	}

	/**
	 * @see DocumentModel#removeCreationListener(CreationListener)
	 * @param creationListener
	 */
	void removeCreationListener(final CreationListener creationListener) {
		Assert.assertNotNull("Null creation listener.", creationListener);
		synchronized(DocumentModelImpl.creationListenersLock) {
			Assert.assertTrue("Creation listener not registered.",
					DocumentModelImpl.creationListeners.contains(creationListener));
			DocumentModelImpl.creationListeners.remove(creationListener);
		}
	}

	/**
	 * @see DocumentModel#removeUpdateListener(UpdateListener)
	 * @param updateListener
	 *            The update listener to remove.
	 */
	void removeUpdateListener(final UpdateListener updateListener) {
		Assert.assertNotNull("Null update listener.", updateListener);
		synchronized(DocumentModelImpl.updateListenersLock) {
			Assert.assertTrue("Update listener not registered.",
					DocumentModelImpl.updateListeners.contains(updateListener));
			DocumentModelImpl.updateListeners.remove(updateListener);
		}
	}

	void sendDocument(final Collection<User> users, final Document document)
			throws ParityException {
		trace();
		debug("DocumentModelImpl.sendDocument():users", users);
		debug("DocumentModelImpl.sendDocument():document", document);
		final DocumentVersion newDocumentVersion =
			createDocumentVersion(document);
		for(Iterator<User> i = users.iterator(); i.hasNext();) {
			sendDocument(i.next(), newDocumentVersion);
		}
	}

	void updateDocument(final Document document) throws ParityException {
		debug("DocumentModelImpl.updateDocument():document", document);
		try {
			serialize(document);
			notifyUpdate_objectUpdated(document);
		}
		catch(DocumentException dx) { throw new ParityException(dx); }
	}

	private void createDocument(final Project project, final Document document)
			throws DocumentException {
		createMetaData(document);
		updateProjectMetaData(project, document);
		notifyCreation_objectCreated(document);
	}

	private void createDocumentVersion(final DocumentVersion documentVersion)
			throws DocumentException {
		serialize(documentVersion);
		final Document document = documentVersion.getDocument();
		document.add(documentVersion);
		serialize(document);
		notifyCreation_objectVersionCreated(documentVersion);
	}

	/**
	 * Use the <code>java.io.File</code> API to create a new file on disk.
	 * 
	 * @param file
	 *            <code>java.io.File</code>
	 * @throws ParityException -
	 *             If an IOException is thrown while creating the file, or if
	 *             the creation API does not return true.
	 */
	private void createFile(final File file) throws ParityException {
		try {
			final Boolean didCreateFile = file.createNewFile();
			if(Boolean.TRUE != didCreateFile)
				throw new ParityException("Could not create file:  " + file.getAbsolutePath() + ".");
		}
		catch(IOException iox) {
			logger.error("Could not create file:  " + file.getAbsolutePath() + ".");
			throw new ParityException(iox);
		}
	}

	private void createMetaData(final Document document) throws DocumentException {
		try { DocumentXml.writeCreationXml(document); }
		catch(IOException iox ) { throw new DocumentException(iox); }
	}

	private File createTempFile(final DocumentVersion documentVersion)
			throws DocumentException {
		try {
			final Document document = documentVersion.getDocument();
			final String documentName = document.getName();
			return FileUtil.writeTempFile(ParityUtil
					.getClientName(), documentName, documentVersion
					.getContent());
		}
		catch(IOException iox) { throw new DocumentException(iox); }
	}

	private void debug(final String context, final Collection<User> users) {
		if(null == users) { debug(context, (Loggable) null); }
		else {
			for(Iterator<User> i = users.iterator(); i.hasNext();) {
				debug(context, i.next());
			}
		}
	}

	private void debug(final String context, final File file) {
		logger.debug(loggerFormatter.format(context, file));
	}

	private void debug(final String context, final Loggable loggable) {
		logger.debug(loggerFormatter.format(context, loggable));
	}

	private void debug(final String context, final String string) {
		logger.debug(loggerFormatter.format(context, string));
	}

	private void debug(final String context, final UUID uuid) {
		logger.debug(loggerFormatter.format(context, uuid));
	}

	private void deleteMetaData(final Document document) {
		DocumentXml.deleteXml(document);
	}

	private void exportDocument_CheckRules(final File file,
			final Document document) throws ParityException {
		if(file.exists())
			throw new ParityException("Export file " + file.getAbsolutePath()
					+ " already exists.");
	}

	/**
	 * Obtain the directory used to cache content for the document. If the
	 * directory does not yet exist it is created.
	 * 
	 * @param document
	 *            <code>Document</code>
	 * @return <code>java.io.File</code>
	 */
	private File getCacheDirectory(final Document document) {
		final File cacheDirectory = new File(document.getDirectory(), ".cache");
		if(!cacheDirectory.exists())
			Assert.assertTrue("Could not create cache directory:  "
					+ cacheDirectory.getAbsolutePath(), cacheDirectory.mkdir());
		return cacheDirectory;
	}

	/**
	 * Obtain the file used to store the cached content of the document.
	 * @param document <code>Document</code>
	 * @return <code>java.io.File</code>
	 */
	private File getCacheFile(final Document document) {
		final File cacheDirectory = getCacheDirectory(document);
		return new File(cacheDirectory, document.getName());
	}

	private byte[] getCacheFileBytes(final Document document)
			throws IOException {
		return FileUtil.readFile(getCacheFile(document));
	}


	/**
	 * Obtain the document's representative file from the underlying disk cache.
	 * 
	 * @param document
	 *            <code>Document</code.
	 * @return <code>java.io.File</code>
	 */
	private File getFileFromDiskCache(final Document document)
			throws IOException {
		final File documentCacheFile = getCacheFile(document);
		if(!documentCacheFile.exists()) {
			// write the cache file
			FileUtil.writeFile(documentCacheFile, document.getContent());
		}
		return documentCacheFile;
	}

	/**
	 * Fire the objectCreated event for all of the creation listeners.
	 * 
	 * @param document
	 *            The document to use as the event source.
	 * @see CreationListener#objectCreated(CreationEvent)
	 */
	private void notifyCreation_objectCreated(final Document document) {
		synchronized(DocumentModelImpl.creationListenersLock) {
			for(CreationListener listener : DocumentModelImpl.creationListeners) {
				listener.objectCreated(new CreationEvent(document));
			}
		}
	}

	/**
	 * Fire the objectReceived event for all creation listeners.
	 * 
	 * @param document
	 *            The document to use as the event source.
	 * @see CreationListener#objectReceived(CreationEvent)
	 */
	private void notifyCreation_objectReceived(final Document document) {
		synchronized(DocumentModelImpl.creationListenersLock) {
			for(CreationListener listener : DocumentModelImpl.creationListeners) {
				listener.objectReceived(new CreationEvent(document));
			}
		}
	}

	/**
	 * Fire the objectVersionCreated event for all of the creation listeners.
	 * 
	 * @param documentVersion
	 *            The document version to use as the event source.
	 * @see CreationListener#objectVersionCreated(VersionCreationEvent)
	 */
	private void notifyCreation_objectVersionCreated(final DocumentVersion documentVersion) {
		synchronized(DocumentModelImpl.creationListenersLock) {
			for(CreationListener listener : DocumentModelImpl.creationListeners) {
				listener.objectVersionCreated(new VersionCreationEvent(documentVersion));
			}
		}
	}

	/**
	 * Fire the objectUpdated event for all of the udpate listeners.
	 * 
	 * @param document
	 *            The document to use as the event source.
	 * @see UpdateListener#objectUpdated(UpdateEvent)
	 */
	private void notifyUpdate_objectUpdated(final Document document) {
		synchronized(DocumentModelImpl.updateListenersLock) {
			for(UpdateListener listener : DocumentModelImpl.updateListeners) {
				listener.objectUpdated(new UpdateEvent(document));
			}
		}
	}

	/**
	 * Read the document's underlying content from the filesystem.
	 * 
	 * @param document
	 *            <code>com.thinkparity.model.parity.api.document.Document</code>
	 * @return <code>java.lang.byte[]</code>
	 * @throws ParityException -
	 *             If the document's underlying file cannot be found, or if the
	 *             document's underlying file cannot be read.
	 */
	private byte[] readDocument(final Document document)
			throws ParityException {
		final File documentFile = new File(document.getDocumentAbsolutePath());
		try { return FileUtil.readFile(documentFile); }
		catch(FileNotFoundException fnfx) {
			logger.error("Could not find document:  " + document.getDocumentAbsolutePath() + ".");
			throw new ParityException(fnfx);
		}
		catch(IOException iox) {
			logger.error("Could not read document:  " + document.getDocumentAbsolutePath() + ".");
			throw new ParityException(iox);
		}
	}

	private void sendDocument(final User user,
			final DocumentVersion documentVersion) throws ParityException {
		sessionModel.send(user, documentVersion);
	}

	/**
	 * Serialize the document to xml.
	 * @param document <code>Document</code>
	 * @throws DocumentException
	 */
	private void serialize(final Document document) throws DocumentException {
		try { DocumentXml.writeUpdateXml(document); }
		catch(IOException iox) {
			logger.error("Could not serialize document xml.", iox);
			throw new DocumentException("Could not serialize document xml.");
		}
	}

	/**
	 * Serialize the document version to xml.
	 * @param documentVersion <code>DocumentVersion</code>
	 * @throws DocumentException
	 */
	private void serialize(final DocumentVersion documentVersion)
			throws DocumentException {
		try { DocumentXml.serializeXml(documentVersion); }
		catch(IOException iox) { throw new DocumentException(iox); }
	}

	/**
	 * Serialize the project to xml.
	 * @param project <code>Project</code>
	 * @throws DocumentException
	 */
	private void serialize(final Project project) throws DocumentException {
		try { ProjectXml.writeUpdateXml(project); }
		catch(IOException iox) {
			logger.error("Could not serialie project xml.", iox);
			throw new DocumentException("Could not serialize project xml.");
		}
	}

	private void trace() { logger.debug("REMEMBER TO INSERT STACK-TRACE"); }

	private void updateProjectMetaData(final Project project,
			final Document document) throws DocumentException {
		project.addDocument(document);
		serialize(project);
	}

	/**
	 * Write the document's content to a file.
	 * @param file <code>java.io.File</code>
	 * @param documentContent <code>java.lang.byte[]</code>
	 * @throws ParityException - If the file cannot be written.
	 */
	private void writeDocumentContent(final File file,
			final byte[] documentContent) throws ParityException {
		try { FileUtil.writeFile(file, documentContent); }
		catch(IOException iox) {
			logger.error("Could not write file:  " + file.getAbsolutePath() + ".");
			throw new ParityException(iox);
		}
	}
}
