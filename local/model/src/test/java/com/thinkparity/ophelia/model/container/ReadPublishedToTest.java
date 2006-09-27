/*
 * Created On: Sep 26, 2006 8:52:08 PM
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.OpheliaTestUser;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadPublishedToTest extends ContainerTestCase {

    /** Test name. */
    private static final String NAME = "Read Published To Test";

    private Fixture datum;

    /** Create ReadPublishedToTest. */
    public ReadPublishedToTest() {
        super(NAME);
    }

    public void testReadPublishedTo() {
        final List<User> publishedTo = datum.containerModel.readPublishedTo(
                datum.container.getId(), datum.version.getVersionId());
        assertNotNull(NAME + " - Published to user list is null.", publishedTo);
        assertTrue(NAME + " - Published to list does not contain " + OpheliaTestUser.JUNIT_X,
                publishedTo.contains(OpheliaTestUser.JUNIT_X));
        assertTrue(NAME + " - Published to list does not contain " + OpheliaTestUser.JUNIT_Y,
                publishedTo.contains(OpheliaTestUser.JUNIT_Y));
        assertTrue(NAME + " - Published to list does not contain " + OpheliaTestUser.JUNIT_Z,
                publishedTo.contains(OpheliaTestUser.JUNIT_Z));
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
        publish(OpheliaTestUser.JUNIT, container);
        final ContainerVersion version = containerModel.readLatestVersion(container.getId());
        datum = new Fixture(container, containerModel, version);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        logout(OpheliaTestUser.JUNIT);
        datum = null;
        super.tearDown();
    }

    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private final ContainerVersion version;
        private Fixture(final Container container,
                final ContainerModel containerModel,
                final ContainerVersion version) {
            super();
            this.container = container;
            this.containerModel = containerModel;
            this.version = version;
        }
    }
}
