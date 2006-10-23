/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.stream.Direction;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class StreamServerTest extends StreamTestCase {
    private static final String NAME = "Stream Server Test";
    private Fixture datum;
    public StreamServerTest() {
        super(NAME);
    }
    public void testCreateUploadSession() {
        final String sessionId = datum.streamServer.createSession(Direction.UPSTREAM);
        assertNotNull("Stream session id is null.", sessionId);
        
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);
        final StreamServer streamServer = createStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        datum = new Fixture(streamServer);
    }
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }
    private final class Fixture extends StreamTestCase.Fixture {
        private final StreamServer streamServer;
        private Fixture(final StreamServer streamServer) {
            super();
            this.streamServer = streamServer;
        }
    }
}
