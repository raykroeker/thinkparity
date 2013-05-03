/*
 * Created On: Aug 23, 2006 1:04:52 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RenamePostPublishTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Rename post publish test.";

    /** Test datum. */
    private Fixture datum;

    /**
     * Create RenamePostPublishTest.
     *
     */
    public RenamePostPublishTest() { super(NAME); }

    /** Test the rename api. */
    public void testRename() {
        Container c = createContainer(datum.junit, NAME);
        final String c_name = c.getName();
        addDocuments(datum.junit, c.getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        try {
            renameContainer(datum.junit, c.getId(), NAME + "  Renamed.");
        } catch (final ThinkParityException tpx) {
            if (tpx.getCause().getMessage().equals(
                    "Container has already been distributed.")) {
            } else { 
                fail(createFailMessage(tpx));
            }
        }
        c = readContainer(datum.junit, c.getId());
        assertEquals("The container has been renamed.", c_name, c.getName());
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
