/*
 * Created On:  17-Dec-06 12:56:23 AM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishPostRemoveTest extends ContainerTestCase {

    /** Test name <code>String</code>. */
    private static final String NAME = "Publish post remove test.";

    /** Test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create PublishPostRemoveTest.
     *
     * @param name
     */
    public PublishPostRemoveTest() {
        super(NAME);
    }

    /**
     * Test publishing a package after the removal of a document.
     *
     */
    public void testPublish() {
        final Container c = createContainer(datum.junit, NAME);
        addDocuments(datum.junit, c.getId());
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();
        // ensure the document we want to remove exists
        ContainerVersion cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        List<Document> d_list = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());
        boolean didHit = false;
        for (final Document d : d_list) {
            if (d.getName().equals("JUnitTestFramework.txt")) {
                didHit = true;
                break;
            }
        }
        assertTrue("Document does not exist -> cannot be removed.", didHit);
        createDraft(datum.junit, c.getId());
        datum.waitForEvents();
        removeDocuments(datum.junit, c.getId(), "JUnitTestFramework.txt");
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        datum.waitForEvents();

        cv_latest = readContainerLatestVersion(datum.junit, c.getId());
        d_list = readContainerVersionDocuments(datum.junit, c.getId(), cv_latest.getVersionId());
        for (final Document d : d_list) {
            assertTrue("Document was not removed.",
                    !d.getName().equals("JUnitTestFramework.txt"));
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     * 
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
     * 
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
