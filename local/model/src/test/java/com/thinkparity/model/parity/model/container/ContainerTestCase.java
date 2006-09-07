/*
 * Created On: Jun 27, 2006 4:01:02 PM
 */
package com.thinkparity.model.parity.model.container;


import java.util.List;

import com.thinkparity.model.artifact.Artifact;
import com.thinkparity.model.container.Container;
import com.thinkparity.model.parity.api.events.ContainerEvent;
import com.thinkparity.model.parity.api.events.ContainerListener;
import com.thinkparity.model.parity.model.ModelTestCase;

/**
 * <b>Title:</b>thinkParity Container Test Abstraction<br>
 * <b>Description:</b>A thinkParity container test abstraction.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
abstract class ContainerTestCase extends ModelTestCase {

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
    protected ContainerTestCase(final String name) { super(name); }

    /**
     * @see junit.framework.TestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {}

    /**
     * @see junit.framework.TestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {}

    /**
     * <b>Title:</b>thinkParity Container Test Fixture<br>
     * <b>Description:</b>A thinkParity container test fixture provides a way
     * to abstract the container listener events to a central location. If any
     * events are fired in the abstraction the test case will fail.
     * 
     */
    protected abstract class Fixture implements ContainerListener {
        public void containerClosed(ContainerEvent e) {
            fail(getName() + " [CONTAINER CLOSED EVENT FIRED]");
        }
        public void containerCreated(ContainerEvent e) {
            fail(getName() + " [CONTAINER CREATED EVENT FIRED]");
        }
        public void containerDeleted(ContainerEvent e) {
            fail(getName() + " [CONTAINER DELETED EVENT FIRED]");
        }
        public void containerReactivated(ContainerEvent e) {
            fail(getName() + " [CONTAINER REACTIVATED EVENT FIRED]");
        }
        public void containerShared(ContainerEvent e) {
            fail(getName() + " CONTAINER SHARED EVENT FIRED");
        }
        public void containerUpdated(ContainerEvent e) {
            fail(getName() + " [CONTAINER UPDATED EVENT FIRED]");
        }
        public void documentAdded(ContainerEvent e) {
            fail(getName() + " [DOCUMENT ADDED EVENT FIRED]");
        }
        public void documentRemoved(ContainerEvent e) {
            fail(getName() + " [DOCUMENT REMOVED EVENT FIRED]");
        }
        public void draftCreated(ContainerEvent e) {
            fail(getName() + " [DRAFT CREATED EVENT FIRED]");
        }
        public void draftDeleted(ContainerEvent e) {
            fail(getName() + " [CONTAINER DRAFT DELETED EVENT FIRED]");
        }
        public void draftPublished(ContainerEvent e) {
            fail(getName() + " [DRAFT PUBLISHED EVENT FIRED]");
        }
        public void teamMemberAdded(ContainerEvent e) {
            fail(getName() + " [TEAM MEMBER ADDED EVENT FIRED]");
        }
        public void teamMemberRemoved(ContainerEvent e) {
            fail(getName() + " [TEAM MEMBER REMOVED EVENT FIRED]");
        }
    }
}
