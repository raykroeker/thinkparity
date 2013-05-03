/*
 * Created On: Oct 20, 2006 08:30
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

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
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity");
        datum.waitForEvents();

        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        addContainerListener(datum.junit, datum.listener);
        publishVersionToUsers(datum.junit, c.getId(), cv_latest.getVersionId(), "JUnit.Y thinkParity");
        datum.waitForEvents();
        removeContainerListener(datum.junit, datum.listener);
        assertTrue("The draft published event was not fired.", datum.published);
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
        private boolean published;
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private final ContainerListener listener;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.listener = new ContainerAdapter() {
                @Override
                public void containerPublished(final ContainerEvent e) {
                    published = true;
                    assertNotNull("Container event is null.", e);
                    assertNotNull("Container event container is null.", e.getContainer());
                    assertNotNull("Container event version is null.", e.getVersion());
                    assertNotNull("Container event team member is null.", e.getTeamMember());
                }
            };
            this.published = false;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
    }
}
