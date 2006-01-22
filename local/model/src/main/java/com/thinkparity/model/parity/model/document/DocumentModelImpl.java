/*
 * Mar 6, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.api.events.CreationEvent;
import com.thinkparity.model.parity.api.events.CreationListener;
import com.thinkparity.model.parity.api.events.DeleteEvent;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.api.events.VersionCreationEvent;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.artifact.ArtifactSorter;
import com.thinkparity.model.parity.model.io.xml.document.DocumentXmlIO;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.MD5Util;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.user.User;

/**
 * Implementation of the document model interface.
 * 
 * @author raykroeker@gmail.com
 * @version 1.5.2.43
 */
class DocumentModelImpl extends AbstractModelImpl {

	/**
	 * List of listeners to notify when a document is created or received.
	 * 
	 * @see DocumentModelImpl#creationListenersLock
	 */
	private static final Collection<CreationListener> creationListeners;

	/**
	 * Synchronization lock for the creation listeners.
	 * 
	 * @see DocumentModelImpl#creationListeners
	 */
	private static final Object creationListenersLock;

	/**
	 * List of listeners to notify when a document has been updated.
	 * 
	 * @see DocumentModelImpl#updateListenersLock
	 */
	private static final Collection<UpdateListener> updateListeners;

	/**
	 * Synchronization lock for the update listeners.
	 * 
	 * @see DocumentModelImpl#updateListeners
	 */
	private static final Object updateListenersLock;

	static {
		// initialize the creation event listeners
		creationListeners = new Vector<CreationListener>(7);
		creationListenersLock = new Object();
		// initialize the update event listeners
		updateListeners = new Vector<UpdateListener>(7);
		updateListenersLock = new Object();
	}

	/**
	 * Document xml input\output.
	 */
	private final DocumentXmlIO documentXmlIO;

	/**
	 * Create a DocumentModelImpl
	 * 
	 * @param workspace
	 *            The workspace to work within.
	 */
	DocumentModelImpl(final Workspace workspace) {
		super(workspace);
		this.documentXmlIO = new DocumentXmlIO(workspace);
	}

	/**
	 * @see DocumentModel#addListener(CreationListener)
	 * @param listener
	 *            The listener to add.
	 */
	void addListener(final CreationListener listener) {
		logger.info("addCreationListener(CreationListener)");
		logger.debug(listener);
		Assert.assertNotNull("addCreationListener(CreationListener)", listener);
		synchronized (DocumentModelImpl.creationListenersLock) {
			Assert.assertNotTrue(
					"addCreationListener(CreationListener)",
					DocumentModelImpl.creationListeners.contains(listener));
			DocumentModelImpl.creationListeners.add(listener);
		}
	}

	/**
	 * Add a document update event listener.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	void addListener(final UpdateListener listener) {
		logger.info("addUpdateListener(UpdateListener)");
		logger.debug(listener);
		Assert.assertNotNull("addUpdateListener(UpdateListener)", listener);
		synchronized (DocumentModelImpl.updateListenersLock) {
			Assert.assertNotTrue("Update listener already registered.",
					DocumentModelImpl.updateListeners.contains(listener));
			DocumentModelImpl.updateListeners.add(listener);
		}
	}

	/**
	 * Add a note to a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param note
	 *            The note.
	 * @throws ParityException
	 */
	Note addNote(final UUID documentId, final String note)
			throws ParityException {
		logger.debug(documentId);
		logger.debug(note);
		if(null == note) { throw new NullPointerException(); }
		if(0 == note.trim().length()) { throw new IllegalArgumentException(); }
		try {
			// add a note and update
			final Document document = get(documentId);
			final Note newNote = new Note(note.trim());	// remove leading\trailing
			document.add(newNote);						// whitespace
			documentXmlIO.update(document);

			// fire an update notification
			notifyUpdate_objectUpdated(document);
			return newNote;
		}
		catch(IOException iox) {
			logger.error("addNote(Document,Note)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("addNote(Document,Note)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Import a document into a project. This will take a name, description and
	 * location of a document and copy the document into an internal store for
	 * the project, then returns the newly created document.
	 * 
	 * @param projectId
	 *            The parent project unique id.
	 * @param name
	 *            The name of the document.
	 * @param description
	 *            The description of the document.
	 * @param file
	 *            The file for the document.
	 * @return The created document.
	 * @throws ParityException
	 */
	Document create(final UUID projectId, final String name,
			final String description, final File file) throws ParityException {
		logger.info("create(Project,String,String,File)");
		logger.debug(projectId);
		logger.debug(name);
		logger.debug(description);
		logger.debug(file);
		assertCanCreateArtifacts();
		Assert.assertTrue(
				// TODO Centralize
				"File \"" + file.getAbsolutePath() + "\" does not exist.",
				file.exists());
		Assert.assertTrue(
				"create(Project,String,String,File)",
				(file.length() <= IParityModelConstants.FILE_SIZE_UPPER_BOUNDS));
		try {
			final Calendar now = getTimestamp();
			final Document document = new Document(preferences.getUsername(),
					now, description, NO_FLAGS, UUIDGenerator.nextUUID(), name,
					projectId, preferences.getUsername(), now);
			final byte[] contentBytes = FileUtil.readBytes(file);
			final DocumentContent content = new DocumentContent(
					MD5Util.md5Hex(contentBytes), contentBytes, document.getId());

			// create the local file
			final LocalFile localFile = getLocalFile(document);
			localFile.write(contentBytes);

			// create the document
			documentXmlIO.create(document, content);

			// create a version
			createVersion(
					document.getId(), DocumentAction.CREATE, createActionData(document));

			// flag the document as having been seen.
			flagAsSEEN(document);
			// flag the document with the key
			flagKey(document.getId());

			// fire a creation event
			notifyCreation_objectCreated(document);
			return document;
		}
		catch(IOException iox) {
			logger.error("createDocument(Document)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("createDocument(Document)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Create a new document version based upon an existing document. This will
	 * check the cache for updates to the document, write the updates to the
	 * document, then create a new version based upon that document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param action
	 *            The action causing the version creation.
	 * @param actionData
	 *            The data associated with the version creation action.
	 * @return The newly created version.
	 * @throws ParityException
	 */
	DocumentVersion createVersion(final UUID documentId,
			final DocumentAction action, final DocumentActionData actionData)
			throws ParityException {
		logger.info("createVersion(UUID,DocumentAction,DocumentActionData)");
		logger.debug(documentId);
		logger.debug(action);
		logger.debug(actionData);
		try {
			final Document document = get(documentId);
			// update the document updated by\on
			document.setUpdatedBy(preferences.getUsername());
			document.setUpdatedOn(getTimestamp());
			documentXmlIO.update(document);

			// read the document local file
			final LocalFile documentLocalFile = getLocalFile(document);
			documentLocalFile.read();

			final DocumentContent content = getContent(documentId);
			// update the content bytes\checksum
			content.setContent(documentLocalFile.getFileBytes());
			content.setChecksum(documentLocalFile.getFileChecksum());
			documentXmlIO.update(document, content);

			// create a new version\version content
			final DocumentVersion version = new DocumentVersion(documentId,
					nextVersionId(documentId), document, action, actionData);
			final DocumentVersionContent versionContent =
				new DocumentVersionContent(content, documentId, version.getVersionId());
			documentXmlIO.create(document, version, versionContent);

			// create the version local file
			final LocalFile versionLocalFile = getLocalFile(document, version);
			versionLocalFile.write(content.getContent());
			versionLocalFile.lock();

			// fire the object version event notification
			notifyCreation_objectVersionCreated(version);
			return version;
		}
		catch(IOException iox) {
			logger.error("createVersion(Document,DocumentAction,DocumentActionData)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("createVersion(Document,DocumentAction,DocumentActionData)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Delete a document.
	 * 
	 * @param document
	 *            The document to delete.
	 * @throws ParityException
	 */
	void delete(final Document document) throws ParityException {
		logger.info("delete(Document)");
		logger.debug(document);
		try {
			// flag the document as seen
			flagAsSEEN(document);

			// delete the content
			final DocumentContent content = getContent(document.getId());
			documentXmlIO.deleteContent(document, content);

			// delete the versions
			final Collection<DocumentVersion> versions = listVersions(document.getId());
			for(DocumentVersion version : versions) { deleteVersion(document, version); }

			// delete the document
			final LocalFile localFile = getLocalFile(document);
			localFile.delete();
			localFile.deleteParent();
			documentXmlIO.delete(document);

			// notify
			notifyUpdate_objectDeleted(document);
		}
		catch(IOException iox) {
			logger.error("delete(Document)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("delete(Document)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Take the given document, and export it to the specified file. This will
	 * obtain the document's content, and save it to the file. Note that if file
	 * exists, it will be overwritten.
	 * 
	 * @param document
	 *            The document to export.
	 * @param file
	 *            The file to export the document to.
	 * @throws ParityException
	 */
	void export(final Document document, final File file)
			throws ParityException {
		logger.info("export(Document)");
		logger.debug(document);
		logger.debug(file);
		try {
			Assert.assertNotTrue("File cannot already exist.", file.exists());
			Assert.assertTrue("Could not create new file.", file.createNewFile());
			writeDocumentContent(document.getId(), file);
		}
		catch(IOException iox) {
			logger.error("export(Document,File)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("export(Document,File)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a document with a specified id.
	 * 
	 * @param id
	 *            The id of the document.
	 * @return The document
	 * @throws ParityException
	 */
	Document get(final UUID id) throws ParityException {
		logger.info("get(UUID)");
		logger.debug(id);
		try { return documentXmlIO.get(id); }
		catch(IOException iox) {
			logger.error("get(UUID)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("get(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain the document content for a given document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The document's content.
	 * @throws ParityException
	 */
	DocumentContent getContent(final UUID documentId) throws ParityException {
		logger.info("getContent(UUID)");
		logger.debug(documentId);
		try {
			final Document document = get(documentId);
			return documentXmlIO.getContent(document);
		}
		catch(IOException iox) {
			logger.error("getContent(Document)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("getContent(Document)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a document version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The document version.
	 * @throws ParityException
	 */
	DocumentVersion getVersion(final UUID documentId, final String versionId)
			throws ParityException {
		logger.info("getVersion(UUID,String)");
		logger.debug(documentId);
		logger.debug(versionId);
		try { return documentXmlIO.getVersion(get(documentId), versionId); }
		catch(IOException iox) {
			logger.error("getVersion(UUID,String)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("getVersion(UUID,String)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain the content for a specific version.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @return The content.
	 * @throws ParityException
	 */
	DocumentVersionContent getVersionContent(final UUID documentId,
			final String versionId) throws ParityException {
		logger.info("getVersionContent(UUID,String)");
		logger.debug(documentId);
		logger.debug(versionId);
		try {
			final Document document = get(documentId);
			final DocumentVersion version = getVersion(documentId, versionId);
			return documentXmlIO.getVersionContent(document, version);
		}
		catch(IOException iox) {
			logger.error("getVersionContent(DocumentVersion)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("getVersionContent(DocumentVersion)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a list of documents for a project.
	 * 
	 * @param projectId
	 *            The project unique id.
	 * @return A list of documents for a project.
	 * @throws ParityException
	 */
	Collection<Document> list(final UUID projectId)
			throws ParityException {
		logger.info("list(UUID)");
		logger.debug(projectId);
		try {
			final ProjectModel projectModel = getProjectModel();
			final Project project = projectModel.get(projectId);
			final Collection<Document> documents = documentXmlIO.list(project);
			ArtifactSorter.sortDocumentsByName(documents);
			return documents;
		}
		catch(IOException iox) {
			logger.error("list(Project)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("list(Project)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a list of document versions for a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The list of document versions.
	 * @throws ParityException
	 */
	Collection<DocumentVersion> listVersions(final UUID documentId)
			throws ParityException {
		logger.info("listVersions(Document)");
		logger.debug(documentId);
		try {
			final Document document = get(documentId);
			final Collection<DocumentVersion> versions =
				documentXmlIO.listVersions(document);
			ArtifactSorter.sortDocumentVersionsByVersionId(versions);
			return versions;
		}
		catch(IOException iox) {
			logger.error("listVersions(Document)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("listVersions(Document)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Lock a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	void lock(final UUID documentId) throws ParityException {
		logger.info("lock(UUID)");
		logger.debug(documentId);
		try {
			// re-create the local file from the meta-data
			final Document document = get(documentId);
			final LocalFile localFile = getLocalFile(document);
			localFile.delete();
			localFile.write(getContent(documentId).getContent());
			localFile.lock();
			// flag the document without the key
			flagNotKey(documentId);
		}
		catch(IOException iox) {
			logger.error("lock(UUID)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("lock(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Move the document to an another project.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param destinationProjectId
	 *            The destination project unique id.
	 * @throws ParityException
	 */
	void move(final UUID documentId, final UUID destinationProjectId)
			throws ParityException {
		logger.info("move(UUID,UUID)");
		logger.debug(documentId);
		logger.debug(destinationProjectId);
		try {
			// if the document was unseen, flag it as unseen
			final Document document = get(documentId);
			final Boolean hadBeenSeen = document.contains(ParityObjectFlag.SEEN);
			if(Boolean.FALSE == hadBeenSeen) { flagAsSEEN(document); }

			// update the document references
			final ProjectModel projectModel = getProjectModel();
			final Project destinationProject =
				projectModel.get(destinationProjectId);
			document.setParentId(destinationProjectId);
			documentXmlIO.update(document);
			documentXmlIO.move(document, destinationProject);

			// flag as not seen
			if(Boolean.FALSE == hadBeenSeen) { flagAsNotSEEN(document); }

			// fire an update notification
			notifyUpdate_objectUpdated(document);
		}
		catch(IOException iox) {
			logger.error("move(Document,Project)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("move(Document,Project)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Open a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	void open(final UUID documentId) throws ParityException {
		logger.info("open(UUID)");
		logger.debug(documentId);
		try {
			final Document document = get(documentId);

			// open the local file
			final LocalFile localFile = getLocalFile(document);
			localFile.open();

			// flag it as having been seen
			flagAsSEEN(document);
		}
		catch(IOException iox) {
			logger.error("open(UUID)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("open(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Open a document version. Extract the version's content and open it.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param versionId
	 *            The version id.
	 * @throws ParityException
	 */
	void openVersion(final UUID documentId, final String versionId)
			throws ParityException {
		logger.info("openVersion(UUID,String)");
		logger.debug(documentId);
		logger.debug(versionId);
		try {
			final Document document = get(documentId);
			final DocumentVersion version = getVersion(documentId, versionId);

			final LocalFile localFile = getLocalFile(document, version);
			localFile.open();
		}
		catch(IOException iox) {
			logger.error("openVersion(UUID,String)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("openVersion(UUID,String)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Use the document model to receive a document from another parity user.
	 * 
	 * @param xmppDocument
	 *            The xmpp document received from another parity user.
	 * @throws ParityException
	 */
	void receive(final XMPPDocument xmppDocument)
			throws ParityException {
		logger.info("receiveDocument(XMPPDocument)");
		logger.debug(xmppDocument);
		try {
			final Document existingDocument = get(xmppDocument.getId());
			logger.debug(existingDocument);
			if(null == existingDocument) { receiveCreate(xmppDocument); }
			else { receiveUpdate(xmppDocument, existingDocument); }
		}
		catch(IOException iox) {
			logger.error("receiveDocument(XMPPDocument)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("receiveDocument(XMPPDocument)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Remove a document creation event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	void removeListener(final CreationListener listener) {
		logger.info("removeCreationListener(CreationListener)");
		logger.debug(listener);
		Assert.assertNotNull(
				"removeCreationListener(CreationListener)", listener);
		synchronized(DocumentModelImpl.creationListenersLock) {
			Assert.assertTrue(
					"removeCreationListener(CreationListener)",
					DocumentModelImpl.creationListeners.contains(listener));
			DocumentModelImpl.creationListeners.remove(listener);
		}
	}

	/**
	 * Remove a document update event listener.
	 * 
	 * @param listener
	 *            The listener to remove.
	 */
	void removeListener(final UpdateListener listener) {
		logger.info("removeUpdateListener(UpdateListener)");
		logger.debug(listener);
		Assert.assertNotNull("removeUpdateListener(UpdateListener)", listener);
		synchronized (DocumentModelImpl.updateListenersLock) {
			Assert.assertTrue(
					"removeUpdateListener(UpdateListener)",
					DocumentModelImpl.updateListeners.contains(listener));
			DocumentModelImpl.updateListeners.remove(listener);
		}
	}

	/**
	 * Unlock a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @throws ParityException
	 */
	void unlock(final UUID documentId) throws ParityException {
		logger.info("importKey(UUID)");
		logger.debug(documentId);
		try {
			// re-create the local file from the meta-data
			final Document d = get(documentId);
			final LocalFile localFile = getLocalFile(d);
			localFile.delete();
			localFile.write(getContent(documentId).getContent());

			// flag the document with the key
			flagKey(documentId);
		}
		catch(IOException iox) {
			logger.error("importKey(UUID)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("importKey(UUID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Update a document.
	 * 
	 * @param document
	 *            The document to update.
	 * @throws ParityException
	 */
	void update(final Document document) throws ParityException {
		logger.info("update(Document)");
		logger.debug(document);
		try {
			documentXmlIO.update(document);
			notifyUpdate_objectUpdated(document);
		}
		catch(IOException iox) {
			logger.error("update(Document)", iox);
			throw ParityErrorTranslator.translate(iox);
		}
		catch(RuntimeException rx) {
			logger.error("update(Document)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Create the action data for the receive api.
	 * 
	 * @param document
	 *            The document that was received.
	 * @return The action data for the receive api.
	 */
	private DocumentActionData createActionData(final Document document) {
		return new DocumentActionData();
	}

	/**
	 * Create the action data for the version created by receiving a document.
	 * 
	 * @param user
	 *            The user who sent the document.
	 * @return The receive version action data.
	 */
	private DocumentActionData createReceiveActionData(final User user) {
		final DocumentActionData actionData = new DocumentActionData();
		actionData.setDataItem("user", user.getUsername());
		return actionData;
	}

	/**
	 * Delete a version.
	 * 
	 * @param document
	 *            The document.
	 * @param version
	 *            The version.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParityException
	 */
	private void deleteVersion(final Document document,
			final DocumentVersion version) throws FileNotFoundException,
			IOException, ParityException {
		// delete the version content
		final DocumentVersionContent versionContent =
			getVersionContent(document.getId(), version.getVersionId());
		documentXmlIO.deleteVersionContent(document, versionContent);

		// delete the local file
		getLocalFile(document, version).delete();

		// delete the version
		documentXmlIO.deleteVersion(document, version);
	}

	/**
	 * Add the key flag to the document.
	 * 
	 * @param document
	 *            The document.
	 */
	private void flagKey(final UUID documentId) throws FileNotFoundException,
			IOException, ParityException {
		final Document document = get(documentId);
		Assert.assertNotTrue(
				"flagKey(Document)", document.contains(ParityObjectFlag.KEY));
		document.add(ParityObjectFlag.KEY);
		documentXmlIO.update(document);
	}

	/**
	 * Remove the key flag from the document.
	 * 
	 * @param document
	 *            The document.
	 * @throws ParityException
	 */
	private void flagNotKey(final UUID documentId)
			throws FileNotFoundException, IOException, ParityException {
		final Document document = get(documentId);
		Assert.assertTrue(
				"flagNotKey(Document)", document.contains(ParityObjectFlag.KEY));
		document.remove(ParityObjectFlag.KEY);
		documentXmlIO.update(document);
	}

	/**
	 * Create a document local file reference for a given document.
	 * 
	 * @param document
	 *            The document.
	 * @return The document local file reference.
	 */
	private LocalFile getLocalFile(final Document document) {
		return new LocalFile(workspace, document);
	}

	/**
	 * Create a document local file reference for a given version.
	 * 
	 * @param version
	 *            The version.
	 * @return The document local file reference.
	 */
	private LocalFile getLocalFile(final Document document,
			final DocumentVersion version) {
		return new LocalFile(workspace, document, version);
	}

	/**
	 * Determine whether or not the user has the key for a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return True if the user has the key; false otherwise.
	 * @throws ParityException
	 */
	private Boolean hasKey(final UUID documentId) throws ParityException {
		final Document document = get(documentId);
		return document.contains(ParityObjectFlag.KEY);
	}

	/**
	 * Obtain the next version in the sequence for a document.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @return The next version in the sequence.
	 */
	private String nextVersionId(final UUID documentId)
			throws ParityException {
		final Integer numberOfVersions = listVersions(documentId).size();
		return new StringBuffer("v").append(numberOfVersions + 1).toString();
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
		synchronized (DocumentModelImpl.creationListenersLock) {
			for (CreationListener listener : DocumentModelImpl.creationListeners) {
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
	private void notifyCreation_objectVersionCreated(
			final DocumentVersion documentVersion) {
		synchronized (DocumentModelImpl.creationListenersLock) {
			for (CreationListener listener : DocumentModelImpl.creationListeners) {
				listener.objectVersionCreated(new VersionCreationEvent(
						documentVersion));
			}
		}
	}

	/**
	 * Fire the object deleted event for all of the update listeners.
	 * 
	 * @param document
	 *            The document that was deleted.
	 */
	private void notifyUpdate_objectDeleted(final Document document) {
		synchronized(DocumentModelImpl.updateListeners) {
			for(UpdateListener listener : DocumentModelImpl.updateListeners) {
				listener.objectDeleted(new DeleteEvent(document));
			}
		}
	}

	/**
	 * Fire the objectReceived event for all update listeners.
	 * 
	 * @param document
	 *            The document to use as the event source.
	 * @see UpdateListener#objectReceived(UpdateEvent)
	 */
	private void notifyUpdate_objectReceived(final Document document) {
		synchronized(DocumentModelImpl.updateListenersLock) {
			for(UpdateListener listener : DocumentModelImpl.updateListeners) {
				listener.objectReceived(new UpdateEvent(document));
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
	 * Receive the xmpp document and create a new local document.
	 * 
	 * @param xmppDocument
	 *            The xmpp document received.
	 * @throws ParityException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void receiveCreate(final XMPPDocument xmppDocument)
			throws ParityException, FileNotFoundException, IOException {
		/*
		 * Obtain the inbox parity project within the workspace, and place the
		 * received document within it, check if the SEEN flag has been
		 * transferred across and if it has, remove it; then notify all
		 * listeners about the new document.
		 */
		final Project inbox = getProjectModel().getInbox();
		final Document document = new Document(xmppDocument.getCreatedBy(),
				xmppDocument.getCreatedOn(), xmppDocument.getDescription(),
				xmppDocument.getFlags(), xmppDocument.getId(),
				xmppDocument.getName(), inbox.getId(),
				xmppDocument.getUpdatedBy(), xmppDocument.getUpdatedOn());
		final DocumentContent content = new DocumentContent(
				MD5Util.md5Hex(xmppDocument.getContent()),
				xmppDocument.getContent(), xmppDocument.getId());

		// create the local file
		final LocalFile localFile = getLocalFile(document);
		localFile.write(content.getContent());

		// create the document
		documentXmlIO.create(document, content);

		// create a version
		createVersion(document.getId(), DocumentAction.RECEIVE,
				createReceiveActionData(getUser(xmppDocument.getUpdatedBy())));

		// flag as not seen
		flagAsNotSEEN(document);

		// lock the file
		lock(document.getId());

		// send the server a subscription request
		final SessionModel sessionModel = getSessionModel();
		sessionModel.sendSubscribe(document);

		// fire a receive event
		notifyCreation_objectReceived(document);
	}

	/**
	 * Receive the xmpp document and update the existing local document.
	 * 
	 * @param xmppDocument
	 *            The xmpp document received.
	 * @param document
	 *            The existing local document.
	 * @throws IOException
	 * @throws ParityException
	 * @throws FileNotFoundException
	 */
	private void receiveUpdate(final XMPPDocument xmppDocument,
			final Document document) throws IOException, ParityException,
			FileNotFoundException {
		// there is a potential for wierdness here
		Assert.assertNotTrue(
				"User with the key is receiving a document update?",
				hasKey(document.getId()));

		// create a new version of the existing document
		createVersion(
				document.getId(), DocumentAction.RECEIVE,
				createReceiveActionData(getUser(xmppDocument.getUpdatedBy())));

		// update the content
		final DocumentContent content = new DocumentContent(
				MD5Util.md5Hex(xmppDocument.getContent()),
				xmppDocument.getContent(), document.getId());
		documentXmlIO.update(document, content);

		// update the content local file
		final LocalFile localFile = getLocalFile(document);
		localFile.delete();
		localFile.write(content.getContent());
		localFile.lock();

		// flag the document
		flagAsNotSEEN(document);

		// notify
		notifyUpdate_objectReceived(document);
	}

	/**
	 * Write the content of a document to a file.
	 * 
	 * @param documentId
	 *            The document unique id.
	 * @param file
	 *            The target file.
	 * @throws IOException
	 */
	private void writeDocumentContent(final UUID documentId, final File file)
			throws ParityException, IOException {
		final DocumentContent content = getContent(documentId);
		FileUtil.writeBytes(file, content.getContent());
	}
}
