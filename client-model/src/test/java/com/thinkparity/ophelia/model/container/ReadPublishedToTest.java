/*
 * Created On: Sep 26, 2006 8:52:08 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Map;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadPublishedToTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Read Published To Test";

    private Fixture datum;

    /** Create ReadPublishedToTest. */
    public ReadPublishedToTest() {
        super(NAME);
    }

    /**
     * Test the read published to api.
     *
     */
    public void testReadPublishedTo() {
        final Container c = createContainer(datum.junit, NAME);
        addDocuments(datum.junit, c.getId());
        publish(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity");
        datum.waitForEvents();
        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());

        final Map<User, ArtifactReceipt> pt = readPublishedTo(datum.junit, c.getId(), cv_latest.getVersionId());
        assertNotNull(NAME + " - Published to user list is null.", pt);
        assertTrue(NAME + " - Published to list does not contain " + datum.junit_x.getSimpleUsername(), 
                USER_UTILS.containsKey(pt, datum.junit_x));
        assertTrue(NAME + " - Published to list does not contain " + datum.junit_y.getSimpleUsername(), 
                USER_UTILS.containsKey(pt, datum.junit_y));
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
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
    }
}