/*
 * Created On: Aug 23, 2006 1:04:52 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

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
        final InternalContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        final Document document = addDocument(OpheliaTestUser.JUNIT, container, getInputFiles()[0]);
        login(OpheliaTestUser.JUNIT);
        publish(OpheliaTestUser.JUNIT, container);
        createContainerDraft(OpheliaTestUser.JUNIT, container);
        modifyDocument(OpheliaTestUser.JUNIT, document);
        logout(OpheliaTestUser.JUNIT);
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
