/*
 * Created On: Jun 27, 2006 4:01:02 PM
 */
package com.thinkparity.ophelia.model.container;


import java.io.File;
import java.util.List;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.model.ModelTestCase;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;

/**
 * <b>Title:</b>thinkParity Container Test Abstraction<br>
 * <b>Description:</b>A thinkParity container test abstraction.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public abstract class ContainerTestCase extends ModelTestCase {

    /**
     * Assert that the expected draft matches the actual.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected draft.
     * @param actual
     *            The actual draft.
     */
    protected static void assertEquals(final String assertion,
            final ContainerDraft expected, final ContainerDraft actual) {
        assertEquals(assertion + " [CONTAINER DRAFT DOES NOT MATCH EXPECTATION]", (Object) expected, (Object) actual);
        assertEquals(assertion + " [CONTAINER DRAFT CONTAINER ID DOES NOT MATCH EXPECTATION]", expected.getContainerId(), actual.getContainerId());
    }

    /**
     * Assert the draft is not null.
     * 
     * @param assertion
     *            An assertion.
     * @param draft
     *            A draft.
     */
    protected static void assertNotNull(final String assertion,
            final ContainerDraft draft) {
        assertNotNull(assertion + " [DRAFT IS NULL]", (Object) draft);
        assertNotNull(assertion + " [DRAFT ID IS NULL]", draft.getContainerId());
        assertNotNull(assertion + " [DRAFT ARTIFACTS ARE IS NULL]", draft.getArtifacts());
        for(final Artifact artifact : draft.getArtifacts()) {
            assertNotNull(assertion + " [DRAFT ARTIFACT STATE IS NULL]", draft.getState(artifact));
        }
        assertNotNull(assertion + " [DRAFT OWNER IS NULL]", draft.getOwner());
    }

    /**
     * Assert the containers are not null.
     * 
     * @param assertion
     *            An assertion.
     * @param containers
     *            A list of containers.
     */
    protected static void assertNotNull(final String assertion, final List<Container> containers) {
        assertNotNull(assertion + " [CONTAINERS IS NULL]", (Object) containers);
        for(final Container container : containers) {
            assertNotNull(assertion, container);
        }
    }

    /**
     * Create ContainerTestCase.
     * 
     * @param name
     *            The test name.
     */
    protected ContainerTestCase(final String name) {
        super(name);
    }

    /**
     * Obtain an output directory for a container test case. This will locate
     * the output directory in a per-test director beneath a common container
     * root.
     * 
     * @return A directory <code>File</code>.
     */
    @Override
    public File getOutputDirectory() {
        final File parentFile = new File(super.getOutputDirectory(), "Container");
        final File outputDirectory = new File(parentFile, getName());
        if (!outputDirectory.exists())
            assertTrue(outputDirectory.mkdirs());
        return outputDirectory;
    }

    /**
     * @see junit.framework.TestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <b>Title:</b>thinkParity Container Test Fixture<br>
     * <b>Description:</b>A thinkParity container test fixture provides a way
     * to abstract the container listener events to a central location. If any
     * events are fired in the abstraction the test case will fail.
     * 
     */
    protected abstract class Fixture extends ModelTestCase.Fixture implements
            ContainerListener {
        public void containerArchived(ContainerEvent e) {
            fail(getName() + " - Container archived event was fired.");
        }
        public void containerClosed(ContainerEvent e) {
            fail(getName() + " - Container closed event was fired.");
        }
        public void containerCreated(ContainerEvent e) {
            fail(getName() + " - Container created event was fired.");
        }
        public void containerDeleted(ContainerEvent e) {
            fail(getName() + " - Container deleted event was fired.");
        }
        public void containerFlagged(final ContainerEvent e) {
            fail(getName() + " - Container flagged event was fired.");
        }
        public void containerPublished(ContainerEvent e) {
            fail(getName() + " - Draft published event was fired.");
        }
        public void containerReceived(final ContainerEvent e) {
            fail(getName() + " - Container received event was fired.");
        }
        public void containerRenamed(ContainerEvent e) {
            fail(getName() + " - Container renamed event was fired.");
        }
        public void containerRestored(ContainerEvent e) {
            fail(getName() + " - Container restored event was fired.");
        }
        public void containerShared(ContainerEvent e) {
            fail(getName() + " - Container shared event was fired.");
        }
        public void containerUpdated(ContainerEvent e) {
            fail(getName() + " - Container updated event was fired.");
        }
        public void documentAdded(ContainerEvent e) {
            fail(getName() + " - Document added event was fired.");
        }
        public void documentRemoved(ContainerEvent e) {
            fail(getName() + " - Document removed event was fired.");
        }
        public void documentReverted(ContainerEvent e) {
            fail(getName() + " - Document reverted event was fired.");
        }
        public void draftCreated(ContainerEvent e) {
            fail(getName() + " - Draft created event was fired.");
        }
        public void draftDeleted(ContainerEvent e) {
            fail(getName() + " - Draft deleted event was fired.");
        }
        public void draftUpdated(ContainerEvent e) {
            fail(getName() + " - Draft updated event was fired.");
        }
        public void teamMemberAdded(ContainerEvent e) {
            fail(getName() + " - Team member added event was fired.");
        }
        public void teamMemberRemoved(ContainerEvent e) {
            fail(getName() + " - Team member removed event was fired.");
        }
    }
}
