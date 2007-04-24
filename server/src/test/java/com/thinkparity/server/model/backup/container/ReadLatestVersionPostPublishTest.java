/*
 * Created On: Jun 27, 2006 4:00:45 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Container Read Latest Version Test<br>
 * <b>Description:</b>thinkParity Container Read Latest Version Test
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReadLatestVersionPostPublishTest extends ContainerTestCase {

    /** Test test name. */
    private static final String NAME = "Read latest version post publish test.";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create ReadLatestVersionPostPublishTest.
     *
     */
    public ReadLatestVersionPostPublishTest() { super(NAME); }

    /**
     * Test the container model's read latest version api.
     * 
     */
    public void testReadLatestVersion() {
        final Container c = createContainer(datum.junit, NAME);
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        
        final ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        assertNotNull("Latest version is null.", cv_latest);
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