/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class PublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Publish Test";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishTest. */
    public PublishTest() { super(NAME); }

    /** Test the publish api. */
    public void testPublish() {
        logger.logTraceId();
        final Container c = createContainer(datum.junit, NAME);
        final List<Document> documents = addDocuments(datum.junit, c.getId());
        getContainerModel(datum.junit).addListener(datum);
        publishToContacts(datum.junit, c.getId(), "JUnit.X thinkParity", "JUnit.Y thinkParity");
        getContainerModel(datum.junit).addListener(datum);
        datum.waitForEvents();
        final Container c_x = readContainer(datum.junit_x, c.getUniqueId());
        logger.logVariable("c_x", c_x);
        assertNotNull("Container for user \"" + datum.junit_x.getSimpleUsername() + "\" is null.", c_x);
        final Container c_y = readContainer(datum.junit_y, c.getUniqueId());
        logger.logVariable("c_y", c_y);
        assertNotNull("Container for user \"" + datum.junit_y.getSimpleUsername() + "\" is null.", c_y);

        final ContainerDraft draft = readContainerDraft(datum.junit, c.getId());
        assertNull("Draft for container " + c.getName() + " for user " + datum.junit.getSimpleUsername() + " is not null.", draft);
        final ContainerDraft draft_x = readContainerDraft(datum.junit_x, c_x.getId());
        assertNull("Draft for container " + c_x.getName() + " for user " + datum.junit_y.getSimpleUsername() + " is not null.", draft_x);
        final ContainerDraft draft_y = readContainerDraft(datum.junit_y, c_x.getId());
        assertNull("Draft for container " + c_y.getName() + " for user " + datum.junit_y.getSimpleUsername() + " is not null.", draft_y);

        assertTrue("The draft published event was not fired.", datum.didNotify);
        final JabberId keyHolder = getArtifactModel(datum.junit).readKeyHolder(c.getId());
        assertEquals("Local artifact key holder does not match expectation.",
                User.THINK_PARITY.getId(), keyHolder);
        assertTrue("Local key flag is still mistakenly applied.",
                !getArtifactModel(datum.junit).isFlagApplied(c.getId(), ArtifactFlag.KEY));
        InternalDocumentModel documentModel;
        Document d_other;
        for (final Document d : documents) {
            documentModel = getDocumentModel(datum.junit);
            assertFalse("Document \"" + d.getName() + "\" for user \"" + datum.junit.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d.getId()));

            documentModel = getDocumentModel(datum.junit_x);
            d_other = readDocument(datum.junit_x, d.getUniqueId());
            assertFalse("Document \"" + d_other.getName() + "\" for user \"" + datum.junit_x.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d_other.getId()));

            documentModel = getDocumentModel(datum.junit_y);
            d_other = readDocument(datum.junit_y, d.getUniqueId());
            assertFalse("Document \"" + d_other.getName() + "\" for user \"" + datum.junit_y.getSimpleUsername() + "\" still maintains draft.",
                    documentModel.doesExistDraft(d_other.getId()));
        }
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
        private Boolean didNotify;
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private final OpheliaTestUser junit_y;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x, final OpheliaTestUser junit_y) {
            this.junit = junit;
            this.junit_x = junit_x;
            this.junit_y = junit_y;
            this.didNotify = Boolean.FALSE;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
            addQueueHelper(junit_y);
        }
        @Override
        public void draftPublished(ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
    }
}
