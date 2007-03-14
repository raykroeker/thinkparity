/*
 * Created On:  11-Dec-06 9:43:53 AM
 */
package com.thinkparity.ophelia.model.container.backup;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerTestCase;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class BackupTestCase extends ContainerTestCase {

    protected static void assertEquals(final String assertion,
            final ArtifactReceipt expected, final ArtifactReceipt actual) {
        assertEquals(assertion + " [RECIEPT'S ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getReceivedOn(), actual.getReceivedOn());
        assertEquals(assertion + " [RECIEPT'S ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getUser(), actual.getUser());
    }

    protected static void assertEquals(final String assertion,
            final Container expected, final Container actual) {
        assertEquals(assertion + " [CONTAINER DOES NOT MATCH EXPECTATION]", (Object) expected, (Object) actual);
        assertEquals(assertion + " [CONTAINER'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [CONTAINER'S FLAGS DO NOT MATCH EXPECTATION]", expected.getFlags(), actual.getFlags());
        assertEquals(assertion + " [CONTAINER'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [CONTAINER'S REMOTE INFO'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedBy(), actual.getRemoteInfo().getUpdatedBy());
        assertEquals(assertion + " [CONTAINER'S STATE DOES NOT MATCH EXPECTATION]", expected.getState(), actual.getState());
        assertEquals(assertion + " [CONTAINER'S TYPE DOES NOT MATCH EXPECTATION]", expected.getType(), actual.getType());
        assertEquals(assertion + " [CONTAINER'S UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getUniqueId(), actual.getUniqueId());
        assertEquals(assertion + " [CONTAINER'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
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
        assertEquals(assertion + " [CONTAINER VERSION'S ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getArtifactType(), actual.getArtifactType());
        assertEquals(assertion + " [CONTAINER VERSION'S ARTIFACT UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getArtifactUniqueId(), actual.getArtifactUniqueId());
        assertEquals(assertion + " [CONTAINER VERSION'S CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [CONTAINER VERSION'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [CONTAINER VERSION'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [CONTAINER VERSION'S VERSION ID DOES NOT MATCH EXPECTATION]", expected.getVersionId(), actual.getVersionId());
    }

    protected static void assertNotNull(final String assertion, final ArtifactReceipt receipt) {
        assertNotNull(assertion + " [RECEIPT IS NULL]", (Object) receipt);
        assertNotNull(assertion + " [RECEIPT'S USER IS NULL]", receipt.getUser());
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
     * Assert that a container version is not null.
     * 
     * @param assertion
     *            The assertion.
     * @param version
     *            The container version.
     */
    protected static void assertNotNull(final String assertion, final ContainerVersion version) {
        assertNotNull(assertion + " [CONTAINER VERSION IS NULL]", (Object) version);
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
        assertEquals(assertion + " [DOCUMENT'S FLAGS DO NOT MATCH EXPECTATION]", expected.getFlags(), actual.getFlags());
        assertEquals(assertion + " [DOCUMENT'S NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [DOCUMENT'S REMOTE INFO'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getRemoteInfo().getUpdatedBy(), actual.getRemoteInfo().getUpdatedBy());
        assertEquals(assertion + " [DOCUMENT'S STATE DOES NOT MATCH EXPECTATION]", expected.getState(), actual.getState());
        assertEquals(assertion + " [DOCUMENT'S TYPE DOES NOT MATCH EXPECTATION]", expected.getType(), actual.getType());
        assertEquals(assertion + " [DOCUMENT'S UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getUniqueId(), actual.getUniqueId());
        assertEquals(assertion + " [DOCUMENT'S UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
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
        assertEquals(assertion + " [ARTIFACT TYPE DOES NOT MATCH EXPECTATION]", expected.getArtifactType(), actual.getArtifactType());
        assertEquals(assertion + " [ARTIFACT UNIQUE ID DOES NOT MATCH EXPECTATION]", expected.getArtifactUniqueId(), actual.getArtifactUniqueId());
        assertEquals(assertion + " [CHECKSUM DOES NOT MATCH EXPECTATION]", expected.getChecksum(), actual.getChecksum());
        assertEquals(assertion + " [CHECKSUM ALGORITHM DOES NOT MATCH EXPECTATION]", expected.getChecksumAlgorithm(), actual.getChecksumAlgorithm());
        assertEquals(assertion + " [CREATED BY DOES NOT MATCH EXPECTATION]", expected.getCreatedBy(), actual.getCreatedBy());
        assertEquals(assertion + " [NAME DOES NOT MATCH EXPECTATION]", expected.getName(), actual.getName());
        assertEquals(assertion + " [UPDATED BY DOES NOT MATCH EXPECTATION]", expected.getUpdatedBy(), actual.getUpdatedBy());
        assertEquals(assertion + " [VERSION ID DOES NOT MATCH EXPECTATION]", expected.getVersionId(), actual.getVersionId());
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
        assertNotNull(assertion + " [DOCUMENT VERSION ARTIFACT TYPE IS NULL]", version.getArtifactType());
        assertNotNull(assertion + " [DOCUMENT VERSION UNIQUE ID IS NULL]", version.getArtifactUniqueId());
        assertNotNull(assertion + " [DOCUMENT VERSION CHECKSUM IS NULL]", version.getChecksum());
        assertNotNull(assertion + " [DOCUMENT VERSION CHECKSUM ALGORITHM IS NULL]", version.getChecksumAlgorithm());
        assertNotNull(assertion + " [DOCUMENT VERSION CREATED BY IS NULL]", version.getCreatedBy());
        assertNotNull(assertion + " [DOCUMENT VERSION CREATED ON IS NULL]", version.getCreatedOn());
        assertNotNull(assertion + " [DOCUMENT VERSION NAME IS NULL]", version.getName());
        assertNotNull(assertion + " [DOCUMENT VERSION UPDATED ON IS NULL]", version.getUpdatedBy());
        assertNotNull(assertion + " [DOCUMENT VERSION UPDATED BY IS NULL]", version.getUpdatedOn());
        assertNotNull(assertion + " [DOCUMENT VERSION VERSION ID IS NULL]", version.getVersionId());
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
        assertNotNull(assertion + " [USER'S LOCAL ID IS NULL]", user.getLocalId());
        assertNotNull(assertion + " [USER'S NAME IS NULL]", user.getName());
    }

    /**
     * Create BackupTestCase.
     *
     * @param name
     */
    public BackupTestCase(String name) {
        super(name);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    protected abstract class Fixture extends ContainerTestCase.Fixture {}

}
