/*
 * Dec 19, 2005
 */
package com.thinkparity.ophelia.model.document;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model getVersion api.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadVersionTest extends DocumentTestCase {

    /** Test the read version api. */
    private static final String NAME = "Test read version.";

    /** Test datum <code>Fixture</code>. */
	private Fixture datum;

    /**
     * Create ReadVersionTest.
     *
     */
	public ReadVersionTest() {
        super(NAME);
    }

	/**
	 * Test the document model getVersion api.
	 *
	 */
	public void testGetVersion() {
        final Container c = createContainer(datum.junit, NAME);
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        final DocumentVersion dv_latest = getDocumentModel(datum.junit).readLatestVersion(d.getId());
        final DocumentVersion dv = getDocumentModel(datum.junit).readVersion(d.getId(), dv_latest.getVersionId());

        assertNotNull("Document version is null.", dv);
		assertEquals("Document version artifact id does not match expectation.", dv_latest.getArtifactId(), dv.getArtifactId());
		assertEquals("Document version artifact type does not match expectation.", dv_latest.getArtifactType(), dv.getArtifactType());
		assertEquals("Document version unique id does not match expectation.", dv_latest.getArtifactUniqueId(), dv.getArtifactUniqueId());
		assertEquals("Document version created by does not match expectation.", dv_latest.getCreatedBy(), dv.getCreatedBy());
		assertEquals("Document version created on does not match expectation.", dv_latest.getCreatedOn(), dv.getCreatedOn());
		assertEquals("Document version meta data does not match expectation.", dv_latest.getMetaData(), dv.getMetaData());
		assertEquals("Document version name does not match expectation.", dv_latest.getName(), dv.getName());
		assertEquals("Document version updated by does not match expectation.", dv_latest.getUpdatedBy(), dv.getUpdatedBy());
		assertEquals("Document version updated on does not match expectation.", dv_latest.getUpdatedOn(), dv.getUpdatedOn());
		assertEquals("Document version version id does not match expectation.", dv_latest.getVersionId(), dv.getVersionId());
	}

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X);
        login(datum.junit);
        login(datum.junit_x);
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends DocumentTestCase.Fixture {
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x) {
            this.junit = junit;
            this.junit_x = junit_x;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
        }
    }
}
