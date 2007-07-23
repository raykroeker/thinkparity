/*
 * Created On:  23-Jul-07 1:29:28 PM
 */
package com.thinkparity.ophelia.model.container;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Ophelia Model Version Seen Flag Test<br>
 * <b>Description:</b>Test application/removal of the version seen flag and the
 * approriate events.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class VersionSeenFlagTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "Version seen flag test.";

    /** The test datum. */
    private Fixture datum;

    /**
     * Create VersionSeenFlagTest.
     *
     * @param name
     */
    public VersionSeenFlagTest() {
        super(NAME);
    }

    /**
     * Test applying the seen flag and whether or not the:
     * <ul>
     * <li> Events are fired.
     * <li> Flag is applied.
     * 
     */
    public void testApplyFlag() {
        // create/publish
        final Container c = createContainer(datum.junit, getName());
        addDocuments(datum.junit, c.getId(), "JUnitTestFramework.odt");
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity", "JUnit.Z thinkParity");
        // read published version
        final ContainerVersion cv = readContainerLatestVersion(datum.junit, c.getId());

        // apply flag
        getContainerModel(datum.junit).addListener(datum.applyFlagListener);
        getContainerModel(datum.junit).applyFlagSeen(cv);
        getContainerModel(datum.junit).removeListener(datum.applyFlagListener);
        if (!datum.didFireFlagApplied) {
            fail("Apply flag event not fired.");
        }
        final ContainerVersion cv_after = getContainerModel(datum.junit).readVersion(cv.getArtifactId(), cv.getVersionId());
        if (!cv_after.isSeen()) {
            fail("Apply flag not persisted.");
        }
    }

    /**
     * Test removing the seen flag and whether or not the:
     * <ul>
     * <li> Events are fired.
     * <li> Flag is removed.
     * 
     */
    public void testRemoveFlag() {
        // create/publish
        final Container c = createContainer(datum.junit, getName());
        addDocuments(datum.junit, c.getId(), "JUnitTestFramework.odt");
        publishToUsers(datum.junit, c.getId(), "JUnit.X thinkParity",
                "JUnit.Y thinkParity", "JUnit.Z thinkParity");
        // read published version
        final ContainerVersion cv = readContainerLatestVersion(datum.junit, c.getId());
        getContainerModel(datum.junit).applyFlagSeen(cv);

        // remove flag
        getContainerModel(datum.junit).addListener(datum.removeFlagListener);
        getContainerModel(datum.junit).removeFlagSeen(cv);
        getContainerModel(datum.junit).removeListener(datum.removeFlagListener);
        if (!datum.didFireFlagRemoved) {
            fail("Remove flag event not fired.");
        }
        final ContainerVersion cv_after = getContainerModel(datum.junit).readVersion(cv.getArtifactId(), cv.getVersionId());
        if (cv_after.isSeen()) {
            fail("Remove flag not persisted.");
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        datum = new Fixture(OpheliaTestUser.JUNIT);
        login(datum.junit);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        datum = null;

        super.tearDown();
    }

    /** <b>Title:</b>Test Data Definition<br> */
    private class Fixture extends ContainerTestCase.Fixture {

        /** An apply flag listener. */
        private final ContainerListener applyFlagListener;

        /** Whether or not the apply flag event has been fired. */
        private boolean didFireFlagApplied;

        /** Whether or not the remove flag event has been fired. */
        private boolean didFireFlagRemoved;

        /** The junit <code>OpheliaTestUser</code>. */
        private OpheliaTestUser junit;

        /** An emove flag listener. */
        private final ContainerListener removeFlagListener;

        /**
         * Create Fixture.
         * 
         * @param junit
         *            An <code>OpheliaTestUser</code>.
         */
        private Fixture(final OpheliaTestUser junit) {
            super();
            this.applyFlagListener = new ContainerAdapter() {
                @Override
                public void containerVersionFlagSeenApplied(final ContainerEvent e) {
                    didFireFlagApplied = true;
                    assertNotNull("Container version is null.", e.getVersion());
                }
            };
            this.didFireFlagApplied = false;
            this.didFireFlagRemoved = false;
            this.junit = junit;
            this.removeFlagListener = new ContainerAdapter() {
                @Override
                public void containerVersionFlagSeenApplied(final ContainerEvent e) {
                    didFireFlagApplied = true;
                    assertNotNull("Container version is null.", e.getVersion());
                }
            };

            addQueueHelper(junit);
        }
    }
}
