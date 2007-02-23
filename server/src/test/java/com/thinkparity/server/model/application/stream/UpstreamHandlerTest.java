/*
 * Created On: Sun Oct 22 2006 11:00 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.DesdemonaTestUser;

/**
 * <b>Title:</b>thinkParity Upstream Handler Test<br>
 * <b>Description:</b>Test the streaming server's upstream handler.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UpstreamHandlerTest extends StreamTestCase {

    /** The test name <code>Fixture</code>. */
    private static final String NAME = "Upstream Handler Test";

    /** The test datum <code>Fixture</code>. */
    private Fixture datum;

    /**
     * Create UpstreamHandlerTest.
     *
     */
    public UpstreamHandlerTest() {
        super(NAME);
    }

    /**
     * Test the upstream handler.
     *
     */
    public void testUpstreamHandler() {
        datum.handler.run();
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final File workingDirectory = new File(getOutputDirectory(), "working");
        Assert.assertTrue(workingDirectory.mkdir(), "Could not create directory {0}.", workingDirectory);

        final File streamFile = getInputFiles()[0];
        final StreamServer server = startStreamServer(DesdemonaTestUser.JUNIT, workingDirectory);
        final StreamSession session = createSession(DesdemonaTestUser.JUNIT, server);
        final String streamId = createStream(server, session, streamFile.getName());

        final File streamIdFile = new File(workingDirectory, streamId + "-complete");
        datum = new Fixture(server, new UpstreamHandler(server, session, streamId, 0L,
                streamFile.length(), new FileInputStream(streamFile), new FileOutputStream(streamIdFile)));
    }

    /**
     * @see com.thinkparity.desdemona.model.stream.StreamTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        datum.streamServer.stop(Boolean.TRUE);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b>thinkParity Upstream Handler Test Fixture<br>
     * <b>Description:</b>The test datum fixture.<br>
     */
    private final class Fixture extends StreamTestCase.Fixture {
        private final UpstreamHandler handler;
        private final StreamServer streamServer;
        private Fixture(final StreamServer streamServer,
                final UpstreamHandler handler) {
            super();
            this.streamServer = streamServer;
            this.handler = handler;
        }
    }
}
