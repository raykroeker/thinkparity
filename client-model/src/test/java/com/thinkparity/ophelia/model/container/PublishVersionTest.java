/*
 * Created On: Oct 20, 2006 08:30
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.user.UserUtils;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class PublishVersionTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Publish Version Test";

    /** Test datum. */
    private Fixture datum;

    /** Create PublishVersionTest. */
    public PublishVersionTest() { super(NAME); }

    /** Test the publish version api. */
    public void testPublishVersion() {
        datum.containerModel.publishVersion(
                datum.container.getId(), datum.version.getVersionId(), datum.contacts);
        assertTrue("The draft published event was not fired.", datum.didNotify);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        login(OpheliaTestUser.JUNIT);
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        addDocuments(OpheliaTestUser.JUNIT, container);
        publishToContacts(OpheliaTestUser.JUNIT, container.getId(), OpheliaTestUser.JUNIT_X.getName());
        final List<Contact> contacts = readContacts(OpheliaTestUser.JUNIT);
        UserUtils.getInstance().remove(contacts, OpheliaTestUser.JUNIT_X);
        final ContainerVersion version = containerModel.readLatestVersion(container.getId());
        datum = new Fixture(contacts, container, containerModel, version);
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
        super.tearDown();
    }

    /** Test datumn fixture. */
    private class Fixture extends ContainerTestCase.Fixture {
        private final List<Contact> contacts;
        private final Container container;
        private final ContainerModel containerModel;
        private Boolean didNotify;
        private final ContainerVersion version;
        private Fixture(final List<Contact> contacts,
                final Container container, final ContainerModel containerModel,
                final ContainerVersion version) {
            this.contacts = contacts;
            this.container = container;
            this.containerModel = containerModel;
            this.didNotify = Boolean.FALSE;
            this.version = version;
        }
        @Override
        public void draftPublished(ContainerEvent e) {
            didNotify = Boolean.TRUE;
        }
    }
}
