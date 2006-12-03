/*
 * Created On: Jul 3, 2006 2:05:41 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.ophelia.OpheliaTestUser;
import com.thinkparity.ophelia.model.events.ContainerEvent;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ReceivePublishTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "TEST PUBLISH";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishTest. */
    public ReceivePublishTest() { super(NAME); }

    /** Test the publish api. */
    public void testReceivePublish() {
        datum.containerModel.publish(datum.container.getId(), datum.contacts, datum.teamMembers);
        assertTrue("Did not fire publish event.", datum.didNotify);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        login(OpheliaTestUser.JUNIT_X);
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container.getId());
        final List<Contact> contacts = new ArrayList<Contact>(1);
        contacts.add(readContact(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X));
        datum = new Fixture(contacts, container, containerModel);
        containerModel.addListener(datum);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        datum.containerModel.removeListener(datum);
        datum = null;
        logout(OpheliaTestUser.JUNIT);
        logout(OpheliaTestUser.JUNIT_X);
        super.tearDown();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final List<Contact> contacts;
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private final List<TeamMember> teamMembers;
        private Fixture(final List<Contact> contacts,
                final Container container, final ContainerModel containerModel) {
            this.contacts = contacts;
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.teamMembers = Collections.emptyList();
        }
        @Override
        public void draftPublished(ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
    }
}
