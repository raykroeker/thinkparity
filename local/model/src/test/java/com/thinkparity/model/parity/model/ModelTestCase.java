/*
 * Aug 6, 2005
 */
package com.thinkparity.model.parity.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.assertion.NotYetImplementedAssertion;

import com.thinkparity.model.ModelTestUser;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactModel;
import com.thinkparity.model.parity.model.artifact.InternalArtifactModel;
import com.thinkparity.model.parity.model.artifact.KeyRequest;
import com.thinkparity.model.parity.model.audit.HistoryItem;
import com.thinkparity.model.parity.model.container.Container;
import com.thinkparity.model.parity.model.container.ContainerHistoryItem;
import com.thinkparity.model.parity.model.container.ContainerModel;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.container.InternalContainerModel;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentHistoryItem;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.document.InternalDocumentModel;
import com.thinkparity.model.parity.model.library.InternalLibraryModel;
import com.thinkparity.model.parity.model.library.LibraryModel;
import com.thinkparity.model.parity.model.message.system.InternalSystemMessageModel;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
import com.thinkparity.model.parity.model.release.InternalReleaseModel;
import com.thinkparity.model.parity.model.release.ReleaseModel;
import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * ModelTestCase
 * Abstract root for all of the parity test cases.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends com.thinkparity.model.ModelTestCase {

    /**
	 * Assert that the document list provided contains the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	protected static void assertContains(final Collection<Document> documentList,
			Document document) {
		ModelTestCaseHelper.assertContains(documentList, document);
	}

    /**
     * Assert that the expected container matches the actual.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected container.
     * @param actual
     *            The actual container.
     */
    protected static void assertEquals(final String assertion, final Container expected, final Container actual) {
        assertEquals(assertion + " [CONTAINER DOES NOT MATCH EXPECTATION]", (Object) expected, (Object) actual);
        assertEquals(assertion + " [CONTAINER'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [CONTAINER'S CREATED ON DOES NOT MATCH EXPECTATION]", expected.getCreatedOn(), actual.getCreatedOn());
        assertEquals(assertion + " [CONTAINER'S FLAGS DO NOT MATCH EXPECTATION]", expected.getFlags(), actual.getFlags());
        assertEquals(assertion + " [CONTAINER'S ID DOES NOT MATCH EXPECTATION]", expected.getId(), actual.getId());
        assertEquals(assertion + " [CONTAINER'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [CONTAINER'S REMOTE INFO DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo(), actual.getRemoteInfo());
        assertEquals(assertion + " [CONTAINER'S REMOTE INFO'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedBy(), actual.getRemoteInfo().getUpdatedBy());
        assertEquals(assertion + " [CONTAINER'S REMOTE INFO'S UPDATED ON DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedOn(), actual.getRemoteInfo().getUpdatedOn());
        assertEquals(assertion + " [CONTAINER'S STATE DOES NOT MATCH EXPECTATION]", expected.getState(), actual.getState());
        assertEquals(assertion + " [CONTAINER'S TYPE DOES NOT MATCH EXPECTATION]", expected.getType(), actual.getType());
        assertEquals(assertion + " [CONTAINER'S UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getUniqueId(), actual.getUniqueId());
        assertEquals(assertion + " [CONTAINER'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [CONTAINER'S UPDATED ON DOES NOT MATCH EXPECTATION]", expected.getUpdatedOn(), actual.getUpdatedOn());
    }

    /**
     * Assert that the members that match in a container match those in the
     * version.
     * 
     * @param assertion
     *            The assertion.
     * @param container
     *            The container.
     * @param version
     *            The container version.
     */
    protected static void assertEquals(final String assertion, final Container container, final ContainerVersion version) {
        assertEquals(assertion + " [CONTAINER'S ID DOES NOT MATCH THE VERSION'S]", container.getId(), version.getArtifactId());
        assertEquals(assertion + " [CONTAINER'S ARTIFACT TYPE DOES NOT MATCH THE VERSION'S]", container.getType(), version.getArtifactType());
        assertEquals(assertion + " [CONTAINER'S UNIQUE ID DOES NOT MATCH THE VERSION'S]", container.getUniqueId(), version.getArtifactUniqueId());
        assertEquals(assertion + " [CONTAINER'S CREATED BY DOES NOT MATCH THE VERSION'S]", container.getCreatedBy(), version.getCreatedBy());
        assertEquals(assertion + " [CONTAINER'S CREATED ON DOES NOT MATCH THE VERSION'S]", container.getCreatedOn(), version.getCreatedOn());
        assertEquals(assertion + " [CONTAINER'S NAME DOES NOT MATCH THE VERSION'S]", container.getName(), version.getName());
        assertEquals(assertion + " [CONTAINER'S UPDATED BY DOES NOT MATCH THE VERSION'S]", container.getUpdatedBy(), version.getUpdatedBy());
        assertEquals(assertion + " [CONTAINER'S UPDATED ON DOES NOT MATCH THE VERSION'S]", container.getUpdatedOn(), version.getUpdatedOn());
    }

    /**
     * Assert the expected version matches the actual one.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected container version.
     * @param actual
     *            The actual container version.
     */
    protected static void assertEquals(final String assertion, final ContainerVersion expected, final ContainerVersion actual) {
        assertEquals(assertion + " [CONTAINER VERSION DOES NOT MATCH EXPECTATION]", (Object) expected, (Object) actual);
        assertEquals(assertion + " [CONTAINER VERSION'S ARTIFACT ID DOES NOT MATCH EXPECTATION]", expected.getArtifactId(), actual.getArtifactId());
        assertEquals(assertion + " [CONTAINER VERSION'S ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getArtifactType(), actual.getArtifactType());
        assertEquals(assertion + " [CONTAINER VERSION'S ARTIFACT UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getArtifactUniqueId(), actual.getArtifactUniqueId());
        assertEquals(assertion + " [CONTAINER VERSION'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [CONTAINER VERSION'S CREATED ON DOES NOT MATCH EXPECTATION]", expected.getCreatedOn(), actual.getCreatedOn());
        assertEquals(assertion + " [CONTAINER VERSION'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [CONTAINER VERSION'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [CONTAINER VERSION'S UPDATED ON DOES NOT MATCH EXPECTATION]", expected.getUpdatedOn(), actual.getUpdatedOn());
        assertEquals(assertion + " [CONTAINER VERSION'S VERSION ID DOES NOT MATCH EXPECTATION]", expected.getVersionId(), actual.getVersionId());
    }

    /**
     * Assert the expected document matches the actual.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected document.
     * @param actual
     *            The actual document.
     */
    protected static void assertEquals(final String assertion,
            final Document expected, final Document actual) {
        assertEquals(assertion + " [DOCUMENT DOES NOT MATCH EXPECTATION]", (Object) expected, (Object) actual);
        assertEquals(assertion + " [DOCUMENT'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [DOCUMENT'S CREATED ON DOES NOT MATCH EXPECTATION]", expected.getCreatedOn(), actual.getCreatedOn());
        assertEquals(assertion + " [DOCUMENT'S FLAGS DO NOT MATCH EXPECTATION]", expected.getFlags(), actual.getFlags());
        assertEquals(assertion + " [DOCUMENT'S ID DOES NOT MATCH EXPECTATION]", expected.getId(), actual.getId());
        assertEquals(assertion + " [DOCUMENT'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [DOCUMENT'S REMOTE INFO DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo(), actual.getRemoteInfo());
        assertEquals(assertion + " [DOCUMENT'S REMOTE INFO'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedBy(), actual.getRemoteInfo().getUpdatedBy());
        assertEquals(assertion + " [DOCUMENT'S REMOTE INFO'S UPDATED ON DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedOn(), actual.getRemoteInfo().getUpdatedOn());
        assertEquals(assertion + " [DOCUMENT'S STATE DOES NOT MATCH EXPECTATION]", expected.getState(), actual.getState());
        assertEquals(assertion + " [DOCUMENT'S TYPE DOES NOT MATCH EXPECTATION]", expected.getType(), actual.getType());
        assertEquals(assertion + " [DOCUMENT'S UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getUniqueId(), actual.getUniqueId());
        assertEquals(assertion + " [DOCUMENT'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [DOCUMENT'S UPDATED ON DOES NOT MATCH EXPECTATION]", expected.getUpdatedOn(), actual.getUpdatedOn());
    }

    /**
     * Assert that the expected version matches the actual one.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected version.
     * @param actual
     *            The actual version.
     */
    protected static void assertEquals(final String assertion, final DocumentVersion expected, final DocumentVersion actual) {
        assertEquals(assertion + " [ARTIFACT ID DOES NOT MATCH EXPECTATION]", expected.getArtifactId(), actual.getArtifactId());
        assertEquals(assertion + " [ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getArtifactType(), actual.getArtifactType());
        assertEquals(assertion + " [ARTIFACT UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getArtifactUniqueId(), actual.getArtifactUniqueId());
        assertEquals(assertion + " [CHECKSUM DOES NOT MATCH EXPECTATION]", expected.getChecksum(), actual.getChecksum());
        assertEquals(assertion + " [COMPRESSION DOES NOT MATCH EXPECTATION]", expected.getCompression(), actual.getCompression());
        assertEquals(assertion + " [CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [CREATED ON DOES NOT MATCH EXPECTATION]", expected.getCreatedOn(), actual.getCreatedOn());
        assertEquals(assertion + " [ENCODING DOES NOT MATCH EXPECTATION]", expected.getEncoding(), actual.getEncoding());
        assertEquals(assertion + " [NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [UPDATED ON DOES NOT MATCH EXPECTATION]", expected.getUpdatedOn(), actual.getUpdatedOn());
        assertEquals(assertion + " [VERSION ID DOES NOT MATCH EXPECTATION]", expected.getVersionId(), actual.getVersionId());
    }

	/**
     * Assert that the expected version content matches the actual version
     * content.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected version content.
     * @param actual
     *            The actual version content.
     */
    protected static void assertEquals(final String assertion, final DocumentVersionContent expected, final DocumentVersionContent actual) {
        assertEquals(assertion, expected.getContent(), actual.getContent());
        assertEquals(assertion, expected.getVersion(), actual.getVersion());
    }

	/**
	 * Assert that the document list provided doesn't contain the document.
	 * 
	 * @param documentList
	 *            The document list to check.
	 * @param document
	 *            The document to validate.
	 */
	protected static void assertNotContains(final Collection<Document> documentList,
			Document document) {
		ModelTestCaseHelper.assertNotContains(documentList, document);
	}

    /**
     * Assert that a list of versions is not null and all of the versions in the
     * list are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param versions
     *            A list of versions.
     */
    protected static void assertNotNull(final String assertion, final Collection<DocumentVersion> versions) {
        assertNotNull(assertion + " [DOCUMENT VERSIONS IS NULL]", (Object) versions);
        for(final DocumentVersion version : versions) {
            assertNotNull(assertion, version);
        }
    }

    /**
     * Assert the container is not null.
     * 
     * @param assertion
     *            An assertion.
     * @param container
     *            A container.
     */
    protected static void assertNotNull(final String assertion, final Container container) {
        assertNotNull(assertion + " [CONTAINER IS NULL]", (Object) container);
        assertNotNull(assertion + " [CONTAINER'S CREATED BY IS NULL]", container.getCreatedBy());
        assertNotNull(assertion + " [CONTAINER'S CREATED ON IS NULL]", container.getCreatedOn());
        assertNotNull(assertion + " [CONTAINER'S FLAGS IS NULL]", container.getFlags());
        assertNotNull(assertion + " [CONTAINER'S ID IS NULL]", container.getId());
        assertNotNull(assertion + " [CONTAINER'S NAME IS NULL]", container.getName());
        assertNotNull(assertion + " [CONTAINER'S REMOTE INFO IS NULL]", container.getRemoteInfo());
        assertNotNull(assertion + " [CONTAINER'S UPDATED BY REMOTE INFO IS NULL]", container.getRemoteInfo().getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER'S UPDATED ON REMOTE INFO IS NULL]", container.getRemoteInfo().getUpdatedOn());
        assertNotNull(assertion + " [CONTAINER'S STATE IS NULL]", container.getState());
        assertNotNull(assertion + " [CONTAINER'S TYPE IS NULL]", container.getType());
        assertNotNull(assertion + " [CONTAINER'S UNIQUE ID IS NULL]", container.getUniqueId());
        assertNotNull(assertion + " [CONTAINER'S UPDATED BY IS NULL]", container.getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER'S UPDATED ON IS NULL]", container.getUpdatedOn());
    }

    /**
     * Assert the history item and all of its required memebers are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param historyItem
     *            The history item.
     */
    protected static void assertNotNull(final String assertion,
            final ContainerHistoryItem historyItem) {
        assertNotNull(assertion, (HistoryItem) historyItem);
        assertNotNull(assertion + " [CONTAINER HISTORY ITEM CONTAINER ID IS NULL]", historyItem.getContainerId());
    }

    /**
     * Assert that a container version is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param version
     *            The container version.
     */
    protected static void assertNotNull(final String assertion, final ContainerVersion version) {
        assertNotNull(assertion + " [CONTAINER VERSION IS NULL]", (Object) version);
        assertNotNull(assertion + " [CONTAINER VERSION'S ARTIFACT ID IS NULL]", version.getArtifactId());
        assertNotNull(assertion + " [CONTAINER VERSION'S ARTIFACT TYPE IS NULL]", version.getArtifactType());
        assertNotNull(assertion + " [CONTAINER VERSION'S UNIQUE ID IS NULL]", version.getArtifactUniqueId());
        assertNotNull(assertion + " [CONTAINER VERSION'S CREATED BY IS NULL]", version.getCreatedBy());
        assertNotNull(assertion + " [CONTAINER VERSION'S CREATED ON IS NULL]", version.getCreatedOn());
        assertNotNull(assertion + " [CONTAINER VERSION'S NAME IS NULL]", version.getName());
        assertNotNull(assertion + " [CONTAINER VERSION'S UPDATED BY IS NULL]", version.getUpdatedBy());
        assertNotNull(assertion + " [CONTAINER VERSION'S UPDATED ON IS NULL]", version.getUpdatedOn());
        assertNotNull(assertion + " [CONTAINER VERSION'S VERSION ID IS NULL]", version.getVersionId());
    }

    /**
     * Assert that the document and all of its required members are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param document
     *            The document.
     */
    protected static void assertNotNull(final String assertion, final Document document) {
        assertNotNull(assertion + " [DOCUMENT IS NULL]", (Object) document);
        assertNotNull(assertion + " [DOCUMENT CREATED BY IS NULL]", document.getCreatedBy());
        assertNotNull(assertion + " [DOCUMENT CREATED ON IS NULL]", document.getCreatedOn());
        assertNotNull(assertion + " [DOCUMENT FLAGS IS NULL]", document.getFlags());
        assertNotNull(assertion + " [DOCUMENT ID IS NULL]", document.getId());
        assertNotNull(assertion + " [DOCUMENT NAME IS NULL]", document.getName());
        assertNotNull(assertion + " [DOCUMENT REMOTE INFO IS NULL]", document.getRemoteInfo());
        assertNotNull(assertion + " [DOCUMENT REMOTE INFO UPDATED BY IS NULL]", document.getRemoteInfo().getUpdatedBy());
        assertNotNull(assertion + " [DOCUMENT REMOTE INFO UPDATED ON IS NULL]", document.getRemoteInfo().getUpdatedOn());
        assertNotNull(assertion + " [DOCUMENT STATE IS NULL]", document.getState());
        assertNotNull(assertion + " [DOCUMENT TYPE IS NULL]", document.getType());
        assertNotNull(assertion + " [DOCUMENT UNIQUE ID IS NULL]", document.getUniqueId());
        assertNotNull(assertion + " [DOCUMENT UPDATED BY IS NULL]", document.getUpdatedBy());
        assertNotNull(assertion + " [DOCUMENT UPDATED ON IS NULL]", document.getUpdatedOn());
    }

    /**
     * Assert the history item and all of its required memebers are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param historyItem
     *            The history item.
     */
    protected static void assertNotNull(final String assertion,
            final DocumentHistoryItem historyItem) {
        assertNotNull(assertion + " [HISTORY ITEM IS NULL]", (HistoryItem) historyItem);
        assertNotNull(assertion + " [DOCUMENT HISTORY ITEM DOCUMENT ID IS NULL]", historyItem.getDocumentId());
    }

    /**
     * Assert that the document version and all of its required fields are not
     * null.
     * 
     * @param assertion The assertion.
     * @param version The document version.
     */
    protected static void assertNotNull(final String assertion, final DocumentVersion version) {
        assertNotNull(assertion + " [DOCUMENT VERSION IS NULL]", (Object) version);
        assertNotNull(assertion + " [DOCUMENT VERSION ARTIFACT ID IS NULL]", version.getArtifactId());
        assertNotNull(assertion + " [DOCUMENT VERSION ARTIFACT TYPE IS NULL]", version.getArtifactType());
        assertNotNull(assertion + " [DOCUMENT VERSION UNIQUE ID IS NULL]", version.getArtifactUniqueId());
        assertNotNull(assertion + " [DOCUMENT VERSION CHECKSUM IS NULL]", version.getChecksum());
        assertNotNull(assertion + " [DOCUMENT VERSION COMPRESSION IS NULL]", version.getCompression());
        assertNotNull(assertion + " [DOCUMENT VERSION CREATED BY IS NULL]", version.getCreatedBy());
        assertNotNull(assertion + " [DOCUMENT VERSION CREATED ON IS NULL]", version.getCreatedOn());
        assertNotNull(assertion + " [DOCUMENT VERSION ENCODING IS NULL]", version.getEncoding());
        assertNotNull(assertion + " [DOCUMENT VERSION NAME IS NULL]", version.getName());
        assertNotNull(assertion + " [DOCUMENT VERSION UPDATED ON IS NULL]", version.getUpdatedBy());
        assertNotNull(assertion + " [DOCUMENT VERSION UPDATED BY IS NULL]", version.getUpdatedOn());
        assertNotNull(assertion + " [DOCUMENT VERSION VERSION ID IS NULL]", version.getVersionId());
    }

    /**
     * Assert that a version content and all of its required members are not
     * null.
     * 
     * @param assertion
     *            The assertion.
     * @param versionContent
     *            The version content.
     */
    protected static void assertNotNull(final String assertion, final DocumentVersionContent versionContent) {
        assertNotNull(assertion + " [VERSION CONTENT IS NULL]", (Object) versionContent);
        assertNotNull(assertion + " [VERSION CONTENT'S CONTENT IS NULL]", versionContent.getContent());
        assertNotNull(assertion + " [VERSION CONTENT'S VERSION IS NULL]", versionContent.getVersion());
    }

    /**
     * Assert the history item and all of its required memebers are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param historyItem
     *            The history item.
     */
    protected static void assertNotNull(final String assertion, final HistoryItem historyItem) {
        assertNotNull(assertion + " [HISTORY ITEM IS NULL]", (Object) historyItem);
        assertNotNull(assertion + " [HISTORY ITEM DATE IS NULL]", historyItem.getDate());
        assertNotNull(assertion + " [HISTORY ITEM EVENT IS NULL]", historyItem.getEvent());
        assertNotNull(assertion + " [HISTORY ITEM ID IS NULL]", historyItem.getId());
    }

    /**
     * Assert that the set of users is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param users
     *            The set of users.
     */
    protected static void assertNotNull(final String assertion,
            final Set<User> users) {
        assertNotNull(assertion + " [USERS IS NULL]", (Object) users);
        for(final User user : users) {
            assertNotNull(assertion, user);
        }
    }

    /**
     * Assert that the user and all of the user required fields are not null.
     * 
     * @param assertion
     *            The assertion.
     * @param user
     *            The user.
     */
    protected static void assertNotNull(final String assertion, final User user) {
        assertNotNull(assertion + " [USER IS NULL]", (Object) user);
        assertNotNull(assertion + " [USER'S ID IS NULL]", user.getId());
        assertNotNull(assertion + " [USER'S LOCAL ID IS NULL]", user.getLocalId());
        assertNotNull(assertion + " [USER'S NAME IS NULL]", user.getName());
        assertNotNull(assertion + " [USER'S ORGANIZATION IS NULL]", user.getOrganization());
    }

    /** The internal artifact model. */
    private InternalArtifactModel artifactModel;

	private InternalContainerModel iContainerModel;

    private InternalDocumentModel iDocumentModel;

    private InternalLibraryModel ilModel;

	private InternalReleaseModel irModel;

	private InternalSystemMessageModel messageModel;

	/**
	 * The parity preferences.
	 * 
	 */
	private Preferences preferences;

	/**
	 * The session model.
	 * 
	 */
	private SessionModel sessionModel;

	/**
	 * The parity workspace.
	 * 
	 */
	private Workspace workspace;
	
	/**
	 * The workspace model.
	 * 
	 */
	private WorkspaceModel workspaceModel;

    /**
	 * Create a ModelTestCase
	 * 
	 * @param name
	 *            The test name.
	 */
	protected ModelTestCase(final String name) { super(name); }

	/**
     * Add a document to a container.
     * 
     * @param container
     *            A container.
     * @param inputFile
     *            An input file.
     * @return A document.
     * @throws IOException
     * @throws ParityException
     */
    protected Document addDocument(final Container container, final File inputFile) throws IOException, ParityException {
        final Document document = create(getInputFiles()[0]);
        getContainerModel().addDocument(container.getId(), document.getId());
        return document;
    }

	protected void addTeam(final Container container) throws Exception {
        addTeamToContainer(container.getId());
    }

    /**
     * Create a document.
     * 
     * @param file
     *            A file.
     * @return A document.
     * @throws IOException
     * @throws ParityException
     */
    protected Document create(final File file) throws IOException,
            ParityException {
        final FileInputStream fis = new FileInputStream(file);
        try {
            return getInternalDocumentModel().create(null, file.getName(), fis);
        }
        finally { fis.close(); }
    }

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The container.
     */
    protected Container createContainer(final String name)
            throws ParityException {
        return getInternalContainerModel().create(name);
    }

    /**
	 * @see com.raykroeker.junitx.TestCase#createFailMessage(java.lang.Throwable)
	 * 
	 */
	protected String createFailMessage(Throwable t) {
		testLogger.error("Failure", t);
		return super.createFailMessage(t);
	}

    /**
	 * Obtain a single test file.
	 * 
	 * @param name
	 *            The file name.
	 * @return The test file.
	 */
	protected File getInputFile(final String name) throws IOException {
		for(final File file : getInputFiles()) {
			if(file.getName().equals(name)) { return file; }
		}
		return null;
	}

    /**
	 * @see com.raykroeker.junitx.TestCase#getInputFiles()
	 * 
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	}

    protected InternalArtifactModel getInternalArtifactModel() {
        if(null == artifactModel) {
            artifactModel = ArtifactModel.getInternalModel(new Context(getClass()));
        }
        return artifactModel;
    }

    protected InternalContainerModel getInternalContainerModel() {
        if(null == iContainerModel) {
            iContainerModel = ContainerModel.getInternalModel(new Context(getClass()));
        }
        return iContainerModel;
    }

    protected InternalDocumentModel getInternalDocumentModel() {
        if(null == iDocumentModel) {
            return DocumentModel.getInternalModel(new Context(getClass()));
        }
        return iDocumentModel;
    }

    protected InternalLibraryModel getInternalLibraryModel() {
        if(null == ilModel) {
            ilModel = LibraryModel.getInternalModel(new Context(getClass()));
        }
        return ilModel;
    }

    protected InternalSystemMessageModel getInternalMessageModel() {
        if(null == messageModel) {
            messageModel = SystemMessageModel.getInternalModel(new Context(getClass()));
        }
        return messageModel;
    }

	protected InternalReleaseModel getInternalReleaseModel() {
        if(null == irModel) {
            irModel = ReleaseModel.getInternalModel(new Context(getClass()));
        }
        return irModel;
    }

	protected File[] getModFiles() throws IOException {
        final File[] modFiles = new File[5];
        System.arraycopy(super.getModFiles(), 0, modFiles, 0, 5);
        return modFiles;
    }

	/**
	 * Obtain the parity preferences.
	 * 
	 * @return The parity preferences.
	 */
	protected Preferences getPreferences() {
		if(null == preferences) {
			preferences = getWorkspace().getPreferences();
		}
		return preferences;
	}

	/**
	 * Obtain the session model.
	 * 
	 * @return The session model.
	 */
	protected SessionModel getSessionModel() {
		if(null == sessionModel) {
			sessionModel = SessionModel.getModel();
		}
		return sessionModel;
	}

	/**
	 * Obtain the parity workspace.
	 * 
	 * @return The parity workspace.
	 */
	protected Workspace getWorkspace() {
		if(null == workspace) {
			workspace = getWorkspaceModel().getWorkspace();
		}
		return workspace;
	}

    /**
	 * Obtain a handle to the parity workspace model.
	 * 
	 * @return A handle to the parity workspace model.
	 */
	protected WorkspaceModel getWorkspaceModel() {
		if(null == workspaceModel) {
			workspaceModel = WorkspaceModel.getModel();
		}
		return workspaceModel;
	}
	
    /**
     * Determine if the session model is currently logged in.
     * 
     * @return True if it is.
     */
    protected Boolean isLoggedIn() { return getSessionModel().isLoggedIn(); }

    /**
	 * Determine if the parity error is generated by the model due to operating
	 * system constraints.
	 * 
	 * @param px
	 *            The parity error.
	 * @return True if the error is caused by a NotYetImplementedAssertion and
	 *         the current operating system is linux.
	 */
	protected boolean isNYIAOnLinux(final ParityException px) {
		Throwable cause = px.getCause();
		while(null != cause) {
			if(NotYetImplementedAssertion.class.isAssignableFrom(cause.getClass())) {
				switch(OSUtil.getOS()) {
				case LINUX:
					testLogger.warn("[PARITY] Running test on un-supported platform.");
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	/**
     * Establish a session for the model test user. If the session is already
     * established (happens when a test errors in the setup phase) an error is
     * logged and the session is re-establihsed.
     * 
     */
	protected void login() {
        if(isLoggedIn()) {
            logger.warn(getName() + " [USER ALREADY LOGGED IN]");
            logout();
        }

		final ModelTestUser modelTestUser = getModelTestUser();
		try { getSessionModel().login(modelTestUser.getCredentials()); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	/**
     * Terminate an existing session.
     *
     */
	protected void logout() {
		try { getSessionModel().logout(); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

    protected void modifyDocument(final Document document) throws Exception {
        modifyDocument(document.getId());
    }

    protected void modifyDocument(final Long documentId) throws Exception {
        final String prefix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
        final String suffix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
        final File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();
    
        FileUtil.writeBytes(tempFile,
                ("jUnit Test MOD " +
                DateUtil.format(DateUtil.getInstance(), DateImage.ISO)).getBytes());
        getDocumentModel().updateWorkingVersion(documentId, tempFile);
    }

    /**
     * Read an artifact's team.
     * 
     * @param artifact
     *            An artifact.
     * @return A list of jabber ids.
     */
    protected List<JabberId> readTeam(final Artifact artifact) {
        final List<JabberId> team = new ArrayList<JabberId>();
        for(final User user : getArtifactModel().readTeam(
                artifact.getId())) {
            team.add(user.getId());
        }
        return team;
    }

    /**
     * Request the key for the container from the user.
     * 
     * @param container
     *            The container.
     * @param testUser
     *            The user.
     * @return The key request.
     */
    protected KeyRequest requestKey(final Container container,
            final ModelTestUser testUser) {
        final SystemMessage systemMessage =
                getInternalMessageModel().createKeyRequest(
                        container.getId(), testUser.getJabberId());
        return getInternalArtifactModel().readKeyRequest(systemMessage.getId());
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception { super.setUp(); }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception { super.tearDown(); }

    private void addTeamToContainer(final Long containerId) throws Exception {
        final ModelTestUser userX = ModelTestUser.getX();
        getContainerModel().share(containerId, userX.getJabberId());
    
        final ModelTestUser userY = ModelTestUser.getY();
        getContainerModel().share(containerId, userY.getJabberId());
    
        final ModelTestUser userZ = ModelTestUser.getZ();
        getContainerModel().share(containerId, userZ.getJabberId());
    }
}
