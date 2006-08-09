/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import com.thinkparity.model.parity.api.events.ContainerEvent;



/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class PublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [TEST PUBLISH]";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishTest. */
    public PublishTest() { super(NAME); }

    /** Test the publish api. */
    public void testPublish() {
        datum.containerModel.publish(datum.container.getId());

        assertTrue(NAME + " [DRAFT PUBLISHED EVENT NOT FIRED]", datum.didNotify);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login();
        final ContainerModel containerModel = getContainerModel();
        final Container container = createContainer(NAME);
        addTeam(container);
        addDocuments(container);
        datum = new Fixture(container, containerModel);
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.model.parity.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout();
        super.tearDown();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private Fixture(final Container container,
                final ContainerModel containerModel) {
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
        }
        @Override
        public void draftPublished(ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
    }
}
