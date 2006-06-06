/*
 * Created On: Tue Jun 06 2006 10:34 PDT
 * $Id$
 */
package com.thinkparity.server.model.artifact;

import java.util.UUID;

import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.ModelTestCase;

/**
 * Test the artifact model's close api.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class CloseTest extends ModelTestCase {

    /** The test name. */
    private static final String NAME = "[RMODEL] [ARTIFACT MODEL] [CLOSE TEST]";

    /** Test data. */
    private Fixture datum;

    /** Create CloseTest. */
    public CloseTest() { super(NAME); }

    /** Test the artifact model's close api.*/
//    public void testClose() {
//        try { datum.aModel.close(datum.uniqueId); }
//        catch(final ParityServerModelException psmx) { fail(createFailMessage(psmx)); }
//    }

    /** @see com.thinkparity.server.model.ModelTestCase#setUp() */
    protected void setUp() throws Exception {
        super.setUp();

        final ArtifactModel aModel = getArtifactModel();
        final Artifact eArtifact = createArtifact();


        datum = new Fixture(aModel, eArtifact.getArtifactUUID());
    }

    /** @see com.thinkparity.server.model.ModelTestCase#tearDown() */
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    /** Test data definition. */
    private class Fixture {
        private final ArtifactModel aModel;
        private final UUID uniqueId;
        private Fixture(final ArtifactModel aModel, final UUID uniqueId) {
            this.aModel = aModel;
            this.uniqueId = uniqueId;
        }
    }
}
