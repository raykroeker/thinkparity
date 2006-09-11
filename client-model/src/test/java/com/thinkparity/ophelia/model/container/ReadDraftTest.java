/*
 * Created On: Aug 23, 2006 11:36:42 AM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.InternalContainerModel;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadDraftTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST READ DRAFT";

    /** Test datum. */
    private Fixture datum;

    /** Create ReadDraftTest. */
    public ReadDraftTest() { super(NAME); }

    /** Test the read draft api. */
    public void testReadDraft() {
        final ContainerDraft draft = datum.containerModel.readDraft(datum.container.getId());
        assertNotNull(NAME + " DRAFT IS NULL", draft);
        assertEquals(NAME + " DRAFT DOES NOT MATCH EXPECTATION", datum.draft, draft);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        final ContainerDraft draft = containerModel.readDraft(container.getId());
        datum = new Fixture(container, containerModel, draft);
        datum.containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final InternalContainerModel containerModel;
        private final ContainerDraft draft;
        private Fixture(final Container container,
                final InternalContainerModel containerModel,
                final ContainerDraft draft) {
            this.container = container;
            this.containerModel = containerModel;
            this.draft = draft;
        }
    }
}
