/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.container;

import java.util.List;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.xmpp.user.User;

/**
 * Test the parity container interface close api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CloseTest extends ContainerTestCase {

    /** The test name. */
    private static final String NAME = "[LMODEL] [CONTAINER] [CLOSE TEST]";

    /** The test data. */
    private Fixture datum;

    /** Create CloseTest. */
    public CloseTest() {
        super(NAME);
    }

    /** Test the parity container interface close api. */
    public void testClose() {
        try {
            datum.cModel.close(datum.containerId);
        }
        catch(final ParityException px) {
            fail(createFailMessage(px));
        }

        // assert the container remains intact
        final Container container = datum.cModel.read(datum.containerId);
        assertNotNull(NAME, container);
        assertEquals(NAME + " [CONTAINER STATE DOES NOT MATCH EXPECTATION]",
                container.getState(), ArtifactState.CLOSED);

        // the local team remains intact
        final List<User> team = datum.cModel.readTeam(datum.containerId);
        assertNotNull(NAME + " [TEAM IS NULL]", team);
        assertEquals(NAME + " [TEAM SIZE DOES NOT MATCH EXPECTATION]",
                datum.eTeamSize.intValue(), team.size());
    }

    /**
     * @see com.thinkparity.model.parity.model.document.DocumentTestCase#setUp()
     * 
     */
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel cModel = getContainerModel();

        login();
        final Container container = createContainer(NAME);
        final Document document = create(getInputFiles()[0]);
        cModel.addDocument(container.getId(), document.getId());
        addTeam(container);
        modifyDocument(document);
        cModel.publish(container.getId());

        datum = new Fixture(cModel, container.getId(), cModel.readTeam(
                container.getId()).size());
    }

    /**
     * @see com.thinkparity.model.parity.model.document.DocumentTestCase#tearDown()
     * 
     */
    protected void tearDown() throws Exception {
        datum = null;
        logout();
        super.tearDown();
    }

    private class Fixture {
        private final ContainerModel cModel;

        private final Long containerId;

        private final Integer eTeamSize;

        private Fixture(final ContainerModel cModel, final Long containerId,
                final Integer eTeamSize) {
            this.cModel = cModel;
            this.containerId = containerId;
            this.eTeamSize = eTeamSize;
        }
    }
}
