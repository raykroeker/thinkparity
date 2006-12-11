/*
 * Created On:  11-Dec-06 9:14:15 AM
 */
package com.thinkparity.ophelia.model.container.archive;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.container.ContainerTestCase;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ArchiveTestCase extends ContainerTestCase {

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
     * Assert the container versions are not null.
     * 
     * @param assertion
     *            An assertion.
     * @param containers
     *            A list of containers.
     */
    protected static void assertNotNull(final String assertion,
            final List<ContainerVersion> versions) {
        assertNotNull(assertion + " [VERSIONS IS NULL]", (Object) versions);
        for(final ContainerVersion version : versions) {
            assertNotNull(assertion, version);
        }
    }

    /**
     * Create ArchiveTestCase.
     *
     */
    protected ArchiveTestCase(final String name) {
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
