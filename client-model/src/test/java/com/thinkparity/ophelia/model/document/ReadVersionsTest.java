/*
 * Nov 10, 2005
 */
package com.thinkparity.ophelia.model.document;

import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * Test the document model listVersions api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public class ReadVersionsTest extends DocumentTestCase {
	
	/** Test name <code>String</code>. */
    private static final String NAME = "Read versions test";

	/** Test datum. */
	private Fixture datum;

    /**
     * Create ReadVersionsTest.
     *
     */
	public ReadVersionsTest() { super(NAME); }

    /**
	 * Test the document model listVersions api.
	 *
	 */
	public void testReadVersions() {
	    final Container c = createContainer(datum.junit, NAME);
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        saveDraft(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d.getId());
        saveDraft(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        List<DocumentVersion> dv_list = getDocumentModel(datum.junit).readVersions(d.getId());
        // ensure the list is valid
		assertNotNull("Document version list is null.", dv_list);
		assertEquals("Document version list size does not match expectation.", 3, dv_list.size());

		// ensure the versions are sorted in an ascending order
		int previousVersion = 0;
		int currentVersion;
		for (final DocumentVersion dv : dv_list) {
		    currentVersion = dv.getVersionId().intValue();
		    assertEquals("Current version id does not match expectation.",
                    previousVersion + 1, currentVersion);
		    previousVersion = dv.getVersionId().intValue();
		}

        // obtain a sorted list
		final Comparator<ArtifactVersion> comparator =
            new ComparatorBuilder().createVersionById(Boolean.FALSE);
		dv_list = getDocumentModel(datum.junit).readVersions(d.getId(), comparator);

		// ensure the list is valid
        assertNotNull("Document version list is null.", dv_list);
        assertEquals("Document version list size does not match expectation.", 3, dv_list.size());

        // ensure the versions are sorted in an descending order
		previousVersion = 4;
		for (final DocumentVersion dv : dv_list) {
            currentVersion = dv.getVersionId().intValue();
            assertEquals("Current version id does not match expectation.",
                    previousVersion - 1, currentVersion);
            previousVersion = dv.getVersionId().intValue();
		}
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
