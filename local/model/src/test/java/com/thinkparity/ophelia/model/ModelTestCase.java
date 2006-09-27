/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.assertion.NotYetImplementedAssertion;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.document.DocumentVersionContent;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.OpheliaTestCase;
import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.archive.ArchiveModel;
import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerHistoryItem;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.DocumentHistoryItem;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.message.InternalSystemMessageModel;
import com.thinkparity.ophelia.model.message.SystemMessageModel;
import com.thinkparity.ophelia.model.migrator.InternalLibraryModel;
import com.thinkparity.ophelia.model.migrator.InternalReleaseModel;
import com.thinkparity.ophelia.model.migrator.LibraryModel;
import com.thinkparity.ophelia.model.migrator.ReleaseModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.TeamMember;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

/**
 * ModelTestCase
 * Abstract root for all of the parity test cases.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends OpheliaTestCase {

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
        final StringBuffer actualIds = new StringBuffer("");
        Boolean didContain = Boolean.FALSE;
        int actualCounter = 0;
        for(Document actual : documentList) {
            if(actual.getId().equals(document.getId())) {
                didContain = Boolean.TRUE;
            }
            actualIds.append(actualCounter == 0 ? "" : ",");
            actualIds.append(actual.getId().toString());
        }
        ModelTestCase.assertTrue("expected:<" + document.getId() + " but was:<" + actualIds.toString(), didContain);

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
        final StringBuffer actualIds = new StringBuffer("");
        Boolean didContain = Boolean.FALSE;
        int actualCounter = 0;
        for(Document actual : documentList) {
            if(actual.getId().equals(document.getId())) {
                didContain = Boolean.TRUE;
            }
            actualIds.append(actualCounter++ == 0 ? "" : ",");
            actualIds.append(actual.getId().toString());
        }
        ModelTestCase.assertFalse("expected:<" + document.getId() + "> but was:<" + actualIds.toString() + ">", didContain);
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

    /** A thinkParity context. */
    protected Context context;

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
     */
    protected Document addDocument(final OpheliaTestUser testUser,
            final Container container, final File inputFile) throws IOException {
        logTrace("{0} - Creating document \"{1}\".", getName(), inputFile);
        final Document document = createDocument(testUser, inputFile);
        logTrace("{0} - Adding document \"{1}\" to container \"{2}\".", getName(),
                inputFile, container.getName());
        getContainerModel(testUser).addDocument(container.getId(), document.getId());
        return document;
    }

    /**
     * Add all of the input test files to the container as documents.
     * 
     * @param container
     *            A container.
     *            @return A list of documents.
     */
    protected List<Document> addDocuments(final OpheliaTestUser testUser,
            final Container container) throws IOException {
        final List<Document> documents = new ArrayList<Document>();
        for(final File inputFile : getInputFiles()) {
            documents.add(addDocument(testUser, container, inputFile));
        }
        return documents;
    }

    /**
     * Create a document.
     * 
     * @param file
     *            A file.
     * @return A document.
     * @throws IOException
     */
    protected Document create(final OpheliaTestUser testUser, final File file)
            throws IOException {
        return createDocument(testUser, file);
    }

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return The container.
     */
    protected Container createContainer(final OpheliaTestUser testUser,
            final String name) {
        final String containerName = MessageFormat.format(
                "{0} - {1,date,yyyyMMdd.HHmm}", name,
                currentDateTime().getTime());
        logTrace("{0} - Creating container \"{2}\" for {1}.", getName(), testUser, containerName);
        return getContainerModel(testUser).create(containerName);
    }

    /**
     * Create a container draft.
     * 
     * @param container
     *            A container.
     * @return A container draft.
     */
    protected ContainerDraft createContainerDraft(
            final OpheliaTestUser testUser, final Container container) {
        return createContainerDraft(testUser, container.getId());
    }

    /**
     * Create a container draft.
     * 
     * @param containerId
     *            A container id.
     * @return A container draft.
     */
    protected ContainerDraft createContainerDraft(
            final OpheliaTestUser testUser, final Long containerId) {
        return getContainerModel(testUser).createDraft(containerId);
    }

    /**
     * Create a document.
     * 
     * @param inputFile
     *            An input file.
     * @return A document.
     * @throws IOException
     * @throws ParityException
     */
    protected Document createDocument(final OpheliaTestUser testUser,
            final File inputFile) throws IOException {
        return getDocumentModel(testUser).create(inputFile.getName(),
                new FileInputStream(inputFile));
    }

    /**
     * Create a document version.
     * 
     * @param document
     *            A document.
     * @return A version.
     */
    protected DocumentVersion createDocumentVersion(
            final OpheliaTestUser testUser, final Document document) {
        return getDocumentModel(testUser).createVersion(document.getId());
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#createFailMessage(java.lang.Throwable)
     * 
     */
	protected String createFailMessage(Throwable t) {
		testLogger.error("Failure", t);
		return super.createFailMessage(t);
	}

    protected InternalArtifactModel getArtifactModel(
            final OpheliaTestUser testUser) {
        return ArtifactModel.getInternalModel(context, testUser.getWorkspace());
    }

    protected InternalContactModel getContactModel(final OpheliaTestUser testUser) {
        return ContactModel.getInternalModel(context, testUser.getWorkspace());
    }

    protected InternalArchiveModel getArchiveModel(final OpheliaTestUser testUser) {
        return ArchiveModel.getInternalModel(context, testUser.getWorkspace());
    }

    protected InternalContainerModel getContainerModel(final OpheliaTestUser testUser) {
        return ContainerModel.getInternalModel(context, testUser.getWorkspace());
    }

    protected InternalDocumentModel getDocumentModel(final OpheliaTestUser testUser) {
        return DocumentModel.getInternalModel(context, testUser.getWorkspace());
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
	 * @see com.thinkparity.codebase.junitx.TestCase#getInputFiles()
	 * 
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[5];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, 5);
		return inputFiles;
	}

    protected InternalLibraryModel getLibraryModel(final Workspace workspace) {
        return LibraryModel.getInternalModel(context, workspace);
    }

    protected InternalSystemMessageModel getMessageModel(final Workspace workspace) {
        return SystemMessageModel.getInternalModel(context, workspace);
    }

    protected File[] getModFiles() throws IOException {
        final File[] modFiles = new File[5];
        System.arraycopy(super.getModFiles(), 0, modFiles, 0, 5);
        return modFiles;
    }

    protected InternalProfileModel getProfileModel(
            final OpheliaTestUser testUser) {
        return ProfileModel.getInternalModel(context, testUser.getWorkspace());
    }

	protected InternalReleaseModel getReleaseModel(
            final OpheliaTestUser testUser) {
        return ReleaseModel.getInternalModel(context, testUser.getWorkspace());
    }

	protected InternalSessionModel getSessionModel(
            final OpheliaTestUser testUser) {
        return SessionModel.getInternalModel(context, testUser.getWorkspace());
    }

    protected InternalUserModel getUserModel(final OpheliaTestUser testUser) {
        return UserModel.getInternalModel(context, testUser.getWorkspace());
    }

    /**
	 * Obtain a handle to the parity workspace model.
	 * 
	 * @return A handle to the parity workspace model.
	 */
	protected WorkspaceModel getWorkspaceModel() {
		return WorkspaceModel.getModel();
	}

    /**
     * Determine if the session model is currently logged in.
     * 
     * @return True if it is.
     */
    protected Boolean isLoggedIn(final OpheliaTestUser testUser) {
        return getSessionModel(testUser).isLoggedIn();
    }

    /**
	 * Determine if the parity error is generated by the model due to operating
	 * system constraints.
	 * 
	 * @param px
	 *            The parity error.
	 * @return True if the error is caused by a NotYetImplementedAssertion and
	 *         the current operating system is linux.
	 */
	protected boolean isNYIAOnLinux(final Throwable t) {
		Throwable cause = t.getCause();
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
    protected void login(final OpheliaTestUser testUser) {
        logTrace("{0} - Logging in as {1}.", getName(), testUser);
        if(isLoggedIn(testUser)) {
            logWarning("{0} - User {1} already logged in.", testUser);
            logout(testUser);
        }
    	getSessionModel(testUser).login(testUser.getCredentials());
    }

    /**
     * Terminate an existing session.
     *
     */
    protected void logout(final OpheliaTestUser testUser) {
        getSessionModel(testUser).logout();
    }

    /**
     * Modify a document.
     * 
     * @param document
     *            A document.
     * @throws FileNotFoundException
     * @throws IOException
     * @see DocumentModel#updateDraft(Long, InputStream)
     */
    protected void modifyDocument(final OpheliaTestUser testUser,
            final Document document) throws FileNotFoundException, IOException {
        final String prefix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
        final String suffix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
        final File tempFile = File.createTempFile(prefix, suffix);
        tempFile.deleteOnExit();

        FileUtil.writeBytes(tempFile,
                ("jUnit Test MOD " +
                DateUtil.format(DateUtil.getInstance(), DateImage.ISO)).getBytes());
        final InputStream content = new FileInputStream(tempFile);
        try {
            getDocumentModel(testUser).updateDraft(document.getId(), content);
        } finally {
            content.close();
        }
    }

    /**
     * Modify all of the documents in the container's draft.
     * 
     * @param container
     *            A container.
     */
    protected void modifyDocuments(final OpheliaTestUser testUser,
            final Container container) throws FileNotFoundException,
            IOException {
        final ContainerModel containerModel = getContainerModel(testUser);
        final ContainerDraft draft = containerModel.readDraft(container.getId());
        for(final Document document : draft.getDocuments()) {
            modifyDocument(testUser, document);
        }
    }

    /**
     * Publish the container.
     * 
     * @param container
     *            The container.
     */
    protected void publish(final OpheliaTestUser testUser,
            final Container container) {
        final List<TeamMember> teamMembers = Collections.emptyList();
        final List<Contact> contacts = readContacts(testUser);
        logTrace("{0} - Publishing container {1} to contacts {2} and team members {3}.",
                getName(), container.getName(), contacts, teamMembers);
        getContainerModel(testUser).publish(container.getId(), contacts, teamMembers);
    }

    /**
     * Share a container.
     * 
     * @param container
     *            The container.
     */
    protected void share(final OpheliaTestUser testUser,
            final Container container, final ContainerVersion version) {
        final List<TeamMember> teamMembers = Collections.emptyList();
        final List<Contact> contacts = readContacts(testUser);
        getContainerModel(testUser).share(container.getId(),
                version.getVersionId(), contacts, teamMembers);
    }

    /**
     * Publish a container.
     * 
     * @param container
     *            A container.
     */
    protected void publishContainer(final OpheliaTestUser testUser,
            final Container container) {
        publish(testUser, container);
    }

    /**
     * Read the contacts.
     * 
     * @return A list of contacts.
     */
    protected List<Contact> readContacts(final OpheliaTestUser testUser) {
        return getContactModel(testUser).read();
    }

    /**
     * Read an artifact's team.
     * 
     * @param artifact
     *            An artifact.
     * @return A list of jabber ids.
     */
    protected List<JabberId> readTeam(final OpheliaTestUser testUser,
            final Artifact artifact) {
        final List<JabberId> team = new ArrayList<JabberId>();
        for(final User user : getArtifactModel(testUser).readTeam(
                artifact.getId())) {
            team.add(user.getId());
        }
        return team;
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
        super.setUp();
        this.context = TestModel.getModelContext();
	}

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
        super.tearDown();
        this.context = null;
	}
}
