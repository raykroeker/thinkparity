/*
 * Created On: Aug 2, 2006 9:17:00 AM
 */
package com.thinkparity.model.handler.artifact;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.jivesoftware.messenger.handler.IQHandler;

import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.artifact.CreateDraft;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class CreateDraftTest extends ArtifactHandlerTestCase {

    private static final String NAME = "[HANDLER] [CREATE DRAFT TEST]";

    /** Test datum. */
    private Fixture datum;

    /** Create CreateDraftTest. */
    public CreateDraftTest() { super(NAME); }

    /**
     * Test the service api for creating a draft.
     *
     */
    public void testCreatDraft() {
        IQ iqResponse = null;
        try { iqResponse = datum.iqHandler.handleIQ(datum.iq); }
        catch(final UnauthorizedException ux) {
            fail(createFailMessage(NAME + " [UNAUTHORIZED]", ux));
        }

        assertNotNull(NAME + " [RESPONSE IS NULL]", iqResponse);
        if(didFail(iqResponse)) {
            fail(createFailMessage(NAME  + " [RESPONSE FAILURE]", iqResponse));
        }
    }

    /**
     * @see com.thinkparity.model.handler.artifact.ArtifactHandlerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(handlerDatum.iq, new CreateDraft());
    }

    /**
     * @see com.thinkparity.model.handler.artifact.ArtifactHandlerTestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /** Test datum definition. */
    private class Fixture {
        private final IQ iq;
        private final IQHandler iqHandler;
        private Fixture(final IQ iq, final IQHandler iqHandler) {
            this.iq = iq;
            this.iqHandler = iqHandler;
        }
    }
}
