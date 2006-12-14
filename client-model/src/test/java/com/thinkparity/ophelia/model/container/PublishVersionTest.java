/*
 * Created On: Oct 20, 2006 08:30
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class PublishVersionTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Publish Version Test";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishVersionTest. */
    public PublishVersionTest() { super(NAME); }

    /** Test the publish version api. */
    public void testPublishVersion() {
        final Container c = createContainer(datum.junit, NAME);
        addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
        publish(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        datum.addListener(datum.junit);
        publishVersion(datum.junit, c.getId(), cv_latest.getVersionId(), "JUnit.Y thinkParity");
        datum.waitForEvents();
        datum.removeListener(datum.junit);
        assertTrue("The draft published event was not fired.", datum.didNotify);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X,
                OpheliaTestUser.JUNIT_Y);
        login(datum.junit);
        login(datum.junit_x);
        login(datum.junit_y);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        logout(datum.junit_y);
        datum = null;
        super.tearDown();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private OpheliaTestUser junit;
        private OpheliaTestUser junit_x;
        private OpheliaTestUser junit_y;
        private Boolean didNotify;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.didNotify = Boolean.FALSE;
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
        private void addListener(final OpheliaTestUser addAs) {
            getContainerModel(addAs).addListener(this);
        }
        private void removeListener(final OpheliaTestUser removeAs) {
            getContainerModel(removeAs).removeListener(this);
        }
        @Override
        public void containerUpdated(final ContainerEvent e) {}
        @Override
        public void draftPublished(ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
    }
}
