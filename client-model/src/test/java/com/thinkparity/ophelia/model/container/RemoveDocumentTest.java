/*
 * Created On: Jun 27, 2006 4:00:45 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.io.File;

import com.thinkparity.codebase.assertion.UnreachableCodeAssertion;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;
import com.thinkparity.ophelia.model.util.Opener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Remove Document Test<br>
 * <b>Description:</b>thinkParity Container Remove Document Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class RemoveDocumentTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "REMOVE ADDED DOCUMENT TEST";

    /** Test datum. */
    private Fixture datum;

    /** Create RemoveDocumentTest. */
    public RemoveDocumentTest() { super(NAME); }

    /**
     * Test removing an added document.
     *
     */
    public void testRemoveAdded() {
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_initial = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.doc");
        // remove
        removeDocument(datum.junit, c_initial.getId(), d_initial.getId());
        // ensure the doc is deleted
        final Document d = readDocument(datum.junit, d_initial.getUniqueId());
        assertNull("Document not removed.", d);
    }

    /**
     * Test removing a modified document from the first draft.
     *
     */
    public void testRemoveModifiedPrePublish() {
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_initial = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.doc");
        modifyDocument(datum.junit, d_initial.getId());
        // remove
        removeDocument(datum.junit, c_initial.getId(), d_initial.getId());
        // ensure the doc is removed
        final Document d = readDocument(datum.junit, d_initial.getUniqueId());
        assertNull("Document has not been deleted.", d);
    }

    /**
     * Test removing a modified document from a draft subsequent to a publish.
     *
     */
    public void testRemoveModifiedPostPublish() {
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_initial = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.doc");
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c_initial.getId());
        datum.waitForEvents();
        modifyDocument(datum.junit, d_initial.getId());
        // remove
        removeDocument(datum.junit, c_initial.getId(), d_initial.getId());
        // ensure the doc is removed
        final Document d = readDocument(datum.junit, d_initial.getUniqueId());
        assertNotNull("Document has been deleted.", d);
        final ContainerDraft draft = readContainerDraft(datum.junit, c_initial.getId());
        assertEquals("Document not removed.", draft.getState(d.getId()), ArtifactState.REMOVED);
        getDocumentModel(datum.junit).open(d.getId(), new Opener() {
            public void open(final File file) {
                assertFalse("Document not read-only.", file.canWrite());
            }
        });
    }

    /**
     * Test removing an unchanged document.
     *
     */
    public void testRemoveNone() {
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_initial = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.doc");
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c_initial.getId());
        datum.waitForEvents();
        // remove
        removeDocument(datum.junit, c_initial.getId(), d_initial.getId());
        // ensure the doc is removed
        final Document d = readDocument(datum.junit, d_initial.getUniqueId());
        assertNotNull("Document has been deleted.", d);
        final ContainerDraft draft = readContainerDraft(datum.junit, c_initial.getId());
        assertEquals("Document not removed.", draft.getState(d.getId()), ArtifactState.REMOVED);
        getDocumentModel(datum.junit).open(d.getId(), new Opener() {
            public void open(final File file) {
                assertFalse("Document not read-only.", file.canWrite());
            }
        });
    }

    /**
     * Test removing a removed document.
     *
     */
    public void testRemoveRemoved() {
        final Container c_initial = createContainer(datum.junit, NAME);
        final Document d_initial = addDocument(datum.junit, c_initial.getId(), "JUnitTestFramework.doc");
        publish(datum.junit, c_initial.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();
        createDraft(datum.junit, c_initial.getId());
        // remove
        removeDocument(datum.junit, c_initial.getId(), d_initial.getId());
        try {
            removeDocument(datum.junit, c_initial.getId(), d_initial.getId());
        } catch (final UnreachableCodeAssertion uca) {
            if (!"INVALID DRAFT DOCUMENT STATE".equals(uca.getMessage())) {
                fail("Remove assertion not raised.");
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X);
        login(datum.junit);
        login(datum.junit_x);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        datum = null;
        super.tearDown();
    }

    /** Test datum fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
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
