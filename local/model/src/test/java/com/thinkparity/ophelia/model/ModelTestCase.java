/*
 * Created On: Aug 6, 2005
 */
package com.thinkparity.ophelia.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.DateUtil.DateImage;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotYetImplementedAssertion;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.document.DocumentVersionContent;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.audit.HistoryItem;
import com.thinkparity.ophelia.model.backup.InternalBackupModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerHistoryItem;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.container.monitor.PublishMonitor;
import com.thinkparity.ophelia.model.container.monitor.PublishStage;
import com.thinkparity.ophelia.model.document.DocumentHistoryItem;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.message.InternalSystemMessageModel;
import com.thinkparity.ophelia.model.migrator.InternalLibraryModel;
import com.thinkparity.ophelia.model.migrator.InternalReleaseModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.script.InternalScriptModel;
import com.thinkparity.ophelia.model.session.DefaultLoginMonitor;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.UserUtils;
import com.thinkparity.ophelia.model.util.Opener;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;

import com.thinkparity.ophelia.OpheliaTestCase;
import com.thinkparity.ophelia.OpheliaTestModelFactory;
import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * ModelTestCase
 * Abstract root for all of the parity test cases.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ModelTestCase extends OpheliaTestCase {

    /** A set of <code>User</code> object utilities. */
    protected static final UserUtils USER_UTILS;

    /** Number of files to use from the total test input set. */
    private static final Integer NUMBER_OF_INPUT_FILES = 7;

    static {
        USER_UTILS = UserUtils.getInstance();
    }

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

    protected static <T extends Artifact> void assertContains(
            final List<T> list, final T o) {
        final StringBuffer actualIds = new StringBuffer();
        for (final T element : list) {
            if (element.getUniqueId().equals(o.getUniqueId())) {
                return;
            } else {
                if (0 < actualIds.length()) {
                    actualIds.append(",");
                }
                actualIds.append(element.getUniqueId());
            }
        }
        Assert.assertTrue(Boolean.FALSE, "expected:<{0}> but was:<{1}>", o.getUniqueId(), actualIds);
    }

    protected static <T extends Artifact> void assertDoesNotContain(
            final List<T> list, final T o) {
        for (final T element : list) {
            if (element.getUniqueId().equals(o.getUniqueId())) {
                Assert.assertTrue(Boolean.FALSE, "Did not expect:<{0}>", o.getUniqueId());
            }
        }
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

    protected static void assertNotNull(final String assertion, final ArtifactReceipt receipt) {
        assertNotNull(assertion + " [RECEIPT IS NULL]", (Object) receipt);
        assertNotNull(assertion + " [RECEIPT'S ARTIFACT ID IS NULL]", receipt.getArtifactId());
        assertNotNull(assertion + " [RECEIPT'S USER ID IS NULL]", receipt.getUserId());
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

    protected static void assertNotNull(final String assertion, final TeamMember teamMember) {
        assertNotNull(assertion + " [TEAM MEMBER IS NULL]", (Object) teamMember);
        assertNotNull(assertion + " [TEAM MEMBER's ARTIFACT ID IS NULL]", teamMember.getArtifactId());
        assertNotNull(assertion + " [TEAM MEMBER'S ID IS NULL]", teamMember.getId());
        assertNotNull(assertion + " [TEAM MEMBER'S LOCAL ID IS NULL]", teamMember.getLocalId());
        assertNotNull(assertion + " [TEAM MEMBER'S NAME IS NULL]", teamMember.getName());
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
    }

    /**
     * Assert that the expected container matches the actual; aside from the local id
     * and dates.  This api is used to compare containers across users.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected container.
     * @param actual
     *            The actual container.
     */
    protected static void assertSimilar(final String assertion, final Container expected, final Container actual) {
        assertEquals(assertion + " [CONTAINER'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [CONTAINER'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [CONTAINER'S REMOTE INFO'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedBy(), actual.getRemoteInfo().getUpdatedBy());
        assertEquals(assertion + " [CONTAINER'S STATE DOES NOT MATCH EXPECTATION]", expected.getState(), actual.getState());
        assertEquals(assertion + " [CONTAINER'S TYPE DOES NOT MATCH EXPECTATION]", expected.getType(), actual.getType());
        assertEquals(assertion + " [CONTAINER'S UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getUniqueId(), actual.getUniqueId());
        assertEquals(assertion + " [CONTAINER'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
    }

    /**
     * Assert the expected version matches the actual one; aside from an
     * artifact id and dates. This api is used to compare container versions
     * across users.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected container version.
     * @param actual
     *            The actual container version.
     */
    protected static void assertSimilar(final String assertion, final ContainerVersion expected, final ContainerVersion actual) {
        assertEquals(assertion + " [CONTAINER VERSION DOES NOT MATCH EXPECTATION]", (Object) expected, (Object) actual);
        assertEquals(assertion + " [CONTAINER VERSION'S ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getArtifactType(), actual.getArtifactType());
        assertEquals(assertion + " [CONTAINER VERSION'S ARTIFACT UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getArtifactUniqueId(), actual.getArtifactUniqueId());
        assertEquals(assertion + " [CONTAINER VERSION'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [CONTAINER VERSION'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [CONTAINER VERSION'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [CONTAINER VERSION'S VERSION ID DOES NOT MATCH EXPECTATION]", expected.getVersionId(), actual.getVersionId());
    }

    /**
     * Assert the expected document matches the actual; aside from the local id.
     * This api can be used to compare documents across users.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected document.
     * @param actual
     *            The actual document.
     */
    protected static void assertSimilar(final String assertion,
            final Document expected, final Document actual) {
        assertEquals(assertion + " [DOCUMENT DOES NOT MATCH EXPECTATION]", (Object) expected, (Object) actual);
        assertEquals(assertion + " [DOCUMENT'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [DOCUMENT'S FLAGS DO NOT MATCH EXPECTATION]", expected.getFlags(), actual.getFlags());
        assertEquals(assertion + " [DOCUMENT'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [DOCUMENT'S REMOTE INFO'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedBy(), actual.getRemoteInfo().getUpdatedBy());
        assertEquals(assertion + " [DOCUMENT'S STATE DOES NOT MATCH EXPECTATION]", expected.getState(), actual.getState());
        assertEquals(assertion + " [DOCUMENT'S TYPE DOES NOT MATCH EXPECTATION]", expected.getType(), actual.getType());
        assertEquals(assertion + " [DOCUMENT'S UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getUniqueId(), actual.getUniqueId());
        assertEquals(assertion + " [DOCUMENT'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
    }

    /**
     * Assert that the expected version matches the actual one; aside from the artifact ids
     * and dates.  This is used to compare document versions across users.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected version.
     * @param actual
     *            The actual version.
     */
    protected static void assertSimilar(final String assertion, final DocumentVersion expected, final DocumentVersion actual) {
        assertEquals(assertion + " [ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getArtifactType(), actual.getArtifactType());
        assertEquals(assertion + " [ARTIFACT UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getArtifactUniqueId(), actual.getArtifactUniqueId());
        assertEquals(assertion + " [CHECKSUM DOES NOT MATCH EXPECTATION]", expected.getChecksum(), actual.getChecksum());
        assertEquals(assertion + " [COMPRESSION DOES NOT MATCH EXPECTATION]", expected.getCompression(), actual.getCompression());
        assertEquals(assertion + " [CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [ENCODING DOES NOT MATCH EXPECTATION]", expected.getEncoding(), actual.getEncoding());
        assertEquals(assertion + " [NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [VERSION ID DOES NOT MATCH EXPECTATION]", expected.getVersionId(), actual.getVersionId());
    }

    /** A test model factory. */
    private OpheliaTestModelFactory modelFactory;

    /** A collection of user utility functions. */
    private final UserUtils userUtils;

    /**
	 * Create a ModelTestCase
	 * 
	 * @param name
	 *            The test name.
	 */
	protected ModelTestCase(final String name) {
        super(name);
        this.userUtils = UserUtils.getInstance();
	}

    /**
     * Add a document.
     * 
     * @param addAs
     *            The user to add the document as.
     * @param localContainerId
     *            A local container id <code>Long</code> relative to addAs.
     * @param filename
     *            A filename <code>String</code>.
     * @return A <code>Document</code>.
     */
    protected Document addDocument(final OpheliaTestUser addAs,
            final Long localContainerId, final String filename) {
        final File file = getInputFile(filename);
        final Container c = getContainerModel(addAs).read(localContainerId);
        logger.logInfo("Creating document from \"{0}\" for container \"{1}\" as \"{2}.\"",
                file.getName(), c.getName(), addAs.getSimpleUsername());
        final Document document = createDocument(addAs, file);
        logger.logVariable("document", document);
        logger.logInfo("Adding document \"{0}\" to container \"{1}\" as \"{2}.\"",
                document.getName(), c.getName(),
                addAs.getSimpleUsername());
        getContainerModel(addAs).addDocument(c.getId(), document.getId());
        return logger.logVariable("document", document);
    }

    /**
     * Add all of the input test files to the container as documents.
     * 
     * @param addAs
     *            An <code>OpheliaTestUser</code> to add the container as.
     * @param localContainerId
     *            A container id <code>Long</code> local to addAs.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    protected List<Document> addDocuments(final OpheliaTestUser addAs,
            final Long localContainerId) {
        final List<Document> documents = new ArrayList<Document>();
        for(final String inputFileName : getInputFileNames()) {
            documents.add(addDocument(addAs, localContainerId, inputFileName));
        }
        return documents;
    }

    /**
     * Add documents.
     * 
     * @param addAs
     *            An <code>OpheliaTestUser</code> to add the container as.
     * @param localContainerId
     *            A container id <code>Long</code> local to addAs.
     * @return An optional <code>List</code> of file name <code>String</code>s.
     */
    protected List<Document> addDocuments(final OpheliaTestUser addAs,
            final Long localContainerId, final String... filenames) {
        final List<Document> documents = new ArrayList<Document>(filenames.length);
        for(final String filename : filenames) {
            documents.add(addDocument(addAs, localContainerId, filename));
        }
        return documents;
    }

    /**
     * Archive a container.
     * 
     * @param archiveAs
     *            An <code>OpheliaTestUser</code> to publish as.
     * @param localContainerId
     *            A container id <code>Long</code> local to archiveAs.
     */
    protected void archive(final OpheliaTestUser archiveAs, final Long localContainerId) {
        final Container container = getContainerModel(archiveAs).read(localContainerId);
        logger.logInfo("Archiving container \"{0}\" as \"{1}\".", container,
                archiveAs.getSimpleUsername());
        getContainerModel(archiveAs).archive(localContainerId);
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
     * @param createAs
     * An <code>OpheliaTestUser</code> to create the container as.
     * @param name
     *            The container name.
     * @return The container.
     */
    protected Container createContainer(final OpheliaTestUser createAs,
            final String name) {
        final String actualName = new StringBuffer(name)
            .append(" - ").append(toGMTISO()).toString();
        logger.logInfo("Creating container \"{0}\" as \"{1}.\"", actualName,
                createAs.getSimpleUsername());
        return logger.logVariable("container", getContainerModel(createAs).create(actualName));
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
            final File inputFile) {
        try {
            return getDocumentModel(testUser).create(inputFile.getName(),
                    new FileInputStream(inputFile));
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        }
    }

    protected Document createDocument(final OpheliaTestUser testUser, 
            final String filename) {
        logger.logInfo("Creating document \"{0}\".", filename);
        try {
            return getDocumentModel(testUser).create(filename,
                new FileInputStream(getInputFile(filename)));
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        }
    }

    protected List<Document> createDocuments(final OpheliaTestUser createAs,
            final String... filenames) {
        final List<Document> documents = new ArrayList<Document>(filenames.length);
        for(final String filename : filenames) {
            documents.add(createDocument(createAs, filename));
        }
        return documents;

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
     * Create a draft.
     * 
     * @param testUser
     *            A <code>OpheliaTestUser</code>.
     * @param container
     *            A <code>Container</code>.
     * @return A <code>ContainerDraft</code>.
     */
    protected ContainerDraft createDraft(final OpheliaTestUser createAs,
            final Long localContainerId) {
        final Container c = getContainerModel(createAs).read(localContainerId);
        logger.logInfo("Creating draft for container \"{0}\" as \"{1}.\"",
                c.getName(), createAs.getSimpleUsername());
        return getContainerModel(createAs).createDraft(localContainerId);
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#createFailMessage(java.lang.Throwable)
     * 
     */
	protected String createFailMessage(final Throwable t) {
		logger.logFatal(t, "Test {0} failed.", getName());
		return super.createFailMessage(t);
	}

    /**
     * Delete a container.
     * @param deleteAs
     * @param localContainerId
     */
    protected void deleteContainer(final OpheliaTestUser deleteAs,
            final Long localContainerId) {
        final Container container = getContainerModel(deleteAs).read(localContainerId);
        logger.logInfo("Deleting container \"{0}\" as \"{1}\".", container
                .getName(), deleteAs.getSimpleUsername());
        getContainerModel(deleteAs).delete(localContainerId);
    }

    /**
     * Create a draft.
     * 
     * @param testUser
     *            A <code>OpheliaTestUser</code>.
     * @param container
     *            A <code>Container</code>.
     * @return A <code>ContainerDraft</code>.
     */
    protected void deleteDraft(final OpheliaTestUser deleteAs,
            final Long localContainerId) {
        final Container c = getContainerModel(deleteAs).read(localContainerId);
        logger.logInfo("Deleting draft for container \"{0}\" as \"{1}.\"",
                c.getName(), deleteAs.getSimpleUsername());
        getContainerModel(deleteAs).deleteDraft(localContainerId);
    }

    /**
     * Export a container.
     * 
     * @param exportAs
     *            An <code>OpheliaTestUser</code> to export as.
     * @param localContainerId
     *            A container id <code>Long</code> local to exportAs.
     * @param exportTo
     *            A directory <code>File</code> to export to.
     */
    protected void exportContainer(final OpheliaTestUser exportAs,
            final Long localContainerId, final File exportTo) {
        final Container container = readContainer(exportAs, localContainerId);
        logger.logInfo("Exporting container \"{0}\" as \"{1}\" to \"{2}\".", container.getId(),
                exportAs.getSimpleUsername(), exportTo);
        getContainerModel(exportAs).export(exportTo, localContainerId);
    }

    /**
     * Export a container version.
     * 
     * @param exportAs
     *            An <code>OpheliaTestUser</code> to export as.
     * @param localContainerId
     *            A container id <code>Long</code> local to exportAs.
     * @param versionId
     *            A version id <code>Long</code>.
     * @param exportTo
     *            A directory <code>File</code> to export to.
     */
    protected void exportContainerVersion(final OpheliaTestUser exportAs,
            final Long localContainerId, final Long versionId,
            final File exportTo) {
        final Container container = readContainer(exportAs, localContainerId);
        logger.logInfo("Exporting container \"{0}\" version \"{1}\" as \"{2}\" to \"{3}\".",
                container.getId(), versionId, exportAs.getSimpleUsername(), exportTo);
        getContainerModel(exportAs).exportVersion(exportTo, localContainerId, versionId);
    }

    protected InternalArchiveModel getArchiveModel(final OpheliaTestUser testUser) {
        return modelFactory.getArchiveModel(testUser);
    }

    protected InternalArtifactModel getArtifactModel(
            final OpheliaTestUser testUser) {
        return modelFactory.getArtifactModel(testUser);
    }

    protected InternalBackupModel getBackupModel(final OpheliaTestUser testUser) {
        return modelFactory.getBackupModel(testUser);
    }

    protected InternalContactModel getContactModel(final OpheliaTestUser testUser) {
        return modelFactory.getContactModel(testUser);
    }

    protected InternalContainerModel getContainerModel(final OpheliaTestUser testUser) {
        return modelFactory.getContainerModel(testUser);
    }

    protected InternalDocumentModel getDocumentModel(final OpheliaTestUser testUser) {
        return modelFactory.getDocumentModel(testUser);
   }

    /**
	 * Obtain a single test file.
	 * 
	 * @param name
	 *            The file name.
	 * @return The test file.
	 */
	protected File getInputFile(final String name) {
        try {
            for(final File file : getInputFiles()) {
                if(file.getName().equals(name)) { return file; }
            }
            return null;
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        }
	}

    protected String[] getInputFileMD5Checksums() {
        final String[] inputFileMD5Checksums = new String[NUMBER_OF_INPUT_FILES];
        System.arraycopy(super.getInputFileMD5Checksums(), 0, inputFileMD5Checksums, 0, NUMBER_OF_INPUT_FILES);
        return inputFileMD5Checksums;
    }

    /**
     * @see com.thinkparity.codebase.junitx.TestCase#getInputFileNames()
     *
     */
    @Override
    protected String[] getInputFileNames() {
        final String[] inputFileNames = new String[NUMBER_OF_INPUT_FILES];
        System.arraycopy(super.getInputFileNames(), 0, inputFileNames, 0, NUMBER_OF_INPUT_FILES);
        return inputFileNames;
    }

    /**
	 * @see com.thinkparity.codebase.junitx.TestCase#getInputFiles()
	 * 
	 */
	protected File[] getInputFiles() throws IOException {
		final File[] inputFiles = new File[NUMBER_OF_INPUT_FILES];
		System.arraycopy(super.getInputFiles(), 0, inputFiles, 0, NUMBER_OF_INPUT_FILES);
		return inputFiles;
	}

    protected InternalLibraryModel getLibraryModel(
            final OpheliaTestUser testUser) {
        return modelFactory.getLibraryModel(testUser);
    }

    protected InternalSystemMessageModel getMessageModel(
            OpheliaTestUser testUser) {
        return modelFactory.getSystemMessageModel(testUser);
    }

    protected File[] getModFiles() throws IOException {
        final File[] modFiles = new File[NUMBER_OF_INPUT_FILES];
        System.arraycopy(super.getModFiles(), 0, modFiles, 0, NUMBER_OF_INPUT_FILES);
        return modFiles;
    }

    protected InternalProfileModel getProfileModel(
            final OpheliaTestUser testUser) {
        return modelFactory.getProfileModel(testUser);
    }

    protected InternalReleaseModel getReleaseModel(
            final OpheliaTestUser testUser) {
        return modelFactory.getReleaseModel(testUser);
    }

    protected InternalScriptModel getScriptModel(final OpheliaTestUser testUser) {
        return modelFactory.getScriptModel(testUser);
    }

    protected InternalSessionModel getSessionModel(
            final OpheliaTestUser user) {
        return modelFactory.getSessionModel(user);
    }

	protected InternalUserModel getUserModel(final OpheliaTestUser testUser) {
        return modelFactory.getUserModel(testUser);
    }

	/**
	 * Obtain a handle to the parity workspace model.
	 * 
	 * @return A handle to the parity workspace model.
	 */
	protected WorkspaceModel getWorkspaceModel(final OpheliaTestUser testUser) {
		return modelFactory.getWorkspaceModel(testUser);
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
					logger.logWarning("Running test on un-supported platform.");
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
        if (isLoggedIn(testUser)) {
            logWarning("{0} - User {1} already logged in.", testUser);
            logout(testUser);
        }
        logger.logInfo("Logging in user \"{0}.\"", testUser.getSimpleUsername());
        getSessionModel(testUser).login(new DefaultLoginMonitor() {
            @Override
            public Boolean confirmSynchronize() {
                return Boolean.TRUE;
            }
    	}, testUser.getCredentials());
    }

    /**
     * Terminate an existing session.
     *
     */
    protected void logout(final OpheliaTestUser testUser) {
        logger.logInfo("Logging out user \"{0}.\"", testUser.getSimpleUsername());
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
     * @return The file that contains the new content.
     */
    protected File modifyDocument(final OpheliaTestUser modifyAs,
            final Long localDocumentId) {
        try {
            final Document document = getDocumentModel(modifyAs).read(localDocumentId);
            logger.logInfo("Modifying document \"{0}\" as \"{1}.\"",
                    document.getName(), modifyAs.getSimpleUsername());
            final String prefix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
            final String suffix = DateUtil.format(DateUtil.getInstance(), DateImage.FileSafeDateTime);
            final File tempFile = File.createTempFile(prefix, suffix);
            tempFile.deleteOnExit();
    
            FileUtil.writeBytes(tempFile,
                    ("jUnit Test MOD " +
                    DateUtil.format(DateUtil.getInstance(), DateImage.ISO)).getBytes());
            final InputStream content = new FileInputStream(tempFile);
            try {
                getDocumentModel(modifyAs).updateDraft(localDocumentId, content);
            } finally {
                content.close();
            }
            return tempFile;
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        }
    }

    /**
     * Modify all of the documents in the container's draft.
     * 
     * @param container
     *            A container.
     */
    protected void modifyDocuments(final OpheliaTestUser testUser,
            final Container container) {
        final ContainerModel containerModel = getContainerModel(testUser);
        final ContainerDraft draft = containerModel.readDraft(container.getId());
        for(final Document document : draft.getDocuments()) {
            modifyDocument(testUser, document.getId());
        }
    }

    /**
     * Open a document for a user.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localDocumentId
     *            A document id <code>Long</code> local to openAs.
     */
    protected void openDocument(final OpheliaTestUser openAs,
            final Long localDocumentId) {
        getDocumentModel(openAs).open(localDocumentId, new Opener() {
            public void open(final File file) {
                assertNotNull("File to open is null", file);
                assertTrue("File to open does not exist.", file.exists());
                assertTrue("File to open cannot be read.", file.canRead());
            }
        });
    }

    /**
     * Open a document version for a user.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localDocumentId
     *            A document id <code>Long</code> local to openAs.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    protected void openDocumentVersion(final OpheliaTestUser openAs,
            final Long localDocumentId, final Long versionId) {
        getDocumentModel(openAs).openVersion(localDocumentId, versionId,
                new Opener() {
                    public void open(final File file) {
                        assertNotNull("File to open is null", file);
                        assertTrue("File to open does not exist.", file.exists());
                        assertTrue("File to open cannot be read.", file.canRead());
                    }
        });
    }

    /**
     * Publish to a discrete set of team members.
     * 
     * @param publishAs
     *            The <code>OpheliaTestUser</code> to publish as.
     * @param localContainerId
     *            A container id <code>Long</code> local to publishAs.
     * @param teamMemberNames
     *            A <code>String</code> list of <code>TeamMember</code>
     *            names.
     */
    protected void publish(final OpheliaTestUser publishAs,
            final Long localContainerId, final String... userNames) {
        publishWithComment(publishAs, localContainerId, null, userNames);
    }

    /**
     * Publish a container version. By providing a list of user names a filtered
     * list of contacts and team members is created. Only users whose names
     * match that provided in the list are published to. If no names are
     * provided; all contacts and all team members are published to; note that
     * the publishAs user is never published to and that the contacts list will
     * not include any team members.
     * 
     * @param publishAs
     *            The <code>OpheliaTestUser</code> to publish as.
     * @param localContainerId
     *            A container id <code>Long</code> local to publishAs.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param userNames
     *            An optional list of user names to filter. The user name can be
     *            either a contact name or a team member name.
     */
    protected void publishVersion(final OpheliaTestUser publishAs,
            final Long localContainerId, final Long versionId,
            final String... userNames) {
        final List<TeamMember> teamMembers = readTeam(publishAs, localContainerId);
        final List<Contact> contacts = readContacts(publishAs);
        userUtils.filter(teamMembers, userNames);
        userUtils.filter(contacts, userNames);
        filter(contacts, teamMembers);
        remove(teamMembers, publishAs);
        final Container c = getContainerModel(publishAs).read(localContainerId);
        final String teamMemberNames = extractUserNames(teamMembers);
        final String contactNames = extractUserNames(contacts);
        if (0 < teamMembers.size()) {
            if (0 < contacts.size()) {
                logger.logInfo("Publishing container \"{0}\" version \"{4}\" as \"{1}\" to contacts \"{2}\" and team members \"{3}\".",
                        c.getName(), publishAs.getSimpleUsername(),
                        contactNames, teamMemberNames, versionId);
            } else {
                logger.logInfo("Publishing container \"{0}\" version \"{3}\" as \"{1}\" to team members \"{2}\".",
                        c.getName(), publishAs.getSimpleUsername(),
                        teamMemberNames, versionId);
            }
        } else {
            if (0 < contacts.size()) {
                logger.logInfo("Publishing container \"{0}\" version \"{3}\" as \"{1}\" to contacts \"{2}\".",
                        c.getName(), publishAs.getSimpleUsername(),
                        contactNames, versionId);
            } else {
                fail("Cannot publish to nobody.");
            }
        }
        getContainerModel(publishAs).publishVersion(new TestPublishMonitor(),
                localContainerId, versionId, contacts, teamMembers);
    }

    /**
     * Publish to a discrete set of team members.
     * 
     * @param publishAs
     *            The <code>OpheliaTestUser</code> to publish as.
     * @param localContainerId
     *            A container id <code>Long</code> local to publishAs.
     * @param comment
     *            A publish comment <code>String</code>.
     * @param teamMemberNames
     *            A <code>String</code> list of <code>TeamMember</code>
     *            names.
     */
    protected void publishWithComment(final OpheliaTestUser publishAs,
            final Long localContainerId, final String comment,
            final String... userNames) {
        final List<TeamMember> teamMembers = readTeam(publishAs, localContainerId);
        final List<Contact> contacts = readContacts(publishAs);
        if (0 < userNames.length) {
            userUtils.filter(teamMembers, userNames);
            userUtils.filter(contacts, userNames);
        }
        filter(contacts, teamMembers);
        remove(teamMembers, publishAs);
        final Container c = getContainerModel(publishAs).read(localContainerId);
        final String teamMemberNames = extractUserNames(teamMembers);
        final String contactNames = extractUserNames(contacts);
        if (0 < teamMembers.size()) {
            if (0 < contacts.size()) {
                logger.logInfo("Publishing container \"{0}\" as \"{1}\" to contacts \"{2}\" and team members \"{3}\".",
                        c.getName(), publishAs.getSimpleUsername(), contactNames,
                        teamMemberNames);
            } else {
                logger.logInfo("Publishing container \"{0}\" as \"{1}\" to team members \"{2}\".",
                        c.getName(), publishAs.getSimpleUsername(), teamMemberNames);
            }
        } else {
            if (0 < contacts.size()) {
                logger.logInfo("Publishing container \"{0}\" as \"{1}\" to contacts \"{2}\".",
                        c.getName(), publishAs.getSimpleUsername(), contactNames);
            } else {
                fail("Cannot publish to nobody.");
            }
        }
        getContainerModel(publishAs).publish(localContainerId, comment, contacts, teamMembers);
    }

    /**
     * Read a contacts.
     * 
     * @return A <code>Contacts</code>.
     */
    protected Contact readContact(final OpheliaTestUser testUser,
            final OpheliaTestUser contact) {
        return getContactModel(testUser).read(contact.getId());
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
     * Read a container for a user.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    protected Container readContainer(final OpheliaTestUser readAs,
            final Long localContainerId) {
        return getContainerModel(readAs).read(localContainerId);
    }

    /**
     * Read a container for a user.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>Container</code>.
     */
    protected Container readContainer(final OpheliaTestUser readAs,
            final UUID uniqueId) {
        final Long localContainerId = getArtifactModel(readAs).readId(uniqueId);
        if (null == localContainerId) {
            return null;
        } else {
            return readContainer(readAs, localContainerId);
        }
    }

    /**
     * Read a container draft for a user.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localContainerId
     *            A container id <code>Long</code> local to readAs.
     * @return A <code>Container</code>.
     */
    protected ContainerDraft readContainerDraft(final OpheliaTestUser readAs,
            final Long localContainerId) {
        return getContainerModel(readAs).readDraft(localContainerId);
    }

    /**
     * Read the latest container version.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localContainerId
     *            A container id <code>Long</code> local to readAs.
     * @return A <code>ContainerVersion</code>.
     */
    protected ContainerVersion readContainerLatestVersion(
            final OpheliaTestUser readAs, final Long localContainerId) {
        return getContainerModel(readAs).readLatestVersion(localContainerId);
    }

    /**
     * Read the previous container version.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localContainerId
     *            A container id <code>Long</code> local to readAs.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A <code>ContainerVersion</code>.
     */
    protected ContainerVersion readContainerPreviousVersion(
            final OpheliaTestUser readAs, final Long localContainerId,
            final Long versionId) {
        return getContainerModel(readAs).readPreviousVersion(localContainerId,
                versionId);
    }

    /**
     * Read the documents for a version.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localContainerId
     *            A container id <code>Long</code> local to readAs.
     * @param versionId
     *            A container version id.
     * @return A <code>List</code> of <code>Document</code>s.
     */
    protected List<Document> readContainerVersionDocuments(
            final OpheliaTestUser readAs, final Long localContainerId,
            final Long versionId) {
        final List<Document> documents = new ArrayList<Document>();
        documents.addAll(getContainerModel(readAs).readDocuments(
                localContainerId, versionId));
        return documents;
    }

    /**
     * Read the document versions for a version.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localContainerId
     *            A container id <code>Long</code> local to readAs.
     * @param versionId
     *            A container version id.
     * @return A <code>List</code> of <code>DocumentVersion</code>s.
     */
    protected List<DocumentVersion> readContainerVersionDocumentVersions(
            final OpheliaTestUser readAs, final Long localContainerId,
            final Long versionId) {
        final List<DocumentVersion> versions = new ArrayList<DocumentVersion>();
        versions.addAll(getContainerModel(readAs).readDocumentVersions(
                localContainerId, versionId));
        return versions;
    }

    protected List<ContainerVersion> readContainerVersions(
            final OpheliaTestUser readAs, final Long localContainerId) {
        return getContainerModel(readAs).readVersions(localContainerId);
    }

    /**
     * Read a document for a user.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param uniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>Document</code>.
     */
    protected Document readDocument(final OpheliaTestUser readAs,
            final UUID uniqueId) {
        final Long localId = getArtifactModel(readAs).readId(uniqueId);
        if (null == localId) {
            return null;
        } else {
            return getDocumentModel(readAs).read(localId);
        }
    }

    /**
     * Read a document version.
     * 
     * @param readAs
     *            An <code>OpheliaTestUser</code> to read as.
     * @param localDocumentId
     *            A local document id <code>Long</code> relative to readAs.
     * @return A <code>DocumentVersion</code>.
     */
    protected DocumentVersion readDocumentLatestVersion(
            final OpheliaTestUser readAs, final Long localDocumentId) {
        return getDocumentModel(readAs).readLatestVersion(localDocumentId);
    }

    protected Map<User, ArtifactReceipt> readPublishedTo(
            final OpheliaTestUser readAs, final Long localContainerId,
            final Long versionId) {
        return getContainerModel(readAs).readPublishedTo(localContainerId,
                versionId);
    }

    /**
     * Read a team for an artifact.
     * 
     * @param readAs
     *            The <code>OpheliaTestUser</code> to read as.
     * @param localArtifactId
     *            The artifact id <code>Long</code> local to readAs.
     * @return A <code>List</code> of <code>TeamMember</code>s.
     */
    protected List<TeamMember> readTeam(final OpheliaTestUser readAs,
            final Long localArtifactId) {
        final InternalArtifactModel artifactModel = getArtifactModel(readAs);
        return artifactModel.readTeam2(localArtifactId);
    }

    /**
     * Read the team user ids for an artifact.
     * 
     * @param readAs
     *            The <code>OpheliaTestUser</code> to read as.
     * @param localArtifactId
     *            The artifact id <code>Long</code> local to readAs.
     * @return A <code>List</code> of <code>JabberId</code>s.
     */
    protected List<JabberId> readTeamIds(final OpheliaTestUser readAs,
            final Long localArtifactId) {
        final List<JabberId> teamIds = new ArrayList<JabberId>();
        for(final TeamMember teamMember : readTeam(readAs, localArtifactId)) {
            teamIds.add(teamMember.getId());
        }
        return teamIds;
    }

    /**
     * Remove a document.
     * 
     * @param removeAs
     *            The user to remove the document as.
     * @param localContainerId
     *            A local container id <code>Long</code> relative to removeAs.
     * @param localDocumentId
     *            A local document id <code>Long</code> relative to removeAs.
     */
    protected void removeDocument(final OpheliaTestUser removeAs,
            final Long localContainerId, final Long localDocumentId) {
        final Document document = getDocumentModel(removeAs).read(localDocumentId);
        final Container container = getContainerModel(removeAs).read(localContainerId);
        logger.logInfo("Removing document \"{0}\" from container \"{1}\" as \"{2}.\"",
                document.getName(), container.getName(), removeAs.getSimpleUsername());
        getContainerModel(removeAs).removeDocument(localContainerId, localDocumentId);
    }

    protected void removeDocuments(final OpheliaTestUser removeAs,
            final Long localContainerId, final String... names) {
        final ContainerDraft draft = getContainerModel(removeAs).readDraft(localContainerId);
        final List<Document> documents = new ArrayList<Document>();
        documents.addAll(draft.getDocuments());
        FilterManager.filter(documents, new Filter<Document>() {
            public Boolean doFilter(final Document o) {
                for (final String name : names)
                    if (name.equals(o.getName()))
                        return false;
                return true;
            }
        });
        final Container container = getContainerModel(removeAs).read(localContainerId);
        for (final Document document : documents) {
            logger.logInfo("Removing document \"{0}\" from container \"{1}\" as \"{2}.\"",
                    document.getName(), container.getName(), removeAs.getSimpleUsername());
            getContainerModel(removeAs).removeDocument(localContainerId, document.getId());
        }
    }

    protected void renameContainer(final OpheliaTestUser renameAs,
            final Long localContainerId, final String renameTo) {
        final Container container = readContainer(renameAs, localContainerId);
        logger.logInfo("Renaming container \"{0}\" as \"{1}\" to \"{2}\".",
                container.getName(), renameAs.getSimpleUsername(), renameTo);
        getContainerModel(renameAs).rename(localContainerId, renameTo);
    }

    /**
     * Restore a container.
     * 
     * @param restoreAs
     *            An <code>OpheliaTestUser</code> to restore as.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     */
    protected void restore(final OpheliaTestUser restoreAs, final UUID uniqueId) {
        logger.logInfo("Restoring container \"{0}\" as \"{1}\".", uniqueId,
                restoreAs.getSimpleUsername());
        getContainerModel(restoreAs).restore(uniqueId);
    }

    protected void revertDocument(final OpheliaTestUser revertAs,
            final Long localContainerId, final Long localDocumentId) {
        final Document document = getDocumentModel(revertAs).read(localDocumentId);
        final Container container = getContainerModel(revertAs).read(localContainerId);
        logger.logInfo("Reverting document \"{0}\" for container \"{1}\" as \"{2}.\"",
                document.getName(), container.getName(), revertAs.getSimpleUsername());
        getContainerModel(revertAs).revertDocument(localContainerId, localDocumentId);
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 * 
	 */
	protected void setUp() throws Exception {
        super.setUp();
        this.modelFactory = OpheliaTestModelFactory.getInstance(this);
	}

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
        super.tearDown();
        this.modelFactory = null;
	}

    /**
     * Extract a separated list of names from a list of users.
     * 
     * @param <T>
     *            A <code>User</code> type.
     * @param users
     *            A <code>List</code> of <code>User</code>s.
     * @return A separated list of <code>User</code>s' names.
     */
    private <T extends User> String extractUserNames(final List<T> users) {
        final StringBuffer buffer = new StringBuffer();
        for (final T user : users) {
            if (0 < buffer.length())
                buffer.append(Separator.SemiColon);
            buffer.append(user.getName());
        }
        return buffer.toString();
    }

    /**
     * Filter one list of users with a second.
     * 
     * @param <T>
     *            A <code>User</code> type.
     * @param <U>
     *            A <code>User</code> type.
     * @param users
     *            A list of <code>User</code>s.
     * @param filterUsers
     *            A list of <code>User</code>s to filter out of users.
     */
    private <T extends User, U extends User> void filter(final List<T> users,
            final List<U> filterUsers) {
        T user;
        for (final Iterator<T> iUsers = users.iterator(); iUsers.hasNext();) {
            user = iUsers.next();
            for (final U filterUser : filterUsers)
                if (user.getId().equals(filterUser.getId()))
                    iUsers.remove();
        }
    }

    private void remove(final List<? extends User> users, final User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.remove(i);
                break;
            }
        }
    }

    public final class TestPublishMonitor implements PublishMonitor {
        
        public TestPublishMonitor() {
            super();
        }

        public void determine(final Integer stages) {
            logger.logTraceId();
            logger.logVariable("stages", stages);
        }
        public void processBegin() {
            logger.logTraceId();
        }
        public void processEnd() {
            logger.logTraceId();
        }
        public void stageBegin(final PublishStage stage, final Object data) {
            logger.logTraceId();
            logger.logVariable("stage", stage);
            logger.logVariable("data", data);
        }
        public void stageEnd(final PublishStage stage) {
            logger.logTraceId();
            logger.logVariable("stage", stage);
        }
    }

    /**
     * <b>Title:</b>thinkParity Model Test Case Fixture<br>
     * <b>Description:</b>Provides some helpful fixture functionality in terms
     * of tracking remote events for a user.<br>
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    protected abstract class Fixture {
        /** A <code>List</code> of <code>FixtureQueueHelper</code>s for users. */
        private final Map<OpheliaTestUser, FixtureQueueHelper> queueHelpers;
        /**
         * Create Fixture.
         *
         */
        protected Fixture() {
            super();
            this.queueHelpers = new HashMap<OpheliaTestUser, FixtureQueueHelper>();
        }
        /**
         * Block until all queue events have been processed.
         *
         */
        public void waitForEvents() {
            logger.logInfo("Waiting for events.");
            for (final OpheliaTestUser testuser : queueHelpers.keySet()) {
                waitForEvents(testuser);
            }
        }
        /**
         * Block until all events for a user have been processed.
         * 
         * @param user
         *            A <code>User</code>.
         */
        public void waitForEvents(final OpheliaTestUser testuser) {
            queueHelpers.get(testuser).waitForEvents();
        }
        /**
         * Add a queue event monitor for the test user.
         * 
         * @param testUser
         *            An <code>OpheliaTestUser</code>.
         */
        protected void addQueueHelper(final OpheliaTestUser testUser) {
            Assert.assertNotTrue(queueHelpers.containsKey(testUser),
                    "Cannot add a second queue processor for user {0}.",
                    testUser);
            queueHelpers.put(testUser, new FixtureQueueHelper(testUser));
        }
    }

    /**
     * <b>Title:</b>thinkParity Model Test Case Fixture Queue Monitor<br>
     * <b>Description:</b>A queue monitor for model test case fixtures. Has the
     * ability to monitor the local queue of remote events and block until those
     * events have been processed.<br>
     * 
     * @author raymond@thinkparity.com
     * @version 1.1.2.1
     */
    private final class FixtureQueueHelper {
        /** An <code>OpheliaTestUser</code>. */
        private final OpheliaTestUser testUser;
        /**
         * Create FixtureQueueHelper.
         * 
         * @param testUser
         *            An <code>OpheliaTestUser</code>.
         */
        private FixtureQueueHelper(final OpheliaTestUser testUser) {
            this.testUser = testUser;
        }
        /**
         * Block until all events for the monitor have been processed.
         *
         */
        private void waitForEvents() {
            final InternalSessionModel sessionModel = getSessionModel(testUser);
            if (sessionModel.isLoggedIn()) {
                while (0 < sessionModel.readQueueSize()) {
                    logger.logTrace("Waiting for events for user \"{0}.\"", testUser.getSimpleUsername());
                    synchronized (this) {
                        try {
                            wait(3 * 1000);
                        } catch (final InterruptedException ix) {
                            logger.logError(ix, "Could not wait.");
                        }
                    }
                }
                logger.logTrace("Processing queue for user \"{0}.\"", testUser.getSimpleUsername());
                sessionModel.processQueue();
            } else {
                logger.logWarning("User \"{0}\" is not logged in.", testUser.getSimpleUsername());
            }
        }
    }
}
