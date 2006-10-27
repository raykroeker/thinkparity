/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.DesdemonaTestUser;

public final class UpstreamHandlerTest extends StreamTestCase {
    private static final String NAME = "Upstream Handler Test";
    private Fixture datum;
    public UpstreamHandlerTest() {
        super(NAME);
    }
    public void testRun() {
        datum.handler.run();
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);

        final File streamFile = getInputFiles()[0];
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final StreamSession session = createSession(DesdemonaTestUser.JUNIT, server);
        final String streamId = createStream(server, session, streamFile.getName());

        datum = new Fixture(new UpstreamHandler(server, session, streamId, new FileInputStream(streamFile)));
    }
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }
    private final class Fixture extends StreamTestCase.Fixture {
        private final UpstreamHandler handler;
        private Fixture(final UpstreamHandler handler) {
            super();
            this.handler = handler;
        }
    }
}
