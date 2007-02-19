/*
 * Created On:  16-Feb-07 5:16:06 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.assertion.NotTrueAssertion;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class SaveDraftTest extends ContainerTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "Save draft test.";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create SaveDraftTest.
     *
     */
    public SaveDraftTest() {
        super(NAME);
    }

    /**
     * Test saving a draft.
     *
     */
    public void testSaveFirstDraft() {
        final Container c = createContainer(datum.junit, getName());
        addDocuments(datum.junit, c.getId());

        // save the draft
        saveDraft(datum.junit, c.getId());
    }

    /**
     * Test saving a draft that does not exist.
     *
     */
    public void testSaveNonExistantDraft() {
        final Container c = createContainer(datum.junit, getName());
        deleteDraft(datum.junit, c.getId());

        try {
            saveDraft(datum.junit, c.getId());
        } catch (final NotTrueAssertion nta) {
            if (!"A local draft does not exist.".equals(nta.getMessage())) {
                fail("Save draft did not fail as expected:  {0}",
                        nta.getMessage());
            }
        }
    }

    /**
     * Test saving a second draft.
     *
     */
    public void testSaveSecondDraft() {
        final Container c = createContainer(datum.junit, getName());
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.png");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        final Document d_unknown = addDocument(datum.junit, c.getId(), "JUnitTestFramework.unknown");
        saveDraft(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        // create second draft
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        // try a save without any changes
        saveDraft(datum.junit, c.getId());
        // try a save after an add
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        saveDraft(datum.junit, c.getId());
        // try a save after a remove
        removeDocument(datum.junit, c.getId(), d_unknown.getId());
        saveDraft(datum.junit, c.getId());
        // try a save afer a mod
        modifyDocument(datum.junit, d.getId());
        saveDraft(datum.junit, c.getId());
        // try a save after a subsequent mod
        modifyDocument(datum.junit, d.getId());
        saveDraft(datum.junit, c.getId());
    }

    /**
     * Test saving a second draft.
     *
     */
    public void testSaveThirdDraft() {
        final Container c = createContainer(datum.junit, getName());
        final Document d = addDocument(datum.junit, c.getId(), "JUnitTestFramework");
        final Document d_doc = addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.png");
        final Document d_txt = addDocument(datum.junit, c.getId(), "JUnitTestFramework.txt");
        final Document d_unknown = addDocument(datum.junit, c.getId(), "JUnitTestFramework.unknown");
        saveDraft(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        // create second draft
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.odt");
        removeDocument(datum.junit, c.getId(), d_unknown.getId());
        modifyDocument(datum.junit, d.getId());
        saveDraft(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        // create third draft
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        // try a save without any changes
        saveDraft(datum.junit, c.getId());
        // try a save after an add
        saveDraft(datum.junit, c.getId());
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.pdf");
        // try a save after a remove
        removeDocument(datum.junit, c.getId(), d_txt.getId());
        saveDraft(datum.junit, c.getId());
        // try a save afer a mod
        modifyDocument(datum.junit, d_doc.getId());
        saveDraft(datum.junit, c.getId());
        // try a save after a subsequent mod
        modifyDocument(datum.junit, d_doc.getId());
        saveDraft(datum.junit, c.getId());
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT);
        login(datum.junit);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b>Save Draft Fixture<br>
     * <b>Description:</b><br>
     */
    private class Fixture extends ContainerTestCase.Fixture {
        private final OpheliaTestUser junit;
        private Fixture(final OpheliaTestUser junit) {
            super();
            this.junit = junit;
            this.addQueueHelper(junit);
        }
    }
}
