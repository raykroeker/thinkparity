/*
 * Created On: Aug 23, 2006 12:05:03 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadVersionsPostPublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Read versions post publish test.";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create ReadVersionsPostPublishTest.
     *
     */
    public ReadVersionsPostPublishTest() {
        super(NAME);
    }

    public void testReadVersions() {
        final Container c = createContainer(datum.junit, NAME);
        addDocuments(datum.junit, c.getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        final List<ContainerVersion> cv_list = readContainerVersions(datum.junit, c.getId());
        assertNotNull("Container versions list is null.", cv_list);
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