/*
 * Created On: Aug 23, 2006 1:04:52 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.Document;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RevertPostPublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "TEST REVERT POST PUBLISH";

    /** Test datum. */
    private Fixture datum;

    /** Create RevertPostPublishTest. */
    public RevertPostPublishTest() { super(NAME); }

    /** Test the rename api. */
    public void testRevert() {
        datum.containerModel.revertDocument(datum.container.getId(), datum.document.getId());
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final InternalContainerModel containerModel = getInternalContainerModel();
        final Container container = createContainer(NAME);
        final Document document = addDocument(container, getInputFiles()[0]);
        login();
        publish(container);
        createContainerDraft(container);
        modifyDocument(document);
        logout();
        datum = new Fixture(container, containerModel, document);
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
        private final Document document;
        private Fixture(final Container container,
                final InternalContainerModel containerModel, final Document document) {
            this.container = container;
            this.containerModel = containerModel;
            this.document = document;
        }
    }
}
